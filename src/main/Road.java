package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Road {
	private int id;
	private int type;
	private String label;
	private String city;
	private int oneWay;
	private int speed;
	private int roadClass;
	private int notForCar;
	private int notForPede;
	private int notForBicy;
	private List<Segment> segments;
	private List<Node> intersections;


	public Road(String[]values){
		this.id=Integer.parseInt(values[0]);
		this.type=Integer.parseInt(values[1]);
		this.label=values[2];
		this.city=values[3];
		this.oneWay=Integer.parseInt(values[4]);
		this.speed=Integer.parseInt(values[5]);
		this.roadClass=Integer.parseInt(values[6]);
		this.notForCar=Integer.parseInt(values[7]);
		this.notForPede=Integer.parseInt(values[8]);
		this.notForBicy=Integer.parseInt(values[9]);
		segments=new ArrayList<Segment>();
		intersections=new ArrayList<Node>();
	}

	public String getDetails(){

		String sSpeed=decodeSpeed();
		String sRoadClass=decodeClass();
		String sOneWay = oneWay==1 ? "yes": "no";
		String forCar = notForCar==0 ? "yes": "no";
		String forPede = notForPede==0 ? "yes": "no";
		String forBicy = notForBicy==0 ? "yes": "no";
		return String.format("Road: %s, %s\nRoad Class: %s, Speed: %s\nOne Way: %s, For Cars: %s, For Pedestrians: %s, For Bicycles: %s\n", label,city,sRoadClass,sSpeed,sOneWay,forCar,forPede,forBicy);

	}


	public void addSegment(Segment seg){
		segments.add(seg);
	}

	public List<Segment> getSegments(){
		return segments;
	}
	public String name(){
		return label+" "+city;
	}

	public void addIntersection(Node node) {
		intersections.add(node);

	}

	public List<Node> getIntersections() {
		return intersections;
	}

	public String decodeSpeed(){
		switch(speed){
		case(0):return "5km/h";
		case(1):return "20km/h";
		case(2):return "40km/h";
		case(3):return "60km/h";
		case(4):return "80km/h";
		case(5):return "100km/h";
		case(6):return "110km/h";
		default:return "---";		
		}

	}
	
	private String decodeClass() {
		switch(roadClass){
			case(1):return "Major highway-thick";
			case(2):return "Principal highway-thick";
			case(3):return "Principal highway-medium";
			case(4):return "Arterial road-thick";
			case(5):return "Arterial road-medium";
			case(6):return "Road-thin";
			case(7):return "Alley-thick";
			case(8):return "Ramp";
			case(9):return "Ramp";
			case(10):return "Unpaved road-thin";
			case(11):return "Major Highway Connector-thick";
			case(12):return "Roundabout";
			case(22):return "Track/Trail";
			case(26):return "Ferry";
		default:return "---";
		}
		
		
	}
	public boolean isOneWay(){
		return oneWay==1;
	}



}
