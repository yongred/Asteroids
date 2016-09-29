package cisc3120.asteroids;

import java.awt.*;

/**
 * Models a polygon in 2-D space. This is a bit different from the
 * <code>Polygon</code> class Java provides&mdash; use
 * that class when actually want to draw this polygon on the screen!
 * <p>
 * graphically by a polygon. This class models the graphics-ish aspects of a polygon; by extending it, it can create classes that have all this useful polygon behavior plus additional, special, behavior.
 * <p>
 * A polygon is described by three components: its shape, its offset, and its
 * rotation.
 * <p>
 * The <b>shape</b> is defined by a sequence of points. list
 * the points "in order" like draw it without lifting up the pencil. If
 * the sequence of points doesn't form a polygon, various calculations (and drawing) simply won't work. When give the shape, it's only the
 * <i>relative positions</i> of the points that matters. In other words:
 * {(0,1),(1,1),(1,0)} is the same shape as {(9,10),(10,10),(10,9)}.
 * <p>
 * The <b>offset</b> describes where the polygon is located in space--it's the
 * position of the top-left boundary of the polygon.
 * <p>
 * Finally, the <b>rotation</b> describes the rotation of the polygon, in
 * degrees. In Java geometry, 0 degrees corresponds to "east", and positive
 * rotations go clockwise (so 90 degrees is "south").
 * <p>
 * Usage: The idea is that a polygon is created with a particular shape which is
 * never changed. Its offset and rotation, though, can be changed via the
 * <code>move()</code> and <code>rotate()</code> methods. This class can be
 * extended to create polygon-shaped game elements with additional
 * characteristics and behaviors (like, say, asteroids and ships).
 * 
 *
 */
class Polygon {
	protected Point[] shape; // An array of points.
	protected Point offset; // The offset mentioned above
	protected double rotation; // Zero degrees is "east."

	/*
	 * The constructor 'stores' the shape as being at the origin; anytime we're
	 * asked where the actual object is, we'll calculate the 'real' points based
	 * on the offset and rotation
	 */

	/**
	 * Creates internal representation of a polygon. The main work is storing
	 * this polygon's shape as a sequence of points located at the origin of the
	 * coordinate system.
	 * 
	 * @param inShape
	 *            array of Points that defines the polygon's shape
	 * @param inOffset
	 *            polygon's offset, as explained above
	 * @param inRotation
	 *            polygon's rotation, as explained above
	 */
	public Polygon(Point[] inShape, Point inOffset, double inRotation) {
		shape = inShape;
		offset = inOffset;
		rotation = inRotation;

		// First, we find the shape's top-most left-most boundary, its "origin"

		Point origin = (Point) shape[0].clone(); // start with the first point
		// as
		// candidate origin -- why do we
		// clone the object?

		// then look through the points looking for smaller values of x and y

		for (Point p : shape) {
			if (p.x < origin.x) {
				origin.x = p.x;
			}
			if (p.y < origin.y) {
				origin.y = p.y;
			}
		}

		// Now 'move' the shape so that it's at the 'real' origin, (0,0)

		for (Point p : shape) {
			p.x -= origin.x;
			p.y -= origin.y;
		}
		
	}

	/**
	 * A convenience constructor which takes a sequence of points and puts the
	 * polygon at the origin with rotation 0.
	 * 
	 * @param inShape
	 *            array of Points that defines this polygon's shape
	 */
	public Polygon(Point[] inShape) {
		this(inShape, new Point(0, 0), 0.0);
	}

	/**
	 * Rotates the polygon.
	 * 
	 * @param degrees
	 *            Number of degrees by which to rotate this polygon clockwise.
	 */
	public void rotate(double degrees) {
		rotation = (rotation + degrees) % 360.0;
	}

	/**
	 * Moves the polygon by adjusting its current offset.
	 * 
	 * @param x
	 *            value by which to increment this polygon's x-offset
	 * @param y
	 *            value by which to increment this polygon's y-offset
	 */

	public void move(double x, double y) {
		offset.x += x;
		offset.y += y;
	}

	/**
	 * This describes how to draw a Polygon on the screen.
	 * 
	 * @param brush
	 *            The object that will do the drawing.
	 * @see java.awt.Graphics#drawPolygon(int[], int[], int)
	 */

