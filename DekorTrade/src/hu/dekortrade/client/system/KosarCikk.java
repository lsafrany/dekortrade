package hu.dekortrade.client.system;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.kosar.KosarCikkDataSource;
import hu.dekortrade.client.kosar.KosarConstants;

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
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class KosarCikk {

	private SystemLabels systemLabels = GWT.create(SystemLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	public Canvas get() {
		DisplayRequest.counterInit();

		final VLayout middleLayout = new VLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		middleLayout.setStyleName("middle");

		VLayout kosarLayout = new VLayout();
		kosarLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final KosarCikkDataSource kosarCikkDataSource = new KosarCikkDataSource(
				null, null, "") {

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

		kosarCikkDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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

		final ListGrid kosarGrid = new ListGrid();
		kosarGrid.setTitle(systemLabels.kosar());
		kosarGrid.setWidth("80%");
		kosarGrid.setShowHeaderContextMenu(false);
		kosarGrid.setShowHeaderMenuButton(false);
		kosarGrid.setCanSort(false);
		kosarGrid.setShowAllRecords(true);
		kosarGrid.setDataSource(kosarCikkDataSource);
		kosarGrid.setAutoFetchData(true);

		ListGridField eladoGridField = new ListGridField(
				KosarConstants.KOSAR_ELADO);
		eladoGridField.setWidth("10%");

		ListGridField vevoGridField = new ListGridField(
				KosarConstants.KOSAR_VEVO);
		vevoGridField.setWidth("10%");

		ListGridField tipusGridField = new ListGridField(
				KosarConstants.KOSAR_TIPUS);
		tipusGridField.setWidth("10%");

		ListGridField cikkszamGridField = new ListGridField(
				KosarConstants.KOSAR_CIKKSZAM);
		cikkszamGridField.setWidth("15%");

		ListGridField megnevezesGridField = new ListGridField(
				KosarConstants.KOSAR_MEGNEVEZES);

		ListGridField exportkartonGridField = new ListGridField(
				KosarConstants.KOSAR_EXPORTKARTON);
		exportkartonGridField.setWidth("10%");

		ListGridField kiskartonGridField = new ListGridField(
				KosarConstants.KOSAR_KISKARTON);
		kiskartonGridField.setWidth("10%");

		ListGridField darabGridField = new ListGridField(
				KosarConstants.KOSAR_DARAB);
		darabGridField.setWidth("10%");

		kosarGrid.setFields(eladoGridField, vevoGridField, tipusGridField,
				cikkszamGridField, megnevezesGridField,
				exportkartonGridField, kiskartonGridField, darabGridField);
	
		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("80%");

		HLayout refreshButtonLayout = new HLayout();
		refreshButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		IButton refreshButton = new IButton(commonLabels.refresh());
		refreshButtonLayout.setAlign(Alignment.CENTER);
		refreshButtonLayout.addMember(refreshButton);

		buttonsLayout.addMember(refreshButtonLayout);

		kosarLayout.addMember(kosarGrid);
		kosarLayout.addMember(buttonsLayout);

		middleLayout.addMember(kosarLayout);

		refreshButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				kosarGrid.invalidateCache();
				kosarGrid.fetchData();
			}
		});
			
		return middleLayout;
	}

}
