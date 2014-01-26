package hu.dekortrade.server;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Blob;
import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable
public class Kep implements IsSerializable {

	@Persistent
	private String cikkszam;

	@Persistent
	private String sorszam;
	
	@Persistent
	private Blob blob;

	@Persistent
	private Boolean szinkron;

	@Persistent
	private Boolean torolt;

	public Kep(String cikkszam, String sorszam, Blob blob, Boolean szinkron, Boolean torolt) {
		this.cikkszam = cikkszam;
		this.sorszam = sorszam;
		this.blob = blob;
		this.szinkron = szinkron;
		this.torolt = torolt;
	}

	public String getCikkszam() {
		return cikkszam;
	}

	public void setCikkszam(String cikkszam) {
		this.cikkszam = cikkszam;
	}

	public String getSorszam() {
		return sorszam;
	}

	public void setSorszam(String sorszam) {
		this.sorszam = sorszam;
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

	public Boolean getTorolt() {
		return torolt;
	}

	public void setTorolt(Boolean torolt) {
		this.torolt = torolt;
	}

}

