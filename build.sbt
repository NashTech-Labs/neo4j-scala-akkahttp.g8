name := """neo4j-akka-http-starter-kit"""

version := "1.0"

scalaVersion := "2.11.8"

organization := "com.knoldus"

val akkaV = "2.4.5"
libraryDependencies ++= Seq(
  "org.neo4j.driver" % "neo4j-java-driver" % "1.0.4",
  "io.spray" %%  "spray-json" % "1.3.3",
  "com.typesafe.akka" %% "akka-http-core" % akkaV,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaV % "test",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
  "org.scalatest"     %% "scalatest" % "2.2.6" % "test"
)