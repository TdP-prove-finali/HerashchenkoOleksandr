package it.polito.olehera.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.olehera.model.Calciatore;
import it.polito.olehera.model.Rosa;

public class CalciatoreDAO {
	
	/*
	 *   Ottengo la lista di Calciatori dato il Nome della squadra
	 */
	public List<Calciatore> getRosa(String club) {
		
		final String sql = "SELECT * FROM calciatori WHERE Club = ?";
		List<Calciatore> rosa = new ArrayList<Calciatore>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, club);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				rosa.add(new Calciatore(rs.getInt("Id"), rs.getString("Nome"), rs.getInt("Anni"), rs.getString("Nazionalità"), 
						rs.getInt("Overall"), rs.getInt("Potential"), new Rosa(rs.getString("Club")), rs.getString("Valore"),
						rs.getString("Posizione"), rs.getInt("Cross"), rs.getInt("Dribbling"), rs.getInt("Controllo"), 
						rs.getInt("Accelerazione"), rs.getInt("Velocità"), rs.getInt("Agilità"), rs.getInt("Reazione"), rs.getInt("Tiro"))); 
						
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return rosa;
	}
	
	/*
	 *   Ottengo la lista di Calciatori dato il Campionato
	 */
	public List<Calciatore> getCalciatore(String campionato) {
		
		final String sql = "SELECT * FROM calciatori, squadre WHERE calciatori.Club = squadre.Club AND campionato = ?";
		List<Calciatore> calciatori = new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, campionato);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				calciatori.add(new Calciatore(rs.getInt("Id"), rs.getString("Nome"), rs.getInt("Anni"), rs.getString("Nazionalità"), 
						rs.getInt("Overall"), rs.getInt("Potential"), new Rosa(rs.getString("Club")), rs.getString("Valore"),
						rs.getString("Posizione"), rs.getInt("Cross"), rs.getInt("Dribbling"), rs.getInt("Controllo"), 
						rs.getInt("Accelerazione"), rs.getInt("Velocità"), rs.getInt("Agilità"), rs.getInt("Reazione"), rs.getInt("Tiro"))); 
						
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return calciatori;
	}

}