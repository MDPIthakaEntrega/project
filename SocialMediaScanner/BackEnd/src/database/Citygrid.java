package database;


import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import service.Server;
import com.jayway.jsonpath.JsonPath;

import service.ResponseStruct;

public class Citygrid extends API {

	static private Map<String, String> path = new HashMap<String, String>();
	static private double rating_conversion = 10.0;
	
	Citygrid() { }

	
	public void init(String folder_location_i) throws IOException{
		String config_file = folder_location_i;
		config_file += (this.getClass().getSimpleName() + ".txt");
		File mapping = new File(config_file);
		BufferedReader read = new BufferedReader(new FileReader(mapping));
		String line = new String();
		while((line = read.readLine()) !=  null) {
			String[] key_val = line.split("\\s+");
			path.put(key_val[0], key_val[1]);
		}
		read.close();
	}
	
	@Override
	public void insert(ResponseStruct responses) throws JSONException, ParseException {
				
		JSONArray reviews = new JSONArray(JsonPath.read(new JSONParser().
				parse(responses.getResponse()), path.get("reviews")).toString());
		JSONObject current_review;
		String current_review_text, full_review;
		String current_review_id;
		
		// Insert one record into the users table
        PreparedStatement insert = current_session.prepare("INSERT INTO " +  review_table + 
        		" (review_id, company_name, json)" + "VALUES (?,?,?) IF NOT EXISTS;");

        //insert reviews 1 by 1
        for(int i = 0; i < reviews.length(); ++i) {	
			
        	current_review = reviews.getJSONObject(i);
			//current_review = this.convert_rating(current_review);
			current_review_text = JsonPath.read(current_review.toString(), path.get("content"));
			current_review_id = this.getClass().getSimpleName() + "_" + JsonPath.read(current_review.toString(), path.get("id"));
			current_review.put("source", this.getClass().getSimpleName());
			current_review.put("sentiment", Server.sentimentAnalyze(current_review_text));
			
			//update path to show where source and sentiment were placed in JSON-formatted review
			path.put("source", "$.source");
			path.put("sentiment", "$.sentiment");
			
			//insert review_text into column in database or do something with SOLR
			full_review = current_review.toString();
			this.insertReview(current_review_id, responses.getCompanyName(), full_review, insert);
		}	
	}
	
	private JSONObject convert_rating(JSONObject before) throws NumberFormatException, JSONException, ParseException {
		//assumes rating is a double in the JSONobject
		if(path.get("rating") != null) {
			double convert = 0;
			Object test = new JSONParser().parse(before.toString());
			convert = ((Long)JsonPath.read(test, path.get("rating"))).doubleValue();
			convert /= rating_conversion;
			before.put("rating", convert);
		}
		return before;
	}
	
	private void insertReview(String review_id, String company_name, String full_review,
			PreparedStatement statement) {
		
        BoundStatement boundStatement = new BoundStatement(statement);
        current_session.execute(boundStatement.bind(review_id, company_name, full_review));
		
	}
	
	@Override
	public JSONObject formatReview(Row current_row, List<String> attributes) throws JSONException {
		System.out.println("inside format review");
		String api = current_row.getString("review_id");
		api = api.substring(0, api.indexOf('_'));
		JSONObject json_api_format = new JSONObject();
		String json_as_string = current_row.getString("json");
		if(attributes.size() == 0) {
			attributes.add("source");
			attributes.add("title");
			attributes.add("content");
			attributes.add("sentiment");
			attributes.add("rating");
		}
		
		for(String attribute: attributes) {
			try{
				if(attribute.equals("rating")) {
					// Perform rating conversion
					json_api_format.put(attribute, (int)JsonPath.read(json_as_string, path.get(attribute))/rating_conversion);
				}
				else {
					json_api_format.put(attribute, JsonPath.read(json_as_string, path.get(attribute)));
				}
			}
			catch (com.jayway.jsonpath.InvalidPathException e) {
				json_api_format.put(attribute, "");
			}
		}
		return json_api_format;		
	}

	@Override
	public void insert(String response, String company_name)
			throws JSONException, ParseException {
		// TODO Auto-generated method stub
		
	}
}