package hu.dekortrade.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SzinkronSer implements IsSerializable {

	private int uploadvevo = 0;
	
	private int uploadcikk = 0;

	private int uploadkep = 0;

	private int downloadrendelt = 0;
	
	public int getUploadvevo() {
		return uploadvevo;
	}

	public void setUploadvevo(int uploadvevo) {
		this.uploadvevo = uploadvevo;
	}

	public int getUploadcikk() {
		return uploadcikk;
	}

	public void setUploadcikk(int uploadcikk) {
		this.uploadcikk = uploadcikk;
	}

	public int getUploadkep() {
		return uploadkep;
	}

	public void setUploadkep(int uploadkep) {
		this.uploadkep = uploadkep;
	}

	public int getDownloadrendelt() {
		return downloadrendelt;
	}

	public void setDownloadrendelt(int downloadrendelt) {
		this.downloadrendelt = downloadrendelt;
	}

}
