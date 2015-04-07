package database;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Configuration;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
//import com.jayway.restassured.path.json.JsonPath;
import com.jayway.jsonpath.JsonPath;


public class Citygrid extends API {

	static private Map<String, String> path = new HashMap<String, String>();
	static private double rating_conversion = 10.0;
	
	Citygrid() { }
	
	public static void main(String[] args) throws IOException {
		Citygrid test = new Citygrid();
		//test.init();
	}
	
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
	public void insert(List<ResponseStruct> responses) throws JSONException, ParseException {
		System.out.println(response);
		JSONArray reviews = new JSONArray(JsonPath.read(new JSONParser().parse(response), path.get("reviews")).toString());
		//reviews = this.getReviewArray(response);
		//System.out.print(reviews);
		JSONObject current_review;
		String current_review_text, full_review;
		String current_review_id;
		
		// Insert one record into the users table
        PreparedStatement insert = current_session.prepare("INSERT INTO " +  review_table + 
        		" (review_id, company_name, json)" + "VALUES (?,?,?) IF NOT EXISTS;");
        
        //insert reviews 1 by 1
        for(int i = 0; i < reviews.length(); ++i) {	
			current_review = reviews.getJSONObject(i);
			current_review_text = JsonPath.read(current_review.toString(), path.get("content"));
			current_review.put("source", this.getClass().getSimpleName());
			path.put("source", "$.source");
			//current_review.put("sentiment", *getreviewsentiment(current_review_text));
			current_review.put("sentiment", "neutral");
			path.put("sentiment", "$.sentiment");
			//current_review = this.convert_rating(current_review);
			//insert review_text into column in database??
			//current_review.put(path.get("text"), current_review_text);
			current_review_id = this.getClass().getSimpleName() + "_" + JsonPath.read(current_review.toString(), path.get("id"));
			//Do something with SOLR
			full_review = current_review.toString();
			this.insertReview(current_review_id, company_name, full_review, insert);
		}	
	}
	
	private JSONObject convert_rating(JSONObject before) throws NumberFormatException, JSONException, ParseException {
		//assumes rating is a double in the JSONobject
		if(path.get("rating") != null) {
			double convert = 0;
			System.out.println(before);
			//if(path.get("rating").equals("$.review_rating")){
			//	System.out.println("HERE");
			//}
			Object test = new JSONParser().parse(before.toString());
			//Integer test = JsonPath.read(before.toString(), path.get("rating"));
			System.out.println("rating path: "+ path.get("rating"));
			System.out.println("object: " + test);
			convert = ((Long)JsonPath.read(test, path.get("rating"))).doubleValue();

			//System.out.println(convert);
			//convert = test.doubleValue();
			convert /= rating_conversion;
			before.put("rating", convert);
			//path.put("rating", "rating");
			//System.out.println(before);
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
		System.out.println(current_row);
		String api = current_row.getString("review_id");
		api = api.substring(0, api.indexOf('_'));
		JSONObject json_api_format = new JSONObject();
		String json_as_string = current_row.getString("json");

		for(String attribute: attributes) {
			try{
				if(attribute.equals("rating")) {
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
}
