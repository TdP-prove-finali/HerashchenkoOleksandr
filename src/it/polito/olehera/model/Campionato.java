package it.polito.olehera.model;

import java.util.ArrayList;
import java.util.List;

public class Campionato {
	
	private String nome;
	private List<Rosa> squadre;
	
	public Campionato(String nome) {
		this.nome = nome;
		squadre = new ArrayList<Rosa>();
	}

	public List<Rosa> getSquadre() {
		return squadre;
	}

	public void setSquadre(List<Rosa> squadre) {
		this.squadre = squadre;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Campionato other = (Campionato) obj;
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
