package utility;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Parser {
	
	public static final String API_KEY_PATH = "testdir/api_key.txt";
	
	/**
	 * path of resources folder.
	 */
	public static final String SOURCE_PATH = Paths.get(".").toAbsolutePath()
			+ "/resources/";
	
	/**
	 * path of grabbers folder.
	 */
	public static final String GRABBER_PATH = Paths.get(".").toAbsolutePath() + "/src/grabber/";

	private static Scanner scanner;

	static class APIParser {
		public static Map<String, String> getCityGrid() {
			return null;
		}
		
		public static Map<String, String> getYelp() {
			return null;
		}
		
		public static Map<String, String> getTwitter() {
			return null;
		}
		
		private static Map<String, String> APIConfigGetterGeneric(String filePath) {
			return null;
		}
	}

	public static List<String> getServiceInitConfig() {
		String serviceConfigPath = SOURCE_PATH + "/ServiceInit.conf";
		return readFileToList(serviceConfigPath);
	}
	
	public static List<String> getBusinessAttributes() {
		String businessAttributesPath = SOURCE_PATH + "/business.txt";
		return readFileToList(businessAttributesPath);
	}	
	
	private static List<String> readFileToList(String filePath) {
		List<String> listLine = new ArrayList<String>();
		scanner = new Scanner(filePath);
		while (scanner.hasNext()) {
			listLine.add(scanner.nextLine());
		}
		
		return listLine;
	}
}
