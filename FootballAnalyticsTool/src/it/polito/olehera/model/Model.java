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
	private double t;
	private double q;
	private double primoBest;
	private double secondoBest;
	
	public Model() {
		cdao = new CalciatoreDAO();
		sdao = new SquadraDAO();
		campionati = sdao.getCampionati();
		calciatori = new ArrayList<Calciatore>();
//		for (Campionato c : campionati)
			calciatori.addAll(cdao.getCalciatori("Serie A"));
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
	
	public Rosa calcolaRosaOttimizzata(List<Calciatore> venduti, int budget, double t, double q) {
		best = new Rosa("best");
		Rosa parziale = new Rosa(squadra, venduti);
		this.budget = budget;
		this.t = t;
		this.q = q;
		
		filtraCalciatori();
		
		ottimizza(parziale, 0);
		
		return best;
	}
	
	private void ottimizza(Rosa parziale, int L) { 
		
		if ( L == calciatori.size() ) {
			if ( controlloMinCalciatori(parziale) && migliore(parziale) ) {
				best.setCalciatori(parziale.getCalciatori());
		        primoBest = (1-t) * best.mediaOverall() + t * best.mediaPotenziale();
		        secondoBest = (1-q) * best.mediaTecnica() + q * best.mediaFisico();
		    }
		    return ;
		}
		
		ottimizza(parziale, L+1);
		
		
		parziale.addCalciatore(calciatori.get(L));
			
		if ( controlloBudget(parziale) && controlloMaxCalciatori(parziale) )
			ottimizza(parziale, L+1);
			
		parziale.removeCalciatore(calciatori.get(L));
		
	}
	
	/*
	 *  Vincolo sul Budget
	 */
	private boolean controlloBudget(Rosa parziale) {
		if ( parziale.valoreTot() > (squadra.valoreTot() + budget) )
			return false;
		else
			return true;
	}
	
	/*
	 *  Vincoli aggiuntivi sul numero massimo di Calciatori
	 */
	private boolean controlloMaxCalciatori(Rosa parziale) {
		if ( parziale.numCalciatori() > 34 )
			return false;
		if ( parziale.numAttaccanti() > 8 )
			return false;
		if ( parziale.numCentrocampisti() > 11 )
			return false;
		if ( parziale.numDifensori() > 11 )
			return false;
		if ( parziale.numPortieri() > 4 )
			return false;
		
		return true;
	}
	
	/*
	 *  Vincoli aggiuntivi sul numero minimo di Calciatori
	 */
	private boolean controlloMinCalciatori(Rosa completa) {
		if ( completa.numCalciatori() < 24 )
			return false;
		if ( completa.numAttaccanti() < 4 )
			return false;
		if ( completa.numCentrocampisti() < 8 )
			return false;
		if ( completa.numDifensori() < 8 )
			return false;
		if ( completa.numPortieri() < 3 )
			return false;
		
		return true;
	}
	
	/*
	 *  Multi-obiettivo da massimizzare
	 */
	private boolean migliore(Rosa completa) {
		if ( best.numCalciatori() < 1 )
			return true;
		
		double primo = (1-t) * completa.mediaOverall() + t * completa.mediaPotenziale();
		double secondo = (1-q) * completa.mediaTecnica() + q * completa.mediaFisico();
		
		if ( primo >= primoBest && secondo >= secondoBest  )
			return true;
		else 
			return false;
	}
	
	private void filtraCalciatori() {
		List<Calciatore> rimuovi = new ArrayList<>();
		calciatori.removeAll(squadra.getCalciatori());	
		
//		for (Calciatore c : calciatori)
//			if ( c.getPrezzo() >  )
//				rimuovi.add(c);
		
		calciatori.removeAll(rimuovi);
		System.out.print("Calciatori filtrati: "+calciatori.size()+"\n");
	}

}