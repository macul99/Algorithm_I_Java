import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private final LineSegment[] ls;

    public FastCollinearPoints(
            Point[] points)     // finds all line segments containing 4 or more points
    {
        if (points == null)
            throw new IllegalArgumentException();

        for (int i = 0; i < points.length; ++i) {
            if (points[i] == null)
                throw new IllegalArgumentException();
        }

        for (int i = 0; i < points.length - 1; ++i) {
            for (int j = i + 1; j < points.length; ++j) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
            }
        }

        Point[] points1 = Arrays.copyOf(points, points.length);

        Arrays.sort(points1, 0, points1.length);

        Stack<LineSegment> lineSegmentStack = new Stack<LineSegment>();

        for (int i = 0; i < points1.length; ++i) {
            Point[] tmpPoints = Arrays.copyOfRange(points1, i + 1, points1.length);

            Arrays.sort(tmpPoints, 0, tmpPoints.length, points1[i].slopeOrder());

            int pCnt = 0;
            double ang1 = 0, ang2;
            for (int j = 0; j < tmpPoints.length; ++j) {
                if (pCnt == 0) {
                    ang1 = points1[i].slopeTo(tmpPoints[j]);
                    pCnt++;
                }
                else {
                    ang2 = points1[i].slopeTo(tmpPoints[j]);
                    if (ang1 == ang2) {
                        pCnt++;
                    }
                    else {
                        if (pCnt >= 3) {
                            lineSegmentStack.push(new LineSegment(points1[i], tmpPoints[j - 1]));
                            pCnt = 0;
                        }
                        pCnt = 1;
                        ang1 = ang2;
                    }
                }

                if ((pCnt >= 3) && (j == tmpPoints.length - 1)) {
                    lineSegmentStack.push(new LineSegment(points1[i], tmpPoints[j]));
                    pCnt = 0;
                }
            }
        }

        ls = new LineSegment[lineSegmentStack.size()];

        for (int i = 0; i < ls.length; ++i) {
            ls[i] = lineSegmentStack.pop();
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
