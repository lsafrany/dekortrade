package hu.dekortrade.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.gwt.user.client.rpc.IsSerializable;

@PersistenceCapable
public class Jog implements IsSerializable {
	
	@Persistent
	private String rovidnev;

	@Persistent
	private String jog;

	public Jog(String rovidnev,String jog) {
		this.rovidnev = rovidnev;
		this.jog = jog;
	}

	public String getRovidnev() {
		return rovidnev;
	}

	public void setRovidnev(String rovidnev) {
		this.rovidnev = rovidnev;
	}

	public String getJog() {
		return jog;
	}

	public void setJog(String jog) {
		this.jog = jog;
	}
	
}

