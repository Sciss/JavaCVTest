name         := "JavaCVTest"
version      := "0.1.0-SNAPSHOT"
description  := "Small test project for trying out JavaCV"
organization := "de.sciss"
homepage     := Some(url(s"https://github.com/Sciss/${name.value}"))
licenses     := Seq("gpl v2+" -> url("http://www.gnu.org/licenses/gpl-2.0.txt"))
scalaVersion := "2.11.8"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Xfuture", "-encoding", "utf8")

libraryDependencies ++= Seq(
  "de.sciss" %% "fileutil" % "1.1.1"
)

javaCppPresetLibs ++= Seq(
  "ffmpeg" -> "2.8.1"
)