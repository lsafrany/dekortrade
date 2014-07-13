package hu.dekortrade.client.torzsadat.vevo;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.client.lekerdezes.cedulak.Cedulak;
import hu.dekortrade.client.rendeles.elorendeles.Elorendeles;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.events.ErrorEvent;
import com.smartgwt.client.data.events.HandleErrorHandler;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ExpansionMode;
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
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Vevo {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private VevoLabels vevoLabels = GWT.create(VevoLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	public Canvas get(final String menu) {
		DisplayRequest.counterInit();

		final HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		VLayout vevoLayout = new VLayout();
		vevoLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final VevoDataSource vevoDataSource = new VevoDataSource() {

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

		vevoDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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

		final ListGrid vevoGrid = new ListGrid();
		vevoGrid.setTitle(vevoLabels.vevok());
		vevoGrid.setWidth("70%");
		vevoGrid.setShowHeaderContextMenu(false);
		vevoGrid.setShowHeaderMenuButton(false);
		vevoGrid.setCanSort(false);
		vevoGrid.setShowAllRecords(true);
		vevoGrid.setDataSource(vevoDataSource);
		vevoGrid.setAutoFetchData(true);
		vevoGrid.setCanExpandRecords(true);
		vevoGrid.setExpansionMode(ExpansionMode.DETAILS);

		ListGridField rovidnevGridField = new ListGridField(
				VevoConstants.VEVO_ROVIDNEV);
		rovidnevGridField.setWidth("10%");

		ListGridField nevGridField = new ListGridField(VevoConstants.VEVO_NEV);
		nevGridField.setWidth("40%");

		ListGridField cimGridField = new ListGridField(VevoConstants.VEVO_CIM);

		ListGridField tipusGridField = new ListGridField(
				VevoConstants.VEVO_TIPUS);
		tipusGridField.setWidth("10%");

		ListGridField internetGridField = new ListGridField(
				VevoConstants.VEVO_INTERNET);
		internetGridField.setWidth("10%");

		vevoGrid.setFields(rovidnevGridField, nevGridField, cimGridField,
				tipusGridField, internetGridField);

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("70%");

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
		final IButton jelszoButton = new IButton(vevoLabels.jelszo());
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

		vevoLayout.addMember(vevoGrid);
		vevoLayout.addMember(buttonsLayout);

		final IButton selectOKButton = new IButton(commonLabels.select());

		if (menu.equals(Constants.MENU_RENDELES_ELORENDELES)
				|| menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)) {
			HLayout buttonsLayout2 = new HLayout();
			buttonsLayout2.setHeight("3%");
			buttonsLayout2.setWidth("70%");

			HLayout selectOKButtonLayout = new HLayout();
			selectOKButtonLayout
					.setDefaultLayoutAlign(VerticalAlignment.CENTER);
			selectOKButtonLayout.setAlign(Alignment.CENTER);
			selectOKButton.disable();
			selectOKButtonLayout.addMember(selectOKButton);

			buttonsLayout2.addMember(selectOKButtonLayout);

			vevoLayout.addMember(buttonsLayout2);

		}

		middleLayout.addMember(vevoLayout);

		vevoGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				modifyButton.setDisabled(false);
				deleteButton.setDisabled(false);

				if (menu.equals(Constants.MENU_RENDELES_ELORENDELES)
						&& (vevoGrid.getSelectedRecord().getAttributeAsString(
								VevoConstants.VEVO_TIPUS)
								.equals(VevoConstants.VEVO_TIPUS_BELFOLDI))) {
					selectOKButton.setDisabled(true);
				} else {
					selectOKButton.setDisabled(false);
				}

				if (vevoGrid.getSelectedRecord().getAttributeAsBoolean(
						VevoConstants.VEVO_INTERNET))
					jelszoButton.setDisabled(false);
				else
					jelszoButton.setDisabled(true);
			}
		});

		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				vevoEdit(vevoDataSource, vevoGrid, Boolean.TRUE);
			}
		});

		modifyButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				vevoEdit(vevoDataSource, vevoGrid, Boolean.FALSE);
			}
		});

		jelszoButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask(commonLabels.sure(), new BooleanCallback() {
					public void execute(Boolean value) {
						if (value != null && value) {
							jelszoButton.setDisabled(true);
							DisplayRequest.startRequest();
							dekorTradeService.setVevoJelszo(
									vevoGrid.getSelectedRecord().getAttribute(
											VevoConstants.VEVO_ROVIDNEV),
									new AsyncCallback<String>() {
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
											jelszoButton.setDisabled(false);
										}

										public void onSuccess(String result) {
											DisplayRequest.serverResponse();
											SC.say(result + " : "
													+ commonLabels.existingid());
											jelszoButton.setDisabled(false);
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
							vevoGrid.removeSelectedData();
							modifyButton.setDisabled(true);
							jelszoButton.setDisabled(true);
							deleteButton.setDisabled(true);
						}
					}
				});

			}
		});

		selectOKButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				if (menu.equals(Constants.MENU_RENDELES_ELORENDELES)) {
					dekorTradeService.addKosar(
							UserInfo.userId,
							vevoGrid.getSelectedRecord().getAttribute(
									VevoConstants.VEVO_ROVIDNEV), menu, "",
							new AsyncCallback<String>() {
								public void onFailure(Throwable caught) {
									DisplayRequest.serverResponse();
									if (caught instanceof SQLExceptionSer)
										SC.warn(commonLabels.server_sqlerror()
												+ " : " + caught.getMessage());
									else
										SC.warn(commonLabels.server_error());
								}

								public void onSuccess(String result) {
									DisplayRequest.serverResponse();
									middleLayout.removeMembers(middleLayout
											.getMembers());
									Elorendeles preorder = new Elorendeles();
									middleLayout.addMember(preorder.get());
								}
							});
				}
				if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)) {
					middleLayout.removeMembers(middleLayout.getMembers());
					Cedulak cedula = new Cedulak();
					middleLayout.addMember(cedula.get(
							vevoGrid.getSelectedRecord().getAttribute(
									VevoConstants.VEVO_ROVIDNEV), menu));
				}
			}
		});

		return middleLayout;

	}

	void vevoEdit(VevoDataSource dataSource, ListGrid listGrid, boolean uj) {

		final Window winModal = new Window();
		winModal.setWidth(700);
		winModal.setHeight(600);
		winModal.setTitle(vevoLabels.vevo());
		winModal.setShowMinimizeButton(false);
		winModal.setShowCloseButton(false);
		winModal.setIsModal(true);
		winModal.setShowModalMask(true);
		winModal.centerInPage();

		final DynamicForm editForm = new DynamicForm();
		editForm.setNumCols(2);
		editForm.setColWidths("25%", "*");
		editForm.setDataSource(dataSource);
		editForm.setUseAllDataSourceFields(true);

		if (uj) {
			editForm.getField(VevoConstants.VEVO_TIPUS).setDefaultValue(
					"BELFOLDI");
			editForm.getField(VevoConstants.VEVO_ORSZAG).setDefaultValue("hu");
			editForm.editNewRecord();
		} else {
			editForm.getField(VevoConstants.VEVO_ROVIDNEV).setCanEdit(false);
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

}
