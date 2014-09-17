package hu.dekortrade.client.raktar.beszallitas;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.torzsadat.cikktorzs.Cikktorzs;
import hu.dekortrade.client.torzsadat.cikktorzs.CikktorzsConstants;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.events.ErrorEvent;
import com.smartgwt.client.data.events.HandleErrorHandler;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ExpansionMode;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Beszallitas {

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	private BeszallitasLabels beszallitasLabels = GWT
			.create(BeszallitasLabels.class);

	private Cikktorzs ctorzs = new Cikktorzs();
	
	public Canvas get() {
		DisplayRequest.counterInit();

		final VLayout middleLayout = new VLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		middleLayout.setStyleName("middle");

		HLayout titleLayout = new HLayout();
		titleLayout.setDefaultLayoutAlign(Alignment.CENTER);
		titleLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		titleLayout.setStyleName("middle");
		titleLayout.setHeight("3%");

		final Label titleLabel = new Label();
		titleLabel.setContents("");
		titleLabel.setAlign(Alignment.CENTER);
		titleLabel.setWidth("80%");
		titleLayout.addMember(titleLabel);

		final HLayout middleLayout1 = new HLayout();
		middleLayout1.setAlign(Alignment.CENTER);
		middleLayout1.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		middleLayout1.setStyleName("middle");

		final HLayout leftLayout = new HLayout();
		leftLayout.setAlign(Alignment.CENTER);
		leftLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		leftLayout.setStyleName("middle");
		leftLayout.setWidth("35%");

		final HLayout rightLayout = new HLayout();
		rightLayout.setAlign(Alignment.CENTER);
		rightLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		rightLayout.setStyleName("middle");

		VLayout beszallitasLayout = new VLayout();
		beszallitasLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final BeszallitottCikkDataSource beszallitottCikkDataSource = new BeszallitottCikkDataSource() {
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

		beszallitottCikkDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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

		final ListGrid beszallitasGrid = new ListGrid();
		beszallitasGrid.setTitle(beszallitasLabels.beszallitas());
		beszallitasGrid.setWidth("95%");
		beszallitasGrid.setShowHeaderContextMenu(false);
		beszallitasGrid.setShowHeaderMenuButton(false);
		beszallitasGrid.setCanSort(false);
		beszallitasGrid.setShowAllRecords(true);
		beszallitasGrid.setDataSource(beszallitottCikkDataSource);
		beszallitasGrid.setCanExpandRecords(true);
		beszallitasGrid.setExpansionMode(ExpansionMode.DETAILS);

		ListGridField exportkartonGridField = new ListGridField(
				BeszallitasConstants.BESZALLITAS_EXPORTKARTON);
		exportkartonGridField.setWidth("10%");

		ListGridField kiskartonGridField = new ListGridField(
				BeszallitasConstants.BESZALLITAS_KISKARTON);
		kiskartonGridField.setWidth("10%");

		ListGridField darabGridField = new ListGridField(
				BeszallitasConstants.BESZALLITAS_DARAB);
		darabGridField.setWidth("10%");

		ListGridField datumbGridField = new ListGridField(
				BeszallitasConstants.BESZALLITAS_DATUM);
		
		beszallitasGrid.setFields(exportkartonGridField, kiskartonGridField, darabGridField, datumbGridField);

		HLayout buttons1Layout = new HLayout();
		buttons1Layout.setHeight("3%");
		buttons1Layout.setWidth("90%");

		HLayout beszallitButtonLayout = new HLayout();
		beszallitButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton beszallitButton = new IButton(beszallitasLabels.beszallitas());
		beszallitButton.setDisabled(true);
		beszallitButtonLayout.setAlign(Alignment.CENTER);
		beszallitButtonLayout.addMember(beszallitButton);

		buttons1Layout.addMember(beszallitButtonLayout); 
		
		middleLayout.addMember(middleLayout1);
		middleLayout1.addMember(leftLayout);
		middleLayout1.addMember(rightLayout);

		leftLayout.addMember(beszallitasLayout);

		beszallitasLayout.addMember(titleLayout);
		beszallitasLayout.addMember(beszallitasGrid);
		beszallitasLayout.addMember(buttons1Layout);
		
		rightLayout.addMember(ctorzs.get(beszallitButton,beszallitasGrid,titleLabel));

		beszallitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final Window winModal = new Window();
				winModal.setWidth(700);
				winModal.setHeight(600);
				winModal.setTitle(ctorzs.getSelectedRecord().getAttribute(CikktorzsConstants.CIKK_CIKKSZAM) + " - " + ctorzs.getSelectedRecord().getAttribute(CikktorzsConstants.CIKK_SZINKOD));
				winModal.setShowMinimizeButton(false);
				winModal.setShowCloseButton(false);
				winModal.setIsModal(true);
				winModal.setShowModalMask(true);
				winModal.centerInPage();

				final DynamicForm editForm = new DynamicForm();
				editForm.setNumCols(2);
				editForm.setColWidths("25%", "*");
				editForm.setDataSource(beszallitottCikkDataSource);
				editForm.setUseAllDataSourceFields(true);
				
				editForm.getField(BeszallitasConstants.BESZALLITAS_CIKKSZAM).setDefaultValue(ctorzs.getSelectedRecord().getAttribute(CikktorzsConstants.CIKK_CIKKSZAM));
				editForm.getField(BeszallitasConstants.BESZALLITAS_CIKKSZAM).setCanEdit(false);
				editForm.getField(BeszallitasConstants.BESZALLITAS_SZINKOD).setDefaultValue(ctorzs.getSelectedRecord().getAttribute(CikktorzsConstants.CIKK_SZINKOD));
				editForm.getField(BeszallitasConstants.BESZALLITAS_SZINKOD).setCanEdit(false);
				editForm.getField(BeszallitasConstants.BESZALLITAS_ROGZITO).hide();
				editForm.getField(BeszallitasConstants.BESZALLITAS_DATUM).hide();
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
