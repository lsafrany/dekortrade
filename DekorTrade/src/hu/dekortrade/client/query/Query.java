package hu.dekortrade.client.query;

import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.query.cedula.Cedula;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class Query {

	private QueryLabels queryLabels = GWT.create(QueryLabels.class);

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

		final Tab cedulaTab = new Tab(queryLabels.cedula());
		Cedula cedula = new Cedula();
		cedulaTab.setPane(cedula.get());
		tabSet.addTab(cedulaTab);

		middleLayout.addMember(tabSet);

		return middleLayout;

	}
		
}
