import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private Node root;
    private int numPoints;
    private final static int xLevel = 0;
    // private final static int Y_Level = 1;

    public KdTree()                               // construct an empty set of points
    {
        this.root = null;
        this.numPoints = 0;

    }

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D pt, RectHV rectHV) {
            this.p = pt;
            this.rect = rectHV;
            this.lb = null;
            this.rt = null;
        }
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
        root = insert(root, p, 0.0, 0.0, 1.0, 1.0, 0);
    }

    private Node insert(Node x, Point2D p, double x1, double y1, double x2, double y2, int xy) {
        if (x == null) {
            numPoints += 1;
            return new Node(p, new RectHV(x1, y1, x2, y2));
        }

        if (x.p.equals(p)) return x;

        if (xy == xLevel) {
            if (p.x() >= x.p.x()) {
                x.rt = insert(x.rt, p, x.p.x(), y1, x2, y2, (xy + 1) % 2);
            } else {
                x.lb = insert(x.lb, p, x1, y1, x.p.x(), y2, (xy + 1) % 2);
            }
        } else { // y coordinate
            if (p.y() >= x.p.y()) {
                x.rt = insert(x.rt, p, x1, x.p.y(), x2, y2, (xy + 1) % 2);
            } else {
                x.lb = insert(x.lb, p, x1, y1, x2, x.p.y(), (xy + 1) % 2);
            }
        }
        return x;
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new java.lang.IllegalArgumentException("Null input is not allowed!!!");
        return get(p) != null;
    }

    private Node get(Point2D p) {
        return get(root, p, 0);
    }

    private Node get(Node x, Point2D p, int xy) {
        if (p == null) throw new java.lang.IllegalArgumentException("Null input is not allowed!!!");
        if (x == null) return null;
        if (x.p.equals(p)) return x;
        if (xy == xLevel) {
            if (p.x() >= x.p.x()) {
                return get(x.rt, p, (xy + 1) % 2);
            } else {
                return get(x.lb, p, (xy + 1) % 2);
            }
        } else { // y coordinate
            if (p.y() >= x.p.y()) {
                return get(x.rt, p, (xy + 1) % 2);
            } else {
                return get(x.lb, p, (xy + 1) % 2);
            }
        }
    }

    public void draw()                         // draw all points to standard draw
    {
        draw(root, 0);
    }

    private void draw(Node x, int xy) {
        if (x == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.p.draw();
        if (xy == xLevel) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        draw(x.rt, (xy + 1) % 2);
        draw(x.lb, (xy + 1) % 2);
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new java.lang.IllegalArgumentException("Null input is not allowed!!!");

        Queue<Point2D> rst = new Queue<Point2D>();
        rangeSearch(root, rect, rst);
        return rst;
    }

    private void rangeSearch(Node x, RectHV rect, Queue<Point2D> queue) {
        if (x == null) return;
        if (rect.contains(x.p)) queue.enqueue(x.p);
        if (rect.intersects(x.rect)) {
            rangeSearch(x.lb, rect, queue);
            rangeSearch(x.rt, rect, queue);
        }
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new java.lang.IllegalArgumentException("Null input is not allowed!!!");

        if (isEmpty()) return null;

        PtDist rst = nearestSearch(root, p, 0, null);

        if (rst == null) return null;

        return rst.pt;
    }

    private static class PtDist {
        private Point2D pt;
        private double dist;

        public PtDist(Point2D p, double d) {
            this.pt = p;
            this.dist = d;
        }
    }

    private PtDist nearestSearch(Node x, Point2D pt, int xy, PtDist pd) {
        if (x == null) return null;
        PtDist curBestPt = new PtDist(x.p, pt.distanceSquaredTo(x.p));
        PtDist tmpPt = null;
        if (pd != null && pd.dist < curBestPt.dist) {
            curBestPt = pd;
        }

        if (!x.rect.contains(pt) && curBestPt.dist < x.rect.distanceSquaredTo(pt)) {
            return curBestPt;
        }

        if (xy == xLevel) {
            if (pt.x() >= x.p.x()) {
                tmpPt = nearestSearch(x.rt, pt, (xy + 1) % 2, curBestPt);
                if (tmpPt != null && tmpPt.dist < curBestPt.dist) {
                    curBestPt = tmpPt;
                }
                tmpPt = nearestSearch(x.lb, pt, (xy + 1) % 2, curBestPt);
                if (tmpPt != null && tmpPt.dist < curBestPt.dist) {
                    curBestPt = tmpPt;
                }
            } else {
                tmpPt = nearestSearch(x.lb, pt, (xy + 1) % 2, curBestPt);
                if (tmpPt != null && tmpPt.dist < curBestPt.dist) {
                    curBestPt = tmpPt;
                }
                tmpPt = nearestSearch(x.rt, pt, (xy + 1) % 2, curBestPt);
                if (tmpPt != null && tmpPt.dist < curBestPt.dist) {
                    curBestPt = tmpPt;
                }
            }
        } else {
            if (pt.y() >= x.p.y()) {
                tmpPt = nearestSearch(x.rt, pt, (xy + 1) % 2, curBestPt);
                if (tmpPt != null && tmpPt.dist < curBestPt.dist) {
                    curBestPt = tmpPt;
                }
                tmpPt = nearestSearch(x.lb, pt, (xy + 1) % 2, curBestPt);
                if (tmpPt != null && tmpPt.dist < curBestPt.dist) {
                    curBestPt = tmpPt;
                }
            } else {
                tmpPt = nearestSearch(x.lb, pt, (xy + 1) % 2, curBestPt);
                if (tmpPt != null && tmpPt.dist < curBestPt.dist) {
                    curBestPt = tmpPt;
                }
                tmpPt = nearestSearch(x.rt, pt, (xy + 1) % 2, curBestPt);
                if (tmpPt != null && tmpPt.dist < curBestPt.dist) {
                    curBestPt = tmpPt;
                }
            }
        }

        return curBestPt;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {

    }
}
