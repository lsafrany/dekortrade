package hu.dekortrade99.server.sync;

import com.google.appengine.api.datastore.Blob;

public class CikkfotipusSzinkron  {

	private String kod;

	private String nev;

	private Blob blob;
	
	public CikkfotipusSzinkron() {
		
	}

	public CikkfotipusSzinkron(String kod, String nev, Blob blob) {
		this.kod = kod;
		this.nev = nev;
		this.blob = blob;
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
