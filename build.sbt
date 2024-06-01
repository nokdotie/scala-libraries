import java.time.format.DateTimeFormatter
import java.time.{Instant, ZoneOffset}

val scala3Version = "3.3.3"

lazy val root = project
  .in(file("."))
  .settings(
    organization := "ie.nok",
    name         := "scala-libraries",
    version := DateTimeFormatter
      .ofPattern("yyyyMMdd.HHmmss.n")
      .withZone(ZoneOffset.UTC)
      .format(Instant.now()),
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "com.google.apis"     % "google-api-services-indexing" % "v3-rev20230927-2.0.0",
      "com.google.firebase" % "firebase-admin"               % "9.3.0",
      "dev.zio"            %% "zio"                          % "2.0.22",
      "dev.zio"            %% "zio-http"                     % "0.0.5",
      "dev.zio"            %% "zio-json"                     % "0.6.2",
      "dev.zio"            %% "zio-nio"                      % "2.0.2",
      "io.netty"            % "netty-all"                    % "4.1.110.Final",
      "org.jsoup"           % "jsoup"                        % "1.17.2",
      "org.scalameta"      %% "munit"                        % "1.0.0" % Test,
      "org.scalameta"      %% "munit-scalacheck"             % "1.0.0" % Test
    ),
    githubOwner            := "nokdotie",
    githubRepository       := "scala-libraries",
    Test / publishArtifact := true
  )
