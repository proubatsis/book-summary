const gulp = require("gulp");
const connect = require("gulp-connect");
const mustache = require("gulp-mustache");
const tap = require("gulp-tap");
const rename = require("gulp-rename");
const path = require("path");

const CLIENT_DEST = "client/dest";

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

gulp.task("dev", ["templates"], function() {
    connect.server({
        root: CLIENT_DEST,
        livereload: true
    });

    gulp.watch(["src/main/resources/templates/*.mustache", "client/dev-data/*.json"], ["templates"]);
});
