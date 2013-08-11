package pathSearch;

import main.Node;
import main.Segment;

public class AStarNode implements Comparable<AStarNode> {
	private Node repNode;
	private Node fromNode;
	private Segment segTraveled;
	private double costToHere;
	private double estCostTotal;
	
	public AStarNode(Node node, Node from, Segment traveled, double toHere, double estTotal){
		setRepNode(node);
		setFromNode(from);
		setCostToHere(toHere);
		setEstCostTotal(estTotal);
		segTraveled=traveled;
	}
	

	@Override
	public int compareTo(AStarNode o) {
		if(this.getEstCostTotal()<o.getEstCostTotal())return -1;
		else if(this.getEstCostTotal()>o.getEstCostTotal())return 1;
		else return 0;
	}


	private double getEstCostTotal() {
		return estCostTotal;
	}


	private void setEstCostTotal(double estDistTotal) {
		this.estCostTotal = estDistTotal;
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


	public double getCostToHere() {
		return costToHere;
	}


	public void setCostToHere(double distToHere) {
		this.costToHere = distToHere;
	}


	public Segment getSegTraveled() {
		return segTraveled;
	}

	

}
