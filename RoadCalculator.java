import big.data.DataSource;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.lang.Throwable;
import java.util.Stack;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

class Node{
	private String name;
	private HashSet<Edge> edges;
	private boolean visited;
	private LinkedList<String> path;
	private int distance;
	
	//Constructor
	Node(String name){
		this.name = name;
		this.edges = new HashSet<Edge>();
		this.visited = false;
		this.path = new LinkedList<String>();
		this.distance = 100000;
	}
	
	//Getters and Setters
	String getName(){
		return name;
	}
	public HashSet<Edge> getEdge(){
		return edges;
	}
	
	public void addEdges(Edge A){
		edges.add(A);
	}
	
	public void setVisited(boolean val){
		visited = val;
	}
	
	public boolean getVisited(){
		return visited;
	}
	
	public void setPath(LinkedList<String> A){
		path= A;
	}
	
	public LinkedList<String> getPath(){
		return path;
	}
	
	public void setDistance(int dist){
		distance = dist;
	}
	
	public int getDistance(){
		return distance;
	}	
}

class Edge implements Comparable<Object>{
	private Node A;
	private Node B;
	private int cost;
	
	Edge(Node A, Node B, int cost){
		this.A = A;
		this.B = B;
		this.cost = cost;
	}
	
	//Getters and Setters
	public int getCost(){
		return cost;
	}
	
	public Node getNodeB(){
		return B;
	}
	public String toString(){
		return A.getName() +" --> "+B.getName();
	}
	
	//Compares the current edge’s cost to otherEdge’s cost. Returns -1 if the current edge’s cost is less than otherEdge’s cost, 0 if equal, and 1 if greater than.
	public int compareTo(Object otherEdge){
		Edge other = (Edge) otherEdge;
		if(other.getCost() < cost)
			return 1;
		
		if(other.getCost() > cost)
			return -1;
		
		return 0;
	}
}

class RoadCalculator{
	private static HashMap<String, Node> graph;
	private static LinkedList<Edge> mst;

	
	
	
	private static Node validNode(String name){
		if(!(graph.containsKey(name))){
			Node tmp = new Node(name);
			graph.put(name,tmp);
			return tmp;
		}
		return graph.get(name);
	}
	
	public static HashMap<String, Node> buildGraph(String location){
		String [] values = location.replaceAll("\"","").split(",");
		Node tmp1 = validNode(values[0]);
		Node tmp2 = validNode(values[1]);
		if(values.length<3){
			System.out.println("AQUI");
			return graph;
		}
		
		Edge link1 = new Edge(tmp1,tmp2,Integer.parseInt(values[2]));
		Edge link2 = new Edge(tmp2,tmp1,Integer.parseInt(values[2]));
		tmp1.addEdges(link1);
		tmp2.addEdges(link2);
		
		return graph;
	}
	
	public static LinkedList<Edge> buildMST(HashMap<String, Node> graph,String start){
		Stack<Node> visited = new Stack<Node>() ;
		//Pick a starting point
		Node sp = graph.get(start);
		Node next = sp;
		visited.add(sp);
		while(!(visited.empty())){
			ArrayList<Edge> edges =  new ArrayList<Edge>();
			sp.setVisited(true);
			//Iterate through it's points
			for(Edge e: sp.getEdge())
				edges.add(e);
			Collections.sort(edges);
			for(Edge e: edges){
				if(e.getNodeB().getVisited())
					continue;
				next = e.getNodeB();
				visited.add(next);
				mst.add(e);
				break;
			}
			if(next == sp){
				sp = visited.pop();
			}else{
				sp = next;
			}
		}
		return mst;
	}
	
	public static int Djikstra(HashMap<String,Node> graph, String source, String dest){
		if(!(graph.containsKey(source)) || !(graph.containsKey(dest)) ){
			System.out.println("Does not exist");
			return -1;
		}
		
		Node sp = graph.get(source);
		sp.setDistance(0);
		while(sp!=graph.get(dest)){
			Node next = sp;
			sp.setVisited(true);
			for(Edge e: sp.getEdge()){
				if(e.getNodeB().getVisited())
					continue;
				if(e.getCost()+sp.getDistance()<e.getNodeB().getDistance()){
					e.getNodeB().setDistance(e.getCost()+sp.getDistance());
					LinkedList<String> path = sp.getPath();
					path.add(sp.getName());
					e.getNodeB().setPath(path);
				}
				next = e.getNodeB();
			}
			
			//Get Next Node 
			for(Edge e: sp.getEdge()){
				if(e.getNodeB().getVisited())
					continue;
				if(e.getNodeB().getDistance() < next.getDistance())
					next = e.getNodeB();
			}
			
			sp = next;
		}
		//Print map
		LinkedList<String> path = graph.get(dest).getPath();
		
		for(String s: path)
			System.out.print(s+",");
		
		//Return number
		return graph.get(dest).getDistance();
	}
	
	private static void reset(String[] cityNames){
		for(int i=0; i<cityNames.length; i++)
			graph.get(cityNames[i]).setVisited(false);
	}
	
	private static void UImenu(){
		System.out.println("Menu:\n    C)Print Cities\n    R)Print Roads\n    M)Print MST\n    D)Djikstra\n    Q)Quit");
	}
	public static void main(String args[]){
        HashMap<String,Node> cities = new HashMap<String,Node>();
		graph = new HashMap<String,Node>();
		mst = new LinkedList<Edge>();
		
	

		System.out.print("Please enter URL: ");
		String url = System.console().readLine();
		DataSource ds = DataSource.connectXML(url);
		ds.load();
		String cityNamesStr=ds.fetchString("cities");
		String[] cityNames=cityNamesStr.substring(1,cityNamesStr.length()-1).replace("\"","").split(",");
		String roadNamesStr=ds.fetchString("roads");
		String[] roadNames=roadNamesStr.substring(1,roadNamesStr.length()-1).split("\",\"");	
		
		
		for(int i=0; i<roadNames.length; i++)
			buildGraph(roadNames[i]);
		buildMST(graph,cityNames[0]);
		
		UImenu();
		
		while(true){
			System.out.print("Please select an option: ");
			String ans = System.console().readLine();
			char option = ans.charAt(0);
			if (option == 'C' || option == 'c'){
				for(int i=0; i<cityNames.length; i++)
					System.out.println(cityNames[i]);
			}else if (option == 'R' || option == 'r'){
				for(int i=0; i<roadNames.length; i++)
					System.out.println(roadNames[i]);
			}else if (option == 'M' || option == 'm'){
				for (Edge e: mst)
					System.out.println(e.toString());
			}else if (option == 'D' || option == 'd'){
				reset(cityNames);
				System.out.print("Source: ");
				String src = System.console().readLine();
				System.out.print("Dest: ");
				String dest = System.console().readLine();
				System.out.println(Djikstra(graph,src,dest));
			}else if (option == 'Q' || option == 'q'){
				System.out.print("We congratulate you on your decision to do something more productive with your time.");
				System.exit(0);
			}else{
				System.out.println("Invalid Choice");
			}
			System.out.println("");

		}
				
		
	}

	
	
}

