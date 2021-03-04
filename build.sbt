lazy val baseName       = "Serial"
lazy val baseNameL      = baseName.toLowerCase

lazy val projectVersion = "2.0.1"
lazy val mimaVersion    = "2.0.0"

lazy val deps = new {
  val test = new {
    val scalaTest = "3.2.3"
  }
}

lazy val commonJvmSettings = Seq(
  crossScalaVersions := Seq("3.0.0-RC1", "2.13.4", "2.12.13"),
)

// sonatype plugin requires that these are in global
ThisBuild / version      := projectVersion
ThisBuild / organization := "de.sciss"

lazy val commonSettings = Seq(
  name               := baseName,
//  version            := projectVersion,
//  organization       := "de.sciss",
  description        := "Simple binary serialization library for Scala",
  homepage           := Some(url(s"https://git.iem.at/sciss/${name.value}")),
  licenses           := Seq("LGPL v2.1+" -> url( "http://www.gnu.org/licenses/lgpl-2.1.txt")),
  scalaVersion       := "2.13.4",
  mimaPreviousArtifacts := Set("de.sciss" %% baseNameL % mimaVersion),
  libraryDependencies ++= {
    if (scalaVersion.value.startsWith("3")) Nil else Seq(
      "org.scalatest" %%% "scalatest" % deps.test.scalaTest % Test,
    )
  },
  scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-encoding", "utf8"),
  scalacOptions ++= {
    if (scalaVersion.value.startsWith("3")) Nil else Seq("-Xlint", "-Xsource:2.13")
  },
  scalacOptions in (Compile, compile) ++= {
    val dot = scalaVersion.value.startsWith("3")
    if (!dot && scala.util.Properties.isJavaAtLeast("9")) {
      Seq("-release", "8")   // JDK >8 breaks API; skip scala-doc
    } else {
      Nil
    }
  },
  testOptions in Test += Tests.Argument("-oDF"),   // ScalaTest: durations and full stack traces
  parallelExecution in Test := false,
  unmanagedSourceDirectories in Test := {
    if (scalaVersion.value.startsWith("3")) Nil else (unmanagedSourceDirectories in Test).value  // while ScalaTest is unavailable
  },
)

// ---- publishing ----

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  developers := List(
    Developer(
      id    = "sciss",
      name  = "Hanns Holger Rutz",
      email = "contact@sciss.de",
      url   = url("https://www.sciss.de")
    )
  ),
  scmInfo := {
    val h = "git.iem.at"
    val a = s"sciss/${name.value}"
    Some(ScmInfo(url(s"https://$h/$a"), s"scm:git@$h:$a.git"))
  },
)

lazy val root = crossProject(JSPlatform, JVMPlatform).in(file("."))
  .settings(commonSettings)
  .jvmSettings(commonJvmSettings)
  .settings(publishSettings)

