import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileHandler {	
	char[][] c;
	boolean[][] booleanC;
	String content = "";
	public FileHandler() {
	try {
		Scanner scanner = new Scanner(new File("labyrinth.txt"));
		
		while(scanner.hasNextLine()) {
			content += scanner.nextLine() + "\n";
		}
		
		String[] l = content.split("\n");
		c = new char[l.length][l[0].length()];
		booleanC = new boolean[l.length][l[0].length()];
		
		for(int i = 0; i < l.length; i++) {
			for(int j = 0; j < l[i].length(); j++) {
				c[i][j] = l[i].charAt(j);
				if(Character.toString(c[i][j]) == "#") {
					booleanC[i][j] = false;
				} else {
					booleanC[i][j] = true;
				}
			}
		}
		
		scanner.close();			
		
	} catch(FileNotFoundException e){
		System.out.println("O arquivo não foi encontrado! Verifique se o caminho está correto!");
	}
}


	public boolean[][] getC() {
		return this.booleanC;
	}
	
	public static void main(String[] args) {
		
	}
}