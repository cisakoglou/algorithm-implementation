/** Χριστίνα Ισάκογλου 2056
  * christci@csd.auth.gr
  */
	
#include <iostream>
#include <cstdlib>
#include <fstream>
#include <vector>  // χρησιμοποιώ διανύσματα,στοίβες,σωρό μεγίστων(ουρά προτεραιότητας) από STL
#include <stack>
#include <queue>
#include <unordered_map> // και την υλοποίηση του κατακερματισμού από TR1(επέκταση της C++0x) -- εναλλακτικά θα μπορούσα να χρησιμοποιήσω map από STL
			 // η αναζήτηση όμως στοιχείων θα πρέπει να γίνεται σε σταθερό χρόνο και όχι σε λογαριθμικό όπως γίνεται με τη δεύτερη αφού υλοποιεί τη δομή
			 // ως δυαδικό δέντρο και όχι ως πίνακα. Αντίθετα στην περίπτωση που με ενδιαφέρει η μνήμη ή το μέγεθος της εισόδου είναι πολύ μεγάλο η πρώτη 
			 // θα πρέπει να αποφεύγεται λόγω του γραμμικού της κόστους στη χειρότερη περίπτωση (πολλές συγκρούσεις)
			 // (για ενεργοποίηση μεταγλώττισης από gcc : $ g++ -std=c++0x StableMatching.cpp)


using namespace std;
typedef unordered_map<int,int> hmap;

// υλοποιώ δύο κλάσεις για πανεπιστήμια και φοιτητές αντίστοιχα
// όπου θα έχω αποθηκευμένα τα στοιχεία που χρειάζομαι για την υλοποίηση του αλγορίθμου
class College
{
public:
    int id; 
    int positions;
    priority_queue<int> ranking; // σωρός μεγίστων που θα περιέχει τις αξιολογήσεις των φοιτητών που μέχρι την παρούσα στιγμή έχει επιλέξει
                                 // περισσότερο επιθυμητός θεωρείται ο φοιτητής που έχει μικρότερο αριθμό αξιολόγησης 
    hmap lowerRanking; // πίνακας κατακερματισμού για να ανιστοιχίζονται οι αξιολογήσεις των φοιτητών ενός πανεπιστημίου με τους ίδιους τους φοιτητές
    vector<int> collegePref; // διάνυσμα στο οποίο αποθηκεύται η λίστα προτίμησης
    int nextPref; // μεταβλητή η οποία αποτελεί δείκτη της θεσης του επόμενου φοιτητή στη λίστα προτεραίοτητας του πανεπιστημίου στον οποίο θα κάνει πρόταση
    College()
    {
        positions = 0;  
        nextPref = 0; // αρχικά θέλω το πανεπιστήμιο να κάνει πρόταση στον πρώτο φοιτητή της λίστας του
    }
};

class Student
{
public:
    int sid;
    vector<int> studentPref; // αντίστοιχα όπως και με τα πανεπιστήμια οι λίστες προτεραίοτητας των φοιτητών και ο δείκτης του επόμενου πανεπιστημίο για πρόταση
    int sNextPref;
    int currentState; // η τρέχουσα κατάσταση του φοιτητή : 0 όταν δεν έχει επιλεγεί ακόμα από κανένα πανεπιστήμιο, 1 όταν κάποιο πανεπιστήμιο έχει δηλώσει ότι τον 
 		       // δέχεται και -1 όταν δεν του απομένουν άλλα πανεπιστήμια για να κάνει πρόταση και άρα είτε βρίσκεται στο πανεπιστήμιο της τελευταίας του 	 			       // επιλογής είτε τον έχουν απορρίψει όλα τα πανεπιστήμια
    Student()
    {
        currentState = 0;    // αρχικά θέλω κανένας φοιτητής να μη θεωρείται επιλεγμένος και να κάνει πρόταση στο πρώτο πανεπιστήμιο της αρεσκείας του
        sNextPref = 0;
    }
};

