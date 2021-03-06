package social_media_scanner.backend.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.alchemyapi.api.AlchemyAPI;

import social_media_scanner.backend.database.AccessData;
import social_media_scanner.backend.grabber.DataGrabberGeneric;
import social_media_scanner.backend.utility.Parser;

/**
 * The actual server that provides service.
 * 
 * @author Yuke
 * 
 */
public class Server extends ServerGeneric {

	private class CompanyLocationPair {

		private String companyName;

		private String location;

		CompanyLocationPair(String companyName, String location) {

			this.companyName = companyName;
			this.location = location;
		}

		public String getCompanyName() {
			return companyName;
		}

		public String getLocation() {
			return location;
		}
	}

	private final String dbAddr = "127.0.0.1";

	private final String keyspaceName = "review_keyspace";

	private final String tableName = "review_table";

	private final String invertTableName = "invert_table";

	private AccessData dbAccessor = new AccessData();

	/**
	 * list of class class of all api grabbers.
	 */
	private List<Class<? extends DataGrabberGeneric>> listGrabberClass = new LinkedList<Class<? extends DataGrabberGeneric>>();

	/**
	 * list of instances of all api grabbers.
	 */
	private List<DataGrabberGeneric> listGrabber = new LinkedList<DataGrabberGeneric>();

	/**
	 * Entrance to set up the service.
	 * 
	 * @param argv
	 *            In the argument, there should be only one integer specifying
	 *            the port number.
	 * @throws IOException
	 */
	public static void main(String argv[]) throws IOException {

		int portNum = -1;
		try {
			portNum = Integer.parseInt(argv[0]);
		} catch (NumberFormatException nfe) {
			System.err.println("Cannot parse port number: " + argv[0]);
		}

		Server server = new Server(portNum);
		server.initServer();
		server.serve();
	}

	/**
	 * execute sentiment analysis for a list of reviews;
	 * 
	 * @param reviewList
	 * @return
	 */
	/*
	 * public static List<String> sentimentAnalyze(List<String> reviewList) {
	 * 
	 * List<String> sentimentList = new LinkedList<String>(); for (int i = 0; i
	 * < reviewList.size(); i++) {
	 * 
	 * sentimentList.add(sentimentAnalyze(reviewList.get(i))); }
	 * 
	 * return sentimentList; }
	 */

	/**
	 * execute sentiment analysis for one review;
	 * 
	 * @param review
	 * @return
	 */
	public static SentimentStruct sentimentAnalyze(String review) {

		try {
			AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromFile(Parser.API_KEY_PATH);
//			System.out.println("review: " + review);
			Document result = alchemyObj.TextGetTextSentiment(review);
			SentimentStruct s = parseSentimentStruct(result);
//			System.out.println("sentiment: " + s.getFeeling());
			return s;

		} catch (IOException e) {
//			System.out.println("Alchemy IOException");
		} catch (XPathExpressionException e) {
//			System.out.println("Alchemy XPathExpressionException");
		} catch (SAXException e) {
//			System.out.println("Alchemy SAXException");
		} catch (ParserConfigurationException e) {
//			System.out.println("Alchemy ParserConfigurationException");
		}
		return null;
	}

	private static SentimentStruct parseSentimentStruct(Document xmlDoc) {

		Element rootElement = xmlDoc.getDocumentElement();
		String typeStr = null;
		double score = 0;
		NodeList typeList = rootElement.getElementsByTagName("type");
		typeStr = typeList.item(0).getTextContent();
		if (!typeStr.equalsIgnoreCase("neutral") && !typeStr.equalsIgnoreCase("mixed")) {

			score = Double.parseDouble(rootElement.getElementsByTagName("score").item(0).getTextContent());
		}
		SentimentStruct sentiStruct = new SentimentStruct(typeStr, score);

		return sentiStruct;
	}

	/**
	 * Constructor.
	 * 
	 * @param port
	 */
	public Server(int port) {
		super(port);
		// TODO Auto-generated constructor stub
	}

	/**
	 * get the company name and location of all users.
	 * 
	 * @return
	 */
	List<CompanyLocationPair> getAllUsers() {

		List<CompanyLocationPair> listUsers = new LinkedList<CompanyLocationPair>();
		listUsers.add(new CompanyLocationPair("Zingerman's", "ann arbor,mi"));
		return listUsers;
	}

