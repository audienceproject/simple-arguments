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
    <name>${organization}:${name}</name>
    <description>A simple library for parsing command line arguments.</description>
    <url>https://github.com/audienceproject/simple-arguments</url>
    <licenses>
        <license>
            <name>MIT License</name>
            <url>http://www.opensource.org/licenses/mit-license.php</url>
        </license>
    </licenses>
    <scm>
        <url>git@github.com:audienceproject/simple-arguments.git</url>
        <connection>scm:git:git//github.com/audienceproject/simple-arguments.git</connection>
        <developerConnection>scm:git:ssh://github.com:audienceproject/simple-arguments.git</developerConnection>
    </scm>
    <developers>
        <developer>
            <id>audienceproject</id>
            <email>adtdev@audienceproject.com</email>
            <name>AudienceProject Dev</name>
            <organization>AudienceProject</organization>
            <organizationUrl>http://www.audienceproject.com</organizationUrl>
        </developer>
    </developers>)