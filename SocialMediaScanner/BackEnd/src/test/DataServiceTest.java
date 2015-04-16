package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import service.Server;

public class DataServiceTest {

	@Test
	public void test() throws IOException, JSONException {
		
		//empty the cassandra database before executing this test!
		
		//start service.
		String[] args = {"3456"};
		Server.main(args);
		
		//now, we assume the only one user is Zingerman, so after initialization, database should only contains 
		//information related to Zingerman's, and contains nothing about starbucks
		String urlStr = "http://localhost:3456/search?company%20name=starbucks%27s&keyword=sandwiches";
		String response = makeHttpRequest(urlStr);
		
		try {
			
			assertTrue("before pulling anything, the response should be empty", response.isEmpty());
		} catch (AssertionError e) {
			
			System.out.println(e.getMessage());
			throw e;
		}
		
		//the response of searching zingerman's with keyword expensive should be "valid";
		urlStr = "http://localhost:3456/search?company%20name=zingerman's%27s&keyword=expensive";
		response = makeHttpRequest(urlStr);
		CheckResponseValidity(response, "CityGrid", "expensive");
		
		
		//pull data for starbucks
		urlStr = "http://localhost:3456/pull?company%20name=starbucks&location=ann%20arbor,48105";
		response = makeHttpRequest(urlStr);
		
		//after pulling data for starbucks, then the search result should be non-empty and valid;
		urlStr = "http://localhost:3456/search?company%20name=zingerman's%27s&keyword=expensive";
		response = makeHttpRequest(urlStr);
		CheckResponseValidity(response, "CityGrid", "sandwiches");
	}
	
	//test source, content, rating, id.
	private void CheckResponseValidity(String response, String APIName, String keyword) throws JSONException {
		
		JSONArray jsonArray = new JSONArray(response);
		for (int i = 0; i < jsonArray.length(); i++) {
			
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			String sourceStr = jsonObj.getString("source");
			String contentStr = jsonObj.getString("content");
			String idStr = jsonObj.getString("id");
			double rating = jsonObj.getDouble("rating");
			try {
				
				assertTrue("source should be " + APIName, sourceStr.equals(APIName));
			} catch(AssertionError e) {
				
				System.out.println(e.getMessage());
				throw e;
			}
			
			try {
				
				String[] arrWord = contentStr.toLowerCase().split(" ");
				assertTrue("content should contains " + keyword, Arrays.asList(arrWord).contains(keyword.toLowerCase()));
			} catch (AssertionError e) {
				
				System.out.println(e.getMessage());
				throw e;
			}
			
			try {
				
				assertTrue("id should start with API name: " + APIName, idStr.startsWith(APIName));
			} catch (AssertionError e) {
				
				System.out.println(e.getMessage());
				throw e;
			}
			
			try {
				
				assertTrue("rating should be between 0, 1", 0 <= rating && rating <= 1);
			} catch (AssertionError e) {
				
				System.out.println(e.getMessage());
				throw e;
			}
		}
	}
	
	private String makeHttpRequest(String urlStr) throws IOException {
		
		URL url = new URL(urlStr);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "Plain/Text");
		int responseCode = con.getResponseCode();
		try {
			
			assertEquals("if request is sent successfully, response code should be 200", 
					responseCode, 200);
		} catch(AssertionError e) {
			
			System.out.println(e.getMessage());
			throw e;
		}
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
	}
}
