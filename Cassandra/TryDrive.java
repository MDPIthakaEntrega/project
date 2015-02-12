//import statements
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

/**
 * 
 * @author Yuke Liao
 * @since 2/11/2015
 *
 */
public class TryDrive {
	
	/**
	 * 
	 * @param args input variable from command lines.
	 */
	public static void main(String[] args) {
		
		/**
		 * connection driver.
		 */
		Cluster cluster;
		
		/**
		 * keyspace driver.
		 */
		Session session;
		
		//initialization.
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
		session = cluster.connect("myspace");
		
		try {
			session.execute("DROP TABLE users;");
			session.execute("CREATE TABLE users (lastname text, firstname text, age int, PRIMARY KEY(lastname));");
			session.execute("INSERT INTO users (lastname, firstname, age) VALUES ('Liao', 'Yuke', 20);");
			session.execute("INSERT INTO users (lastname, firstname, age) VALUES ('Scott', 'Charlie', 20);");
			session.execute("INSERT INTO users (lastname, firstname, age) VALUES ('Chang', 'Emily', 20);");
			session.execute("INSERT INTO users (lastname, firstname, age) VALUES ('Zhao', 'Fangwen', 20);");
			session.execute("INSERT INTO users (lastname, firstname, age) VALUES ('Lu', 'Ren', 20);");
		}
		catch(Exception e) {
			
			e.printStackTrace();
		}
		
		ResultSet results = session.execute("SELECT * FROM users");
		for (Row row : results) {
			
			System.out.format("%s %s %d\n", row.getString("firstname"), row.getString("lastname"), row.getInt("age"));
		}
		
		cluster.close();
	}
}