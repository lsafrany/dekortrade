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
	private Float egyenlegusd;
	
	@Persistent
	private Float egyenlegeur;
	
	@Persistent
	private Float egyenleghuf;
	
	@Persistent
	private Float tarolasidij;
	
	@Persistent
	private Float eloleg;
	
	@Persistent
	private String bankszamlaszam;

	@Persistent
	private String euadoszam;
	
	@Persistent
	private Float elorarkedvezmeny;
	
	@Persistent
	private Float ajanlottarkedvezmeny;
	
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
			Float egyenlegusd, Float egyenlegeur, Float egyenleghuf, Float tarolasidij,
			Float eloleg, String bankszamlaszam, String euadoszam, Float elorarkedvezmeny,
			Float ajanlottarkedvezmeny, String orszag, String megjegyzes,
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
	
}
