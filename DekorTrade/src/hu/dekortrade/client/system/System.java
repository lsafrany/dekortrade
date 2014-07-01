package hu.dekortrade.client.system;

import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.cedula.Cedula;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class System {

	private SystemLabels systemLabels = GWT.create(SystemLabels.class);
	
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

		final Tab felhasznaloTab = new Tab(systemLabels.felhasznalok());
		Felhasznalo felhasznalo = new Felhasznalo();
		felhasznaloTab.setPane(felhasznalo.get());
		tabSet.addTab(felhasznaloTab);

		final Tab szinkronTab = new Tab(systemLabels.szinkron());
		Szinkron szinkron = new Szinkron();
		szinkronTab.setPane(szinkron.get());
		tabSet.addTab(szinkronTab);

		final Tab kosarTab = new Tab(systemLabels.kosar());
		KosarCikk kosarCikk = new KosarCikk();
		kosarTab.setPane(kosarCikk.get());
		tabSet.addTab(kosarTab);

		final Tab cedulaTab = new Tab(systemLabels.cedula());
		Cedula cedula = new Cedula();
		cedulaTab.setPane(cedula.get());
		tabSet.addTab(cedulaTab);

		middleLayout.addMember(tabSet);

		return middleLayout;

	}
}
