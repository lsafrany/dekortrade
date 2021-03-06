package hu.dekortrade.client.rendelles.internet;

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

public class Internet {

	private InternetLabels internetorderLabels = GWT
			.create(InternetLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	private String rendeles = "";

	public Canvas get(final String vevo,final IButton extIButton) {
		DisplayRequest.counterInit();

		final HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		VLayout rendeltLayout = new VLayout();
		rendeltLayout.setWidth("40%");
		rendeltLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final InternetRendeltDataSource rendeltDataSource = new InternetRendeltDataSource() {

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
		rendeltGrid.setTitle(internetorderLabels.rendeles());
		rendeltGrid.setWidth("90%");
		rendeltGrid.setShowHeaderContextMenu(false);
		rendeltGrid.setShowHeaderMenuButton(false);
		rendeltGrid.setCanSort(false);
		rendeltGrid.setShowAllRecords(true);
		rendeltGrid.setDataSource(rendeltDataSource);
		
		Criteria criteria = new Criteria();
		criteria.addCriteria(InternetConstants.INTERNETRENDELT_ROVIDNEV,vevo);
		rendeltGrid.fetchData(criteria);

		ListGridField rovidnevGridField = new ListGridField(
				InternetConstants.INTERNETRENDELT_ROVIDNEV);
		rovidnevGridField.setWidth("30%");

		ListGridField rendelesGridField = new ListGridField(
				InternetConstants.INTERNETRENDELT_RENDELES);

		ListGridField datumGridField = new ListGridField(
				InternetConstants.INTERNETRENDELT_DATUM);
		datumGridField.setWidth("35%");

		rendeltGrid.setFields(rovidnevGridField, rendelesGridField,
				datumGridField);

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("100%");

		IButton frissitIButton = new IButton(internetorderLabels.frissit());

		buttonsLayout.addMember(frissitIButton);

		rendeltLayout.addMember(rendeltGrid);
		rendeltLayout.addMember(buttonsLayout);

		VLayout rendeltcikkLayout = new VLayout();
		rendeltcikkLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final InternetRendeltcikkDataSource rendeltcikkDataSource = new InternetRendeltcikkDataSource() {

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
		rendeltcikkGrid.setTitle(internetorderLabels.rendeltcikk());
		rendeltcikkGrid.setWidth("90%");
		rendeltcikkGrid.setShowHeaderContextMenu(false);
		rendeltcikkGrid.setShowHeaderMenuButton(false);
		rendeltcikkGrid.setCanSort(false);
		rendeltcikkGrid.setShowAllRecords(true);
		rendeltcikkGrid.setDataSource(rendeltcikkDataSource);

		ListGridField cikkszamGridField = new ListGridField(
				InternetConstants.INTERNETRENDELTCIKK_CIKKSZAM);

		ListGridField szinkodGridField = new ListGridField(
				InternetConstants.INTERNETRENDELTCIKK_SZINKOD);
		szinkodGridField.setWidth("15%");

		ListGridField exportkartonGridField = new ListGridField(
				InternetConstants.INTERNETRENDELTCIKK_EXPORTKARTON);
		exportkartonGridField.setWidth("20%");

		ListGridField kiskartonGridField = new ListGridField(
				InternetConstants.INTERNETRENDELTCIKK_KISKARTON);
		kiskartonGridField.setWidth("20%");

		ListGridField darabGridField = new ListGridField(
				InternetConstants.INTERNETRENDELTCIKK_DARAB);
		darabGridField.setWidth("20%");

		rendeltcikkGrid.setFields(cikkszamGridField, szinkodGridField, exportkartonGridField,
				kiskartonGridField, darabGridField);

		rendeltcikkLayout.addMember(rendeltcikkGrid);

		middleLayout.addMember(rendeltLayout);
		middleLayout.addMember(rendeltcikkLayout);

		frissitIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (extIButton != null) {
					extIButton.setDisabled(true);
				}
				rendeltGrid.invalidateCache();
				Criteria criteria = new Criteria();
				criteria.addCriteria(InternetConstants.INTERNETRENDELT_ROVIDNEV,vevo);
				rendeltGrid.fetchData(criteria);
				rendeltcikkGrid.setData(new ListGridRecord[] {});
			}
		});

		rendeltGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				if (extIButton != null) {
					extIButton.setDisabled(false);
				}
				Criteria criteria = new Criteria();
				criteria.setAttribute(
						InternetConstants.INTERNETRENDELTCIKK_ROVIDNEV,
						rendeltGrid.getSelectedRecord().getAttribute(
								InternetConstants.INTERNETRENDELT_ROVIDNEV));
				rendeles = rendeltGrid.getSelectedRecord().getAttribute(
						InternetConstants.INTERNETRENDELT_RENDELES);
				criteria.setAttribute(
						InternetConstants.INTERNETRENDELTCIKK_RENDELES,
						rendeles);
				rendeltcikkGrid.fetchData(criteria);
			}
		});

		return middleLayout;

	}

	public String getRendeles() {
		return this.rendeles;
	}

}
