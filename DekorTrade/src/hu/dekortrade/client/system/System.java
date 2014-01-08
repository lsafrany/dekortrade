package hu.dekortrade.client.system;

import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class System {

	private SystemLabels systemLabels = GWT
			.create(SystemLabels.class);

	private CommonLabels commonLabels = GWT
			.create(CommonLabels.class);
	
	private final VLayout middleLayout = new VLayout();

	public Canvas get() {
		DisplayRequest.counterInit();
		middleLayout.removeMembers(middleLayout.getMembers());
		middleLayout.setStyleName("middle");
		HLayout titleLayout = new HLayout();
		titleLayout.setAlign(Alignment.CENTER);
		titleLayout.setHeight("5%");
		Label titleLabel = new Label(systemLabels.order());
		titleLayout.addMember(titleLabel);
		titleLabel.setStyleName("page_label");
		middleLayout.addMember(titleLayout);

		final VLayout middle1Layout = new VLayout();
		middle1Layout.setAlign(Alignment.CENTER);

		Label loadLabel = new Label(commonLabels.loading());
		loadLabel.setAlign(Alignment.CENTER);
		middle1Layout.addMember(loadLabel);

		middleLayout.addMember(middle1Layout);

		return middleLayout;

	}
}
