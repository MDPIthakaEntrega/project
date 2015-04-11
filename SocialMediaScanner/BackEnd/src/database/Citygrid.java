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

import service.ResponseStruct;
import service.Server;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Row;
import com.jayway.jsonpath.JsonPath;

public class Citygrid extends API {

//	static private Map<String, String> path = new HashMap<String, String>();
//	static private double rating_conversion = 10.0;
//
//	Citygrid() {
//	}
//
//	public void init(String folder_location_i, List<String> attributes) throws IOException {
//		String config_file = folder_location_i;
//		config_file += ("API" + this.getClass().getSimpleName() + ".txt");
//		File mapping = new File(config_file);
//		BufferedReader read = new BufferedReader(new FileReader(mapping));
//		String line = new String();
//		while ((line = read.readLine()) != null) {
//			String[] key_val = line.split("\\s+");
//			path.put(key_val[0], key_val[1]);
//		}
//		this.addMissingAttributes(attributes);
//		read.close();
//	}
//	
//	private void addMissingAttributes(List<String> attributes) {
//		for(String attribute: attributes) {
//			if(path.get(attribute) == null) {
//				path.put(attribute, "$." + attribute);
//			}
//		}
//	}
//
//	@Override
//	public void insert(ResponseStruct responses) throws JSONException,
//			ParseException {
//
//		JSONArray reviews = new JSONArray(JsonPath.read(
//				new JSONParser().parse(responses.getResponse()),
//				path.get("reviews")).toString());
//
//		JSONObject current_review;
//		String current_review_text, full_review;
//		String current_review_id;
//
//		// insert reviews 1 by 1
//		for (int i = 0; i < reviews.length(); ++i) {
//			current_review = reviews.getJSONObject(i);
//			current_review_text = JsonPath.read(current_review.toString(),
//					path.get("content"));
//			current_review_id = this.getClass().getSimpleName() + "_"
//					+ JsonPath.read(current_review.toString(), path.get("id"));
//			current_review.put("source", this.getClass().getSimpleName());
//			current_review.put("sentiment",
//					Server.sentimentAnalyze(current_review_text));
//
//			// insert review_text into column in database or do something with
//			// SOLR
//			full_review = current_review.toString();
//
//			this.insertReview(current_review_id, responses.getCompanyName(),
//					full_review);
//		}
//	}
//
//	private void insertReview(String review_id, String company_name,
//			String full_review) {
//
//		BoundStatement boundStatement = new BoundStatement(preparedStmt);
//		current_session.execute(boundStatement.bind(review_id, company_name,
//				full_review));
//
//	}
//
//	@Override
//	public JSONObject formatReview(Row current_row, List<String> attributes)
//			throws JSONException {
//		JSONObject json_api_format = new JSONObject();
//		String json_as_string = current_row.getString("json");
//		for (String attribute : attributes) {
//			try {
//
//				Object val = JsonPath.read(json_as_string, path.get(attribute));
//				if (val == null) {
//
//					json_api_format.put(attribute, JSONObject.NULL);
//				} else {
//
//					if (attribute.equals("rating")) {
//						// Perform rating conversion
//						json_api_format.put(attribute, (int) val
//								/ rating_conversion);
//					} else {
//						json_api_format.put(attribute, val);
//					}
//				}
//			} catch (com.jayway.jsonpath.InvalidPathException e) {
//				json_api_format.put(attribute, "");
//			}
//		}
//
//		return json_api_format;
//	}
//
//	@Override
//	public void insert(String response, String company_name)
//			throws JSONException, ParseException {
//		// TODO Auto-generated method stub
//	}
}