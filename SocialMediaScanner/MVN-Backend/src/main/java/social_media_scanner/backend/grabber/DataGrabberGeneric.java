package social_media_scanner.backend.grabber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import social_media_scanner.backend.service.CompanyStruct;
import social_media_scanner.backend.service.ResponseStruct;

public abstract class DataGrabberGeneric {
	
	/*
	 * Set location to null if not specified.
	 */
	public abstract List<ResponseStruct> pullData(CompanyStruct company) throws UnsupportedEncodingException;
	
	/*
	 * A wrapper of pullData to pull data for a list of company-location pair. 
	 */
	public List<ResponseStruct> pullDataForAll(List<CompanyStruct> companyList) throws UnsupportedEncodingException {
		
		List<ResponseStruct> responseList = new LinkedList<ResponseStruct>();
		for (CompanyStruct companyName : companyList) {

			//			String location = locationList.get(i);
			responseList.addAll(pullData(companyName));
		}
		
		return responseList;
	}
	
	String sendGet(String url) throws IOException {
		
		//url = "http://api.citygridmedia.com/content/reviews/v2/search/where?where=Ann%20Arbor,%20mi&what=Zingerman%27s&publisher=10000008938&format=json";
		
		URL urlObj = new URL(url);
		HttpURLConnection httpCon = (HttpURLConnection) urlObj.openConnection();
		httpCon.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
		httpCon.setRequestMethod("GET");
			
		BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			
			response.append(new String(inputLine.getBytes("UTF-8"), "UTF-8"));
		}
		in.close();
		
		return response.toString();
	}
}