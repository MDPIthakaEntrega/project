package database;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
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

	private Map<String, String> pathMapping = new HashMap<String, String>();
	private final double RATING_CONVERSION = 10.0;
	
	Citygrid() { }

	
	public void init(String folder_location_i) throws IOException{
		String config_file = folder_location_i;
		config_file += (this.getClass().getSimpleName() + ".txt");
		File mapping = new File(config_file);
		BufferedReader read = new BufferedReader(new FileReader(mapping));
		String line = new String();
		while((line = read.readLine()) !=  null) {
			String[] key_val = line.split("\\s+");
			pathMapping.put(key_val[0], key_val[1]);
		}
		//update path to show where source and sentiment were placed in JSON-formatted review
		pathMapping.put("source", "$.source");
		pathMapping.put("sentiment", "$.sentiment");
		read.close();
	}
	
	@Override
	public void insert(ResponseStruct responses) throws JSONException, ParseException {
		
		JSONArray reviews = new JSONArray(JsonPath.read(new JSONParser().
				parse(responses.getResponse()), pathMapping.get("reviews")).toString());
		System.out.println(reviews.toString());
		System.out.println(reviews.length());
		
		JSONObject current_review;
		String current_review_text, full_review;
		String current_review_id;
		
		// Insert one record into the users table
		PreparedStatement insert = null;
		try{
        insert = current_session.prepare("INSERT INTO " +  review_table + 
        		" (review_id, company_name, json)" + "VALUES (?,?,?) IF NOT EXISTS;");
		}
		catch(Exception e) {
			
			e.printStackTrace();
		}
        
        //insert reviews 1 by 1
        for(int i = 0; i < reviews.length(); ++i) {	
        	current_review = reviews.getJSONObject(i);
			//current_review = this.convert_rating(current_review);
			current_review_text = JsonPath.read(current_review.toString(), pathMapping.get("content"));
			current_review_id = this.getClass().getSimpleName() + "_" + JsonPath.read(current_review.toString(), pathMapping.get("id"));
			current_review.put("source", this.getClass().getSimpleName());
			current_review.put("sentiment", Server.sentimentAnalyze(current_review_text));
			
			//insert review_text into column in database or do something with SOLR
			full_review = current_review.toString();
			
			this.insertReview(current_review_id, responses.getCompanyName(), full_review, insert);
		}	
	}
	
	private JSONObject convert_rating(JSONObject before) throws NumberFormatException, JSONException, ParseException {
		//assumes rating is a double in the JSONobject
		if(pathMapping.get("rating") != null) {
			double convert = 0;
			Object test = new JSONParser().parse(before.toString());
			convert = ((Long)JsonPath.read(test, pathMapping.get("rating"))).doubleValue();
			convert /= RATING_CONVERSION;
			before.put("rating", convert);
		}
		return before;
	}
	
	private void insertReview(String review_id, String company_name, String full_review,
			PreparedStatement statement) {
		
        BoundStatement boundStatement = new BoundStatement(statement);
        
        System.out.println(boundStatement.toString());
        current_session.execute(boundStatement.bind(review_id, company_name, full_review));
		
	}
	
	@Override
	public JSONObject formatReview(Row current_row, List<String> attributes) throws JSONException {
		
		attributes = new LinkedList<String>();
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
				
				//System.out.println("attribute: " + attribute + "|" + path.get(attribute) + " json path result: " + JsonPath.read(json_as_string, path.get(attribute)));
				Object val = JsonPath.read(json_as_string, pathMapping.get(attribute));
				if (val.equals(null)) {
					
					json_api_format.put(attribute, JSONObject.NULL);
				}
				else {
					
					if (attribute.equalsIgnoreCase("rating")) {
						
						json_api_format.put(attribute, ((int)val) / RATING_CONVERSION);
					}
					else {
						
						json_api_format.put(attribute, (String)val);
					}
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