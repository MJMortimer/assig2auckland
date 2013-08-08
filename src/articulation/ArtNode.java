package articulation;

import main.Node;

public class ArtNode {
	
	private Node node;
	private Node fromNode;
	private int depth;
	
	public ArtNode(Node n, int d, Node fN){
		this.setNode(n);
		this.setFromNode(fN);
		this.setDepth(d);
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public Node getFromNode() {
		return fromNode;
	}

	public void setFromNode(Node fromNode) {
		this.fromNode = fromNode;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	

}
