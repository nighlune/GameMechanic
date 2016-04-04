
public class Point
{
	int xCoord;
	int yCoord;
	
	public Point(int x, int y)
	{
		xCoord = x;
		yCoord = y;
	}
	
	public Point(Point p)
	{
		xCoord = p.xCoord;
		yCoord = p.yCoord;
	}
}
