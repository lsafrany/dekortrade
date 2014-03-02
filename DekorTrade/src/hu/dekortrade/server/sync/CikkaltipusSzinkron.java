package hu.dekortrade.server.sync;

public class CikkaltipusSzinkron  {

	private String kod;

	private String nev;

	public CikkaltipusSzinkron() {
		
	}

	public CikkaltipusSzinkron(String kod, String nev, Boolean szinkron) {
		this.kod = kod;
		this.nev = nev;
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
