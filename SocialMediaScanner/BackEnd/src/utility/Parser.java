package utility;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

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

	
	public static List<String> GetServiceInitConfig() {
		return null;
	}
	
	static class APIParser {
		public static Map<String, String> GetCityGrid() {
			return null;
		}
		
		public static Map<String, String> GetYelp() {
			return null;
		}
		
		public static Map<String, String> GetTwitter() {
			return null;
		}
		
		private static Map<String, String> APIConfigGetterGeneric(String filePath) {
			return null;
		}
	}
}
