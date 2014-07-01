package hu.dekortrade.client.cedula;

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
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Cedula {

	private CedulaLabels cedulaLabels = GWT.create(CedulaLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	public Canvas get() {
		DisplayRequest.counterInit();

		final HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		VLayout cedulaLayout = new VLayout();
		cedulaLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final CedulaDataSource cedulaDataSource = new CedulaDataSource() {

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

		cedulaDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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

		final ListGrid cedulaGrid = new ListGrid();
		cedulaGrid.setTitle(cedulaLabels.cedula());
		cedulaGrid.setWidth("60%");
		cedulaGrid.setShowHeaderContextMenu(false);
		cedulaGrid.setShowHeaderMenuButton(false);
		cedulaGrid.setCanSort(false);
		cedulaGrid.setShowAllRecords(true);
		cedulaGrid.setDataSource(cedulaDataSource);
		cedulaGrid.setAutoFetchData(true);

		ListGridField cedulaGridField = new ListGridField(
				CedulaConstants.CEDULA_CEDULA);
		cedulaGridField.setWidth("30%");

		ListGridField statusGridField = new ListGridField(
				CedulaConstants.CEDULA_STATUS);
		statusGridField.setWidth("30%");
		
		ListGridField vevoGridField = new ListGridField(
				CedulaConstants.CEDULA_VEVO);
		vevoGridField.setWidth("35%");

		ListGridField datumGridField = new ListGridField(
				CedulaConstants.CEDULA_DATUM);
		
		cedulaGrid.setFields(cedulaGridField, statusGridField, vevoGridField,
				datumGridField);

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("60%");

		HLayout refreshButtonLayout = new HLayout();
		refreshButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		IButton refreshIButton = new IButton(commonLabels.refresh());
		refreshButtonLayout.setAlign(Alignment.CENTER);
		refreshButtonLayout.addMember(refreshIButton);

		HLayout selectButtonLayout = new HLayout();
		selectButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton selectIButton = new IButton(commonLabels.select());
		selectIButton .setDisabled(true);
		selectButtonLayout.setAlign(Alignment.CENTER);
		selectButtonLayout.addMember(selectIButton);

		buttonsLayout.addMember(refreshButtonLayout);
		buttonsLayout.addMember(selectButtonLayout);

		cedulaLayout.addMember(cedulaGrid);
		cedulaLayout.addMember(buttonsLayout);

		VLayout cedulacikkLayout = new VLayout();
		cedulacikkLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final CedulacikkDataSource cedulacikkDataSource = new CedulacikkDataSource() {

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

		cedulacikkDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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

		final ListGrid cedulacikkGrid = new ListGrid();
		cedulacikkGrid.setTitle(cedulaLabels.cedulacikk());
		cedulacikkGrid.setWidth("60%");
		cedulacikkGrid.setShowHeaderContextMenu(false);
		cedulacikkGrid.setShowHeaderMenuButton(false);
		cedulacikkGrid.setCanSort(false);
		cedulacikkGrid.setShowAllRecords(true);
		cedulacikkGrid.setDataSource(cedulacikkDataSource);

		ListGridField cikkszamGridField = new ListGridField(
				CedulaConstants.CEDULACIKK_CIKKSZAM);
	
		ListGridField szinkodGridField = new ListGridField(
				CedulaConstants.CEDULACIKK_SZINKOD);
		szinkodGridField.setWidth("10%");
		
		ListGridField exportkartonGridField = new ListGridField(
				CedulaConstants.CEDULACIKK_EXPORTKARTON);
		exportkartonGridField.setWidth("20%");

		ListGridField kiskartonGridField = new ListGridField(
				CedulaConstants.CEDULACIKK_KISKARTON);
		kiskartonGridField.setWidth("20%");

		ListGridField darabGridField = new ListGridField(
				CedulaConstants.CEDULACIKK_DARAB);
		darabGridField.setWidth("20%");

		cedulacikkGrid.setFields(cikkszamGridField,szinkodGridField,exportkartonGridField,kiskartonGridField,darabGridField);

		cedulacikkLayout.addMember(cedulacikkGrid);

		middleLayout.addMember(cedulaLayout);
		middleLayout.addMember(cedulacikkLayout);

		refreshIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				cedulaGrid.invalidateCache();
				cedulaGrid.fetchData();
				cedulacikkGrid.setData(new ListGridRecord[] {});
				selectIButton.setDisabled(true);
			}
		});

		selectIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {			
				middleLayout.removeMembers(middleLayout.getMembers());
				middleLayout.addMember(printCedula(cedulaGrid.getSelectedRecord().getAttribute(
						CedulaConstants.CEDULA_CEDULA),cedulaGrid.getSelectedRecord().getAttribute(
								CedulaConstants.CEDULA_STATUS),get()));																								
		}
		});

		cedulaGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				Criteria criteria = new Criteria();
				criteria.setAttribute(
						CedulaConstants.CEDULA_CEDULA,
						cedulaGrid.getSelectedRecord().getAttribute(
								CedulaConstants.CEDULA_CEDULA));				
				criteria.setAttribute(
						CedulaConstants.CEDULA_STATUS,
						cedulaGrid.getSelectedRecord().getAttribute(
								CedulaConstants.CEDULA_STATUS));			
				cedulacikkGrid.fetchData(criteria);
				selectIButton.setDisabled(false);
			}
		});

		return middleLayout;

	}
	
	public Canvas printCedula(String cedula,String status, final Canvas retLayout) {
		DisplayRequest.counterInit();

		final VLayout middleLayout = new VLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		final VLayout middleLayout1 = new VLayout();
		middleLayout1.setAlign(Alignment.CENTER);
		middleLayout1.setStyleName("middle");

		HLayout titleLayout = new HLayout();
		titleLayout.setDefaultLayoutAlign(Alignment.CENTER);
		titleLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		titleLayout.setStyleName("middle");
		titleLayout.setHeight("3%");
		titleLayout.setWidth("80%");

		Label titleLabel = new Label(cedula + " - " + status);
		titleLabel.setAlign(Alignment.CENTER);
		titleLabel.setWidth("80%");
		titleLayout.addMember(titleLabel);

		final VLayout cedulacikkLayout = new VLayout();
		cedulacikkLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final CedulacikkDataSource cedulacikkDataSource = new CedulacikkDataSource() {

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

		cedulacikkDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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

		final ListGrid cedulacikkGrid = new ListGrid();
		cedulacikkGrid.setTitle(cedulaLabels.cedulacikk());
		cedulacikkGrid.setWidth("60%");
		cedulacikkGrid.setShowHeaderContextMenu(false);
		cedulacikkGrid.setShowHeaderMenuButton(false);
		cedulacikkGrid.setCanSort(false);
		cedulacikkGrid.setShowAllRecords(true);
		cedulacikkGrid.setDataSource(cedulacikkDataSource);

		Criteria criteria = new Criteria();
		criteria.setAttribute(
				CedulaConstants.CEDULA_CEDULA,
				cedula);				
		criteria.setAttribute(
				CedulaConstants.CEDULA_STATUS,
				status);			
		cedulacikkGrid.fetchData(criteria);

		ListGridField cikkszamGridField = new ListGridField(
				CedulaConstants.CEDULACIKK_CIKKSZAM);
	
		ListGridField szinkodGridField = new ListGridField(
				CedulaConstants.CEDULACIKK_SZINKOD);
		szinkodGridField.setWidth("10%");
		
		ListGridField exportkartonGridField = new ListGridField(
				CedulaConstants.CEDULACIKK_EXPORTKARTON);
		exportkartonGridField.setWidth("20%");

		ListGridField kiskartonGridField = new ListGridField(
				CedulaConstants.CEDULACIKK_KISKARTON);
		kiskartonGridField.setWidth("20%");

		ListGridField darabGridField = new ListGridField(
				CedulaConstants.CEDULACIKK_DARAB);
		darabGridField.setWidth("20%");

		cedulacikkGrid.setFields(cikkszamGridField,szinkodGridField,exportkartonGridField,kiskartonGridField,darabGridField);

		cedulacikkLayout.addMember(cedulacikkGrid);
	
		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("100%");

		HLayout printButtonLayout = new HLayout();
		printButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		IButton printIButton = new IButton(commonLabels.print());
		printButtonLayout.setAlign(Alignment.CENTER);
		printButtonLayout.addMember(printIButton);

		HLayout okButtonLayout = new HLayout();
		okButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		IButton okIButton = new IButton(commonLabels.ok());
		okButtonLayout.setAlign(Alignment.CENTER);
		okButtonLayout.addMember(okIButton);

		buttonsLayout.addMember(printButtonLayout);
		buttonsLayout.addMember(okButtonLayout);
		
		middleLayout1.addMember(titleLayout);
		middleLayout1.addMember(cedulacikkLayout);
		middleLayout.addMember(middleLayout1);
		middleLayout.addMember(buttonsLayout);

		okIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {			
				middleLayout.removeMembers(middleLayout.getMembers());
				middleLayout.addMember(retLayout);																								
			}
		});
	
		printIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {			
				 Canvas.showPrintPreview(middleLayout1);  																		
			}
		});

		return middleLayout;
		
	}
	
		
}
