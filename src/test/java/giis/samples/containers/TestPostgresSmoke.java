package giis.samples.containers;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;

public class TestPostgresSmoke extends Base {
	
	@Test
	public void testPostgresQuery() throws SQLException, IOException {
		setUp("postgres");
		testAndAssertQuery("postgres");
	}
}
