package it.polito.olehera.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rosa {
	
	private String nome;
	private List<Calciatore> calciatori;
	
	public Rosa(String nome) {
		this.nome = nome;
		calciatori = new ArrayList<Calciatore>();
	}
	
	public Rosa(Rosa iniziale, List<Calciatore> venduti) {
		this.nome = "parziale";
		calciatori = new ArrayList<Calciatore>();
		
		for (Calciatore c : iniziale.getCalciatori())
			if (!venduti.contains(c))
				calciatori.add(c);
	}

	public List<Calciatore> getCalciatori() {
		Collections.sort(calciatori);
		return calciatori;
	}

	public void setCalciatori(List<Calciatore> calciatori) {
		this.calciatori = calciatori;
	}

	public String getNome() {
		return nome;
	}
	
	public void addCalciatore(Calciatore c) {
		calciatori.add(c);
	}
	
	public void removeCalciatore(Calciatore c) {
		calciatori.remove(c);
	}
	
	public int numCalciatori() {
		return calciatori.size();
	}
	
	public int numPortieri() {
		int somma = 0;
		
		for (Calciatore c : calciatori)
			if (c.getRuolo().equals("portiere"))
				somma++;
		
		return somma;
	}
	
	public int numDifensori() {
		int somma = 0;
		
		for (Calciatore c : calciatori)
			if (c.getRuolo().equals("difensore"))
				somma++;
		
		return somma;
	}

	public int numCentrocampisti() {
		int somma = 0;
		
		for (Calciatore c : calciatori)
			if (c.getRuolo().equals("centrocampista"))
				somma++;
		
		return somma;
	}
	
	public int numAttaccanti() {
		int somma = 0;
		
		for (Calciatore c : calciatori)
			if (c.getRuolo().equals("attaccante"))
				somma++;
		
		return somma;
	}
	
	public double etaMedia() {
		double somma = 0.0;
		
		for (Calciatore c : calciatori)
			somma += c.getAnni();
		
		double result = somma/numCalciatori();
		
		return Math.floor(result*10.0)/10.0;
	}
	
	public int valoreTot() {
		int somma = 0;
		
		for (Calciatore c : calciatori)
			somma += c.getPrezzo();
		
		return somma;
	}
	
	public double mediaTecnica() {
		double somma = 0.0;
		
		for (Calciatore c : calciatori)
			somma += c.getTecnica();
		
		return somma/numCalciatori();
	}
	
	public double mediaFisico() {
		double somma = 0.0;
		
		for (Calciatore c : calciatori)
			somma += c.getFisico();
		
		return somma/numCalciatori();
	}
	
	public double mediaPotenziale() {
		double somma = 0.0;
		
		for (Calciatore c : calciatori)
			somma += c.getPotential();
		
		return somma/numCalciatori();
	}
	
	public double mediaOverall() {
		double somma = 0.0;
		
		for (Calciatore c : calciatori)
			somma += c.getOverall();
		
		return somma/numCalciatori();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((calciatori == null) ? 0 : calciatori.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rosa other = (Rosa) obj;
		if (calciatori == null) {
			if (other.calciatori != null)
				return false;
		} else if (!calciatori.equals(other.calciatori))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nome;
	}

}
