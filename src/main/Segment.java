package main;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class Segment {
	private int roadID;
	private double length;
	private int node1ID;
	private int node2ID;
	private List<Location> coords;
	private Road road=null;



	public Segment(String[]values){
		coords=new ArrayList<Location>();
		roadID=Integer.parseInt(values[0]);
		setLength(Double.parseDouble(values[1]));
		node1ID=Integer.parseInt(values[2]);
		node2ID=Integer.parseInt(values[3]);
		for(int i=4;i<values.length;i+=2){
			double lat=Double.parseDouble(values[i]);
			double lon=Double.parseDouble(values[i+1]);
			coords.add(Location.newFromLatLon(lat, lon));
		}
	}
	
	public int getRoadID(){
		return this.roadID;
	}
	
	public List<Location>getCoords(){
		return coords;
	}
	
	public void draw(Graphics2D g2D, Location origin, double scale){
		boolean first=true;
		Path2D seg=new Path2D.Double();
		first=true;
		for(Location l:this.getCoords()){
			if(first){
				seg.moveTo(l.getPoint(origin, scale).x,l.getPoint(origin, scale).y);
				first=false;
			}else{
				seg.lineTo(l.getPoint(origin, scale).x,l.getPoint(origin, scale).y);
			}
			
		}


		g2D.draw(seg);

		
	}
	
	public int getNode1(){
		return node1ID;
	}
	
	public int getNode2(){
		return node2ID;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public Road getRoad() {
		return road;
	}

	public void setRoad(Road road) {
		this.road = road;
	}
}
