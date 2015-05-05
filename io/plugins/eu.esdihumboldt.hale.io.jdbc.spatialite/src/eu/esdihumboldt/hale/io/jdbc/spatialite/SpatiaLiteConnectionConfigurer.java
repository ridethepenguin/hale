package eu.esdihumboldt.hale.io.jdbc.spatialite;

import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteConnection;
import org.sqlite.core.DB;

import de.fhg.igd.slf4jplus.ALogger;
import de.fhg.igd.slf4jplus.ALoggerFactory;
import eu.esdihumboldt.hale.io.jdbc.extension.ConnectionConfigurer;

public class SpatiaLiteConnectionConfigurer implements
		ConnectionConfigurer<SQLiteConnection> {

	private static final ALogger log = ALoggerFactory
			.getLogger(SpatiaLiteConnectionConfigurer.class);

	/**
	 * Enable SpatiaLite extension for the provided connection.
	 */
	@Override
	public void configureConnection(SQLiteConnection connection) {
		DB sqliteDB = connection.db();
		Statement stmt = null;
		try {
			sqliteDB.enable_load_extension(true);

			stmt = connection.createStatement();
			stmt.setQueryTimeout(30); // set timeout to 30 sec.

			// loading SpatiaLite
			stmt.execute("SELECT load_extension('mod_spatialite')");
		} catch (SQLException e) {
			log.error("Failed to load SpatiaLite extension.", e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// ignore
				}
			}
		}
	}

}
