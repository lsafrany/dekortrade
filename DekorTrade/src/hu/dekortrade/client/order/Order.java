package hu.dekortrade.client.order;

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
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Order {

	private OrderLabels orderLabels = GWT.create(OrderLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	public Canvas get() {
		DisplayRequest.counterInit();
		
		final HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");
		
		VLayout rendeltLayout = new VLayout();
		rendeltLayout.setDefaultLayoutAlign(Alignment.CENTER);
				
		final RendeltDataSource rendeltDataSource = new RendeltDataSource() {

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

		rendeltDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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
		
		final ListGrid rendeltGrid = new ListGrid();
		rendeltGrid.setTitle(orderLabels.rendeles());
		rendeltGrid.setWidth("70%");
		rendeltGrid.setShowHeaderContextMenu(false);
		rendeltGrid.setShowHeaderMenuButton(false);
		rendeltGrid.setCanSort(false);
		rendeltGrid.setShowAllRecords(true);
		rendeltGrid.setDataSource(rendeltDataSource);
		rendeltGrid.setAutoFetchData(true);

		ListGridField rovidnevGridField = new ListGridField(
				OrderConstants.RENDELT_ROVIDNEV);
		rovidnevGridField.setWidth("30%");
		
		ListGridField rendelesGridField = new ListGridField(
				OrderConstants.RENDELT_RENDELES);
	
		ListGridField datumGridField = new ListGridField(
				OrderConstants.RENDELT_DATUM);
		datumGridField.setWidth("20%");
		
		rendeltGrid.setFields(rovidnevGridField, rendelesGridField, datumGridField);
		
		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("100%");

		IButton frissitIButton = new IButton(orderLabels.frissit());

		buttonsLayout.addMember(frissitIButton);
		
		rendeltLayout.addMember(rendeltGrid);
		rendeltLayout.addMember(buttonsLayout);
		
		VLayout rendeltcikkLayout = new VLayout();
		rendeltcikkLayout.setDefaultLayoutAlign(Alignment.CENTER);
		
		final RendeltcikkDataSource rendeltcikkDataSource = new RendeltcikkDataSource() {

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

		rendeltcikkDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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
		
		final ListGrid rendeltcikkGrid = new ListGrid();
		rendeltcikkGrid.setTitle(orderLabels.rendeltcikk());
		rendeltcikkGrid.setWidth("50%");
		rendeltcikkGrid.setShowHeaderContextMenu(false);
		rendeltcikkGrid.setShowHeaderMenuButton(false);
		rendeltcikkGrid.setCanSort(false);
		rendeltcikkGrid.setShowAllRecords(true);
		rendeltcikkGrid.setDataSource(rendeltcikkDataSource);

		ListGridField cikkszamGridField = new ListGridField(
				OrderConstants.RENDELTCIKK_CIKKSZAM);
	
		ListGridField exportkartonGridField = new ListGridField(
				OrderConstants.RENDELTCIKK_EXPORTKARTON);
		exportkartonGridField.setWidth("30%");
		
		rendeltcikkGrid.setFields(cikkszamGridField, exportkartonGridField);

		rendeltcikkLayout.addMember(rendeltcikkGrid);
		
		middleLayout.addMember(rendeltLayout);
		middleLayout.addMember(rendeltcikkLayout);
		
		frissitIButton.addClickHandler(new ClickHandler() {  
	    	public void onClick(ClickEvent event) {	    		
	    		rendeltGrid.invalidateCache();
	    		rendeltGrid.fetchData();
	    		rendeltcikkGrid.setData(new ListGridRecord[]{});
  	    	}	
        });         	    	    	 

		rendeltGrid.addRecordClickHandler(new RecordClickHandler() {   
            public void onRecordClick(RecordClickEvent event) {
            	Criteria criteria = new Criteria();
            	criteria.setAttribute(OrderConstants.RENDELTCIKK_ROVIDNEV, rendeltGrid.getSelectedRecord().getAttribute(OrderConstants.RENDELT_ROVIDNEV));         
            	criteria.setAttribute(OrderConstants.RENDELTCIKK_RENDELES, rendeltGrid.getSelectedRecord().getAttribute(OrderConstants.RENDELT_RENDELES));
            	rendeltcikkGrid.fetchData(criteria);
            }
        });   
	
		return middleLayout;

	}
}
