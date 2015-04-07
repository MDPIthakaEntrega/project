package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Mysql database driver
 * @author yuke
 *
 */
public class MySqlDbDriver {

	public static void main(String[] args) {

		MySqlDbDriver mysqlDriver = new MySqlDbDriver();
		mysqlDriver.init();
		mysqlDriver.getUserInfo("sss");
	}
	
	/**
	 * connectino to mysql database.
	 */
	private Connection con = null;
	
	/**
	 * mysql statement.
	 */
	private Statement stmt = null;
	
	/**
	 * mysql prepared statement.
	 */
	private PreparedStatement preparedStmt = null;
	
	/**
	 * mysql result set.
	 */
	private ResultSet rst = null;
	
	/**
	 * url to database address.
	 */
	private final String url = "jdbc:mysql://localhost:3306";
	
	/**
	 * mysql database user name.
	 */
	private final String userName = "root";
	
	/**
	 * mysql database password.
	 */
	private final String passWord = "12345678";
	
	/**
	 * default database name, and the application will automatically detect if it is already exists, and if not, a new database will be created.
	 */
	private final String dbName = "UserInfoDb";
	
	/**
	 * default table name, and the application will automatically detect if it is already exists, and if not, a new table will be created.
	 */
	private final String tableName = "UserInfoTb";

	/**
	 * select all user information in the database.
	 * @return
	 */
	public List<HashMap<String, String>> getAllUserInfo() {

		String query = "SELECT * FROM " + tableName;
		try {
			rst = stmt.executeQuery(query);
			List<HashMap<String, String>> listUserInfo = new LinkedList<HashMap<String, String>>();
			while (rst.next()) {

				String managerName = rst.getString("manager_name");
				String companyName = rst.getString("company_name");
				String location = rst.getString("location");
				listUserInfo.add(getUserInfoMap(managerName, companyName,
						location));
			}

			return listUserInfo;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * select user information with specified company name.
	 * @param companyName
	 * @return
	 */
	public HashMap<String, String> getUserInfo(String companyName) {

		String query = "SELECT * FROM " + tableName + " WHERE company_name = "
				+ companyName;
		try {
			rst = stmt.executeQuery(query);
			HashMap<String, String> mapUserInfo = new HashMap<String, String>();
			while (rst.next()) {
				mapUserInfo = getUserInfoMap(rst.getString("manager_name"),
						companyName, rst.getString("location"));
			}

			return mapUserInfo;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * a helper function to generate a hashmap with given attributes.
	 * 
	 * @param managerName
	 * @param companyName
	 * @param location
	 * @return
	 */
	private HashMap<String, String> getUserInfoMap(String managerName,
			String companyName, String location) {

		HashMap<String, String> mapUserInfo = new HashMap<String, String>();
		mapUserInfo.put("manager_name", managerName);
		mapUserInfo.put("company_name", companyName);
		mapUserInfo.put("location", location);

		return mapUserInfo;
	}
	
	/**
	 * Initialization, tasks include initialize connection, driver, database, table etc.
	 */
	public void init() {

		try {

			Class.forName("com.mysql.jdbc.Driver");
			con = java.sql.DriverManager.getConnection(url, userName, passWord);
			stmt = con.createStatement();
			stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName + ";");
			stmt.execute("USE " + dbName);
			String createTbStr = "CREATE TABLE IF NOT EXISTS " + tableName
					+ " (manager_name VARCHAR(255),"
					+ " company_name VARCHAR(255)," + " location VARCHAR(255),"
					+ " PRIMARY KEY (company_name))";
			stmt.execute(createTbStr);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Insert user information into the database.
	 * @param managerName
	 * @param companyName
	 * @param location
	 */
	public void insertUser(String managerName, String companyName,
			String location) {
		String query = "INSERT INTO " + tableName + " VALUES (?, ?, ?)";
		try {
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString(1, managerName);
			preparedStmt.setString(2, companyName);
			preparedStmt.setString(3, location);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}