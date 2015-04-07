package database;

public class UserInfo {
	
	private String managerName = null;
	
	private String companyName = null;
	
	private String location = null;
	
	public UserInfo(String managerName, String companyName, String location) {
		
		this.managerName = managerName;
		this.companyName = companyName;
		this.location = location;
	}
	
	public String toQuery() {
		
		return managerName + ", " + companyName + ", " + location;
	}
}