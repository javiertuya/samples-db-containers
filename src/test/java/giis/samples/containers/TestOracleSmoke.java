package giis.samples.containers;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;

public class TestOracleSmoke extends Base {
	
	@Test
	public void testOracleQuery() throws SQLException, IOException {
		setUp("oracle");
		testAndAssertQuery("oracle");
	}
}
