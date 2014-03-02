package airportscheduling;

/* diaforopoiw tin akmi apo tin ptisi giati par' olo pou oi ptiseis antistoixoun se akmes(toulaxiston sto arxiko grafima- sto teliko exoun mhdenikh xwrhtikotita
 * den antistoixoun oles oi akmes se ptiseis.
 * etsi i klasi Flights xrisimeuei stin anaparastasi tis arxikis pliroforias
 * kai i klasi edge gia tin anaparastasi kai tis prosthetis pliroforias pou xreiazetai gia na
 * epiluthei to provlima px oi prospelasimes ptiseis anaparistantai me akmi
 */
public class Edge {
	private Node from; // komvos anaxwrhshs pthshs
	private Node to; // komvos proorismou
	private int capacity;
	private int flow;
	private int residualCapacity; // upoleipomenh xvrhtikothta - xrhsimeuei gia thn arxh diathrhshsh stis sunthikes ths xwrhtikotitas
	public boolean isBackward;// true an einai einai anadromi sto upoleipomeno grafima 
	public Edge(Node f,Node t,int c,int rc,boolean flag){
		from=f;to=t;capacity=c;flow=0;residualCapacity=rc;isBackward=flag;
	}
	// methodoi prospelasis
	public Node getFrom(){
		return from;
	}
	public Node getTo(){
		return to;
	}
	public int getFlow(){
		return flow;
	}
	public int getCapacity(){
		return capacity;
	}
	public int getResidualCapacity(){
		return residualCapacity;
	}
	public boolean isBackward(){
		return isBackward;
	}
	// oi elegxoi gia to an prokeitai gia anadromi i euthidromi akmi pou tha kathorisei to pws tha diamorfwthei i upoleipomenh xwrhtikothta kai i roi ths akmis ginontai sth main
	public void setResidualCapacity(int f){
		residualCapacity -= f;
	}
	public void setFlow(int f){
		flow += f; 
	}
	// gia na mporw na vrw poios einai o komvos stin alli akri tis akmiss
	public Node other(Node c){
		if (c.getId()==from.getId()) return to;
		else if (c.getId() == to.getId()) return from;
		else return null;
	}
	@Override
	public String toString(){
		return from+"-"+to;
	}
}
