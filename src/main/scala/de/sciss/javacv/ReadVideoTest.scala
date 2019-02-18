/*
 *  ReadVideoTest.scala
 *  (JavaCVTest)
 *
 *  Copyright (c) 2016-2019 Hanns Holger Rutz. All rights reserved.
 *
 *  This software is published under the GNU General Public License v3+
 *
 *
 *  For further information, please contact Hanns Holger Rutz at
 *  contact@sciss.de
 */

package de.sciss.javacv

import de.sciss.file._
import org.bytedeco.javacv.{FFmpegFrameGrabber, Java2DFrameConverter}

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
//  val fVideo = userHome / "Documents" / "projects" / "Unlike" / "moor_out.mp4"
    val g = new FFmpegFrameGrabber(fVideo.path)
    g.start()

    val con = new Java2DFrameConverter

    val t0 = System.currentTimeMillis()
    var i = 0
    while (i < 50) {
      val frame = g.grab()
      /*val img   =*/ con.getBufferedImage(frame, 1.0)
      println(s"frame $i")
      i += 1
    }
    val t1 = System.currentTimeMillis()
    println(s"For 50 frames, took ${t1-t0} milliseconds.")

    g.stop()
  }
}
