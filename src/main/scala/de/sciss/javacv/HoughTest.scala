/*
 *  HoughTest.scala
 *  (JavaCVTest)
 *
 *  Copyright (c) 2016 Hanns Holger Rutz. All rights reserved.
 *
 *  This software is published under the GNU General Public License v2+
 *
 *
 *  For further information, please contact Hanns Holger Rutz at
 *  contact@sciss.de
 */

package de.sciss.javacv

import org.bytedeco.javacpp.indexer.{FloatRawIndexer, IntRawIndexer}
import org.bytedeco.javacpp.opencv_core.{Mat, Point, Scalar}
import org.bytedeco.javacpp.{opencv_core, opencv_imgcodecs, opencv_imgproc}
import de.sciss.file._

object HoughTest {
  def main(args: Array[String]): Unit = {
    val pathIn      = if (args.length > 0) args(0) else {
//      val fIn       = userHome / "Pictures" / "retina_hh.jpg"
//      val fIn       = file("test2.png")
//      val fIn = userHome / "Documents" / "projects" / "Imperfect" / "david" / "causality_report" / "ev_003_plane_12.png"
//      val fIn = userHome / "Documents" / "projects" / "Imperfect" / "david" / "causality_report" / "ev_001_plane_12.png"
//      fIn.path
      "/home/hhrutz/Documents/devel/minimal-mistakes/images/min15_peripherie_ac0d8490_2.jpg"
//      "/home/hhrutz/Pictures/Webcam/foo.jpg"
    }
    val mat         = opencv_imgcodecs.imread(pathIn)
    val greyMat     = new Mat()
    val lines       = new Mat()
    val t1 = System.currentTimeMillis()
    opencv_imgproc.cvtColor(mat, greyMat, opencv_imgproc.CV_BGR2GRAY, 1)
    val t2 = System.currentTimeMillis()
    println(s"Analysis took ${t2-t1}ms.")
    val rho         = 1.0
    val theta       = 1.0.toRadians
    val thresh      = 50
    val minLineLen  = 40
    val maxLineGap  = 5

    def houghP(): Unit = {
      opencv_imgproc.HoughLinesP(greyMat, lines, rho, theta, thresh, minLineLen, maxLineGap)

      println(s"rows = ${lines.rows()}; cols = ${lines.cols()}")
      val indexer: IntRawIndexer = lines.createIndexer() //.asInstanceOf[UByteRawIndexer]
      println(s"sizes = ${indexer.sizes().mkString("[", ", ", "]")}")

      for (i <- 0 until indexer.rows().toInt /* lines.rows() */) {
        val x1    = indexer.get(i, 0, 0)
        val y1    = indexer.get(i, 0, 1)
        val x2    = indexer.get(i, 0, 2)
        val y2    = indexer.get(i, 0, 3)

        println(s"x1 = $x1, y1 = $y1, x2 = $x2, y2 = $y2")

        val pt1 = new Point(x1, y1)
        val pt2 = new Point(x2, y2)

        //      val pt1   = new Point(indexer.get(i, 0, 0), indexer.get(i, 0, 1))
        //      val pt2   = new Point(indexer.get(i, 0, 2), indexer.get(i, 0, 3))
        val colr  = new Scalar(0, 0, 255, 128)

        opencv_imgproc.line(mat, pt1, pt2, colr, 1, opencv_core.LINE_AA, 0)
      }
    }

    def hough(): Unit = {
      opencv_imgproc.HoughLines(greyMat, lines, rho, theta, thresh)

      println(s"rows = ${lines.rows()}; cols = ${lines.cols()}")
      val indexer: FloatRawIndexer = lines.createIndexer() //.asInstanceOf[UByteRawIndexer]
      println(s"sizes = ${indexer.sizes().mkString("[", ", ", "]")}")

      for (i <- 0 until indexer.rows().toInt /* lines.rows() */) {
        val rhoV    = indexer.get(i, 0, 0)
        val thetaV  = indexer.get(i, 0, 1)
        println(s"rho = $rhoV, theta = $thetaV")

        val a   = math.cos(thetaV)
        val b   = math.sin(thetaV)
        val x0  = a * rhoV
        val y0  = b * rhoV
        val x1  = math.round(x0 + 100 * (-b)).toInt
        val y1  = math.round(y0 + 100 * a).toInt
        val x2  = math.round(x0 - 100 * (-b)).toInt
        val y2  = math.round(y0 - 100 * a).toInt
        val pt1 = new Point(x1, y1)
        val pt2 = new Point(x2, y2)

        val colr  = new Scalar(0, 0, 255, 128)

        opencv_imgproc.line(mat, pt1, pt2, colr, 1, opencv_core.LINE_AA, 0)
      }
    }

    houghP()

    val fOut    = userHome / "Documents" / "temp" / "test.jpg"
    val pathOut = fOut.path
    opencv_imgcodecs.imwrite(pathOut, mat)
  }
}
