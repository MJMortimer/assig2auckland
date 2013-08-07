package main;

import javax.swing.*;


import pathSearch.AStarNode;

import structures.ColHolder;
import structures.TrieNode;
import suggestor.AutoSuggestionTextField;
import suggestor.SuggestionListener;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

public class RoadMap {

	private JFrame frame;
	private AutoSuggestionTextField<String> searchField;
	private JTextArea textArea;
	private JComponent drawing;
	private String currentRoad=null;
	private ArrayList<Road> currentRoadObj=null;
	private Node currentIntersection=null;
	private Node goalIntersection=null;
	private ColHolder cols;
	private String dir;
	private double scale;
	private Location origin;
	private double maxX=0;
	private double maxY=0;
	private double minX=0;
	private double minY=0;
	private boolean init;

	private int startX;
	private int startY;

	private Mode modeType = Mode.DESCRIPTION;


	private Set<Node> articulationPoints;

	private ArrayList<Node> debugSearch;

	private static enum Mode{
		DESCRIPTION,
		ARTICULATION_POINTS,
		PATH_FINDING_DIST,
		PATH_FINDING_TIME

	}



	public RoadMap(){
		init=true;
		init();


	}

	public void init(){
		initGUI();
		JFileChooser f=  new JFileChooser();
		f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		f.showOpenDialog(frame);
		if(f.getSelectedFile()!=null){
			dir=f.getSelectedFile().getAbsolutePath();
			cols = new ColHolder(dir);
			searchField.setTrie(cols.getTrie());
			initBounds();
			cols.addQuadTree(maxX+2,maxY+2,minX-1,minY-1);
			init=false;
			drawing.repaint();
		}
	}

