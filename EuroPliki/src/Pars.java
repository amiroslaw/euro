import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
/**
 * parsowanie akrkusza quizów
 * program pobieraj¹cy dane z arkusza i na podstawie istniej¹cych katalogow i plikow generuje plik smanifest
 * @author Euro-Forum_B3
 *
 */
public class Pars {

	static String pathHome = System.getProperty("user.home");
	static File folder = new File(pathHome+"/euro");
	static File[] katalogiQuiz = folder.listFiles();
	// static List <String> katalogiQuizu= new ArrayList<>(); 
	static List <String> wiersze= new ArrayList<>(); 
	static int ileZapis=0; 
	static	int liczLinie=0; // ile wierszy
	public static void czytaj_linie() throws IOException {
		FileReader fr = new FileReader(pathHome+"/arkusz");
		BufferedReader br = new BufferedReader(fr);
		String d;
		while ((d = br.readLine()) != null) {
			//splitCol(d);
			wiersze.add(d);
			
			liczLinie++;
		}

			System.out.println(liczLinie); 
		br.close();
	}
	
	public static void splitCol(String s) throws IOException {

		String slowaKluczowe[] = null;

		String kolumny[] = s.split("\t"); // zapisuje do tablicy wyrazy
											// rozdzielone spacja
		String podstawa = kolumny[5];
		int liczPrzed = podstawa.length() - podstawa.replace(";", "").length() + 1;
		String przedmioty[] = new String[liczPrzed];
		String[][] kody = new String[liczPrzed][];
		for (int i = 0; i < kolumny.length; i++) {
			kolumny[i] = kolumny[i].trim(); // usuniecie bialych znakow na
			// slowa kluczowe
			if (i == 4) {

				slowaKluczowe = kolumny[i].split(",");
				for (int j = 0; j < slowaKluczowe.length; j++) {
					slowaKluczowe[j] = slowaKluczowe[j].trim();
				}
			}
			// podstawa programowa
			if (i == 5) {
				przedmioty = kolumny[i].split(";");
				for (int j = 0; j < przedmioty.length; j++) {
					przedmioty[j] = przedmioty[j].trim();
					// sprawdzanie czy jest srednik
					int ileDwojek = przedmioty[j].length() - przedmioty[j].replace("2", "").length();
					if (ileDwojek>1) { 
						System.out.println("W podstawie prog nie ma ; ");
					}
				}
			}
			if (i == 6) {
				String[] tempTab = kolumny[i].split(";");
			//	System.out.println(tempTab.length + "," + liczPrzed);
				for (int j = 0; j < tempTab.length; j++) {

					kody[j] = tempTab[j].split(",");
				}
				for (int k = 0; k < kody.length; k++) {
					for (int j = 0; j < kody[k].length; j++) {
						kody[k][j] = kody[k][j].trim();
		//				System.out.println(kody[k][j]);
					}
				}
			}

		}
		kolumny[0] = replaceChar(kolumny[0]);
		kolumny[1]= replaceChar(kolumny[1]);
		zapis(kolumny, slowaKluczowe, przedmioty, kody);
	}
	public static String replaceChar(String znak) {
		znak = znak.replace("/", "_");
		znak = znak.replace("(", "_");
		znak = znak.replace(")", "");
		znak = znak.replace(".", "_");
		return znak;
	}

