package hu.dekortrade.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class EladasSer implements IsSerializable {

	private String cedula;

	private Integer exportkarton;

	private Integer kiskarton;

	private Integer darab;

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public Integer getExportkarton() {
		return exportkarton;
	}

	public void setExportkarton(Integer exportkarton) {
		this.exportkarton = exportkarton;
	}

	public Integer getKiskarton() {
		return kiskarton;
	}

	public void setKiskarton(Integer kiskarton) {
		this.kiskarton = kiskarton;
	}

	public Integer getDarab() {
		return darab;
	}

	public void setDarab(Integer darab) {
		this.darab = darab;
	}

}
