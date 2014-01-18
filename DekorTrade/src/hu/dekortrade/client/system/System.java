package hu.dekortrade.client.system;

import hu.dekortrade.client.DisplayRequest;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;

public class System {

	public Canvas get() {
		DisplayRequest.counterInit();

		HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		Felhasznalo felhasznalo = new Felhasznalo();
		middleLayout.addMember(felhasznalo.get());

		Szinkron szinkron = new Szinkron();
		middleLayout.addMember(szinkron.get());

		return middleLayout;

	}
}
