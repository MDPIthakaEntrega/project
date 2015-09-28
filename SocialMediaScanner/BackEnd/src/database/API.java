package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import service.ResponseStruct;
import service.SentimentStruct;
import service.Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.jayway.jsonpath.JsonPath;

public abstract class API extends AccessData {

	static protected Map<String, Map<String, String>> path_map = new HashMap<String, Map<String, String>>();
	static private Map<String, Double> conversion_map = new HashMap<String, Double>();

	public static void init(String folder_location_i, List<String> attributes, String APIName) throws IOException {
	
		path_map.put(APIName, new HashMap<String, String>());
		
		String config_file = folder_location_i;
		config_file += ("API" + APIName + ".txt");
		File mapping = new File(config_file);
		BufferedReader read = new BufferedReader(new FileReader(mapping));
		String line = new String();
		while ((line = read.readLine()) != null) {
			String[] key_val = line.split("\\s+");
			path_map.get(APIName).put(key_val[0], key_val[1]);
		}
		API.addMissingAttributes(attributes, APIName);
		read.close();
		
		conversion_map.put(APIName, 10.0);
	}
	
	private static void addMissingAttributes(List<String> attributes, String APIName) {
		System.out.println("AddMissingAttributes called for " + APIName);
		for(String attribute: attributes) {
			
			if(path_map.get(APIName).get(attribute) == null) {
				System.out.println("attribute: " + attribute + " is null");
				path_map.get(APIName).put(attribute, "$." + attribute);
			}
		}
	}
	
	public abstract String getContent(JSONObject currentReview);
	public abstract String getId(JSONObject currentReview);
	
	public void insert(ResponseStruct responses) throws JSONException,
	ParseException {
		
		System.out.println("reviews_path: " +  path_map.get(this.getClass().getSimpleName()).get("reviews"));
		JSONArray reviews = new JSONArray(JsonPath.read(
				new JSONParser().parse(responses.getResponse()),
				path_map.get(this.getClass().getSimpleName()).get("reviews")).toString());
		
		JSONObject current_review;
		String current_review_text;
		String current_review_id;
		SentimentStruct current_review_sentiment;
		// insert reviews 1 by 1
		for (int i = 0; i < reviews.length(); ++i) {
			current_review = reviews.getJSONObject(i);
			
			current_review_text = this.getContent(current_review);
			
			current_review_id = this.getId(current_review);
			
			current_review.put("source", this.getClass().getSimpleName());
			
			if(current_review_text != null) {
				
				current_review_sentiment = Server.sentimentAnalyze(current_review_text);
				current_review.put("sentiment_score", current_review_sentiment.getScore());
				current_review.put("sentiment_feeling", current_review_sentiment.getFeeling());
			}
			
			// TODO insert review_text into column in database or do something with SOLR

			// TODO what should be case if id is null
			if(current_review_id != null) {
				this.insertReview(current_review_id, responses.getCompanyName(),
						current_review.toString());
			}
			
		}
	}
	
	private void insertReview(String review_id, String company_name,
			String full_review) {
		
		BoundStatement boundStatement = new BoundStatement(preparedStmt);
		current_session.execute(boundStatement.bind(review_id, company_name,
				full_review));

	}
	
	public JSONObject formatReview(Row current_row, List<String> attributes)
			throws JSONException {
		JSONObject json_api_format = new JSONObject();
		String json_as_string = current_row.getString("json");
		for (String attribute : attributes) {
			try {

				Object val = JsonPath.read(json_as_string, path_map.get(this.getClass().getSimpleName()).get(attribute));
				if (val == null) {

					json_api_format.put(attribute, JSONObject.NULL);
				} else {

					if (attribute.equals("rating")) {
						// Perform rating conversion
						json_api_format.put(attribute, (int) val
								/ conversion_map.get(this.getClass().getSimpleName()));
					} else {
						json_api_format.put(attribute, val);
					}
				}
			} catch (com.jayway.jsonpath.InvalidPathException e) {
				json_api_format.put(attribute, "");
			}
		}

		return json_api_format;
	}

}