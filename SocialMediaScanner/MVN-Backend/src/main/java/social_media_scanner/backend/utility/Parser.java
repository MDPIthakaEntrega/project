package social_media_scanner.backend.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FilenameUtils;

public class Parser {

	/**
	 * path of resources folder.
	 */
	public static final String RESOURCE_PATH = Paths.get("").toAbsolutePath() + File.separator + "src" + File.separator
			+ "main" + File.separator + "resources" + File.separator;

	public static final String API_KEY_PATH = RESOURCE_PATH + File.separator + "alchemy_api_key.txt";

	/**
	 * path of grabbers folder.
	 */
	public static final String GRABBER_PATH = Paths.get("").toAbsolutePath() + File.separator + "src" + File.separator
			+ "main" + File.separator + "java" + File.separator + "social_media_scanner" + File.separator + "backend"
			+ File.separator + "grabber" + File.separator;

	private static Scanner scanner;

	/**
	 * Map of review to attributes
	 */
	private static Map<String, Map<String, String>> path_map = new HashMap<String, Map<String, String>>();

	/**
	 * 
	 */
	private static Map<String, Double> conversion_map = new HashMap<String, Double>();

	private static List<String> business_attributes = new LinkedList<String>();

	private static List<String> sources = new LinkedList<String>();

	static {

		try {
			APIParser.initializeBusiness();
		} catch (IOException e) {
			System.out.println("Error 1");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			APIParser.initializeSources();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Error 2");

			e.printStackTrace();
		}
		try {
			APIParser.initAllAPI();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error 3");
			e.printStackTrace();
		}
	}

	public static class APIParser {
		public static List<String> getBusAttributes() {
			return business_attributes;
		}

		public static Map<String, String> getCityGrid() {
			return path_map.get("Citygrid");
		}

		public static Map<String, String> getYelp() {
			return path_map.get("ImportMagicYelp");
		}

		public static Map<String, String> getTwitter() {
			return path_map.get("Twitter");
		}

		public static Map<String, String> getAPIMap(String APIName) {

			if (APIName.equals("Citygrid")) {
				return getCityGrid();
			}
			if (APIName.equals("ImportMagicYelp")) {
				return getYelp();
			}
			if (APIName.equals("Twitter")) {
				return getTwitter();
			}
			return null;
		}

		public static double getConversionMap(String APIName) {

			return conversion_map.get(APIName);
		}

		/**
		 * 
		 * Adds all the data sources
		 */
		private static void initializeSources()
				throws InstantiationException, IllegalAccessException, ClassNotFoundException {

			File api_folder = new File(RESOURCE_PATH);
			File[] listofFiles = api_folder.listFiles();

			for (File file : listofFiles) {

				String basename = FilenameUtils.getBaseName(file.getName());
				if (basename.startsWith("API")) {
					basename = basename.substring(3);
					sources.add(basename);
				}
			}
		}

		private static void initializeBusiness() throws IOException {

			List<String> businessAttributes = getBusinessAttributes();
			for (String attribute : businessAttributes) {
				business_attributes.add(attribute);
			}

		}

		public static void initAPI(String APIName) throws IOException {

			path_map.put(APIName, new HashMap<String, String>());
			String config_file = RESOURCE_PATH + ("API" + APIName + ".txt");

			File mapping = new File(config_file);
			BufferedReader read = new BufferedReader(new FileReader(mapping));
			String line = new String();
			while ((line = read.readLine()) != null) {
				String[] key_val = line.split("\\s+");
				path_map.get(APIName).put(key_val[0], key_val[1]);
			}
			addMissingAttributes(APIName);
			read.close();

			conversion_map.put(APIName, 10.0);
		}

		public static void initAllAPI() throws IOException {

			for (String API : sources) {
				initAPI(API);

			}
		}

		private static void addMissingAttributes(String APIName) {

			for (String attribute : business_attributes) {

				if (path_map.get(APIName).get(attribute) == null) {
					path_map.get(APIName).put(attribute, "$." + attribute);
				}
			}
		}
	}

	public static List<String> getServiceInitConfig() {
		String serviceConfigPath = RESOURCE_PATH + File.separator + "ServiceInit.conf";
		return readFileToList(serviceConfigPath);
	}

	public static List<String> getBusinessAttributes() {
		String businessAttributesPath = RESOURCE_PATH + "business.txt";
		return readFileToList(businessAttributesPath);
	}

	private static List<String> readFileToList(String filePath) {
		List<String> listLine = new ArrayList<String>();
		try {
			scanner = new Scanner(new File(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (scanner.hasNext()) {
			String next = scanner.nextLine();
			listLine.add(next.replaceAll("\\s+", ""));
		}

		return listLine;
	}

}