package grabber;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import service.CompanyStruct;
import service.ResponseStruct;

public class ImportYelp extends ImportIO {

    private static final int RPP = 40;
    private static final String yelpBaseURL = "http://www.yelp.com/biz/";
    private static final String yelpPageExtension = "?start=";
    private static final String yelpURLNumPages = "https://api.import.io/store/data/292e7a60-c36d-4ce3-843a-33454cf8d3f9/_query?input/webpage/url=";
    private static final String yelpURLResults = "https://api.import.io/store/data/e9a7e4dc-d0c8-4884-b477-e6b12ee99a5c/_query?input/webpage/url=";

    public static void main(String[] args) {

//		ImportYelp test = null;
//		try {
//		    test = new ImportYelp("espresso-royale-ann-arbor-5");
//		} catch (UnsupportedEncodingException e) {
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		}
//	
//		try {
//		    test.pullData("Espresso Royale", "Ann Arbor");
//		} catch (UnsupportedEncodingException e) {
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		}

    }

    private String yelpCompanyName;

    public ImportYelp(String yelpCompanyName_in) throws UnsupportedEncodingException {
	yelpCompanyName = yelpCompanyName_in;
	searchURL = yelpBaseURL + URLEncoder.encode(yelpCompanyName, "UTF-8");
    }

    @Override
    public List<ResponseStruct> pullData(CompanyStruct companyName, String location) throws UnsupportedEncodingException {

	String userParam = "_user=" + user;
	String apiParam = "_apikey=" + apiKey;

	// Grab num pages
	int numPages = 1;
	// System.out.println(yelpBaseURL);
	// String encoded = URLEncoder.encode(yelpBaseURL, "UTF-8");
	// System.out.println(encoded);
	String requestURL = yelpURLNumPages + URLEncoder.encode(yelpBaseURL, "UTF-8")
		+ URLEncoder.encode(yelpCompanyName, "UTF-8") + "&" + userParam + "&" + apiParam;
	System.out.println(requestURL);
	String response = null;
	try {
	    response = sendGet(requestURL);
	    // TODO remove prints
	    System.out.println("Response:");
	    System.out.println(response);

	    JSONObject getNumReviews = new JSONObject(response);
	    if (!getNumReviews.has("results")) {
		System.out.println("error");
		return null;
	    }
	    JSONArray results = getNumReviews.getJSONArray("results");

	    Double num_reviews = (Double) results.getJSONObject(0).get("num_reviews");
	    int num_reviews_int = num_reviews.intValue();
	    numPages = (num_reviews_int - 1) / RPP;
	    // System.out.println(num_reviews_int);

	} catch (IOException | JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    System.out.println("Error, returning null");
	    return null;
	}

	// Grab results
	for (int i = 0; i < numPages; i++) {

	    requestURL = yelpURLResults + URLEncoder.encode(yelpBaseURL, "UTF-8")
		    + URLEncoder.encode(yelpCompanyName, "UTF-8") + yelpPageExtension + String.valueOf(i * RPP) + "&"
		    + userParam + "&" + apiParam;
	    try {
		response = sendGet(requestURL);
		System.out.println("response: ");
		System.out.println(response);
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	return null;
    }

}
