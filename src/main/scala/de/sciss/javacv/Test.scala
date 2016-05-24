package de.sciss.javacv

import de.sciss.file._
import org.bytedeco.javacv.{FFmpegFrameGrabber, Java2DFrameConverter}

// cf. https://stackoverflow.com/questions/15735716/how-can-i-get-a-frame-sample-jpeg-from-a-video-mov#22107132
object Test extends App {
  val fVideo = userHome / "Documents" / "projects" / "Unlike" / "moor_out.mp4"
  val g = new FFmpegFrameGrabber(fVideo.path)
  g.start()

  val con = new Java2DFrameConverter

  val t0 = System.currentTimeMillis()
  var i = 0
  while (i < 50) {
    val frame = g.grab()
    val img   = con.getBufferedImage(frame, 1.0)
    println(s"frame $i")
    i += 1
  }
  val t1 = System.currentTimeMillis()
  println(s"For 50 frames, took ${t1-t0} milliseconds.")

  g.stop()
}
