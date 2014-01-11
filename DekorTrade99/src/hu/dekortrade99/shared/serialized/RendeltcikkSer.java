package hu.dekortrade99.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RendeltcikkSer implements IsSerializable {
	
	private String rovidnev;

	private String rendeles;

	private String cikkszam;

	private Integer exportkarton;
	
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
