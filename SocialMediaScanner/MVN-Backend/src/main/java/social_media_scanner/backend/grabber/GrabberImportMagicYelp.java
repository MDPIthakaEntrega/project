package social_media_scanner.backend.grabber;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import social_media_scanner.backend.service.CompanyStruct;
import social_media_scanner.backend.service.ResponseStruct;

public class GrabberImportMagicYelp extends ImportIO {

    private static final int RPP = 40;
    // TODO grab from first API call to figure out how many results
    private static int totalResults = 40;
    private static final String yelpBaseURL = "http://www.yelp.com/biz/";
    private static final String yelpPageExtension = "?start=";

    private String yelpCompanyName;

    public GrabberImportMagicYelp() {

	yelpCompanyName = "zingermans-delicatessen-ann-arbor-2";
	baseURL = "https://api.import.io/store/connector/_magic?url=";
	try {
	    searchURL = URLEncoder.encode(yelpBaseURL, "UTF-8") + URLEncoder.encode(yelpCompanyName, "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
    }

    public GrabberImportMagicYelp(CompanyStruct yelpCompanyName_in) {

	yelpCompanyName = yelpCompanyName_in.getYelpName();
	baseURL = "https://api.import.io/store/connector/_magic?url=";
	try {
	    searchURL = URLEncoder.encode(yelpBaseURL, "UTF-8") + URLEncoder.encode(yelpCompanyName, "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
    }

    private String formatRequestURL(String userParam, String apiParam, String extension) {

	String jsParam;
	String infiniteScrollParam = null;
	if (processJS) {

	    jsParam = "js=true";
	    if (infiniteScrollPages != -1) {
		infiniteScrollParam = "infiniteScrollPages=" + String.valueOf(infiniteScrollPages);
	    }
	} else {
	    jsParam = "js=false";
	}
	String requestURL = baseURL + searchURL + extension + "&" + jsParam;

	if (infiniteScrollParam != null) {

	    requestURL += "&" + infiniteScrollParam;
	}
	requestURL += "&" + userParam + "&" + apiParam;
	return requestURL;
    }

    @Override
    public List<ResponseStruct> pullData(CompanyStruct companyName, String location)
	    throws UnsupportedEncodingException {

	yelpCompanyName = companyName.getYelpName();
	baseURL = "https://api.import.io/store/connector/_magic?url=";
	try {
	    searchURL = URLEncoder.encode(yelpBaseURL, "UTF-8") + URLEncoder.encode(yelpCompanyName, "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	String userParam = "_user=" + user;
	String apiParam = "_apikey=" + apiKey;

	String requestURL = this.formatRequestURL(userParam, apiParam, "");

	String response;
	List<ResponseStruct> responseStructList = new LinkedList<ResponseStruct>();
	try {
	    response = sendGet(requestURL);
	    responseStructList.add(new ResponseStruct(response, companyName.getCompanyName(), "Yelp"));
	} catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

	// TODO parse and set numPages

	int numRemainingPages = (totalResults - 1) / RPP;

	// Call the rest of the pages
	for (int i = 0; i < numRemainingPages; i++) {

	    requestURL = formatRequestURL(userParam, apiParam,
		    yelpPageExtension + Integer.toString((i + 1) * (RPP + 1)));
	    try {
		response = sendGet(requestURL);
		responseStructList.add(new ResponseStruct(response, companyName.getCompanyName(), "Yelp"));
	    } catch (IOException e) {

		e.printStackTrace();
	    }
	}

	return responseStructList;
    }

    @Override
    public String toString() {

	return "ImportMagicYelp";
    }

}