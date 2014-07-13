package hu.dekortrade.client.penztar;

import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.client.penztar.fizetes.Fizetes;
import hu.dekortrade.client.penztar.hazi.Hazi;
import hu.dekortrade.client.penztar.torlesztes.Torlesztes;
import hu.dekortrade.client.penztar.zaras.Zaras;
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

public class Penztar {

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	// private CashLabels cashLabels = GWT.create(CashLabels.class);

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

			if (UserInfo.menu.get(i).equals(Constants.MENU_PENZTAR_FIZETES)) {

				final Tab tab = new Tab(commonLabels.menu_penztar_fizetes());
				tabSet.addTab(tab);
				final Fizetes fizetes = new Fizetes();

				if (!selecTab) {
					tab.setPane(fizetes.get());
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(fizetes.get());
					}

				});
			}

			if (UserInfo.menu.get(i).equals(Constants.MENU_PENZTAR_TORLESZTES)) {

				final Tab tab = new Tab(commonLabels.menu_penztar_torlesztes());
				tabSet.addTab(tab);
				final Torlesztes torlesztes = new Torlesztes();

				if (!selecTab) {
					tab.setPane(torlesztes.get());
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(torlesztes.get());
					}

				});
			}

			if (UserInfo.menu.get(i).equals(Constants.MENU_PENZTAR_HAZI)) {

				final Tab tab = new Tab(commonLabels.menu_penztar_hazi());
				tabSet.addTab(tab);
				final Hazi hazi = new Hazi();

				if (!selecTab) {
					tab.setPane(hazi.get());
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(hazi.get());
					}

				});
			}

			if (UserInfo.menu.get(i).equals(Constants.MENU_PENZTAR_ZARAS)) {

				final Tab tab = new Tab(commonLabels.menu_penztar_zaras());
				tabSet.addTab(tab);
				final Zaras zaras = new Zaras();

				if (!selecTab) {
					tab.setPane(zaras.get());
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(zaras.get());
					}

				});
			}

		}

		middleLayout.addMember(tabSet);

		return middleLayout;

	}

}
