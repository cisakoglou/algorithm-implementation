package airportscheduling;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// antistoixei sto grafima pou kataskeuazw kata ti diarkeia epilushs tou provlimatos
// sthn ousia to idio grafima anaparista toso to grafima pou katadeiknuei apo poies akmes dierxetai roi 
// oso kai to upoleipomeno grafima - afou oi akmes tou mporoun na periexoun plhrofories gia roi,xwrhtikothta, upoleipomenh xwrhtikotita
public class Network {
	private int v;//# komvwn
	private int e;//# akmwn
	private List<Node> nodes;
	//lista akmwn pou ksekinoun apo kathe komvo - mesa se autes tha periexontai oi forward kai oi noites backward pou dimourgountai - tha mporouse na einai kai linked list
	private ArrayList<Edge>[] adjOut; 
	private Node s;
	private Node t;
	// gia apaloifi twn zitiswn kai metatropi tous se akmes me tis katallhles xwrhtikothtes
	private Node sN; // o komvos pou tha parexei roi
	private Node tN; // o komvos pou tha tin aporrofa
	private int D;// to sunolo twn zhthsen pou ikanopoiei o komvos sN - sunolo xwrhtikotitan akmwn pou ksekinoun apo auton
	public Network(){
		v=0;e=0;nodes=new ArrayList<Node>();
		adjOut=null;
		D=0;
	}
	@SuppressWarnings("unchecked")
	public void createAdj(int n){
		v=n;
		adjOut=(ArrayList<Edge>[])new ArrayList[v];
		for (int i = 0; i < v; i++){
            adjOut[i] = new ArrayList<Edge>();
		}
	}
	public void addNode(Node n){
		nodes.add(n);
	}
	public void addEdge(Edge e){
		// topothetei kainouries akmes pou dhmiourgountai sth lista akmwn stis opoies deixnei o kathe komvos
		int idFrom = e.getFrom().getId();
		adjOut[idFrom].add(e);
		this.e++;
	}
	public void setSandT(Node s,Node t){
		this.s=s;this.t=t;
		addNode(s);addNode(t);
	}
	// methodoi prospelasis
	public Node getS(){
		return s;
	}
	public Node getT(){
		return t;
	}
	public Node getSN(){
		return sN;
	}
	public Node getTN(){
		return tN;
	}
	public void printFlow(){
		int i = 1;
		for (Edge e:adjOut[s.getId()]){
			if (e.getFlow()>0) { // tha treksei toses fores osa aeroplana tha ksekinisoun apo to aerodromio
				System.out.println("------Airplane "+i+"-------");
				Edge currentEdge = e;
				Node currentNode = currentEdge.getTo();
				while (currentNode != t){
					if (goes(currentNode,tN)){ // prokeitai gia afethria ptisis // an o komvos sto teliko diagramma pou eftiaksa deixnei sto tN shmeinei oti apotelei afethria gia kapoia ptisi
						//vres poia ptisi einai pou ksekinaei apo auton ton komvo
						Flight f=getFlight(currentNode); 
						//tipwse tin
						System.out.println(f);
						// kainourios komvos o proorismos tis ptisis
						currentNode=f.getTo();
					}
					else { //prokeitai gia proorismo ptisis - vres epomeni afetiria
						currentNode = currentEdge.getTo();
						if (currentNode != t) System.out.println(currentEdge);
						
					}
					currentEdge = nextFlowFrom(currentNode);
				}
			}
			i++;
		}
	}
	// an uparxei akmi pou na deixnei apo ton komvo a ston b
	public boolean goes(Node a,Node b){
		for (Edge e : adjOut[a.getId()]){
			if (e.getTo()==b) return true;
		}
		return false;
	}
	// apaloifh zhthsen apo tous komvous me th dhmiourgia enos arxikou komvou kai enos telikou
	// pou tha parexoun thn apaitoumenh zhthsh kai ua aporrofoun tin prosferomeni antistoixa
 	// dhmiourgia episis twn apaitoumenwn akmwn, gia na enwnontai oi komvoi me tis zhthseis, me tous kainourious komvous
	// me tis katalliles xwrhtikotites
	public void clearDemands(){
		sN = new Node("sN",0,0,0);
		tN = new Node("tN",0,0,0);
		Edge temp;
		for (Node n : nodes){
			if (n.getDemand()<0){
				temp = new Edge(sN,n,-n.getDemand(),-n.getDemand(),false);
				addEdge(temp);
				temp = new Edge(n,sN,-n.getDemand(),0,true);
				addEdge(temp);
			}
			else{
				temp = new Edge(n,tN,n.getDemand(),n.getDemand(),false);
				addEdge(temp);
				temp = new Edge(tN,n,-n.getDemand(),0,true);
				addEdge(temp);
			}
		}
		addNode(sN);
		addNode(tN);
	}
	// athroisma zitisewn pou ikanopoiei to sN 
	public void setCapacity(){
		for (Edge e : adjOut[sN.getId()]){
			D += e.getCapacity();
		}
	}
	// epistrefei tin timi pou upologizei stin prohgoumenh sunarthsh - xreiazetai gia na ekegxthei an uparxei efikth diadromi 
	// anagkaia proypothesi gia na isxuei auto einai i timh auti einai na isoutai me ti megisti roi pou tha vrethei sto grafima
	// i alli sunthki pou prepei na isxuei alla ikanopoieitai apo mono tis logw ths ekfwnhshs einai to sunolo zhthsewn pou ikanopoei o sN
	// na einai iso me to sunolo zhthsewn pou ikanopoiei o tN(k=-(-k))
	public int getCapacity(){
		return D;
	}
	// psaxnei me bfs an uparxei epauksimeno monopati sN-tN
	public boolean hasAugmentingPath(){
		FordFulkerson.path = new ArrayList<Edge>();
		boolean marked[] = new boolean[v]; // gia na elegxw apo poious komvous exw perasei kai na min kataliskw na kanw kuklous sto grafima
		Queue<Node> toVisit =new LinkedList<Node>(); // ulopoiei oura komvwn pou mou apomenei na episkeftw
		toVisit.offer(sN);
		marked[sN.getId()] = true;
		while(!toVisit.isEmpty()){
			Node current = toVisit.poll();
			for (Edge e : adjOut[current.getId()]){
				Node next = e.other(current);
				// prwta tha elegxw an i upoleipomeni xwrhtikotita upakouei stis sunthikes xwrhtikotitas > 0
				if (e.getResidualCapacity() > 0){
					if (!marked[next.getId()]){ // kai sti sunexeia an exw perasei apo auton ton komvo
						marked[next.getId()]=true;
						FordFulkerson.path.add(e);
						toVisit.offer(next);
					}
				}	
			}
		}
		
		return marked[tN.getId()]; // epistrefei true an to monopati pou vrika ftanei mexri to tN
	}
	//oi epomenes 2 sunartiseis xrhsimeuoun thn euresh bottleneck apo ford-fulkerson
	// kai diasxisi tou epauksimenou monopatiou pou vrika
	// mesw poiou komvou kataligei to monopati pou vrika ston komvo t
	public Node pathStartedFrom(Node t){
		for (Edge e : FordFulkerson.path){
			if (e.getTo()==t) return e.getFrom();
		}
		return null;
	}
	// anamesa se autous tous 2 komvous poia einai i upoleipomenh xwrhtikotita
	public int pathResidualCapacity(Node s,Node t){
		for (Edge e: adjOut[s.getId()]){
			if (e.getTo()==t) return e.getResidualCapacity();
		}
		return -1;//periptwsi lathous - i upoleipomeni xwrhtikotita pote den prepei na ginei arnhtiki me vash tis sunthkes gia xwrhtikotita
	}
	// prosarmose tis kainouries upoleipomenes xwrhtikotites
	// eksartatai apo to an i diadromi einai anadromi i euthidromi - kai auto ginetai antistoixa apo th methodo tis akmis
	// opou i prosthesi tha ginei afairesi opou o arithmos einai arnitikos
	public void adjustResiduals(Node s,Node t,int bottleneck){
		for (Edge e: adjOut[s.getId()]){
			if (e.getTo()==t){
				if (!e.isBackward()){
					e.setFlow(bottleneck);
					e.setResidualCapacity(bottleneck);
				}
				else{
					e.setResidualCapacity(bottleneck); // to flow den allazei otan prokeitai gia anadromi akmi afou ousiastika antistoixei stin anadromi akmi tou upoleipomenou grafimatos
				}
			}
		}
	}
	// ptisi pou antistoixei se auton ton komvo afetirias
	public Flight getFlight(Node nodeFrom){
		for(Flight f:FordFulkerson.flights.values() ){
			if (f.getFrom()==nodeFrom) return f;
		}
		return null;	
	}
	
	public Edge nextFlowFrom(Node node){
		for(Edge e:adjOut[node.getId()]){
			if (e.getFlow()>0) return e;
		}
		return null;
	}
}
