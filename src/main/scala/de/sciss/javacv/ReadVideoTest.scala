/*
 *  ReadVideoTest.scala
 *  (JavaCVTest)
 *
 *  Copyright (c) 2016-2022 Hanns Holger Rutz. All rights reserved.
 *
 *  This software is published under the GNU General Public License v3+
 *
 *
 *  For further information, please contact Hanns Holger Rutz at
 *  contact@sciss.de
 */

package de.sciss.javacv

import de.sciss.file._
import org.bytedeco.javacv.{FFmpegFrameGrabber, Frame, Java2DFrameConverter}

// cf. https://stackoverflow.com/questions/15735716/how-can-i-get-a-frame-sample-jpeg-from-a-video-mov#22107132
object ReadVideoTest {
  case class Config(fVideo: File = new File("in"))

  def main(args: Array[String]): Unit = {
    val default = Config()

    val p = new scopt.OptionParser[Config]("Read Video Test") {
      opt[File]('i', "input")
        .text("Input video file")
        .required()
        .action { (f, c) => c.copy(fVideo = f) }
    }
    p.parse(args, default).fold(sys.exit(1)) { implicit config =>
//      if (config.fOut.length() > 0) {
//        println(s"File '${config.fOut} already exists. Not overwriting.")
//      } else {
        run()
//      }
    }
  }

  def run()(implicit config: Config): Unit = {
    import config._
    require (fVideo.isFile, s"File not found: $fVideo")
    val g = new FFmpegFrameGrabber(fVideo.path)
    g.start()
    try {
      val con = new Java2DFrameConverter
      val t0 = System.currentTimeMillis()
      var i = 0
      var frame: Frame = null
      while ({
        frame = g.grabFrame(/* doAudio = */ false, /* doVideo = */ true, /* processImage */ true, /* keyFrames */ false)
        frame != null
      }) {
        val img = con.getBufferedImage(frame, 1.0)
        println(s"frame $i - ${img.getWidth} x ${img.getHeight}")
        i += 1
      }
      val t1 = System.currentTimeMillis()
      println(s"For $i frames, took ${t1-t0} milliseconds.")

    } finally {
      g.stop()
    }
  }
}
