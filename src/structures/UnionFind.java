package structures;

import java.util.HashSet;
import java.util.Set;

import main.Node;

public class UnionFind {
	
	private Set<Node> sets;
	
	public UnionFind(){
		sets = new HashSet<Node>(); 
	}
	
	public void makeSet(Node n){
		n.setParent(n);
		sets.add(n);
	}
	
	public void union(Node n1, Node n2){
		Node n1Root = find(n1);
		Node n2Root = find(n2);
		
		if(n1Root.equals(n2Root))return;
		
		n1Root.setParent(n2Root);
		sets.remove(n1Root);
	}
	
	public Node find(Node n){
		if(n.getParent().equals(n)){
			return n;
		}else{
			n.setParent(find(n.getParent()));
			return n.getParent();
		}
	}

	public Set<Node> getSets() {
		return sets;
	}

}
