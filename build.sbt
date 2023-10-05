name := "widget"

organization := "com.acmeco"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.13.8"

scalacOptions += "-deprecation"

enablePlugins(AkkaGrpcPlugin)

// Run in a separate JVM, to make sure sbt waits until all threads have
// finished before returning.
// If you want to keep the application running while executing other
// sbt tasks, consider https://github.com/spray/sbt-revolver/
fork := true

lazy val akkaVersion     = "2.8.4"
lazy val akkaHttpVersion = "10.5.2"
lazy val logbackVersion  = "1.3.1"
lazy val scalaTestVersion = "3.2.16"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"          % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http2-support" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor-typed"   % akkaVersion,
  "com.typesafe.akka" %% "akka-stream"        % akkaVersion,
  "com.typesafe.akka" %% "akka-discovery"     % akkaVersion,
  "com.typesafe.akka" %% "akka-pki"           % akkaVersion,

  // The Akka HTTP overwrites are required because Akka-gRPC depends on 10.1.x
  "com.typesafe.akka" %% "akka-http"          % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http2-support" % akkaHttpVersion,

  "ch.qos.logback" % "logback-classic" % logbackVersion,

  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion      % Test,
  "com.typesafe.akka" %% "akka-stream-testkit"      % akkaVersion      % Test,
  "org.scalatest"     %% "scalatest"                % scalaTestVersion % Test
)
