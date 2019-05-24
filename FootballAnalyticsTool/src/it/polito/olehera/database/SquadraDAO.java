package it.polito.olehera.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.olehera.model.Campionato;
import it.polito.olehera.model.Rosa;

public class SquadraDAO {
	
	/*
	 *   Ottengo la lista di Campionati
	 */
	public List<Campionato> getCampionati() {
		
		final String sql = "SELECT DISTINCT campionato FROM squadre WHERE campionato IS NOT NULL";
		List<Campionato> campionati = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				campionati.add(new Campionato(rs.getString("Campionato")));
			}
			
			conn.close();
			return campionati;
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 *   Ottengo la lista dei Club dato il Campionato
	 */
	public List<Rosa> getSquadre(String campionato) {
		
		final String sql = "SELECT Club FROM squadre WHERE campionato = ?";
		List<Rosa> squadre = new ArrayList<Rosa>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, campionato);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				squadre.add(new Rosa(rs.getString("Club")));
			}
			
			conn.close();
			return squadre;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}