	// public static void splitThis(String[] kol, String[] sKlucz)
	public static void zapis(String[] kol, String[] sKlucz, String[] przedmiot, String[][] kod)
			throws FileNotFoundException {
		System.out.println("quiz: " +kol[0]);
		String sciezkaSmanifest = pathHome + "/euro/ZAS_" + kol[0] + "/smanifest.xml";
		PrintWriter zapis = new PrintWriter(sciezkaSmanifest);
		zapis.println("<?xml version=\"1.0\"?>");
		zapis.println("<scholaris>");
		zapis.println("\t<publishInformation>");
		zapis.println("\t\t<publisher>oreip2_8</publisher>");
		
		zapis.println("\t\t<identifier>" + kol[0] + "</identifier>");
		zapis.println("\t\t<version>1</version>");
		zapis.println("\t\t<createDate>2015-07-22</createDate>");
		zapis.println("\t\t<updateDate>2015-07-22</updateDate>");
		zapis.println("\t</publishInformation>");
		zapis.println("<relations>");
		zapis.println("\t<relation>");
		zapis.println("\t\t<kind>ispartof</kind>");
		zapis.println("\t\t<identifier>" + kol[1] + "</identifier>");
		zapis.println("\t</relation>");
		zapis.println("</relations>");
		zapis.println("<resource>");
		zapis.println("\t<description>");
		zapis.println("\t\t<title>" + kol[2] + "</title>");
		zapis.println("\t\t<abstract><![CDATA[<p>" + kol[3] + "</p>]]></abstract>");
		// slowa kluczowe
		zapis.println("\t\t<keywords>");
		for (int i = 0; i < sKlucz.length; i++) {
			zapis.println("\t\t\t<keyword>" + sKlucz[i] + "</keyword>");
		}
		zapis.println("\t\t</keywords>");
		zapis.println("\t<educational>");
		for (int i = 0; i < przedmiot.length; i++) {
			zapis.println("\t\t<educationalLevel>");
			zapis.println("\t\t\t<stage>POCZ</stage>");
			zapis.println("\t\t\t<subject>" + przedmiot[i] + "</subject>");

			zapis.println("\t\t\t<basisPoints>");
			for (int j = 0; j < kod[i].length; j++) {
				zapis.println("\t\t\t\t<basisPoint>" + kod[i][j] + "</basisPoint>");
				int dl=kod[i][j].length();
				char ostatniZnak= kod[i][j].charAt(dl-1);
				if(!kod[i][j].contains(".") && (int )ostatniZnak>60){
					System.out.println("kod punktu z podstawy programowej nie zawiera kropki");
				}
			}
			zapis.println("\t\t\t</basisPoints>");
			zapis.println("\t\t</educationalLevel>");
		}
		
		zapis.println("\t</educational>");
		zapis.println("\t</description>");
	
		String nazwaZasobu = katalogiQuiz[ileZapis].getPath();
		// System.out.println("nazwa zas "+nazwaZasobu);
		File sc= new File(nazwaZasobu);
		File [] screen= sc.listFiles(); 
		//System.out.println("lista plików");
		for (int i = 0; i < screen.length; i++) {
			if (screen[i].getName().contains(".png") || screen[i].getName().contains(".jpg")){
		zapis.println("<miniatureImg>" + screen[0].getName() +"</miniatureImg>");
				System.out.println("Zasób zawiera screen");
			}
		}
		zapis.println("<previewImg></previewImg>");
		zapis.println("<resourceType>excercise</resourceType>");
		zapis.println("<aggregationLevel>single</aggregationLevel>");
		zapis.println("<fileSet>");
		zapis.println("\t<targets>");
		zapis.println("\t\t<target>desktop</target>");
		zapis.println("\t\t<target>mobile</target>");
		zapis.println("\t</targets>");
		zapis.println("\t<file>");
		zapis.println("\t\t<format>application/zip</format>");
		
		File f = new File(nazwaZasobu+"/pliki");
//		File f = new File(katalogiQuizu.get(ileZapis)+"/plik");
		File [] pliki= f.listFiles(); 
//		zapis.println("\t\t<name>pliki/" + kol[1] + "_zip.zip</name>");
		zapis.println("\t\t<name>pliki/"+pliki[0].getName()+"</name>");
		zapis.println("\t\t<size>" + pliki[0].length()+ "</size>");
//		zapis.println("\t\t<size>" + kol[3] + "</size>");
		zapis.println("\t</file>");
		zapis.println("</fileSet>");
		zapis.println("</resource>");
		zapis.println("</scholaris> ");
		zapis.close();
		
		ileZapis++; 

	}


	public static void main(String[] args) throws IOException {
		czytaj_linie();
		for (int i = 0; i < wiersze.size(); i++) {
			System.out.println("ile zapis: "+ileZapis);
			String [] wierszKol=  wiersze.get(i).split("\t");
			wierszKol[0]=replaceChar(wierszKol[0]); 
			wierszKol[0]=wierszKol[0].trim();
			// System.out.println(wiersze.get(i));
			splitCol(wiersze.get(i));
	
		}
		
	}

}
