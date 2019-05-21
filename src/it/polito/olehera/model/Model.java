package it.polito.olehera.model;

import java.util.List;

import it.polito.olehera.database.CalciatoreDAO;
import it.polito.olehera.database.SquadraDAO;

public class Model {
	
	private List<Campionato> campionati;
	private Rosa squadra;
	private SquadraDAO sdao;
	
	public Model() {
		sdao = new SquadraDAO();
		campionati = sdao.getCampionati();
	}
	
	public List<Rosa> getSquadre(Campionato campionato) {
		if (campionato.getSquadre().isEmpty())
			campionato.setSquadre(sdao.getSquadre(campionato.getNome()));
		
		return campionato.getSquadre();
	}
	
	public List<Campionato> getCampionati() {
		return campionati;
	}

	public Rosa getSquadraAnalizza() {
		return squadra;
	}

	public void setSquadraAnalizza(Rosa squadra) {
		this.squadra = squadra;
		CalciatoreDAO cdao = new CalciatoreDAO();
		squadra.setCalciatori(cdao.getRosa(squadra.getNome()));
	}

}
