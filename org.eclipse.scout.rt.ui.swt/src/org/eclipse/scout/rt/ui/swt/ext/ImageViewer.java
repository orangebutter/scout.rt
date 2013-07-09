/*******************************************************************************
 * Copyright (c) 2010 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package org.eclipse.scout.rt.ui.swt.ext;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class ImageViewer extends Canvas {

  private int m_xAglin = SWT.CENTER;
  private int m_yAglin = SWT.CENTER;
  private boolean m_autoFit = false;

  private Image m_image;
  private Image m_scaledImage;
  private AffineTransform m_transform;
  private Rectangle m_canvas;

  public ImageViewer(Composite parent) {
    super(parent, SWT.NONE);
    addPaintListener(new PaintListener() {
      @Override
      public void paintControl(PaintEvent e) {
        handleSwtPaintEvent(e.gc);
      }
    });
    addDisposeListener(new DisposeListener() {
      @Override
      public void widgetDisposed(DisposeEvent e) {
        freeResources();
      }
    });
  }

  private void freeResources() {
    if (m_scaledImage != null && !m_scaledImage.isDisposed()) {
      m_scaledImage.dispose();
      m_scaledImage = null;
    }
  }

  public void setAffineTransform(AffineTransform transform) {
    m_transform = transform;
    redraw();
  }

  public void setCanvas(int x, int y, int width, int height) {
    setCanvas(new Rectangle(x, y, width, height));
  }

  public void setCanvas(Rectangle canvas) {
    m_canvas = canvas;
    redraw();
  }

//  public void setImageTransform(double dx, double dy, double sx, double sy, double angle) {
//    m_paneX = dx;
//    m_paneY = dy;
//    m_zoomX = sx;
//    m_zoomY = sy;
//    m_angle = angle;
//    System.out.println("img transformation [paneX = '" + m_paneX + "'] [paneY = '" + m_paneY + "'] [zoomX = '" + m_zoomX + "'] [zoomY = '" + m_zoomY + "'] [angle = '" + m_angle + "']");
//    redraw();
////    revalidateAndRepaint();
//  }

  private Image scaleImage(Image img) {
    freeResources();
    if (m_autoFit && img != null) {
      Point swtFieldSize = getSize();
      Rectangle imageBounds = img.getBounds();
      double scaleFactor = (double) swtFieldSize.x / (double) imageBounds.width;
      scaleFactor = Math.min(scaleFactor, (double) swtFieldSize.y / (double) img.getBounds().height);
      m_scaledImage = new Image(getDisplay(), img.getImageData().scaledTo((int) (scaleFactor * img.getBounds().width), (int) (scaleFactor * img.getBounds().height)));
      return m_scaledImage;
    }
    else {
      return img;
    }
  }

//  private Image transformImage(Image img) {
//    freeResources();
//
//    if (img != null) {
//
//      // zoom
//      double zoomX = m_zoomX;
//      double zoomY = m_zoomY;
//      if (m_autoFit) {
//        // calculate zoom factor autofit is stronger than zoom factors
//        Point swtFieldSize = getSize();
//        Rectangle imageBounds = img.getBounds();
//        double scaleFactor = (double) swtFieldSize.x / (double) imageBounds.width;
//        scaleFactor = Math.min(scaleFactor, (double) swtFieldSize.y / (double) img.getBounds().height);
//        zoomX = zoomY = scaleFactor;
//      }
//      ImageData imgData = img.getImageData();
//      if (zoomX != 1 || zoomY != 1) {
//        imgData = imgData.scaledTo((int) (zoomX * img.getBounds().width), (int) (zoomY * img.getBounds().height));
//      }
//      // rotate
//
//      m_scaledImage = new Image(getDisplay(), imgData);
//      return m_scaledImage;
//    }
//    return img;
//  }

  private Image scaleImage(Image img, double zoomX, double zoomY) {
    freeResources();
    if (img != null) {
      if (m_autoFit) {
        // calculate zoom factor autofit is stronger than zoom factors
        Point swtFieldSize = getSize();
        Rectangle imageBounds = img.getBounds();
        double scaleFactor = (double) swtFieldSize.x / (double) imageBounds.width;
        scaleFactor = Math.min(scaleFactor, (double) swtFieldSize.y / (double) img.getBounds().height);
        zoomX = zoomY = scaleFactor;
      }
      m_scaledImage = new Image(getDisplay(), img.getImageData().scaledTo((int) (zoomX * img.getBounds().width), (int) (zoomY * img.getBounds().height)));
      return m_scaledImage;
    }
    else {
      return img;
    }
  }

  protected void handleSwtPaintEvent(GC gc) {
    Image img = getImage();
    if (img != null) {
      freeResources();
      m_scaledImage = createTranslatedImg01(img);
      Rectangle bounds = gc.getClipping();
      Rectangle imgBounds = m_scaledImage.getBounds();
      // alignment
      int x = 0;
      if (imgBounds.width <= bounds.width) {
        switch (getAlignmentX()) {
          case SWT.CENTER:
            x = (bounds.width - imgBounds.width) / 2;
            break;
          case SWT.RIGHT:
            x = bounds.width - imgBounds.width;
            break;
          default:
            x = 0;
            break;
        }
      }
      int y = 0;
      if (imgBounds.height <= bounds.height) {
        switch (getAlignmentY()) {
          case SWT.CENTER:
            y = (bounds.height - imgBounds.height) / 2;
            break;
          case SWT.RIGHT:
            y = bounds.height - imgBounds.height;
            break;
          default:
            y = 0;
            break;
        }
      }
      gc.drawImage(m_scaledImage, x, y);
    }

//      Rectangle bounds = gc.getClipping();
//      Rectangle imgBounds = img.getBounds();
//      Transform transform = null;
//      Transform oldTransform = null;
//      try {
//        transform = new Transform(getDisplay());
//        oldTransform = new Transform(gc.getDevice());
//        gc.getTransform(oldTransform);
//
//        // zoom
//
//        transform.scale(scaleX, scaleY)
//        // rotation
//        int offsetX = (bounds.width / 2);
//        int offsetY = (bounds.height / 2);
//        transform.translate(offsetX, offsetY);
//        transform.rotate(new Float(m_angle).floatValue());
//        transform.translate(-offsetX, -offsetY);
//
//        // alignment
//        int x = 0;
//        if (imgBounds.width <= bounds.width) {
//          switch (getAlignmentX()) {
//            case SWT.CENTER:
//              x = (bounds.width - imgBounds.width) / 2;
//              break;
//            case SWT.RIGHT:
//              x = bounds.width - imgBounds.width;
//              break;
//            default:
//              x = 0;
//              break;
//          }
//        }
//        int y = 0;
//        if (imgBounds.height <= bounds.height) {
//          switch (getAlignmentY()) {
//            case SWT.CENTER:
//              y = (bounds.height - imgBounds.height) / 2;
//              break;
//            case SWT.RIGHT:
//              y = bounds.height - imgBounds.height;
//              break;
//            default:
//              y = 0;
//              break;
//          }
//        }
//
//        // draw image
//        gc.setTransform(transform);
//        gc.drawImage(img, x, y);
//      }
//      finally {
//        if (oldTransform != null) {
//          gc.setTransform(oldTransform);
//          oldTransform.dispose();
//        }
//        if (transform != null) {
//          transform.dispose();
//        }
//      }
//    }
//    Rectangle bounds = gc.getClipping();
//    if (img != null) {
//      // scale img
//      img = scaleImage(img, m_zoomX, m_zoomY);
////      if (isAutoFit()) {
////        img = scaleImage(img);
////      }
//      Rectangle imgBounds = img.getBounds();
//      int x = 0;
//      if (imgBounds.width <= bounds.width) {
//        switch (getAlignmentX()) {
//          case SWT.CENTER:
//            x = (bounds.width - imgBounds.width) / 2;
//            break;
//          case SWT.RIGHT:
//            x = bounds.width - imgBounds.width;
//            break;
//          default:
//            x = 0;
//            break;
//        }
//      }
//      int y = 0;
//      if (imgBounds.height <= bounds.height) {
//        switch (getAlignmentY()) {
//          case SWT.CENTER:
//            y = (bounds.height - imgBounds.height) / 2;
//            break;
//          case SWT.RIGHT:
//            y = bounds.height - imgBounds.height;
//            break;
//          default:
//            y = 0;
//            break;
//        }
//      }
//      // draw
//      gc.drawImage(img, x, y);
//    }
  }

  private Image createTranslatedImg(Image oriImage) {
    Rectangle imgBounds = oriImage.getBounds();
    Rectangle canvas = m_canvas;
    Point origin = new Point(0, 0);
    AffineTransform transformLocal = null;
    if (canvas == null) {
      // transform bounds
      if (m_transform == null) {
        canvas = imgBounds;
      }
      else {
        transformLocal = new AffineTransform(m_transform);
        // top left
        Point2D origTopLeft = new Point2D.Double(0, 0);
        Point2D transformedTopLeft = new Point2D.Double();
        transformLocal.transform(origTopLeft, transformedTopLeft);
        System.out.println("TopLeft = " + transformedTopLeft);
        // top right
        Point2D origTopRight = new Point2D.Double(imgBounds.width, 0);
        Point2D transformedTopRight = new Point2D.Double();
        transformLocal.transform(origTopRight, transformedTopRight);
        // bottom left
        Point2D origBottomLeft = new Point2D.Double(0, -imgBounds.height);
        Point2D transformedBottomLeft = new Point2D.Double();
        transformLocal.transform(origBottomLeft, transformedBottomLeft);
        // bottom right
        Point2D origBottomRight = new Point2D.Double(imgBounds.width, -imgBounds.height);
        Point2D transformedBottomRight = new Point2D.Double();
        transformLocal.transform(origBottomRight, transformedBottomRight);
        // calc canvas
        double minX = Math.min(transformedTopLeft.getX(), transformedTopRight.getX());
        minX = Math.min(minX, transformedBottomLeft.getX());
        minX = Math.min(minX, transformedBottomRight.getX());
        double maxX = Math.max(transformedTopLeft.getX(), transformedTopRight.getX());
        maxX = Math.max(maxX, transformedBottomLeft.getX());
        maxX = Math.max(maxX, transformedBottomRight.getX());
        // calc canvas
        double minY = Math.min(transformedTopLeft.getY(), transformedTopRight.getY());
        minY = Math.min(minY, transformedBottomLeft.getY());
        minY = Math.min(minY, transformedBottomRight.getY());
        double maxY = Math.max(transformedTopLeft.getY(), transformedTopRight.getY());
        maxY = Math.max(maxY, transformedBottomLeft.getY());
        maxY = Math.max(maxY, transformedBottomRight.getY());
        canvas = new Rectangle((int) minX, (int) minY, (int) (maxX - minX), (int) (maxY - minY));

//        transformLocal.setToTranslation(-minX, maxY);
//        transformLocal.shear(-minX, maxY);
        origin = new Point((int) canvas.x, -(int) canvas.y);
      }
    }
    System.out.println(canvas);
    //
    Image transformedImg = new Image(getDisplay(), canvas.width, canvas.height);
    Transform transform = null;
    GC gc = null;
    try {
      gc = new GC(transformedImg);
      gc.setBackground(transformedImg.getDevice().getSystemColor(SWT.COLOR_BLUE));
      gc.fillRectangle(0, 0, canvas.width, canvas.height);
      transform = new Transform(getDisplay());
      if (transformLocal != null) {
        transform.setElements((float) transformLocal.getScaleX(), (float) transformLocal.getShearX(), (float) transformLocal.getShearY(), (float) transformLocal.getScaleY(), (float) transformLocal.getTranslateX(), (float) transformLocal.getTranslateY());
      }
      // compute clipping
      float[] topLeft = new float[]{0, 0};
      transform.transform(topLeft);
      System.out.println("****" + Arrays.toString(topLeft));
      float[] topRight = new float[]{imgBounds.width, 0};
      transform.transform(topRight);
      System.out.println("****" + Arrays.toString(topRight));
      float[] bottomLeft = new float[]{0, -imgBounds.height};
      transform.transform(bottomLeft);
      System.out.println("****" + Arrays.toString(bottomLeft));
      float[] bottomRight = new float[]{imgBounds.width, -imgBounds.height};
      transform.transform(bottomRight);
      System.out.println("****" + Arrays.toString(bottomRight));

//      transform.translate(origin.x, origin.y);
      System.out.println(origin);
      int x = (int) ((canvas.width - imgBounds.width) / 2);
      int y = (int) ((canvas.height - imgBounds.height) / 2);
      gc.setTransform(transform);
      System.out.println("draw image to [" + x + " - " + y + "]");
      gc.drawImage(oriImage, x, y);
//      gc.drawImage(oriImage, origin.x, origin.y);

//      gc.drawImage(image, srcX, srcY, srcWidth, srcHeight, destX, destY, destWidth, destHeight)
    }
    finally {
      if (transform != null) {
        transform.dispose();
      }
      if (gc != null) {
        gc.dispose();
      }

    }
    return transformedImg;
  }

  private Image createTranslatedImg01(Image oriImage) {
    Rectangle imgBounds = oriImage.getBounds();
    Rectangle canvas = m_canvas;
    Rectangle transformationArea = null;
    if (canvas == null) {
      if (m_transform == null) {
        canvas = new Rectangle(0, 0, imgBounds.width, imgBounds.height);
        transformationArea = new Rectangle(canvas.x, canvas.y, canvas.width, canvas.height);
      }
      else {
        // compute clipping
        canvas = new Rectangle(0, 0, 0, 0);
        Transform transform = new Transform(getDisplay());
        try {
          transform.setElements((float) m_transform.getScaleX(), (float) m_transform.getShearX(), (float) m_transform.getShearY(), (float) m_transform.getScaleY(), (float) m_transform.getTranslateX(), (float) m_transform.getTranslateY());
          // corner points
          // compute clipping
          float[] imgBoundsPoints = new float[]{
              /*top left*/0, 0,
              /*bottom left*/0, -imgBounds.height,
              /*bottom right*/imgBounds.width, -imgBounds.height,
              /*top right*/imgBounds.width, 0
          };
          System.out.println("A{o}: [" + imgBoundsPoints[0] + ", " + imgBoundsPoints[1] + "]");
          System.out.println("B{o}: [" + imgBoundsPoints[2] + ", " + imgBoundsPoints[3] + "]");
          System.out.println("C{o}: [" + imgBoundsPoints[4] + ", " + imgBoundsPoints[5] + "]");
          System.out.println("D{o}: [" + imgBoundsPoints[6] + ", " + imgBoundsPoints[7] + "]");
          transform.transform(imgBoundsPoints);
          System.out.println(Arrays.toString(imgBoundsPoints));
          System.out.println("A: [" + imgBoundsPoints[0] + ", " + imgBoundsPoints[1] + "]");
          System.out.println("B: [" + imgBoundsPoints[2] + ", " + imgBoundsPoints[3] + "]");
          System.out.println("C: [" + imgBoundsPoints[4] + ", " + imgBoundsPoints[5] + "]");
          System.out.println("D: [" + imgBoundsPoints[6] + ", " + imgBoundsPoints[7] + "]");
          float maxx = Float.MIN_VALUE;
          float maxy = Float.MIN_VALUE;
          float minx = Float.MAX_VALUE;
          float miny = Float.MAX_VALUE;
          for (int i = 0; i < imgBoundsPoints.length; i += 2) {
            minx = Math.min(minx, imgBoundsPoints[i]);
            maxx = Math.max(maxx, imgBoundsPoints[i]);
            miny = Math.min(miny, imgBoundsPoints[i + 1]);
            maxy = Math.max(maxy, imgBoundsPoints[i + 1]);
          }
          System.out.println("X: MIN[" + minx + "], MAX[" + maxx + "]");
          System.out.println("Y: MIN[" + miny + "], MAX[" + maxy + "]");
          canvas = new Rectangle((int) minx, (int) miny, (int) (maxx - minx), (int) (maxy - miny));
          System.out.println("canvas: " + canvas);
          // transformation area
          minx = Math.min(minx, imgBounds.x);
          miny = Math.min(miny, -imgBounds.height);
          maxx = Math.max(maxx, imgBounds.x + imgBounds.width);
          maxy = Math.max(maxy, 0);

          transformationArea = new Rectangle((int) minx, (int) miny, (int) (maxx - minx), (int) (maxy - miny));
          System.out.println("transformationArea: " + transformationArea);
