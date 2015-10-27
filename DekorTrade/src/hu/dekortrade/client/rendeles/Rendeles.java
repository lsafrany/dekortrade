package hu.dekortrade.client.rendeles;

import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.client.rendeles.elorendeles.Elorendeles;
import hu.dekortrade.client.rendeles.megrendeles.Megrendeles;
import hu.dekortrade.client.rendeles.veglegites.Veglegesites;
import hu.dekortrade.client.rendelles.internet.Internet;
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

public class Rendeles {

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	// private OrderLabels orderLabels = GWT.create(OrderLabels.class);

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

			if (UserInfo.menu.get(i).equals(Constants.MENU_RENDELES_INTERNET)) {

				final Tab tab = new Tab(commonLabels.menu_rendeles_internet());
				tabSet.addTab(tab);
				final Internet internet = new Internet();

				if (!selecTab) {
					tab.setPane(internet.get(null,null));
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(internet.get(null,null));
					}

				});
			}

			if (UserInfo.menu.get(i)
					.equals(Constants.MENU_RENDELES_ELORENDELES)) {

				final Tab tab = new Tab(
						commonLabels.menu_rendeles_elorendeles());
				tabSet.addTab(tab);
				final Elorendeles elorendel = new Elorendeles();

				if (!selecTab) {
					tab.setPane(elorendel.get());
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(elorendel.get());
					}

				});
			}

			if (UserInfo.menu.get(i).equals(
					Constants.MENU_RENDELES_VEGLEGESITES)) {

				final Tab tab = new Tab(
						commonLabels.menu_rendeles_veglegesites());
				tabSet.addTab(tab);
				final Veglegesites veglegesit = new Veglegesites();

				if (!selecTab) {
					tab.setPane(veglegesit.get());
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(veglegesit.get());
					}

				});
			}

			if (UserInfo.menu.get(i)
					.equals(Constants.MENU_RENDELES_MEGRENDELES)) {

				final Tab tab = new Tab(
						commonLabels.menu_rendeles_megrendeles());
				tabSet.addTab(tab);
				final Megrendeles megrendeles = new Megrendeles();

				if (!selecTab) {
					tab.setPane(megrendeles.get());
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(megrendeles.get());
					}

				});
			}

		}

		middleLayout.addMember(tabSet);

		return middleLayout;
	}
}
