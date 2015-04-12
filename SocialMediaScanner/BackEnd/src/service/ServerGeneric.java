package service;

import grabber.DataGrabberGeneric;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * The generic server.
 * 
 * @author Yuke
 * 
 */
abstract class ServerGeneric {

	class QueryHandlerPull implements HttpHandler {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			// TODO Auto-generated method stub
			Headers responseHeaders = exchange.getResponseHeaders();
			responseHeaders.set("Content-Type", "text/plain");
			exchange.sendResponseHeaders(200, 0);
			OutputStream responseBody = exchange.getResponseBody();
			URI uri = exchange.getRequestURI();
			List<String> companyNameList = new LinkedList<>();
			List<String> locationList = new LinkedList<>();
			String query = uri.getQuery();
			String[] elements = query.split("&");
			for (int i = 0; i < elements.length; i++) {
				
				String element = elements[i];
				String val = URLDecoder.decode(element.split("=")[1], "UTF-8");
				if (i % 2 == 0) {
					
					companyNameList.add(val);
				}
				else {
					
					locationList.add(val);
				}
			}
			
			//System.out.println(companyNameList);
			//System.out.println(locationList);
			
			pullAllAPIAndStoreForUsers(companyNameList, locationList);

			String output = "Successfully pulled data for ";
			for (int i = 0; i < companyNameList.size(); i++) {
				
				output += companyNameList.get(i) + " ,";
			}
			responseBody.write(output.getBytes());
			responseBody.close();
		}
	}

	/**
	 * Handler to handle client request.
	 * 
	 * @author Yuke
	 * 
	 */
	class QueryHandlerSearch implements HttpHandler {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			// TODO Auto-generated method stub
						
			Headers responseHeaders = exchange.getResponseHeaders();
			responseHeaders.set("Access-Control-Allow-Origin", "*");
			responseHeaders.set("Content-Type", "application/json");
			exchange.sendResponseHeaders(200, 0);

			OutputStream responseBody = exchange.getResponseBody();
			URI uri = exchange.getRequestURI();
			String companyName = null;
			String keyword = null;

			String query = uri.getQuery();
			String elements[] = query.split("&");
			for (String element : elements) {
				
				if (element.split("=").length == 1) {
					
					continue;
				}
				
				String name = element.split("=")[0];
				String val = URLDecoder.decode(element.split("=")[1],
						"UTF-8");
				
				if (name.equalsIgnoreCase("company name")) {
					
					companyName = val;
				}
				else if (name.equalsIgnoreCase("keyword")) {
					
					keyword = val;
				}
				else {
					
					System.err.println("URI format invalid!");
				}
			}
			
			if (keyword == null) {
				
				keyword = "";
			}
			
			
			//System.out.println(companyName + "   " + keyword);
			String jsonResponse = searchReviews(companyName, keyword);
			responseBody.write(jsonResponse.getBytes());
			responseBody.close();

		}
	}
	
	

	/**
	 * the port number specified for listening.
	 */
	int port;

	/**
	 * Constructor
	 * 
	 * @param port
	 */
	ServerGeneric(int port) {

		this.port = port;
	}

	/**
	 * Initialize the server
	 */
	abstract void initServer();

	/**
	 * pull data for users and store into database.
	 * 
	 * @param companyNameList
	 * @param locationList
	 */
	abstract void pullAllAPIAndStoreForUsers(List<String> companyNameList,
			List<String> locationList);
	
	/**
	 * pull data for all users.
	 * 
	 * @return
	 */
	abstract void pullAPIsAndStoreForAllUsers(List<DataGrabberGeneric> listGrabber);
	
	abstract String searchReviews(String companyName, String keyword);
	
	/**
	 * Start the service.
	 * 
	 * @throws IOException
	 */
	void serve() throws IOException {

		InetSocketAddress address = new InetSocketAddress(port);
		HttpServer server = HttpServer.create(address, 0);

		// Specify the handlers to deal with different contexts.
		server.createContext("/search", new QueryHandlerSearch());
		server.createContext("/pull", new QueryHandlerPull());
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		System.out.println("Server is listening on port " + port);
	}

}