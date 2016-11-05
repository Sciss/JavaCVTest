/*
 *  LiveHough.scala
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

import org.bytedeco.javacpp.indexer.IntRawIndexer
import org.bytedeco.javacpp.opencv_core.{Mat, Point, Scalar}
import org.bytedeco.javacpp.{opencv_core, opencv_imgcodecs, opencv_imgproc}
import org.bytedeco.javacv.FrameGrabber.ImageMode
import org.bytedeco.javacv.{CanvasFrame, OpenCVFrameConverter, OpenCVFrameGrabber}

object LiveHough {
  def main(args: Array[String]): Unit = run()

  def run(): Unit = {
    val grabber   = new OpenCVFrameGrabber(0)
    val width     = 640
    val height    = 480
    grabber.setImageWidth (width )
    grabber.setImageHeight(height)
    grabber.setBitsPerPixel(opencv_core.CV_8U)
    grabber.setImageMode(ImageMode.COLOR)

    val lines       = new Mat()
    val toMat       = new OpenCVFrameConverter.ToMat
    val toIplImage  = new OpenCVFrameConverter.ToIplImage

    val rho         = 1.0
    val theta       = 1.0.toRadians
    val thresh      = 50
    val minLineLen  = 80
    val maxLineGap  = 1050
    val canvas      = new CanvasFrame("Test")
    val colr        = new Scalar(0, 0, 255, 128)

    val grayMat     = new Mat(width, height, opencv_core.CV_8U)
    val grayImg     = toMat.convert(grayMat)
    val grayIpl     = toIplImage.convert(grayImg)  // WTF
    val bwMat       = new Mat(width, height, opencv_core.CV_8U)
    val bwIpl       = toIplImage.convert(toMat.convert(bwMat))  // WTF

    grabber.start()

//    val white = new Mat(width, height, opencv_core.CV_8U)
    val white       = opencv_imgcodecs.imread("whiteC.png")
//    val black       = opencv_imgcodecs.imread("black.png")
    val whiteG      = new Mat(width, height, opencv_core.CV_8U)
    opencv_imgproc.cvtColor(white, whiteG, opencv_imgproc.CV_BGR2GRAY, 1)

    while (true) {
      val img0  = grabber.grab()
      val mat   = toMat.convert(img0)
      val img   = toIplImage.convert(img0)
      opencv_core.cvFlip(img, img, 1)
      opencv_imgproc.cvtColor(mat, grayMat, opencv_imgproc.CV_BGR2GRAY, 1)
//      val threshBin   = 0.5
//      val maxValue    = 0.0 // 1.0
//      val threshType  = opencv_imgproc.THRESH_BINARY_INV
//      opencv_imgproc.cvThreshold(grayIpl, bwIpl, threshBin, maxValue, threshType)
//      opencv_core.bitwise_not(whiteG, grayMat, grayMat)

//      opencv_imgproc.cvtColor(bwMat, mat, opencv_imgproc.CV_GRAY2BGR)
//      opencv_imgproc.cvtColor(mat, grayMat, opencv_imgproc.CV_BGR2GRAY, 1)
      opencv_imgproc.HoughLinesP(grayMat /* whiteG */, lines, rho, theta, thresh, minLineLen, maxLineGap)
      val indexer: IntRawIndexer = lines.createIndexer()

      var i = 0
      val rows = math.min(100, indexer.rows().toInt)
      while (i < rows) {
        val x1    = indexer.get(i, 0, 0)
        val y1    = indexer.get(i, 0, 1)
        val x2    = indexer.get(i, 0, 2)
        val y2    = indexer.get(i, 0, 3)

//        println(s"x1 = $x1, y1 = $y1, x2 = $x2, y2 = $y2")

        val pt1   = new Point(x1, y1)
        val pt2   = new Point(x2, y2)

        opencv_imgproc.line(grayMat /* mat */, pt1, pt2, colr, 1, opencv_core.LINE_AA, 0)
        i += 1
      }

      opencv_imgproc.cvtColor(grayMat, mat, opencv_imgproc.CV_GRAY2BGR)
      canvas.showImage(/* grayImg */ img0)
    }
  }
}
