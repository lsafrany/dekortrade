package hu.dekortrade.client.cash;

import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.client.cash.close.CashClose;
import hu.dekortrade.client.cash.pay.CashPay;
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

public class Cash {

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

//	private CashLabels cashLabels = GWT.create(CashLabels.class);

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
					.equals(Constants.MENU_CASH_PAY) ){

				final Tab tab = new Tab(commonLabels.menu_pay());
				tabSet.addTab(tab);
				final CashPay cashPay = new CashPay();

				if (!selecTab) {
					tab.setPane(cashPay.get());
					tabSet.selectTab(0);
					selecTab = true;	
				}
				
				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(
							TabSelectedEvent event) {

						tab.setPane(cashPay.get());
					}

				});				
			}
			
			if (UserInfo.menu.get(i)
					.equals(Constants.MENU_CASH_CLOSE) ){

				final Tab tab = new Tab(commonLabels.menu_close());
				tabSet.addTab(tab);
				final CashClose cashClose = new CashClose();

				if (!selecTab) {
					tab.setPane(cashClose.get());
					tabSet.selectTab(0);
					selecTab = true;	
				}
				
				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(
							TabSelectedEvent event) {

						tab.setPane(cashClose.get());
					}

				});				
			}

		}
		
		middleLayout.addMember(tabSet);

		return middleLayout;

	}
		
}
