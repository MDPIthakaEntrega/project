package social_media_scanner.backend.database;

import java.util.List;

import org.json.JSONObject;

import com.jayway.jsonpath.JsonPath;

import social_media_scanner.backend.utility.Parser.APIParser;

public class ImportMagicYelp extends API {

    @Override
    public String getContent(JSONObject currentReview) {

	try {
	    try {

		List<String> descriptions = JsonPath.read(currentReview.toString(),
			APIParser.getAPIMap(this.getClass().getSimpleName()).get("content"));
		String result = "";
		for (String description : descriptions) {

		    result += description;
		}

		return result;

	    } catch (Exception e) {
		return JsonPath.read(currentReview.toString(),
			APIParser.getAPIMap(this.getClass().getSimpleName()).get("content"));
	    }
	} catch (NullPointerException e) {

	    return null;
	} catch (Exception e) {

	    return null;
	}
    }

    @Override
    public String getId(JSONObject currentReview) {

	try {
	    String[] split = this.getContent(currentReview).split("\\s+");

	    String id = this.getClass().getSimpleName() + "_" + split[0];

	    if (split.length > 1) {
		id += split[1];
	    }
	    if (split.length > 2) {
		id += split[2];
	    }
	    return id;
	} catch (Exception e) {

	    return null;
	}
    }
}
