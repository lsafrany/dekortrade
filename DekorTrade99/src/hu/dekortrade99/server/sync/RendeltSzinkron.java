package hu.dekortrade99.server.sync;

import hu.dekortrade99.server.sync.RendeltcikkSzinkron;

import java.util.Date;
import java.util.List;

public class RendeltSzinkron {

	private String rovidnev;

	private String rendeles;

	private Date datum;

	List<RendeltcikkSzinkron> rendeltCikkszam = null;
	
	public RendeltSzinkron() {
		
	}
	
	public RendeltSzinkron(String rovidnev, String rendeles, Date datum, List<RendeltcikkSzinkron> rendeltCikkszam) {
		this.rovidnev = rovidnev;
		this.rendeles = rendeles;
		this.datum = datum;
		this.rendeltCikkszam = rendeltCikkszam;
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

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public List<RendeltcikkSzinkron> getRendeltCikkszam() {
		return rendeltCikkszam;
	}

	public void setRendeltCikkszam(List<RendeltcikkSzinkron> rendeltCikkszam) {
		this.rendeltCikkszam = rendeltCikkszam;
	}

}
