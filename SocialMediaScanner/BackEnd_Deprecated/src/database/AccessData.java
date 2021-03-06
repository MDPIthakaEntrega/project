/*
 * 
 * Charlie Scott
 * Implementation for "Data" interface, allows insertion into database, and selection of
 * reviews for database
 * 
 */

package database;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import service.ResponseStruct;
import utility.Parser;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.querybuilder.QueryBuilder;

public class AccessData implements Data {

	private static Cluster cluster;
	protected static Session current_session;
	protected static PreparedStatement preparedStmt; 
	protected static String host;
	protected static String keyspace_name;
	protected static String review_table;
	protected static String inverted_table;
	private static boolean initialized = false;
	
	public static void main(String[] args) throws InstantiationException,

		IllegalAccessException, ClassNotFoundException, JSONException {
	
	  AccessData test = new AccessData();
	  AccessData.initializeDatabase("127.0.0.1", "review_keyspace","review_table", "inverted_table"); 
//	  test.init("C:/Users/Charlie/Documents/MDP-Ithaka/Service-Java/project/SocialMediaScanner/BackEnd/resources/");
	  List<String> test_list = new LinkedList<String>(); 
//	  String test_string = "{\"results\":{\"query_id\":\"123\",\"uri\":\"http://api.citygridmedia.com/reviews/reviews/v2/search/where?format=json&page=1&rpp=3&what=zingerman%27s&histograms=false&where=Ann+Arbor&publisher=10000008938&region_type=circle\",\"first_hit\":1,\"last_hit\":3,\"total_hits\":66,\"page\":1,\"rpp\":3,\"did_you_mean\":null,\"regions\":[{\"type\":\"city\",\"name\":\"Ann Arbor, MI\",\"latitude\":42.275434999999995,\"longitude\":-83.731138999999999,\"default_radius\":5.3200}],\"histograms\":[],\"reviews\":[{\"review_id\":\"ip_10302154082\",\"review_title\":\"Zingerman's Delicatessen is great!\",\"review_text\":\"Zingerman's is notorious for its sandwiches and it lives up to the hype.  They variety and quality of the sandwiches is great.  There's also plenty of seating.\",\"pros\":null,\"cons\":null,\"review_rating\":10,\"review_date\":\"2009-07-29T03:46:00Z\",\"review_author\":\"Sally L\",\"helpful_count\":null,\"unhelpful_count\":null,\"type\":\"user_review\",\"source\":\"INSIDERPAGES\",\"reference_id\":null,\"source_id\":\"17\",\"attribution_logo\":\"http://www.insiderpages.com/images/ip_logo_88x33.jpg\",\"attribution_text\":\"Insider Pages\",\"attribution_url\":\"http://www.insiderpages.com/\",\"listing_id\":5168887,\"business_name\":\"Zingerman's Delicatessen\",\"impression_id\":\"000b0000037b007f1e8fea44feb3bc59e6e4421661\",\"review_author_url\":\"http://my.citysearch.com/members/public/profile/Sally+L?i=000b0000037b007f1e8fea44feb3bc59e6e4421661\",\"review_url\":\"http://www.insiderpages.com/b/13313585094/zingermans-delicatessen-ann-arbor\",\"public_id\":\"zingermans-delicatessen-ann-arbor-2\"},{\"review_id\":\"ip_10218180545\",\"review_title\":\"Zingerman's\",\"review_text\":\"This is a famous deli in Ann Arbor, that is known for its fresh bread, and wonderful, large sandwiches.  Not only do they have sandwiches, but the deli has expanded to lots of items that you can purchase.  Don't get me wrong, the�\",\
//	  "pros\":null,\"cons\":null,\"review_rating\":6,\"review_date\":\"2005-09-29T13:53:00Z\",\"review_author\":\"Teresa F\",\"helpful_count\":null,\"unhelpful_count\":null,\"type\":\"user_review\",\"source\":\"INSIDERPAGES\",\"reference_id\":null,\"source_id\":\"17\",\"attribution_logo\":\"http://www.insiderpages.com/images/ip_logo_88x33.jpg\",\"attribution_text\":\"Insider Pages\",\"attribution_url\":\"http://www.insiderpages.com/\",\"listing_id\":5168887,\"business_name\":\"Zingerman's Delicatessen\",\"impression_id\":\"000b000003396ba6fe8c5d4584b12d722de557726d\",\"review_author_url\":\"http://my.citysearch.com/members/public/profile/Teresa+F?i=000b000003396ba6fe8c5d4584b12d722de557726d\",\"review_url\":\"http://www.insiderpages.com/b/13313585094/zingermans-delicatessen-ann-arbor\",\"public_id\":\"zingermans-delicatessen-ann-arbor-2\"},{\"review_id\":\"mism_125160\",\"review_title\":\"Zingerman&#39;s and some zing to my taste buds!\",\"review_text\":\"Zingerman&#39;s Deli has been an Ann Arbor classic and favorite for years and years, with many more to come....\",\"pros\":null,\"cons\":null,\"review_rating\":8,\"review_date\":\"2011-04-13T22:13:43Z\",\"review_author\":\"mmmfood1\",\"helpful_count\":null,\"unhelpful_count\":null,\"type\":\"user_review\",\"source\":\"Menuism\",\"reference_id\":null,\"source_id\":\"51\",\"attribution_logo\":\"http://1.static.menuism.com/images/logos/logo-transparent-88x31.png\",\"attribution_text\":\"Menuism\",\"attribution_url\":\"http://www.menuism.com/\",\"listing_id\":5168887,\"business_name\":\"Zingerman's Delicatessen\",\"impression_id\":\"000b0000032d61476a752d458a93f2e3c307bad808\",\"review_author_url\":\"http://my.citysearch.com/members/public/profile/mmmfood1?i=000b0000032d61476a752d458a93f2e3c307bad808\",\"review_url\":\"http://www.menuism.com/restaurants/zingermans-delicatessen-ann-arbor-189073#p125160\",\"public_id\":\"zingermans-delicatessen-ann-arbor-2\"}]}}"
//	  ; 
	  String test_string = "{\"statuses\":[{\"metadata\":{\"iso_language_code\":\"en\",\"result_type\":\"recent\"},\"created_at\":\"Sat Apr 11 12:49:18 +0000 2015\",\"id\":586873658875883500,\"id_str\":\"586873658875883520\",\"text\":\"Foodie entrepreneur talks to students about success: Ari Weinzweig, co-owner of Zingerman�s Deli, spoke to Alp... http://t.co/DKF4VVHbx7\", \"source\":\"<a href=http://twitterfeed.com rel=nofollow>twitterfeed</a>\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweet_count\":0,\"favorite_count\":1,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[],\"urls\":[{\"url\":\"http://t.co/DKF4VVHbx7\",\"expanded_url\":\"http://bit.ly/1FwONxM\",\"display_url\":\"bit.ly/1FwONxM\",\"indices\":[114,136]}]},\"favorited\":false,\"retweeted\":false,\"possibly_sensitive\":false,\"lang\":\"en\"}],\"search_metadata\":{\"completed_in\":0.015,\"max_id\":586873658875883500,\"max_id_str\":\"586873658875883520\",\"next_results\":\"?max_id=586873658875883519&q=Zingerman%27s%20deli&count=1&include_entities=1\",\"query\":\"Zingerman%27s+deli\",\"refresh_url\":\"?since_id=586873658875883520&q=Zingerman%27s%20deli&include_entities=1\",\"count\":1,\"since_id\":0,\"since_id_str\":\"0\"}}";
	  test_list.add(test_string);
	  
	  List<ResponseStruct> test_api_struct = new LinkedList<ResponseStruct>(); 
	  ResponseStruct testresponse = new ResponseStruct(test_string, "zingerman's", "Twitter");
	  test_api_struct.add(testresponse); 
	  test.insertData(test_api_struct);		  
//	  System.out.println(test.select("", "zingerman's", new LinkedList<String>()).length());
	  AccessData.close();
	  System.out.println("Success"); 
	  
	  return;
	}
	
