package grabber;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import service.ResponseStruct;

public class GrabberImportMagicYelp extends ImportIO {

 /**
  * 
  * 
  */
	
	private String yelpCompanyName;
	private static final String yelpBaseURL = "http://www.yelp.com/biz/";
	private static final String yelpPageExtension = "?start=";
	private static final int RPP = 40;
	
	// TODO grab from first API call to figure out how many results
	private static int totalResults = 40;

	
	public static void main(String args[]) {
		
		GrabberImportMagicYelp test = new GrabberImportMagicYelp();
		
		try {
			test.pullData("zingerman's", "Ann Arbor");
			System.out.println("Success");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	public GrabberImportMagicYelp() {
		
		// TODO change to dynamic name
		yelpCompanyName = "zingermans-delicatessen-ann-arbor-2";
		baseURL = "https://api.import.io/store/connector/_magic?url=";
		try {
			searchURL = URLEncoder.encode(yelpBaseURL, "UTF-8") + URLEncoder.encode(yelpCompanyName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public GrabberImportMagicYelp(String yelpCompanyName_in) {
		
		// TODO change to dynamic name
		yelpCompanyName = yelpCompanyName_in;
		baseURL = "https://api.import.io/store/connector/_magic?url=";
		try {
			searchURL = URLEncoder.encode(yelpBaseURL, "UTF-8") + URLEncoder.encode(yelpCompanyName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String formatRequestURL(String userParam, String apiParam, String extension) {
			
		String jsParam;
		String infiniteScrollParam = null;
		if(processJS) {
			
			jsParam = "js=true";
			if(infiniteScrollPages != -1) {
				infiniteScrollParam = "infiniteScrollPages=" + String.valueOf(infiniteScrollPages);
			}
		}
		else {
			jsParam = "js=false";
		}
		String requestURL = baseURL + searchURL + extension + "&" + jsParam;
		
		if(infiniteScrollParam != null) {
			
			requestURL += "&" + infiniteScrollParam;
		}
		requestURL += "&" + userParam + "&" + apiParam;	
		return requestURL;
	}
	
	@Override
	public List<ResponseStruct> pullData(String companyName, String location)
			throws UnsupportedEncodingException {

		String userParam = "_user=" + user;
		String apiParam = "_apikey=" + apiKey;
		
		String requestURL = this.formatRequestURL(userParam, apiParam, "");
		
		String response;
		List<ResponseStruct> responseStructList = new LinkedList<ResponseStruct>();
		try {

			response = sendGet(requestURL);
			responseStructList.add(new ResponseStruct(response, companyName, "Yelp"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// TODO parse and set numPages
				
		int numRemainingPages = (totalResults - 1) / RPP;
		
		// Call the rest of the pages
		for(int i = 0; i < numRemainingPages; i++) {

			requestURL = formatRequestURL(userParam, apiParam, yelpPageExtension + Integer.toString((i + 1) * (RPP + 1)));
			try {
				System.out.println(requestURL);
				response = sendGet(requestURL);
				responseStructList.add(new ResponseStruct(response, companyName, "Yelp"));
				System.out.println("Returned");
				System.out.println(response);
			}
			catch (IOException e) {
				
				// TODO
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