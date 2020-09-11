lazy val baseName       = "Serial"
lazy val baseNameL      = baseName.toLowerCase

lazy val projectVersion = "1.1.3"
lazy val mimaVersion    = "1.1.0"

lazy val commonSettings = Seq(
  name               := baseName,
  version            := projectVersion,
  organization       := "de.sciss",
  description        := "Extension of Scala-STM, adding optional durability layer, and providing API for confluent and reactive event layers",
  homepage           := Some(url(s"https://git.iem.at/sciss/${name.value}")),
  licenses           := Seq("LGPL v2.1+" -> url( "http://www.gnu.org/licenses/lgpl-2.1.txt")),
  scalaVersion       := "2.13.3",
  crossScalaVersions := Seq("0.27.0-RC1", "2.13.3", "2.12.12"),
  mimaPreviousArtifacts := Set("de.sciss" %% baseNameL % mimaVersion),
  libraryDependencies += {
    "org.scalatest" %% "scalatest" % "3.2.2" % Test
  },
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-encoding", "utf8", "-Xlint", "-Xsource:2.13"),
  scalacOptions in (Compile, compile) ++= {
    if (scala.util.Properties.isJavaAtLeast("9") && scalaVersion.value.startsWith("2.")) {
      Seq("-release", "8")   // JDK >8 breaks API; skip scala-doc
    } else {
      Nil
    }
  },
  testOptions in Test += Tests.Argument("-oDF"),   // ScalaTest: durations and full stack traces
  parallelExecution in Test := false
)

// ---- publishing ----

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishTo := {
    Some(if (isSnapshot.value)
      "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
    else
      "Sonatype Releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
    )
  },
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  pomExtra := { val n = name.value
<scm>
  <url>git@git.iem.at:sciss/{n}.git</url>
  <connection>scm:git:git@git.iem.at:sciss/{n}.git</connection>
</scm>
<developers>
  <developer>
    <id>sciss</id>
    <name>Hanns Holger Rutz</name>
    <url>http://www.sciss.de</url>
  </developer>
</developers>
}
)

lazy val root = project.in(file("."))
  .settings(commonSettings)
  .settings(publishSettings)

