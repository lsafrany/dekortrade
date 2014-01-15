package hu.dekortrade.client.system;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.Criteria;
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
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Felhasznalo {

	private SystemLabels systemLabels = GWT
			.create(SystemLabels.class);

	private CommonLabels commonLabels = GWT
			.create(CommonLabels.class);
	
	public Canvas get() {
		DisplayRequest.counterInit();
						
		HLayout middleLayout = new HLayout();
		middleLayout.setWidth("70%");
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
						SC.warn(commonLabels.server_sqlerror()
								+ " : "
								+ event.getResponse().getAttribute(
										ClientConstants.SERVER_SQLERROR));
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
		
		felhasznaloGrid.setFields(rovidnevGridField, nevGridField);

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("80%");
		
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
		
		felhasznaloLayout.addMember(felhasznaloGrid);
		felhasznaloLayout.addMember(buttonsLayout);
		
		VLayout jogLayout = new VLayout();
		jogLayout.setWidth("30%");
		jogLayout.setDefaultLayoutAlign(Alignment.CENTER);
		
		final JogDataSource jogDataSource = new JogDataSource() {

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

		jogDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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
		
		ListGridField jogGridField = new ListGridField(
				SystemConstants.JOG_JOG);
		
		jogGrid.setFields(jognevGridField, jogGridField);

		jogLayout.addMember(jogGrid);
		
		middleLayout.addMember(felhasznaloLayout);
		middleLayout.addMember(jogLayout);	

		felhasznaloGrid.addRecordClickHandler(new RecordClickHandler() {   
            public void onRecordClick(RecordClickEvent event) {
            	Criteria criteria = new Criteria();
             	criteria.setAttribute(SystemConstants.FELHASZNALO_ROVIDNEV, felhasznaloGrid.getSelectedRecord().getAttribute(SystemConstants.FELHASZNALO_ROVIDNEV));
            	jogGrid.fetchData(criteria);
            }
        });   

		
		return middleLayout;

	}
}
