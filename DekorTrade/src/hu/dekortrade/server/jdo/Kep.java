package hu.dekortrade.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable
public class Kep implements IsSerializable {

	@Persistent
	private String rovidnev;

	@Persistent
	private String nev;

	@Persistent
	private String blobkey;
	
	public Kep(String rovidnev, String nev,String blobkey) {
		this.rovidnev = rovidnev;
		this.nev = nev;
		this.blobkey = blobkey;
	}

	public String getRovidnev() {
		return rovidnev;
	}

	public void setRovidnev(String rovidnev) {
		this.rovidnev = rovidnev;
	}

	public String getNev() {
		return nev;
	}

	public void setNev(String nev) {
		this.nev = nev;
	}

	public String getBlobkey() {
		return blobkey;
	}

	public void setBlobkey(String blobkey) {
		this.blobkey = blobkey;
	}
	
}

