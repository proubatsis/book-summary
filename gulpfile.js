const gulp = require("gulp");
const connect = require("gulp-connect");
const mustache = require("gulp-mustache");
const tap = require("gulp-tap");
const rename = require("gulp-rename");
const sass = require("gulp-sass");
const browserify = require("gulp-browserify");
const path = require("path");

const CLIENT_DEST = "web/assets";

gulp.task("templates", function() {
    return gulp.src("src/main/resources/templates/*.mustache")
        .pipe(tap(function(file, t) {
            const name = path.basename(file.path, ".mustache");
            const jsonFile = `client/dev-data/${name}.json`;
            return t.through(mustache, [jsonFile, {}, {}]);
        }))
        .pipe(rename(function(p) {
            p.extname = ".html";
        }))
        .pipe(gulp.dest(CLIENT_DEST))
        .pipe(connect.reload());
});

gulp.task("sass", function() {
    return gulp.src("client/sass/*.scss")
        .pipe(sass())
        .pipe(gulp.dest(path.join(CLIENT_DEST, "styles")))
        .pipe(connect.reload());
});

gulp.task("bootstrap", function() {
    return gulp.src("node_modules/bootstrap/dist/css/bootstrap.min.css")
        .pipe(gulp.dest(path.join(CLIENT_DEST, "styles")));
});

gulp.task("scripts", function() {
    return gulp.src("client/scripts/*.js")
        .pipe(browserify())
        .pipe(gulp.dest(path.join(CLIENT_DEST, "scripts")));
});

gulp.task("dev", ["templates", "sass", "bootstrap", "scripts"], function() {
    connect.server({
        root: CLIENT_DEST,
        livereload: true
    });

    gulp.watch(["src/main/resources/templates/*.mustache", "client/dev-data/*.json"], ["templates"]);
    gulp.watch("client/sass/*.scss", ["sass"]);
    gulp.watch("client/scripts/*.js", ["scripts"])
});

gulp.task("default", ["sass", "bootstrap", "scripts"]);
