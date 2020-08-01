import java.util.Arrays;

public class BruteCollinearPoints {

    private final Point[] points;
    private int numberOfSegments;
    private LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();

        this.points = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null) throw new IllegalArgumentException();
                if (points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY) {
                    throw new IllegalArgumentException();
                }
            }

            this.points[i] = points[i];
        }
        this.numberOfSegments = 0;
        computeLineSegments();
    }

    public int numberOfSegments() {
        return numberOfSegments;
    }

    public LineSegment[] segments() {
        LineSegment[] resultLineSegments = new LineSegment[numberOfSegments];
        for (int i = 0; i < numberOfSegments; i++) {
            resultLineSegments[i] = this.lineSegments[i];
        }
        return resultLineSegments;
    }

    private void computeLineSegments() {
        LineSegment[] lineSegments = new LineSegment[points.length];
        for (int p = 0; p < points.length; p++) {
            for (int q = p + 1; q < points.length; q++) {
                for (int r = q + 1; r < points.length; r++) {
                    for (int s = r + 1; s < points.length; s++) {
                        double slopepq = points[p].slopeTo(points[q]);
                        double slopepr = points[p].slopeTo(points[r]);
                        double slopeps = points[p].slopeTo(points[s]);
                        if (slopepq == slopepr && slopepq == slopeps) {
                            Point[] pqrs = {points[p], points[q], points[r], points[s]};
                            Arrays.sort(pqrs);
                            lineSegments[numberOfSegments++] = new LineSegment(pqrs[0], pqrs[3]);
                        }
                    }
                }
            }
        }
        this.lineSegments = new LineSegment[numberOfSegments];
        for (int i = 0; i < numberOfSegments; i++) {
            this.lineSegments[i] = lineSegments[i];
        }
    }
}