package service;

import grabber.DataGrabberGeneric;

import com.alchemyapi.api.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Paths;
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

import database.AccessData;

/**
 * The actual server that provides service.
 * 
 * @author Yuke
 * 
 */
public class Server extends ServerGeneric {
	
	private static final String API_KEY_PATH = "testdir/api_key.txt";
	
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
	 * path of resources folder.
	 */
	private static final String SOURCE_PATH = Paths.get(".").toAbsolutePath()
			+ "/resources/";
	
	/**
	 * path of grabbers folder.
	 */
	private static final String GRABBER_PATH = Paths.get(".").toAbsolutePath() + "/src/grabber/";

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
	 * @param reviewList
	 * @return
	 */
	/*public static List<String> sentimentAnalyze(List<String> reviewList) {
		
		List<String> sentimentList = new LinkedList<String>();
		for (int i = 0; i < reviewList.size(); i++) {
			
			sentimentList.add(sentimentAnalyze(reviewList.get(i)));
		}
		
		return sentimentList;
	}*/

	/**
	 * execute sentiment analysis for one review;
	 * @param review
	 * @return
	 */
	public static SentimentStruct sentimentAnalyze(String review) {
		
		//System.out.println(review);
		
		try {
			AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromFile(API_KEY_PATH);
			Document result = alchemyObj.TextGetTextSentiment(review);
			SentimentStruct s = parseSentimentStruct(result);
			//System.out.println(s);
			return s;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static SentimentStruct parseSentimentStruct(Document xmlDoc) {
		
		//System.out.println(xmlDoc.toString());
		//System.out.println(getStringFromDocument(xmlDoc));
		Element rootElement = xmlDoc.getDocumentElement();
		String typeStr = null;
		double score = 0;
		NodeList typeList = rootElement.getElementsByTagName("type");
		typeStr = typeList.item(0).getTextContent();
		//System.out.println(typeStr);
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
		
		// Charlie adding to test new implementation
		//dbAccessor.initializeDatabase(dbAddr, keyspaceName, tableName, invertTableName);
		AccessData.initializeDatabase(dbAddr, keyspaceName, tableName, invertTableName);
		
		// dbAccessor.init(SOURCE_PATH);
		AccessData.init(SOURCE_PATH);
		
		//Get all grabbers;
		File grabberFolder = new File(GRABBER_PATH);
		List<String> typeNameList = new LinkedList<String>();
		String packagePath = "grabber.";
		for (File file: grabberFolder.listFiles()) {
			
			//System.out.println(file.getName());
			if (file.getName().startsWith("Grabber")) {
				
				typeNameList.add(packagePath + file.getName().split("\\.")[0]);
			}
		}
		
		for (String typeName: typeNameList) {
			
			try {
				listGrabberClass.add((Class<? extends DataGrabberGeneric>) Class.forName(typeName));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for (Class<? extends DataGrabberGeneric> grabberClass: listGrabberClass) {
			
			try {
				listGrabber.add(grabberClass.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block...
				e.printStackTrace();
			}
		}
		
		File confFile = new java.io.File(SOURCE_PATH + "ServiceInit.conf");
		List<String> listAPIs = new LinkedList<String>();
		List<DataGrabberGeneric> listNewGrabber = new LinkedList<DataGrabberGeneric>();
		try {
			Scanner scanner = new Scanner(confFile);
			while (scanner.hasNext()) {

				String lineStr = scanner.nextLine();
				System.out.println(lineStr);
				listAPIs.add(lineStr.split(" ")[0]);
				if (lineStr.split(" ")[1].equalsIgnoreCase("NO")) {

					// pull data for all users.
					for (DataGrabberGeneric grabber: listGrabber) {
						
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
			for (String APIName: listAPIs) {
				
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
	void pullAllAPIAndStoreForUsers(List<String> companyNameList,
			List<String> locationList) {
		// TODO Auto-generated method stub
		
		List<ResponseStruct> responseStructList = pullAPIsForUsers(companyNameList, locationList, listGrabber);
		
		//store responseStructList to the database;
		try {
			dbAccessor.insertData(responseStructList);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	void pullAPIsAndStoreForAllUsers(List<DataGrabberGeneric> listPartGrabbers) {
		// TODO Auto-generated method stub
		List<CompanyLocationPair> userList = getAllUsers();
		List<String> companyNameList = new LinkedList<String>();
		List<String> locationList = new LinkedList<String>();
		for (CompanyLocationPair pair: userList) {
			
			companyNameList.add(pair.getCompanyName());
			locationList.add(pair.getLocation());
		}
		
		List<ResponseStruct> responseStructList = pullAPIsForUsers(companyNameList, locationList, listPartGrabbers);
		
		//store responseStructList into database;
		try {
			dbAccessor.insertData(responseStructList);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * pull data and store for a list of given users on a list of given apis.
	 * @param companyNameList
	 * @param locationList
	 * @param listGrabber
	 * @return
	 */
	List<ResponseStruct> pullAPIsForUsers(List<String> companyNameList, List<String> locationList, 
			List<DataGrabberGeneric> listGrabber) {
		
		List<ResponseStruct> responseStructAllList = new LinkedList<ResponseStruct>();
		for (DataGrabberGeneric grabber: listGrabber) {
			
			try {
				List<ResponseStruct> responseStructList = grabber.pullDataForAll(companyNameList, locationList);
				for (ResponseStruct responseStruct: responseStructList) {
					
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
	String searchReviews(String companyName, String keyword) {
		// TODO Auto-generated method stub....
		
		List<String> attributes = new LinkedList<String>();
		
		String result = null;
		try {
			
			result = dbAccessor.select(keyword.toLowerCase(), companyName.toLowerCase(), attributes);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
    private static String getFileContents(String filename)
            throws IOException, FileNotFoundException
        {
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