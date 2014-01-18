package hu.dekortrade.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Rendeltcikk {

	@Persistent
	private String rovidnev;

	@Persistent
	private String rendeles;

	@Persistent
	private String cikkszam;

	@Persistent
	private Integer exportkarton;

	public Rendeltcikk(String rovidnev, String rendeles, String cikkszam,
			Integer exportkarton) {
		this.rovidnev = rovidnev;
		this.rendeles = rendeles;
		this.cikkszam = cikkszam;
		this.exportkarton = exportkarton;
	}

	public String getRovidnev() {
		return rovidnev;
	}

	public void setRovidnev(String rovidnev) {
		this.rovidnev = rovidnev;
	}

	public String getRendeles() {
		return rendeles;
	}

	public void setRendeles(String rendeles) {
		this.rendeles = rendeles;
	}

	public String getCikkszam() {
		return cikkszam;
	}

	public void setCikkszam(String cikkszam) {
		this.cikkszam = cikkszam;
	}

	public Integer getExportkarton() {
		return exportkarton;
	}

	public void setExportkarton(Integer exportkarton) {
		this.exportkarton = exportkarton;
	}

}
