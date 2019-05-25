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
		System.out.print("Calciatori caricati: "+calciatori.size()+"\n");
	}
	
	public Rosa calcolaRosaOttimizzata(List<Calciatore> venduti, int budget, double t, double q) {
		best = new Rosa("best");
		Rosa parziale = new Rosa(squadra, venduti);
		this.budget = budget;
		this.t = t;
		this.q = q;
		
		ottimizza(parziale, 0);
		
		return best;
	}
	
	private void ottimizza(Rosa parziale, int L) {
		
		if ( L > 1 )
			return ;
		
		if ( L == 1 ) {
			if ( migliore(parziale) ) {
				best.setCalciatori(parziale.getCalciatori());
			    primoBest = (1-t) * best.mediaOverall() + t * best.mediaPotenziale();
			    secondoBest = (1-q) * best.mediaTecnica() + q * best.mediaFisico();
			}
			return ; 
		}
		
		for (Calciatore c : calciatori) {
			
			parziale.addCalciatore(c);
			
			if ( controlloBudget(parziale) && controlloNumeroCalciatori(parziale) )
				ottimizza(parziale, L+1);
			
			parziale.removeCalciatore(c);
			
		}
		
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
	 *  Vincoli aggiuntivi sul numero di Calciatori
	 */
	private boolean controlloNumeroCalciatori(Rosa parziale) {
		if ( parziale.numCalciatori() > squadra.numCalciatori() )
			return false;
		if ( parziale.numAttaccanti() > squadra.numAttaccanti() )
			return false;
		if ( parziale.numCentrocampisti() > squadra.numCentrocampisti() )
			return false;
		if ( parziale.numDifensori() > squadra.numDifensori() )
			return false;
		if ( parziale.numPortieri() > squadra.numPortieri() )
			return false;
		
		return true;
	}
	
	/*
	 *  Multi-obiettivo da massimizzare
	 */
	private boolean migliore(Rosa parziale) {
		if ( best.numCalciatori() < 1 )
			return true;
		
		double primo = (1-t) * parziale.mediaOverall() + t * parziale.mediaPotenziale();
		double secondo = (1-q) * parziale.mediaTecnica() + q * parziale.mediaFisico();
		
		if ( primo >= primoBest && secondo >= secondoBest  )
			return true;
		else 
			return false;
	}

}