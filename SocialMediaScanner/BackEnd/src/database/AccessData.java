/*
 * 
 * Charlie Scott
 * Implementation for "Data" interface, allows insertion into database, and selection of
 * reviews for database
 * 
 */

package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import service.ResponseStruct;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;

public class AccessData implements Data {

	private Map<String, Class<?>> sources = new HashMap<String, Class<?>>();
	private String folder_location = new String();
	private List<String> business_attributes = new LinkedList<String>();
	static protected Session current_session;
	static protected String host;
	static protected String keyspace_name;
	static protected String review_table;
	static protected String inverted_table;

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, JSONException {
//		
		  AccessData test = new AccessData();
		  test.initializeDatabase("127.0.0.1", "review_keyspace","review_table", "inverted_table"); 
		  test.init("C:/Users/Charlie/Documents/MDP-Ithaka/Service-Java/project/SocialMediaScanner/BackEnd/resources/");
//		  List<String> test_list = new LinkedList<String>(); 
//		  String test_string = "{\"results\":{\"query_id\":\"123\",\"uri\":\"http://api.citygridmedia.com/reviews/reviews/v2/search/where?format=json&page=1&rpp=3&what=zingerman%27s&histograms=false&where=Ann+Arbor&publisher=10000008938&region_type=circle\",\"first_hit\":1,\"last_hit\":3,\"total_hits\":66,\"page\":1,\"rpp\":3,\"did_you_mean\":null,\"regions\":[{\"type\":\"city\",\"name\":\"Ann Arbor, MI\",\"latitude\":42.275434999999995,\"longitude\":-83.731138999999999,\"default_radius\":5.3200}],\"histograms\":[],\"reviews\":[{\"review_id\":\"ip_10302154082\",\"review_title\":\"Zingerman's Delicatessen is great!\",\"review_text\":\"Zingerman's is notorious for its sandwiches and it lives up to the hype.  They variety and quality of the sandwiches is great.  There's also plenty of seating.\",\"pros\":null,\"cons\":null,\"review_rating\":10,\"review_date\":\"2009-07-29T03:46:00Z\",\"review_author\":\"Sally L\",\"helpful_count\":null,\"unhelpful_count\":null,\"type\":\"user_review\",\"source\":\"INSIDERPAGES\",\"reference_id\":null,\"source_id\":\"17\",\"attribution_logo\":\"http://www.insiderpages.com/images/ip_logo_88x33.jpg\",\"attribution_text\":\"Insider Pages\",\"attribution_url\":\"http://www.insiderpages.com/\",\"listing_id\":5168887,\"business_name\":\"Zingerman's Delicatessen\",\"impression_id\":\"000b0000037b007f1e8fea44feb3bc59e6e4421661\",\"review_author_url\":\"http://my.citysearch.com/members/public/profile/Sally+L?i=000b0000037b007f1e8fea44feb3bc59e6e4421661\",\"review_url\":\"http://www.insiderpages.com/b/13313585094/zingermans-delicatessen-ann-arbor\",\"public_id\":\"zingermans-delicatessen-ann-arbor-2\"},{\"review_id\":\"ip_10218180545\",\"review_title\":\"Zingerman's\",\"review_text\":\"This is a famous deli in Ann Arbor, that is known for its fresh bread, and wonderful, large sandwiches.  Not only do they have sandwiches, but the deli has expanded to lots of items that you can purchase.  Don't get me wrong, the�\",\
//		  "pros\":null,\"cons\":null,\"review_rating\":6,\"review_date\":\"2005-09-29T13:53:00Z\",\"review_author\":\"Teresa F\",\"helpful_count\":null,\"unhelpful_count\":null,\"type\":\"user_review\",\"source\":\"INSIDERPAGES\",\"reference_id\":null,\"source_id\":\"17\",\"attribution_logo\":\"http://www.insiderpages.com/images/ip_logo_88x33.jpg\",\"attribution_text\":\"Insider Pages\",\"attribution_url\":\"http://www.insiderpages.com/\",\"listing_id\":5168887,\"business_name\":\"Zingerman's Delicatessen\",\"impression_id\":\"000b000003396ba6fe8c5d4584b12d722de557726d\",\"review_author_url\":\"http://my.citysearch.com/members/public/profile/Teresa+F?i=000b000003396ba6fe8c5d4584b12d722de557726d\",\"review_url\":\"http://www.insiderpages.com/b/13313585094/zingermans-delicatessen-ann-arbor\",\"public_id\":\"zingermans-delicatessen-ann-arbor-2\"},{\"review_id\":\"mism_125160\",\"review_title\":\"Zingerman&#39;s and some zing to my taste buds!\",\"review_text\":\"Zingerman&#39;s Deli has been an Ann Arbor classic and favorite for years and years, with many more to come....\",\"pros\":null,\"cons\":null,\"review_rating\":8,\"review_date\":\"2011-04-13T22:13:43Z\",\"review_author\":\"mmmfood1\",\"helpful_count\":null,\"unhelpful_count\":null,\"type\":\"user_review\",\"source\":\"Menuism\",\"reference_id\":null,\"source_id\":\"51\",\"attribution_logo\":\"http://1.static.menuism.com/images/logos/logo-transparent-88x31.png\",\"attribution_text\":\"Menuism\",\"attribution_url\":\"http://www.menuism.com/\",\"listing_id\":5168887,\"business_name\":\"Zingerman's Delicatessen\",\"impression_id\":\"000b0000032d61476a752d458a93f2e3c307bad808\",\"review_author_url\":\"http://my.citysearch.com/members/public/profile/mmmfood1?i=000b0000032d61476a752d458a93f2e3c307bad808\",\"review_url\":\"http://www.menuism.com/restaurants/zingermans-delicatessen-ann-arbor-189073#p125160\",\"public_id\":\"zingermans-delicatessen-ann-arbor-2\"}]}}"
//		  ; 
//		  String test_string = "{\"statuses\":[{\"metadata\":{\"iso_language_code\":\"en\",\"result_type\":\"recent\"},\"created_at\":\"Sat Apr 11 12:49:18 +0000 2015\",\"id\":586873658875883500,\"id_str\":\"586873658875883520\",\"text\":\"Foodie entrepreneur talks to students about success: Ari Weinzweig, co-owner of Zingerman�s Deli, spoke to Alp... http://t.co/DKF4VVHbx7\", \"source\":\"<a href=http://twitterfeed.com rel=nofollow>twitterfeed</a>\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweet_count\":0,\"favorite_count\":1,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[],\"urls\":[{\"url\":\"http://t.co/DKF4VVHbx7\",\"expanded_url\":\"http://bit.ly/1FwONxM\",\"display_url\":\"bit.ly/1FwONxM\",\"indices\":[114,136]}]},\"favorited\":false,\"retweeted\":false,\"possibly_sensitive\":false,\"lang\":\"en\"}],\"search_metadata\":{\"completed_in\":0.015,\"max_id\":586873658875883500,\"max_id_str\":\"586873658875883520\",\"next_results\":\"?max_id=586873658875883519&q=Zingerman%27s%20deli&count=1&include_entities=1\",\"query\":\"Zingerman%27s+deli\",\"refresh_url\":\"?since_id=586873658875883520&q=Zingerman%27s%20deli&include_entities=1\",\"count\":1,\"since_id\":0,\"since_id_str\":\"0\"}}";
//		  test_list.add(test_string);
//		  
//		  List<ResponseStruct> test_api_struct = new LinkedList<ResponseStruct>(); 
//		  ResponseStruct testresponse = new ResponseStruct(test_string, "zingerman's", "Twitter");
//		  test_api_struct.add(testresponse); 
//		  test.insertData(test_api_struct);		  
		  System.out.println(test.select("", "zingerman's", new LinkedList<String>()).length());
		  AccessData.closeConnection();
		  System.out.println("Success"); 

		
		return;
	}

