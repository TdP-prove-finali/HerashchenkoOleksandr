package it.polito.olehera.model;

public class Calciatore {
	
	private int id;
	private String nome;
	private int anni;
	private String nazionalità;
	private int overall;
	private int potential;
	private Rosa club;
	private String valore;
	private String posizione;
	private int cross;
	private int dribbling;
	private int controllo;
	private int accelerazione;
	private int velocità;
	private int agilità;
	private int reazione;
	private int tiro;
	
	public Calciatore(int id, String nome, int anni, String nazionalità, int overall, int potential, Rosa club,
			          String valore, String posizione, int cross, int dribbling, int controllo, int accelerazione, 
			          int velocità, int agilità, int reazione, int tiro) {
		this.id = id;
		this.nome = nome;
		this.anni = anni;
		this.nazionalità = nazionalità;
		this.overall = overall;
		this.potential = potential;
		this.club = club;
		this.valore = valore;
		this.posizione = posizione;
		this.cross = cross;
		this.dribbling = dribbling;
		this.controllo = controllo;
		this.accelerazione = accelerazione;
		this.velocità = velocità;
		this.agilità = agilità;
		this.reazione = reazione;
		this.tiro = tiro;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public int getAnni() {
		return anni;
	}

	public String getNazionalità() {
		return nazionalità;
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
		return valore;
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
		      prezzo = Float.parseFloat(valore.substring(0, valore.length()-1));
		} catch(NumberFormatException nfe) {
		      return 0;
		}

		char c = valore.charAt(valore.length()-1);
		if (c == 'M')
			prezzo *= 1000000;
		else if (c == 'K') 
			prezzo *= 1000;
			
		return (int)prezzo;
	}
	
	public String getRuolo() {
		String ruolo = "";
		
		switch (posizione.trim()) {
		case "GK": ruolo = "portiere"; break;
		case "RB": ruolo = "difensore"; break;
		case "CB": ruolo = "difensore"; break;
		case "LCB": ruolo = "difensore"; break;
		case "RCB": ruolo = "difensore"; break;
		case "LB": ruolo = "difensore"; break;
		case "RWB": ruolo = "difensore"; break;
		case "LWB": ruolo = "difensore"; break;
		case "CDM": ruolo = "centrocampista"; break;
		case "LDM": ruolo = "centrocampista"; break;
		case "RDM": ruolo = "centrocampista"; break;
		case "CM": ruolo = "centrocampista"; break;
		case "CAM": ruolo = "centrocampista"; break;
		case "RAM": ruolo = "centrocampista"; break;
		case "LAM": ruolo = "centrocampista"; break;
		case "LCM": ruolo = "centrocampista"; break;
		case "RCM": ruolo = "centrocampista"; break;
		case "RM": ruolo = "centrocampista"; break;
		case "LM": ruolo = "centrocampista"; break;
		case "RW": ruolo = "attaccante"; break;
		case "LW": ruolo = "attaccante"; break;
		case "CF": ruolo = "attaccante"; break;
		case "RF": ruolo = "attaccante"; break;
		case "LF": ruolo = "attaccante"; break;
		case "LS": ruolo = "attaccante"; break;
		case "RS": ruolo = "attaccante"; break;
		case "ST": ruolo = "attaccante"; break;
		}
		
		return ruolo;
	}
	
	public double getTecnica() {
		return (cross+dribbling+controllo+tiro)/4;
	}
	
	public double getFisico() {
		return (accelerazione+velocità+reazione+agilità)/4;
	}
	
	public double getPotenziale() {
		return (potential-overall)/anni;
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
		return nome+" "+anni+" "+nazionalità+" "+getRuolo()+" "+valore;
	}

}
