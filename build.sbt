import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneOffset}

val scala3Version = "3.3.0"

lazy val root = project
  .in(file("."))
  .settings(
    organization := "ie.nok",
    name := "scala-libraries",
    version := DateTimeFormatter
      .ofPattern("yyyyMMdd.HHmmss.n")
      .withZone(ZoneOffset.UTC)
      .format(Instant.now()),
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "com.google.firebase" % "firebase-admin" % "9.1.1",
      "dev.zio" %% "zio" % "2.0.15",
      "dev.zio" %% "zio-http" % "0.0.4",
      "dev.zio" %% "zio-json" % "0.5.0",
      "org.jsoup" % "jsoup" % "1.15.3",
      "org.scalameta" %% "munit" % "0.7.29" % Test
    ),
    githubOwner := "nok-ie",
    githubRepository := "scala-libraries"
  )
