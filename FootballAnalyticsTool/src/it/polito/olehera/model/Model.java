package it.polito.olehera.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.olehera.database.CalciatoreDAO;
import it.polito.olehera.database.SquadraDAO;

public class Model {
	
	private List<Campionato> campionati;
	private Campionato campionato;
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
	}
	
	public List<Rosa> getSquadre(Campionato campionato) {
		if (campionato.getSquadre().isEmpty())
			campionato.setSquadre(sdao.getSquadre(campionato.getNome()));
		
		return campionato.getSquadre();
	}
	
	public List<Campionato> getCampionati() {
		return campionati;
	}
	
	public Campionato getCampionato() {
		return campionato;
	}
	
	public void setCampionato(Campionato campionato) {
		this.campionato = campionato;
	}

	public Rosa getSquadraAnalizza() {
		return squadra;
	}

	public void setSquadraAnalizza(Rosa squadra) {
		this.squadra = squadra;
		this.squadra.setCalciatori(cdao.getRosa(squadra.getNome()));
	}
	
	public Rosa calcolaRosaOttimizzata(List<Calciatore> venduti, int budget, double t, double q) {
		best = new Rosa("best");
		Rosa parziale = new Rosa(squadra, venduti);
		best.setCalciatori(new ArrayList<>(parziale.getCalciatori()));
		primoBest = (1-t) * best.mediaOverall() + t * best.mediaPotenziale();
        secondoBest = (1-q) * best.mediaTecnica() + q * best.mediaFisico();
		this.budget = budget;
		this.t = t;
		this.q = q;
		
		int somma = 0;
		for (Calciatore calciatore : venduti)
			somma += calciatore.getPrezzo();
		
		filtraCalciatori(parziale, budget+somma);
		
		ottimizza(parziale, 0);
		
		return best;
	}
	
	private void ottimizza(Rosa parziale, int L) { 
		
		if ( L == calciatori.size() ) {
			if ( controlloMinCalciatori(parziale) && migliore(parziale) ) {
				best.setCalciatori(new ArrayList<>(parziale.getCalciatori()));
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
		if ( parziale.numCalciatori() > max(30, squadra.numCalciatori()) )
			return false;
		if ( parziale.numAttaccanti() > max(6, squadra.numAttaccanti()) )
			return false;
		if ( parziale.numCentrocampisti() > max(10, squadra.numCentrocampisti()) )
			return false;
		if ( parziale.numDifensori() > max(10, squadra.numDifensori()) )
			return false;
		if ( parziale.numPortieri() > max(3, squadra.numPortieri()) )
			return false;
		
		return true;
	}
	
	/*
	 *  Vincoli aggiuntivi sul numero minimo di Calciatori
	 */
	private boolean controlloMinCalciatori(Rosa completa) {
		if ( completa.numCalciatori() < min(25, squadra.numCalciatori()) )
			return false;
		if ( completa.numAttaccanti() < min(4, squadra.numAttaccanti()) )
			return false;
		if ( completa.numCentrocampisti() < min(8, squadra.numCentrocampisti()) )
			return false;
		if ( completa.numDifensori() < min(8, squadra.numDifensori()) )
			return false;
		if ( completa.numPortieri() < min(3, squadra.numPortieri()) )
			return false;
		
		return true;
	}
	
	/*
	 *  Multi-obiettivo da massimizzare
	 */
	private boolean migliore(Rosa completa) {
		
		double primo = (1-t) * completa.mediaOverall() + t * completa.mediaPotenziale();
		double secondo = (1-q) * completa.mediaTecnica() + q * completa.mediaFisico();
		
		if ( (primo*0.5+secondo*0.5) >= (primoBest*0.5+secondoBest*0.5) )
			return true;
		else 
			return false;
	}
	
	private void filtraCalciatori(Rosa iniziale, int b) {
		calciatori.clear();
		calciatori.addAll(cdao.getCalciatori(campionato.getNome()));
		calciatori.removeAll(squadra.getCalciatori());
		List<Calciatore> rimuovi = new ArrayList<>();
		
		double primo = (1-t) * iniziale.mediaOverall() + t * iniziale.mediaPotenziale();
		double secondo = (1-q) * iniziale.mediaTecnica() + q * iniziale.mediaFisico();
		
		boolean por = true;
		if ( iniziale.numPortieri() < 3 )
			por = false;
		
		boolean dif = false;
		if ( iniziale.numDifensori() > 10 )
			dif = true;
		
		boolean att = false;
		if ( iniziale.numAttaccanti() > 10 )
			att = true;
		
		boolean cen = false;
		if ( iniziale.numCentrocampisti() > 6 )
			cen = true;
		
		for (Calciatore c : calciatori) {
			double p = (1-t) * c.getOverall() + t * c.getPotential();
			double s = (1-q) * c.getTecnica() + q * c.getFisico();
			if ( (p < primo || s < secondo) || c.getPrezzo() > b || (cen && c.getRuolo()=="centrocampista")
				|| (dif && c.getRuolo()=="difensore") || (att && c.getRuolo()=="attaccante") || (por && c.getRuolo()=="portiere") )
				rimuovi.add(c); 
		}
		
		calciatori.removeAll(rimuovi);
	}
	
	private int max(int a, int b) {
		if (a > b)
			return a;
		else 
			return b;
	} 
	
	private int min(int a, int b) {
		if (a < b)
			return a;
		else 
			return b;
	} 

}