package structures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import main.Road;

public class TrieNode extends TrieHeader {

	private char letter;
	private ArrayList<Road> roads=new ArrayList<Road>();

	public TrieNode(char letter, Road road){
		this.letter=letter;
		roads.add(road);
	}

	public TrieNode(char letter){
		this.letter=letter;
		
	}

	public char getLetter(){
		return this.letter;
	}

	public boolean hasRoad(){
		return !roads.isEmpty();
	}

	public void addRoad(Road road){
		roads.add(road);
	}	
	
	public ArrayList<Road> getRoads(){
		return this.roads;
	}

	public Set<String> addWords(StringBuilder prefix,Set<String> list){
		if(this.hasRoad()){				
			list.add(prefix.toString());
		}
		for(TrieNode t:this.getNodes().values()){
			t.addWords(new StringBuilder(prefix).append(t.getLetter()),list);
		}
		return list;
	}
}
