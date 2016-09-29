package cisc3120.asteroids;

import java.util.Random;
 
public class Meteor extends Polygon{
	
	Random rand= new Random();
	double dirX = 0, dirY= 0;
	int speed = 2;
	private boolean exploded;
	private boolean containSmallMeteor;

	Meteor(Point [] inShape, Point inOffset)
	{
		super(inShape, inOffset, 0);
		randDir();
		exploded = false;
		containSmallMeteor = true;
	}
	
	Meteor(Point [] inShape, Point inOffset, boolean contain)
	{
		super(inShape, inOffset, 0);
		randDir();
		exploded = false;
		containSmallMeteor = contain;
	}
	
	public void tick()
	{
		move(dirX, dirY);
	}
	
	public void randDir(){
		//random 2 points for calculate random direction
		double y0 = rand.nextInt(1000)+1;
		double y1 = rand.nextInt(1000)+1;
		double x0 = rand.nextInt(1000)+1;
		double x1 = rand.nextInt(1000)+1;
		
		while(y0 == y1){
			y1 = rand.nextInt(1000)+1;
		}
		while(x0 == x1){
			x1 = rand.nextInt(1000)+1;
		}
		dirX = x0 - x1;
		dirY = y0 - y1;
		
		//unit vector for the dir min speed
		double magnitude = Math.sqrt((dirX * dirX) + (dirY * dirY));
		dirY = dirY / magnitude;
		dirX = dirX / magnitude; 
		
		//min dir speed multiply by speed
		dirX *= speed;
		dirY *= speed;
	}
	
	public boolean isCollide(Polygon p){
		for(Point m : this.getPoints()){
			if(p.contains(m)){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isExploded(){
		return exploded;
	}
	
	public void setExploded(boolean exploded){
		this.exploded= exploded;
	}
	
	public boolean containSmallMeteor() {
		return containSmallMeteor;
	}

	public void setContainSmallMeteor(boolean containSmallMeteor) {
		this.containSmallMeteor = containSmallMeteor;
	}

}
