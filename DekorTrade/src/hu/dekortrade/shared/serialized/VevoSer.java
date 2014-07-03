package hu.dekortrade.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class VevoSer implements IsSerializable {

	private String rovidnev;

	private String tipus;

	private String nev;

	private String cim;

	private String elerhetoseg;

	private Float egyenlegusd;
	
	private Float egyenlegeur;
	
	private Float egyenleghuf;
	
	private Float tarolasidij;
	
	private Float eloleg;
	
	private String bankszamlaszam;

	private String euadoszam;
		
	private Float elorarkedvezmeny;
	
	private Float ajanlottarkedvezmeny;
	
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

	public Float getEgyenlegusd() {
		return egyenlegusd;
	}

	public void setEgyenlegusd(Float egyenlegusd) {
		this.egyenlegusd = egyenlegusd;
	}

	public Float getEgyenlegeur() {
		return egyenlegeur;
	}

	public void setEgyenlegeur(Float egyenlegeur) {
		this.egyenlegeur = egyenlegeur;
	}

	public Float getEgyenleghuf() {
		return egyenleghuf;
	}

	public void setEgyenleghuf(Float egyenleghuf) {
		this.egyenleghuf = egyenleghuf;
	}

	public Float getTarolasidij() {
		return tarolasidij;
	}

	public void setTarolasidij(Float tarolasidij) {
		this.tarolasidij = tarolasidij;
	}

	public Float getEloleg() {
		return eloleg;
	}

	public void setEloleg(Float eloleg) {
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

	public Float getElorarkedvezmeny() {
		return elorarkedvezmeny;
	}

	public void setElorarkedvezmeny(Float elorarkedvezmeny) {
		this.elorarkedvezmeny = elorarkedvezmeny;
	}

	public Float getAjanlottarkedvezmeny() {
		return ajanlottarkedvezmeny;
	}

	public void setAjanlottarkedvezmeny(Float ajanlottarkedvezmeny) {
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
