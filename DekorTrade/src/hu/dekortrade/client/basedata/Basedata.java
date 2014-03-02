package hu.dekortrade.client.basedata;

import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.basedata.cikktipus.Cikktipus;
import hu.dekortrade.client.basedata.cikktorzs.Ctorzs;
import hu.dekortrade.client.basedata.szallito.Szallito;
import hu.dekortrade.client.basedata.vevo.Vevo;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class Basedata {

	private BasedataLabels basedataLabels = GWT.create(BasedataLabels.class);

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

		final Tab szallitoTab = new Tab(basedataLabels.szallitok());
		Szallito szallito = new Szallito();
		szallitoTab.setPane(szallito.get());
		tabSet.addTab(szallitoTab);

		final Tab vevoTab = new Tab(basedataLabels.vevok());
		Vevo vevo = new Vevo();
		vevoTab.setPane(vevo.get());
		tabSet.addTab(vevoTab);

		final Tab cikktipusTab = new Tab(basedataLabels.cikktipusok());
		Cikktipus cikktipus = new Cikktipus();
		cikktipusTab.setPane(cikktipus.get());
		tabSet.addTab(cikktipusTab);

		final Tab ctorzsTab = new Tab(basedataLabels.cikkek());
		Ctorzs ctorzs = new Ctorzs();
		ctorzsTab.setPane(ctorzs.get(null));
		tabSet.addTab(ctorzsTab);

		middleLayout.addMember(tabSet);

		return middleLayout;

	}
}
