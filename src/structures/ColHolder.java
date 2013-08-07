package structures;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import main.Node;
import main.Road;
import main.Segment;


public class ColHolder {

	private Map<Integer,Node>nodeMap;
	private TrieHeader trie;
	private Map<Integer,Road>roadMap;
	private List<Segment>segList;
	private QuadTree quadTree;
	private UnionFind disjSets;

	public ColHolder(String dir){
		initTrie(dir);	
		initNodeMap(dir);
		initUnionFind();
		initRoadSegs(dir);
		System.out.println("after: "+disjSets.getSets().size());
		


	}


	private void initUnionFind() {
		disjSets = new UnionFind();
		for(Node n : nodeMap.values()){
			disjSets.makeSet(n);
		}
		System.out.println("before: "+disjSets.getSets().size());

	}


	public void initTrie(String dir){

		trie=new TrieHeader();
		roadMap=new HashMap<Integer,Road>();
		File file = new File(dir+"/roadID-roadInfo.tab");
		Scanner scan;
		try {
			scan = new Scanner(file);
			scan.nextLine();
			while(scan.hasNextLine()){
				String line =scan.nextLine();
				String[] values = line.split("\t");
				Road r=new Road(values);
				roadMap.put(Integer.parseInt(values[0]),r);
				trie.add(values[2]+" "+values[3],r);

			}

		} catch (FileNotFoundException e) {
		}

	}

	public void initNodeMap(String dir){
		setNodeMap(new HashMap<Integer,Node>());
		File file = new File(dir+"/nodeID-lat-lon.tab");
		Scanner scan;
		try {
			scan = new Scanner(file);
			while(scan.hasNextLine()){
				String line =scan.nextLine();
				String[] values = line.split("\t");
				Node node= new Node(values);
				getNodeMap().put(Integer.parseInt(values[0]),node);
			}

		} catch (FileNotFoundException e) {
		}


	}

	public void initRoadSegs(String dir){
		File file = new File(dir+"/roadSeg-roadID-length-nodeID-nodeID-coords.tab");
		setSegList(new ArrayList<Segment>());
		Scanner scan;
		try {
			scan = new Scanner(file);
			scan.nextLine();
			while(scan.hasNextLine()){
				String line =scan.nextLine();
				String[] values = line.split("\t");
				Segment newSeg=new Segment(values);
				Road road =roadMap.get(newSeg.getRoadID());
				road.addSegment(newSeg);
				newSeg.setRoad(road);
				getSegList().add(newSeg);

				Node node1=getNodeMap().get(newSeg.getNode1());
				Node node2=getNodeMap().get(newSeg.getNode2());

				//union the two nodes in the UnionFind structure. Helpful for articulation
				if(node1!=null && node2!=null)
					disjSets.union(node1, node2);

					if(node1!=null&&node2!=null){
						node1.addNeighbour(node2);
						node2.addNeighbour(node1);
					}
				if(node1!=null){
					node1.addRoad(road);
					road.addIntersection(node1);
					node1.addEdgeOut(newSeg);
					if(!newSeg.getRoad().isOneWay())
						node1.addEdgeIn(newSeg);
				}
				if(node2!=null){
					node2.addRoad(road);
					road.addIntersection(node2);
					node2.addEdgeIn(newSeg);
					if(!newSeg.getRoad().isOneWay())
						node2.addEdgeOut(newSeg);
				}
			}

		} catch (FileNotFoundException e) {
		}
	}

	public TrieHeader getTrie(){
		return this.trie;
	}

	public void addQuadTree(double maxX, double maxY, double minX, double minY){
		setQuadTree(new QuadTree(maxX,maxY,minX,minY));
		for(Node n:this.getNodeMap().values()){
			getQuadTree().add(n);

		}
	}


	public List<Segment> getSegList() {
		return segList;
	}


	public void setSegList(List<Segment> segList) {
		this.segList = segList;
	}


	public Map<Integer,Node> getNodeMap() {
		return nodeMap;
	}


	public void setNodeMap(Map<Integer,Node> nodeMap) {
		this.nodeMap = nodeMap;
	}


	public QuadTree getQuadTree() {
		return quadTree;
	}


	public void setQuadTree(QuadTree quadTree) {
		this.quadTree = quadTree;
	}


	public UnionFind getUnionFind() {
		return disjSets;
	}


	





}
