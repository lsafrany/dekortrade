package hu.dekortrade.shared.serialized;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserSer implements IsSerializable {

	private String userId = "";

	private String name = "";

	private ArrayList<TabPageSer> tabList = new ArrayList<TabPageSer>();

	private int defultTab = 0;

	private ArrayList<String> menu = new ArrayList<String>();

	private ArrayList<String> jog = new ArrayList<String>();
	
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

	public ArrayList<String> getMenu() {
		return menu;
	}

	public void setMenu(ArrayList<String> menu) {
		this.menu = menu;
	}

	public ArrayList<String> getJog() {
		return jog;
	}

	public void setJog(ArrayList<String> jog) {
		this.jog = jog;
	}

}
