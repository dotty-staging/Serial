lazy val baseName       = "Serial"
lazy val baseNameL      = baseName.toLowerCase

lazy val projectVersion = "1.1.0"
lazy val mimaVersion    = "1.1.0"

name               := baseName
version            := projectVersion
organization       := "de.sciss"
description        := "Extension of Scala-STM, adding optional durability layer, and providing API for confluent and reactive event layers"
homepage           := Some(url(s"https://github.com/Sciss/${name.value}"))
licenses           := Seq("LGPL v2.1+" -> url( "http://www.gnu.org/licenses/lgpl-2.1.txt"))
scalaVersion       := "2.12.5"
crossScalaVersions := Seq("2.12.5", "2.11.12")

mimaPreviousArtifacts := Set("de.sciss" %% baseNameL % mimaVersion)

libraryDependencies +=
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Xfuture", "-encoding", "utf8", "-Xlint")

scalacOptions ++= Seq("-Xelide-below", "INFO")     // elide debug logging!

testOptions in Test += Tests.Argument("-oDF")   // ScalaTest: durations and full stack traces

parallelExecution in Test := false

// ---- test ----

/*
testListeners += new TestReportListener {
  def endGroup(name: String, result: TestResult.Value): Unit = println(s"End Group $name (succeeded)")
  def endGroup(name: String, t: Throwable): Unit = println(s"End Group $name (failed)")
  def startGroup(name: String): Unit = println(s"Start Group $name")
  def testEvent(event: TestEvent): Unit = println(s"Test Event: ${event.result}")
}
*/

// ---- publishing ----

publishMavenStyle := true

publishTo :=
  Some(if (isSnapshot.value)
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  else
    "Sonatype Releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
  )

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := { val n = name.value
<scm>
  <url>git@github.com:Sciss/{n}.git</url>
  <connection>scm:git:git@github.com:Sciss/{n}.git</connection>
</scm>
<developers>
  <developer>
    <id>sciss</id>
    <name>Hanns Holger Rutz</name>
    <url>http://www.sciss.de</url>
  </developer>
</developers>
}
