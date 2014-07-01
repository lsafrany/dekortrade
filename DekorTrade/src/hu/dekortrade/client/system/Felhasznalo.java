package hu.dekortrade.client.system;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.events.ErrorEvent;
import com.smartgwt.client.data.events.HandleErrorHandler;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Felhasznalo {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private SystemLabels systemLabels = GWT.create(SystemLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	public Canvas get() {
		DisplayRequest.counterInit();

		HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		VLayout felhasznaloLayout = new VLayout();
		felhasznaloLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final FelhasznaloDataSource felhasznaloDataSource = new FelhasznaloDataSource() {

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

		felhasznaloDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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

		final ListGrid felhasznaloGrid = new ListGrid();
		felhasznaloGrid.setTitle(systemLabels.felhasznalok());
		felhasznaloGrid.setWidth("80%");
		felhasznaloGrid.setShowHeaderContextMenu(false);
		felhasznaloGrid.setShowHeaderMenuButton(false);
		felhasznaloGrid.setCanSort(false);
		felhasznaloGrid.setShowAllRecords(true);
		felhasznaloGrid.setDataSource(felhasznaloDataSource);
		felhasznaloGrid.setAutoFetchData(true);

		ListGridField rovidnevGridField = new ListGridField(
				SystemConstants.FELHASZNALO_ROVIDNEV);
		rovidnevGridField.setWidth("30%");

		ListGridField nevGridField = new ListGridField(
				SystemConstants.FELHASZNALO_NEV);

		ListGridField menuGridField = new ListGridField(
				SystemConstants.FELHASZNALO_MENU);
		menuGridField.setWidth("20%");

		felhasznaloGrid.setFields(rovidnevGridField, nevGridField,
				menuGridField);

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("80%");

		HLayout addButtonLayout = new HLayout();
		addButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		IButton addButton = new IButton(commonLabels.add());
		addButtonLayout.setAlign(Alignment.CENTER);
		addButtonLayout.addMember(addButton);

		HLayout modifyButtonLayout = new HLayout();
		modifyButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		modifyButtonLayout.setAlign(Alignment.CENTER);
		final IButton modifyButton = new IButton(commonLabels.modify());
		modifyButton.disable();
		modifyButtonLayout.addMember(modifyButton);

		HLayout jelszoButtonLayout = new HLayout();
		jelszoButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		jelszoButtonLayout.setAlign(Alignment.CENTER);
		final IButton jelszoButton = new IButton(systemLabels.jelszo());
		jelszoButton.disable();
		jelszoButtonLayout.addMember(jelszoButton);

		HLayout deleteButtonLayout = new HLayout();
		deleteButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		deleteButtonLayout.setAlign(Alignment.CENTER);
		final IButton deleteButton = new IButton(commonLabels.delete());
		deleteButton.disable();
		deleteButtonLayout.addMember(deleteButton);

		buttonsLayout.addMember(addButtonLayout);
		buttonsLayout.addMember(modifyButtonLayout);
		buttonsLayout.addMember(jelszoButtonLayout);
		buttonsLayout.addMember(deleteButtonLayout);

		felhasznaloLayout.addMember(felhasznaloGrid);
		felhasznaloLayout.addMember(buttonsLayout);

		VLayout jogLayout = new VLayout();
		jogLayout.setWidth("30%");
		jogLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final JogDataSource jogDataSource = new JogDataSource() {

			protected Object transformRequest(DSRequest dsRequest) {
				dsRequest.setAttribute(
						SystemConstants.FELHASZNALO_ROVIDNEV,
						felhasznaloGrid.getSelectedRecord().getAttribute(
								SystemConstants.FELHASZNALO_ROVIDNEV));
				DisplayRequest.startRequest();
				return super.transformRequest(dsRequest);
			}

			protected void transformResponse(DSResponse response,
					DSRequest request, Object data) {
				DisplayRequest.serverResponse();
				super.transformResponse(response, request, data);
			}
		};

		jogDataSource.addHandleErrorHandler(new HandleErrorHandler() {
			public void onHandleError(ErrorEvent event) {

				if (event.getResponse().getStatus() == DSResponse.STATUS_FAILURE) {
					if (event.getResponse().getAttribute(
							ClientConstants.SERVER_ERROR) != null)
						SC.warn(commonLabels.server_error());
					else if (event.getResponse().getAttribute(
							ClientConstants.SERVER_SQLERROR) != null) {
						SC.warn(commonLabels.server_sqlerror()
								+ " : "
								+ event.getResponse().getAttribute(
										ClientConstants.SERVER_SQLERROR));
					}
					event.cancel();
				}
			}
		});

		final ListGrid jogGrid = new ListGrid();
		jogGrid.setTitle(systemLabels.jogok());
		jogGrid.setWidth("80%");
		jogGrid.setShowHeaderContextMenu(false);
		jogGrid.setShowHeaderMenuButton(false);
		jogGrid.setCanSort(false);
		jogGrid.setShowAllRecords(true);
		jogGrid.setDataSource(jogDataSource);

		ListGridField jognevGridField = new ListGridField(
				SystemConstants.JOG_NEV);
		jognevGridField.setWidth("30%");

		ListGridField jogGridField = new ListGridField(SystemConstants.JOG_JOG);

		jogGrid.setFields(jognevGridField, jogGridField);

		HLayout buttons1Layout = new HLayout();
		buttons1Layout.setHeight("3%");
		buttons1Layout.setWidth("80%");

		HLayout modify1ButtonLayout = new HLayout();
		modify1ButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton modify1Button = new IButton(commonLabels.modify());
		modify1Button.setDisabled(true);
		modify1ButtonLayout.setAlign(Alignment.CENTER);
		modify1ButtonLayout.addMember(modify1Button);

		buttons1Layout.addMember(modify1ButtonLayout);

		jogLayout.addMember(jogGrid);
		jogLayout.addMember(buttons1Layout);

		middleLayout.addMember(felhasznaloLayout);
		middleLayout.addMember(jogLayout);

		felhasznaloGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				modifyButton.setDisabled(false);
				jelszoButton.setDisabled(false);
				deleteButton.setDisabled(false);
				jogGrid.invalidateCache();
				jogGrid.fetchData();
				modify1Button.setDisabled(true);
			}
		});

		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				felhasznaloEdit(felhasznaloDataSource, felhasznaloGrid,
						jogGrid, Boolean.TRUE);
			}
		});

		modifyButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				felhasznaloEdit(felhasznaloDataSource, felhasznaloGrid,
						jogGrid, Boolean.FALSE);
			}
		});

		jelszoButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask(commonLabels.sure(), new BooleanCallback() {
					public void execute(Boolean value) {
						if (value != null && value) {
							jelszoButton.setDisabled(true);
							DisplayRequest.startRequest();
							dekorTradeService
									.setFelhasznaloJelszo(
											felhasznaloGrid
													.getSelectedRecord()
													.getAttribute(
															SystemConstants.FELHASZNALO_ROVIDNEV),
											new AsyncCallback<String>() {
												public void onFailure(
														Throwable caught) {
													DisplayRequest
															.serverResponse();
													if (caught instanceof SQLExceptionSer)
														SC.warn(commonLabels
																.server_sqlerror()
																+ " : "
																+ caught.getMessage());
													else
														SC.warn(commonLabels
																.server_error());
													jelszoButton
															.setDisabled(false);
												}

												public void onSuccess(
														String result) {
													DisplayRequest
															.serverResponse();
													SC.say(result
															+ " : "
															+ commonLabels
																	.defaultpassword());
													jelszoButton
															.setDisabled(false);
												}
											});

						}
					}
				});

			}
		});

		deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask(commonLabels.sure(), new BooleanCallback() {
					public void execute(Boolean value) {
						if (value != null && value) {
							felhasznaloGrid.removeSelectedData();
							modifyButton.setDisabled(true);
							jelszoButton.setDisabled(true);
							deleteButton.setDisabled(true);
							jogGrid.setData(new ListGridRecord[] {});
						}
					}
				});

			}
		});

		jogGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				modify1Button.setDisabled(false);
			}
		});

		jogGrid.addDataArrivedHandler(new DataArrivedHandler() {
			public void onDataArrived(DataArrivedEvent event) {
				for (int i = 0; i < jogGrid.getRecords().length; i++) {
					if (jogGrid
							.getRecord(i)
							.getAttributeAsString(SystemConstants.JOG_NEV)
							.equals(felhasznaloGrid.getSelectedRecord()
									.getAttribute(
											SystemConstants.FELHASZNALO_MENU))) {
						jogGrid.getRecord(i).setEnabled(false);
					}
				}
			}
		});

		modify1Button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				jogEdit(jogDataSource, jogGrid);
			}
		});

		return middleLayout;

	}

	void felhasznaloEdit(FelhasznaloDataSource dataSource,
			final ListGrid listGrid, final ListGrid jogGrid, final boolean uj) {

		final Window winModal = new Window();
		winModal.setWidth(400);
		winModal.setHeight(150);
		winModal.setTitle(systemLabels.felhasznalo());
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
			editForm.getField(SystemConstants.FELHASZNALO_ROVIDNEV).setCanEdit(
					false);
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
						if (response.getStatus() == DSResponse.STATUS_SUCCESS) {
							if (uj) {
								jogGrid.setData(new ListGridRecord[] {});
							} else {
								Criteria criteria = new Criteria();
								criteria.setAttribute(
										SystemConstants.FELHASZNALO_ROVIDNEV,
										listGrid.getSelectedRecord()
												.getAttribute(
														SystemConstants.FELHASZNALO_ROVIDNEV));
								jogGrid.fetchData(criteria);
							}
							winModal.destroy();
						}
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

	void jogEdit(JogDataSource dataSource, final ListGrid listGrid) {

		final Window winModal = new Window();
		winModal.setWidth(400);
		winModal.setHeight(150);
		winModal.setTitle(systemLabels.jog());
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

		editForm.editSelectedData(listGrid);

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
						if (response.getStatus() == DSResponse.STATUS_SUCCESS) {
							winModal.destroy();
						}
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

}
