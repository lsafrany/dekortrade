package hu.dekortrade.client.torzsadat;

import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.client.torzsadat.cikktipus.Cikktipus;
import hu.dekortrade.client.torzsadat.cikktorzs.Cikktorzs;
import hu.dekortrade.client.torzsadat.gyarto.Gyarto;
import hu.dekortrade.client.torzsadat.vevo.Vevo;
import hu.dekortrade.shared.Constants;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class Torzsadat {

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	// private BasedataLabels basedataLabels = GWT.create(BasedataLabels.class);

	public Canvas get() {
		DisplayRequest.counterInit();

		HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		final TabSet tabSet = new TabSet();
		tabSet.setTabBarPosition(Side.TOP);
		tabSet.setTabBarAlign(Side.LEFT);
		tabSet.setWidth100();
		tabSet.setHeight100();

		boolean selecTab = false;
		for (int i = 0; i < UserInfo.menu.size(); i++) {

			if (UserInfo.menu.get(i).equals(Constants.MENU_TORZSADAT_GYARTO)) {

				final Tab tab = new Tab(commonLabels.menu_torzsadat_gyarto());
				tabSet.addTab(tab);
				final Gyarto gyarto = new Gyarto();

				if (!selecTab) {
					tab.setPane(gyarto.get());
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(gyarto.get());
					}

				});
			}

			if (UserInfo.menu.get(i).equals(Constants.MENU_TORZSADAT_VEVO)) {

				final Tab tab = new Tab(commonLabels.menu_torzsadat_vevo());
				tabSet.addTab(tab);
				final Vevo vevo = new Vevo();

				if (!selecTab) {
					tab.setPane(vevo.get(Constants.MENU_TORZSADAT_VEVO));
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(vevo.get(Constants.MENU_TORZSADAT_VEVO));
					}

				});
			}

			if (UserInfo.menu.get(i).equals(Constants.MENU_TORZSADAT_CIKKTIPUS)) {

				final Tab tab = new Tab(commonLabels.menu_torzsadat_cikktipus());
				tabSet.addTab(tab);
				final Cikktipus cikktipus = new Cikktipus();

				if (!selecTab) {
					tab.setPane(cikktipus.get());
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(cikktipus.get());
					}

				});
			}

			if (UserInfo.menu.get(i).equals(Constants.MENU_TORZSADAT_CIKKTORZS)) {

				final Tab tab = new Tab(commonLabels.menu_torzsadat_cikktorzs());
				tabSet.addTab(tab);
				final Cikktorzs cikktorzs = new Cikktorzs();

				if (!selecTab) {
					tab.setPane(cikktorzs.get(null,null,null));
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(cikktorzs.get(null,null,null));
					}

				});
			}

		}

		middleLayout.addMember(tabSet);

		return middleLayout;

	}
}
