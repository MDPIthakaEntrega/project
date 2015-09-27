/*
 * Charlie Scott
 * 
 * 
 * 
 */
package grabber;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import service.ResponseStruct;

public abstract class ImportIO extends DataGrabberGeneric {
	
	public static void main(String [] args) {
		System.out.println("Start test");
		try {
			ImportYelp test = new ImportYelp("zingermans-delicatessen-ann-arbor-2");
			test.pullData("Zingerman's", "Ann Arbor");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("End Test");
	}
	
	// Using default extractor
	// private String baseURL = "https://api.import.io/store/connector/_magic?url=";
	// custom extractor
	private String baseURL;
	
	protected String companyName;
	
	// URL to process
	protected String searchURL;
	
	// Process the area that contains this phrase
	protected String regionText;
	
	//	Whether to process with javascript on (slower)
	protected boolean processJS = false;
	
	//	 How many times to load infinite scroll - requires js=true
	protected int infiniteScrollPages = -1;
	
	//	What needs to be appended to url to get results on following pages
	protected String nextPage;
	
	//  To authenticate ImportIO API
	protected String apiKey = "ae3388c0633f4035a299ab81e7e5f12226a1c9bd982527d5252cf6a198be01e7cc2c439a298d8457f163f207c455e4e642fceb4e3dcd2bdc317b6a102bea5bb59b71f68566574766b3f082670a6013e6";	

	//	User parameter
	protected String user = "ae3388c0-633f-4035-a299-ab81e7e5f122";
	
	@Override
	public List<ResponseStruct> pullData(String companyName, String location)
			throws UnsupportedEncodingException {

		
		String jsParam = null;
		String infiniteScrollParam = null;
		String userParam = "_user=" + user;
		String apiParam = "_apikey=" + apiKey;
		
		if(processJS) {
			
			jsParam = "js=true";
			if(infiniteScrollPages != -1) {
				infiniteScrollParam = "infiniteScrollPages=" + String.valueOf(infiniteScrollPages);
			}
		}
		else {
			jsParam = "js=false";
		}
		
		// Build request URL
		
		String requestURL = baseURL + searchURL + "&" + jsParam;
		
		if(infiniteScrollParam != null) {
			
			requestURL += "&" + infiniteScrollParam;
		}
		
		requestURL += userParam + "&" + apiParam;		
		String response = null;
		try {
			System.out.println(requestURL);
			response = sendGet(requestURL);
			System.out.println("Returned");
			System.out.println(response);

		}
		catch (IOException e) {
			
			// TODO
			
		}
		
		
		return null;
	}
	
}