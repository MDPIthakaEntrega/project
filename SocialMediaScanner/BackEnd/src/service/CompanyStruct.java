package service;

public class CompanyStruct {

	
	private String companyName;
	private String twitterName;
	private String yelpName;
	private String citygridName;
	private String location;
	
	
    /**
     * constructor;
     * 
     * @param response
     * @param companyName
     * @param APIName
     */
    public CompanyStruct(String companyName_in, String twitterName_in, String citygridName_in,
    		String yelpName_in, String location_in) {

		this.twitterName = twitterName_in;
		this.companyName = companyName_in;
		this.yelpName = yelpName_in;
		this.citygridName = citygridName_in;
		this.location = location_in;
	

    }


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public String getCompanyName() {
		return companyName;
	}


	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public String getTwitterName() {
		return twitterName;
	}


	public void setTwitterName(String twitterName) {
		this.twitterName = twitterName;
	}


	public String getYelpName() {
		return yelpName;
	}


	public void setYelpName(String yelpName) {
		this.yelpName = yelpName;
	}


	public String getCitygridName() {
		return citygridName;
	}


	public void setCitygridName(String citygridName) {
		this.citygridName = citygridName;
	}
	
}
