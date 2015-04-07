package service;

import grabber.DataGrabberGeneric;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
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
		
		File confFile = new java.io.File(SOURCE_PATH + "\\init.conf");
		boolean ifPull = false;
		try {
			Scanner scanner = new Scanner(confFile);
			while (scanner.hasNext()) {

				String lineStr = scanner.nextLine();
				if (lineStr.split(" ")[1].equalsIgnoreCase("NO")) {

					// pull data for all users.
					ifPull = true;
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

	@Override
	void pullAPIsforAllUsers() {
		// TODO Auto-generated method stub
		List<CompanyLocationPair> listUsers = getAllUsers();
		List<String> companyNameList = new LinkedList<String>();
		List<String> locationList = new LinkedList<String>();
		for (CompanyLocationPair pair: listUsers) {
			
			companyNameList.add(pair.getCompanyName());
			locationList.add(pair.getLocation());
		}
		
		pullAPIAndStoreForUsers(companyNameList, locationList);
	}

	@Override
	void pullAPIAndStoreForUsers(List<String> companyNameList,
			List<String> locationList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	String searchReviews(String companyName, String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

}