	/*
	 * 
	 * Adds all the API's that have been implemented Called by this.init()
	 */
	private void initializeSources() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		
		System.out.println(folder_location);
		File api_folder = new File(folder_location);
		File[] listofFiles = api_folder.listFiles();

		for (File file : listofFiles) {
			String basename = FilenameUtils.getBaseName(file.getName());
			if (basename.startsWith("API")) {
				basename = basename.substring(3);
				sources.put(
						basename,
						Class.forName(this.getClass().getPackage().getName()
								+ "." + basename));
			}	
			else if(basename.equals("business")) {
				try {
					this.initializeBusiness();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void initializeBusiness() throws IOException {
		String config_file = folder_location + "business.txt";;
		File attributes = new File(config_file);
		BufferedReader read = new BufferedReader(new FileReader(attributes));
		String line = new String();
		while ((line = read.readLine()) != null) {
			line = line.replaceAll("\\s", "");
			business_attributes.add(line);
			//System.out.println("line length: " + line.length());
		}
		read.close();
	}

	/*
	 * 
	 * Initialize variables needed to create database and creates database Must
	 * be called before insertData() or select()
	 */
	@Override
	public void initializeDatabase(String host_i, String keyspace_name_i,
			String review_table_i, String inverted_table_i) {
		host = host_i;
		keyspace_name = keyspace_name_i;
		review_table = review_table_i;
		inverted_table = inverted_table_i;
		CreateReviewKeyspace init_session = new CreateReviewKeyspace(host,
				keyspace_name, review_table, inverted_table);
		init_session.init();
		current_session = init_session.connect();
		// current_session.execute("USE" + keyspace_name);
		API.initializeDatabase(host_i, keyspace_name_i, review_table_i,
				inverted_table_i);
	}

	/*
	 * Calls wrapper to add all API's that have been implemented And then calls
	 * wrapper so each API class will read and store the path for attributes
	 * 
	 * "Folder_location_i" is the path to the folder that contains the
	 * configuration files for each API all API configuration files should be in
	 * one folder
	 * 
	 * Must be called before insertData() or select()
	 */
	@Override
	public void init(String folder_location_i) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		folder_location = folder_location_i;
		try {
			this.initializeSources();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
		for (String key : sources.keySet()) {
			try {
				((API) sources.get(key).newInstance()).init(folder_location, business_attributes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
				// System.out.println("response: " + response);
				((API) api.newInstance()).insert(response);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String select(String search, String company_name,
			List<String> attributes) throws JSONException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {

		Statement statement;
		ResultSet results = null;
		JSONArray formatted_reviews = new JSONArray();
		
		if(attributes.size() == 0) {
			for(String attribute: business_attributes) {
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
			if (containsAll(jsonObj.getString("content"), each_word)) {

				formatted_reviews.put(jsonObj);
			}
		}
		JSONObject all_reviews = new JSONObject();
		all_reviews.put("reviews", formatted_reviews);
		return all_reviews.toString();
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

	/*
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

			// get reviews
			/*
			 * for (String review_id : review_ids_all) { statement =
			 * QueryBuilder.select() .from(keyspace_name, review_table)
			 * .where(QueryBuilder.eq("review_id", review_id))
			 * .and(QueryBuilder.eq("company_name", company_name)); results =
			 * current_session.execute(statement); for (Row row : results) {
			 * String api = row.getString("review_id"); api = api.substring(0,
			 * api.indexOf('_')); Class<?> api_type =
			 * Class.forName(this.getClass() .getPackage().getName() + "." +
			 * api); formatted_reviews.put(((API) (api_type.newInstance()))
			 * .formatReview(row, new LinkedList<String>())); } }
			 */
		}
		return formatted_reviews.toString();
	}
}
