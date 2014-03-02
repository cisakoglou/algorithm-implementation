package reduction2sat;

// antistoixei sti frasi pou periexei 2 leksigrammata
// kathena apo autes tha metatrepetai se 2 akmes

public class Clause {
	private Literal first,second;
	Clause(){
		first=second=null;
	}
	Clause(Literal l1,Literal l2){
		first=l1;second=l2;
	}
	public void addFirst(Literal l){
		first=l;
	}
	public void addSecond(Literal l){
		second=l;
	}
	public Literal getFirst(){
		return first;
	}
	public Literal getSecond(){
		return second;
	}
	@Override
	public String toString(){
		return "(" + first + " OR "+ second + ")";
	}
}
