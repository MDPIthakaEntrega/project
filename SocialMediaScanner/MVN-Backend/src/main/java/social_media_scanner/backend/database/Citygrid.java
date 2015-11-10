package social_media_scanner.backend.database;

import org.json.JSONObject;

import com.jayway.jsonpath.JsonPath;

import social_media_scanner.backend.utility.Parser.APIParser;

public class Citygrid extends API {

	@Override
	public String getContent(JSONObject currentReview) {

		try {
			
			return JsonPath.read(currentReview.toString(),
					APIParser.getAPIMap(this.getClass().getSimpleName()).get("content"));
		}
		catch (NullPointerException e) {
			
			return null;
		}
	}

	@Override
	public String getId(JSONObject currentReview) {

		try {
			return this.getClass().getSimpleName() + "_"
					+ JsonPath.read(currentReview.toString(), 
							APIParser.getAPIMap(this.getClass().getSimpleName()).get("id"));
		}
		catch (NullPointerException e) {
			return null;
		}
	}

}