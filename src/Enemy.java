import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Enemy extends Entity
{	
	static int radius = 300;
		
	public Enemy(Point p)
	{		
		super(p);
		
		entitySize = 30;
		collisionSize = (int)(entitySize * 1/Math.sqrt(2));
		
		body = new Ellipse2D.Double(point.xCoord, point.yCoord, entitySize, entitySize);
		collisionBody = new Rectangle2D.Double(point.xCoord, point.yCoord, collisionSize, collisionSize);		
		collisionZone = new Ellipse2D.Double(point.xCoord - radius * .5 + entitySize * .5, point.yCoord - radius * .5 + entitySize * .5, radius, radius);
		
		isActive = false;
	}
}
