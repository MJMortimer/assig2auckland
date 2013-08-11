package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Node {
	private int id;
	private Location loc;
	private Set<Node> neighbours;
	private Set<Road> roads;
	private Set<Segment> edgesIn;
	private Set<Segment> edgesOut;

	private boolean visited=false;//used for path finding
	private Node nodeFromPath=null;//used for path finding
	private Segment segTraveled;//used for path finding
	private double distToHere;//used for path finding

	private int depth;//used for articulation points
	private Node nodeFromArticulation=null;//used for articulation points

	private Node parent;//used for union find (For articulation)


	public Node(String[]values){
		neighbours=new HashSet<Node>();
		roads=new HashSet<Road>();
		edgesIn=(new HashSet<Segment>());
		edgesOut = new HashSet<Segment>();
		this.id=Integer.parseInt(values[0]);
		loc=Location.newFromLatLon(Double.parseDouble(values[1]), Double.parseDouble(values[2]));	
	}

	public String getDetails() {
		StringBuilder sB=new StringBuilder();
		for(Road r:roads){
			if(sB.length()>0){
				sB.append(", ");
			}
			sB.append(r.name());
		}
		return String.format("Location: %s\n Roads Through: %s",loc,sB.toString());
	}


	public Location getLoc(){
		return this.loc;
	}

	public Set<Node>getNeighbours(){
		return neighbours;
	}

	public void addNeighbour(Node n){
		neighbours.add(n);
	}

	public void addRoad(Road r){
		roads.add(r);
	}

	public Set<Road> getRoads(){
		return roads;
	}

	public void draw(Graphics2D g2D, Location origin,double scale){
		g2D.fillOval((getLoc().getPoint(origin, scale).x)-1, (getLoc().getPoint(origin, scale).y)-1, 2, 2);
	}

	public void draw(Graphics2D g2D, Location origin,double scale,int size){
		g2D.fillOval(getLoc().getPoint(origin, scale).x-(size/2), getLoc().getPoint(origin, scale).y-(size/2), size, size);
	}

	public void drawPath(Graphics2D g2D, Location origin, double scale) {
		g2D.setColor(Color.RED);
		g2D.fillOval((getLoc().getPoint(origin, scale).x)-1, (getLoc().getPoint(origin, scale).y)-1, 2, 2);
		if(segTraveled==null)return;
		segTraveled.draw(g2D, origin, scale);
		nodeFromPath.drawPath(g2D, origin, scale);


	}

	public double addPath(){
		if(segTraveled==null)return 0;
		return segTraveled.getLength()+nodeFromPath.addPath();
	}

	public double addPathWithTextDist(Stack<String> stack, double length, double total ) {
		if(segTraveled==null)return total;
		if(nodeFromPath!=null && nodeFromPath.segTraveled!=null && segTraveled.getRoad().name().equals(nodeFromPath.getSegTraveled().getRoad().name())){
			return total+nodeFromPath.addPathWithTextDist(stack, segTraveled.getLength()+length,segTraveled.getLength());
		}else{
			stack.push(String.format("%s : %4.2f km\n",segTraveled.getRoad().name() ,segTraveled.getLength()+length));
			return total+nodeFromPath.addPathWithTextDist(stack,0.0,segTraveled.getLength());
		}
	}

	public double addPathWithTextTime(Stack<String> stack, double time, double total ) {
		if(segTraveled==null)return total;
		if(nodeFromPath!=null && nodeFromPath.segTraveled!=null && segTraveled.getRoad().name().equals(nodeFromPath.getSegTraveled().getRoad().name())){
			return total+nodeFromPath.addPathWithTextTime(stack, (segTraveled.getLength() / segTraveled.getRoad().getSpeedValue())+time,segTraveled.getLength() / segTraveled.getRoad().getSpeedValue());
		}else{
			stack.push(String.format("%s : %4.2f minutes\n",segTraveled.getRoad().name() ,(((segTraveled.getLength() /segTraveled.getRoad().getSpeedValue()) +time)*60)));
			return total+nodeFromPath.addPathWithTextTime(stack,0.0,segTraveled.getLength() / segTraveled.getRoad().getSpeedValue());
		}
	}

	public double addPTime(Stack<String> stack, double currentTime, double totalTime){
		if(segTraveled == null)return totalTime;
		if(nodeFromPath!=null && nodeFromPath.segTraveled!=null && segTraveled.getRoad().name().equals(nodeFromPath.getSegTraveled().getRoad().name())){ 
			return totalTime + nodeFromPath.addPTime(stack, currentTime + (segTraveled.getLength() / segTraveled.getRoad().getSpeedValue()),totalTime);
		}else{
			stack.push(String.format("%s : %4.2f minutes\n", segTraveled.getRoad().name(), (currentTime + (segTraveled.getLength() / segTraveled.getRoad().getSpeedValue()))*60));
			return totalTime + nodeFromPath.addPTime(stack, 0.0, segTraveled.getLength()/segTraveled.getRoad().getSpeedValue());
		}

		
	}


	public int getID() {
		return id;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public Node getNodeFrom() {
		return nodeFromPath;
	}

	public void setNodeFrom(Node nodeFrom) {
		this.nodeFromPath = nodeFrom;
	}

	public void setPathCost(double distToHere) {
		this.distToHere=distToHere;

	}

	public void addEdgeIn(Segment seg){
		getEdgesIn().add(seg);
	}

	public void addEdgeOut(Segment seg){
		getEdgesOut().add(seg);
	}

	public Set<Segment> getEdgesIn() {
		return edgesIn;
	}

	public Set<Segment>getEdgesOut(){
		return edgesOut;
	}

	public double getDistToHere() {
		return distToHere;
	}

	public Segment getSegTraveled() {
		return segTraveled;
	}

	public void setSegTraveled(Segment segTraveled) {
		this.segTraveled = segTraveled;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Node getNodeFromArticulation() {
		return nodeFromArticulation;
	}

	public void setNodeFromArticulation(Node nodeFromArticulation) {
		this.nodeFromArticulation = nodeFromArticulation;
	}

	public void setParent(Node n){
		this.parent=n;
	}

	public Node getParent(){
		return this.parent;
	}








}
