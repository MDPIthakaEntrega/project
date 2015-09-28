package database;

import org.json.JSONObject;

import com.jayway.jsonpath.JsonPath;

public class Yelp extends API {

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
			String [] split = this.getContent(currentReview).split("\\s+");
			
			String id = this.getClass().getSimpleName() + "_" + split[0];
			
			if(split.length > 1) {
				id += split[1];
			}
			if(split.length > 2) {
				id += split[2];
			}
			return id;
		}
		catch (Exception e) {
			
			return null;
		}
	}
}

