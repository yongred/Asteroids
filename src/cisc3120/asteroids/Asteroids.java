package cisc3120.asteroids;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;


/**
 * Class which controls the game logic specific to Asteroids. Because it extends
 * <code>Game</code>, this automatically "redraws" itself every hundredth of a
 * second.  the constructor, create every object that needed at the beginning of the game (ships,
 * asteroids...), and the <code>paint()</code> method, describe
 * how to paint the game each time the screen is refreshed. 
 *
 */
class Asteroids extends Game {

	int life = 3;
	int level = 1;
	static final int EXPLODE_MAX = 60; //how big explode animation
	static final int EXPLODE_INIT = 5;  //initial explosion animation length
	int explodeLen; //current exploding length
	ArrayList<Point> explodePos = new ArrayList<Point>();
	int renderTextTime = 0;
	//ArrayList<Ship> ships = new ArrayList<Ship>();
	 private Ship curShip;
	 private boolean shooted;
	 private int meteorNum;
	 private boolean gameOver;
	 private ArrayList<Meteor> meteors;
	 private Random rand;
	 boolean accelerating = false;
	 boolean readyInit = false; //init first them paint, else nothing to paint
	 
	 
	/**
	 * Populate the game. calls the <code>Game</code>
	 * constructor to set things up, and also defines a <code>Point</code> at the center of the game.  
	 * the last thing the constructor does is invoke <code>repaint()</code>
	 * 
	 */
	static final int WIDTH=800;
	static final int HEIGHT=600;

	public Asteroids() {
		super("Asteroids!", WIDTH, HEIGHT);
		init();
		addKeyListener(new KeyBoard(this));
		
		 run();
		repaint();
	}
	
	public void init(){
		shooted = false;
		gameOver = false;
		on = true;
		meteorNum = 5;
		meteors = new ArrayList<Meteor>();
		rand = new Random();
		initShip();
		initMeteors();
		
		readyInit = true;
	}

	//Asteriods game loop
	@Override 
	public void run(){
		long lastTime= System.nanoTime();
		double amtOfTicks= 60.0;
		double ns= 1_000_000_000/ amtOfTicks;
		double delta=0;
		int updates= 0;
		int frames= 0;
		long timer= System.currentTimeMillis();
		
		while(on)
		{
			long now= System.nanoTime();
			delta+= (now- lastTime)/ ns;
			lastTime= now;
			
			if(delta>=1)
			{
				tick();
				updateStates();
				delta--;
				updates++;
			}
			render();
			frames++;
			
			if(System.currentTimeMillis()- timer > 1000)
			{
				timer+= 1000;
				System.out.println(updates + " ticks, fps " + frames);
				updates=0;
				frames=0;
			}
		}
		
	}
	
	public void tick()
	{
	
		if(!meteors.isEmpty()){
			//needs a copy of arraylist in order for remove/add methods in arraylist to work
			//since the elements is removed, iterator does not iterate next element correctly
			
			for(Meteor m : meteors)
			{
				m.tick();
				
				if(m.isCollide(curShip)){
					//gameOver = true;
					//on = false;
					if(curShip.getInvincible() <= 0){
						curShip.setAlive(false);
						System.out.println("meteor.isCollide");
					}
				}
				else if(curShip.isCollide(m)){
					//gameOver = true;
					//on = false;
					if(curShip.getInvincible() <= 0){
						curShip.setAlive(false);
						System.out.println("meteor.isCollide");
					}
				}
				
				//needs a copy of arraylist in order for remove/add methods in arraylist to work
				//since the elements is removed, iterator does not iterate next element correctly
				
				for(Bullet b : curShip.getBullets()){
					if(b.isHit(m)){
						//meteors.remove(m);
						//ship.getBullets().remove(b);
						m.setExploded(true);
						b.setUsed(true);
					}
				}
				
				if(m.offset.x< 0){
					m.offset.x= WIDTH;
				}
				
				if(m.offset.x> WIDTH){
					m.offset.x= 0;
				}
				if(m.offset.y< 0){
					m.offset.y= HEIGHT;
				}
				if(m.offset.y> HEIGHT){
					m.offset.y= 0;
				}
			}
		}
		
		if(accelerating)
			curShip.accelerate();
		else{
			curShip.decelerate();
		}
		
		curShip.rotate(curShip.getVelRotation());
		curShip.matchBackFireRotate();
		curShip.thrust();
		
		if(!curShip.getBullets().isEmpty())
		{
			
			for(Bullet b : curShip.getBullets()){
				b.move(b.getVelX(), b.getVelY());
				
				if(b.offset.x< 0 || b.offset.x> WIDTH 
				   || b.offset.y < 0 || b.offset.y >HEIGHT){
					//ship.getBullets().remove(b);
					b.setUsed(true);
				}
			}
		}
		
		if(curShip.offset.x< 0)
		{
			curShip.offset.x= WIDTH;
		}else if(curShip.offset.x> WIDTH)
		{
			curShip.offset.x= 0;
		}if(curShip.offset.y< 0)
		{
			curShip.offset.y= HEIGHT;
		}else if(curShip.offset.y> HEIGHT)
		{
			curShip.offset.y= 0;
		}
		
		if(gameOver)
			on = false;
	}

