import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.Iterator;

public class PointSET {
    private final SET<Point2D> ptSet;
    private int numPoints;

    public PointSET()                               // construct an empty set of points
    {
        ptSet = new SET<Point2D>();
        numPoints = ptSet.size();
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return numPoints == 0;
    }

    public int size()                         // number of points in the set
    {
        return numPoints;
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new java.lang.IllegalArgumentException("Null input is not allowed!!!");

        ptSet.add(p);
        numPoints = ptSet.size();
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new java.lang.IllegalArgumentException("Null input is not allowed!!!");

        return ptSet.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        Iterator<Point2D> itr = ptSet.iterator();

        while (itr.hasNext()) {
            itr.next().draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new java.lang.IllegalArgumentException("Null input is not allowed!!!");

        Iterator<Point2D> itr = ptSet.iterator();
        Point2D tmpPt;
        Queue<Point2D> rst = new Queue<Point2D>();

        while (itr.hasNext()) {
            tmpPt = itr.next();
            if (rect.contains(tmpPt)) rst.enqueue(tmpPt);
        }

        return rst;
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new java.lang.IllegalArgumentException("Null input is not allowed!!!");

        if (isEmpty()) return null;

        Iterator<Point2D> itr = ptSet.iterator();
        Point2D tmpPt;
        Point2D rst = null;
        double distance = 0;
        double tmpDist = 0;

        while (itr.hasNext()) {
            tmpPt = itr.next();
            tmpDist = p.distanceSquaredTo(tmpPt);
            if (rst == null || tmpDist < distance) {
                rst = tmpPt;
                distance = tmpDist;
            }
        }

        return rst;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {

    }
}
