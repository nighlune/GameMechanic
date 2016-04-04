import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class MechanicFrame extends JFrame implements KeyListener, ActionListener
{	
	static int playerSpeed = 10;
	static int gameSpeed = 50;	
	static int frameXSize = 1200;
	static int frameYSize = 700;	
	static int collisionRadius = 300;
	static int directionSpeed = 20;
	
	Random generator = new Random();
	
	ArrayList<Entity> entities;
	
	//game objects
	Player player;	
	Enemy enemy1;
	Enemy enemy2;
	Enemy enemy3;	
	Base base;
	Trigger trigger;
	
	//booleans for multiple key-presses
	boolean left = false;
	boolean right = false;
	boolean up = false;
	boolean down = false;
	
	//initial starting points
	Point pInitial;
	Point e1Initial;
	Point e2Initial;
	Point e3Initial;
	
	//background
	Rectangle2D.Double background;
	
	//mechanic variables
	int enemyCounter;	
	boolean baseCollision;
	int direction;
	int changeDirectionCounter;
	
	//constructor
	public MechanicFrame()
	{
		addKeyListener(this);
		
		entities = new ArrayList<Entity>();		
		
		//initialize player
		pInitial = new Point(50, (int)(frameYSize * .5));
		player = new Player(pInitial);
		
		//initialize enemies
		e1Initial = new Point(frameXSize, (int)(frameYSize * .5));
		e2Initial = new Point(new Point((int)(frameXSize * .5), -100));
		e3Initial = new Point(new Point((int)(frameXSize * .5), frameYSize + 100));
		enemy1 = new Enemy(e1Initial);
		enemy1.isActive = true;
		enemy2 = new Enemy(e2Initial);
		enemy3 = new Enemy(e3Initial);
		
		//initialize base and trigger
		base = new Base(new Point(frameXSize - 200, (int)(frameYSize * .5)));
		trigger = new Trigger(new Point((int)(frameXSize * .25) - 100, (int)(frameYSize * .5)));
		
		background = new Rectangle2D.Double(0, 0, frameXSize, frameYSize);
		
		//add the enemies to the array list
		entities.add(enemy1);
		entities.add(enemy2);
		entities.add(enemy3);
		
		enemyCounter = 0;		
		baseCollision = false;
		changeDirectionCounter = 0;
		direction = 0;
		
		Timer t = new Timer(gameSpeed, this);
		t.start();
	}
	
	//main
	public static void main(String[] args)
	{
		MechanicFrame mechanicFrame = new MechanicFrame();		
		mechanicFrame.setSize(frameXSize, frameYSize);
		mechanicFrame.setVisible(true);
		mechanicFrame.setResizable(false);
		mechanicFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);		
	}

	@SuppressWarnings("static-access")
	@Override
	public void actionPerformed(ActionEvent e)
	{		
		updateKeyPress();
		
		update();
		
		//check boundaries
		checkBounds(player);
		
		for(int i = 0; i < entities.size(); i++)
		{
			if(entities.get(i).isActive == true)
			{
				checkBounds(entities.get(i));
			}
		}		
		
		//update the draw locations
		//for the base
		base.collisionBody.setFrame(base.point.xCoord, base.point.yCoord, (int)base.collisionBody.getHeight(), (int)base.collisionBody.getHeight());
		
		//for the trigger
		trigger.collisionBody.setFrame(trigger.point.xCoord, trigger.point.yCoord, (int)trigger.collisionBody.getHeight(), (int)trigger.collisionBody.getHeight());
		
		//for each enemy
		for(int i = 0; i < entities.size(); i++)
		{
			if(entities.get(i).isActive)
			{
				entities.get(i).collisionZone.setFrame(entities.get(i).point.xCoord - (enemy1.radius * .5) + (enemy1.entitySize * .5), entities.get(i).point.yCoord - (enemy1.radius * .5) + (enemy1.entitySize * .5), enemy1.collisionZone.getWidth(), enemy1.collisionZone.getHeight());
				entities.get(i).body.setFrame(entities.get(i).point.xCoord, entities.get(i).point.yCoord, enemy1.body.getWidth(), enemy1.body.getHeight());
				entities.get(i).collisionBody.setFrame(entities.get(i).point.xCoord, entities.get(i).point.yCoord, enemy1.collisionSize, enemy1.collisionSize);
			}
		}
		
		//for the player
		player.body.setFrame(player.point.xCoord, player.point.yCoord, (int)player.body.getHeight(), (int)player.body.getHeight());
		player.collisionBody.setFrame(player.point.xCoord, player.point.yCoord, player.collisionSize, player.collisionSize);
		
		repaint();
	}
	
	public void paint(Graphics g)
	{
		Graphics2D brush = (Graphics2D)g;
		
		//paint the background
		brush.setColor(Color.WHITE);
		brush.fill(background);
		
		//paint the base
		brush.setColor(Color.YELLOW);
		brush.fill(base.collisionBody);
		
		//paint the trigger
		brush.setColor(Color.GREEN);
		brush.fill(trigger.collisionBody);
		
		//paint the enemies/collision zones
		brush.setColor(Color.RED);		
		for(int i = 0; i < entities.size(); i++)
		{
			if(entities.get(i).isActive)
			{
				brush.draw(entities.get(i).collisionZone);
				brush.fill(entities.get(i).body);
			}
		}
		
		//paint the player
		brush.setColor(Color.BLACK);
		brush.fill(player.body);		
	}
	
	//called by action performed
	public void update()
	{
		//core game logic/enemy movement
		
		//spawn new enemy if applicable
		if(enemyCounter == 1)
		{
			entities.get(enemyCounter).isActive = true;
		}
		
		if(enemyCounter == 2)
		{
			entities.get(enemyCounter).isActive = true;
		}
		////////////////////////////////
		
		
		
		//base/win condition (causing new enemies to spawn
		if(player.checkCollision(base))
		{			
			if(!baseCollision)
			{
				enemyCounter++;
			}
			
			baseCollision = true;
		}
		
		else
		{
			baseCollision = false;
		}
		///////////////////////////////////////////////////
		
		
		//reset condition (check for player collision with enemies
		if(checkEnemyCollision())
		{
			player.reset();
			
			for(int i = 0; i < entities.size(); i++)
			{
				if(entities.get(i).isActive == true)
				{
					entities.get(i).reset();
				}
			}
		}
		////////////////////////////////////////////////////
		
		
		//enemy movement		
		for(int i = 0; i < entities.size(); i++)
		{
			if(entities.get(i).isActive == true)
			{
				//if the player is within the "aggro zone"
				if((Math.abs(distance(player.point, entities.get(i).point)) <= collisionRadius * .5))
				{
					entities.get(i).pursue(player.point);
				}
				
				//if the player touches the trigger
				else if(player.checkCollision(trigger))
				{
					//TODO check to see which enemy is closest to the base to have them follow the player
					entities.get(i).pursue(trigger.point);
				}				
		
				//otherwise choose random movement
				else
				{					
					entities.get(i).move(player.point, base.point);
				}
			}
		}
		//////////////////////////////////////

		
		//change the direction of the enemies
		if(changeDirectionCounter % directionSpeed == 0)
		{
			for(int i = 0; i < entities.size(); i++)
			{
				entities.get(i).direction = generator.nextInt(9);
			}			
		}
		//////////////////////////////////////
		
		changeDirectionCounter++;
	}

	@Override
	public void keyTyped(KeyEvent e){}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyChar() == 'q')
		{
			System.exit(0);
		}
		
		if(e.getKeyChar() == 'w')
		{
			up = true;
		}
		
		if(e.getKeyChar() == 'a')
		{
			left = true;
		}
		
		if(e.getKeyChar() == 'd')
		{
			right = true;
		}
		
		if(e.getKeyChar() == 's')
		{
			down = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyChar() == 'w')
		{
			up = false;
		}
		
		if(e.getKeyChar() == 'a')
		{
			left = false;
		}
		
		if(e.getKeyChar() == 'd')
		{
			right = false;
		}
		
		if(e.getKeyChar() == 's')
		{
			down = false;
		}
	}
	
	public void updateKeyPress()
	{
		if(up)
		{
			player.point.yCoord -= playerSpeed;
		}
		
		if(left)
		{
			player.point.xCoord -= playerSpeed;
		}
		
		if(right)
		{
			player.point.xCoord += playerSpeed;
		}
		
		if(down)
		{
			player.point.yCoord += playerSpeed;
		}
		
		if(up && right)
		{
			player.point.yCoord -= (playerSpeed * .1);
			player.point.xCoord += (playerSpeed * .1);
		}
		
		if(up && left)
		{
			player.point.yCoord -= (playerSpeed * .1);
			player.point.xCoord -= (playerSpeed * .1);
		}
		
		if(down && right)
		{
			player.point.yCoord += (playerSpeed * .1);
			player.point.xCoord += (playerSpeed * .1);
		}
		
		if(down && left)
		{
			player.point.yCoord += (playerSpeed * .1);
			player.point.xCoord -= (playerSpeed * .1);
		}
	}
	
	public boolean checkEnemyCollision()
	{		
		for(int i = 0; i < entities.size(); i++)
		{
			if(player.checkCollision(entities.get(i)) && entities.get(i).isActive == true)
			{				
				return true;
			}
		}
		
		return false;
	}
	
	public void checkBounds(Entity entity)
	{	
		if(entity.point.xCoord > frameXSize)
			entity.point.xCoord = 0;
		
		if(entity.point.xCoord < 0)
			entity.point.xCoord = frameXSize;
		
		if(entity.point.yCoord > frameYSize)
			entity.point.yCoord = 0;
		
		if(entity.point.yCoord < 0)
			entity.point.yCoord = frameYSize;
	}
	
	public double distance(Point p1, Point p2)
	{
		double a = (double)(p2.xCoord - p1.xCoord);
		double b = (double)(p2.yCoord - p1.yCoord);
		
		return Math.sqrt(a * a + b * b);
	}
}
