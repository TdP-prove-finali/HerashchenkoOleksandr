package it.polito.olehera.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.olehera.database.CalciatoreDAO;
import it.polito.olehera.database.SquadraDAO;

public class Model {
	
	private List<Campionato> campionati;
	private Rosa squadra;
	private Rosa best;
	private SquadraDAO sdao;
	private CalciatoreDAO cdao;
	private List<Calciatore> calciatori;
	private int budget;
	
	public Model() {
		cdao = new CalciatoreDAO();
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
		squadra.setCalciatori(cdao.getRosa(squadra.getNome()));
	}
	
	public void caricaCalciatori() {
		calciatori = new ArrayList<Calciatore>();
		
		for (Campionato c : campionati)
			calciatori.addAll(cdao.getCalciatori(c.getNome()));
		
		calciatori.removeAll(squadra.getCalciatori());
	}
	
	public Rosa calcolaRosaOttimizzata(int budget) {
		best = new Rosa("best");
		Rosa parziale = new Rosa(squadra);
		this.budget = budget;
		
		ottimizza(parziale, 0);
		
		return best;
	}
	
	private void ottimizza(Rosa parziale, int L) {
		
		if ( parziale.valoreTot() > (squadra.valoreTot() + budget) )
			return ;
		
		if ( parziale.numPortieri() < 3 ) 
			return ;
		
		if ( parziale.etaMedia() > best.etaMedia() ) {
			best.setCalciatori(parziale.getCalciatori());
			return ;
		}
		
		
		
		for (Calciatore c : calciatori) {
			
			parziale.addCalciatore(c);
			
			ottimizza(parziale, L+1);
			
			parziale.removeCalciatore(c);
			
		}
		
		
		
		
	}

}