//          float[] topLeft = new float[]{0, 0};
//          transform.transform(topLeft);
//          System.out.println("topleft****" + Arrays.toString(topLeft));
//          float[] topRight = new float[]{imgBounds.width, 0};
//          transform.transform(topRight);
//          System.out.println("topRight****" + Arrays.toString(topRight));
//          float[] bottomLeft = new float[]{0, imgBounds.height};
//          transform.transform(bottomLeft);
//          System.out.println("bottomLeft****" + Arrays.toString(bottomLeft));
//          float[] bottomRight = new float[]{imgBounds.width, imgBounds.height};
//          transform.transform(bottomRight);
//          System.out.println("bottomRight****" + Arrays.toString(bottomRight));
//          float minX = Math.min(topLeft[0], topRight[0]);
//          minX = Math.min(minX, bottomLeft[0]);
//          minX = Math.min(minX, bottomRight[0]);
//          float maxX = Math.max(topLeft[0], topRight[0]);
//          maxX = Math.max(maxX, bottomLeft[0]);
//          maxX = Math.max(maxX, bottomRight[0]);
//          float minY = Math.min(topLeft[1], topRight[1]);
//          minY = Math.min(minY, bottomLeft[1]);
//          minY = Math.min(minY, bottomRight[1]);
//          float maxY = Math.max(topLeft[1], topRight[1]);
//          maxY = Math.max(maxY, bottomLeft[1]);
//          maxY = Math.max(maxY, bottomRight[1]);
//          System.out.println("minX = " + minX);
//          System.out.println("maxX = " + maxX);
//          System.out.println("minY = " + minY);
//          System.out.println("maxY = " + maxY);
//          canvas = new Rectangle((int) minX, (int) minY, (int) (maxX - minX), (int) (maxY - minY));
//          System.out.println(canvas);
        }
        finally {
          transform.dispose();
        }
      }
    }
    else {
      transformationArea = new Rectangle(canvas.x, canvas.y, canvas.width, canvas.height);
    }

    // paint img
    Image transformedImg = new Image(getDisplay(), canvas.width, canvas.height);
    Image transformationImage = new Image(getDisplay(), transformationArea.width, transformationArea.height);
    Transform transform = null;
    Transform originalTransform = null;
    GC gc = null;
    try {
      gc = new GC(transformationImage);
      gc.setBackground(transformedImg.getDevice().getSystemColor(SWT.COLOR_BLUE));
      gc.fillRectangle(0, 0, canvas.width, canvas.height);
      originalTransform = new Transform(transformedImg.getDevice());
      gc.getTransform(originalTransform);
      transform = new Transform(transformedImg.getDevice());
      if (m_transform != null) {
        transform.setElements((float) m_transform.getScaleX(), (float) m_transform.getShearY(), (float) m_transform.getShearX(), (float) m_transform.getScaleY(), (float) m_transform.getTranslateX(), (float) m_transform.getTranslateY());
//        transform.setElements((float) m_transform.getScaleX(), (float) m_transform.getShearX(), (float) m_transform.getShearY(), (float) m_transform.getScaleY(), (float) m_transform.getTranslateX(), (float) m_transform.getTranslateY());
      }
      transform.translate(-transformationArea.x, (-transformationArea.height) + imgBounds.height);
//      transform.translate(100, 00);

      gc.setTransform(transform);
      gc.drawImage(oriImage, 0, 0);
      gc.drawImage(oriImage,       -transformationArea.x, (-transformationArea.height) + imgBounds.height);
//      gc.setTransform(originalTransform);
//      gc.copyArea(transformedImg, -50, 0);
//      gc.copyArea(canvas.x, canvas.y, canvas.width, canvas.height, 0, 0, true);
//      GC gc2 = new GC(transformedImg);
//      gc2.setBackground(transformedImg.getDevice().getSystemColor(SWT.COLOR_YELLOW));
//      gc2.fillRectangle(gc2.getClipping());
      gc.copyArea(transformedImg, canvas.x, canvas.y + canvas.height);
      return transformationImage;
    }
    finally {
      if (transform != null) {
        transform.dispose();
      }
      if (gc != null) {
        gc.dispose();
      }
//      transformationImage.dispose();
    }
