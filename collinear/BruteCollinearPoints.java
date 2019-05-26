import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Stack;

public class BruteCollinearPoints {
    private final LineSegment[] ls;

    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        if (points == null)
            throw new IllegalArgumentException();

        for (int i = 0; i < points.length; ++i) {
            if (points[i] == null)
                throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length - 1; ++i) {
            for (int j = i + 1; j < points.length; ++j) {
                if (points[i].compareTo(points[j]) == 0) {
                    // StdOut.println(points[i].toString());
                    // StdOut.println(points[i].toString());
                    throw new IllegalArgumentException();
                }
            }
        }

        Point[] tmpPoints = Arrays.copyOf(points, points.length);

        Arrays.sort(tmpPoints, 0, tmpPoints.length);


        Stack<Point> firstP = new Stack<Point>();
        Stack<Point> lastP = new Stack<Point>();

        double ang1, ang2, ang3;

        for (int i = 0; i < tmpPoints.length - 3; ++i) {
            for (int j = i + 1; j < tmpPoints.length - 2; ++j) {
                ang1 = tmpPoints[i].slopeTo(tmpPoints[j]);
                for (int k = j + 1; k < tmpPoints.length - 1; ++k) {
                    ang2 = tmpPoints[j].slopeTo(tmpPoints[k]);
                    if (ang1 != ang2)
                        continue;
                    for (int m = k + 1; m < tmpPoints.length; ++m) {
                        ang3 = tmpPoints[k].slopeTo(tmpPoints[m]);
                        if (ang1 == ang2 && ang2 == ang3) {
                            firstP.push(tmpPoints[i]);
                            lastP.push(tmpPoints[m]);
                        }
                    }
                }
            }
        }

        ls = new LineSegment[firstP.size()];

        for (int i = 0; i < ls.length; ++i) {
            ls[i] = new LineSegment(firstP.pop(), lastP.pop());
        }

    }

    public int numberOfSegments()        // the number of line segments
    {
        return ls.length;
    }

    public LineSegment[] segments()                // the line segments
    {
        return Arrays.copyOf(ls, ls.length);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
