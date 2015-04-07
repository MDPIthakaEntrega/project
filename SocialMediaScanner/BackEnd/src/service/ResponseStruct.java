package service;

import java.util.LinkedList;
import java.util.List;

public class ResponseStruct {
	
	public ResponseStruct(String response, String companyName, String APIName) {
		
		this.response = response;
		this.companyName = companyName;
		this.APIName = APIName;
		
	}
	
	public static List<ResponseStruct> getReponseStructListForOneAPI(List<String> responseList, List<String> companyNameList, 
			String APIName) {
		
		List<ResponseStruct> listResponseStruct = new LinkedList<ResponseStruct>();
		for (int i = 0; i < responseList.size(); i++) {
			
			listResponseStruct.add(new ResponseStruct(responseList.get(i), companyNameList.get(i), APIName));
		}
		
		return listResponseStruct;
	}
	
	private String response;
	
	private String companyName;
	
	private String APIName;

	public String getResponse() {
		return response;
	}

	public String getCompanyName() {
		return companyName;
	}
	
	public String getAPIName() {
		
		return APIName;
	}
	
	@Override
	public String toString() {
		
		String str = "API Name: " + APIName + "\n";
		str += "Company Name: " + companyName + "\n";
		str += "Reponse: " + response + "\n";
		
		return str;
	}
}