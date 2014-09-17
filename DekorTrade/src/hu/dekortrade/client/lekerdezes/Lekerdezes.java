package hu.dekortrade.client.lekerdezes;

import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.client.lekerdezes.cedulak.Cedulak;
import hu.dekortrade.client.lekerdezes.torlestesek.Torlesztesek;
import hu.dekortrade.client.lekerdezes.zarasok.Zarasok;
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

public class Lekerdezes {

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	// private QueryLabels queryLabels = GWT.create(QueryLabels.class);

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

			if (UserInfo.menu.get(i).equals(Constants.MENU_LEKERDEZES_CEDULAK)) {

				final Tab tab = new Tab(commonLabels.menu_lekerdezes_cedulak());
				tabSet.addTab(tab);
				final Cedulak cedulak = new Cedulak();

				if (!selecTab) {
					tab.setPane(cedulak.get(null,
							Constants.MENU_LEKERDEZES_CEDULAK));
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(cedulak.get(null,
								Constants.MENU_LEKERDEZES_CEDULAK));
					}

				});
			}

			if (UserInfo.menu.get(i).equals(Constants.MENU_LEKERDEZES_TORLESZTESEK)) {

				final Tab tab = new Tab(commonLabels.menu_lekerdezes_torlesztesek());
				tabSet.addTab(tab);
				final Torlesztesek torlesztesek = new Torlesztesek();

				if (!selecTab) {
					tab.setPane(torlesztesek.get());
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(torlesztesek.get());
					}

				});
			}

			if (UserInfo.menu.get(i).equals(Constants.MENU_LEKERDEZES_ZARASOK)) {

				final Tab tab = new Tab(commonLabels.menu_lekerdezes_zarasok());
				tabSet.addTab(tab);
				final Zarasok zarasok = new Zarasok();

				if (!selecTab) {
					tab.setPane(zarasok.get());
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(zarasok.get());
					}

				});
			}

		}

		middleLayout.addMember(tabSet);

		return middleLayout;

	}

}
