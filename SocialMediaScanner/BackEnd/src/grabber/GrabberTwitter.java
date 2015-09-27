package grabber;

import java.io.UnsupportedEncodingException;
import java.util.List;

import service.ResponseStruct;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;

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
	
	public static void main(String[] args) {
		// Write simple test.
		GrabberTwitter grabberTwitter = new GrabberTwitter();
		grabberTwitter.testTry();
	}
	
	private void testTry() {
		Twitter twitter = new TwitterFactory().getInstance();
		AccessToken accessToken = new AccessToken(ACCESS_TOKEN, ACCESS_SECRET);
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
		twitter.setOAuthAccessToken(accessToken);
		try {
			Query query = new Query("KOBE");
			QueryResult qResult;
		    QueryResult result = twitter.search(query);
		    for (Status status : result.getTweets()) {
		        System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
		    }
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public List<ResponseStruct> pullData(String companyName, String location) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}
	
}