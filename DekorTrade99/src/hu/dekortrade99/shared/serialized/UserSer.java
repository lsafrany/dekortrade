package hu.dekortrade99.shared.serialized;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserSer implements IsSerializable {

	private String userId = "";

	private String name = "";

	private boolean sysadmin = false;

	private ArrayList<TabPageSer> tabList = new ArrayList<TabPageSer>();

	private int defultTab = 0;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSysadmin() {
		return sysadmin;
	}

	public void setSysadmin(boolean sysadmin) {
		this.sysadmin = sysadmin;
	}

	public ArrayList<TabPageSer> getTabList() {
		return tabList;
	}

	public void setTabList(ArrayList<TabPageSer> tabList) {
		this.tabList = tabList;
	}

	public int getDefultTab() {
		return defultTab;
	}

	public void setDefultTab(int defultTab) {
		this.defultTab = defultTab;
	}

}
