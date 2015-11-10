package database;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;

public class AccessReviewKeyspace {
		
		private Session current_session;
		private String host;
		private String keyspace_name;
		private String review_table;
		private String inverted_table;
		
		AccessReviewKeyspace() {
			host = "127.0.0.1";
			keyspace_name = "review_keyspace";
			review_table = "review_table2";
			inverted_table = "inverted_table";
		}
		
		AccessReviewKeyspace(String host_i, String keyspace_name_i, String review_table_i, 
				String inverted_table_i) {
			host = host_i;
			keyspace_name = keyspace_name_i;
			//can change to allow parameters
			review_table = review_table_i;
			inverted_table = inverted_table_i;
		}
		
//		~AccessReviewKeyspace() {
//			current_session.close(); //???
//		}
		
		public static void main(String[] args) throws JSONException {
//			String test = "{"review_id":"cg_477754981","review_title":"It's Good but You're Going To Pay an Arm and a Leg","review_text":"When we moved to Ann Arbor from New York all we heard about was Zingermans, Zingermans, Zingermans.  We went.  The bread was great and the food is good but it's unapologetically expensive and shouldn't be.  There is no excuse for the�","pros":"Good","cons":"Far too expensive","review_rating":6,"review_date":"2008-03-21T14:09:29Z","review_author":"creativelolita","helpful_count":0,"unhelpful_count":0,"type":"user_review","source":"CITYSEARCH","reference_id":"0","source_id":"131","attribution_logo":"http://images.citysearch.net/assets/imgdb2/cs_logo_88x31.png","attribution_text":"Citysearch","attribution_url":"http://national.citysearch.com/profile/5168884/ann_arbor_mi/zingerman_s_catering.html#%7B%22history%22:%7B%22reviewId%22:%22477754981","listing_id":5168884,"business_name":"Zingerman's Catering","impression_id":"000b00000330d19ff504cd4660b885ce5773da1382","review_author_url":"http://my.citysearch.com/members/public/profile/creativelolita?i=000b00000330d19ff504cd4660b885ce5773da1382","review_url":"http://www.citysearch.com/review/5168884?reviewId=477754981","public_id":"zingermans-catering-ann-arbor"}";
//			AccessReviewKeyspace create = new AccessReviewKeyspace("127.0.0.1", "review_keyspace", "review_table", "inverted_table");
//			create.init();
//			Session current_session = create.connect("127.0.0.1", "review_keyspace");
//			String test = "{\"results\":{\"query_id\":\"123\",\"uri\":\"http://api.citygridmedia.com/reviews/reviews/v2/search/where?format=json&page=1&rpp=3&what=zingerman%27s&histograms=false&where=Ann+Arbor&publisher=10000008938&region_type=circle\",\"first_hit\":1,\"last_hit\":3,\"total_hits\":66,\"page\":1,\"rpp\":3,\"did_you_mean\":null,\"regions\":[{\"type\":\"city\",\"name\":\"Ann Arbor, MI\",\"latitude\":42.275434999999995,\"longitude\":-83.731138999999999,\"default_radius\":5.3200}],\"histograms\":[],\"reviews\":[{\"review_id\":\"ip_10302154082\",\"review_title\":\"Zingerman's Delicatessen is great!\",\"review_text\":\"Zingerman's is notorious for its sandwiches and it lives up to the hype.  They variety and quality of the sandwiches is great.  There's also plenty of seating.\",\"pros\":null,\"cons\":null,\"review_rating\":10,\"review_date\":\"2009-07-29T03:46:00Z\",\"review_author\":\"Sally L\",\"helpful_count\":null,\"unhelpful_count\":null,\"type\":\"user_review\",\"source\":\"INSIDERPAGES\",\"reference_id\":null,\"source_id\":\"17\",\"attribution_logo\":\"http://www.insiderpages.com/images/ip_logo_88x33.jpg\",\"attribution_text\":\"Insider Pages\",\"attribution_url\":\"http://www.insiderpages.com/\",\"listing_id\":5168887,\"business_name\":\"Zingerman's Delicatessen\",\"impression_id\":\"000b0000037b007f1e8fea44feb3bc59e6e4421661\",\"review_author_url\":\"http://my.citysearch.com/members/public/profile/Sally+L?i=000b0000037b007f1e8fea44feb3bc59e6e4421661\",\"review_url\":\"http://www.insiderpages.com/b/13313585094/zingermans-delicatessen-ann-arbor\",\"public_id\":\"zingermans-delicatessen-ann-arbor-2\"},{\"review_id\":\"ip_10218180545\",\"review_title\":\"Zingerman's\",\"review_text\":\"This is a famous deli in Ann Arbor, that is known for its fresh bread, and wonderful, large sandwiches.  Not only do they have sandwiches, but the deli has expanded to lots of items that you can purchase.  Don't get me wrong, the匼",\"pros\":null,\"cons\":null,\"review_rating\":6,\"review_date\":\"2005-09-29T13:53:00Z\",\"review_author\":\"Teresa F\",\"helpful_count\":null,\"unhelpful_count\":null,\"type\":\"user_review\",\"source\":\"INSIDERPAGES\",\"reference_id\":null,\"source_id\":\"17\",\"attribution_logo\":\"http://www.insiderpages.com/images/ip_logo_88x33.jpg\",\"attribution_text\":\"Insider Pages\",\"attribution_url\":\"http://www.insiderpages.com/\",\"listing_id\":5168887,\"business_name\":\"Zingerman's Delicatessen\",\"impression_id\":\"000b000003396ba6fe8c5d4584b12d722de557726d\",\"review_author_url\":\"http://my.citysearch.com/members/public/profile/Teresa+F?i=000b000003396ba6fe8c5d4584b12d722de557726d\",\"review_url\":\"http://www.insiderpages.com/b/13313585094/zingermans-delicatessen-ann-arbor\",\"public_id\":\"zingermans-delicatessen-ann-arbor-2\"},{\"review_id\":\"mism_125160\",\"review_title\":\"Zingerman&#39;s and some zing to my taste buds!\",\"review_text\":\"Zingerman&#39;s Deli has been an Ann Arbor classic and favorite for years and years, with many more to come....\",\"pros\":null,\"cons\":null,\"review_rating\":8,\"review_date\":\"2011-04-13T22:13:43Z\",\"review_author\":\"mmmfood1\",\"helpful_count\":null,\"unhelpful_count\":null,\"type\":\"user_review\",\"source\":\"Menuism\",\"reference_id\":null,\"source_id\":\"51\",\"attribution_logo\":\"http://1.static.menuism.com/images/logos/logo-transparent-88x31.png\",\"attribution_text\":\"Menuism\",\"attribution_url\":\"http://www.menuism.com/\",\"listing_id\":5168887,\"business_name\":\"Zingerman's Delicatessen\",\"impression_id\":\"000b0000032d61476a752d458a93f2e3c307bad808\",\"review_author_url\":\"http://my.citysearch.com/members/public/profile/mmmfood1?i=000b0000032d61476a752d458a93f2e3c307bad808\",\"review_url\":\"http://www.menuism.com/restaurants/zingermans-delicatessen-ann-arbor-189073#p125160\",\"public_id\":\"zingermans-delicatessen-ann-arbor-2\"}]}}";
//			List<String> test_list = new LinkedList<String>();
//			test_list.add(test);
//			List<String> test_api_list = new LinkedList<String>();
//			test_api_list.add("citygrid");
//			AccessReviewKeyspace test_input = new AccessReviewKeyspace();
//			test_input.parseJSON(test_list, "Zingerman's", test_api_list);
//			String test_query = test_input.grabReviews("Zingerman's", "its fresh bread");
//			System.out.print(test_query + "\n");
			System.out.print("SUCCESS");	

		}
		
//		public void init() {
//			CreateReviewKeyspace setup = new CreateReviewKeyspace(host, keyspace_name, review_table, inverted_table);
//			setup.init();
//			current_session = setup.connect();
//		}
				
