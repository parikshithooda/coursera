import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class KdTree {

    private static class Node {
        Point2D point2D;
        Node left;
        Node right;
        RectHV rect;
        int level;

        public Node(Point2D point2D, int level, RectHV rect) {
            this.point2D = point2D;
            this.level = level;
            this.rect = rect;
            this.left = null;
            this.right = null;
        }
        public String toString() {
            return point2D.toString();
        }
    }

    private Node root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (contains(p)) return;

        Node newNode = new Node(p, 0, new RectHV(0, 0, 1, 1));
        if (root == null) {
            root = newNode;
        }
        else {
            Node current = root;
            while (true) {
                newNode.level = current.level + 1;
                if (current.level % 2 == 0) {
                    // Vertical line division
                    if (p.x() < current.point2D.x()) {
                        if (current.left == null) {
                            newNode.rect = new RectHV(current.rect.xmin(), current.rect.ymin(), current.point2D.x(), current.rect.ymax());
                            current.left = newNode;
                            break;
                        } else {
                            current = current.left;
                        }
                    } else {
                        if (current.right == null) {
                            newNode.rect = new RectHV(current.point2D.x(), current.rect.ymin(), current.rect.xmax(), current.rect.ymax());
                            current.right = newNode;
                            break;
                        } else {
                            current = current.right;
                        }
                    }
                } else {
                    // Horizontal line division
                    if (p.y() < current.point2D.y()) {
                        if (current.left == null) {
                            newNode.rect = new RectHV(current.rect.xmin(), current.rect.ymin(), current.rect.xmax(), current.point2D.y());
                            current.left = newNode;
                            break;
                        } else {
                            current = current.left;
                        }
                    } else {
                        if (current.right == null) {
                            newNode.rect = new RectHV(current.rect.xmin(), current.point2D.y(), current.rect.xmax(), current.rect.ymax());
                            current.right = newNode;
                            break;
                        } else {
                            current = current.right;
                        }
                    }
                }
            }
        }
        size++;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        Node currentNode = root;
        while (currentNode != null) {
            if (currentNode.point2D.equals(p)) return true;
            if (currentNode.level % 2 == 0) {
                // Vertical line division
                if (p.x() < currentNode.point2D.x()) currentNode = currentNode.left;
                else currentNode = currentNode.right;
            } else {
                // Horizontal line division
                if (p.y() < currentNode.point2D.y()) currentNode = currentNode.left;
                else currentNode = currentNode.right;
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        Node[] nodes = new Node[size];
        int current = 0;
        int ind = 0;
        nodes[ind++] = root;

        while (current < size && nodes[current] != null) {
            StdDraw.setPenColor(StdDraw.BLACK); StdDraw.setPenRadius(0.01);
            StdDraw.point(nodes[current].point2D.x(), nodes[current].point2D.y());
            if (nodes[current].left != null) nodes[ind++] = nodes[current].left;
            if (nodes[current].right != null) nodes[ind++] = nodes[current].right;

            if (nodes[current].level % 2 == 0) {
                StdDraw.setPenColor(StdDraw.RED); StdDraw.setPenRadius(0.001);
                StdDraw.line(
                        nodes[current].point2D.x(),
                        nodes[current].rect.ymin(),
                        nodes[current].point2D.x(),
                        nodes[current].rect.ymax()
                );
            } else {
                StdDraw.setPenColor(StdDraw.BLUE); StdDraw.setPenRadius(0.001);
                StdDraw.line(
                        nodes[current].rect.xmin(),
                        nodes[current].point2D.y(),
                        nodes[current].rect.xmax(),
                        nodes[current].point2D.y()
                );
            }
            current++;
        }
    }

    private int range(Node root, RectHV rect, ArrayList<Point2D> point2DS, int n) {
        if (root.rect.intersects(rect)) {
            if (rect.contains(root.point2D)) {
                point2DS.add(root.point2D);
            }
            if (root.left != null) n = range(root.left, rect, point2DS, n);
            if (root.right != null) n = range(root.right, rect, point2DS, n);
        }
        return n;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        ArrayList<Point2D> pointsInRect = new ArrayList<>();
        if (root != null)
            range(root, rect, pointsInRect, 0);

        return new Point2DIterable(pointsInRect);
    }

    private static class Point2DIterable implements Iterable<Point2D> {

        ArrayList<Point2D> point2DS;

        public Point2DIterable(ArrayList<Point2D> point2DS) {
            this.point2DS = point2DS;
        }
        @Override
        public Iterator<Point2D> iterator() {
            return new Iterator<>() {
                int current = 0;
                @Override
                public boolean hasNext() {
                    return current < point2DS.size();
                }
                @Override
                public Point2D next() {
                    if (!hasNext()) throw new NoSuchElementException();
                    return point2DS.get(current++);
                }
            };
        }
    }

    private Point2D nearest (Point2D p, Node root, Point2D nearest) {
        if (root == null) return null;
        if (nearest == null || root.point2D.distanceSquaredTo(p) < p.distanceSquaredTo(nearest))
            nearest = root.point2D;

        if (root.level % 2 == 0) {
            if (root.point2D.x() > p.x()) {
                if (root.left != null) nearest = nearest(p, root.left, nearest);
                if (root.right != null && root.right.rect.distanceSquaredTo(p) < p.distanceSquaredTo(nearest))
                    nearest = nearest(p, root.right, nearest);
            } else {
                if (root.right != null) nearest = nearest(p, root.right, nearest);
                if (root.left != null && root.left.rect.distanceSquaredTo(p) < p.distanceSquaredTo(nearest))
                    nearest = nearest(p, root.left, nearest);
            }
        } else {
            if (root.point2D.y() > p.y()) {
                if (root.left != null) nearest = nearest(p, root.left, nearest);
                if (root.right != null && root.right.rect.distanceSquaredTo(p) < p.distanceSquaredTo(nearest))
                    nearest = nearest(p, root.right, nearest);
            } else {
                if (root.right != null) nearest = nearest(p, root.right, nearest);
                if (root.left != null && root.left.rect.distanceSquaredTo(p) < p.distanceSquaredTo(nearest))
                    nearest = nearest(p, root.left, nearest);
            }
        }
        return nearest;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return nearest(p, root, null);
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        Point2D[] point2DS = new Point2D[10];
        for (int i = 0; i < 10; i++) {
            point2DS[i] = new Point2D(StdIn.readDouble(), StdIn.readDouble());
        }
        for (Point2D point2D: point2DS) {
            kdTree.insert(point2D);
        }
        kdTree.draw();
        for (Point2D point2D: kdTree.range(new RectHV(0.267578125, 0.3828125, 0.830078125, 0.75390625))) {
            StdOut.println(point2D);
        }
        kdTree.nearest(new Point2D(0.49, 0.74));
    }
}