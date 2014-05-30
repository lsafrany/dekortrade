package hu.dekortrade.server.sync;

import com.google.appengine.api.datastore.Blob;

public class CikkaltipusSzinkron {

	private String fokod;

	private String kod;

	private String nev;

	private Blob blob;

	public CikkaltipusSzinkron() {

	}

	public CikkaltipusSzinkron(String fokod, String kod, String nev, Blob blob) {
		this.fokod = fokod;
		this.kod = kod;
		this.nev = nev;
		this.blob = blob;
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

	public Blob getBlob() {
		return blob;
	}

	public void setBlob(Blob blob) {
		this.blob = blob;
	}

}
