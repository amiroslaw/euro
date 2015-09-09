import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * nieaktualne
 * tworzenie plikow smanifest do kart pracy oraz tworzenie katalogow
 * 
 * @author Euro-Forum_B3
 *
 */
public class ParsowanieKart {

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
		System.out.println(kolumny[2]);
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
		if(!kol[2].isEmpty()){
		zapis.println("\t\t<kind>ispartof</kind>");
		zapis.println("\t\t<identifier>" + kol[2] + "</identifier>");
		}else {
		zapis.println("\t\t<kind>haspart</kind>");
		zapis.println("\t\t<identifier>" + kol[1] + "</identifier>");
		}
		zapis.println("\t</relation>");
		zapis.println("</relations>");
		zapis.println("<resource>");
		zapis.println("\t<description>");
		zapis.println("\t\t<title>" + kol[9] + "</title>");
		zapis.println("\t\t<abstract><![CDATA[<p>" + kol[10] + "</p>]]></abstract>");
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
		zapis.println("<resourceType>learning-program</resourceType>");
		int ileplikiow =0; 
		for (int i = 3; i < 7; i++) {
			if(!kol[i].isEmpty()){
				ileplikiow++; 
			}
		}
		//int ileplikiow=Integer.parseInt(kol[3])+Integer.parseInt(kol[5])+Integer.parseInt(kol[7])+Integer.parseInt(kol[9]);
		if (ileplikiow>1) { 
		zapis.println("<aggregationLevel>collection</aggregationLevel>");
		}else {
		zapis.println("<aggregationLevel>single</aggregationLevel>");
		}
		zapis.println("<fileSet>");
		zapis.println("\t<targets>");
		zapis.println("\t\t<target>desktop</target>");
		zapis.println("\t\t<target>mobile</target>");
		zapis.println("\t</targets>");
		if(!kol[3].isEmpty()){
		zapis.println("\t<file>");
		zapis.println("\t\t<format>application/pdf</format>");
		zapis.println("\t\t<name>pliki/" + kol[1] + ".pdf</name>");
		zapis.println("\t\t<size>" + kol[3] + "</size>");
		zapis.println("\t</file>");
		}
		if(!kol[4].isEmpty()){
		zapis.println("\t<file>");
		zapis.println("\t\t<format>application/msword</format>");
		zapis.println("\t\t<name>pliki/" + kol[1] + ".doc</name>");
		zapis.println("\t\t<size>" + kol[4] + "</size>");
		zapis.println("\t</file>");
		}
		if(!kol[5].isEmpty()){
			zapis.println("\t<file>");
			zapis.println("\t\t<format>application/vnd.openxmlformats-officedocument.wordprocessingml.document</format>");
			zapis.println("\t\t<name>pliki/" + kol[1] + ".docx</name>");
			zapis.println("\t\t<size>" + kol[5] + "</size>");
			zapis.println("\t</file>");
		}
		if(!kol[6].isEmpty()){
			zapis.println("\t<file>");
			zapis.println("\t\t<format>application/pdf</format>");
			zapis.println("\t\t<name>pliki/" + kol[1] + "_INSTRUKCJA.pdf</name>");
			zapis.println("\t\t<size>" + kol[6] + "</size>");
			zapis.println("\t</file>");
		}
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
		String folderZip = folderZasobu + "/pliki";
		boolean success2 = (new File(folderZip)).mkdirs();
		if (!success2) {
			System.out.append("\n Folder pliki nie utworzony bo już istnieje  :\n");
		} else {
			System.out.append("\n Folder pliki utworzony\n");
		}
	}

	public static void main(String[] args) throws IOException {
		czytaj_linie();
		
	}


}