	public void render()
	{
		super.update(getGraphics());
	}
	
	public void keyPressed(KeyEvent e)
	{
		int key= e.getKeyCode();
		
		if(key== KeyEvent.VK_W){
			accelerating = true;
			curShip.accelerate();
			curShip.thrust();
		}
		else if(key== KeyEvent.VK_A){
			
			curShip.setVelRotation(-3);
			curShip.rotate(curShip.getVelRotation());
			
			curShip.updateDir();
			curShip.matchBackFireRotate();
		}
		else if(key== KeyEvent.VK_D){
			
			curShip.setVelRotation(3);
			curShip.rotate(curShip.getVelRotation());
			
			curShip.updateDir();
			curShip.matchBackFireRotate();
		}
		else if(key== KeyEvent.VK_SPACE && !shooted){
			curShip.updateDir();
			shooted = true;
			curShip.shoot();
		}
		
	}
	
	public void keyReleased(KeyEvent e)
	{
		int key= e.getKeyCode();
		
		if(key== KeyEvent.VK_W)
		{
			accelerating = false;
			curShip.decelerate();
			
		}
		else if(key== KeyEvent.VK_A)
		{
			//turning = false;
			curShip.updateDir();
			curShip.setVelRotation(0);
			curShip.rotate(curShip.getVelRotation());
			curShip.matchBackFireRotate();
		}
		else if(key== KeyEvent.VK_D)
		{
			//turning = false;
			curShip.updateDir();
			curShip.setVelRotation(0);
			curShip.rotate(curShip.getVelRotation());
			curShip.matchBackFireRotate();
		}else if(key== KeyEvent.VK_SPACE){
			shooted = false;
		}
	}
	
