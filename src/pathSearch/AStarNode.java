package pathSearch;

import main.Node;
import main.Segment;

public class AStarNode implements Comparable<AStarNode> {
	private Node repNode;
	private Node fromNode;
	private Segment segTraveled;
	private double distToHere;
	private double estDistTotal;
	
	public AStarNode(Node node, Node from, Segment traveled, double toHere, double estTotal){
		setRepNode(node);
		setFromNode(from);
		setDistToHere(toHere);
		setEstDistTotal(estTotal);
		segTraveled=traveled;
	}
	

	@Override
	public int compareTo(AStarNode o) {
		if(this.getEstDistTotal()<o.getEstDistTotal())return -1;
		else if(this.getEstDistTotal()>o.getEstDistTotal())return 1;
		else return 0;
	}


	private double getEstDistTotal() {
		return estDistTotal;
	}


	private void setEstDistTotal(double estDistTotal) {
		this.estDistTotal = estDistTotal;
	}


	public Node getRepNode() {
		return repNode;
	}


	public void setRepNode(Node repNode) {
		this.repNode = repNode;
	}


	public Node getFromNode() {
		return fromNode;
	}


	public void setFromNode(Node fromNode) {
		this.fromNode = fromNode;
	}


	public double getDistToHere() {
		return distToHere;
	}


	public void setDistToHere(double distToHere) {
		this.distToHere = distToHere;
	}


	public Segment getSegTraveled() {
		return segTraveled;
	}

	

}
