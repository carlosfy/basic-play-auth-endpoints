name         := "reachfive-training"
organization := "co.reachfive"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.16"

libraryDependencies ++= List(
  "com.softwaremill.macwire" %% "macros"             % "2.5.8",
  "org.postgresql"            % "postgresql"         % "42.5.0",
  "org.tpolecat"              % "doobie-core_2.12"   % "1.0.0-RC2",
  "org.liquibase"             % "liquibase-core"     % "3.10.3",
  "org.scalatestplus.play"   %% "scalatestplus-play" % "5.0.0" % Test,
)
