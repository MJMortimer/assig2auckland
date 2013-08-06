package structures;
import java.util.*;

import main.Road;


public class TrieHeader {

	private Map<Character, TrieNode> nodes;

	public TrieHeader(){
		this.nodes =new HashMap<Character,TrieNode>();
	}

	public TrieHeader(String data, Road r){
		this.nodes=new HashMap<Character, TrieNode>();
		this.add(data,r);
	}

	public void add(String data,Road r){
		//if the String is null or
		//if the length of the string is zero then just return. There is nothing to add
		if(data==null || data.length()==0)return;
		//convert the string to array
		char[] dataArray = data.toLowerCase().toCharArray();

		TrieHeader node=this;
		for(char c:dataArray){
			if(!node.getNodes().containsKey(c)){
				TrieNode newNode=new TrieNode(c);
				node.getNodes().put(c,newNode);
				node=newNode;
			}
			else{
				node=node.getNodes().get(c);
			}
		}

		((TrieNode)node).addRoad(r);
	}

	public Set<String> listPrefix(String data){
		if(data==null||data.length()==0) return null;

		char[] dataArray = data.toLowerCase().toCharArray();

		TrieHeader node=this;

		//search through To find prefix. then add all words to the set and return it to be displayed in text box
		TreeSet<String> list= new TreeSet<String>();

		for(char c:dataArray){
			if(node.getNodes().containsKey(c)) 
				node=node.getNodes().get(c);
			else return null;				
		}

		((TrieNode)node).addWords(new StringBuilder(data),list);


		//traverse and add words. check each node for markers.
		return list;
	}

	//this checks if there is a mark at last letter
	public boolean isWord(String data){
		//if the String is null or
		//if the length of the string is zero then just return false
		if(data==null || data.length()==0)return false;
		//convert the string to array
		char[] dataArray = data.toLowerCase().toCharArray();

		TrieHeader node=this;
		for(char c:dataArray){
			if(node.getNodes().containsKey(c)) 
				node=node.getNodes().get(c);
			else 
				return false;
		}
		return ((TrieNode)node).hasRoad();

	}

	//this disregards marks
	public boolean isPrefix(String data){
		//if the String is null or
		//if the length of the string is zero then just return false
		if(data==null || data.length()==0)return false;
		//convert the string to array
		char[] dataArray = data.toLowerCase().toCharArray();

		TrieHeader node=this;
		for(char c:dataArray){
			if(node.getNodes().containsKey(c)) 
				node=node.getNodes().get(c);
			else 
				return false;
		}
		return true;
	}

	public Map<Character, TrieNode> getNodes() {
		return nodes;
	}

	public TrieHeader getNode(String data){
		//if the String is null or
		//if the length of the string is zero then just return false
		if(data==null || data.length()==0)return null;
		//convert the string to array
		char[] dataArray = data.toLowerCase().toCharArray();

		TrieHeader node=this;
		for(char c:dataArray){
			if(node.getNodes().containsKey(c)) 
				node=node.getNodes().get(c);
			else 
				return null;
		}
		return node;

	}
}






