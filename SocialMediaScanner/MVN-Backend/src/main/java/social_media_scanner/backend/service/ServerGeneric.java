package social_media_scanner.backend.service;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import social_media_scanner.backend.grabber.DataGrabberGeneric;

/**
 * The generic server.
 * 
 * @author Yuke
 * 
 */
abstract class ServerGeneric {


    private boolean checkRequired(Map<String, ? extends Object> input, List<String> required) {

        for(String requirement : required) {

            if(input.get(requirement) == null) {
                System.out.println("requirement failed: " + requirement);
                return false;
            }
        }
        return true;
    }

    private void addToMap(String[] elements, Map<String, String> keyVal) throws UnsupportedEncodingException {

        for (String element : elements) {

            String[] split = element.split("=");
            String key = URLDecoder.decode(split[0], "UTF-8");
            String val = "";
            if(split.length > 1) {
                val = URLDecoder.decode(split[1], "UTF-8");
            }
//                System.out.println("key: " + key + " val: " + val);
            keyVal.put(key, val);

        }

    }


    /**
     *
     * /pull
     * Pulls data for a specific company from all API's (deprecated)
     * GET
     * company, location, yelpName, twitterName, citygridName
     * http://localhost:3456/pull?company=zingerman%27s&location=ann%20arbor&
     *  yelpName=zingermans-delicatessen-ann-arbor-2
     *
     */
    class QueryHandlerPull implements HttpHandler {

        List<String> required = Arrays.asList("company", "location", "yelpName", "twitterName", "citygridName");

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Access-Control-Allow-Origin", "*");
            responseHeaders.set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            OutputStream responseBody = exchange.getResponseBody();
            URI uri = exchange.getRequestURI();

            List<CompanyStruct> companyNameList = new LinkedList<CompanyStruct>();

            String query = uri.getQuery();
            String[] elements = query.split("&");

            Map<String, String> keyVal = new HashMap<>();

            addToMap(elements, keyVal);

            if(!checkRequired(keyVal, required)) {

                // TODO send invalid response
                String responseText = "Invalid, missing 1 or more parameter";
                responseBody.write(responseText.getBytes());
                responseBody.close();
                return;
            }

            CompanyStruct newCompany = new CompanyStruct(keyVal.get("company"), keyVal.get("twitterName"),
                    keyVal.get("citygridName"), keyVal.get("yelpName"), keyVal.get("location"));
            companyNameList.add(newCompany);

            pullAllAPIAndStoreForUsers(companyNameList);

            String output = "Successfully pulled data for ";
            for (CompanyStruct aCompanyNameList : companyNameList) {

                output += aCompanyNameList + " ,";
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

        List<String> required = Arrays.asList("company");

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // TODO Auto-generated method stub

            System.out.println("HERE1");
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Access-Control-Allow-Origin", "*");
            responseHeaders.set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);

            OutputStream responseBody = exchange.getResponseBody();
            URI uri = exchange.getRequestURI();
            List<String> APIs = new LinkedList<String>();

            Map<String, String> keyVal = new HashMap<>();

            String query = uri.getQuery();
            String elements[] = query.split("&");

            addToMap(elements, keyVal);

            if(!checkRequired(keyVal, required)) {

                //TODO send JSON response
                String responseText = "Invalid, missing 1 or more parameter";
                responseBody.write(responseText.getBytes());
                responseBody.close();
                return;
            }

            if(keyVal.get("twitter") != null && keyVal.get("twitter").equals("yes")) {
                System.out.println("in tiwtter only");
                APIs.add("twitter");
            }
            if(keyVal.get("citygrid") != null && keyVal.get("citygrid").equals("yes")) {

                APIs.add("citygrid");
            }
            if(keyVal.get("yelp") != null && keyVal.get("yelp").equals("yes")) {

                APIs.add("yelp");
            }
            if(APIs.isEmpty()) {

                APIs.addAll(Arrays.asList("twitter", "citygrid", "yelp"));
            }

            SearchStruct search = new SearchStruct(keyVal.get("company"), keyVal.get("query") == null ?
                    "": keyVal.get("query"), APIs);
            String jsonResponse = searchReviews(search);
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

        List<String> required = Arrays.asList("company", "location", "yelpName", "twitterName", "citygridName");

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
                    System.out.println("HERE!");
                    OutputStream responseBody = exchange.getResponseBody();
                    if(!checkRequired(parameters, required)) {

                        //TODO throw invalid
                        String responseText = "Invalid, missing 1 or more parameter";
                        responseBody.write(responseText.getBytes());
                        responseBody.close();
                        return;
                    }

                    String companyName = (String)parameters.get("company");
                    String twitterName = (String)parameters.get("twitterName");
                    String yelpName = (String)parameters.get("yelpName");
                    String citygridName = (String) parameters.get("citygridName");
                    String locationName = (String) parameters.get("location");

                    CompanyStruct company = new CompanyStruct(companyName, twitterName, citygridName,
                            yelpName, locationName);
                    List<CompanyStruct> companies = new LinkedList<CompanyStruct>();
                    companies.add(company);

                    pullAllAPIAndStoreForUsers(companies);

                    String output = "Successfully pulled data for ";

                    output += companyName + " ,";

                    responseBody.write(output.getBytes());
                    responseBody.close();


                }
            }
    }
    
    class QueryHandlerUpdateAPI implements HttpHandler {

        List<String> required = Arrays.asList("company", "location", "yelpName", "twitterName", "citygridName");

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

                OutputStream responseBody = exchange.getResponseBody();
                if(!checkRequired(parameters, required)) {

                    //TODO throw JSON
                    String responseText = "Invalid, missing 1 or more parameter";
                    responseBody.write(responseText.getBytes());
                    responseBody.close();
                    return;
                }

                String companyName = (String)parameters.get("company");
                String twitterName = (String)parameters.get("twitterName");
                String yelpName = (String)parameters.get("yelpName");
                String citygridName = (String) parameters.get("citygridName");
                String locationName = (String) parameters.get("location");
                
                String APIs = (String) parameters.get("apis");
                
                List<String> APIlist = new LinkedList<>(Arrays.asList(APIs.split("\\s*,\\s*")));

                try {
                    if(locationName.length() < 1) {
                        APIlist.remove("citygrid");
                    }
                    if(yelpName.length() < 1) {
                        APIlist.remove("yelp");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                
                CompanyStruct company = new CompanyStruct(companyName, twitterName, citygridName,
                        yelpName, locationName);
                List<CompanyStruct> companies = new LinkedList<CompanyStruct>();
                companies.add(company);
                pullSpecificAPIforUsers(APIlist, companies);

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

        try {
            if (query != null) {
                String pairs[] = query.split("[&]");
                System.out.println("parssize: " + pairs.length);
                for (String pair : pairs) {
                    System.out.println("paircurrent: " + pair);
                    String param[] = pair.split("[=]");

                    String key = null;
                    String value = "";
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
        catch (Exception e) {
            e.printStackTrace();
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
     */
    abstract void pullAllAPIAndStoreForUsers(List<CompanyStruct> companyNameList);

    /**
     * pull data for all users.
     * 
     * @return
     */
    abstract void pullAPIsAndStoreForAllUsers(List<DataGrabberGeneric> listGrabber);

    abstract void pullSpecificAPIforUsers(List<String> APIs, List<CompanyStruct> companyNameList);

    abstract String searchReviews(SearchStruct search);

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