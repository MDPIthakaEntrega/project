package service;

import grabber.DataGrabberGeneric;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * The actual server that provides service.
 * 
 * @author Yuke
 * 
 */
class Server extends ServerGeneric {
	
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
	
	private List<Class<? extends DataGrabberGeneric>> listGrabberClass = new LinkedList<Class<? extends DataGrabberGeneric>>();
	
	private List<DataGrabberGeneric> listGrabber = new LinkedList<DataGrabberGeneric>();

	private static final String SOURCE_PATH = Paths.get(".").toAbsolutePath()
			+ "\\resources";
	
	private static final String GRABBER_PATH = Paths.get(".").toAbsolutePath() + "\\src\\grabber";

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
	 * Constructor.
	 * 
	 * @param port
	 */
	public Server(int port) {
		super(port);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Initialize the server, and tasks include: set up the service, connect to
	 * database (Cassandra and MySql), activate the social media data grabber.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initServer() {
		// TODO Auto-generated method stub
		
		//Get all grabbers;
		System.out.println(GRABBER_PATH);
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
		
		// Connect to Cassandra;
		
		File confFile = new java.io.File(SOURCE_PATH + "\\ServiceInit.conf");
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

		if (ifPull == true) {

			try {
				PrintWriter pw = new PrintWriter(confFile);
				pw.println("CityGrid YES");
				pw.flush();
				pw.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	List<CompanyLocationPair> getAllUsers() {
		
		List<CompanyLocationPair> listUsers = new LinkedList<CompanyLocationPair>();
		listUsers.add(new CompanyLocationPair("Zingerman's", "ann arbor,48105"));
		return listUsers;
	}
	
	public static String sentimentAnalyze(String review) {
		
		return "neutral";
	}
	
	public static List<String> sentimentAnalyze(List<String> reviewList) {
		
		List<String> sentimentList = new LinkedList<String>();
		for (int i = 0; i < reviewList.size(); i++) {
			
			sentimentList.add(sentimentAnalyze(reviewList.get(i)));
		}
		
		return sentimentList;
	}

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
	}
	
	@Override
	void pullAllAPIAndStoreForUsers(List<String> companyNameList,
			List<String> locationList) {
		// TODO Auto-generated method stub
		
		List<ResponseStruct> responseStructList = pullAPIsForUsers(companyNameList, locationList, listGrabber);

		//store responseStructList to the database;
	}

	@Override
	String searchReviews(String companyName, String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

}