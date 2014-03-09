package hu.dekortrade99.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Blob;
import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable
public class Cikkfotipus implements IsSerializable {

	@Persistent
	private String kod;

	@Persistent
	private String nev;

	@Persistent
	private Blob blob;
	
	public Cikkfotipus(String kod, String nev, Blob blob) {
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
