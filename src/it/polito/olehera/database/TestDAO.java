package it.polito.olehera.database;

import java.sql.Connection;

import it.polito.olehera.model.Calciatore;

public class TestDAO {

	public static void main(String[] args) {
		
		try {
			Connection connection = ConnectDB.getConnection();
			connection.close();
			System.out.println("Connection Test PASSED");
			
			CalciatoreDAO cdao = new CalciatoreDAO();
			SquadraDAO sDao = new SquadraDAO();
			
			System.out.println(sDao.getCampionati());
			
			for ( Calciatore c : cdao.getRosa("Milan") )
				System.out.println(c.toString());
			
		} catch (Exception e) {
			throw new RuntimeException("Test FAILED", e);
		}
	}

}
