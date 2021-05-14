package rsclicker;

import java.awt.Point;

public class PointType {
	private Point point;
	private int type;
	private boolean noDelay;
	
	public PointType() {
		
	}
	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public boolean isNoDelay() {
		return noDelay;
	}
	public void setNoDelay(boolean noDelay) {
		this.noDelay = noDelay;
	}
}
