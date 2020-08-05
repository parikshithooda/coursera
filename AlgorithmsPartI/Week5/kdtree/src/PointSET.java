import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> treeSet;

    // construct an empty set of points
    public PointSET() {
        this.treeSet = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return treeSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return treeSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (contains(p)) return;

        treeSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return treeSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point2D: treeSet) {
            StdDraw.point(point2D.x(), point2D.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        Point2D[] point2DS = new Point2D[treeSet.size()];
        int point2DSSize = 0;
        for (Point2D point2D: treeSet) {
            if (rect.contains(point2D)) {
                point2DS[point2DSSize++] = new Point2D(point2D.x(), point2D.y());
            }
        }
        return new Point2DIterable(point2DS, point2DSSize);
    }

    private static class Point2DIterable implements Iterable<Point2D> {
        Point2D[] point2DS;
        int point2DSSize;

        public Point2DIterable(Point2D[] point2DS, int point2DSSize) {
            this.point2DS = point2DS;
            this.point2DSSize = point2DSSize;
        }

        @Override
        public Iterator<Point2D> iterator() {
            return new Iterator<>() {
                int current = 0;

                @Override
                public boolean hasNext() {
                    return current < point2DSSize;
                }

                @Override
                public Point2D next() {
                    if (!hasNext()) throw new NoSuchElementException();
                    return point2DS[current++];
                }
            };
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        Point2D nearest = null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (Point2D point2D: treeSet) {
            if (p.distanceSquaredTo(point2D) < nearestDistance) {
                nearest = point2D;
                nearestDistance = p.distanceSquaredTo(point2D);
            }
        }
        if (nearest == null) return null;
        return new Point2D(nearest.x(), nearest.y());
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}