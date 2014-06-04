package hu.dekortrade.client.basedata.gyarto;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;

import com.google.gwt.core.client.GWT;
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

public class Gyarto {

	private GyartoLabels gyartoLabels = GWT.create(GyartoLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	public Canvas get() {
		DisplayRequest.counterInit();

		HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		VLayout gyartoLayout = new VLayout();
		gyartoLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final GyartoDataSource gyartoDataSource = new GyartoDataSource() {

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

		gyartoDataSource.addHandleErrorHandler(new HandleErrorHandler() {
			public void onHandleError(ErrorEvent event) {

				if (event.getResponse().getStatus() == DSResponse.STATUS_FAILURE) {
					if (event.getResponse().getAttribute(
							ClientConstants.SERVER_ERROR) != null)
						SC.warn(commonLabels.server_error());
					else if (event.getResponse().getAttribute(
							ClientConstants.SERVER_SQLERROR) != null)
						SC.warn(commonLabels.server_sqlerror()
								+ " : "
								+ event.getResponse().getAttribute(
										ClientConstants.SERVER_SQLERROR));
					event.cancel();
				}
			}
		});

		final ListGrid gyartoGrid = new ListGrid();
		gyartoGrid.setTitle(gyartoLabels.gyartok());
		gyartoGrid.setWidth("70%");
		gyartoGrid.setShowHeaderContextMenu(false);
		gyartoGrid.setShowHeaderMenuButton(false);
		gyartoGrid.setCanSort(false);
		gyartoGrid.setShowAllRecords(true);
		gyartoGrid.setDataSource(gyartoDataSource);
		gyartoGrid.setAutoFetchData(true);
		gyartoGrid.setCanExpandRecords(true);
		gyartoGrid.setExpansionMode(ExpansionMode.DETAILS);

		ListGridField kodGridField = new ListGridField(
				GyartoConstants.GYARTO_KOD);
		kodGridField.setWidth("5%");

		ListGridField nevGridField = new ListGridField(
				GyartoConstants.GYARTO_NEV);
		nevGridField.setWidth("20%");

		ListGridField cimGridField = new ListGridField(
				GyartoConstants.GYARTO_CIM);
		cimGridField.setWidth("30%");

		ListGridField elerhetosegGridField = new ListGridField(
				GyartoConstants.GYARTO_ELERHETOSEG);

		gyartoGrid.setFields(kodGridField, nevGridField, cimGridField,
				elerhetosegGridField);

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

		HLayout deleteButtonLayout = new HLayout();
		deleteButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		deleteButtonLayout.setAlign(Alignment.CENTER);
		final IButton deleteButton = new IButton(commonLabels.delete());
		deleteButton.disable();
		deleteButtonLayout.addMember(deleteButton);

		buttonsLayout.addMember(addButtonLayout);
		buttonsLayout.addMember(modifyButtonLayout);
		buttonsLayout.addMember(deleteButtonLayout);

		gyartoLayout.addMember(gyartoGrid);
		gyartoLayout.addMember(buttonsLayout);

		middleLayout.addMember(gyartoLayout);

		gyartoGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				modifyButton.setDisabled(false);
				deleteButton.setDisabled(false);
			}
		});

		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				gyartoEdit(gyartoDataSource, gyartoGrid, Boolean.TRUE);
			}
		});

		modifyButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				gyartoEdit(gyartoDataSource, gyartoGrid, Boolean.FALSE);
			}
		});

		deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask(commonLabels.sure(), new BooleanCallback() {
					public void execute(Boolean value) {
						if (value != null && value) {
							gyartoGrid.removeSelectedData();
							modifyButton.setDisabled(true);
							deleteButton.setDisabled(true);
						}
					}
				});

			}
		});

		return middleLayout;

	}

	void gyartoEdit(GyartoDataSource dataSource, ListGrid listGrid,
			boolean uj) {

		final Window winModal = new Window();
		winModal.setWidth(700);
		winModal.setHeight(400);
		winModal.setTitle(gyartoLabels.gyarto());
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

		if (uj) {
			editForm.getField(GyartoConstants.GYARTO_KEDVEZMENY)
			.setDefaultValue("1");
			editForm.editNewRecord();
		}
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

}
