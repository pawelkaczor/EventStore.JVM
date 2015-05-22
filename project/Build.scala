import sbt._
import Keys._
import sbtrelease.ReleasePlugin._
import sbtprotobuf.ProtobufPlugin.protobufSettings
import scoverage.ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages

object Build extends Build {
  lazy val basicSettings = Seq(
    name                 := "eventstore-client",
    organization         := "pl.newicom.dddd",
    scalaVersion         := "2.11.6",
    licenses             := Seq("BSD 3-Clause" -> url("http://raw.github.com/EventStore/EventStore.JVM/master/LICENSE")),
    homepage             := Some(new URL("http://github.com/pawelkaczor/EventStore.JVM")),
    description          := "Event Store JVM Client",
    startYear            := Some(2013),
    scalacOptions        := Seq("-encoding", "UTF-8", "-unchecked", "-deprecation", "-feature", "-Xlint"),
    resolvers            ++= Seq(
      "spray" at "http://repo.spray.io/",
      "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"),
    libraryDependencies  ++= Seq(
      Akka.actor, Akka.testkit, protobuf, typesafeConfig, codec, Joda.time, Joda.convert,
      Specs2.core, Specs2.mock, mockito, Spray.json, Spray.client)
  )

  object Akka {
    val actor   = apply("actor")
    val testkit = apply("testkit") % "test"

    private def apply(x: String) = "com.typesafe.akka" %% s"akka-$x" % "2.4-M1"
  }

  object Specs2 {
    val core = apply("core")
    val mock = apply("mock")

    private def apply(x: String) = "org.specs2" %% s"specs2-$x" % "2.4.15" % "test"
  }

  object Spray {
    val client = "io.spray" %% "spray-client" % "1.3.2"
    val json   = "io.spray" %% "spray-json" % "1.3.1"
  }

  val typesafeConfig = "com.typesafe" % "config" % "1.2.1"
  val codec          = "org.apache.directory.studio" % "org.apache.commons.codec" % "1.8"
  val protobuf       = "com.google.protobuf" % "protobuf-java" % "2.5.0"
  val jodaTime       = "joda-time" % "joda-time" % "2.7"
  val mockito        = "org.mockito" % "mockito-all" % "1.9.5" % "test"

  object Joda {
    val time    = "joda-time" % "joda-time" % "2.7"
    val convert = "org.joda" % "joda-convert" % "1.7"
  }

  lazy val IntegrationTest = config("it") extend Test
  lazy val ClusterTest = config("c") extend Test

  lazy val root = Project(
    "eventstore-client",
    file("."),
    settings = basicSettings ++ Defaults.coreDefaultSettings ++ releaseSettings ++ protobufSettings ++ Scalariform.settings ++ Publish.settings)
    .configs(IntegrationTest, ClusterTest)
    .settings(inConfig(IntegrationTest)(Defaults.testTasks): _*)
    .settings(inConfig(ClusterTest)(Defaults.testTasks): _*)
    .settings(
      coverageExcludedPackages :=
        "eventstore.examples;eventstore.j;" +
        "eventstore.proto;eventstore.pipeline;" +
        "eventstore.tcp.EventStoreProtoFormats;" +
        "eventstore.tcp.MarkerByte;" +
        "eventstore.util.ToCoarsest",
      testOptions in Test := Seq(Tests.Filter(_ endsWith "Spec")),
      testOptions in IntegrationTest := Seq(Tests.Filter(_ endsWith "ITest")),
      testOptions in ClusterTest := Seq(Tests.Filter(_ endsWith "CTest")),
      parallelExecution in IntegrationTest := false,
      parallelExecution in ClusterTest := false)
}