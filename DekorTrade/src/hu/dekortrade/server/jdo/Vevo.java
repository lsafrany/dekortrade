package hu.dekortrade.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Vevo {

	@Persistent
	private String rovidnev;

	@Persistent
	private String tipus;

	@Persistent
	private String nev;

	@Persistent
	private String cim;

	@Persistent
	private String elerhetoseg;

	@Persistent
	private Double egyenlegusd;
	
	@Persistent
	private Double egyenlegeur;
	
	@Persistent
	private Double egyenleghuf;
	
	@Persistent
	private Double tarolasidij;
	
	@Persistent
	private Double eloleg;
	
	@Persistent
	private String bankszamlaszam;

	@Persistent
	private String euadoszam;
	
	@Persistent
	private Double elorarkedvezmeny;
	
	@Persistent
	private Double ajanlottarkedvezmeny;
	
	@Persistent
	private String orszag;
	
	@Persistent
	private String megjegyzes;
	
	@Persistent
	private Boolean internet;

	@Persistent
	private Boolean szinkron;

	@Persistent
	private Boolean torolt;

	public Vevo(String rovidnev, String tipus, String nev, String cim, String elerhetoseg,
			Double egyenlegusd, Double egyenlegeur, Double egyenleghuf, Double tarolasidij,
			Double eloleg, String bankszamlaszam, String euadoszam, Double elorarkedvezmeny,
			Double ajanlottarkedvezmeny, String orszag, String megjegyzes,
			Boolean internet, Boolean szinkron, Boolean torolt) {
		this.rovidnev = rovidnev;
		this.tipus = tipus;
		this.nev = nev;
		this.cim = cim;
		this.elerhetoseg = elerhetoseg;
		this.egyenlegusd = egyenlegusd;
		this.egyenlegeur = egyenlegeur;
		this.egyenleghuf = egyenleghuf;
		this.tarolasidij = tarolasidij;
		this.eloleg = eloleg;
		this.bankszamlaszam = bankszamlaszam;	
		this.euadoszam = euadoszam;		
		this.elorarkedvezmeny = elorarkedvezmeny;				
		this.ajanlottarkedvezmeny = ajanlottarkedvezmeny;	
		this.orszag = orszag;	
		this.megjegyzes = megjegyzes;		
		this.internet = internet;
		this.szinkron = szinkron;
		this.torolt = torolt;
	}

	public String getRovidnev() {
		return rovidnev;
	}

	public void setRovidnev(String rovidnev) {
		this.rovidnev = rovidnev;
	}
	
	public String getTipus() {
		return tipus;
	}

	public void setTipus(String tipus) {
		this.tipus = tipus;
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

	public Boolean getInternet() {
		return internet;
	}

	public void setInternet(Boolean internet) {
		this.internet = internet;
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
	
}
