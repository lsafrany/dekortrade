package hu.dekortrade99.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Kosar {

	@Persistent
	private String rovidnev;

	@Persistent
	private String cikkszam;

	@Persistent
	private Integer exportkarton;

	public Kosar(String rovidnev, String cikkszam, Integer exportkarton) {
		this.rovidnev = rovidnev;
		this.cikkszam = cikkszam;
		this.exportkarton = exportkarton;
	}

	public String getRovidnev() {
		return rovidnev;
	}

	public void setRovidnev(String rovidnev) {
		this.rovidnev = rovidnev;
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
