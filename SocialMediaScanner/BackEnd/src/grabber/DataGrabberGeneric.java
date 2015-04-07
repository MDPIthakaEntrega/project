package grabber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public abstract class DataGrabberGeneric {
	
	String sendGet(String url) throws IOException {
		
		//url = "http://api.citygridmedia.com/content/reviews/v2/search/where?where=Ann%20Arbor,%20mi&what=Zingerman%27s&publisher=10000008938&format=json";
		
		URL urlObj = new URL(url);
		HttpURLConnection httpCon = (HttpURLConnection) urlObj.openConnection();
		httpCon.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
		httpCon.setRequestMethod("GET");
		
		System.out.println();
			
		BufferedReader in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
		String inputLine = null;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			
			response.append(new String(inputLine.getBytes("UTF-8"), "UTF-8"));
		}
		in.close();
		
		return response.toString();
	}
	
	public abstract List<String> pullData(String companyName, String location) throws UnsupportedEncodingException;
	
	public List<String> pullDataForAll(List<String> companyNameList, List<String> locationList) throws UnsupportedEncodingException {
		
		List<String> strList = new LinkedList<String>();
		for (int i = 0; i < companyNameList.size(); i++) {
			
			String companyName = companyNameList.get(i);
			String location = locationList.get(i);
			strList.addAll(pullData(companyName, location));
		}
		
		return strList;
	}
}