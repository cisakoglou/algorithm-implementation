package airportscheduling;

public class Flight {
	private int id; // kwdikos ptisis 
	private Node from;
	private Node to;
	public Flight(Node f,Node t,int id){
		from=f;to=t;this.id=id;
	}
	// methodoi prospelasis
	public Node getFrom(){
		return from;
	}
	public Node getTo(){
		return to;
	}
	@Override
	public String toString(){
		return "Flight:"+"("+id+")"+"["+from+"-"+to+"]";
	}
}
