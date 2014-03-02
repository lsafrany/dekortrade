package hu.dekortrade.server.sync;

public class CikkfotipusSzinkron  {

	private String fokod;

	private String kod;

	private String nev;

	public CikkfotipusSzinkron() {
		
	}

	public CikkfotipusSzinkron(String fokod, String kod, String nev, Boolean szinkron) {
		this.fokod = fokod;
		this.kod = kod;
		this.nev = nev;
	}
	
	public String getFokod() {
		return fokod;
	}

	public void setFokod(String fokod) {
		this.fokod = fokod;
	}

	public String getKod() {
		return kod;
	}

	public void setKod(String kod) {
		this.kod = kod;
	}

	public String getNev() {
		return nev;
	}

	public void setNev(String nev) {
		this.nev = nev;
	}
}
