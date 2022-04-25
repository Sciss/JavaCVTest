lazy val root = project.in(file("."))
  .settings(
    name         := "JavaCVTest",
    version      := "0.1.0-SNAPSHOT",
    description  := "Small test project for trying out JavaCV",
    organization := "de.sciss",
    homepage     := Some(url(s"https://github.com/Sciss/${name.value}")),
    licenses     := Seq("gpl v3+" -> url("http://www.gnu.org/licenses/gpl-3.0.txt")),
    scalaVersion := "2.13.8",
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature", "-encoding", "utf8", "-Xlint"),
    libraryDependencies ++= Seq(
      "de.sciss"          %% "fileutil"   % "1.1.5",
      "de.sciss"          %% "swingplus"  % "0.5.0",
      "com.github.scopt"  %% "scopt"      % "4.0.1"
    ),
    javaCppPresetLibs ++= Seq(
      "ffmpeg" -> "4.0.2" // "3.4.1"
    ),
  )
