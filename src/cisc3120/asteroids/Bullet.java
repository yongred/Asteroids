package cisc3120.asteroids;

public class Bullet extends Polygon{
	
	private int velX, velY;
	private boolean used;

	public Bullet(Point[] inShape, Point inOffset, double inRotation)
	{
		super(inShape,  inOffset, inRotation);
	}
	
	public Bullet(Point[] inShape)
	{
		super(inShape);
	}

	public int getVelX() {
		return velX;
	}

	public void setVelX(int velX) {
		this.velX = velX;
	}

	public int getVelY() {
		return velY;
	}

	public void setVelY(int velY) {
		this.velY = velY;
	}
	
	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}
	
	public boolean isHit(Polygon p){
		if(p.contains(this.offset))
			return true;
		
		return false;
	}
	
}