		private JSONArray getReviewArray(String entire_review, String api_site) throws JSONException {
			JSONObject response = new JSONObject(entire_review);
			if(api_site.equals("citygrid")) {
				JSONArray reviews;
				reviews = response.getJSONObject("results").getJSONArray("reviews");
				return reviews;
			}
			return new JSONArray();
		}
		
		private String getReviewText(JSONObject current_review, String api_site) throws JSONException {
			if(api_site.equals("citygrid")) {
				return current_review.getString("review_text");
			}
			return "";
		}
		
		private String getReviewID(JSONObject current_review, String api_site) throws JSONException {
			if(api_site.equals("citygrid")) {
				return current_review.getString("review_id");
			}
			return "";
		}
		
		private JSONObject convert_rating(JSONObject before, String api_name) throws NumberFormatException, JSONException {
			double convert;
			//System.out.println(before);
			if(api_name.equals("citygrid")) {
				convert = before.getDouble("review_rating");
				convert /= 10.0;
				before.put("review_rating", Double.toString(convert));
				//System.out.println(before);
				return before;
			}
			return new JSONObject();
		}
		
		public void parseJSON(List<String> responses, String company_name, List<String> api_names) throws JSONException, UnsupportedEncodingException {
			int position = 0;
			String api_name;
			for(String response: responses) {
				api_name = api_names.get(position++);
				JSONArray reviews = null;
				reviews = this.getReviewArray(response, api_name);
				//System.out.print(reviews);
				JSONObject current_review;
				String current_review_text, full_review;
				String current_review_id;
				
				// Insert one record into the users table
		        PreparedStatement insert = current_session.prepare("INSERT INTO review_table2" + 
		        		"(review_id, company_name, json)" + "VALUES (?,?,?) IF NOT EXISTS;");
		        
		        //insert reviews 1 by 1
		        for(int i = 0; i < reviews.length(); ++i) {	
					current_review = reviews.getJSONObject(i);
					current_review = this.convert_rating(current_review, api_name);
					current_review_text = this.getReviewText(current_review, api_name);
					//current_review_text = URLDecoder.decode(current_review_text, "UTF-8");
					current_review.put("review_text", current_review_text);
					current_review_id = api_name + "_" + this.getReviewID(current_review, api_name);
					this.updateReview(current_review_text, current_review_id, "inverted_table");
					full_review = current_review.toString();
					this.insertReview(current_review_id, company_name, full_review, insert);
				}
			}
		}
		
