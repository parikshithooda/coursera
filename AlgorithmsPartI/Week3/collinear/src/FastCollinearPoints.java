import java.util.Arrays;

public class FastCollinearPoints {
    private final Point[] points;
    private int numberOfSegments;
    private LineSegment[] lineSegments;

    private class Line {
        Point p;
        Point q;

        public Line(Point p, Point q) {
            this.p = p;
            this.q = q;
        }
    }

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();

        this.points = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null) throw new IllegalArgumentException();
                if (points[i].compareTo(points[j]) == 0) {
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
        // Total possible ways to select 2 points from n.
        Line[] lines = new Line[(points.length * (points.length) - 1) / 2];

        Arrays.sort(points);
        for (int p = 0; p < points.length; p++) {
            Point origin = points[p];
            Point[] tempPoints = new Point[points.length - 1];
            for (int i = 0, j = 0; i < points.length; i++) {
                if (points[i].compareTo(origin) != 0)
                    tempPoints[j++] = points[i];
            }
            Arrays.sort(tempPoints, origin.slopeOrder());
            // Start line with just one point
            Point startingPoint = origin;
            Point endingPoint = origin;
            int numberOfPointsOnLine = 1;
            double slope = startingPoint.slopeTo(endingPoint);
            for (int q = 0; q < tempPoints.length; q++) {
                Point newPointToCheck = tempPoints[q];
                // When there is just one point in the line, i.e Origin OR slope is equal, add that point to the line
                if ((startingPoint == origin && endingPoint == origin) || startingPoint.slopeTo(newPointToCheck) == slope) {
                    if (newPointToCheck.compareTo(startingPoint) < 0) {
                        startingPoint = newPointToCheck;
                        numberOfPointsOnLine += 1;
                    } else if (newPointToCheck.compareTo(endingPoint) > 0) {
                        endingPoint = newPointToCheck;
                        numberOfPointsOnLine += 1;
                    } else if (newPointToCheck.compareTo(startingPoint) > 0 && newPointToCheck.compareTo(endingPoint) < 0) {
                        numberOfPointsOnLine += 1;
                    }
                } else {
                    if (numberOfPointsOnLine >= 4 && startingPoint.compareTo(origin) == 0) {
                        lines[numberOfSegments++] = new Line(startingPoint, endingPoint);
                    }
                    if (newPointToCheck.compareTo(origin) > 0) {
                        startingPoint = origin;
                        endingPoint = newPointToCheck;
                        numberOfPointsOnLine = 2;
                    } else if (newPointToCheck.compareTo(origin) < 0){
                        startingPoint = newPointToCheck;
                        endingPoint = origin;
                        numberOfPointsOnLine = 2;
                    } else {
                        // This else will never happen with current inputs, but including it in any case.
                        startingPoint = origin;
                        endingPoint = origin;
                        numberOfPointsOnLine = 1;
                    }
                }
                if (q == tempPoints.length - 1 && startingPoint.compareTo(origin) == 0) {
                    if (numberOfPointsOnLine >= 4) {
                        lines[numberOfSegments++] = new Line(startingPoint, endingPoint);
                    }
                }
                slope = startingPoint.slopeTo(endingPoint);
            }
        }

        this.lineSegments = new LineSegment[numberOfSegments];
        for (int lineInd = 0; lineInd < numberOfSegments(); lineInd++) {
            this.lineSegments[lineInd] = new LineSegment(lines[lineInd].p, lines[lineInd].q);
        }
    }
}
