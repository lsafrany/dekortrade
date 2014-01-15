package hu.dekortrade.client.basedata.szallito;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.events.ErrorEvent;
import com.smartgwt.client.data.events.HandleErrorHandler;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Szallito {

	private SzallitoLabels szallitoLabels = GWT
			.create(SzallitoLabels.class);

	private CommonLabels commonLabels = GWT
			.create(CommonLabels.class);
	
	public Canvas get() {
		DisplayRequest.counterInit();
						
		HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		VLayout szallitoLayout = new VLayout();
		szallitoLayout.setDefaultLayoutAlign(Alignment.CENTER);
		
		final SzallitoDataSource szallitoDataSource = new SzallitoDataSource() {

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

		szallitoDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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
		
		final ListGrid szallitoGrid = new ListGrid();
		szallitoGrid.setTitle(szallitoLabels.szallitok());
		szallitoGrid.setWidth("70%");
		szallitoGrid.setShowHeaderContextMenu(false);
		szallitoGrid.setShowHeaderMenuButton(false);
		szallitoGrid.setCanSort(false);
		szallitoGrid.setShowAllRecords(true);
		szallitoGrid.setDataSource(szallitoDataSource);
		szallitoGrid.setAutoFetchData(true);

		ListGridField kodGridField = new ListGridField(
				SzallitoConstants.SZALLITO_KOD);
		kodGridField.setWidth("5%");
		
		ListGridField nevGridField = new ListGridField(
				SzallitoConstants.SZALLITO_NEV);
		nevGridField.setWidth("20%");

		ListGridField cimGridField = new ListGridField(
				SzallitoConstants.SZALLITO_CIM);
		cimGridField.setWidth("30%");
		
		ListGridField elerhetosegGridField = new ListGridField(
				SzallitoConstants.SZALLITO_ELERHETOSEG);
		
		szallitoGrid.setFields(kodGridField, nevGridField, cimGridField, elerhetosegGridField);

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("70%");
		
		HLayout addButtonLayout = new HLayout();
		addButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		IButton addButton = new IButton(commonLabels.add());
		addButtonLayout.setAlign(Alignment.CENTER);
		addButtonLayout.addMember(addButton);
			
		HLayout modifyButtonLayout = new HLayout();
		modifyButtonLayout
				.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		modifyButtonLayout.setAlign(Alignment.CENTER);
		final IButton modifyButton = new IButton(commonLabels.modify());
		modifyButton.disable();
		modifyButtonLayout.addMember(modifyButton);

		HLayout deleteButtonLayout = new HLayout();
		deleteButtonLayout
				.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		deleteButtonLayout.setAlign(Alignment.CENTER);
		final IButton deleteButton = new IButton(commonLabels.delete());
		deleteButton.disable();
		deleteButtonLayout.addMember(deleteButton);
		
		buttonsLayout.addMember(addButtonLayout);
		buttonsLayout.addMember(modifyButtonLayout);
		buttonsLayout.addMember(deleteButtonLayout);

		szallitoLayout.addMember(szallitoGrid);
		szallitoLayout.addMember(buttonsLayout);
		
		middleLayout.addMember(szallitoLayout);
		
		return middleLayout;

	}
}
