package hu.dekortrade.client.cash;

import hu.dekortrade.client.DisplayRequest;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;

public class Cash {

//	private CashLabels cashLabels = GWT.create(CashLabels.class);

	public Canvas get() {
		DisplayRequest.counterInit();

		HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

/*		final TabSet tabSet = new TabSet();
		tabSet.setTabBarPosition(Side.TOP);
		tabSet.setTabBarAlign(Side.LEFT);
		tabSet.setWidth100();
		tabSet.setHeight100();

		final Tab cedulaTab = new Tab(queryLabels.cedula());
		Cedula cedula = new Cedula();
		cedulaTab.setPane(cedula.get());
		tabSet.addTab(cedulaTab);

		middleLayout.addMember(tabSet);
*/
		return middleLayout;

	}
		
}
