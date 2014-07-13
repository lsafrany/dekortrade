package hu.dekortrade.client.torzsadat.cikktipus;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.UploadSer;

import java.util.Random;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.events.ErrorEvent;
import com.smartgwt.client.data.events.HandleErrorHandler;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Cikktipus {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private CikktipusLabels cikktipusLabels = GWT.create(CikktipusLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	public Canvas get() {
		DisplayRequest.counterInit();

		HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		VLayout fotipusLayout = new VLayout();
		fotipusLayout.setWidth("30%");
		fotipusLayout.setDefaultLayoutAlign(Alignment.CENTER);
		fotipusLayout.setAlign(Alignment.CENTER);

		final CikkfotipusDataSource cikkfotipusDataSource = new CikkfotipusDataSource() {

			protected Object transformRequest(DSRequest dsRequest) {
				DisplayRequest.startRequest();
				return super.transformRequest(dsRequest);
			}

			protected void transformResponse(DSResponse response,
					DSRequest request, Object data) {
				DisplayRequest.serverResponse();
				super.transformResponse(response, request, data);
			}
		};

		cikkfotipusDataSource.addHandleErrorHandler(new HandleErrorHandler() {
			public void onHandleError(ErrorEvent event) {

				if (event.getResponse().getStatus() == DSResponse.STATUS_FAILURE) {
					if (event.getResponse().getAttribute(
							ClientConstants.SERVER_ERROR) != null)
						SC.warn(commonLabels.server_error());
					else if (event.getResponse().getAttribute(
							ClientConstants.SERVER_SQLERROR) != null)
						if (event.getResponse()
								.getAttribute(ClientConstants.SERVER_SQLERROR)
								.equals(Constants.EXISTSID)) {
							SC.warn(commonLabels.existingid());
						} else {
							SC.warn(commonLabels.server_sqlerror()
									+ " : "
									+ event.getResponse().getAttribute(
											ClientConstants.SERVER_SQLERROR));
						}
					event.cancel();
				}
			}
		});

		final ListGrid cikkfotipusGrid = new ListGrid();
		cikkfotipusGrid.setTitle(cikktipusLabels.fotipusok());
		cikkfotipusGrid.setWidth("80%");
		cikkfotipusGrid.setShowHeaderContextMenu(false);
		cikkfotipusGrid.setShowHeaderMenuButton(false);
		cikkfotipusGrid.setCanSort(true);
		cikkfotipusGrid.setSortField(CikktipusConstants.CIKKFOTIPUS_KOD);
		cikkfotipusGrid.setShowAllRecords(true);
		cikkfotipusGrid.setDataSource(cikkfotipusDataSource);
		cikkfotipusGrid.setAutoFetchData(true);

		ListGridField fonevGridField = new ListGridField(
				CikktipusConstants.CIKKFOTIPUS_NEV);
		fonevGridField.setWidth("100%");

		cikkfotipusGrid.setFields(fonevGridField);

		HLayout fobuttonsLayout = new HLayout();
		fobuttonsLayout.setHeight("3%");
		fobuttonsLayout.setWidth("80%");

		HLayout foaddButtonLayout = new HLayout();
		foaddButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		IButton foaddButton = new IButton(commonLabels.add());
		foaddButtonLayout.setAlign(Alignment.CENTER);
		foaddButtonLayout.addMember(foaddButton);

		HLayout fomodifyButtonLayout = new HLayout();
		fomodifyButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		fomodifyButtonLayout.setAlign(Alignment.CENTER);
		final IButton fomodifyButton = new IButton(commonLabels.modify());
		fomodifyButton.disable();
		fomodifyButtonLayout.addMember(fomodifyButton);

		fobuttonsLayout.addMember(foaddButtonLayout);
		fobuttonsLayout.addMember(fomodifyButtonLayout);

		fotipusLayout.addMember(cikkfotipusGrid);
		fotipusLayout.addMember(fobuttonsLayout);

		final VLayout fotipusKepLayout = new VLayout();
		fotipusKepLayout.setWidth("20%");
		fotipusKepLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		fotipusKepLayout.setAlign(Alignment.CENTER);

		final HLayout fotipusImgLayout = new HLayout();
		fotipusImgLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		fotipusImgLayout.setAlign(Alignment.CENTER);

		HLayout fokepbuttonsLayout = new HLayout();
		fokepbuttonsLayout.setHeight("3%");
		fokepbuttonsLayout.setWidth("80%");

		HLayout foloadButtonLayout = new HLayout();
		foloadButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton foloadButton = new IButton(cikktipusLabels.kepfeltoltes());
		foloadButton.setDisabled(true);
		foloadButtonLayout.setAlign(Alignment.CENTER);
		foloadButtonLayout.addMember(foloadButton);

		fokepbuttonsLayout.addMember(foloadButtonLayout);

		fotipusKepLayout.addMember(fotipusImgLayout);
		fotipusKepLayout.addMember(fokepbuttonsLayout);

		VLayout altipusLayout = new VLayout();
		altipusLayout.setWidth("30%");
		altipusLayout.setDefaultLayoutAlign(Alignment.CENTER);
		altipusLayout.setAlign(Alignment.CENTER);

		final CikkaltipusDataSource cikkaltipusDataSource = new CikkaltipusDataSource() {

			protected Object transformRequest(DSRequest dsRequest) {
				DisplayRequest.startRequest();
				return super.transformRequest(dsRequest);
			}

			protected void transformResponse(DSResponse response,
					DSRequest request, Object data) {
				DisplayRequest.serverResponse();
				super.transformResponse(response, request, data);
			}
		};

		cikkaltipusDataSource.addHandleErrorHandler(new HandleErrorHandler() {
			public void onHandleError(ErrorEvent event) {

				if (event.getResponse().getStatus() == DSResponse.STATUS_FAILURE) {
					if (event.getResponse().getAttribute(
							ClientConstants.SERVER_ERROR) != null)
						SC.warn(commonLabels.server_error());
					else if (event.getResponse().getAttribute(
							ClientConstants.SERVER_SQLERROR) != null)
						if (event.getResponse()
								.getAttribute(ClientConstants.SERVER_SQLERROR)
								.equals(Constants.EXISTSID)) {
							SC.warn(commonLabels.existingid());
						} else {
							SC.warn(commonLabels.server_sqlerror()
									+ " : "
									+ event.getResponse().getAttribute(
											ClientConstants.SERVER_SQLERROR));
						}
					event.cancel();
				}
			}
		});

		final ListGrid cikkaltipusGrid = new ListGrid();
		cikkaltipusGrid.setTitle(cikktipusLabels.altipusok());
		cikkaltipusGrid.setWidth("80%");
		cikkaltipusGrid.setShowHeaderContextMenu(false);
		cikkaltipusGrid.setShowHeaderMenuButton(false);
		cikkaltipusGrid.setCanSort(true);
		cikkaltipusGrid.setSortField(CikktipusConstants.CIKKALTIPUS_KOD);
		cikkaltipusGrid.setShowAllRecords(true);
		cikkaltipusGrid.setDataSource(cikkaltipusDataSource);

		ListGridField alnevGridField = new ListGridField(
				CikktipusConstants.CIKKFOTIPUS_NEV);
		alnevGridField.setWidth("100%");

		cikkfotipusGrid.setFields(fonevGridField);

		HLayout albuttonsLayout = new HLayout();
		albuttonsLayout.setHeight("3%");
		albuttonsLayout.setWidth("80%");

		HLayout aladdButtonLayout = new HLayout();
		aladdButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton aladdButton = new IButton(commonLabels.add());
		aladdButtonLayout.setAlign(Alignment.CENTER);
		aladdButton.disable();
		aladdButtonLayout.addMember(aladdButton);

		HLayout almodifyButtonLayout = new HLayout();
		almodifyButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		almodifyButtonLayout.setAlign(Alignment.CENTER);
		final IButton almodifyButton = new IButton(commonLabels.modify());
		almodifyButton.disable();
		almodifyButtonLayout.addMember(almodifyButton);

		albuttonsLayout.addMember(aladdButtonLayout);
		albuttonsLayout.addMember(almodifyButtonLayout);

		altipusLayout.addMember(cikkaltipusGrid);
		altipusLayout.addMember(albuttonsLayout);

		final VLayout altipusKepLayout = new VLayout();
		altipusKepLayout.setWidth("20%");
		altipusKepLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		altipusKepLayout.setAlign(Alignment.CENTER);

		final HLayout altipusImgLayout = new HLayout();
		altipusImgLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		altipusImgLayout.setAlign(Alignment.CENTER);

		HLayout alkepbuttonsLayout = new HLayout();
		alkepbuttonsLayout.setHeight("3%");
		alkepbuttonsLayout.setWidth("80%");

		HLayout alloadButtonLayout = new HLayout();
		alloadButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton alloadButton = new IButton(cikktipusLabels.kepfeltoltes());
		alloadButton.setDisabled(true);
		alloadButtonLayout.setAlign(Alignment.CENTER);
		alloadButtonLayout.addMember(alloadButton);

		alkepbuttonsLayout.addMember(alloadButtonLayout);

		altipusKepLayout.addMember(altipusImgLayout);
		altipusKepLayout.addMember(alkepbuttonsLayout);

		middleLayout.addMember(fotipusLayout);
		middleLayout.addMember(fotipusKepLayout);
		middleLayout.addMember(altipusLayout);
		middleLayout.addMember(altipusKepLayout);

		cikkfotipusGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				fomodifyButton.setDisabled(false);
				aladdButton.setDisabled(false);
				almodifyButton.setDisabled(true);
				foloadButton.setDisabled(false);
				alloadButton.setDisabled(true);
				Criteria criteria = new Criteria();
				criteria.setAttribute(
						CikktipusConstants.CIKKALTIPUS_FOKOD,
						cikkfotipusGrid.getSelectedRecord().getAttribute(
								CikktipusConstants.CIKKFOTIPUS_KOD));

				cikkaltipusGrid.fetchData(criteria);

				fotipusImgLayout.removeMembers(fotipusImgLayout.getMembers());
				Random generator = new Random();
				final String random = Double.toString(generator.nextDouble());
				final Img fotipusImg = new Img(GWT.getModuleBaseURL()
						+ "download?fotipus="
						+ cikkfotipusGrid.getSelectedRecord().getAttribute(
								CikktipusConstants.CIKKFOTIPUS_KOD)
						+ "&random=" + random, 250, 150);
				fotipusImgLayout.addMember(fotipusImg);

				altipusImgLayout.removeMembers(altipusImgLayout.getMembers());
			}
		});

		foaddButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				cikkfotipusEdit(cikkfotipusDataSource, cikkfotipusGrid,
						Boolean.TRUE);
			}
		});

		fomodifyButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				cikkfotipusEdit(cikkfotipusDataSource, cikkfotipusGrid,
						Boolean.FALSE);
			}
		});

		cikkaltipusGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				alloadButton.setDisabled(false);
				altipusImgLayout.removeMembers(altipusImgLayout.getMembers());
				Random generator = new Random();
				final String random = Double.toString(generator.nextDouble());
				final Img altipusImg = new Img(GWT.getModuleBaseURL()
						+ "download?altipus="
						+ cikkaltipusGrid.getSelectedRecord().getAttribute(
								CikktipusConstants.CIKKALTIPUS_KOD)
						+ "&random=" + random, 250, 150);
				altipusImgLayout.addMember(altipusImg);

			}
		});

		foloadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				upload(cikkfotipusGrid.getSelectedRecord().getAttribute(
						CikktipusConstants.CIKKFOTIPUS_KOD),
						null,
						cikkfotipusGrid.getSelectedRecord().getAttribute(
								CikktipusConstants.CIKKFOTIPUS_NEV),
						fotipusImgLayout);
			}
		});

		alloadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				upload(null,
						cikkaltipusGrid.getSelectedRecord().getAttribute(
								CikktipusConstants.CIKKALTIPUS_KOD),
						cikkaltipusGrid.getSelectedRecord().getAttribute(
								CikktipusConstants.CIKKALTIPUS_NEV),
						altipusImgLayout);
			}
		});

		return middleLayout;

	}

	void cikkfotipusEdit(CikkfotipusDataSource dataSource, ListGrid listGrid,
			boolean uj) {

		final Window winModal = new Window();
		winModal.setWidth(400);
		winModal.setHeight(200);
		winModal.setTitle(cikktipusLabels.fotipus());
		winModal.setShowMinimizeButton(false);
		winModal.setShowCloseButton(false);
		winModal.setIsModal(true);
		winModal.setShowModalMask(true);
		winModal.centerInPage();

		final DynamicForm editForm = new DynamicForm();
		editForm.setNumCols(2);
		editForm.setColWidths("15%", "*");
		editForm.setDataSource(dataSource);
		editForm.setUseAllDataSourceFields(true);

		if (uj)
			editForm.editNewRecord();
		else {
			editForm.editSelectedData(listGrid);
		}

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		buttonsLayout.setWidth("100%");

		HLayout saveLayout = new HLayout();
		saveLayout.setAlign(Alignment.CENTER);
		saveLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		IButton saveIButton = new IButton(commonLabels.save());
		saveIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				editForm.saveData(new DSCallback() {
					public void execute(DSResponse response, Object rawData,
							DSRequest request) {
						if (response.getStatus() == DSResponse.STATUS_SUCCESS)
							winModal.destroy();
					}
				});
			}
		});
		saveLayout.addMember(saveIButton);
		buttonsLayout.addMember(saveLayout);

		HLayout cancelLayout = new HLayout();
		cancelLayout.setAlign(Alignment.CENTER);
		cancelLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		IButton cancelIButton = new IButton(commonLabels.cancel());
		cancelIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				winModal.destroy();
			}
		});
		cancelLayout.addMember(cancelIButton);
		buttonsLayout.addMember(cancelLayout);

		winModal.addItem(editForm);
		winModal.addItem(buttonsLayout);
		winModal.show();

	}

	void upload(final String fokod, final String alkod, String title,
			final HLayout imgLayout) {

		final Window winModal = new Window();
		winModal.setWidth(600);
		winModal.setHeight(150);
		winModal.setTitle(cikktipusLabels.kepfeltoltes());
		winModal.setShowMinimizeButton(false);
		winModal.setShowCloseButton(false);
		winModal.setIsModal(true);
		winModal.setShowModalMask(true);
		winModal.centerInPage();

		final HLayout formLayout = new HLayout();
		formLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		formLayout.setAlign(Alignment.CENTER);
		formLayout.setWidth("100%");
		formLayout.setHeight("30%");

		final DynamicForm uploadForm = new DynamicForm();
		uploadForm.setEncoding(Encoding.MULTIPART);
		uploadForm.setMethod(FormMethod.POST);
		uploadForm.setWidth100();
		uploadForm.setNumCols(2);
		uploadForm.setColWidths("40%", "*");
		uploadForm.setTarget("fileUploadFrame");
		Random generator = new Random();
		final String random = Double.toString(generator.nextDouble());
		if (fokod != null)
			uploadForm.setAction(GWT.getModuleBaseURL() + "uploadfokod?kod="
					+ fokod + "&random=" + random);
		else
			uploadForm.setAction(GWT.getModuleBaseURL() + "uploadalkod?kod="
					+ alkod + "&random=" + random);

		final UploadItem uploadItem = new UploadItem();
		uploadItem.setWidth(300);
		uploadItem.setName("kep");
		uploadItem.setTitle(title);
		uploadItem.setRequired(true);

		uploadForm.setFields(uploadItem);

		formLayout.addMember(uploadForm);

		final HLayout labelLayout = new HLayout();
		labelLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		labelLayout.setAlign(Alignment.CENTER);
		labelLayout.setWidth("100%");
		labelLayout.setHeight("20%");

		final Label label = new Label();
		label.setWidth("100%");
		label.setContents("");
		label.setAlign(Alignment.CENTER);

		labelLayout.addMember(label);

		final HLayout formButtonsLayout = new HLayout();
		formButtonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		formButtonsLayout.setAlign(Alignment.CENTER);
		formButtonsLayout.setWidth("100%");

		HLayout saveLayout = new HLayout();
		saveLayout.setAlign(Alignment.CENTER);
		saveLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		IButton saveIButton = new IButton(commonLabels.save());

		HLayout cancelLayout = new HLayout();
		cancelLayout.setAlign(Alignment.CENTER);
		cancelLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		IButton cancelIButton = new IButton(commonLabels.cancel());

		saveLayout.addMember(saveIButton);
		cancelLayout.addMember(cancelIButton);
		formButtonsLayout.addMember(saveLayout);
		formButtonsLayout.addMember(cancelLayout);

		winModal.addItem(formLayout);
		winModal.addItem(labelLayout);
		winModal.addItem(formButtonsLayout);

		saveIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if ((uploadItem.getValueAsString() != null)
						&& (uploadItem.getValueAsString().contains(".jpg"))) {
					dekorTradeService
							.initUploadFileStatus(new AsyncCallback<String>() {
								public void onFailure(Throwable caught) {

								}

								public void onSuccess(String result) {
									uploadForm.submitForm();
									uploadForm.setDisabled(true);
									formButtonsLayout.setDisabled(true);
									label.setContents(cikktipusLabels.toltes());
									new Timer() {
										int timesWaited = 0;
										int maxTimes = 100;

										public void run() {
											timesWaited++;
											if (timesWaited >= maxTimes) {
												uploadForm.setDisabled(false);
												formButtonsLayout
														.setDisabled(false);
												label.setContents("");
												SC.say(cikktipusLabels
														.idotullepes());
											} else {
												dekorTradeService
														.getUploadFileStatus(new AsyncCallback<UploadSer>() {
															public void onFailure(
																	Throwable caught) {
																winModal.destroy();
																SC.say(cikktipusLabels
																		.tolteshiba());
															}

															public void onSuccess(
																	UploadSer result) {
																if (result
																		.getStatus()
																		.equals(Constants.LOADING)) {
																	schedule(ClientConstants.PROGRESS_SCHEDULE);
																} else {
																	if (result
																			.getStatus()
																			.equals(Constants.ERROR)) {
																		uploadForm
																				.setDisabled(false);
																		formButtonsLayout
																				.setDisabled(false);
																		label.setContents("");
																		if (result
																				.getError()
																				.equals(Constants.FILE_SAVE_ERROR))
																			SC.say(cikktipusLabels
																					.tolteshiba());
																		else
																			SC.say(cikktipusLabels
																					.tulnagyfile());
																	} else {
																		imgLayout
																				.removeMembers(imgLayout
																						.getMembers());
																		Random generator = new Random();
																		final String random = Double
																				.toString(generator
																						.nextDouble());
																		if (fokod != null) {
																			final Img fotipusImg = new Img(
																					GWT.getModuleBaseURL()
																							+ "download?fotipus="
																							+ fokod
																							+ "&random="
																							+ random,
																					250,
																					150);
																			imgLayout
																					.addMember(fotipusImg);
																		} else {
																			final Img altipusImg = new Img(
																					GWT.getModuleBaseURL()
																							+ "download?altipus="
																							+ alkod
																							+ "&random="
																							+ random,
																					250,
																					150);
																			imgLayout
																					.addMember(altipusImg);
																		}
																		imgLayout
																				.markForRedraw();
																		winModal.destroy();
																	}
																}
															}
														});
											}
										}
									}.schedule(ClientConstants.PROGRESS_SCHEDULE);

								}
							});
				} else
					SC.say(cikktipusLabels.jpg());
			}
		});

		cancelIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				winModal.destroy();
			}
		});

		winModal.show();

	}

}
