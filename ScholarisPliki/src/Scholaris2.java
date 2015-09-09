import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * parsowanie kart pracy i filmów
 * program pobieraj�cy dane z arkusza i na podstawie istniej�cych katalogow i plikow generuje plik smanifest
 * uwagi:
 * w katalogu euro nie moze byc innych plikow poza katalogami zasobow
 * @author Euro-Forum_B3
 *
 */
public class Scholaris2 {
	static String pathHome = System.getProperty("user.home");
	// lista plikow w euro
	static File folder = new File(pathHome + "/euro");
	static File[] katalogiQuiz = folder.listFiles();
	// static List <String> katalogiQuizu= new ArrayList<>();
	static List<String> wiersze = new ArrayList<>();
	static int ileZapis = 0;
	static int liczLinie = 0; // ile wierszy
	static boolean czyFilmy=false; 
	public static void czytaj_linie() throws IOException {
		Arrays.sort(katalogiQuiz);
		for (int i = 0; i < katalogiQuiz.length; i++) {
			System.out.println(katalogiQuiz[i].getName());
		}
		FileReader fr = new FileReader(pathHome + "/arkusz");
		BufferedReader br = new BufferedReader(fr);
		String d;
		while ((d = br.readLine()) != null) {
			// splitCol(d);
			wiersze.add(d);
			liczLinie++;
			if (liczLinie==2) {
				char pierwszaLitera = wiersze.get(1).charAt(0);
				if (pierwszaLitera=='P' || pierwszaLitera=='p') {
					czyFilmy=true; 
					System.out.println("zasoby filmy");
				}
				
			}
		}

		System.out.println("Ile jest zasobow: "+liczLinie);
		br.close();
	}

	public static void splitCol(String s) throws IOException {
		
		String slowaKluczowe[] = null;

		String kolumny[] = s.split("\t"); // zapisuje do tablicy wyrazy
											// rozdzielone spacja
		// tworzenie tablic z pods programowa i kodami
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
			// punkty podstawy programowej
			if (i == 6) {
				String[] tempTab = kolumny[i].split(";");
				// System.out.println(tempTab.length + "," + liczPrzed);
				for (int j = 0; j < tempTab.length; j++) {

					kody[j] = tempTab[j].split(",");
				}
				for (int k = 0; k < kody.length; k++) {
					for (int j = 0; j < kody[k].length; j++) {
						kody[k][j] = kody[k][j].trim();
						// System.out.println(kody[k][j]);
					}
				}
			}

		}

		kolumny[0] = replaceChar(kolumny[0]);
		kolumny[1] = replaceChar(kolumny[1]);
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
		System.out.println("zasob: " +kol[0]);
		String sciezkaSmanifest = pathHome + "/euro/ZAS_" + kol[0] + "/smanifest.xml";
		PrintWriter zapis = new PrintWriter(sciezkaSmanifest);
		zapis.println("<?xml version=\"1.0\"?>");
		zapis.println("<scholaris>");
		zapis.println("\t<publishInformation>");
		zapis.println("\t\t<publisher>oreip2_8</publisher>");

		zapis.println("\t\t<identifier>" + kol[0] + "</identifier>");
		zapis.println("\t\t<version>1</version>");
		zapis.println("\t\t<createDate>2015-09-09</createDate>");
		zapis.println("\t\t<updateDate>2015-09-09</updateDate>");
		zapis.println("\t</publishInformation>");
		zapis.println("<relations>");
		zapis.println("\t<relation>");
		if (!kol[1].isEmpty()) {
			zapis.println("\t\t<kind>ispartof</kind>");
			zapis.println("\t\t<identifier>" + kol[1] + "</identifier>");
		} else {
			zapis.println("\t\t<kind>haspart</kind>");
			zapis.println("\t\t<identifier>" + kol[0] + "</identifier>");
		}
		zapis.println("\t</relation>");
		zapis.println("</relations>");
		zapis.println("<resource>");
		zapis.println("\t<description>");
		// tytul i opis
		zapis.println("\t\t<title>" + kol[2] + "</title>");
		zapis.println("\t\t<abstract><![CDATA[<p>" + kol[3] + "</p>]]></abstract>");
		// slowa kluczowe
		zapis.println("\t\t<keywords>");
		for (int i = 0; i < sKlucz.length; i++) {
			zapis.println("\t\t\t<keyword>" + sKlucz[i] + "</keyword>");
		}
		zapis.println("\t\t</keywords>");
		// podstawa programowa
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
		// lista plik z katalogu zasobu
		File sc = new File(nazwaZasobu);
		File[] screen = sc.listFiles();
		// System.out.println("lista plik�w");
		for (int i = 0; i < screen.length; i++) {
			if (screen[i].getName().contains(".png") || screen[i].getName().contains(".jpg")){
				System.out.println("Zas�b zawiera screen");
				zapis.println("<miniatureImg>" + screen[0].getName() + "</miniatureImg>");
			}
		}
		zapis.println("<previewImg></previewImg>");
		char pierwszaLitera = kol[0].charAt(0);
		switch (pierwszaLitera) {
		case 'Q':
		zapis.println("<resourceType>excercise</resourceType>");
			break;
		case 'K':
		zapis.println("<resourceType>worksheet</resourceType>");
			break;
		case 'T':
		zapis.println("<resourceType>test</resourceType>");
			break;
		// szablon
		case 'O':
		zapis.println("<resourceType>questionnaire</resourceType>");
		//zapis.println("<resourceType>scenario</resourceType>");
			break;
		case 'P':
		zapis.println("<resourceType>video</resourceType>");
			break;

		default:
		System.out.println("nie mo�na przypisa� typu zasobu");
			break;
		}
		