int main(int argc,char* argv[])
{
    hmap matches; // πίνακας κατακερματισμού στον οποίο αποθηκεύονται τα ζευγάρια πανεπιστημίων και φοιτητών δεκτών σε αυτά
			  // τα ζευγάρια που διατηρούνται μέχρι το τέλος είναι και τα τελικά - το κλειδί στο hash είναι το sid των φοιτητών
    ifstream in;
    ofstream out;

    // αρχικοποιώ τις δομές που έχω κατασκευάσει μέχρι τώρα διαβάζοντας από το αρχείο
    in.open("prefs.txt",ios::in);
    out.open("output.txt",ios::out);
    int numberOfColleges;
    int numberOfStudents;
    College* collegeInstance; // δύο δείκτες σε αντικείμενα των κλάσεων college και student όπου θα δεσμεύσω δυναμικά πίνακες ανάλογα με τον αριθμό 
    Student* studentInstance; // πανεπιστημίων και φοιτητών που θα διαβάσει από το αρχείο
    in>>numberOfColleges>>numberOfStudents;
    collegeInstance = new College[numberOfColleges];
    studentInstance = new Student[numberOfStudents];
    int next; // προσωρινή μεταβλητή 
    while (!in.eof())
    {
        for (int i=0; i<numberOfColleges; i++)
        {
            in>>collegeInstance[i].positions;
            collegeInstance[i].id = i+1;
            for (int j=0; j<numberOfStudents; j++)
            {
                in>>next;
                collegeInstance[i].collegePref.push_back(next);
            }
        }
        for (int z=0; z<numberOfStudents; z++)
        {
            for (int w=0; w<numberOfColleges; w++)
            {
                in>>next;
                studentInstance[z].studentPref.push_back(next);
                studentInstance[z].sid = z+1;
            }
        }
    }
    in.close();
    int choise = atoi(argv[1]);
    if (choise == 1)
    /* περίπτωση όπου τα πανεπιστήμια κάνουν προτάσεις -- τα πανεπιστήμια θα κάνουν προτάσεις ξεκινώντας απο τους πρωτους στη λίστα προτεραίοτητας του φοιτητές 
       και σταδιακά θα χαμηλώνουν τα στάνταρ τους, μεχρι να γεμίσουν τις θέσεις τους. Η πρώτη περίπτωση αστάθειας δε γίνεται να συμβεί αφού αν κάποια σχολή επιθυμεί
       κάποιον φοιτητή και δεν τον έχει θα σημαίνει ότι ο φοιτητής προτιμά κάποιο άλλο πανεπιστήμιο, δεδομένου ότι τα πανεπιστήμια ξεκινούν απο τους υψηλότερους
       στη λίστα τους φοιτητές. Επομένως δε διατρέχεται κίνδυνος ευστάθειας στη συγκεκριμένη ανάθεση. Με τον ίδιο τρόπο αποκλείεται και η δεύτερη περίπτωση αστάθειας
	αφού αν υπήρχε κάποιος φοιτητής διαθέσιμος που καποιο πανεπιστήμιο επιθυμούσε θα του είχε κάνει ήδη πρόταση και αυτός θα έπαυε να είναι διαθέσιμος. 
	Επίσης το γεγονός ότι οι θέσεις κάποια στιγμή σίγουρα θα γεμίσουν και αποκλείεται κάποια θέση να μείνει κενή έγκειται στο  ότι οι συνολικές
	θέσεις είναι λιγότερες από τους φοιτητές και οι φοιτητές δεν έχουν το δικαίωμα να απορρίψουν μία σχολή εφόσον είναι διαθέσιμοι ακόμα κι αν αυτή είναι
	η τελευταία τους επιλογή
	*/	
    {
        stack<College> free; // στοίβα για να αποθηκεύω τα πανεπιστήμια που έχουν ακόμα διαθέσιμες θέσεις
        for (int i=0; i<numberOfColleges; i++)
            free.push(collegeInstance[i]);

        int ranking[numberOfStudents][numberOfColleges]; // κατασκευάζω με κυβική πολυπλοκότητα(στη χειρότερη περίπτωση) έναν δισδιάστατο πίνακα 
        for (int i=0; i<numberOfStudents; i++)           // προκειμένου να βρίσκω σε σταθερό χρόνο όταν ο φοιτητής δεν είναι διαθέσιμος
        {                                                // ποιο απο τα δύο πανεπιστήμια προτιμάει- αυτό που του κάνει πρόταση η αυτό στο οποίο βρίσκεται  
            for (int j=0; j<numberOfColleges; j++)       // και έτσι αποφεύγω το γραμμικό κόστος που θα απαιτούσε η διάσχιση της λίστας προτίμησης του εναλλακτικά
            {
                for (int w=0;w<numberOfColleges;w++)
                {
                    if (studentInstance[i].studentPref.at(w) == j+1) ranking[i][j] = w+1;
                }
            }

        }
	// ο βρόγχος while θα επαναληφθεί στη χειρότερη περίπτωση τόσες φορές όσες και τον πληθάριθμο του καρτεσιανού γινομένου (φοιτητής,πανεπιστήμιο)
	// όσες δηλαδή και οι προτάσεις που θα μπορούσαν να γίνουν συνολικά. Αυτό ισούμαι με NM. Μέσα στο βρόγχο δεν υπαχει κάποια βασική πράξη η οποία
	// να επιβαρύνει παραπάνω το πρόγραμμα καθώς όλες πραγματοποιούνται σε Ο(1) χάρη στις παραπάνω δομές και άρα η συνολική πολυπλοκότητα του 
	// αλγορίθμου είναι O(NM)

        while (!free.empty())
        {
            int collegeProposing = free.top().id ; // επιλέγω το πανεπιστήμιο που βρίσκεται στην κορυφή της στοίβας των διαθέσιμων χωρίς κάποιο κριτήριο
            next = collegeInstance[collegeProposing-1].nextPref;
            int studentProposed = collegeInstance[collegeProposing-1].collegePref.at(next);
            if (studentInstance[studentProposed-1].currentState == 0) // αν ο φοιτητής στον οποίο γίνεται πρόταση είναι διαθέσιμος
            {
                matches[studentProposed]=collegeProposing; // τοποθετώ το ζευγάρι φοιτητή-πανεπιστήμιο στον πίνακα κατακερματισμού
                collegeInstance[collegeProposing-1].positions--; // μειώνω τις διαθέσιμες θέσεις του πανεπιστημίος
                if (collegeInstance[collegeProposing-1].positions == 0) // αν δεν έχει άλλες θέσεις διαθέσιμες το πανεπιστήμιο τότε βγαίνει από τη στοίβα των διαθέσιμων
                {
                    free.pop();  // εισαγωγή και διαγραφή από τη στοίβα σε σταθερό χρόνο
                }
                collegeInstance[collegeProposing-1].nextPref++; // αυξάνω τον δείκτη του επόμενου που θα ρωτήσει,έτσι ώστε να εξασφαλίσω
								// ότι σε περίπτωση ο φοιτητής στην πορεία προτιμήσει κάποιο άλλο πανεπιστήμιο
								// το πανεπιστήμιο που απέρριψε να μη του ξανακάνει πρόταση
                studentInstance[studentProposed-1].currentState = 1; 
            }
            else // όταν ο φοιτητής στον οποίο γίνεται πρόταση είναι δεν είναι διαθέσιμος προκύπτουν δύο περιπτώσεις
            {
                int currentCollege = matches[studentProposed];
		// ο φοιτητής να προτιμάει το καινούριο πανεπιστήμιο που του κάνει πρόταση και οπότε να κάνει μετεγγραφεί σε αυτό
                if ( (studentInstance[studentProposed-1].currentState == 1) &&
                        (ranking[studentProposed-1][collegeProposing-1]<ranking[studentProposed-1][currentCollege-1]) )  
                {
                    matches[studentProposed]=collegeProposing;
                    collegeInstance[collegeProposing-1].nextPref++;
                    collegeInstance[collegeProposing-1].positions--; 
                    free.push(collegeInstance[currentCollege-1]); // το πανεπιστήμιο που άφησε ο φοιτητής πρέπει να ξαναθεωρηθεί διαθέσιμο αφού ελευθερώθηκε μια θέση
                    collegeInstance[currentCollege-1].positions++;
                }
                else //ή ο φοιτητής να παραμείνει σε αυτό που ήδη βρίσκεται 
                {
                    collegeInstance[collegeProposing-1].nextPref++;
                }
            }
        }

    }
    /* περίπτωση που οι φοιτητές ρωτάνε -- ο αλγόριθμος επαναλαμβάνεται όσο υπάρχει κάποιος φοιτητής που δεν έχει επιλεχτεί από κανένα πανεπιστήμιο και όμως δεν 
       έχει απορριφτεί ακόμα από όλα. Όμοια με την άλλη περίπτωση δεν υπάρχει περίπτωση αστάθειας, αν ένας φοιτητής προτιμάει ένα πανεπιστήμιο και τον προτιμά και
       αυτό, η πρόταση θα είχε γίνει από τις πρώτες και ο φοιτητής θα είχε γραφτεί στο αντίστοιχο πανεπιστήμιο. Σε κάθε άλλη περίπτωση σημαίνει ότι το πανεπιστήμιο
       επέλεξε τελικά κάποιον άλλον αντι αυτόν και έτσι δε τίθεται θέμα διακινδύνευσης της αστάθειας. Η διαφορά συγκριτικά μς την άλλη περίτπωση είναι πως
       τα πανεπιστήμια, όταν δεν έχουν κενές θέσεις και κάποιος φοιτητής τους κάνει πρόταση, θα πρέπει να συγκρίνουν όλους τους φοιτητές που έχουν με το φοιτητή που
       τους κάνει πρόταση. Το πρόβλημα λύνεται με τη χρήση ενός σωρού μεγίστων σε συνδυασμό με έναν πίνακα κατακερματισμόυ, οπότε και θα μπορώ να εξάγω σε σταθερό 
	χρόνο τον λιγότερο επιθυμητό απο τη σχολή και να τον αντικαθιστώ με το καινούριο(μετά από κάθε αντικατάσταση ο σωρός αναδομείται γαι να διατηρεί την ιδιότητα του
	και έτσι θα έχω και την επόμενη φορά τον καινούριο λιγότερο επιθυμητό φοιτητή του πανεπιστημίου)
	*/
    else if (choise == 2)
    {
	// παρόμοια όπως και στην προηγούμενη περίπτωση δημιουργώ μια στοίβα για τους διαθέσιμους φοιτητές
	// και έναν δισδιάστατο πίνακα για να μπορώ να συγκρίνω τις προτιμήσεις τους
        stack<Student> free;  
        for (int i=0;i<numberOfStudents;i++)
            free.push(studentInstance[i]);
        int ranking[numberOfColleges][numberOfStudents];
        for (int i=0; i<numberOfColleges; i++)
        {
            for (int j=0; j<numberOfStudents; j++)
            {
                for (int w=0; w<numberOfStudents;w++)
                {
                    if (collegeInstance[i].collegePref.at(w) == j+1) ranking[i][j] = w+1;
                }

            }

        }
	/* όμοια με άλλη περίπτωση ο συνολικός χρόνος ανάθεσης είναι Ο(ΝΜ)  
	   αφού οι ενέργειες στο εσωτερικό της επανάληψης δεν έχουν κάποια επιπλέον επανάληψη ή πράξη που θα μπορούσε να γίνει υπερβολικά χρονοβόρα για
	   τον υπολογιστή
	*/
        while (!free.empty())
        {
            int studentProposing = free.top().sid;
            next = studentInstance[studentProposing-1].sNextPref;
            int collegeProposed = studentInstance[studentProposing-1].studentPref.at(next);
            if (collegeInstance[collegeProposed-1].positions != 0) // περίπτωση το πανεπιστήμιο να έχει κενές θέσεις αρά μπορεί να δεχτεί απευθείας το φοιτητή
            {
                matches[studentProposing] = collegeProposed;
                collegeInstance[collegeProposed-1].positions--;
                studentInstance[studentProposing-1].sNextPref++;
                studentInstance[studentProposing-1].currentState = 1; // θεωρείται πλέον επιλεγμένος
                free.pop(); // και βγαίνει από τη στοίβα των διαθέσιμων
                collegeInstance[collegeProposed-1].ranking.push(ranking[collegeProposed-1][studentProposing-1]);
                collegeInstance[collegeProposed-1].lowerRanking[ranking[collegeProposed-1][studentProposing-1]] = studentProposing;
            }
            else
            {
                // περίπτωση το πανεπιστήμιο να είναι γεμάτο αλλά να προτιμάει το φοιτητή που του κάνει πρόταση από τον λιγότερο επιθυμητό του 
                if (ranking[collegeProposed-1][studentProposing-1] < collegeInstance[collegeProposed-1].ranking.top())
                {
                    int rejectedRanking = collegeInstance[collegeProposed-1].ranking.top();
                    int rejected = collegeInstance[collegeProposed-1].lowerRanking[rejectedRanking];
                    matches[rejected] = 0;
                    studentInstance[rejected-1].currentState = 0;
                    matches[studentProposing] = collegeProposed;
                    free.pop();
                    free.push(studentInstance[rejected-1]); // η αναδόμηση του σωρού σε χειρότερη περίπτωση μπορεί να έχει λογαριθμικό κόστος 
							    // παρ' όλα αυτά επειδή το μέγεθος του σωρού θα είναι κατά πολύ μικρότερο από το συνολικό είσοδος του
							    // αλγορίθμου, θεωρώ ότι η πολυπλοκότητα αυτή θα αυξάνεται πολύ πιο αργά σε σχέση με την αρχική και δεν την 							    // προσμετρώ
                    collegeInstance[collegeProposed-1].ranking.pop();
                    collegeInstance[collegeProposed-1].ranking.push(ranking[collegeProposed-1][studentProposing-1]);
                    collegeInstance[collegeProposed-1].lowerRanking[ranking[collegeProposed-1][studentProposing-1]] = studentProposing;
                    studentInstance[studentProposing-1].sNextPref++;
                    studentInstance[studentProposing-1].currentState = 1;
                }
                else //περίπτωση που το πανεπιστήμιο είναι γεμάτο και προτιμάει τους φοιτητές που έχει από αυτούς που του κάνουν πρόταση
                {
                    studentInstance[studentProposing-1].sNextPref++;
                }
		 // ελέγχω αν ο φοιτητής έχει ακόμα το δικαίωμα να κάνει πρόταση σε πανεπιστήμια ή έχει απορριφτεί από όλα και τότε βγαίνει από τη στοίβα των διαθέσιμων
                if (studentInstance[studentProposing-1].sNextPref >= numberOfColleges)
												
                {
                    studentInstance[studentProposing-1].currentState = -1;
                    free.pop();
                }

            }

        }
    }
    else
    {
        out<<"Invalid argument";
    }
    if ((choise == 1)||(choise == 2))
    {
	    for (hmap::iterator ii=matches.begin();ii!=matches.end();ii++)
	    {
		if ( ((*ii).second != 0) && ((*ii).first !=0) )
		    out << "School "<< (*ii).second<<" accepts student "<<(*ii).first<<"."<<endl;
	    }
    } 

    out.close();
    exit(EXIT_SUCCESS);
}



