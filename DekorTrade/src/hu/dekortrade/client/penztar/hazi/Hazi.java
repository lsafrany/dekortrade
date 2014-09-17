package hu.dekortrade.client.penztar.hazi;

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
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.validator.IsFloatValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Hazi {

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);
	
	private HaziLabels haziLabels = GWT.create(HaziLabels.class);

	public Canvas get() {
		final VLayout middleLayout = new VLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		HLayout titleLayout = new HLayout();
		titleLayout.setDefaultLayoutAlign(Alignment.CENTER);
		titleLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		titleLayout.setStyleName("middle");
		titleLayout.setHeight("3%");
		titleLayout.setWidth("100%");

		VLayout haziLayout = new VLayout();
		haziLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final HaziDataSource haziDataSource = new HaziDataSource() {

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

		haziDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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

		final ListGrid haziGrid = new ListGrid();
		haziGrid.setTitle(haziLabels.hazipenztar());
		haziGrid.setWidth("65%");
		haziGrid.setShowHeaderContextMenu(false);
		haziGrid.setShowHeaderMenuButton(false);
		haziGrid.setCanSort(false);
		haziGrid.setShowAllRecords(true);
		haziGrid.setDataSource(haziDataSource);
		haziGrid.setAutoFetchData(true);

		ListGridField megjegyzesceGridField = new ListGridField(
				HaziConstants.HAZI_MEGJEGYZES);

		ListGridField penztarosGridField = new ListGridField(
				HaziConstants.HAZI_PENZTAROSNEV);
		penztarosGridField.setWidth("20%");

		ListGridField fizetGridField = new ListGridField(
				HaziConstants.HAZI_FIZET);
		fizetGridField.setWidth("10%");
		
		ListGridField fizeteurGridField = new ListGridField(
				HaziConstants.HAZI_FIZETEUR);
		fizeteurGridField.setWidth("10%");

		ListGridField fizetusdGridField = new ListGridField(
				HaziConstants.HAZI_FIZETUSD);
		fizetusdGridField.setWidth("10%");

		haziGrid.setFields(megjegyzesceGridField, penztarosGridField, fizetGridField, fizeteurGridField,fizetusdGridField);

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("80%");

		HLayout refreshButtonLayout = new HLayout();
		IButton refreshIButton = new IButton(commonLabels.refresh());
		refreshButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		refreshButtonLayout.setAlign(Alignment.CENTER);
		refreshButtonLayout.addMember(refreshIButton);

		HLayout addButtonLayout = new HLayout();
		addButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton addIButton = new IButton(commonLabels.add());
		addButtonLayout.setAlign(Alignment.CENTER);
		addButtonLayout.addMember(addIButton);

		buttonsLayout.addMember(refreshButtonLayout);
		buttonsLayout.addMember(addButtonLayout);

		haziLayout.addMember(haziGrid);
		haziLayout.addMember(buttonsLayout);

		middleLayout.addMember(haziLayout);

		refreshIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				haziGrid.invalidateCache();
				haziGrid.fetchData();			
			}
		});

		addIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				final Window winModal = new Window();
				winModal.setWidth(400);
				winModal.setHeight(200);
				winModal.setTitle(haziLabels.hazipenztar());
				winModal.setShowMinimizeButton(false);
				winModal.setShowCloseButton(false);
				winModal.setIsModal(true);
				winModal.setShowModalMask(true);
				winModal.centerInPage();

				final DynamicForm editForm = new DynamicForm();
				editForm.setNumCols(2);
				editForm.setColWidths("15%", "*");
				editForm.setDataSource(haziDataSource);
				editForm.setUseAllDataSourceFields(true);

				editForm.getField(HaziConstants.HAZI_PENZTAROS).setVisible(false);
				editForm.getField(HaziConstants.HAZI_PENZTAROSNEV).setVisible(false);
				editForm.getField(HaziConstants.HAZI_DATUM).setVisible(false);
				editForm.getField(HaziConstants.HAZI_FIZET).setValidators(new IsFloatValidator());
				editForm.getField(HaziConstants.HAZI_FIZETEUR).setValidators(new IsFloatValidator());
				editForm.getField(HaziConstants.HAZI_FIZETUSD).setValidators(new IsFloatValidator());
				
				editForm.editNewRecord();

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
		});

		return middleLayout;
	}

}
