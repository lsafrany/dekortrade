package hu.dekortrade.client.penztar.hazi;

import hu.dekortrade.client.DisplayRequest;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;

public class Hazi {

	private HaziLabels haziLabels = GWT.create(HaziLabels.class);

	public Canvas get() {
		DisplayRequest.counterInit();

		final HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		Label fejlesztesLabel = new Label(haziLabels.fejlesztes_alatt());

		middleLayout.addMember(fejlesztesLabel);

		return middleLayout;
	}

}
