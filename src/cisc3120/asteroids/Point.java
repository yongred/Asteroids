package cisc3120.asteroids;

/**
 * 
 * Models a point in 2-D space. I also use <code>Point</code> objects to
 * represent things which aren't quite objects; 
 *
 */
class Point implements Cloneable {
	double x, y;

	public Point(double inX, double inY) {
		x = inX;
		y = inY;
	}
	
	/** 
	 * make it easy to copy Points.
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Point clone() {
		try {
			return (Point) super.clone();
		} catch (CloneNotSupportedException e) {
			//  Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}