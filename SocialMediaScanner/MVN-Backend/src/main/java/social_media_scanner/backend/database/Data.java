package social_media_scanner.backend.database;

import java.util.List;

import org.json.JSONException;

import social_media_scanner.backend.service.ResponseStruct;
import social_media_scanner.backend.service.SearchStruct;

public interface Data {

	/*
	 * Calls wrapper to add all API's that have been implemented
	 * And then calls wrapper so each API class will read and store the path for attributes
	 * 
	 * "Folder_location_i" is the path to the folder that contains the configuration files for each API
	 * all API configuration files should be in one folder
	 * 
	 * Must be called before insertData() or select()
	 * 
	 */
//	public void init(String folder_location_i) throws InstantiationException, IllegalAccessException, ClassNotFoundException;
	
	
	/*
	 * 
	 * Initialize variables needed to create database and creates database
	 * Must be called before insertData() or select()
	 */
//	public void initializeDatabase(String host_i, String keyspace_name_i, String
//			review_table_i, String inverted_table_i);
	
	
	/*
	 * Inserts responses from API to database for a given company
	 */
	void insertData(List<ResponseStruct> responses)
			throws InstantiationException, IllegalAccessException, JSONException, ClassNotFoundException;
	
	
	/*
	 * 
	 *	Select reviews for a company with a given search, returns JSONArray string
	 *  with JSONObjects containing all default attributes
	 *  
	 */
	String select(SearchStruct search)
			throws JSONException, ClassNotFoundException, InstantiationException, IllegalAccessException;
	
	
	/*
	 * 
	 *	Select reviews for a company with a given search, returns JSONArray string
	 *  with JSONObjects containing specified attributes
	 *  
	 */
	String select(SearchStruct search, List<String> attributes)
			throws JSONException, ClassNotFoundException, InstantiationException, IllegalAccessException;
	
}
