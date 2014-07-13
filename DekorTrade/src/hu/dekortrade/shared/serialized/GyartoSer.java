package hu.dekortrade.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GyartoSer implements IsSerializable {

	private String kod;

	private String nev;

	private String cim;

	private String elerhetoseg;

	private String swifkod;

	private String bankadat;

	private String szamlaszam;

	private Float egyenleg;

	private String kedvezmeny;

	private String megjegyzes;

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

	public String getSwifkod() {
		return swifkod;
	}

	public void setSwifkod(String swifkod) {
		this.swifkod = swifkod;
	}

	public String getBankadat() {
		return bankadat;
	}

	public void setBankadat(String bankadat) {
		this.bankadat = bankadat;
	}

	public String getSzamlaszam() {
		return szamlaszam;
	}

	public void setSzamlaszam(String szamlaszam) {
		this.szamlaszam = szamlaszam;
	}

	public Float getEgyenleg() {
		return egyenleg;
	}

	public void setEgyenleg(Float egyenleg) {
		this.egyenleg = egyenleg;
	}

	public String getKedvezmeny() {
		return kedvezmeny;
	}

	public void setKedvezmeny(String kedvezmeny) {
		this.kedvezmeny = kedvezmeny;
	}

	public String getMegjegyzes() {
		return megjegyzes;
	}

	public void setMegjegyzes(String megjegyzes) {
		this.megjegyzes = megjegyzes;
	}

}