	/**
	 * Initialize the server, and tasks include: set up the service, connect to
	 * database (Cassandra and MySql), activate the social media data grabber.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initServer() {
		// TODO Auto-generated method stub
		// Connect to Cassandra;

		AccessData.initializeDatabase(dbAddr, keyspaceName, tableName, invertTableName);

		// Get all grabbers;
		File grabberFolder = new File(Parser.GRABBER_PATH);
		List<String> typeNameList = new LinkedList<String>();
		String packagePath = "social_media_scanner.backend.grabber.";
		for (File file : grabberFolder.listFiles()) {

			// System.out.println(file.getName());
			if (file.getName().startsWith("Grabber")) {

				typeNameList.add(packagePath + file.getName().split("\\.")[0]);
			}
		}

		for (String typeName : typeNameList) {

			try {
				listGrabberClass.add((Class<? extends DataGrabberGeneric>) Class.forName(typeName));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (Class<? extends DataGrabberGeneric> grabberClass : listGrabberClass) {
			try {
				listGrabber.add(grabberClass.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block...
				e.printStackTrace();
			}
		}

		File confFile = new java.io.File(Parser.RESOURCE_PATH + "ServiceInit.conf");
		List<String> listAPIs = new LinkedList<String>();
		List<DataGrabberGeneric> listNewGrabber = new LinkedList<DataGrabberGeneric>();
		try {
			Scanner scanner = new Scanner(confFile);
			while (scanner.hasNext()) {

				String lineStr = scanner.nextLine();
	
				listAPIs.add(lineStr.split(" ")[0]);
				if (lineStr.split(" ")[1].equalsIgnoreCase("NO")) {

					// pull data for all users.
					for (DataGrabberGeneric grabber : listGrabber) {

						if (grabber.toString().equalsIgnoreCase(listAPIs.get(listAPIs.size() - 1))) {

							listNewGrabber.add(grabber);
						}
					}
				}
			}
			scanner.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (listNewGrabber.size() > 0) {

			pullAPIsAndStoreForAllUsers(listNewGrabber);
		}

		try {
			PrintWriter pw = new PrintWriter(confFile);
			for (String APIName : listAPIs) {

				pw.println(APIName + " YES");
			}
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	void pullAllAPIAndStoreForUsers(List<CompanyStruct> companyNameList) {
		// TODO Auto-generated method stub

		List<ResponseStruct> responseStructList = pullAPIsForUsers(companyNameList, listGrabber);

		// store responseStructList to the database;
		try {
			dbAccessor.insertData(responseStructList);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	void pullSpecificAPIforUsers(List<String> APIs,
			List<CompanyStruct> companyNameList) {
		// TODO Auto-generated method stub

		List<DataGrabberGeneric> listNewGrabber = new LinkedList<DataGrabberGeneric>();

		for(String API: APIs) {
			
			if(API.equals("citygrid")) {
					
				for (DataGrabberGeneric grabber : listGrabber) {
					if (grabber.toString().equalsIgnoreCase("Citygrid")) {
						listNewGrabber.add(grabber);
					}
				}
				
			}
			else if(API.equals("yelp")) {

				for (DataGrabberGeneric grabber : listGrabber) {
					if (grabber.toString().equalsIgnoreCase("ImportMagicYelp")) {
						listNewGrabber.add(grabber);
					}
				}
			}
			else if(API.equals("twitter")) {

				for (DataGrabberGeneric grabber : listGrabber) {
					if (grabber.toString().equalsIgnoreCase("Twitter")) {
						listNewGrabber.add(grabber);
					}
				}
			}
			
		}
		List<String> locationList = new LinkedList<String>();
		for(CompanyStruct company: companyNameList) {
			
			locationList.add(company.getLocation());
		}
		// TODO get rid of location list
		List<ResponseStruct> responseStructList = pullAPIsForUsers(companyNameList, listNewGrabber);
		
		try {
			dbAccessor.insertData(responseStructList);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	@Override
	void pullAPIsAndStoreForAllUsers(List<DataGrabberGeneric> listPartGrabbers) {
		// TODO Auto-generated method stub
		List<CompanyLocationPair> userList = getAllUsers();
//		List<String> companyNameList = new LinkedList<String>();
		List<CompanyStruct> companyNameList = new LinkedList<CompanyStruct>();
		
		List<String> locationList = new LinkedList<String>();
		for (CompanyLocationPair pair : userList) {
			
			CompanyStruct newCompany = new CompanyStruct(pair.getCompanyName(), "","","", "");
			companyNameList.add(newCompany);
			locationList.add(pair.getLocation());
		}

		List<ResponseStruct> responseStructList = pullAPIsForUsers(companyNameList, listPartGrabbers);

		// store responseStructList into database;
		try {
			dbAccessor.insertData(responseStructList);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * pull data and store for a list of given users on a list of given apis.
	 * 
	 * @param companyNameList
	 * @param listGrabber
	 * @return
	 */
	List<ResponseStruct> pullAPIsForUsers(List<CompanyStruct> companyNameList,
			List<DataGrabberGeneric> listGrabber) {

		List<ResponseStruct> responseStructAllList = new LinkedList<ResponseStruct>();
		for (DataGrabberGeneric grabber : listGrabber) {

			try {
				List<ResponseStruct> responseStructList = grabber.pullDataForAll(companyNameList);
				for (ResponseStruct responseStruct : responseStructList) {

					responseStruct.setAPIName(grabber.toString());
				}
				responseStructAllList.addAll(responseStructList);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return responseStructAllList;
	}

	@Override
	String searchReviews(SearchStruct search) {

		List<String> attributes = new LinkedList<String>();

		String result = null;
		try {

			result = dbAccessor.select(search, attributes);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private static String getFileContents(String filename) throws IOException, FileNotFoundException {
		File file = new File(filename);
		StringBuilder contents = new StringBuilder();

		BufferedReader input = new BufferedReader(new FileReader(file));

		try {
			String line = null;

			while ((line = input.readLine()) != null) {
				contents.append(line);
				contents.append(System.getProperty("line.separator"));
			}
		} finally {
			input.close();
		}

		return contents.toString();
	}

	// utility method
	private static String getStringFromDocument(Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);

			return writer.toString();
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

}