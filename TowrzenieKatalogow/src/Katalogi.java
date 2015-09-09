import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
	
/**
 * pobiera dane z arkusza arkusz, gdzie jest lista quizow
 * tworzy katalogi z nazwa quizu
 * @author Euro-Forum_B3
 *
 */
public class Katalogi {
	//zmienne
		static List <String> wiersze= new ArrayList<>(); 
		static String pathHome = System.getProperty("user.home");
		static	int liczLinie=0; // ile wierszy
		static boolean czyFilmy=false; 
		// dzieli plik na linie
		public static void czytaj_linie() throws IOException {
			FileReader fr = new FileReader(pathHome+"/zestaw");
			BufferedReader br = new BufferedReader(fr);
			String d;
			
			while ((d = br.readLine()) != null) {
				//splitCol(d);
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

			br.close();
		}
		
		
		public static String replaceSlash(String slash){
			slash = slash.replace("/", "_");
			slash = slash.replace("(", "_");
			slash = slash.replace(")", "");
			slash = slash.replace(".", "_");
			return slash;
		}

		
		public static void createFolder(String quizNazwa) {

			//System.out.println(pathHome);
			String folderZasobu = pathHome + "/euro/ZAS_" + quizNazwa;
			
			System.out.println(folderZasobu);
			boolean success = (new File(folderZasobu)).mkdirs();
			if (!success) {
				System.out.append("\n Folder nie utworzony bo juz istnieje :\n");
			} else {
				System.out.append("\n Folder utworzony\n");
			}
			
			// tworzy katalogi na kolecje 4 jesli to filmy
			int ileKolekcji=1; 
			if (czyFilmy==true) {
				ileKolekcji=4; 
			}
			for (int i = 1; i <= ileKolekcji; i++) {
			String folderZip = folderZasobu + "/pliki_"+i;
			boolean success2 = (new File(folderZip)).mkdirs();
			if (!success2) {
				System.out.append("\n Folder pliki "+i+" nie utworzony bo juz istnieje  :\n");
			} else {
				System.out.append("\n Folder pliki "+i+" utworzony\n");
			}
				
			}
		}

		public static void main(String[] args) throws IOException {
			czytaj_linie();
			for (int i = 0; i < wiersze.size(); i++) {
				String [] wierszKol=  wiersze.get(i).split("\t");
				wierszKol[0]=replaceSlash(wierszKol[0]); 
				wierszKol[0]=wierszKol[0].trim();
				// System.out.println(wiersze.get(i));
				createFolder(wierszKol[0]);
			//	splitCol(wiersze.get(i));
		
			}
			
		}

	}
