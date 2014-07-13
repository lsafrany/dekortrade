package hu.dekortrade.client.srendszer;

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
import com.smartgwt.client.widgets.layout.VLayout;

public class Szinkron {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private RendszerLabels systemLabels = GWT.create(RendszerLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	public Canvas get() {
		DisplayRequest.counterInit();

		VLayout middleLayout = new VLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		middleLayout.setStyleName("middle");

		HLayout szinkronLayout = new HLayout();
		szinkronLayout.setAlign(Alignment.CENTER);
		szinkronLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		szinkronLayout.setStyleName("middle");
		final IButton szinkronIButton = new IButton(systemLabels.szinkron());
		szinkronLayout.addMember(szinkronIButton);

		HLayout teljesszinkronLayout = new HLayout();
		teljesszinkronLayout.setAlign(Alignment.CENTER);
		teljesszinkronLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		teljesszinkronLayout.setStyleName("middle");
		final IButton teljesszinkronIButton = new IButton(
				systemLabels.teljesszinkron());
		teljesszinkronIButton.setWidth(200);
		teljesszinkronLayout.addMember(teljesszinkronIButton);

		middleLayout.addMember(szinkronLayout);
		middleLayout.addMember(teljesszinkronLayout);

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
											SC.say(systemLabels
													.feltoltottvevo()
													+ " "
													+ result.getUploadvevo()
													+ " "
													+ systemLabels
															.feltoltottcikkfotipus()
													+ " "
													+ result.getUploadcikkfotipus()
													+ " "
													+ systemLabels
															.feltoltottcikkaltipus()
													+ " "
													+ result.getUploadcikkaltipus()
													+ " "
													+ systemLabels
															.feltoltottcikk()
													+ " "
													+ result.getUploadcikk()
													+ " "
													+ systemLabels
															.feltoltottkep()
													+ " "
													+ result.getUploadkep()
													+ " "
													+ systemLabels
															.letoltottrendeles()
													+ " "
													+ result.getDownloadrendelt());
											szinkronIButton.setDisabled(false);
										}
									});

						}
					}
				});
			}
		});

		teljesszinkronIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask(systemLabels.teljesszinkronask() + " "
						+ commonLabels.sure(), new BooleanCallback() {
					public void execute(Boolean value) {
						if (value != null && value) {
							teljesszinkronIButton.setDisabled(true);
							DisplayRequest.startRequest();
							dekorTradeService
									.teljesszinkron(new AsyncCallback<String>() {
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
											teljesszinkronIButton
													.setDisabled(false);
										}

										public void onSuccess(String result) {
											DisplayRequest.serverResponse();
											SC.say(systemLabels
													.teljesszinksonok());
											teljesszinkronIButton
													.setDisabled(false);
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
