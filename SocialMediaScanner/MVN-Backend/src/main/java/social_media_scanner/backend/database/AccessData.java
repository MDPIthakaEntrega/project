/**
 * 
 * Charlie Scott
 * Implementation for "Data" interface, allows insertion into database, and selection of
 * reviews for database
 * 
 */

package social_media_scanner.backend.database;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Truncate;

import social_media_scanner.backend.service.ResponseStruct;
import social_media_scanner.backend.service.SearchStruct;
import social_media_scanner.backend.utility.Parser;

public class AccessData implements Data {

	private static Cluster cluster;
	protected static Session current_session;
	protected static PreparedStatement preparedStmt; 
	protected static String host;
	protected static String keyspace_name;
	protected static String review_table;
	protected static String inverted_table;
	private static boolean initialized = false;
	
	public static void main(String[] args) { }
	
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
	
	public static void truncateTables() {
		
		truncateReviewTable();
		truncateInvertedTable();
	}
	
	public static void truncateReviewTable() {
		
		Truncate truncate = QueryBuilder.truncate(review_table);
		current_session.execute(truncate);
	}
	
	public static void truncateInvertedTable() {
		
		Truncate truncate = QueryBuilder.truncate(inverted_table);
		current_session.execute(truncate);
	}
	
	public static void close() {

		current_session.close();
		cluster.close();
	}
	
	private static void connect() {
		

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
	 */
	public static Session getConnection() {
		
		return current_session;
	}

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

	@Override
	public String select(SearchStruct search, List<String> attributes) throws JSONException,
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

        String keyword = search.getKeyword();
		keyword = keyword.replaceAll("[^a-zA-Z\\s.-]", "");
		keyword = keyword.replaceAll("[-|'.]", " ");
		keyword = keyword.toLowerCase();
		String[] each_word = keyword.split("[ ]+");
		try {

			statement = QueryBuilder.select().all()
					.from(keyspace_name, review_table)
					.where(QueryBuilder.eq("company_name", search.getCompanyName()));
			results = current_session.execute(statement);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (results != null) {
			for (Row row : results) {
                String api = row.getString("review_id");
                api = api.substring(0, api.indexOf('_'));
                Class<?> api_type = Class.forName(this.getClass().getPackage()
                        .getName()
                        + "." + api);
                JSONObject jsonObj = ((API) (api_type.newInstance())).formatReview(
                        row, attributes);

                if (containsAll(jsonObj.getString("content"), each_word) &&
                        inAPIList(search.getAPIs(), jsonObj.getString("source"))) {

                    formatted_reviews.put(jsonObj);
                }
            }
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
	public String select(SearchStruct search)
			throws JSONException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {

		JSONArray formatted_reviews = new JSONArray();

		String keyword = search.getKeyword();
		keyword = keyword.replaceAll("[^a-zA-Z\\s.-]", "");
		keyword = keyword.replaceAll("[-|'.]", " ");
		keyword = keyword.toLowerCase();
		String[] each_word = keyword.split("[ ]+");
		if (keyword == "") {
			List<Row> listRow = getAllRows(search.getCompanyName());
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
			List<Row> listRow = getAllRows(search.getCompanyName());
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