//    return transformedImg;

  }

////  private Image createTranslatedImage(Image orignalImg) {
////    Rectangle originalImageBounds = orignalImg.getBounds();
////    Point size = computeImageArea(originalImageBounds.width, originalImageBounds.height, m_angle);
////    double w = size.x;
////    double h = size.y;
////    Image transformedImg = new Image(orignalImg.getDevice(), (int) w, (int) h);
////    Transform transform = null;
////    GC gc = null;
////    try {
////      gc = new GC(transformedImg);
////      gc.setBackground(transformedImg.getDevice().getSystemColor(SWT.COLOR_BLUE));
////      gc.fillRectangle(0, 0, (int) w, (int) h);
////      transform = new Transform(transformedImg.getDevice());
////      AffineTransform at;
////      at.trans
//////      at.
////      transform.
////      // rotate
////      int offsetX = (int) (w / 2);
////      int offsetY = (int) (h / 2);
////      if (m_angle != 0) {
////        transform.translate(offsetX, offsetY);
////        transform.rotate(new Float(m_angle).floatValue());
////        transform.translate(-offsetX, -offsetY);
////      }
////      // zoom
////      if (m_zoomX != 1 || m_zoomY != 1) {
////        transform.translate(offsetX, offsetY);
////        transform.scale((float) m_zoomX, (float) m_zoomY);
////        transform.translate(-offsetX, -offsetY);
////      }
////      gc.setTransform(transform);
////      int x = (int) ((w - originalImageBounds.width) / 2);
////      int y = (int) ((h - originalImageBounds.height) / 2);
////      gc.drawImage(orignalImg, x, y);
////    }
////    finally {
////      if (transform != null) {
////        transform.dispose();
////      }
////      if (gc != null) {
////        gc.dispose();
////      }
////    }
////    return transformedImg;
////  }
//
//  private Point computeImageArea(int w, int h, double angle) {
//    double width = (double) w;
//    double height = (double) h;
//    if (m_zoomX != 1 || m_zoomY != 1) {
//      width = m_zoomX * width;
//      height = m_zoomY * height;
//    }
//    double radAngle = Math.toRadians(angle) % Math.PI;
//    if (radAngle > Math.PI / 2) {
//      radAngle = Math.PI / 2 - (radAngle % (Math.PI / 2));
//    }
//    double rotatedW = Math.cos(radAngle) * width + Math.sin(radAngle) * height;
//    double rotatedH = Math.cos(radAngle) * height + Math.sin(radAngle) * width;
//    // zoom
////    rotatedW = m_zoomX * rotatedW;
////    rotatedH = m_zoomY * rotatedH;
//    int wEff = (int) rotatedW;
//    int hEff = (int) rotatedH;
//    return new Point(wEff, hEff);
//  }

  @Override
  public Point computeSize(int hint, int hint2, boolean changed) {
    Point size = super.computeSize(hint, hint2, changed);
    if (getImage() != null) {
      Rectangle imgBounds = getImage().getBounds();
      size.x = imgBounds.width;
      size.y = imgBounds.height;
    }
    return size;
  }

  public void setAlignmentX(int alignment) {
    m_xAglin = alignment;
  }

  public int getAlignmentX() {
    return m_xAglin;
  }

  public void setAlignmentY(int alignment) {
    m_yAglin = alignment;
  }

  public int getAlignmentY() {
    return m_yAglin;
  }

  public boolean isAutoFit() {
    return m_autoFit;
  }

  public void setAutoFit(boolean autoFit) {
    m_autoFit = autoFit;
  }

  public void setImage(Image img) {
    m_image = img;
  }

  public Image getImage() {
    return m_image;
  }

}
