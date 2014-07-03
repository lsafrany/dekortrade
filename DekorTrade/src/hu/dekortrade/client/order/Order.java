package hu.dekortrade.client.order;

import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.order.finalize.FinalizeOrder;
import hu.dekortrade.client.order.internet.InternetOrder;
import hu.dekortrade.client.order.pre.PreOrder;

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
		internetOrderTab.setPane(internetOrder.get(null));
		tabSet.addTab(internetOrderTab);

		final Tab preOrderTab = new Tab(orderLabels.preorder());
		PreOrder preOrder = new PreOrder();
		preOrderTab.setPane(preOrder.get());
		tabSet.addTab(preOrderTab);

		final Tab finalizeOrderTab = new Tab(orderLabels.finalizeorder());
		FinalizeOrder finalizeOrder = new FinalizeOrder();
		finalizeOrderTab.setPane(finalizeOrder.get());
		tabSet.addTab(finalizeOrderTab);

		middleLayout.addMember(tabSet);

		return middleLayout;
	}
}
