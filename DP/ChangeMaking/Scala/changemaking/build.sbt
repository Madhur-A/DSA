import Dependencies._

ThisBuild / scalaVersion     := "2.13.12"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organizationName := "Dynamic-Prgramming"

scalacOptions += "-deprecation"

lazy val root = (project in file("."))
  .settings(
    name := "ChangeMaking",
    libraryDependencies += munit % Test
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.

// sbt -J-Xmx2G -J-XX:+UseG1GC
// sbt -J-Xmx4G -J-XX:+UseG1GC "~runMain ChangeMaking"

