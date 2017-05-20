name := "books-summary-website"

version := "1.0"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "com.twitter" % "finatra-http_2.11" % "2.8.0",
  "org.slf4j" % "slf4j-simple" % "1.7.25",
  "io.getquill" % "quill_2.11" % "1.1.0",
  "io.getquill" % "quill-finagle-postgres_2.11" % "1.1.0",
  "com.pauldijou" %% "jwt-core" % "0.12.1"
)

