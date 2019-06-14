package it.polito.olehera.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Calciatore implements Comparable<Calciatore>{
	
	private int id;
	private final SimpleStringProperty nome;
	private final SimpleIntegerProperty anni;
	private final SimpleStringProperty nazionalità;
	private int overall;
	private int potential;
	private Rosa club;
	private final SimpleStringProperty valore;
	private String posizione;
	private int cross;
	private int dribbling;
	private int controllo;
	private int accelerazione;
	private int velocità;
	private int agilità;
	private int reazione;
	private int tiro;
	private SimpleStringProperty ruolo;
	
	public Calciatore(int id, String nome, int anni, String nazionalità, int overall, int potential, Rosa club,
			          String valore, String posizione, int cross, int dribbling, int controllo, int accelerazione, 
			          int velocità, int agilità, int reazione, int tiro) {
		this.id = id;
		this.nome = new SimpleStringProperty(nome);
		this.anni = new SimpleIntegerProperty(anni);
		this.nazionalità = new SimpleStringProperty(nazionalità);
		this.overall = overall;
		this.potential = potential;
		this.club = club;
		this.valore = new SimpleStringProperty(valore);
		this.posizione = posizione;
		this.cross = cross;
		this.dribbling = dribbling;
		this.controllo = controllo;
		this.accelerazione = accelerazione;
		this.velocità = velocità;
		this.agilità = agilità;
		this.reazione = reazione;
		this.tiro = tiro;
		setRuolo();
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome.get();
	}

	public int getAnni() {
		return anni.get();
	}

	public String getNazionalità() {
		return nazionalità.get();
	}

	public int getOverall() {
		return overall;
	}

	public int getPotential() {
		return potential;
	}

	public Rosa getClub() {
		return club;
	}

	public String getValore() {
		return valore.get();
	}

	public String getPosizione() {
		return posizione;
	}

	public int getCross() {
		return cross;
	}

	public int getDribbling() {
		return dribbling;
	}

	public int getControllo() {
		return controllo;
	}

	public int getAccelerazione() {
		return accelerazione;
	}

	public int getVelocità() {
		return velocità;
	}

	public int getAgilità() {
		return agilità;
	}

	public int getReazione() {
		return reazione;
	}

	public int getTiro() {
		return tiro;
	}
	
	public int getPrezzo() {
		float prezzo;
		
		try {
		      prezzo = Float.parseFloat(getValore().substring(0, getValore().length()-1));
		} catch(NumberFormatException nfe) {
		      return 0;
		}

		char c = getValore().charAt(getValore().length()-1);
		if (c == 'M')
			prezzo *= 1000000;
		else if (c == 'K') 
			prezzo *= 1000;
			
		return (int)prezzo;
	}
	
	public void setRuolo() {
		switch (posizione.trim()) {
		case "GK": ruolo = new SimpleStringProperty("portiere"); break;
		case "RB": ruolo = new SimpleStringProperty("difensore"); break;
		case "CB": ruolo = new SimpleStringProperty("difensore"); break;
		case "LCB": ruolo = new SimpleStringProperty("difensore"); break;
		case "RCB": ruolo = new SimpleStringProperty("difensore"); break;
		case "LB": ruolo = new SimpleStringProperty("difensore"); break;
		case "RWB": ruolo = new SimpleStringProperty("difensore"); break;
		case "LWB": ruolo = new SimpleStringProperty("difensore"); break;
		case "CDM": ruolo = new SimpleStringProperty("centrocampista"); break;
		case "LDM": ruolo = new SimpleStringProperty("centrocampista"); break;
		case "RDM": ruolo = new SimpleStringProperty("centrocampista"); break;
		case "CM": ruolo = new SimpleStringProperty("centrocampista"); break;
		case "CAM": ruolo = new SimpleStringProperty("centrocampista"); break;
		case "RAM": ruolo = new SimpleStringProperty("centrocampista"); break;
		case "LAM": ruolo = new SimpleStringProperty("centrocampista"); break;
		case "LCM": ruolo = new SimpleStringProperty("centrocampista"); break;
		case "RCM": ruolo = new SimpleStringProperty("centrocampista"); break;
		case "RM": ruolo = new SimpleStringProperty("centrocampista"); break;
		case "LM": ruolo = new SimpleStringProperty("centrocampista"); break;
		case "RW": ruolo = new SimpleStringProperty("attaccante"); break;
		case "LW": ruolo = new SimpleStringProperty("attaccante"); break;
		case "CF": ruolo = new SimpleStringProperty("attaccante"); break;
		case "RF": ruolo = new SimpleStringProperty("attaccante"); break;
		case "LF": ruolo = new SimpleStringProperty("attaccante"); break;
		case "LS": ruolo = new SimpleStringProperty("attaccante"); break;
		case "RS": ruolo = new SimpleStringProperty("attaccante"); break;
		case "ST": ruolo = new SimpleStringProperty("attaccante"); break;
		}
	}
	
	public String getRuolo() {
		return ruolo.get();
	}
	
	public double getTecnica() {
		return (cross+dribbling+controllo+tiro)/4;
	}
	
	public double getFisico() {
		return (accelerazione+velocità+reazione+agilità)/4;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Calciatore other = (Calciatore) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nome.get();
	}

	@Override
	public int compareTo(Calciatore o) {
		return getRuolo().compareTo(o.getRuolo());
	}

}