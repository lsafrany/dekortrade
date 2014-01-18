package hu.dekortrade.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable
public class Szallito implements IsSerializable {

	@Persistent
	private String kod;

	@Persistent
	private String nev;

	@Persistent
	private String cim;

	@Persistent
	private String elerhetoseg;

	@Persistent
	private Boolean torolt;

	public Szallito(String kod, String nev, String cim, String elerhetoseg,
			Boolean torolt) {
		this.kod = kod;
		this.nev = nev;
		this.cim = cim;
		this.elerhetoseg = elerhetoseg;
		this.torolt = torolt;
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

	public String getCim() {
		return cim;
	}

	public void setCim(String cim) {
		this.cim = cim;
	}

	public String getElerhetoseg() {
		return elerhetoseg;
	}

	public void setElerhetoseg(String elerhetoseg) {
		this.elerhetoseg = elerhetoseg;
	}

	public Boolean getTorolt() {
		return torolt;
	}

	public void setTorolt(Boolean torolt) {
		this.torolt = torolt;
	}

}
