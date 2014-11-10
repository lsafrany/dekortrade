package hu.dekortrade.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class VevoSer implements IsSerializable {

	private String rovidnev;

	private String tipus;

	private String nev;

	private String cim;

	private String elerhetoseg;

	private Double egyenlegusd;

	private Double egyenlegeur;

	private Double egyenleghuf;

	private Double tarolasidij;

	private Double eloleg;

	private String bankszamlaszam;

	private String euadoszam;

	private Double elorarkedvezmeny;

	private Double ajanlottarkedvezmeny;

	private String orszag;

	private String megjegyzes;

	private Boolean internet;

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

	public String getTipus() {
		return tipus;
	}

	public void setTipus(String tipus) {
		this.tipus = tipus;
	}

	public Double getEgyenlegusd() {
		return egyenlegusd;
	}

	public void setEgyenlegusd(Double egyenlegusd) {
		this.egyenlegusd = egyenlegusd;
	}

	public Double getEgyenlegeur() {
		return egyenlegeur;
	}

	public void setEgyenlegeur(Double egyenlegeur) {
		this.egyenlegeur = egyenlegeur;
	}

	public Double getEgyenleghuf() {
		return egyenleghuf;
	}

	public void setEgyenleghuf(Double egyenleghuf) {
		this.egyenleghuf = egyenleghuf;
	}

	public Double getTarolasidij() {
		return tarolasidij;
	}

	public void setTarolasidij(Double tarolasidij) {
		this.tarolasidij = tarolasidij;
	}

	public Double getEloleg() {
		return eloleg;
	}

	public void setEloleg(Double eloleg) {
		this.eloleg = eloleg;
	}

	public String getBankszamlaszam() {
		return bankszamlaszam;
	}

	public void setBankszamlaszam(String bankszamlaszam) {
		this.bankszamlaszam = bankszamlaszam;
	}

	public String getEuadoszam() {
		return euadoszam;
	}

	public void setEuadoszam(String euadoszam) {
		this.euadoszam = euadoszam;
	}

	public Double getElorarkedvezmeny() {
		return elorarkedvezmeny;
	}

	public void setElorarkedvezmeny(Double elorarkedvezmeny) {
		this.elorarkedvezmeny = elorarkedvezmeny;
	}

	public Double getAjanlottarkedvezmeny() {
		return ajanlottarkedvezmeny;
	}

	public void setAjanlottarkedvezmeny(Double ajanlottarkedvezmeny) {
		this.ajanlottarkedvezmeny = ajanlottarkedvezmeny;
	}

	public String getOrszag() {
		return orszag;
	}

	public void setOrszag(String orszag) {
		this.orszag = orszag;
	}

	public String getMegjegyzes() {
		return megjegyzes;
	}

	public void setMegjegyzes(String megjegyzes) {
		this.megjegyzes = megjegyzes;
	}

	public Boolean getInternet() {
		return internet;
	}

	public void setInternet(Boolean internet) {
		this.internet = internet;
	}

}
