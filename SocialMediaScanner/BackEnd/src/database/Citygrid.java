package database;

import org.json.JSONObject;

import com.jayway.jsonpath.JsonPath;


public class Citygrid extends API {

	@Override
	public String getContent(JSONObject currentReview) {

		try {
			
			return JsonPath.read(currentReview.toString(),
					path_map.get(this.getClass().getSimpleName()).get("content"));
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
							path_map.get(this.getClass().getSimpleName()).get("id"));
		}
		catch (NullPointerException e) {
			return null;
		}
	}

}