	public void paint(Graphics brush) {

		// to use Graphics.drawPolygon(), get the points of the Polygon's
		// location and re-wrap them for drawPolygon()

		int xArr[] = new int[shape.length];
		int yArr[] = new int[shape.length];

		// here's where we need to know the actual position of this Polygon in
		// space
		Point[] truePoints = getPoints();

		// see the drawPolygon() documentation for why we do this.
		for (int i = 0; i < shape.length; i++) {
			xArr[i] = (int) truePoints[i].x;
			yArr[i] = (int) truePoints[i].y;
		}
		
		brush.drawPolygon(xArr, yArr, shape.length);
		//brush.fillPolygon(xArr, yArr, shape.length);
	}
	
	//paint funtion to fill instead of draw
	public void paintFill(Graphics brush){
		int xArr[] = new int[shape.length];
		int yArr[] = new int[shape.length];

		// here's where we need to know the actual position of this Polygon in
		// space
		Point[] truePoints = getPoints();

		// see the drawPolygon() documentation for why we do this.
		for (int i = 0; i < shape.length; i++) {
			xArr[i] = (int) truePoints[i].x;
			yArr[i] = (int) truePoints[i].y;
		}
		
		brush.fillPolygon(xArr, yArr, shape.length);
	}

	/**
	 * Computes the actual location of this polygon in 2-D space by applying the
	 * current offset and rotation to the shape.
	 * 
	 * @return array of Points describing the actual position of the polygon
	 */
	public Point[] getPoints() {
	    Point center = findCenter();
	    Point[] points = new Point[shape.length];

	    for (int i = 0; i < shape.length; i++) {
	        double x = ((shape[i].x - center.x) * Math.cos(Math
	                .toRadians(rotation)))
	                - ((shape[i].y - center.y) * Math.sin(Math
	                        .toRadians(rotation))) +  offset.x;
	        double y = ((shape[i].x - center.x) * Math.sin(Math
	                .toRadians(rotation)))
	                + ((shape[i].y - center.y) * Math.cos(Math
	                        .toRadians(rotation))) + offset.y;
	        points[i] = new Point(x, y);
	    }
	    return points;
	}

	/**
	 * Determine whether this polygon contains a given <code>Point</code>.
	 * "ray-casting" algorithm</a>.
	 * 
	 * @param point
	 *            the possibly-interior point to test
	 * @return true if <code>point</code is contained in this polygon
	 */

	public boolean contains(Point point) {
		Point[] points = getPoints();
		double crossingNumber = 0;
		for (int i = 0, j = 1; i < shape.length; i++, j = (j + 1)
				% shape.length) {
			if ((((points[i].x < point.x) && (point.x <= points[j].x)) || ((points[j].x < point.x) && (point.x <= points[i].x)))
					&& (point.y > points[i].y + (points[j].y - points[i].y)
							/ (points[j].x - points[i].x)
							* (point.x - points[i].x))) {
				crossingNumber++;
			}
		}
		return crossingNumber % 2 == 1;
	}

	/**
	 * Helper method for computing the centroid of
	 * this polygon</a>. This relies on computing the area of the polygon.
	 *
	 * @return the <code>Point</code> at the center of this polygon
	 * @see #findArea()
	 */

	private Point findCenter() {
		Point sum = new Point(0, 0);
		for (int i = 0, j = 1; i < shape.length; i++, j = (j + 1)
				% shape.length) {
			sum.x += (shape[i].x + shape[j].x)
					* (shape[i].x * shape[j].y - shape[j].x * shape[i].y);
			sum.y += (shape[i].y + shape[j].y)
					* (shape[i].x * shape[j].y - shape[j].x * shape[i].y);
		}
		double area = findArea();
		return new Point(Math.abs(sum.x / (6 * area)), Math.abs(sum.y
				/ (6 * area)));
	}

	

	/**
	 * Helper method for computing the <a
	 * href="http://en.wikipedia.org/wiki/Polygon#Area_and_centroid">area</a> of
	 * this polygon.
	 * 
	 * @return The area of this polygon.
	 */
	private double findArea() {
		double sum = 0;
		for (int i = 0, j = 1; i < shape.length; i++, j = (j + 1)
				% shape.length) {
			sum += shape[i].x * shape[j].y - shape[j].x * shape[i].y;
		}
		return Math.abs(sum / 2);
	}

}