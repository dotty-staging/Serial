name := "Serial"

version      in ThisBuild := "1.1.0-SNAPSHOT"

organization in ThisBuild := "de.sciss"

description  in ThisBuild := "Extension of Scala-STM, adding optional durability layer, and providing API for confluent and reactive event layers"

homepage     in ThisBuild := Some(url("https://github.com/Sciss/" + name.value))

licenses     in ThisBuild := Seq("LGPL v2.1+" -> url( "http://www.gnu.org/licenses/lgpl-2.1.txt"))

scalaVersion in ThisBuild := "2.11.6"

crossScalaVersions in ThisBuild := Seq("2.11.6", "2.10.5")

libraryDependencies +=
  "org.scalatest" %% "scalatest" % "2.2.5" % "test"

scalacOptions in ThisBuild ++= Seq("-deprecation", "-unchecked", "-feature", "-Xfuture", "-encoding", "utf8")

scalacOptions in ThisBuild += "-no-specialization"  // never use this shit. will give you runtime IllegalAccessErrors in random places of the future. do _not_ use specialization. ever. don't diminish your life expectancy.

scalacOptions in ThisBuild ++= Seq("-Xelide-below", "INFO")     // elide debug logging!

testOptions in Test += Tests.Argument("-oDF")   // ScalaTest: durations and full stack traces

parallelExecution in Test := false

// ---- test ----

testListeners in ThisBuild += new TestReportListener {
  def endGroup(name: String, result: TestResult.Value): Unit = println(s"End Group $name (succeeded)")
  def endGroup(name: String, t: Throwable): Unit = println(s"End Group $name (failed)")
  def startGroup(name: String): Unit = println(s"Start Group $name")
  def testEvent(event: TestEvent): Unit = println(s"Test Event: ${event.result}")
}

// ---- publishing ----

publishMavenStyle in ThisBuild := true

publishTo in ThisBuild :=
  Some(if (isSnapshot.value)
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  else
    "Sonatype Releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
  )

publishArtifact in Test := false

pomIncludeRepository in ThisBuild := { _ => false }

pomExtra in ThisBuild := { val n = name.value
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

// ---- ls.implicit.ly ----

// (LsKeys.tags   in LsKeys.lsync) := Seq("stm", "software-transactional-memory", "persistent")
// (LsKeys.ghUser in LsKeys.lsync) := Some("Sciss")
// (LsKeys.ghRepo in LsKeys.lsync) := Some(name.value)
