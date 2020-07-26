package ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class loads and handles all language data stored in
 * ui/resources/languages.txt in json format.
 * 
 * @author Niklas S.
 *
 */
public class Language {

	private static int preference = 0;
	static String[] languages;
	static List<String[]> keyValues;

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

		lines.remove(0);
		lines.remove(0);
		lines.remove(lines.size() - 1);

		keyValues = new ArrayList<>();

		String appended = "";
		for (String string : lines)
			appended += string;

		Pattern p = Pattern.compile("\\s*\"(.*?)\" : \\{ \"(.*?)\", \"(.*?)\" \\}");
		Matcher m = p.matcher(appended);
		while (m.find())
			keyValues.add(new String[] { m.group(1), m.group(2), m.group(3) });

	}

	/**
	 * This Method sets the language preference stored. The language preference is
	 * stored and used to select translations.
	 * 
	 * @param lang is the language that will be stored as the new language
	 *             preference. Should no language preference exist that equals to
	 *             lang the previous preference will remain.
	 */
	public static void setLanguagePreference(String lang) {
		for (int i = 0; i < languages.length; i++)
			if (languages[i].equals(lang)) {
				preference = i;
				return;
			}
	}

	/**
	 * Getter for the current language preference. By default the first specified
	 * language in the language data file will be selected.
	 * 
	 * @return the current language preference.
	 */
	public static String getLanguagePreference() {
		return languages[preference];
	}

	/**
	 * Getter method for specific language data.
	 * 
	 * @param id   is the key for the requested data.
	 * @param args are the arguments to be inserted into the language data that will
	 *             be returned. "[0]" will be replaced with args[0] of the insertion
	 *             data. Should args not reach the index in the data (e.g. "[5]"
	 *             exists in the language data but the args array does not contain
	 *             the index 5) the placeholder will remain in the data and will be
	 *             returned.
	 * @return the language data with placeholders replaced with arguments.
	 */
	public static String get(String id, Object... args) {
		for (String[] sa : keyValues)
			if (sa[0].equals(id)) {
				String filledVal = sa[1 + preference];

				for (int i = 0; i < args.length; i++)
					if (args[i] != null)
						filledVal = filledVal.replaceAll("\\[" + i + "\\]", args[i].toString());

				return filledVal;
			}

		return null;
	}
}
