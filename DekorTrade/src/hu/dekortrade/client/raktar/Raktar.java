package hu.dekortrade.client.raktar;

import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.client.raktar.beszallitas.Beszallitas;
import hu.dekortrade.client.raktar.kesztlet.Keszlet;
import hu.dekortrade.client.raktar.kiadas.Kiadas;
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

public class Raktar {

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

			if (UserInfo.menu.get(i).equals(Constants.MENU_RAKTAR_BESZALLITAS)) {

				final Tab tab = new Tab(commonLabels.menu_raktar_beszallitas());
				tabSet.addTab(tab);
				final Beszallitas beszallitas = new Beszallitas();

				if (!selecTab) {
					tab.setPane(beszallitas.get());
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(beszallitas.get());
					}

				});
			}

			if (UserInfo.menu.get(i).equals(Constants.MENU_RAKTAR_KESZLET)) {

				final Tab tab = new Tab(commonLabels.menu_raktar_keszlet());
				tabSet.addTab(tab);
				final Keszlet keszlet = new Keszlet();

				if (!selecTab) {
					tab.setPane(keszlet.get(Constants.MENU_RAKTAR_KESZLET,null));
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(keszlet.get(Constants.MENU_RAKTAR_KESZLET,null));
					}

				});
			}

			if (UserInfo.menu.get(i).equals(Constants.MENU_RAKTAR_KIADAS)) {

				final Tab tab = new Tab(commonLabels.menu_raktar_kiadas());
				tabSet.addTab(tab);
				final Kiadas kiadas = new Kiadas();

				if (!selecTab) {
					tab.setPane(kiadas.get());
					tabSet.selectTab(0);
					selecTab = true;
				}

				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(TabSelectedEvent event) {

						tab.setPane(kiadas.get());
					}

				});
			}

		}

		middleLayout.addMember(tabSet);

		return middleLayout;
	}
}
