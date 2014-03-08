package airportscheduling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

// h vasikh klasi tou programmatos - to arxiko provlima anagetai se vimata sto provlima megistis rois
// opou kai efamozetai o algorithmos Ford-Fulkerson sto grafima pou mexri ekeini ti stimgi exei kataskeuastei
// kai elegxetai an uparxe efikti lush
public class FordFulkerson {
	public static Network fn; // to grafima mou
	public static int FLOW; // antistoixei ston aritmo twn aeroplanwn pou diathetw gia tin ulopoihsh
	// tou xronodiagrammatos kai anagwgika sti roi pou parexw gia na metakinhthei mesa sto grafima
	public static int FLIGHTS;
	public static ArrayList<Edge> path; // tha apothikeuetai to epauksimeno monopati pou vriskei se kathe epanalipsi sto diktuo
	public static HashMap<Integer,Flight> flights;
	
	public static void main(String[] args){
		flights=new HashMap<Integer,Flight>();
		fn = new Network();
		// **** KATASKEYH GRAFHMATOS *****
		/*
		 * Topothetw sto grafima
		 * arxi+telos me zitisi
		 * komvoi ptisewn me zitisi
		 * akmes pou enwnoun arxi+telos me komvous ptisewn
		 */
		
		
		//diavasma apo arxeio
		// kataskeui grafimatos xwris katw oria-metatrapontai stis katalliles zitiseis komvwn apeutheias
		// oi arxikes zitiseis komvwn (xwris tis arxis kai tou telous) einai mhdenikes
		// i zitisi tou komvou apo ton opoio ksekina i ptisi tha ginei trexousa_zitisi + katw_orio ara 
		// i zitisi tha einai sunolika +katw_orio(demmand)
		// kai antistoixa i zitisi tou komvou ton opoio exei san afetiria i ptisi tha einai -katw_orio(supply)
		
		// oi komvoi pou tha dimiourgw gia kathe ptisi 
		// den dimourgw akmi gia kathe ptisi kathws
		// kata ti diadikasia metatropis twn katw oriwn stis zitiseis twn komvwn
		// h xwrhtikotita tis kathe akmis(1) tha meiwthei kata to katw orio(1)
		// me apotelesma i kainouria xwrhtikotita tis akmis na mhdenistei
		// kai i akmi auti na einai san na min uparxei
		// (sthn ousia omws uparxei afou gia na vrw to teliko xronodiagramma sumperilamvanw
		// kai tin upoxrewtiki roh pou pernouse apo auti thn akmi) <- oxi kali eksigisi
		Node a,b;
		// xrisimeuoun gia diavasma apo arxeio
		BufferedReader br;
		String s;
		String delims = "[ ]";
		String[] tokens;
		Edge ae,be,aeb,beb;//gia kathe komvo dimourgeitai mia akmi pou tin enwnei me tin arxi i to telos analoga an einai komvos afetirias i proorismou
		try {
			br = new BufferedReader(new FileReader("input.txt"));
			FLOW = Integer.parseInt(br.readLine()); 
			FLIGHTS = Integer.parseInt(br.readLine());
			fn.createAdj(2*FLIGHTS+4); // tosoi komvoi sto grafima - 2 gia kathe ptisi + 2 arxi kai telous pou anaferontai sthn ekfwnhsh +  2 gia thn apaloifh zhthsewn
			// kataskeui arxi kai telous (antikeimena tupou Node me mhdenikous xronous)
			// antistoixes zitiseis analoga me ton arithmo twn aeroplanwn pou thelw na xrisimopoihthoun
			fn.setSandT(new Node("s",0,0,-FLOW), new Node("t",0,0,FLOW));
			while ((s=br.readLine()) != null){ // se kathe grammi antistoixizetai mia ptisi
				tokens = s.split(delims);
				a = new Node(tokens[0],Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]),1); // from 
				ae = new Edge(fn.getS(),a,1,1,false);
				fn.addEdge(ae);
				// prosthetw backward akmi me arxikopoihmeni mhdenikh upoleipomeni xwrhtikotia
				aeb = new Edge(a,fn.getS(),1,0,true); 
				fn.addEdge(aeb);		
				b = new Node(tokens[3],Integer.parseInt(tokens[4]),Integer.parseInt(tokens[5]),-1); // to
				be = new Edge(b,fn.getT(),1,1,false);
				fn.addEdge(be);
				beb = new Edge(fn.getT(),b,1,0,true);
				fn.addEdge(beb);
				fn.addNode(a);fn.addNode(b);
				// dimiourgia antistoixis ptisis kai diavasma kwdikou ptisis
				flights.put(Integer.parseInt(tokens[6]), new Flight(a,b,Integer.parseInt(tokens[6])));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * nea arxi kai telos gia apaloifi zitisewn
		 */
		fn.clearDemands();
		/*
		 * prosthiki twn akmwn pou anaparistoun tis prospelasimes ptiseis sto ditkuo pou kataskeuastike
		 * se kathe grammi tou input kai apo ena zeugari prospelasimwn ptisewn
		 */
		Edge temp;
		try{
			br = new BufferedReader(new FileReader("input2.txt"));
			while ((s=br.readLine()) != null){
				tokens = s.split(delims);
				temp = new Edge(flights.get(Integer.parseInt(tokens[0])).getTo(),flights.get(Integer.parseInt(tokens[1])).getFrom(),1,1,false);
				fn.addEdge(temp);
				temp = new Edge(flights.get(Integer.parseInt(tokens[1])).getFrom(),flights.get(Integer.parseInt(tokens[0])).getTo(),1,0,true);
				fn.addEdge(temp);
			}
			br.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		//fn.printAllEdges();
		// set capacity from source sN = an h megisti timi rois einai isi me auti
		// diladi oi akmes pou ksekinoun apo tin pigi einai koresmenes simainei oti uparxei efikti lisi sto provlima
		// me to sugkekrimeno arithmo aeroplanwn pou dothikan
		fn.setCapacity();
		// ***** ANAZHTHSH GIA EFIKTH LYSH *****
		int value=0;//timi megistis rois - ksekina apo 0 kai se kathe epanalipsi tha auksanei toulaxiston kata bottleneck
		Node iT,iS;
		while (fn.hasAugmentingPath()){ // oso uparxei diadromi sN - tN
			// orizw bottleneck me vasi to epauksimeno monopati pou tha akolouthisei i roi
			int bottleneck = Integer.MAX_VALUE;
			iT=fn.getTN();
			iS=fn.pathStartedFrom(iT);
			while(iS!=fn.getSN()){
				bottleneck = Math.min(bottleneck, fn.pathResidualCapacity(iS,iT));
				iT=iS;
				iS=fn.pathStartedFrom(iT);
			}
			bottleneck = Math.min(bottleneck, fn.pathResidualCapacity(iS,iT));  //ektelei mia akomi fora gia na sumperilavei kai tin akmi sN-s
			// metavallw tis upoloipomenes xwrhtikotites
			iT=fn.getTN();
			iS=fn.pathStartedFrom(iT);
			while(iS!=fn.getSN()){
				fn.adjustResiduals(iS,iT,bottleneck);// gia tin euthidromi
				fn.adjustResiduals(iT, iS, -bottleneck); // gia tin anadromi
				iT=iS;
				iS=fn.pathStartedFrom(iT);
			}
			fn.adjustResiduals(iS,iT,bottleneck);
			fn.adjustResiduals(iT, iS, -bottleneck);
			// auksanw tin timi tis mexri twra megistis rois kata to bottleneck
			value+=bottleneck;
		}
		if (value==fn.getCapacity()){
			System.out.println("Yparxei efikth lush");
			fn.printFlow(); // tupenei xronodiagramma - seira ptisewn kai endiameses metafores twn aeroplanwn pou apaitountai anamesa stous komvous
		}
		else System.out.println("Dokimase ksana");
		
	}
}
