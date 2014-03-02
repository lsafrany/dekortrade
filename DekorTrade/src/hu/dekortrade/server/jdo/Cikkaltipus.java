package hu.dekortrade.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Blob;
import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable
public class Cikkaltipus implements IsSerializable {

	@Persistent
	private String fokod;

	@Persistent
	private String kod;

	@Persistent
	private String nev;

	@Persistent
	private Blob blob;
	
	@Persistent
	private Boolean szinkron;

	public Cikkaltipus(String fokod, String kod, String nev, Boolean szinkron) {
		this.fokod = fokod;
		this.kod = kod;
		this.nev = nev;
		this.szinkron = szinkron;
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

	public Boolean getSzinkron() {
		return szinkron;
	}

	public void setSzinkron(Boolean szinkron) {
		this.szinkron = szinkron;
	}

}
