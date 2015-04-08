package service;

import grabber.DataGrabberGeneric;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONException;

import database.AccessData;

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
	 * path of resources folder.
	 */
	private static final String SOURCE_PATH = Paths.get(".").toAbsolutePath()
			+ "\\resources\\";
	
	/**
	 * path of grabbers folder.
	 */
	private static final String GRABBER_PATH = Paths.get(".").toAbsolutePath() + "\\src\\grabber\\";

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
	public static List<String> sentimentAnalyze(List<String> reviewList) {
		
		List<String> sentimentList = new LinkedList<String>();
		for (int i = 0; i < reviewList.size(); i++) {
			
			sentimentList.add(sentimentAnalyze(reviewList.get(i)));
		}
		
		return sentimentList;
	}

	/**
	 * execute sentiment analysis for one review;
	 * @param review
	 * @return
	 */
	public static String sentimentAnalyze(String review) {
		
		return "neutral";
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
		listUsers.add(new CompanyLocationPair("Zingerman's", "ann arbor,48105"));
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
		dbAccessor.initializeDatabase(dbAddr, keyspaceName, tableName, invertTableName);
		try {
			dbAccessor.init(SOURCE_PATH);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Get all grabbers;
		File grabberFolder = new File(GRABBER_PATH);
		List<String> typeNameList = new LinkedList<String>();
		String packagePath = "grabber.";
		for (File file: grabberFolder.listFiles()) {
			
			if (file.getName().startsWith("API")) {
				
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
				// TODO Auto-generated catch block
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

		pullAPIsAndStoreForAllUsers(listNewGrabber);
		
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
		
		List<ResponseStruct> responseStructList = new LinkedList<ResponseStruct>();
		for (DataGrabberGeneric grabber: listGrabber) {
			
			try {
				List<String> responseList = grabber.pullDataForAll(companyNameList, locationList);
				responseStructList.addAll(ResponseStruct.getReponseStructListForOneAPI(responseList, 
						companyNameList, grabber.toString()));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return responseStructList;
	}

	@Override
	String searchReviews(String companyName, String keyword) {
		// TODO Auto-generated method stub
		
		keyword = "";
		String[] attributes = {};
		
		String result = null;
		try {
			
			result = dbAccessor.select(keyword.toLowerCase(), companyName.toLowerCase(), Arrays.asList(attributes));
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

}