	/**
	 * 
	 * Initialize variables needed to create database and creates database Must
	 * be called before insertData() or select()
	 */
	public static void initializeDatabase(String host_in, String keyspace_name_in, String review_table_in,
			String inverted_table_in) {
			
		if(!initialized) {
			host = host_in;
	        keyspace_name = keyspace_name_in;
	        review_table = review_table_in;
	        inverted_table = inverted_table_in;
	        AccessData.connect();
			preparedStmt = current_session.prepare("INSERT INTO " +  review_table + 
		    		" (review_id, company_name, json)" + "VALUES (?,?,?) IF NOT EXISTS;");
	        initialized = true;
		}
	}
	
	public static void close() {
		
		// TODO close prepared statement? Fxn not found
		current_session.close();
		cluster.close();
	}
	
	public static void connect() {
		

		cluster = Cluster
        	    .builder()
        	    .addContactPoint(host)
        	    .withRetryPolicy(DefaultRetryPolicy.INSTANCE)
        	    .build();

		current_session = cluster.connect(keyspace_name); 

	}
	
	/**
	 * 
	 * returns connection to the database
	 * 
	 */
	public static Session getConnection() {
		
		return current_session;
	}
	
	static public void closeConnection() {
		
		current_session.close();
		API.closeConnection();
	}

