/**
  * The groupId in Maven
  */
organization := "com.audienceproject"

/**
  * The artefactId in Maven
  */
name := "simple-arguments"

/**
  * The version must match "&#94;(\\d+\\.\\d+\\.\\d+)$" to be considered a release
  */
version := "1.0.3"
description := "A simple library fro parsing command line arguments."

scalaVersion := "2.11.8"

/**
  * Additional scala version supported.
  */
crossScalaVersions := Seq("2.10.6", "2.11.8")

/**
  * Fix for plugin sbt-testng-interface (wrong URL)
  */

resolvers += Resolver.url("fix-sbt-plugin-releases", url("https://dl.bintray.com/sbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

/**
  * Dependencies for the whole project
  */

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.5" % Test

/**
  * Maven specific settings for publishing to support Maven native projects
  */
publishMavenStyle := true
pomIncludeRepository := { _ => false }
pomExtra := (
  <url>N/A</url>
    <licenses>
        <license>
            <name>N/A</name>
        </license>
    </licenses>
    <scm>
        <url>git@bitbucket.org:AudienceReportTeam/internal-util-scala.git</url>
        <connection>scm:git@bitbucket.org:AudienceReportTeam/internal-util-scala.git</connection>
    </scm>
    <developers>
        <developer>
            <id>audienceproject</id>
            <name>AudienceProject Team</name>
            <url>http://www.audienceproject.com</url>
        </developer>
    </developers>)