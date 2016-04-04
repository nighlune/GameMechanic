import java.awt.geom.Rectangle2D;


public class Trigger extends Entity
{
	public Trigger(Point p)
	{
		super(p);
		
		entitySize = 40;
		collisionSize = (int)(entitySize * 1/Math.sqrt(2));
		
		collisionBody = new Rectangle2D.Double(point.xCoord, point.yCoord, entitySize, entitySize);
	}
}
