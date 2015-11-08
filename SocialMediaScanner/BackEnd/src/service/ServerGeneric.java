package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import grabber.DataGrabberGeneric;

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
	    responseHeaders.set("Access-Control-Allow-Origin", "*");
	    responseHeaders.set("Content-Type", "application/json");
	    exchange.sendResponseHeaders(200, 0);
	    OutputStream responseBody = exchange.getResponseBody();
	    URI uri = exchange.getRequestURI();
	    List<CompanyStruct> companyNameList = new LinkedList<>();
	   
	    
	    List<String> locationList = new LinkedList<>();
	    String query = uri.getQuery();
	    String[] elements = query.split("&");
	    for (int i = 0; i < elements.length; i++) {

			String element = elements[i];
			String val = URLDecoder.decode(element.split("=")[1], "UTF-8");
			if (i % 2 == 0) {
				CompanyStruct newCompany = new CompanyStruct(val, "","","", "");
			    companyNameList.add(newCompany);
			} else {
	
			    locationList.add(val);
			}
	    }


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
		String val = URLDecoder.decode(element.split("=")[1], "UTF-8");

		if (name.equalsIgnoreCase("company name")) {

		    companyName = val;
		} else if (name.equalsIgnoreCase("keyword")) {

		    keyword = val;
		} else {

		    System.err.println("URI format invalid!");
		}
	    }

	    if (keyword == null) {

		keyword = "";
	    }

	    // System.out.println(companyName + " " + keyword);
	    String jsonResponse = searchReviews(companyName, keyword);
	    responseBody.write(jsonResponse.getBytes());
	    responseBody.close();

	}
    }

    
    /**
     * Handler to handle client request.
     * 
     * @author Yuke
     * 
     */
    class QueryHandlerInit implements HttpHandler {

	@SuppressWarnings("restriction")
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
	    Headers responseHeaders = exchange.getResponseHeaders();
	    responseHeaders.set("Access-Control-Allow-Origin", "*");
	    responseHeaders.set("Content-Type", "application/json");
	    exchange.sendResponseHeaders(200, 0);

	    if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
                @SuppressWarnings("unchecked")
                Map<String, Object> parameters = new HashMap<String, Object>();
                InputStreamReader isr =
                    new InputStreamReader(exchange.getRequestBody(),"utf-8");
                BufferedReader br = new BufferedReader(isr);
                String query = br.readLine();
                
                parseQuery(query, parameters);
                
                String companyName = (String)parameters.get("companyName");
                String twitterName = (String)parameters.get("twitterName");
                String yelpName = (String)parameters.get("yelpName");
                String citygridName = (String) parameters.get("citygridName");
                String locationName = (String) parameters.get("locationName");
                
                CompanyStruct company = new CompanyStruct(companyName, twitterName, citygridName, yelpName, locationName);
                List<CompanyStruct> companies = new LinkedList<CompanyStruct>();
                companies.add(company);
                
                List<String> locations = new LinkedList<String>();
                locations.add(locationName);
                
                pullAllAPIAndStoreForUsers(companies, locations);
                
                
        	    OutputStream responseBody = exchange.getResponseBody();
        	    URI uri = exchange.getRequestURI();
        	    
        	    String output = "Successfully pulled data for ";
    
        	    output += companyName + " ,";
    
        	    responseBody.write(output.getBytes());
        	    responseBody.close();
                
                
            }
         
	}
    }
    
    class QueryHandlerUpdateAPI implements HttpHandler {

	@SuppressWarnings("restriction")
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		System.out.println("inside update api");
	    Headers responseHeaders = exchange.getResponseHeaders();
	    responseHeaders.set("Access-Control-Allow-Origin", "*");
	    responseHeaders.set("Content-Type", "application/json");
	    exchange.sendResponseHeaders(200, 0);

	    if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
                @SuppressWarnings("unchecked")
                Map<String, Object> parameters = new HashMap<String, Object>();
                InputStreamReader isr =
                    new InputStreamReader(exchange.getRequestBody(),"utf-8");
                BufferedReader br = new BufferedReader(isr);
                String query = br.readLine();
                
                parseQuery(query, parameters);
                
                
                
//                List<ResponseStruct> pullAPIsForUsers(List<CompanyStruct> companyNameList, List<String> locationList,
//            			List<DataGrabberGeneric> listGrabber)
                String companyName = (String)parameters.get("companyName");
                String twitterName = (String)parameters.get("twitterName");
                String yelpName = (String)parameters.get("yelpName");
                String citygridName = (String) parameters.get("citygridName");
                String locationName = (String) parameters.get("locationName");
                
                String APIs = (String) parameters.get("apis");

                System.out.println(APIs);
                
                List<String> APIlist = Arrays.asList(APIs.split("\\s*,\\s*"));
                      
//                for(String api: items) {
//                	
//                	System.out.println("API: " + api + " " + api.length());
//                }
                
                CompanyStruct company = new CompanyStruct(companyName, twitterName, citygridName, yelpName, locationName);
                List<CompanyStruct> companies = new LinkedList<CompanyStruct>();
                companies.add(company);
                pullSpecificAPIforUsers(APIlist, companies);
                
                
                
                
//                CompanyStruct company = new CompanyStruct(companyName, twitterName, citygridName, yelpName, locationName);
//                List<CompanyStruct> companies = new LinkedList<CompanyStruct>();
//                companies.add(company);
//                
//                List<String> locations = new LinkedList<String>();
//                locations.add(locationName);
//                
//                pullAllAPIAndStoreForUsers(companies, locations);
                
                
        	    OutputStream responseBody = exchange.getResponseBody();
        	    URI uri = exchange.getRequestURI();
        	    
        	    String output = "Successfully pulled data for ";
    
        	    output += companyName + " ,";
    
        	    responseBody.write(output.getBytes());
        	    responseBody.close();
                          
            }
         
	}
    }
    
    
    @SuppressWarnings("unchecked")
    private void parseQuery(String query, Map<String, Object> parameters)
        throws UnsupportedEncodingException {

        if (query != null) {
            String pairs[] = query.split("[&]");
            System.out.println("parssize: " + pairs.length);
            for (String pair : pairs) {
            	System.out.println("paircurrent: " + pair);
                String param[] = pair.split("[=]");

                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0],
                        System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1],
                        System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if(obj instanceof List<?>) {
                        List<String> values = (List<String>)obj;
                        values.add(value);
                    } else if(obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String)obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
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
    abstract void pullAllAPIAndStoreForUsers(List<CompanyStruct> companyNameList, List<String> locationList);

    /**
     * pull data for all users.
     * 
     * @return
     */
    abstract void pullAPIsAndStoreForAllUsers(List<DataGrabberGeneric> listGrabber);
    
    
    abstract void pullSpecificAPIforUsers(List<String> APIs, List<CompanyStruct> companyNameList);

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
	
	server.createContext("/init", new QueryHandlerInit());
	server.createContext("/updateAPI", new QueryHandlerUpdateAPI());
	
	server.setExecutor(Executors.newCachedThreadPool());
	server.start();
	System.out.println("Server is listening on port " + port);
    }

}