	/*
	 * Inserts responses from API to database for a given company
	 */
	@Override
	public void insertData(List<ResponseStruct> responses)
			throws InstantiationException, IllegalAccessException,
			JSONException, ClassNotFoundException {
		String api_name;
		for (ResponseStruct response : responses) {
			api_name = response.getAPIName();
			Class<?> api = Class.forName(this.getClass().getPackage().getName()
					+ "." + api_name);
			try {
				((API) api.newInstance()).insert(response);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	// TODO improve search object
	@Override
	public String select(String search, String company_name, List<String> APIs,
			List<String> attributes) throws JSONException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {

		Statement statement;
		ResultSet results = null;
		JSONArray formatted_reviews = new JSONArray();
		
		if(attributes.size() == 0) {
			List<String> allAttributes = Parser.APIParser.getBusAttributes();
			for(String attribute: allAttributes) {
				try {
					attributes.add(attribute);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		search = search.replaceAll("[^a-zA-Z\\s-.]", "");
		search = search.replaceAll("[-|'.]", " ");
		search = search.toLowerCase();
		String[] each_word = search.split("[ ]+");
		statement = null;
		try {

			statement = QueryBuilder.select().all()
					.from(keyspace_name, review_table)
					.where(QueryBuilder.eq("company_name", company_name));
			results = current_session.execute(statement);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Row row : results) {
			String api = row.getString("review_id");
			api = api.substring(0, api.indexOf('_'));
			Class<?> api_type = Class.forName(this.getClass().getPackage()
					.getName()
					+ "." + api);
			JSONObject jsonObj = ((API) (api_type.newInstance())).formatReview(
					row, attributes);

//			try {
				
				if (containsAll(jsonObj.getString("content"), each_word) &&
						inAPIList(APIs, jsonObj.getString("source"))) {

					formatted_reviews.put(jsonObj);
				}

//			}
//			catch(JSONException j) {
//				JSONArray descriptions = jsonObj.getJSONArray("content");
//				if (containsAll(descriptions.toString(), each_word)) {
//					formatted_reviews.put(jsonObj);
//				}
//			}
		}
		JSONObject all_reviews = new JSONObject();
		all_reviews.put("reviews", formatted_reviews);
		return all_reviews.toString();
	}
	
	private boolean inAPIList(List<String> APIs, String API) {
		
		for(String checkAPI: APIs) {
			
			if(checkAPI.equalsIgnoreCase(API)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * helper function: return true if str contains all the words in arrWord.
	 * 
	 * @param str
	 * @param arrWord
	 * @return
	 */
	private boolean containsAll(String str, String[] arrWord) {

		String strToLower = str.toLowerCase();
		for (String word : arrWord) {

			if (!strToLower.contains(word.toLowerCase())) {

				return false;
			}
		}

		return true;
	}

	/**
	 * get list of rows according to company name.
	 * 
	 * @param company_name
	 * @return
	 */
	private List<Row> getAllRows(String company_name) {

		List<Row> listRow = new LinkedList<Row>();
		Statement statement = QueryBuilder.select().all()
				.from(keyspace_name, review_table)
				.where(QueryBuilder.eq("company_name", company_name));
		ResultSet results = current_session.execute(statement);
		for (Row row : results) {

			listRow.add(row);
		}

		return listRow;
	}

	/**
	 * 
	 * Select reviews for a company with a given search
	 */

	@Override
	public String select(String search, String company_name)
			throws JSONException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {

		JSONArray formatted_reviews = new JSONArray();

		search = search.replaceAll("[^a-zA-Z\\s-.]", "");
		search = search.replaceAll("[-|'.]", " ");
		search = search.toLowerCase();
		String[] each_word = search.split("[ ]+");
		if (search == "") {
			List<Row> listRow = getAllRows(company_name);
			for (Row row : listRow) {

				String api = row.getString("review_id");
				api = api.substring(0, api.indexOf('_'));
				Class<?> api_type = Class.forName(this.getClass().getPackage()
						.getName()
						+ "." + api);
				formatted_reviews.put(((API) (api_type.newInstance()))
						.formatReview(row, new LinkedList<String>()));
			}
		} else {

			List<Row> listRow = getAllRows(company_name);
			for (Row row : listRow) {

				String api = row.getString("review_id");
				api = api.substring(0, api.indexOf('_'));
				Class<?> api_type = Class.forName(this.getClass().getPackage()
						.getName()
						+ "." + api);
				JSONObject jsonObj = ((API) (api_type.newInstance()))
						.formatReview(row, new LinkedList<String>());
				if (containsAll(jsonObj.getString("content"), each_word)) {

					formatted_reviews.put(jsonObj);
				}
			}
		}
		return formatted_reviews.toString();
	}
}
