package social_media_scanner.backend.grabber;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import net.minidev.json.JSONArray;
import social_media_scanner.backend.service.CompanyStruct;
import social_media_scanner.backend.service.ResponseStruct;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Twitter data source grabber, which must extends DataGrabberGeneric and
 * implement pullData method.
 * 
 * @author liaoyuke
 *
 */
public class GrabberTwitter extends DataGrabberGeneric {

    private final String CONSUMER_KEY = "WsScSSlqyulJC5l35TGXlO26f";
    private final String CONSUMER_SECRET = "gGJgl4GFNmKypvnYmlEi4JjUuWcj8Sa78991W0LcWO2nLHR1Uu";
    private final String ACCESS_TOKEN = "2600995789-9gU6dlfCC1kpVHGOcni0UqiwqP7cpukrUgdYkfy";
    private final String ACCESS_SECRET = "LV2lfjawudzSJgT0MgmTIspiP4MHlJ1QILSZ885Dp7srA";
    private final int MAX_RRP = 100;

    public static void main(String args[]) {
    	
    	DataGrabberGeneric test = new GrabberTwitter();
    	((GrabberTwitter) test).testTry();
    }
    
    @SuppressWarnings("unused")
    private void testTry() {
	Twitter twitter = new TwitterFactory().getInstance();
	AccessToken accessToken = new AccessToken(ACCESS_TOKEN, ACCESS_SECRET);
	twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
	twitter.setOAuthAccessToken(accessToken);
	try {
	    Query query = new Query("Zingermans");
	    query.setCount(MAX_RRP);
	    while (query != null) {
		QueryResult result = twitter.search(query);
		System.out.println("RESULT: " + result.toString());
		System.out.println(result.getTweets().size());
		for (Status status : result.getTweets()) {
		    System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
		    System.out.println(status.getCreatedAt());
		    System.out.println(status.getId());
		    System.out.println(status.getRetweetCount());
		}
		query = result.nextQuery();
	    }
	} catch (Exception e) {
	    // TODO: handle exception
	    e.printStackTrace();
	}
    }


	@Override
	public List<ResponseStruct> pullData(CompanyStruct companyName) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
    	System.out.println("PullData Twitter");
		Twitter twitter = new TwitterFactory().getInstance();
		AccessToken accessToken = new AccessToken(ACCESS_TOKEN, ACCESS_SECRET);
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(accessToken);
	List<ResponseStruct> listResponseStruct = new ArrayList<ResponseStruct>();

	try {
		String nameToPull = companyName.getTwitterName();
		if(nameToPull.length() > 0) {
			Query query = new Query(nameToPull);
			query.setCount(MAX_RRP);
			int numReviews = 0;
			while (query != null) {

				if(numReviews > 100) {
					break;
				}
				JSONArray jsonArray = new JSONArray();
				QueryResult result = twitter.search(query);

				for (Status status : result.getTweets()) {
					numReviews++;
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("text", status.getText());
					jsonObj.put("id_str", status.getId());
					jsonObj.put("rating", status.getFavoriteCount());
					jsonObj.put("date", status.getCreatedAt());
					jsonArray.add(jsonObj);
				}
				query = result.nextQuery();
				JSONObject jsonWrapper = new JSONObject();
				jsonWrapper.put("statuses", jsonArray);
				listResponseStruct
						.add(new ResponseStruct(jsonWrapper.toString(), companyName.getCompanyName(), toString()));
			}
		}

	} catch (Exception e) {
	    e.printStackTrace();
	}
	System.out.println("done with twitter");
	return listResponseStruct;
    }

    @Override
    public String toString() {

	return "Twitter";
    }
}