	/**
	 * Paints the game. 
	 * 
	 * @see java.awt.Canvas#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics brush) {
		if(!readyInit){
			return;
		}
		brush.setColor(Color.black);
		brush.fillRect(0, 0, width, height);
		brush.setColor(new Color(190, 120, 255));
		
		brush.setFont(new Font("Serif", Font.BOLD, 20));
		brush.drawString("LIFE: " + life, 20, 20);
		
		if(gameOver){
			brush.setFont(new Font("Serif", Font.BOLD, 50));
			brush.drawString("GAME OVER!!!", width/2 - 150, height/2);
		}
		// draw invincible period
		if(curShip.getInvincible() > 0){
			curShip.paintInvincible(brush);
			curShip.setInvincible(curShip.getInvincible() -1);
		}
		curShip.paint(brush);
		
		if(accelerating)
			curShip.paintFire(brush);
		
		if(!curShip.getBullets().isEmpty()){
			for(Bullet b : curShip.getBullets()){
				b.paint(brush);
			}
		}
		
		
		for(Meteor m : meteors)
		{
			m.paint(brush);
		}
		
		if(renderTextTime > 0 && renderTextTime <= 100){
			brush.setFont(new Font("Serif", Font.BOLD, 40));
			brush.drawString("LEVEL: " + level, width/2 - 80, height/2);
			renderTextTime++;
		}else{
			renderTextTime = 0;
		}
		
		if(!explodePos.isEmpty()){
			if(explodeLen <= EXPLODE_MAX){
				paintExplode(brush);
				explodeLen++;
			}
			else{
				explodePos.clear();
				explodeLen = EXPLODE_INIT;
			}
		}
		
	}
	
	public void paintExplode(Graphics brush){
		
		Graphics2D g2d = (Graphics2D) brush;
		g2d.setStroke(new BasicStroke(10));
		ArrayList<Point> copy = new ArrayList<Point>( explodePos);
		for(Point pos : copy){
			//for(int i=explodeInit; i< EXPLODE_MAX; i++){
			g2d.drawOval((int)pos.x, (int)pos.y, explodeLen, explodeLen);
			//}
		}
		
	}
	
	public void initShip()
	{
		Point center = new Point(WIDTH / 2, HEIGHT / 2);
		
		Point [] shipShape= new Point[4];
		
		double shipXp[]={0,10,0,-10};
		double shipYp[]={0,35,30,35};
		
		for(int i=0; i<4; i++)
		{
			shipShape[i]= new Point(shipXp[i], shipYp[i]);
		}
		
		curShip = new Ship(shipShape, center, 0.0);
		life--;
	}
	
	
	public void initMeteors()
	{
		ArrayList<Point> meteorPositions = new ArrayList<Point>();
		Point [] meteorShape= new Point[7];
		double Xpoints[]={0, 30, 90, 120, 120, 80, 30};
		double Ypoints[]={0, -60, -60, -20, 30, 60, 60 };
		
		for(int i=0; i<meteorNum; i++)
		{
			meteorPositions.add(new Point(rand.nextInt()%WIDTH, rand.nextInt()%HEIGHT));
		}
		
		
		for(int i=0; i< meteorShape.length; i++)
		{
			meteorShape[i]= new Point(Xpoints[i], Ypoints[i]);
		}
		
		for(int x=0; x< meteorNum; x++)
		 {
			 meteors.add(new Meteor(meteorShape, meteorPositions.get(x))); 
		 }
		
	}
	
	//small meteors
	public void generateSmallMeteors(Meteor m){
		Point [] meteorShape= new Point[7];
		double Xpoints[]={0, 10, 30, 40, 40, 27, 10};
		double Ypoints[]={0, -20, -20, -8, 10, 20, 20 };
		
		for(int i=0; i< meteorShape.length; i++)
		{
			meteorShape[i]= new Point(Xpoints[i], Ypoints[i]);
		}
		meteors.add(new Meteor(meteorShape, m.offset, false));
	}
	
	private void nextLevel() {
		meteorNum++;
		meteors.clear();
		curShip.getBullets().clear();
		initMeteors();
		level++;
	}
	
	private void updateStates(){
		//needs a copy of arraylist in order for remove/add methods in arraylist to work
		//since the elements is removed, iterator does not iterate next element correctly
		ArrayList<Meteor> mCopy = new ArrayList<Meteor>(meteors);
		ArrayList<Bullet> bsCopy = new ArrayList<Bullet> (curShip.getBullets());		
		if(!curShip.isAlive()){
			explodePos.add(curShip.offset);
			if(life > 0){
				initShip();
			}
			else{
				gameOver = true;
			}
		}
		for(Meteor m : mCopy){
			if(m.isExploded()){
				explodePos.add(m.offset);
				meteors.remove(m);
				if(m.containSmallMeteor()){
					generateSmallMeteors(m);
				}
			}
		}
		
		for(Bullet b: bsCopy){
			if(b.isUsed()){
				curShip.getBullets().remove(b);
			}
		}
		
		if(mCopy.isEmpty()){ //destroyed all meteors
			nextLevel();
			renderTextTime++; //start render next level text
		}
	}
	
	
	/**
	 *  here's where everything gets started: invoking the
	 * <code>Asteroids</code> constructor.
	 * 
	 * @param args
	 *            Unused.
	 */
	public static void main(String[] args) {
		new Asteroids();
	}
}