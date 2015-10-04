package service;

import java.util.LinkedList;
import java.util.List;

/**
 * an agreed struct for data layer and service layer to exchange information.
 * 
 * @author yuke
 *
 */
public class ResponseStruct {

    /**
     * helper function;
     * 
     * @param responseList
     * @param companyNameList
     * @param APIName
     * @return
     */
    public static List<ResponseStruct> getReponseStructListForOneAPI(List<String> responseList,
	    List<String> companyNameList, String APIName) {

	List<ResponseStruct> listResponseStruct = new LinkedList<ResponseStruct>();
	for (int i = 0; i < responseList.size(); i++) {

	    listResponseStruct.add(new ResponseStruct(responseList.get(i), companyNameList.get(i), APIName));
	}

	return listResponseStruct;
    }

    /**
     * name of the social media API;
     */
    private String APIName;

    /**
     * corresponding company name for the response;
     */
    private String companyName;

    /**
     * response returned from social media;
     */
    private String response;

    /**
     * constructor;
     * 
     * @param response
     * @param companyName
     * @param APIName
     */
    public ResponseStruct(String response, String companyName, String APIName) {

	this.response = response;
	this.companyName = companyName;
	this.APIName = APIName;

    }

    /**
     * getter of the api name;
     * 
     * @return
     */
    public String getAPIName() {

	return APIName;
    }

    /**
     * getter of the company name;
     * 
     * @return
     */
    public String getCompanyName() {
	return companyName;
    }

    /**
     * getter of the response;
     * 
     * @return
     */
    public String getResponse() {
	return response;
    }

    public void setAPIName(String APIName) {

	this.APIName = APIName;
    }

    @Override
    public String toString() {

	String str = "API Name: " + APIName + "\n";
	str += "Company Name: " + companyName + "\n";
	str += "Reponse: " + response + "\n";

	return str;
    }
}