		// if (pliki.length == 1) 
		if(!czyFilmy)
		{	
		zapis.println("<aggregationLevel>single</aggregationLevel>");
		} else {
			zapis.println("<aggregationLevel>collection</aggregationLevel>");
		}
		// 
        int ileKolekcji=1; 
        if (czyFilmy==true) {
        	ileKolekcji=4; 
                        }
		for (int j = 1; j <= ileKolekcji; j++) {
			File f = new File(nazwaZasobu + "/pliki_"+j);
			File[] pliki = f.listFiles();
		zapis.println("<fileSet>");
		zapis.println("\t<targets>");
		zapis.println("\t\t<target>desktop</target>");
		zapis.println("\t\t<target>mobile</target>");		// czy mobilne przy 720?
		zapis.println("\t</targets>");
		for (int i = 0; i < pliki.length; i++) {
			if(pliki[i].getName().contains(".mp4") || pliki[i].getName().contains(".MP4") ){
				System.out.println("Zasob zawiera mp4: "+pliki[i].getName());
				zapis.println("\t<file>");
				zapis.println("\t\t<format>video/mp4</format>");
				zapis.println("\t\t<name>pliki_"+j+"/" + pliki[i].getName() + "</name>");
				zapis.println("\t\t<size>" + pliki[i].length() + "</size>");
				zapis.println("\t</file>");
			}
		
				if(pliki[i].getName().contains(".webm") || pliki[i].getName().contains(".WEBM") ){
					System.out.println("Zasob zawiera webm: "+pliki[i].getName());
					zapis.println("\t<file>");
					zapis.println("\t\t<format>video/webm</format>");
					zapis.println("\t\t<name>pliki_"+j+"/" + pliki[i].getName() + "</name>");
					zapis.println("\t\t<size>" + pliki[i].length() + "</size>");
					zapis.println("\t</file>");
				}
				if(pliki[i].getName().contains(".srt") || pliki[i].getName().contains(".SRT") ){
					System.out.println("Zasob zawiera napis srt: "+pliki[i].getName());
					zapis.println("\t<file>");
					zapis.println("\t\t<format>text/plain</format>");
					zapis.println("\t\t<name>pliki_"+j+"/" + pliki[i].getName() + "</name>");
					zapis.println("\t\t<size>" + pliki[i].length() + "</size>");
					zapis.println("\t</file>");
				}
			if (pliki[i].getName().contains(".pdf") || pliki[i].getName().contains(".PDF")) {
				if (pliki[i].getName().contains("instrukc") || pliki[i].getName().contains("INSTRUKC")) {
					System.out.println("Zas�b zawiera instrukcj� pdf: "+pliki[i].getName());
					zapis.println("\t<file>");
					zapis.println("\t\t<format>application/pdf</format>");
					zapis.println("\t\t<name>pliki/" + pliki[i].getName() + "</name>");
					zapis.println("\t\t<size>" + pliki[i].length() + "</size>");
					zapis.println("\t</file>");
				} else {
					System.out.println("Zas�b zawiera pdf");
					zapis.println("\t<file>");
					zapis.println("\t\t<format>application/pdf</format>");
					zapis.println("\t\t<name>pliki/" + pliki[i].getName() + "</name>");
					zapis.println("\t\t<size>" + pliki[i].length() + "</size>");
					zapis.println("\t</file>");
				}
			}
			if (pliki[i].getName().contains(".doc") &&  !pliki[i].getName().contains(".docx")) {
				System.out.println("Zas�b zawiera doc: "+pliki[i].getName());
				zapis.println("\t<file>");
				zapis.println("\t\t<format>application/msword</format>");
				zapis.println("\t\t<name>pliki/" + pliki[i].getName() + "</name>");
				zapis.println("\t\t<size>" + pliki[i].length() + "</size>");
				zapis.println("\t</file>");
			}
			if (pliki[i].getName().contains(".docx") || pliki[i].getName().contains(".DOCX")) {
				System.out.println("Zas�b zawiera docx: "+pliki[i].getName());
				zapis.println("\t<file>");
				zapis.println( "\t\t<format>application/vnd.openxmlformats-officedocument.wordprocessingml.document</format>");
				zapis.println("\t\t<name>pliki/" + pliki[i].getName() + "</name>");
				zapis.println("\t\t<size>" + pliki[i].length() + "</size>");
				zapis.println("\t</file>");
			}
		}

		zapis.println("</fileSet>");
	}
		zapis.println("</resource>");
		zapis.println("</scholaris> ");
		zapis.close();

		ileZapis++;

	}

	public static void main(String[] args) throws IOException {
		czytaj_linie();
		for (int i = 0; i < wiersze.size(); i++) {
			System.out.println("ile zapis: " + ileZapis);
			String[] wierszKol = wiersze.get(i).split("\t");
			wierszKol[0] = replaceChar(wierszKol[0]);
			wierszKol[0] = wierszKol[0].trim();
			// System.out.println(wiersze.get(i));
			splitCol(wiersze.get(i));

		}

	}

}