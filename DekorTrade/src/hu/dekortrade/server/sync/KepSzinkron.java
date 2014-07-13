package hu.dekortrade.server.sync;

import com.google.appengine.api.datastore.Blob;
import com.google.gwt.user.client.rpc.IsSerializable;

public class KepSzinkron implements IsSerializable {

	private String cikkszam;

	private String szinkod;

	private String sorszam;

	private Blob blob;

	private Boolean torolt;

	public KepSzinkron() {

	}

	public KepSzinkron(String cikkszam, String szinkod, String sorszam,
			Blob blob, Boolean torolt) {
		this.cikkszam = cikkszam;
		this.szinkod = szinkod;
		this.sorszam = sorszam;
		this.blob = blob;
		this.torolt = torolt;
	}

	public String getCikkszam() {
		return cikkszam;
	}

	public void setCikkszam(String cikkszam) {
		this.cikkszam = cikkszam;
	}

	public String getSzinkod() {
		return szinkod;
	}

	public void setSzinkod(String szinkod) {
		this.szinkod = szinkod;
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

	public Boolean getTorolt() {
		return torolt;
	}

	public void setTorolt(Boolean torolt) {
		this.torolt = torolt;
	}

}
