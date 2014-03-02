package reduction2sat;

import java.util.*;

public class Main {
	public static int nVar;// o arithmos twn metavlitwn
	public static int nClauses; // oarithmos twn frasewn
	public static List<Literal> literals; // oi metavlites pou tha uparxoun ston tupo
	public static List<Literal> toPickFrom; // oi metavlites kai oi sumphrwmatikes tous gia na epileksw tuxaia poia tha xrhsimopoihthoun stis fraseis
	public static boolean compFound; // flag gia to an kata ti diasxisi sunanthsw to sumplhrwmatiko ths ekastote metavlitis
	public static void main(String[] args){
		String[] a = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		Set<Clause> formula = new HashSet<Clause>();
		literals = new ArrayList<Literal>(); // antistoixoun sta variables
		toPickFrom = new ArrayList<Literal>();
		Graph graph = new Graph();
		
		// dexetai input apo xristi
		Scanner input = new Scanner(System.in);
		System.out.println("#variables : ");
        nVar = input.nextInt();
        System.out.println("#clauses : ");
        nClauses = input.nextInt();
        
		//create variables - literals
        // me automato tropo - tuxaies fraseis
        for (int i =0;i<nVar;i++){
        	Literal temp = new Literal(a[i],null);
        	literals.add(temp);
        	toPickFrom.add(temp);
        	toPickFrom.add(temp.getComplement());
        }
        //create formula
        for (int i=0;i<nClauses;i++){
        	formula.add(new Clause(pickRandomly(),pickRandomly()));
        }
        // na tupwnei ton tupo
        Iterator<Clause> iterator = formula.iterator();
        while (iterator.hasNext()) {
        	System.out.print(iterator.next()); if (iterator.hasNext()) System.out.print(" AND ");
        }
        System.out.println();
        // paradeigma apo diafaneies
		/*
        Literal x = new Literal("x",null);literals.add(x);
		Literal y = new Literal("y",null);literals.add(y);
		Literal z = new Literal("z",null);literals.add(z);
		formula.add(new Clause(x.getComplement(),y));
		formula.add(new Clause(y.getComplement(),z));
		formula.add(new Clause(x,z.getComplement()));
		formula.add(new Clause(z,y));
		// formula.add(new Clause(z.getComplement(),y.getComplement())); mi alitheusimos o tupos me auto to clause
		*/
		// dhmiourgw komvous gia variables kai ta sumplhrwmatika touw
		for (Literal literal:literals){
			graph.addNode(literal);
			graph.addNode(literal.getComplement());
		}
		//dhmiourgw akmes 
		for (Clause c : formula){
			//a->b
			graph.addEdge(c.getFirst().getComplement(),c.getSecond());
			//not_b->not_a
			graph.addEdge(c.getSecond().getComplement(),c.getFirst());
		}
		//dfs gia kathe variable me arxi to idio
		boolean satisfiable = true;
		for (Literal l : literals){
			graph.setUnvisited();
			compFound=false;
			graph.dfs(l,l);
			if (compFound==true){ 
				graph.setUnvisited();
				compFound=false;
				graph.dfs(l.getComplement(),l.getComplement());
				if (compFound == true) { // an ksekinwntas apo ena variable kataliksw sto sumplhrwmatiko tou kai
					satisfiable = false;  // an ksekinwntas apo to sumplhrwmatiko kataliksw sto variable den uparxei alhthopoios timodosia
					break;
				}
			}
		}
		if (satisfiable == true) System.out.println("Satisfiable Boolean Expression\n");
		else System.out.println("Not Satisfiable Boolean Expression\n");
		// tupwnw ti timodosia pou ikanopoiei ton tupo i mia opoiadhpote otan o logikos tupos den ikanopoieitai
		graph.giveValues();
	}

	private static Literal pickRandomly(){
		return toPickFrom.get((int)((nVar*2)*Math.random()));
	}
}
