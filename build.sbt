name := "akka-assignment-02"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.11" % "2.4.17",
  "com.typesafe.akka" % "akka-testkit_2.11" % "2.4.17",
  "org.scalatest" %% "scalatest" % "3.0.1"
)

coverageExcludedPackages := "edu\\.knoldus\\.BookingPhone.*"
