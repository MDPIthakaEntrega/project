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
}