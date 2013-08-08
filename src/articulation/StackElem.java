package articulation;

import java.util.Queue;

import main.Node;

public class StackElem {

	private Node firstNode;
	private int depth;
	private StackElem stackElemFrom;
	
	private Queue<Node> children;
	private int reach;
	
	public StackElem(Node first, int d, StackElem p){
		firstNode = first;
		depth = d;
		stackElemFrom = p;
	}

	public Node getFirstNode() {
		return firstNode;
	}

	public void setFirstNode(Node firstNode) {
		this.firstNode = firstNode;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public StackElem getStackElemFrom() {
		return stackElemFrom;
	}

	public void setStackElemFrom(StackElem stackElemFrom) {
		this.stackElemFrom = stackElemFrom;
	}

	public Queue<Node> getChildren() {
		return children;
	}
	
	public void setChildren(Queue<Node> children) {
		this.children = children;
	}

	public int getReach() {
		return reach;
	}

	public void setReach(int reach) {
		this.reach = reach;
	}
}
