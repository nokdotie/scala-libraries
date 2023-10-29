import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneOffset}

val scala3Version = "3.3.1"

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
      "ch.hsr" % "geohash" % "1.4.0",
      "com.google.firebase" % "firebase-admin" % "9.2.0",
      "dev.zio" %% "zio" % "2.0.18",
      "dev.zio" %% "zio-http" % "0.0.5",
      "dev.zio" %% "zio-json" % "0.6.2",
      "dev.zio" %% "zio-nio" % "2.0.2",
      "io.netty" % "netty-all" % "4.1.100.Final",
      "org.jsoup" % "jsoup" % "1.16.1",
      "org.scalameta" %% "munit" % "0.7.29" % Test,
      "org.scalameta" %% "munit-scalacheck" % "0.7.29" % Test
    ),
    githubOwner := "nok-ie",
    githubRepository := "scala-libraries",
    Test / publishArtifact := true
  )
