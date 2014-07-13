package hu.dekortrade.client.raktar.kesztlet;

import hu.dekortrade.client.DisplayRequest;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;

public class Keszlet {

	private KeszletLabels keszletLabels = GWT.create(KeszletLabels.class);

	public Canvas get() {
		DisplayRequest.counterInit();

		final HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		Label fejlesztesLabel = new Label(keszletLabels.fejlesztes_alatt());

		middleLayout.addMember(fejlesztesLabel);

		return middleLayout;
	}

}
