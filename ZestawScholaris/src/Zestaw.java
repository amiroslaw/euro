import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Zestaw {
	static String pathHome = System.getProperty("user.home");
	static File  euroDir = new File(pathHome + "/euro");
	static List<String> wiersze = new ArrayList<>();
	static int ileZapis = 0;
	static int liczLinie = 0; // ile wierszy

	public static void czytaj_linie() throws IOException {
		FileReader fr = new FileReader(euroDir+"/zestaw");
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
		//petla po kolumnach
		for (int i = 0; i < kolumny.length; i++) {
			kolumny[i] = kolumny[i].trim(); // usuniecie bialych znakow na
			// slowa kluczowe
			if (i == 3) {
				slowaKluczowe = kolumny[i].split(",");
				for (int j = 0; j < slowaKluczowe.length; j++) {
					slowaKluczowe[j] = slowaKluczowe[j].trim();
				}
			}
		}

		kolumny[0] = replaceChar(kolumny[0]);
		zapis(kolumny, slowaKluczowe );
	}

	public static String replaceChar(String znak) {
		znak = znak.replace("/", "_");
		znak = znak.replace("(", "_");
		znak = znak.replace(")", "");
		znak = znak.replace(".", "_");
		return znak;
	}

	public static void zapis(String[] kol, String[] sKlucz)
			throws FileNotFoundException {
		try{
		PrintWriter zapis = new PrintWriter(euroDir+"/zestaw_"+kol[0]+".xml");
		zapis.println("<?xml version=\"1.0\"?>");
		zapis.println("<scholaris>");
		zapis.println("\t<publishInformation>");
		zapis.println("\t\t<publisher>oreip2_8</publisher>");

		zapis.println("\t\t<identifier>" + kol[0] + "</identifier>");
		zapis.println("\t\t<version>1</version>");
		zapis.println("\t\t<createDate>2015-08-11</createDate>");
		zapis.println("\t\t<updateDate>2015-08-11</updateDate>");
		zapis.println("\t</publishInformation>");
		zapis.println("<resourceSet>");
		zapis.println("\t<description>");
		// tytul i opis
		zapis.println("\t\t<title>" + kol[1] + "</title>");
		zapis.println("\t\t<abstract><![CDATA[<p>" + kol[2] + "</p>]]></abstract>");
		// slowa kluczowe
		zapis.println("\t\t<keywords>");
		for (int i = 0; i < sKlucz.length; i++) {
			zapis.println("\t\t\t<keyword>" + sKlucz[i] + "</keyword>");
		}
		zapis.println("\t\t</keywords>");
		zapis.println("\t</description>");
		zapis.println("</resourceSet>");
		zapis.println("</scholaris> ");
		zapis.close();
		} catch (IOException e){
			System.err.println("b³ad przy zapisie pliku smanifest");
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
