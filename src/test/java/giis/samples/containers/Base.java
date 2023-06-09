package giis.samples.containers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Base {
	private static final String ENVIRONMENT_PROPERTIES = "setup/environment.properties";
	private static final String DATABASE_PROPERTIES = "setup/database.properties";
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Before
	public void setUp() throws SQLException, IOException {
		try (Connection conn = getConnection("postgres"); Statement stmt = conn.createStatement()) {
			try {
				stmt.execute("drop table stable");
			} catch (SQLException e) {
				// ignore failure if it does not exist
			}
			stmt.execute("create table stable (col0 char(3) not null)");
			stmt.execute("delete from stable");
			stmt.execute("insert into stable (col0) values ('xyz')");
		}
	}
	
	/**
	 * Used by test subclasses, to perform a smoke test with a particular DBMS
	 */
	protected void testAndAssertQuery(String dbmsVendor) throws SQLException, IOException {
		try (Connection conn = getConnection(dbmsVendor); Statement stmt = conn.createStatement()) {
			ResultSet rs = stmt.executeQuery("select col0 from stable");
			assertTrue("At least a value must be read", rs.next());
			assertEquals("xyz", rs.getString("col0"));
		}
	}
	
	/**
	 * Connection user and url are obtanied from a properties file,
	 * password is obtained from environment, if not defined, another properties file is used as fallback
	 */
	protected Connection getConnection(String dbmsVendor) throws SQLException, FileNotFoundException, IOException {
		log.debug("Create connection to '{}' database", "postgres");
		return DriverManager.getConnection(
				getProp(DATABASE_PROPERTIES, "test." + dbmsVendor + ".url"), 
				getProp(DATABASE_PROPERTIES, "test." + dbmsVendor + ".user"),
				getEnvVar("TEST_" + dbmsVendor.toUpperCase() + "_PWD"));
	}

	protected String getEnvVar(String name) throws IOException {
		log.debug("Get '{}' from environment", name);
		String value = System.getenv(name);
		if (value == null || "".equals(value)) // fallback, read from file (as with the source bash command)
			value = getProp(ENVIRONMENT_PROPERTIES, name);
		return value;
	}
	
	protected String getProp(String fileName, String name) throws IOException {
		log.debug("Get '{}' property from file '{}'", name, fileName);
		Properties prop = new java.util.Properties();
		prop.load(new FileInputStream(fileName));
		String value = prop.getProperty(name, "");
		if ("".equals(value))
			throw new RuntimeException("Can't get '" + name + "' from  properties file '" + fileName + "'");
		return value;
	}

}
