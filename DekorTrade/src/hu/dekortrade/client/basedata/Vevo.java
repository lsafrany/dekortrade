package hu.dekortrade.client.basedata;

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

public class Vevo {

	private BasedataLabels basedataLabels = GWT
			.create(BasedataLabels.class);

	private CommonLabels commonLabels = GWT
			.create(CommonLabels.class);
	
	public Canvas get() {
		DisplayRequest.counterInit();
						
		HLayout middleLayout = new HLayout();
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
						SC.warn(commonLabels.server_sqlerror()
								+ " : "
								+ event.getResponse().getAttribute(
										ClientConstants.SERVER_SQLERROR));
					event.cancel();
				}
			}
		});
		
		final ListGrid vevoGrid = new ListGrid();
		vevoGrid.setTitle(basedataLabels.vevok());
		vevoGrid.setWidth("70%");
		vevoGrid.setShowHeaderContextMenu(false);
		vevoGrid.setShowHeaderMenuButton(false);
		vevoGrid.setCanSort(false);
		vevoGrid.setShowAllRecords(true);
		vevoGrid.setDataSource(vevoDataSource);
		vevoGrid.setAutoFetchData(true);

		ListGridField rovidnevGridField = new ListGridField(
				BasedataConstants.VEVO_ROVIDNEV);
		rovidnevGridField.setWidth("10%");
		
		ListGridField nevGridField = new ListGridField(
				BasedataConstants.VEVO_NEV);
		nevGridField.setWidth("20%");

		ListGridField cimGridField = new ListGridField(
				BasedataConstants.VEVO_CIM);
		cimGridField.setWidth("30%");
		
		ListGridField elerhetosegGridField = new ListGridField(
				BasedataConstants.VEVO_ELERHETOSEG);

		ListGridField internetGridField = new ListGridField(
				BasedataConstants.VEVO_INTERNET);
		internetGridField.setWidth("10%");

		vevoGrid.setFields(rovidnevGridField, nevGridField, cimGridField, elerhetosegGridField, internetGridField);

		vevoLayout.addMember(vevoGrid);
		
		middleLayout.addMember(vevoLayout);
		
		return middleLayout;

	}
}
