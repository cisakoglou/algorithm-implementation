package reduction2sat;

import java.util.*;

public class Graph {
	private List<Literal> lNodes;
	private HashMap<Literal,ArrayList<Literal>> adj;
	Graph(){
		lNodes = new ArrayList<Literal>();
		adj = new HashMap<Literal,ArrayList<Literal>>();
	}
	public void addNode(Literal l){
		lNodes.add(l);
		adj.put(l, new ArrayList<Literal>());
	}
	public void addEdge(Literal l1,Literal l2){
		ArrayList<Literal> temp = adj.get(l1);
		if (temp.contains(l2)) return; 
		else adj.get(l1).add(l2); 
	}
	public void showEdges(){
		ArrayList<Literal> temp = new ArrayList<Literal>();
		for (Literal l:lNodes){
			temp.clear();temp=adj.get(l);
			for (Literal ll: temp){
				System.out.println(l+"-"+ll);
			}
		}
	}
	public void setUnvisited(){
		for (Literal ln : lNodes){
			ln.setVisited(false);
		}
	}
	public void dfs(Literal l,Literal original){
		l.setVisited(true);
		for (Literal lToVisit : adj.get(l)){
			if (lToVisit.equals(original.getComplement())) {
				Main.compFound=true;
			}
			if (lToVisit.getVisited() == false ) dfs(lToVisit,original);	
		}
	}
	public void giveValues(){
		// pernaei apo olous tous komvous dinontas tous times mexri na mhn apomeinei kanenas pou na na min exei parei timi
		// phgainei apo ton enan komvo stous geitounikous tou 
		for (Literal l : lNodes){
			Main.compFound=false;
			dfs(l,l);
			if ((!l.beenValued())&&(Main.compFound==false)){ // ksekinaei apo komvo pou den katalhgei sto sumplhrwmatiko tou
				l.giveValue(true);
				l.getComplement().giveValue(false);
				System.out.println(l.toString()+" : "+l.getValue());
				System.out.println(l.getComplement().toString()+" : "+l.getComplement().getValue());
				for (Literal ll : adj.get(l)){
					if (!ll.beenValued()){
						ll.giveValue(true);
						ll.getComplement().giveValue(false);
						System.out.println(ll.toString()+" : "+ll.getValue());
						System.out.println(ll.getComplement().toString()+" : "+ll.getComplement().getValue());
					}
				}
			}
		}
		// se periptwsi pou den uparxei alhthopoios timodosia kapoioi komvoi de tha paroun times
		// tis vazw tuxaia afou opws kai na einai den mporoun na ikanopoihsoun to logiko tupo
		for (Literal l:lNodes){
			if (!l.beenValued()){
				l.giveValue(true);
				l.getComplement().giveValue(false);
				System.out.println(l.toString()+" : "+l.getValue());
				System.out.println(l.getComplement().toString()+" : "+l.getComplement().getValue());
			}
		}
	}

}
