/*
 *  CircleHoughTest.scala
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
import org.bytedeco.javacpp.indexer.FloatRawIndexer
import org.bytedeco.javacpp.opencv_core.{Mat, Point, Scalar}
import org.bytedeco.javacpp.{opencv_core, opencv_imgcodecs, opencv_imgproc}

object CircleHoughTest {
  def main(args: Array[String]): Unit = {
    val id = "0063"
    val pathIn      = if (args.length > 0) args(0) else {
//      "/home/hhrutz/Documents/projects/Kontakt/materials/photos/snap-210419_185417.jpg"
//      "/home/hhrutz/Documents/projects/peek2021/swap_space/scans/220423_group/IMG_0054ovr_eq_scale.jpg"
//      "/home/hhrutz/Documents/projects/peek2021/swap_space/scans/220423_group/IMG_0054ovr_eq_scale-hpf.jpg"
      s"/home/hhrutz/Documents/projects/peek2021/swap_space/scans/220423_group/IMG_${id}ovr_eq_scale.jpg"
    }
    val pathInPre =
//      "/home/hhrutz/Documents/projects/peek2021/swap_space/scans/220423_group/IMG_0054ovr_eq_scale-gray-T.jpg"
      s"/home/hhrutz/Documents/projects/peek2021/swap_space/scans/220423_group/IMG_${id}ovr_eq_scale-gray-T.jpg"
    val mat         = opencv_imgcodecs.imread(pathIn)
    val matPre      = opencv_imgcodecs.imread(pathInPre)
    val greyMat     = new Mat()
//    val blurMat     = new Mat()
    val circles     = new Mat()
    val t1 = System.currentTimeMillis()
//    opencv_imgproc.cvSmooth(mat.asInstanceOf[CvArr], blurMat.asInstanceOf[CvArr], opencv_imgproc.CV_GAUSSIAN, 8, 8, 0.0, 0.0)
    opencv_imgproc.cvtColor(matPre /*blurMat*/, greyMat, opencv_imgproc.CV_BGR2GRAY, 1)
    val t2 = System.currentTimeMillis()
    println(s"Analysis took ${t2-t1}ms.")
    val minRadius     = 6
    val maxRadius     = 200
    val maxCenterGap  = 100
//    val threshCanny   = 52 // 100 // 150
//    val threshCenter  = 52 // 55 // 50 // 100

    def circleHoughLoop(thresh: Int): Int = {
      val threshCanny   = thresh
      val threshCenter  = thresh
      opencv_imgproc.HoughCircles(greyMat, circles, opencv_imgproc.HOUGH_GRADIENT, 1, maxCenterGap,
        threshCanny, threshCenter, minRadius, maxRadius)
      val numCircles = circles.cols()
      numCircles
    }

    def circleHough(numWanted: Int = 6): Boolean = {
      val numCircles = {
        var inc     = 10
        var dir     = true
        var thresh  = 30
        var res     = Map.empty[Int, Int]
        var done    = false
        while (!done) {
          val num = circleHoughLoop(thresh = thresh)
          res += thresh -> num
          println(s"thresh: $thresh, num = $num")
          if (num > numWanted) {
            if (dir) thresh += inc else {
              dir = !dir
              if (inc > 1) {
                inc    -= 1
                thresh += inc
              } else {
                done = true
              }
            }
          } else if (num < numWanted) {
            if (!dir) thresh -= inc else {
              if (inc > 1) {
                inc    -= 1
                thresh -= inc
              } else {
                done = true
              }
            }
          } else {
            done = true
          }
        }
        val cand = res.filter(_._2 >= numWanted)
        val (threshBest, numBest) = cand.minBy(_._2)
        println(s"threshBest: $threshBest, numBest = $numBest")
        circleHoughLoop(thresh = threshBest)  // fills "circles"
        numBest
      }

      val hasCircles = numCircles > 0
      if (hasCircles) {
        val indexer: FloatRawIndexer = circles.createIndexer()
        println(s"sizes = ${indexer.sizes().mkString("[", ", ", "]")}")

        for (i <- 0 until indexer.cols().toInt /* lines.rows() */) {
          val cx        = math.round(indexer.get(0, i, 0))
          val cy        = math.round(indexer.get(0, i, 1))
          val r         = math.round(indexer.get(0, i, 2))
          val strength  = indexer.get(0, i, 2)

          println(s"Center(cx = $cx, cy = $cy, r = $r, strength = $strength),")

          val pt    = new Point(cx, cy)
          val colr  = new Scalar(0, 0, 255, 128)
          opencv_imgproc.circle(mat, pt, r, colr, /* thickness = */2, opencv_core.LINE_AA, 0)
        }
      }
      hasCircles
    }

    if (!circleHough()) println("! No circles detected !") else {
      val base    = userHome / "Documents"
      require (base.isDirectory)
      val dirOut  = base / "temp"
      dirOut.mkdirs()
      val fOut    = dirOut / s"circle-hough-$id-test.jpg"
      val pathOut = fOut.path
      opencv_imgcodecs.imwrite(pathOut, mat)
    }
  }
}
