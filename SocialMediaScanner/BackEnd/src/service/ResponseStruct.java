package service;

public class ResponseStruct {
	
	public ResponseStruct(String response, String companyName, String APIName, String sentimentResult) {
		
		this.response = response;
		this.companyName = companyName;
		this.APIName = APIName;
		this.sentimentResult = sentimentResult;
		
	}
	
	private String response;
	
	private String companyName;
	
	private String APIName;
	
	private String sentimentResult;

	public String getResponse() {
		return response;
	}

	public String getCompanyName() {
		return companyName;
	}
	
	public String getAPIName() {
		
		return APIName;
	}
	
	public String getSentimentResult() {
		
		return sentimentResult;
	}
}