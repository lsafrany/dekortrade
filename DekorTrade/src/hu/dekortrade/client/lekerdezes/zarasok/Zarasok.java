package hu.dekortrade.client.lekerdezes.zarasok;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.penztar.zaras.Zaras;
import hu.dekortrade.client.penztar.zaras.ZarasConstants;
import hu.dekortrade.client.penztar.zaras.ZarasLabels;
import hu.dekortrade.shared.Constants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.data.Criteria;
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
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Zarasok {

	private ZarasokLabels zarasLabels = GWT.create(ZarasokLabels.class);

	private ZarasLabels cashCloseLabels = GWT.create(ZarasLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	public Canvas get() {
		DisplayRequest.counterInit();

		final HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		VLayout zarasLayout = new VLayout();
		zarasLayout.setDefaultLayoutAlign(Alignment.CENTER);
		zarasLayout.setWidth("30%");

		final ZarasokDataSource zarasDataSource = new ZarasokDataSource() {

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

		zarasDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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

		final ListGrid zarasGrid = new ListGrid();
		zarasGrid.setTitle(zarasLabels.zaras());
		zarasGrid.setWidth("90%");
		zarasGrid.setShowHeaderContextMenu(false);
		zarasGrid.setShowHeaderMenuButton(false);
		zarasGrid.setCanSort(false);
		zarasGrid.setShowAllRecords(true);
		zarasGrid.setDataSource(zarasDataSource);
		zarasGrid.setAutoFetchData(true);
		zarasGrid.setCanExpandRecords(true);
		zarasGrid.setExpansionMode(ExpansionMode.DETAILS);

		ListGridField zarasGridField = new ListGridField(
				ZarasokConstants.ZARAS_ZARAS);
		zarasGridField.setWidth("20%");

		ListGridField penztarosnevGridField = new ListGridField(
				ZarasokConstants.ZARAS_PENZTAROSNEV);

		ListGridField datumGridField = new ListGridField(
				ZarasokConstants.ZARAS_DATUM);
		datumGridField.setWidth("30%");

		zarasGrid.setFields(zarasGridField, penztarosnevGridField,
				datumGridField);

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("60%");

		HLayout refreshButtonLayout = new HLayout();
		IButton refreshIButton = new IButton(commonLabels.refresh());
		refreshButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		refreshButtonLayout.setAlign(Alignment.CENTER);
		refreshButtonLayout.addMember(refreshIButton);

		HLayout selectButtonLayout = new HLayout();
		selectButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton selectIButton = new IButton(commonLabels.select());
		selectIButton.setDisabled(true);
		selectButtonLayout.setAlign(Alignment.CENTER);
		selectButtonLayout.addMember(selectIButton);

		buttonsLayout.addMember(refreshButtonLayout);
		buttonsLayout.addMember(selectButtonLayout);

		zarasLayout.addMember(zarasGrid);
		zarasLayout.addMember(buttonsLayout);

		VLayout fizetesLayout = new VLayout();
		fizetesLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final ZarasokFizetesDataSource zarasFizetesDataSource = new ZarasokFizetesDataSource() {

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

		zarasFizetesDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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
		fizetesGrid.setDataSource(zarasFizetesDataSource);
		fizetesGrid.setAutoFetchData(true);

		ListGridField cedulaGridField = new ListGridField(
				ZarasConstants.ZARASFIZETES_CEDULA);
		cedulaGridField.setWidth("8%");

		ListGridField vevoGridField = new ListGridField(
				ZarasConstants.ZARASFIZETES_VEVONEV);

		ListGridField tipusGridField = new ListGridField(
				ZarasConstants.ZARASFIZETES_TIPUS);
		tipusGridField.setWidth("15%");

		ListGridField penztarosGridField = new ListGridField(
				ZarasConstants.ZARASFIZETES_PENZTAROSNEV);
		penztarosGridField.setWidth("20%");

		ListGridField fizetGridField = new ListGridField(
				ZarasConstants.ZARASFIZETES_FIZET);
		fizetGridField.setWidth("8%");

		ListGridField fizeteurGridField = new ListGridField(
				ZarasConstants.ZARASFIZETES_FIZETEUR);
		fizeteurGridField.setWidth("8%");

		ListGridField fizetusdGridField = new ListGridField(
				ZarasConstants.ZARASFIZETES_FIZETUSD);
		fizetusdGridField.setWidth("8%");

		ListGridField dateGridField = new ListGridField(
				ZarasConstants.ZARASFIZETES_DATUM);
		dateGridField.setWidth("10%");

		fizetesGrid.setFields(cedulaGridField, vevoGridField, tipusGridField,
				penztarosGridField, fizetGridField, fizeteurGridField,
				fizetusdGridField, dateGridField);

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

		fizetesLayout.addMember(fizetesGrid);
		fizetesLayout.addMember(fizetLayout);

		middleLayout.addMember(zarasLayout);
		middleLayout.addMember(fizetesLayout);

		fizetesGrid.addDataArrivedHandler(new DataArrivedHandler() {
			public void onDataArrived(DataArrivedEvent event) {

				float fizetusd = 0;
				for (int i = 0; i < fizetesGrid.getRecords().length; i++) {
					if (fizetesGrid.getRecord(i).getAttribute(
							ZarasConstants.ZARASFIZETES_FIZETUSD) != null) {
						fizetusd = fizetusd
								+ fizetesGrid.getRecord(i).getAttributeAsFloat(
										ZarasConstants.ZARASFIZETES_FIZETUSD);
					}

				}
				usdLabel.setContents(NumberFormat.getFormat("#.0000").format(
						fizetusd));
			}
		});

		zarasGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				Criteria criteria = new Criteria();
				criteria.setAttribute(
						ZarasokConstants.ZARAS_ZARAS,
						zarasGrid.getSelectedRecord().getAttribute(
								ZarasokConstants.ZARAS_ZARAS));
				fizetesGrid.fetchData(criteria);
				selectIButton.setDisabled(false);
			}
		});

		selectIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				middleLayout.removeMembers(middleLayout.getMembers());
				Zaras zaras = new Zaras();
				middleLayout.addMember(zaras.printZaras(
						zarasGrid.getSelectedRecord().getAttribute(
								ZarasokConstants.ZARAS_ZARAS),
						Constants.MENU_LEKERDEZES_ZARASOK,
						zarasGrid.getSelectedRecord().getAttributeAsFloat(
								ZarasokConstants.ZARAS_KIVETHUF),
						zarasGrid.getSelectedRecord().getAttributeAsFloat(
								ZarasokConstants.ZARAS_KIVETEUR),
						zarasGrid.getSelectedRecord().getAttributeAsFloat(
								ZarasokConstants.ZARAS_KIVETUSD))						
						);

			}
		});

		return middleLayout;

	}

}
