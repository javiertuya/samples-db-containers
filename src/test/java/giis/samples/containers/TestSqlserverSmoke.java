package giis.samples.containers;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;

public class TestSqlserverSmoke extends Base {
	
	@Test
	public void testSqlserverQuery() throws SQLException, IOException {
		setUp("sqlserver");
		testAndAssertQuery("sqlserver");
	}
}
