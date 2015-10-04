package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.alchemyapi.api.AlchemyAPI;

import database.AccessData;
import grabber.DataGrabberGeneric;

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

    private static final String dbAddr = "127.0.0.1";

    private static final String invertTableName = "invert_table";

    private static final String keyspaceName = "review_keyspace";

    private static final String tableName = "review_table";

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

    private static SentimentStruct parseSentimentStruct(Document xmlDoc) {

	Element rootElement = xmlDoc.getDocumentElement();
	String typeStr = null;
	double score = 0;
	NodeList typeList = rootElement.getElementsByTagName("type");
	typeStr = typeList.item(0).getTextContent();
	// System.out.println(typeStr);
	if (!typeStr.equalsIgnoreCase("neutral") && !typeStr.equalsIgnoreCase("mixed")) {

	    score = Double.parseDouble(rootElement.getElementsByTagName("score").item(0).getTextContent());
	}
	SentimentStruct sentiStruct = new SentimentStruct(typeStr, score);

	return sentiStruct;
    }

    /**
     * execute sentiment analysis for one review;
     * 
     * @param review
     * @return
     */
    public static SentimentStruct sentimentAnalyze(String review) {
	try {
	    AlchemyAPI alchemyObj = AlchemyAPI.GetInstanceFromFile(utility.Parser.API_KEY_PATH);
	    Document result = alchemyObj.TextGetTextSentiment(review);
	    SentimentStruct s = parseSentimentStruct(result);
	    return s;

	} catch (IOException e) {
	    e.printStackTrace();
	} catch (XPathExpressionException e) {
	    e.printStackTrace();
	} catch (SAXException e) {
	    e.printStackTrace();
	} catch (ParserConfigurationException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private AccessData dbAccessor = new AccessData();

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
     * list of instances of all api grabbers.
     */
    private List<DataGrabberGeneric> listGrabber = new LinkedList<DataGrabberGeneric>();

    /**
     * list of class class of all api grabbers.
     */
    private List<Class<? extends DataGrabberGeneric>> listGrabberClass = new LinkedList<Class<? extends DataGrabberGeneric>>();

    /**
     * Constructor.
     * 
     * @param port
     */
    public Server(int port) {
	super(port);
    }

    /**
     * get the company name and location of all users.
     * 
     * @return
     */
    List<CompanyLocationPair> getAllUsers() {
	// TODO connect to front end to get a list of all users.
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

	// Charlie adding to test new implementation
	// dbAccessor.initializeDatabase(dbAddr, keyspaceName, tableName,
	// invertTableName);
	AccessData.initializeDatabase(dbAddr, keyspaceName, tableName, invertTableName);

	AccessData.init(utility.Parser.SOURCE_PATH);

	// Get all grabbers;
	File grabberFolder = new File(utility.Parser.GRABBER_PATH);
	List<String> typeNameList = new LinkedList<String>();
	String packagePath = "grabber.";
	for (File file : grabberFolder.listFiles()) {
	    if (file.getName().startsWith("Grabber")) {
		typeNameList.add(packagePath + file.getName().split("\\.")[0]);
	    }
	}

	for (String typeName : typeNameList) {
	    try {
		listGrabberClass.add((Class<? extends DataGrabberGeneric>) Class.forName(typeName));
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
	    }
	}

	for (Class<? extends DataGrabberGeneric> grabberClass : listGrabberClass) {
	    try {
		listGrabber.add(grabberClass.newInstance());
	    } catch (InstantiationException | IllegalAccessException e) {
		e.printStackTrace();
	    }
	}

	Map<String, String> mapConfigInit = utility.Parser.getServiceInitConfig();
	List<DataGrabberGeneric> listNewGrabber = new LinkedList<DataGrabberGeneric>();
	for (String APIName: mapConfigInit.keySet()) {
	    if (mapConfigInit.get(APIName).equalsIgnoreCase("NO")) {
		for (DataGrabberGeneric grabber: listGrabber) {
		    System.out.println(grabber.toString() + " " + APIName);
		    if (grabber.toString().equalsIgnoreCase(APIName)) {
			listNewGrabber.add(grabber);
		    }
		}
	    }
	}
	
	System.exit(0);
	
	if (listNewGrabber.size() > 0) {
	    pullAPIsAndStoreForAllUsers(listNewGrabber);
	}

	try {
	    String serviceInitConfPath = utility.Parser.SOURCE_PATH + "/ServiceInit.conf";
	    PrintWriter pw = new PrintWriter(serviceInitConfPath);
	    for (String APIName: mapConfigInit.keySet()) {
		pw.println(APIName + " YES");
	    }
	    pw.flush();
	    pw.close();
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
    }

    @Override
    void pullAllAPIAndStoreForUsers(List<String> companyNameList, List<String> locationList) {
	for (String name : companyNameList) {

	    System.out.println("curname: " + name);
	}
	List<ResponseStruct> responseStructList = pullAPIsForUsers(companyNameList, locationList, listGrabber);

	// store responseStructList to the database;
	try {
	    dbAccessor.insertData(responseStructList);
	} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | JSONException e) {
	    e.printStackTrace();
	}
    }

    @Override
    void pullAPIsAndStoreForAllUsers(List<DataGrabberGeneric> listPartGrabbers) {
	List<CompanyLocationPair> userList = getAllUsers();
	List<String> companyNameList = new LinkedList<String>();
	List<String> locationList = new LinkedList<String>();
	for (CompanyLocationPair pair : userList) {
	    companyNameList.add(pair.getCompanyName());
	    locationList.add(pair.getLocation());
	}

	List<ResponseStruct> responseStructList = pullAPIsForUsers(companyNameList, locationList, listPartGrabbers);

	// store responseStructList into database;
	try {
	    dbAccessor.insertData(responseStructList);
	} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | JSONException e) {
	    e.printStackTrace();
	}
    }

    /**
     * pull data and store for a list of given users on a list of given apis.
     * 
     * @param companyNameList
     * @param locationList
     * @param listGrabber
     * @return
     */
    List<ResponseStruct> pullAPIsForUsers(List<String> companyNameList, List<String> locationList,
	    List<DataGrabberGeneric> listGrabber) {

	List<ResponseStruct> responseStructAllList = new LinkedList<ResponseStruct>();
	for (DataGrabberGeneric grabber : listGrabber) {
	    try {
		List<ResponseStruct> responseStructList = grabber.pullDataForAll(companyNameList, locationList);
		for (ResponseStruct responseStruct : responseStructList) {
		    responseStruct.setAPIName(grabber.toString());
		}
		responseStructAllList.addAll(responseStructList);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return responseStructAllList;
    }

    @Override
    String searchReviews(String companyName, String keyword) {
	List<String> attributes = new LinkedList<String>();
	String result = null;
	try {

	    result = dbAccessor.select(keyword.toLowerCase(), companyName.toLowerCase(), attributes);
	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | JSONException e) {
	    e.printStackTrace();
	}

	return result;
    }

}