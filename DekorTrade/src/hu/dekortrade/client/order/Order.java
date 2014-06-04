package hu.dekortrade.client.order;

import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.order.internet.InternetOrder;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class Order {

	private OrderLabels orderLabels = GWT.create(OrderLabels.class);

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

		final Tab internetOrderTab = new Tab(orderLabels.internetorder());
		InternetOrder internetOrder = new InternetOrder();
		internetOrderTab.setPane(internetOrder.get());
		tabSet.addTab(internetOrderTab);

		middleLayout.addMember(tabSet);

		return middleLayout;
	}
}
