import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Entity
{	
	static int frameXSize = 1200;
	static int frameYSize = 700;	
	static int enemySpeed = 7;	
	static int radius = 200;
	
	int entitySize;
	int collisionSize;
	
	Point point;
	Point iPoint;
	
	Ellipse2D.Double body;	
	Rectangle2D.Double collisionBody;	
	Ellipse2D.Double collisionZone;
	
	boolean isActive;
	
	int direction;
	
	public Entity(Point p)
	{
		point = p;		
		iPoint = new Point(p);
		
		direction = 0;
	}
	
	public boolean checkCollision(Entity e)
	{
		if(body.intersects(e.collisionBody))
		{
			return true;
		}
		
		return false;		
	}
	
	public void reset()
	{
		point.xCoord = iPoint.xCoord;
		point.yCoord = iPoint.yCoord;
	}
	
	public void pursue(Point p)
	{
		if(point.yCoord == p.yCoord)
		{
			if(point.xCoord > p.xCoord)
			{
				point.xCoord -= enemySpeed;
			}
			
			else if(point.xCoord < p.xCoord)
			{
				point.xCoord += enemySpeed;
			}
		}
		
		else if(point.xCoord == p.xCoord)
		{
			if(point.yCoord > p.yCoord)
			{
				point.yCoord -= enemySpeed;
			}
			
			else if(point.yCoord < p.yCoord)
			{
				point.yCoord += enemySpeed;
			}
		}
		
		else if(point.xCoord > p.xCoord)
		{
			if(point.yCoord > p.yCoord)
			{
				point.xCoord -= enemySpeed;
				point.yCoord -= enemySpeed;
			}
			
			else if(point.yCoord < p.yCoord)
			{
				point.xCoord -= enemySpeed;
				point.yCoord += enemySpeed;
			}
		}
		
		else if(point.xCoord < p.xCoord)
		{
			if(point.yCoord > p.yCoord)
			{
				point.xCoord += enemySpeed;
				point.yCoord -= enemySpeed;
			}
			
			else if(point.yCoord < p.yCoord)
			{
				point.xCoord += enemySpeed;
				point.yCoord += enemySpeed;
			}				
		}
		
		else if(point.yCoord > p.yCoord)
		{
			if(point.xCoord > p.xCoord)
			{
				point.xCoord -= enemySpeed;
				point.yCoord -= enemySpeed;
			}
			
			else if(point.xCoord < p.xCoord)
			{
				point.xCoord += enemySpeed;
				point.yCoord -= enemySpeed;
			}
		}
	}
	
	public void move(Point p, Point b)
	{			
		if(direction == 0 && point.xCoord > frameXSize)
		{
			//left
			point.xCoord -= enemySpeed;
		}
    	
		else if(direction == 1 && point.yCoord > frameYSize)
		{
			//up
			point.yCoord -= enemySpeed;
		}
    	
		else if(direction == 2 && point.xCoord < frameXSize)
		{
			//right
			point.xCoord += enemySpeed;
		}
    	
		else if(direction == 2 && point.yCoord < frameYSize)
		{
			//down
			point.yCoord += enemySpeed;
		}
    	
		else if(direction == 3 && point.xCoord > frameXSize && point.yCoord > frameYSize)
		{
			//up/left
			point.xCoord -= enemySpeed;
			point.yCoord -= enemySpeed;
		}
    	
		else if(direction == 4 && point.xCoord < frameXSize && point.yCoord > frameYSize)
		{
			//up/right
			point.xCoord += enemySpeed;
			point.yCoord -= enemySpeed;
		}
    	
		else if(direction == 5 && point.xCoord > frameXSize && point.yCoord < frameYSize)
		{
			//down/left
			point.xCoord -= enemySpeed;
			point.yCoord += enemySpeed;
		}
    	
		else if(direction == 6)
		{
			//down/right
			point.xCoord += enemySpeed;
			point.yCoord += enemySpeed;    		
		}
    	
		else if(direction == 7)
		{
			pursue(p);   		
		}
		
		else if(direction == 8)
		{
			pursue(b);
		}
	}
}