name         := "JavaCVTest"
version      := "0.1.0-SNAPSHOT"
description  := "Small test project for trying out JavaCV"
organization := "de.sciss"
homepage     := Some(url(s"https://github.com/Sciss/${name.value}"))
licenses     := Seq("gpl v2+" -> url("http://www.gnu.org/licenses/gpl-2.0.txt"))
scalaVersion := "2.11.8"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-Xfuture", "-encoding", "utf8", "-Xlint")

libraryDependencies ++= Seq(
  "de.sciss" %% "fileutil"  % "1.1.2",
  "de.sciss" %% "swingplus" % "0.2.1"
)

javaCppPresetLibs ++= Seq(
  "ffmpeg" -> "3.1.2"
)
