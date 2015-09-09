import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
// do zrobienia
// nieaktualne tworzy foldery
public class Pars {
	// public static List <String []> slowaKluczowe=new ArrayList<String[]>() ;
	// zamiast tego można zrobić tablice 3d public static String tabManifest
	// [][][] =new String [20][2][]; tabManifest [1][1][0]= "fjidasf";
	// tyko jak dodac zmienna wartosc w 3 cim wymiarze?

	// dzieli plik na linie
	static String pathHome = System.getProperty("user.home");

	public static void czytaj_linie() throws IOException {
		FileReader fr = new FileReader("arkusz");
		BufferedReader br = new BufferedReader(fr);
		String d;
		while ((d = br.readLine()) != null) {
			splitCol(d);

			System.out.println("");
		}
		br.close();
	}

	public static void splitCol(String s) throws IOException {

		String slowaKluczowe[] = null;

		String kolumny[] = s.split("\t"); // zapisuje do tablicy wyrazy
											// rozdzielone spacja
		String podstawa = kolumny[12];
		int liczPrzed = podstawa.length() - podstawa.replace(";", "").length() + 1;
		String przedmioty[] = new String[liczPrzed];
		String[][] kody = new String[liczPrzed][];
		for (int i = 0; i < kolumny.length; i++) {
			kolumny[i] = kolumny[i].trim(); // usuniecie bialych znakow na
			// slowa kluczowe
			if (i == 11) {

				slowaKluczowe = kolumny[i].split(",");
				for (int j = 0; j < slowaKluczowe.length; j++) {
					slowaKluczowe[j] = slowaKluczowe[j].trim();
				}
			}
			// podstawa programowa
			if (i == 12) {
				przedmioty = kolumny[i].split(";");
				for (int j = 0; j < przedmioty.length; j++) {
					przedmioty[j] = przedmioty[j].trim();
				}
			}
			if (i == 13) {
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

		kolumny[1] = kolumny[1].replace("/", "_");
		// sapisac to do finalnej zmiennej jak zdjecia beda zapisywane bez 6l
		kolumny[1] = kolumny[1].replace("(", "_");
		kolumny[1] = kolumny[1].replace(")", "");
		createFolder(kolumny[1]);
		zapis(kolumny, slowaKluczowe, przedmioty, kody);
	}

	// public static void splitThis(String[] kol, String[] sKlucz)
	public static void zapis(String[] kol, String[] sKlucz, String[] przedmiot, String[][] kod)
			throws FileNotFoundException {
		String sciezkaSmanifest = pathHome + "/euro/ZAS_" + kol[1] + "/smanifest.xml";
		PrintWriter zapis = new PrintWriter(sciezkaSmanifest);
		zapis.println("<?xml version=\"1.0\"?>");
		zapis.println("<scholaris>");
		zapis.println("\t<publishInformation>");
		zapis.println("\t\t<publisher>EF</publisher>");
		zapis.println("\t\t<identifier>" + kol[1] + "</identifier>");
		zapis.println("\t\t<version>1</version>");
		zapis.println("\t\t<createDate>2015-07-22</createDate>");
		zapis.println("\t\t<updateDate>2015-07-22</updateDate>");
		zapis.println("\t</publishInformation>");
		zapis.println("<relations>");
		zapis.println("\t<relation>");
		zapis.println("\t\t<kind>ispartof</kind>");
		zapis.println("\t\t<identifier>" + kol[2] + "</identifier>");
		zapis.println("\t</relation>");
		zapis.println("</relations>");
		zapis.println("<resource>");
		zapis.println("\t<description>");
		zapis.println("\t\t<title>" + kol[7] + "</title>");
		zapis.println("\t\t<abstract><![CDATA[<p>" + kol[8] + "</p>]]></abstract>");
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
			}
			zapis.println("\t\t\t</basisPoints>");
			zapis.println("\t\t</educationalLevel>");
		}
		zapis.println("\t</educational>");
		zapis.println("\t</description>");
		zapis.println("<miniatureImg>I_" + kol[1] + ".png</miniatureImg>");
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
		zapis.println("\t\t<name>plik/" + kol[1] + "_zip.zip</name>");
		zapis.println("\t\t<size>" + kol[3] + "</size>");
		zapis.println("\t</file>");
		zapis.println("</fileSet>");
		zapis.println("</resource>");
		zapis.println("</scholaris> ");
		zapis.close();

	}

	public static void createFolder(String quizNazwa) {

		//System.out.println(pathHome);
		String folderZasobu = pathHome + "/euro/ZAS_" + quizNazwa;

		System.out.println(folderZasobu);
		boolean success = (new File(folderZasobu)).mkdirs();
		if (!success) {
			System.out.append("\n Folder nie utworzony bo już istnieje :\n");
		} else {
			System.out.append("\n Folder utworzony\n");
		}
		String folderZip = folderZasobu + "/plik";
		boolean success2 = (new File(folderZip)).mkdirs();
		if (!success2) {
			System.out.append("\n Folder plik nie utworzony bo już istnieje  :\n");
		} else {
			System.out.append("\n Folder plik utworzony\n");
		}
	}

	public static void main(String[] args) throws IOException {
		czytaj_linie();
double a =  0.1;
double b = 0.1; 
System.out.println(a*b);
	}

}
