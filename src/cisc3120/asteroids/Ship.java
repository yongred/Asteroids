package cisc3120.asteroids;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;


public class Ship extends Polygon{ 

	static final int MAX_SPEED = 7;  //max thrusting speed
	
	private double velY=0, velX=0;
	double dirX, dirY;
	private double velRotation=0.0;  // degree to rotate
	private double acceleration = 0.07;
	private double deceleration = 0.99;
	//double movingForceX= 0, movingForceY= 0;
	Polygon backFire;  //acceleration animation
	private boolean alive;
	private int invincible;

	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();

	public Ship(Point[] inShape, Point inOffset, double inRotation)
	{
		super(inShape,  inOffset, inRotation);
		
		updateDir();
		//initSpeed();
		initBackFire();
		alive = true;
		invincible = 300;
	}
	
	public Ship(Point[] inShape)
	{
		super(inShape);
		
		updateDir();
		initBackFire();
		alive = true;
		invincible = 300;
		//initSpeed();
	}
	
	public void shoot()
	{
		bullets.add(getBullet());
	}
	
	//direction of the ship pointing to
	public void updateDir()
	{	
		Point[] curPoint= getPoints();
		 dirY = (curPoint[0].y - curPoint[2].y);
		 dirX =  (curPoint[0].x - curPoint[2].x);
		
		//System.out.println("mag: " + magnitude);
		//System.out.println("xidr " + velX);
		//System.out.println("yidr " + velY);
		
	}
	
	//moving according to speed
	public void thrust(){
		
		super.move(velX, velY);
		//System.out.println("velx " + velX);
		//System.out.println("vely " + velY);
	}
	
	public void initSpeed(){
		double magnitude = Math.sqrt((dirX * dirX) + (dirY * dirY));
		velY = dirY / magnitude;
		velX = dirX / magnitude; 
	}
	
	public void accelerate(){
		double absX = Math.abs(velX);
		double absY = Math.abs(velY);
		//0 is north, 90 is east, -90 is west
		double radian = ((super.rotation-90) * Math.PI) / 180;
		if(absX < MAX_SPEED){
			velX += Math.cos(radian) * acceleration;
		}
		if(absY < MAX_SPEED){
			velY += Math.sin(radian) * acceleration;
		}
	}
	
	//slows down when release thrusting
	public void decelerate(){
		double absX = Math.abs(velX);
		double absY = Math.abs(velY);
		//0 is stop, - or + is moving, so check abs vals
		if(absX > 0.01 && absY > 0.01){
			velX *= deceleration;
		}
		else{
			velX = 0;
		}
		if(absY > 0.01){
			velY *= deceleration;
		}
		else{
			velY = 0;
		}

	}
	
	//create bullet
	public Bullet getBullet()
	{		
		Point bPoint= this.offset.clone();
		Point [] bShape= new Point[4];
		double bulletXp[]={0,2,2,0};
		double bulletYp[]={0,0,2,2};
		
		for(int i=0; i<4; i++)
		{
			bShape[i]= new Point(bulletXp[i], bulletYp[i]);
		}
		
		Bullet bullet= new Bullet(bShape, bPoint,  0);
		bullet.setVelX ((int)dirX /4);
		bullet.setVelY((int) dirY /4);
		return bullet;
	}
	
	public void paintFire(Graphics brush){
		backFire.paintFill(brush);
	}
	
	public void paintInvincible(Graphics brush){
		Graphics2D g2d = (Graphics2D) brush;
		g2d.setStroke(new BasicStroke(3));
		
		g2d.drawRect((int)this.offset.x - 20, (int)this.offset.y - 20, 40, 40);
		brush.drawString("Invincible",(int)this.offset.x - 40, (int)this.offset.y);
		g2d.setStroke(new BasicStroke(1));
	}
	
	//initialize the points of polygon backFire, shape of acceleration fire when ship thrust
	public void initBackFire(){
		Point [] bfPoints = new Point[4];
		int [] xs = {0, -5, 0, 5};
		int [] ys = {0, 10, 60, 10};
		
		for(int i=0; i< bfPoints.length; i++)
		{
			bfPoints[i]= new Point(xs[i], ys[i]);
		}
		
		Point bfOffset = new Point(this.offset.x - dirX, this.offset.y - dirY );
			
		backFire = new Polygon(bfPoints, this.offset, this.rotation);
	}
	
	//when ship rotate, backfire should rotate as well
	public void matchBackFireRotate(){
		backFire.rotate(velRotation);
	}
	
	public boolean isCollide(Polygon p){
		for(Point shipP : this.getPoints()){
			if(p.contains(shipP)){
				return true;
			}
		}
		
		return false;
	}
	
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
	
	public double getRiseY() 
	{
		return velY;
	}
	
	public void setRiseY(double riseY)
	{
		this.velY= riseY;
	}

	
	public double getRunX()
	{
		return velX;
	}
	
	public void setRunX(double runX)
	{
		this.velX= runX;
	}
	
	public double getSpeed(){
		return acceleration;
	}
	
	public void setSpeed(double speed){
		this.acceleration = speed;
	}

	public double getVelRotation() {
		return velRotation;
	}

	public void setVelRotation(double velRotation) {
		this.velRotation = velRotation;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public void setAlive(boolean alive){
		this.alive = alive;
	}
	
	public int getInvincible() {
		return invincible;
	}

	public void setInvincible(int invincible) {
		this.invincible = invincible;
	}

}
