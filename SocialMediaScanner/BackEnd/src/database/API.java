package database;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import service.ResponseStruct;
import service.SentimentStruct;
import service.Server;
import utility.Parser.APIParser;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Row;
import com.jayway.jsonpath.JsonPath;

public abstract class API extends AccessData {

	public abstract String getContent(JSONObject currentReview);
	public abstract String getId(JSONObject currentReview);
	
	public void insert(ResponseStruct responses) throws JSONException,
	ParseException {
		
		JSONArray reviews = new JSONArray(JsonPath.read(
				new JSONParser().parse(responses.getResponse()),
				APIParser.getAPIMap(this.getClass().getSimpleName()).get("reviews")).toString());
		
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
				
				try {
					
					current_review_sentiment = Server.sentimentAnalyze(current_review_text);
					if(current_review_sentiment != null) {
						current_review.put("sentiment_score", current_review_sentiment.getScore());
						current_review.put("sentiment_feeling", current_review_sentiment.getFeeling());
					}
					else {
						current_review.put("sentiment_score", 0);
						current_review.put("sentiment_feeling", "neutral");
					}
				}
				catch(Exception e) {
					
					System.out.println("analysis error");
					e.printStackTrace();
				}
				
			}
			
			// TODO insert review_text into column in database or do something with SOLR

			// TODO what should be case if id is null
			if(current_review_id != null) {
				this.insertReview(current_review_id, responses.getCompanyName().toLowerCase(),
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

				Object val = JsonPath.read(json_as_string, APIParser.getAPIMap(this.getClass().getSimpleName()).get(attribute));
				if (val == null) {

					json_api_format.put(attribute, JSONObject.NULL);
				} 
				else {

					if (attribute.equals("rating")) {
						// Perform rating conversion
						json_api_format.put(attribute, (int) val
								/ APIParser.getConversionMap(this.getClass().getSimpleName()));
					} else {
						
						json_api_format.put(attribute, val);
					}
				}
			} catch (com.jayway.jsonpath.InvalidPathException e) {
				// If attribute is not in the review, then we just put a blank value in
				json_api_format.put(attribute, "");
			}
		}

		return json_api_format;
	}

}