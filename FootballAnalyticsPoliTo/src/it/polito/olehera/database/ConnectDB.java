package it.polito.olehera.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;

public class ConnectDB {

	private static final String jdbcURL = "jdbc:mysql://localhost/fifa2019?serverTimezone=UTC";
	private static HikariDataSource ds;

	public static Connection getConnection() {

		if (ds == null) {
			
			ds = new HikariDataSource();

			ds.setJdbcUrl(jdbcURL);
			ds.setUsername("root");
			ds.setPassword("sascia");

			// configurazione MySQL
			ds.addDataSourceProperty("cachePrepStmts", "true");
			ds.addDataSourceProperty("prepStmtCacheSize", "250");
			ds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

		}

		try {

			return ds.getConnection();

		} catch (SQLException e) {
			System.err.println("Errore connessione al DB");
			throw new RuntimeException(e);
		}
	}

}

