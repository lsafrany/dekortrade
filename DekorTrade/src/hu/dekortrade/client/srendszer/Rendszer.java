package hu.dekortrade.client.srendszer;

import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.UserInfo;
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

public class Rendszer {

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	// private SystemLabels systemLabels = GWT.create(SystemLabels.class);

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

			if (UserInfo.menu.get(i)
					.equals(Constants.MENU_RENDSZER_FELHASZNALO)) {

				final Tab tab = new Tab(
						commonLabels.menu_rendszer_felhasznalo());
				tabSet.addTab(tab);
				final Felhasznalo felhasznalo = new Felhasznalo();

				if (!selecTab) {
					tab.setPane(felhasznalo.get());
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(felhasznalo.get());
					}

				});
			}

			if (UserInfo.menu.get(i).equals(Constants.MENU_RENDSZER_SZINKRON)) {

				final Tab tab = new Tab(commonLabels.menu_rendszer_szinkron());
				tabSet.addTab(tab);
				final Szinkron szinkron = new Szinkron();

				if (!selecTab) {
					tab.setPane(szinkron.get());
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(szinkron.get());
					}

				});
			}

			if (UserInfo.menu.get(i).equals(Constants.MENU_RENDSZER_KOSAR)) {

				final Tab tab = new Tab(commonLabels.menu_rendszer_kosar());
				tabSet.addTab(tab);
				final Kosar kosar = new Kosar();

				if (!selecTab) {
					tab.setPane(kosar.get());
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(kosar.get());
					}

				});
			}

		}

		middleLayout.addMember(tabSet);

		return middleLayout;

	}
}
