package structures;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import main.Location;
import main.Node;


public class QuadTree {

	private boolean isLeaf;
	private QuadTree tL, tR, bL, bR;
	private int maxElements;
	private Location center;
	private Rectangle2D.Double bounds;
	private List<Node> intersections;

	public QuadTree(double maxX, double maxY, double minX, double minY){
		this.maxElements=16;
		bounds=new Rectangle2D.Double(minX,minY,maxX-minX, maxY-minY);
		isLeaf=true;
		intersections=new ArrayList<Node>();


	}


	public void add(Node n){
		if(n==null) return;
		//if bounds dont contain our point then we dont add
		if(!bounds.contains(n.getLoc().x, n.getLoc().y))return;
		//if there is space in this quadrant then add it
		if(isLeaf && intersections.size()<maxElements){
			intersections.add(n);
			return;
		}else if(isLeaf && intersections.size()>=maxElements){
			split();
			add(n);
		}else if(!isLeaf)
			if(tL.bounds.contains(n.getLoc().x,n.getLoc().y)){
				tL.add(n);
			}else if(tR.bounds.contains(n.getLoc().x,n.getLoc().y)){
				tR.add(n);
			}else if(bL.bounds.contains(n.getLoc().x,n.getLoc().y)){
				bL.add(n);
			}else if(bR.bounds.contains(n.getLoc().x,n.getLoc().y)){
				bR.add(n);
			}
	}










	public void clear(){

	}

	public void split(){
		this.isLeaf=false;
		tL=new QuadTree(bounds.x+(bounds.width/2), bounds.y+(bounds.height/2), bounds.x, bounds.y);
		tR=new QuadTree(bounds.x+bounds.width, bounds.y+(bounds.height/2), bounds.x+(bounds.width/2), bounds.y);
		bL=new QuadTree(bounds.x+(bounds.width/2), bounds.y+bounds.height, bounds.x, bounds.y+(bounds.height/2));
		bR=new QuadTree(bounds.x+bounds.width, bounds.y+bounds.height, bounds.x+(bounds.width/2), bounds.y+(bounds.height/2));

		for(Node n:this.intersections){
			this.add(n);

		}

		this.intersections.clear();


	}

	public QuadTree findHolder(Location l){
		if(this.isLeaf && !this.bounds.contains(l.x,l.y))return null;
		else if(this.isLeaf && this.bounds.contains(l.x,l.y))return this;
		else if(!this.isLeaf){
			if(tL.bounds.contains(l.x,l.y)){
				return tL.findHolder(l);
			}else if(tR.bounds.contains(l.x,l.y)){
				return tR.findHolder(l);
			}else if(bL.bounds.contains(l.x,l.y)){
				return bL.findHolder(l);
			}else if(bR.bounds.contains(l.x,l.y)){
				return bR.findHolder(l);
			}
		}
		return null;
	}

	public Node find(Location l){
		QuadTree holder=findHolder(l);
		if(holder==null)return null;
		double dist=0;
		boolean first=true;
		Node closest=null;
		for(Node n:holder.intersections){
			if(first){
				dist=l.distanceTo(n.getLoc());
				closest=n;
				first=false;
			}else{
				if(l.closeTo(n.getLoc(), dist)){
					dist=l.distanceTo(n.getLoc());
					closest=n;
				}
			}					
		}
		return closest;
	}

	public Node findNode(Node n){
		return null;
	}
	
	public void draw(Graphics2D g,Location origin, double scale){
		
		Location minL = new Location(bounds.x,bounds.y);
		Location maxL = new Location(bounds.x+bounds.width, bounds.y+bounds.height);
		
		Point minP=minL.getPoint(origin, scale);
		Point maxP=maxL.getPoint(origin, scale);
		g.drawRect(minP.x,maxP.y,maxP.x-minP.x,minP.y-maxP.y);
		if(tL!=null)tL.draw(g,origin,scale);
		if(tR!=null)tR.draw(g,origin,scale);
		if(bL!=null)bL.draw(g,origin,scale);
		if(bR!=null)bR.draw(g,origin,scale);
	}

}
