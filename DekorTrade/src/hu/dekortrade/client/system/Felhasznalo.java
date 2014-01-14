package hu.dekortrade.client.system;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.events.ErrorEvent;
import com.smartgwt.client.data.events.HandleErrorHandler;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
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
		felhasznaloGrid.setWidth("50%");
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

		felhasznaloLayout.addMember(felhasznaloGrid);
		
		middleLayout.addMember(felhasznaloLayout);
		
		return middleLayout;

	}
}
