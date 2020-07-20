package ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Language {

	static String[] languages;
	
	static {
		// load language file
		InputStream is = Map.class.getResourceAsStream("/ui/resources/languages.txt");
		Scanner sc = new Scanner(is);
		List<String> lines = new ArrayList<>();
		
		while (sc.hasNextLine()) {
			lines.add(sc.nextLine().split("//")[0]); // line without potential line comment
		}
		
		sc.close();
		
		// parsing line data
		String[] langHeads = lines.get(0).split("\"");
		languages = new String[(langHeads.length - 3) / 2];
		for (int i = 0; i < languages.length; i++)
			languages[i] = langHeads[i * 2 + 3];
		
		
	}

	public static void setLanguagePreference(String lang) {

	}

	public static String getLanguagePreference() {
		return null;
	}

	public static String get(String id, Object... args) {
		
		return "";
	}
}
