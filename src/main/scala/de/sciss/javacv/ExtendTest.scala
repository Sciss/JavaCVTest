package de.sciss.javacv

import java.awt.{BasicStroke, Color, RenderingHints}
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

import de.sciss.file._
import de.sciss.javacv.HoughTest2.Line
import org.bytedeco.javacpp.opencv_core.Point

object ExtendTest extends App {
  val w     = 2048
  val h     = 2048
  val ln1   = Line(new Point(1569, 1633), new Point(1583, 2047))
  val ln1x  = ln1.extend(w, h)
  println(s"$ln1 -> $ln1x")

  val ln2   = Line(new Point(1395, 1653), new Point(1581, 1663))
  val ln2x  = ln2.extend(w, h)
  println(s"$ln2 -> $ln2x")

  val ln3   = Line(new Point(1529, 1640), new Point(1581, 1692))
  val ln3x  = ln3.extend(w, h)
  println(s"$ln3 -> $ln3x")

  println(s"ln1  <-> ln2  ${ln1 .intersects(ln2)}")
  println(s"ln1x <-> ln2x ${ln1x.intersects(ln2x)}")

  println(s"ln1  <-> ln3  ${ln1 .intersects(ln3)}")
  println(s"ln1x <-> ln3x ${ln1x.intersects(ln3x)}")

  println(s"ln2  <-> ln3  ${ln2 .intersects(ln3)}")
  println(s"ln2x <-> ln3x ${ln2x.intersects(ln3x)}")

  //  val ln4   = Line(new Point(100, 100), new Point(210, 200))
//  val ln4x  = ln4.extend(w, h)
//  println(s"$ln4 -> $ln4x")
//
//  val ln5   = Line(new Point(100, 100), new Point(210, 50))
//  val ln5x  = ln5.extend(w, h)
//  println(s"$ln5 -> $ln5x")
//
//  val ln6   = Line(new Point(100, 100), new Point(110, 60))
//  val ln6x  = ln6.extend(w, h)
//  println(s"$ln6 -> $ln6x")

  val r       = new util.Random(0L)
  val lines   = Vector.fill(20) {
    Line(new Point(r.nextInt(w), r.nextInt(h)), new Point(r.nextInt(w), r.nextInt(h)))
  }
  val linesX  = lines.map { ln =>
    ln.extend(w, h)
  }

  val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)
  val g = out.createGraphics()
  g.setColor(Color.black)
  g.fillRect(0, 0, w, h)
  g.setColor(Color.white)
  g.setStroke(new BasicStroke(5f))
  g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
  linesX.foreach { case Line(pt1, pt2) =>
    g.drawLine(pt1.x, pt1.y, pt2.x, pt2.y)
  }
  g.setStroke(new BasicStroke(3f))
  g.setColor(Color.red)
  lines.foreach { case Line(pt1, pt2) =>
    g.drawLine(pt1.x, pt1.y, pt2.x, pt2.y)
  }
  g.dispose()
  ImageIO.write(out, "jpeg", userHome/"Documents"/"temp"/"test.jpg")
  out.flush()
}