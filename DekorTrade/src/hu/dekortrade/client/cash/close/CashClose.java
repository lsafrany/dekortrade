package hu.dekortrade.client.cash.close;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.shared.Constants;

import java.text.DecimalFormat;

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
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class CashClose {

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	private CashCloseLabels cashCloseLabels = GWT.create(CashCloseLabels.class);

	private DecimalFormat df = new DecimalFormat("#.#####");
	
	public Canvas get() {
		DisplayRequest.counterInit();

		HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		VLayout fizetesLayout = new VLayout();
		fizetesLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final FizetesDataSource fizetesDataSource = new FizetesDataSource() {

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

		fizetesDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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

		final ListGrid fizetesGrid = new ListGrid();
		fizetesGrid.setTitle(cashCloseLabels.fizetes());
		fizetesGrid.setWidth("80%");
		fizetesGrid.setShowHeaderContextMenu(false);
		fizetesGrid.setShowHeaderMenuButton(false);
		fizetesGrid.setCanSort(false);
		fizetesGrid.setShowAllRecords(true);
		fizetesGrid.setDataSource(fizetesDataSource);
		fizetesGrid.setAutoFetchData(true);

		ListGridField cedulaGridField = new ListGridField(
				CashCloseConstants.FIZETES_CEDULA);
		cedulaGridField.setWidth("8%");

		ListGridField vevoGridField = new ListGridField(
				CashCloseConstants.FIZETES_VEVONEV);

		ListGridField tipusGridField = new ListGridField(
				CashCloseConstants.FIZETES_TIPUS);
		tipusGridField.setWidth("15%");

		ListGridField penztarosGridField = new ListGridField(
				CashCloseConstants.FIZETES_PENZTAROSNEV);
		penztarosGridField.setWidth("20%");

		ListGridField fizetGridField = new ListGridField(
				CashCloseConstants.FIZETES_FIZET);
		fizetGridField.setWidth("8%");

		ListGridField fizeteurGridField = new ListGridField(
				CashCloseConstants.FIZETES_FIZETEUR);
		fizeteurGridField.setWidth("8%");

		ListGridField fizetusdGridField = new ListGridField(
				CashCloseConstants.FIZETES_FIZETUSD);
		fizetusdGridField.setWidth("8%");

		ListGridField datumGridField = new ListGridField(
				CashCloseConstants.FIZETES_DATUM);
		datumGridField.setWidth("10%");

		fizetesGrid.setFields(cedulaGridField, vevoGridField, tipusGridField,
				penztarosGridField, fizetGridField, fizeteurGridField,
				fizetusdGridField, datumGridField);

		HLayout fizetLayout = new HLayout();
		fizetLayout.setHeight("3%");
		fizetLayout.setWidth("80%");
					
		HLayout usdCurrLabelLayout = new HLayout();
		usdCurrLabelLayout.setWidth("80%");
		Label usdCurrLabel = new Label("USD :");
		usdCurrLabel.setAlign(Alignment.CENTER);	
		usdCurrLabelLayout.addMember(usdCurrLabel);
		
		HLayout usdLabelLayout = new HLayout();
		usdLabelLayout.setWidth("20%");
		final Label usdLabel = new Label("0");
		usdLabel.setAlign(Alignment.CENTER);	
		usdLabelLayout.addMember(usdLabel);
	
		fizetLayout.addMember(usdCurrLabelLayout);
		fizetLayout.addMember(usdLabelLayout);

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
	
		HLayout okButtonLayout = new HLayout();
		okButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton okIButton = new IButton(commonLabels.ok());
		okButtonLayout.setAlign(Alignment.CENTER);
		okButtonLayout.addMember(okIButton);

		buttonsLayout.addMember(refreshButtonLayout);
		buttonsLayout.addMember(okButtonLayout);
		
		fizetesLayout.addMember(fizetesGrid);
		fizetesLayout.addMember(fizetLayout);
		fizetesLayout.addMember(buttonsLayout);
		
		middleLayout.addMember(fizetesLayout);

		refreshIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				fizetesGrid.invalidateCache();
				fizetesGrid.fetchData();
			}
		});

		fizetesGrid.addDataArrivedHandler(new DataArrivedHandler() {
			public void onDataArrived(DataArrivedEvent event) {
			
				float fizetusd = 0;
				for (int i = 0; i < fizetesGrid.getRecords().length; i++) {
					if (fizetesGrid.getRecord(i)
							.getAttribute(CashCloseConstants.FIZETES_FIZETUSD) != null) {
						fizetusd = fizetusd + fizetesGrid.getRecord(i)
								.getAttributeAsFloat(CashCloseConstants.FIZETES_FIZETUSD);	
					}
						
				}
				usdLabel.setContents(df.format(fizetusd));
			}
		});

		return middleLayout;

	}

}
