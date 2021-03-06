import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
/**
 * program do pobierania danych z szablonu kart pracy lub quizu i tworzenie smanifestu bez:
 * informacji o pliku
 * ikonki
 * jednak zostawie slowa kluczowe
 * @author Euro-Forum_B3
 *
 */
public class ZasPars {
	static String pathHome = System.getProperty("user.home");
	static File  euroDir = new File(pathHome + "/euro");
	// static File[] katalogiQuiz = folder.listFiles();
	static List<String> wiersze = new ArrayList<>();
	static int ileZapis = 0;
	static int liczLinie = 0; // ile wierszy

	public static void czytaj_linie() throws IOException {
		FileReader fr = new FileReader(euroDir+"/arkusz");
		BufferedReader br = new BufferedReader(fr);
		String d;
		while ((d = br.readLine()) != null) {
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
		// tworzenie tablic z pods programowa i kodami
		String podstawa = kolumny[5];
		int liczPrzed = podstawa.length() - podstawa.replace(";", "").length() + 1;
		String przedmioty[] = new String[liczPrzed];
		String[][] kody = new String[liczPrzed][];
		//petla po kolumnach
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
					// sprawdzanie poprawnosci podstawy programowej 
					String prz=przedmioty[j]; 
					if(
							prz.equals("MUZ2")||
							prz.equals("PLAST2")||
							prz.equals("POL2")||
							prz.equals("PRZYR2")||
							prz.equals("SPOL2")||
							prz.equals("JOB2")||
							prz.equals("KOMP2")||
							prz.equals("TECH2")||
							prz.equals("ZDR2")||
							prz.equals("MAT2")
							){
//						System.out.println("z�a podstawa programowa");
					}else {
						System.out.println("z�a podstawa programowa");
					}
				// sprawdzanie czy jest srednik
				int ileDwojek = przedmioty[j].length() - przedmioty[j].replace("2", "").length();
				if (ileDwojek>1) { 
					System.out.println("W podstawie prog nie ma ; ");
					// dopisa� reperacje
//					przedmioty[j]=przedmioty[j].replace(" ", ";"); 
//					System.out.println(przedmioty[j]);
				}
				}
			}
			// punkty podstawy programowej
			if (i == 6) {
				String[] tempTab = kolumny[i].split(";");
				for (int j = 0; j < tempTab.length; j++) {
					kody[j] = tempTab[j].split(",");
					
					// nie dzia�a sprawdzanie srednika
//					
//					int ilePP =0; 
//					for (int k = 0; k < tempTab.length; k++) {
//						char z= tempTab[j].charAt(k); 
//						if(z>47 && z<58){
//							ilePP++;
//						}
//					}
//					if (tempTab[j].charAt(0)<58 && tempTab[j].charAt(0)>48 && tempTab[j].charAt(1)>=58 && tempTab[j].charAt(1)<48 && ilePP>1 ) { 
//						System.out.println("W punktach prog nie ma ; ");
//						// dopisa� reperacje
////						przedmioty[j]=przedmioty[j].replace(" ", ";"); 
////						System.out.println(przedmioty[j]);
//					}
				}
				for (int k = 0; k < kody.length; k++) {
					for (int j = 0; j < kody[k].length; j++) {
						kody[k][j] = kody[k][j].trim();
					}
				}
			}

		}

		kolumny[0] = replaceChar(kolumny[0]);
		kolumny[1] = replaceChar(kolumny[1]);
		zapis(kolumny, slowaKluczowe, przedmioty, kody);
	}
	public static String deleteCudzy(String cudz) {
		cudz = cudz.replace("\"", "");
		return cudz;
	}
	public static String replaceChar(String znak) {
		znak = znak.replace("/", "_");
		znak = znak.replace("(", "_");
		znak = znak.replace(")", "");
		znak = znak.replace(".", "_");
		return znak;
	}

	public static void zapis(String[] kol, String[] sKlucz, String[] przedmiot, String[][] kod)
			throws FileNotFoundException {
		try{
		PrintWriter zapis = new PrintWriter(euroDir+"/"+kol[0]+"_zas�b.xml");
		zapis.println("<?xml version=\"1.0\"?>");
		zapis.println("<scholaris>");
		zapis.println("\t<publishInformation>");
		zapis.println("\t\t<publisher>oreip2_8</publisher>");

		zapis.println("\t\t<identifier>" + kol[0] + "</identifier>");
		zapis.println("\t\t<version>1</version>");
		zapis.println("\t\t<createDate>2015-08-11</createDate>");
		zapis.println("\t\t<updateDate>2015-08-11</updateDate>");
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
			zapis.println("\t\t\t<keyword>" + deleteCudzy(sKlucz[i]) + "</keyword>");
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

        zapis.println("<miniatureImg></miniatureImg>");
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
//			zapis.println("<aggregationLevel>single</aggregationLevel>");
//			zapis.println("<aggregationLevel>collection</aggregationLevel>");
//		zapis.println("<fileSet>");
//		zapis.println("\t<targets>");
//		zapis.println("\t\t<target>desktop</target>");
//		zapis.println("\t\t<target>mobile</target>");
//		zapis.println("\t</targets>");
//		for (int i = 0; i < pliki.length; i++) {
//			if (pliki[i].getName().contains(".pdf") || pliki[i].getName().contains(".PDF")) {
//				if (pliki[i].getName().contains("instrukc") || pliki[i].getName().contains("INSTRUKC")) {
//					System.out.println("Zas�b zawiera instrukcj� pdf");
//					zapis.println("\t<file>");
//					zapis.println("\t\t<format>application/pdf</format>");
//					zapis.println("\t\t<name>pliki/" + pliki[i].getName() + "</name>");
//					zapis.println("\t\t<size>" + pliki[i].length() + "</size>");
//					zapis.println("\t</file>");
//				} else {
//					System.out.println("Zas�b zawiera pdf");
//					zapis.println("\t<file>");
//					zapis.println("\t\t<format>application/pdf</format>");
//					zapis.println("\t\t<name>pliki/" + pliki[i].getName() + "</name>");
//					zapis.println("\t\t<size>" + pliki[i].length() + "</size>");
//					zapis.println("\t</file>");
//				}
//			}
//			if (pliki[i].getName().contains(".doc") &&  !pliki[i].getName().contains(".docx")) {
//				System.out.println("Zas�b zawiera doc");
//				zapis.println("\t<file>");
//				zapis.println("\t\t<format>application/msword</format>");
//				zapis.println("\t\t<name>pliki/" + pliki[i].getName() + "</name>");
//				zapis.println("\t\t<size>" + pliki[i].length() + "</size>");
//				zapis.println("\t</file>");
//			}
//			if (pliki[i].getName().contains(".docx") || pliki[i].getName().contains(".DOCX")) {
//				System.out.println("Zas�b zawiera docx");
//				zapis.println("\t<file>");
//				zapis.println(
//						"\t\t<format>application/vnd.openxmlformats-officedocument.wordprocessingml.document</format>");
//				zapis.println("\t\t<name>pliki/" + pliki[i].getName() + "</name>");
//				zapis.println("\t\t<size>" + pliki[i].length() + "</size>");
//				zapis.println("\t</file>");
//			}
//		}
//
//		zapis.println("</fileSet>");
		zapis.println("</resource>");
		zapis.println("</scholaris> ");
		zapis.close();
		} catch (IOException e){
			System.err.println("b�ad przy zapisie pliku smanifest");
			e.printStackTrace();
		} finally {
			System.out.println("zapisano smanifest do: "+ kol[0]);
		}

		ileZapis++;

	}

	public static void main(String[] args) throws IOException {
		czytaj_linie();
		for (int i = 0; i < wiersze.size(); i++) {
			System.out.println("ile zapis: " + ileZapis);
			String[] wierszKol = wiersze.get(i).split("\t");
			wierszKol[0] = replaceChar(wierszKol[0]);
			wierszKol[0] = wierszKol[0].trim();
			splitCol(wiersze.get(i));

		}

	}

}