		private void updateReview(String review_text, String review_id, String inverted_table_name) {
			
			review_text = review_text.replaceAll("[^a-zA-Z\\s-.]", "");
			review_text = review_text.replaceAll("[-'.]", " ");
			review_text = review_text.toLowerCase();
			String each_word[] = review_text.split("[ ]+");
//			String test;
			for(String word: each_word) {
//				test = "UPDATE " + inverted_table_name +
//						" SET review_id_set = review_id_set + {'" + review_id + "'} WHERE review_word = '" + word + "';";
//				SimpleStatement test1 = new SimpleStatement(test);
//				current_session.execute(test1);
				current_session.execute("UPDATE " + inverted_table_name +
						" SET review_id_set = review_id_set + {'" + review_id + "'} WHERE review_word = '" + word + "';");
			}		
		}
				
		private void insertReview(String review_id, String company_name, String full_review,
				PreparedStatement statement) {
			
	        BoundStatement boundStatement = new BoundStatement(statement);
	        current_session.execute(boundStatement.bind(review_id, company_name, full_review));
			
		}
		
//		public JSONArray grabReviews(Session session, String company_name, String search, List<String> attributes) throws JSONException {
		public String grabReviews(String company_name, String search) throws JSONException {
			
			Statement statement; ResultSet results = null; JSONArray formatted_reviews = new JSONArray();
			Set<String> review_ids_per_word; Set<String> review_ids_all = new HashSet<String>();
			
			search = search.replaceAll("[^a-zA-Z\\s-.]", "");
			search = search.replaceAll("[-|'.]", " ");
			search = search.toLowerCase();
			String[] each_word = search.split("[ ]+");
			
			//iterate through all words in search
			for(String word: each_word) {
				statement = QueryBuilder.select().all().from("review_keyspace", "inverted_table")
					.where(QueryBuilder.eq("review_word", word));
				results = current_session.execute(statement);
				//select review_ids for word from inverted index
				for ( Row row : results ) {
					review_ids_per_word = row.getSet("review_id_set", String.class);
					//add each review to the list 
					for(String review_id: review_ids_per_word) {
						review_ids_all.add(review_id);
					}
				}
			}
			//no search keywords
			if(search.isEmpty()) {
				statement = QueryBuilder.select().all().from("review_keyspace", "review_table2")
				        .where(QueryBuilder.eq("company_name", company_name));
				results = current_session.execute(statement);
				for(Row row: results) {
					formatted_reviews.put(this.formatReview(row));
				}
			}
			else {
				//get reviews
				for(String review_id: review_ids_all) {
					statement = QueryBuilder.select().from("review_keyspace", "review_table2")
							.where(QueryBuilder.eq("review_id", review_id))
							.and(QueryBuilder.eq("company_name", company_name));
					results = current_session.execute(statement);
					for(Row row: results) {
						formatted_reviews.put(this.formatReview(row));
					}
				}
			}
			return formatted_reviews.toString();
		}

//		public JSONObject formatReview(Row current_row, String search, List<String> attributes) throws JSONException {
		private JSONObject formatReview(Row current_row) throws JSONException {
			System.out.println(current_row);
			String api = current_row.getString("review_id");
			api = api.substring(0, api.indexOf('_'));
			JSONObject convert_from_row = new JSONObject(current_row.getString("json"));
			JSONObject json_api_format = new JSONObject();

			if(api.equals("citygrid")) {
				json_api_format.put("source", api);
				json_api_format.put("title", convert_from_row.get("review_title"));
				json_api_format.put("content", convert_from_row.get("review_text"));
				//need to change so not always neutral
				json_api_format.put("sentiment", "neutral");
				json_api_format.put("rating", convert_from_row.get("review_rating"));
				return json_api_format;
			}
			return new JSONObject();		
		}
}
