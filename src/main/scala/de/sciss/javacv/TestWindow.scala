/*
 *  TestWindow.scala
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

import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage

import de.sciss.file._
import org.bytedeco.javacv.{FFmpegFrameGrabber, Java2DFrameConverter}

import scala.swing.{Component, Frame, Graphics2D, MainFrame, SimpleSwingApplication, Swing}
import Swing._

object TestWindow extends SimpleSwingApplication {
  override def main(args: Array[String]): Unit = {
    super.main(args)
    new Thread {
      override def run(): Unit = grabLoop()
      start()
    }
  }

  @volatile
  private var img: BufferedImage = _

  private def grabLoop(): Unit = {
    val fVideo = userHome / "Documents" / "projects" / "Unlike" / "moor_out.mp4"
    val g = new FFmpegFrameGrabber(fVideo.path)
    val con = new Java2DFrameConverter
    g.start()
    var i = 0
    val t0 = System.currentTimeMillis()
    while (i < 6000) {
      val frame = g.grab()
      img   = con.getBufferedImage(frame, 1.0)
      // println(s"frame $i")
      component.repaint()
      component.peer.getToolkit.sync()
      i += 1
      val t1 = System.currentTimeMillis()
      // val fpsReal = i.toDouble * 1000 / (t1 - t0)
      val tx      = ((i * 1000).toDouble / 25).toLong + t0
      val dt      = tx - t1
      if (dt > 0) Thread.sleep(dt)
    }
  }

  lazy val component = new Component {
    preferredSize = (960, 540)

    private[this] final val atHalf = AffineTransform.getScaleInstance(0.5, 0.5)

    override protected def paintComponent(g: Graphics2D): Unit = {
      super.paintComponent(g)
      val i = img
      if (i != null) {
        // g.drawImage(i, 0, 0, peer)
        g.drawImage(i, atHalf, peer)
      }
    }
  }

  lazy val top: Frame = new MainFrame {
    contents = component
  }
}
