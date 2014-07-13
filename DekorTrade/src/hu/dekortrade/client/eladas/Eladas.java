package hu.dekortrade.client.eladas;

import hu.dekortrade.client.DisplayRequest;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;

public class Eladas {

	private EladasLabels eladasLabels = GWT.create(EladasLabels.class);

	public Canvas get() {
		DisplayRequest.counterInit();

		final HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		Label fejlesztesLabel = new Label(eladasLabels.fejlesztes_alatt());

		middleLayout.addMember(fejlesztesLabel);

		return middleLayout;
	}

}
