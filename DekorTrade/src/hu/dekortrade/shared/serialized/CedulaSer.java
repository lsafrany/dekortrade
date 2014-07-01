package hu.dekortrade.shared.serialized;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CedulaSer implements IsSerializable {

	private String cedula;

	private String rovidnev;

	private String status;

	private String elado;

	private Date datum;

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getRovidnev() {
		return rovidnev;
	}

	public void setRovidnev(String rovidnev) {
		this.rovidnev = rovidnev;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getElado() {
		return elado;
	}

	public void setElado(String elado) {
		this.elado = elado;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

}
