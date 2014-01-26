package hu.dekortrade.client.system;

import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.SzinkronSer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class Szinkron {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private SystemLabels systemLabels = GWT.create(SystemLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	public Canvas get() {
		DisplayRequest.counterInit();

		HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		middleLayout.setStyleName("middle");

		final IButton szinkronIButton = new IButton(systemLabels.szinkron());

		middleLayout.addMember(szinkronIButton);

		szinkronIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask(commonLabels.sure(), new BooleanCallback() {
					public void execute(Boolean value) {
						if (value != null && value) {
							szinkronIButton.setDisabled(true);
							DisplayRequest.startRequest();
							dekorTradeService
									.szinkron(new AsyncCallback<SzinkronSer>() {
										public void onFailure(Throwable caught) {
											DisplayRequest.serverResponse();
											if (caught instanceof SQLExceptionSer)
												SC.warn(commonLabels
														.server_sqlerror()
														+ " : "
														+ caught.getMessage());
											else
												SC.warn(commonLabels
														.server_error());
											szinkronIButton.setDisabled(false);
										}

										public void onSuccess(SzinkronSer result) {
											DisplayRequest.serverResponse();
											SC.say(
												systemLabels.feltoltottvevo() + " " + result.getUploadvevo() + "\n" +
												systemLabels.feltoltottcikk() + " " + result.getUploadcikk() + "\n" +
												systemLabels.feltoltottkep() + " " + result.getUploadkep() + "\n" +
												systemLabels.letoltottrendeles() + " " + result.getDownloadrendelt());
											szinkronIButton.setDisabled(false);
										}
									});

						}
					}
				});
			}
		});

		return middleLayout;

	}
}
