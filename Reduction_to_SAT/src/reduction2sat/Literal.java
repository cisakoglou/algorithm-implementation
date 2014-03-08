package reduction2sat;

// tha antistoixei se kathe metavliti h se kathe sumplhrwmatikh metavliti
// ousiastika sto leksigramma

public class Literal {
	boolean visited; // gia dfs
	Boolean value;
	Boolean beenValued;
	Literal complement;
	String name;
	Literal(String n,Literal l){
		beenValued=false;value=false;
		name=n;visited=false;
		if (l==null){
			complement = new Literal(name+"'",this);
		}
		else complement=l;
	}
	public boolean beenValued(){
		return beenValued;
	}
	public void giveValue(boolean v){
		if (!beenValued) beenValued=true;
		value=v;
	}
	public String getValue(){
		if (value) return "true";
		else return "false";
	}
	public void setVisited(boolean v){
		visited = v;
	}
	public boolean getVisited(){
		return visited;
	}
	@Override
	public String toString(){
		return name;
	}
	Literal getComplement(){
		return complement;
	}
	@Override
	 public boolean equals(Object obj) {
	        if (obj == null)
	            return false;
	        if (obj == this)
	            return true;
	        if (!(obj instanceof Literal))
	            return false;

	        Literal l = (Literal) obj;
	        if (name.equals(l.toString())) return true;
	        return false;
	 }
	public static void main(String[] args){
		Literal a=new Literal("x",null);
		System.out.println(a.getComplement().getComplement());
	}
}
