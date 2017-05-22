// Run `sbt dependencyUpdates` if you want to see what dependencies can be updated
// Run `sbt dependencyGraph` if you want to see the dependencies

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
version := "1.0.1"
description := "A simple library for parsing command line arguments."

scalaVersion := "2.12.2"

/**
  * Additional scala version supported.
  */
crossScalaVersions := Seq("2.10.6", "2.11.11", "2.12.2")

/**
  * Fix for plugin sbt-testng-interface (wrong URL)
  */

resolvers += Resolver.url("fix-sbt-plugin-releases", url("https://dl.bintray.com/sbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

/**
  * Dependencies for the whole project
  */

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.3" % Test

assemblyJarName in assembly := name.value + ".jar"

/**
  * Maven specific settings for publishing to support Maven native projects
  */
publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := { _ => false }
publishTo <<= version { (v: String) =>
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
    else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
                      }
val publishSnapshot:Command = Command.command("publishSnapshot") { state =>
    val extracted = Project extract state
    import extracted._
    val currentVersion = getOpt(version).get
    val newState =
        Command.process(s"""set version := "$currentVersion-SNAPSHOT" """, state)
    val (s, _) = Project.extract(newState).runTask(PgpKeys.publishSigned in Compile, newState)
    state
                                                                 }
commands ++= Seq(publishSnapshot)
pomIncludeRepository := { _ => false }
pomExtra := (
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
    </developers>
  )