package hu.dekortrade.client.query;

import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.client.query.cedula.Cedula;
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

public class Query {

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

//	private QueryLabels queryLabels = GWT.create(QueryLabels.class);

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
					.equals(Constants.MENU_QUERY_TICKET) ){

				final Tab tab = new Tab(commonLabels.menu_cedula());
				tabSet.addTab(tab);
				final Cedula cedula = new Cedula();

				if (!selecTab) {
					tab.setPane(cedula.get(null,Constants.MENU_QUERY_TICKET));
					tabSet.selectTab(0);
					selecTab = true;	
				}
				
				tab.addTabSelectedHandler(new TabSelectedHandler() {

					@Override
					public void onTabSelected(
							TabSelectedEvent event) {

						tab.setPane(cedula.get(null,Constants.MENU_QUERY_TICKET));
					}

				});				
			}
		}
		
		middleLayout.addMember(tabSet);

		return middleLayout;

	}
		
}
