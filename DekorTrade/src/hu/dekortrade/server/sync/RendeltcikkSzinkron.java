package hu.dekortrade.server.sync;

public class RendeltcikkSzinkron {

	private String rovidnev;

	private String rendeles;

	private String cikkszam;

	private Integer exportkarton;

	public RendeltcikkSzinkron() {
		
	}

	public RendeltcikkSzinkron(String rovidnev, String rendeles, String cikkszam,
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
