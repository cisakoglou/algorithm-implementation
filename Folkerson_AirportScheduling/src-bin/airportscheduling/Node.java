package airportscheduling;


/* anaparista tous komvous tou grafimatos pou anaparista to diktuo rois
*  gia ton kathena xreiazetai na gnwrizw onoma aerodromiou,xrono pou tha prepei na vriskomai se auton, zhthsh
**/
public class Node {
	private String name;
	private int id;
	private static int counter = 0;
	// xronos pou tha vrisketai sto aerodromio
	private int timeh;// wres 
	private int timem; // lepta
	private int demand; // zitisi komvou - aksiopoieitai gia tin kataskeui tou telikou grafimatos 
	public Node(String n,int th,int tm,int d){
		name=n;timeh = th;timem=tm;demand=d;
		id = counter;
		counter++;
	}
	// methodoi prospelasis
	public int getDemand(){
		return demand;
	}
	public String getName(){
		return name;
	}
	public int getId(){
		return id;
	}
	@Override
	public String toString(){
		return name+"("+timeh+"."+timem+")";
	}
	// gia anagnwrisi an prokeitai gia ton idio komvo - tha elegxetai to id - kai oxi to onoma tou aerodromiou
	// mias kai mporei na perasw apo ena aerdromio parapanw apo 1 fora se diaforetikous xronous
	@Override
	public boolean equals(Object obj){
		if (this==obj) return true;
		if (!(obj instanceof Node)) return false;
		Node n = (Node)obj;
		if (id == n.id) return true;
		return false;
		
	}
}
