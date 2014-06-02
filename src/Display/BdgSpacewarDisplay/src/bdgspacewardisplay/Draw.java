package bdgspacewardisplay;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.*;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import static java.lang.Math.*;

/**
 * Graphics2D-ben a rajzolást megkönnyítő osztály.
 * Mintája Robert Sedgewick és Kevin Wayne StdDraw osztálya; célja a Swing eszközeit kihasználó rajzolás StdDraw szintű egyszerűsége.
 * @author Czövek Márton, Forrás Bence
 * @version legújabb
 */
public class Draw {
    /** A rajzolási környezet */
    private final Graphics2D G;
    /** A skála végpontjai */
    private double xmin, xmax, ymin, ymax;
    private final int sizex,sizey; 
    private static final double DEFAULT_PEN_RADIUS = 0.002;
    private double penRadius = DEFAULT_PEN_RADIUS;
    
    Draw(Graphics2D G,int sizex,int sizey) {
        this.G = G;
        this.sizex=sizex;
        this.sizey=sizey;
        
        setScale();
    }
    
    private double  scaleX(double x) { return sizex  * (x - xmin) / (xmax - xmin); }
    private double  scaleY(double y) { return sizey * (ymax - y) / (ymax - ymin); }
    private double factorX(double w) { return w * sizex  / Math.abs(xmax - xmin);  }
    private double factorY(double h) { return h * sizey / Math.abs(ymax - ymin);  }
    
    /**
     * Kör adott középponttal és sugárral
     * @param u a középpont x-koordinátája
     * @param v a középpont y-koordinátája
     * @param r a kör sugara
     */
    void circle(double u, double v, double r) {
        double xs = scaleX(u);
        double ys = scaleY(v);
        double ws = factorX(r);
        double hs = factorY(r);
        G.draw(new Ellipse2D.Double(xs - ws, ys - hs, 2*ws, 2*hs));
    }

    /**
     * Kitöltött kör adott középponttal és sugárral
     * @param u a középpont x-koordinátája
     * @param v a középpont y-koordinátája
     * @param r a kör sugara
     */
    void filledCircle(double u, double v, double r) {
        double xs = scaleX(u);
        double ys = scaleY(v);
        double ws = factorX(r);
        double hs = factorY(r);
        G.fill(new Ellipse2D.Double(xs - ws, ys - hs, 2*ws, 2*hs));
    }
    
    /**
     * Szakasz adott végpontokkal
     * @param x0 első végpont x-koordinátája
     * @param y0 első végpont y-koordinátája
     * @param x1 második végpont x-koordinátája
     * @param y1 második végpont y-koordinátája
     */
    void line(double x0, double y0, double x1, double y1) {
        G.draw(new Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1), scaleY(y1)));
    }
    
    void setScale(double xmin, double xmax, double ymin, double ymax) {
        this.xmin = xmin-0.05*(xmax-xmin);
        this.xmax = xmax+.05*(xmax-xmin);
        this.ymin = ymin-.05*(ymax-ymin);
        this.ymax = ymax+.05*(ymax-ymin);
    }
    
    final void setScale() {
        setScale(-2,2,-4,0);
    }
    
    /**
     * Adott színűre változtatja a rajzolás közben használt tollszínt.
     * @param color tollszín
     */
    void setPenColor(Color color) {
        G.setColor(color);
    }

    /**
     * Feketére állítja vissza a tollszínt.
     */
    void setPenColor() {
        setPenColor(Color.BLACK);
    }

    void setPenRadius(double d) {
        penRadius = d * 512;
        BasicStroke stroke = new BasicStroke((float) penRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        G.setStroke(stroke);
    }
    
    /**
     * Négyzetes képet rajzol adott középponttal és fél oldalhosszal
     * @param img a kép
     * @param x középpont x-koordinátája
     * @param y középpont y-koordinátája
     * @param r fél oldalhossz
     */
    void drawSphereImage(Image img, double x, double y, double r) {
        G.drawImage(img, (int) scaleX(x-r), (int) scaleY(y+r), (int) factorX(2*r), (int) factorY(2*r), null);
    }

    /**
     * Négyzetes képet rajzol adott középponttal és fél oldalhosszal, elforgatva
     * @param img a kép
     * @param x középpont x-koordinátája
     * @param y középpont y-koordinátája
     * @param r fél oldalhossz
     * @param theta elforgatás szöge (fokban)
     */
    void drawRotatedImage(BufferedImage img, double x, double y, double r, double theta) {
        AffineTransform at = new AffineTransform();
        at.rotate(Math.toRadians(theta), img.getWidth()/2, img.getHeight()/2);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        img = op.filter(img, null);
        G.drawImage(img, (int) scaleX(x-r), (int) scaleY(y+r), (int) factorX(2*r), (int) factorY(2*r), null);
    }
    
    void setXscale(double xmin, double xmax) {
        this.xmin = xmin-0.05*(xmax-xmin);
        this.xmax = xmax+.05*(xmax-xmin);
    }
    
    void setYScale(double ymin, double ymax) {
        this.ymin = ymin-.05*(ymax-ymin);
        this.ymax = ymax+.05*(ymax-ymin);
    }

    void filledRectangle(double x, double y, double halfWidth, double halfHeight) {
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*halfWidth);
        double hs = factorY(2*halfHeight);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        G.fill(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    }

    void setPenRadius() {
        setPenRadius(DEFAULT_PEN_RADIUS);
    }

    void point(double x, double y) {
        double xs = scaleX(x);
        double ys = scaleY(y);
        double r = penRadius;
        if (r <= 1) pixel(x, y);
        else G.fill(new Ellipse2D.Double(xs - r/2, ys - r/2, r, r));
    }

    private void pixel(double x, double y) {
        G.fillRect((int) Math.round(scaleX(x)), (int) Math.round(scaleY(y)), 1, 1);
    }
    
    void triangle(double x, double y, double R, double phi) {
        double[] x_ = new double[3];
        double[] y_ = new double[3];
        
        x_[0] = x + R * cos(phi);
        x_[1] = x + R * cos(phi + PI*2/3);
        x_[2] = x + R * cos(phi + PI*4/3);
        
        y_[0] = y + R * sin(phi);
        y_[1] = y + R * sin(phi + PI*2/3);
        y_[2] = y + R * sin(phi + PI*4/3);
        
        polygon(x_, y_);
    }
    
    void polygon(double x[], double y[]) {
        if (x.length != y.length) throw new RuntimeException("Csúnya bácsi rossz tömböket adott.");
        for (int i = 0; i < x.length; i++) {
            line(x[i], y[i], x[(i+1)%x.length], y[(i+1)%x.length]);
        }
    }
}