	protected void initGUI() {
		//basic frame initialization
		frame = new JFrame("Auckland Road Maps");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //setting the default close operation of JFrame
		frame.setSize(800,600);
		frame.setResizable(false);

		//Get the container for the frame's content
		Container container = frame.getContentPane();
		//give the container a GridBagLayout
		container.setLayout(new GridBagLayout());
		//Create the constraints for the layout
		GridBagConstraints c = new GridBagConstraints();


		//Create a JPanel to be the search bar at the top of the page with its own
		//GridBagLayout
		JPanel searchBar = new JPanel(new GridBagLayout());
		//Create the constraints for this JPanel.
		GridBagConstraints pC = new GridBagConstraints();

		//Create zoom in button
		JButton zoomIn=new JButton("Zoom In");
		pC.gridx=0;
		pC.gridy=0;
		pC.weightx=.05;
		pC.fill=GridBagConstraints.VERTICAL;
		pC.anchor=GridBagConstraints.EAST;
		searchBar.add(zoomIn, pC);

		pC = new GridBagConstraints();
		//Create zoom out button
		JButton zoomOut=new JButton("Zoom Out");
		pC.gridx=1;
		pC.gridy=0;
		pC.weightx=.15;
		pC.fill=GridBagConstraints.VERTICAL;
		pC.anchor=GridBagConstraints.WEST;
		pC.insets=new Insets(0, 5, 0, 0);
		searchBar.add(zoomOut, pC);

		pC = new GridBagConstraints();
		//Create the read only search label
		JLabel searchLabel = new JLabel("Search: ");
		pC.gridx=2;//Third column of panel
		pC.gridy=0;//first row of panel
		pC.weightx=.3;//The label gets 30 percent of the window width
		pC.anchor=GridBagConstraints.EAST;//anchor to right
		pC.fill=GridBagConstraints.VERTICAL;
		searchBar.add(searchLabel,pC);

		pC = new GridBagConstraints();
		//Create the text field for the Search Bar

		//TODO changed here from JTEXTFIELD to THE NAMECOMPLETER ONE
		searchField=new AutoSuggestionTextField<String>();
		pC.gridx=3;//Fourth column of panel
		pC.gridy=0;//first row of panel
		pC.weightx=.69;//The search bar is allowed 60 percent of the window width
		pC.anchor=GridBagConstraints.EAST;//anchor to left
		pC.fill=GridBagConstraints.BOTH;//Fill the column both vertical and horizontal.
		searchBar.add(searchField,pC);

		pC = new GridBagConstraints();
		//create the button for the search bar
		final JButton search = new JButton("Go");
		pC.gridx=4;//Fifth column of panel
		pC.gridy=0;//first row of panel
		pC.weightx=.01;//the button gets  percent of window width
		pC.anchor=GridBagConstraints.EAST;//anchor left
		pC.insets= new Insets(0, 0, 0, 5);
		pC.fill=GridBagConstraints.VERTICAL;
		searchBar.add(search,pC);


		searchBar.setBackground(Color.LIGHT_GRAY);
		c.gridx = 0;//first column of content pane
		c.gridy = 0;//first row of content pane
		c.weightx = 1.0;//the search bar gets all of the window width
		c.weighty = 0.03;//the search bar gets 3 percent of the window height
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.BOTH;
		container.add(searchBar, c);

		//create the drawing component for the content pane
		drawing=new JComponent(){
			protected void paintComponent(Graphics g){
				redraw(g);		

			}
		};
		c.gridx = 0;//first column of the content pane
		c.gridy = 1;//second row of the content pane
		c.weightx = 1.0;//the drawing component will have all the weight of this row
		c.weighty = 0.87;//it gets 92 percent of the window height
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.fill = GridBagConstraints.BOTH;	
		frame.add(drawing,c);


		JScrollPane scroll=new JScrollPane();
		textArea=new JTextArea();
		scroll.setViewportView(textArea);
		textArea.setRows(5);
		textArea.setEditable(false);

		c.gridx = 0;//first column of the content pane
		c.gridy = 2;//third row of the content pane
		c.weightx = 1.0;//the text area will have all the weight of this row
		c.weighty = 0.0;//it gets 5 percent of the window height
		c.anchor = GridBagConstraints.LAST_LINE_START;
		container.add(scroll, c);

		JMenuBar mBar=new JMenuBar();
		JMenu file=new JMenu("File");
		JMenuItem open =new JMenuItem("Open..");
		file.add(open);
		mBar.add(file);

		JMenu options = new JMenu("Options");
		JMenuItem descriptionMode = new JMenuItem("Assignment 1 Intersection Description.");
		options.add(descriptionMode);
		JMenuItem articulationMode = new JMenuItem("Articulation Point Search");
		options.add(articulationMode);
		JMenuItem aStarDistMode = new JMenuItem("A* Distribution with distance.");
		options.add(aStarDistMode);
		mBar.add(options);

		frame.setJMenuBar(mBar);

		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser f=  new JFileChooser();
				f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				f.showOpenDialog(frame);
				if(f.getSelectedFile()!=null){
					dir=f.getSelectedFile().getAbsolutePath();
					cols = new ColHolder(dir);
					initBounds();
					cols.addQuadTree(maxX,maxY,minX,minY);
					drawing.repaint();
				}

			}
		});

		descriptionMode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				modeType = Mode.DESCRIPTION;
				drawing.repaint();
				setText();
			}
		});

		articulationMode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				modeType = Mode.ARTICULATION_POINTS;
				articulationPointsSearch();
				setText();
				drawing.repaint();
			}
		});	

		aStarDistMode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				modeType = Mode.PATH_FINDING_DIST;	
				if(currentIntersection !=null && goalIntersection!=null){
					AStarSearch();
					setText();
				}
				drawing.repaint();
			}
		});




		/////Definitions of all actions and listeners///////
		////////////////////////////////////////////////////

		//search button listener
		search.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				searchField.getSuggestionListener().onEnter(searchField.getTextField().getText());
			}

		});

		//graphics pane listener

		drawing.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				startX=arg0.getX();
				startY=arg0.getY();

			}

			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {

				if(arg0.getButton()==MouseEvent.BUTTON1){
					if(origin==null||scale==Double.NaN)return;
					Location fromPoint=Location.newFromPoint(arg0.getPoint(), origin, scale);
					Node n =cols.getQuadTree().find(fromPoint);
					if(n!=null){
						currentIntersection=n;


						//TODO move to just performing search after selection on menu bar. Reimplement for description mode
						if(modeType == Mode.DESCRIPTION){
							setText();
						}



						if((modeType == Mode.PATH_FINDING_DIST || modeType == Mode.PATH_FINDING_TIME) && goalIntersection!=null){
							AStarSearch();
							setText();
						}
						drawing.repaint();
					}

				}else if(arg0.getButton()==MouseEvent.BUTTON3 && (modeType == Mode.PATH_FINDING_DIST || modeType == Mode.PATH_FINDING_TIME)){
					if(origin==null||scale==Double.NaN)return;
					Location fromPoint=Location.newFromPoint(arg0.getPoint(), origin, scale);
					Node n =cols.getQuadTree().find(fromPoint);
					if(n!=null){
						goalIntersection=n;
						if(currentIntersection!=null)AStarSearch();
						setText();
						drawing.repaint();
					}
				}
			}
		});

		drawing.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent arg0) {


			}

			@Override
			public void mouseDragged(MouseEvent arg0) {

				int diffX=arg0.getX()-startX;
				int diffY=arg0.getY()-startY;


				Location fromPoint= Location.newFromPoint(new Point(diffX,diffY), origin, scale);

				origin=new Location(origin.x+(origin.x-fromPoint.x),origin.y+(origin.y-fromPoint.y));

				startX=arg0.getX();
				startY=arg0.getY();
				drawing.repaint();


			}
		});

		drawing.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if(arg0.getWheelRotation()<0){
					scale=scale*1.25;
					Location fromPoint=Location.newFromPoint(new Point(drawing.getWidth()/2,drawing.getHeight()/2), origin, scale);
					origin=new Location(origin.x-((origin.x-fromPoint.x)/2.5),origin.y-((origin.y-fromPoint.y)/2.5));
					scale=scale*1.25;
					drawing.repaint();
				}else{
					scale=scale*0.75;
					Location fromPoint=Location.newFromPoint(new Point(drawing.getWidth()/2,drawing.getHeight()/2), origin, scale);
					origin=new Location(origin.x+((origin.x-fromPoint.x)/2.5),origin.y+((origin.y-fromPoint.y)/2.5));
					scale=scale*0.75;
					drawing.repaint();
				}

			}
		});




		searchField.setSuggestionListener(new SuggestionListener<String>() {

			@Override
			public void onSuggestionSelected(String item) {
				if(currentRoad==null || (item!=null && currentRoad!=null && !item.equals(currentRoad))){
					currentRoad=item;
					TrieNode node=(TrieNode) cols.getTrie().getNode(currentRoad);
					currentRoadObj=node.getRoads();
					setText();
					drawing.repaint();
				}
			}

			@Override
			public void onEnter(String item) {
				onSuggestionSelected(item);


			}
		});


		//zoom in listener
		zoomIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				scale=scale*1.25;
				Location fromPoint=Location.newFromPoint(new Point(drawing.getWidth()/2,drawing.getHeight()/2), origin, scale);

				origin=new Location(origin.x-((origin.x-fromPoint.x)/2.5),origin.y-((origin.y-fromPoint.y)/2.5));
				scale=scale*1.25;
				drawing.repaint();
			}
		});


		//zoom out listener
		zoomOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {


				scale=scale*0.75;
				Location fromPoint=Location.newFromPoint(new Point(drawing.getWidth()/2,drawing.getHeight()/2), origin, scale);
				origin=new Location(origin.x+((origin.x-fromPoint.x)/2.5),origin.y+((origin.y-fromPoint.y)/2.5));
				scale=scale*0.75;
				drawing.repaint();
			}
		});

		/*//assig1 button listener
		articulation.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				pathFinding=false;
				if(currentIntersection!=null){
					articulationPointsSearch();
					setText();
				}
				drawing.repaint();
			}
		});

		pathFind.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pathFinding=true;
				if(currentIntersection !=null && goalIntersection!=null){
					AStarSearch();
					setText();
				}
				drawing.repaint();
			}
		});*/





		//////////////////////////////////////////////////
		//////////////////////////////////////////////////

		frame.setVisible(true); //make the frame visible



	}

	public void setText(){
		if(modeType == Mode.DESCRIPTION){
			textArea.setText("");
			if(currentRoadObj!=null)textArea.append("Seleted Road\n"+currentRoadObj.get(0).getDetails()+"\n");
			if(currentIntersection!=null)textArea.append("Selected Intersection\n"+currentIntersection.getDetails());
		}else if(modeType == Mode.PATH_FINDING_DIST || modeType == Mode.PATH_FINDING_TIME){
			if(currentIntersection!=null && goalIntersection!=null){
				Stack<String> stack=new Stack<String>();
				double total = goalIntersection.addPathWithText(stack,0,0);
				textArea.setText("");
				while(!stack.isEmpty()){
					textArea.append(stack.pop());
				}
				textArea.append(String.format("Total Distance: %4.2fkm", total));
			}
		}
	}



	private void initBounds(){
		boolean firstL=true;
		for(Segment s:cols.getSegList()){
			for(Location l: s.getCoords()){
				if(firstL){
					maxX=l.x;
					maxY=l.y;
					minX=l.x;
					minY=l.y;
					firstL=false;
				}else{
					if(maxX<l.x)maxX=l.x;
					if(maxY<l.y)maxY=l.y;
					if(minX>l.x)minX=l.x;
					if(minY>l.y)minY=l.y;
				}

			}
		}

		this.origin = new Location(minX,maxY);
		Location maxL=new Location(maxX,maxY);
		Location minL=new Location(minX,minY);
		this.scale = ( frame.getWidth() /(maxL.x - minL.x));


	}

	private void redraw(Graphics g) {

		if(init)return; //we are still initialising
		Graphics2D g2D = (Graphics2D) g;



		for(Segment s:cols.getSegList()){
			s.draw(g2D,origin,scale);
		}		

		g2D.setColor(Color.BLUE);
		for(Node n:cols.getNodeMap().values()){	
			n.draw(g2D,origin,scale);
		}
		g2D.setColor(Color.BLACK);

		if(currentRoadObj!=null){
			g2D.setColor(Color.red);
			for(Road r : currentRoadObj){
				for(Segment s2:r.getSegments()){
					s2.draw(g2D, origin, scale);

				}
			}
			g2D.setColor(Color.BLACK);
		}



		if( (modeType == Mode.PATH_FINDING_DIST || modeType == Mode.PATH_FINDING_TIME) && goalIntersection!=null){
			g2D.setColor(Color.yellow);
			int size = scale>10? 8 : 4;
			goalIntersection.draw(g2D,origin,scale,size);
		}

		if( (modeType == Mode.PATH_FINDING_DIST || modeType == Mode.PATH_FINDING_TIME) && currentIntersection!=null && goalIntersection!=null){
			goalIntersection.drawPath(g2D,origin,scale);
		}

		if(modeType == Mode.ARTICULATION_POINTS){
			g2D.setColor(Color.MAGENTA);
			int size = scale>10? 8 : 4;
			for(Node n : articulationPoints)
				n.draw(g2D, origin, scale, size);
			for(Node n : cols.getUnionFind().getSets()){
				g2D.setColor(Color.RED);
				n.draw(g2D, origin, scale, size);
			}
			g2D.setColor(Color.BLACK);
		}

		if(currentIntersection!=null){
			g2D.setColor(Color.green);
			int size = scale>10? 8 : 4;
			currentIntersection.draw(g2D,origin,scale,size);
		}
		g2D.setColor(Color.BLACK);

		//debug code for seing the nodes explored
		/*if(debugSearch!=null){
			g2D.setColor(Color.CYAN);
			for(Node n : debugSearch){
				n.draw(g2D, origin, scale, 6);
			}
		}
				g2D.setColor(Color.BLACK);

		 */


		//debug code for seeing quadtree visually
		//cols.getQuadTree().draw(g2D,origin,scale);
	}



	/*
	 * 
	 * ASSIGNMENT 2 ALGORITHMS BELOW
	 * 
	 * 
	 */

	//TODO fix when goal intersection is in a different set from current. Can never reach so forever search in loop ))))):(((((
	public void AStarSearch(){
		int count=0;
		debugSearch = new ArrayList<Node>(); //debug code

		PriorityQueue<AStarNode> fringe= new PriorityQueue<AStarNode>();
		for(Node n:cols.getNodeMap().values()){
			n.setVisited(false);
			n.setNodeFrom(null);
			n.setSegTraveled(null);
			n.setPathDist(0);
		}

		AStarNode startNode = new AStarNode(currentIntersection, null, null, 0, currentIntersection.getLoc().distanceTo(goalIntersection.getLoc()));
		fringe.add(startNode);
		while(!fringe.isEmpty()){
			AStarNode searchNode = fringe.poll();
			// debug code for seeing nodes explored
			debugSearch.add(searchNode.getRepNode());
			drawing.repaint();
			Node intersection= searchNode.getRepNode();
			if(!intersection.isVisited()){
				intersection.setVisited(true);
				count++;
				intersection.setNodeFrom(searchNode.getFromNode());
				intersection.setPathDist(searchNode.getDistToHere());
				intersection.setSegTraveled(searchNode.getSegTraveled());
				if(intersection.equals(goalIntersection))break;
				for(Segment s:intersection.getEdgesOut()){
					Node neighbour;
					if(s.getNode1()==intersection.getID())neighbour=cols.getNodeMap().get(s.getNode2());
					else neighbour=cols.getNodeMap().get(s.getNode1());
					if(neighbour!=null && !neighbour.isVisited()){
						double distToNeigh=searchNode.getDistToHere()+s.getLength();
						double estTotalDist=distToNeigh+neighbour.getLoc().distanceTo(goalIntersection.getLoc());
						AStarNode newAStarNode = new AStarNode(neighbour, intersection, s, distToNeigh, estTotalDist);
						fringe.add(newAStarNode);
					}
				}
			}
		}
		System.out.println(count);
		drawing.repaint();

	}

	public void articulationPointsSearch(){
		articulationPoints = new HashSet<Node>();
		//System.out.println(cols.getUnionFind().getSets().size());
		for(Node n : cols.getNodeMap().values()){
			n.setDepth(-1);			
		}

		Node root = null;
		for(Node n : cols.getUnionFind().getSets()){
			System.out.println("algood?");
			root = n;


			root.setDepth(0);
			int numSubtrees=0;

			for(Node node : root.getNeighbours()){
				if(node.getDepth()==-1){
					recArtPts(node, 1, root);
					numSubtrees++;
				}
			}
			if(numSubtrees>1)articulationPoints.add(root);		
		}
	}

	public int recArtPts(Node node, int depth, Node fromNode){
		node.setDepth(depth);
		int reachBack = depth;
		for(Node n : node.getNeighbours()){
			if(n.equals(fromNode))continue;
			if(n.getDepth()>-1)
				reachBack = (Math.min(n.getDepth(),reachBack));
			else{
				int childReach=recArtPts(n, depth+1, node);
				reachBack = (Math.min(childReach, reachBack));
				if(childReach>=n.getDepth())
					articulationPoints.add(node);
			}
		}
		return reachBack;
	}




	public static void main(String[] args){
		RoadMap rM = new RoadMap();
	}
}
