package hu.dekortrade.client.order;

import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.client.order.finalize.FinalizeOrder;
import hu.dekortrade.client.order.internet.InternetOrder;
import hu.dekortrade.client.order.pre.PreOrder;
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

public class Order {

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

//	private OrderLabels orderLabels = GWT.create(OrderLabels.class);

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
					.equals(Constants.MENU_ORDER_INTERNET) ){

				final Tab tab = new Tab(commonLabels.menu_internetorder());
				tabSet.addTab(tab);
				final InternetOrder internetOrder = new InternetOrder();

				if (!selecTab) {
					tab.setPane(internetOrder.get(null));
					tabSet.selectTab(0);
					selecTab = true;	
				}
				
				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(
							TabSelectedEvent event) {

						tab.setPane(internetOrder.get(null));
					}

				});				
			}

			if (UserInfo.menu.get(i).equals(Constants.MENU_ORDER_PRE)) {

				final Tab tab = new Tab(commonLabels.menu_preorder());
				tabSet.addTab(tab);
				final PreOrder preOrder = new PreOrder();

				if (!selecTab) {
					tab.setPane(preOrder.get());
					tabSet.selectTab(0);
					selecTab = true;		
				}
				
				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(
							TabSelectedEvent event) {

						tab.setPane(preOrder.get());
					}

				});				
			}
			
			if (UserInfo.menu.get(i).equals(Constants.MENU_ORDER_FINALIZE)) {

				final Tab tab = new Tab(commonLabels.menu_finalizeorder());
				tabSet.addTab(tab);
				final FinalizeOrder finalizeOrder = new FinalizeOrder();

				if (!selecTab) {
					tab.setPane(finalizeOrder.get());
					tabSet.selectTab(0);
					selecTab = true;		
				}
				
				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(
							TabSelectedEvent event) {

						tab.setPane(finalizeOrder.get());
					}

				});				
			}

		}	

		middleLayout.addMember(tabSet);

		return middleLayout;
	}
}
