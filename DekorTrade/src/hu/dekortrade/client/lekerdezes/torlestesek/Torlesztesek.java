package hu.dekortrade.client.lekerdezes.torlestesek;

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
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Torlesztesek {

	private TorlesztesekLabels torlesztesekLabels = GWT
			.create(TorlesztesekLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	public Canvas get() {
		
		final VLayout middleLayout = new VLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		HLayout titleLayout = new HLayout();
		titleLayout.setDefaultLayoutAlign(Alignment.CENTER);
		titleLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		titleLayout.setStyleName("middle");
		titleLayout.setHeight("3%");
		titleLayout.setWidth("100%");

		VLayout fizetesLayout = new VLayout();
		fizetesLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final TorlesztesekDataSource torlesztesekDataSource = new TorlesztesekDataSource() {

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

		 torlesztesekDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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
		fizetesGrid.setTitle(torlesztesekLabels.torlesztesek());
		fizetesGrid.setWidth("60%");
		fizetesGrid.setShowHeaderContextMenu(false);
		fizetesGrid.setShowHeaderMenuButton(false);
		fizetesGrid.setCanSort(false);
		fizetesGrid.setShowAllRecords(true);
		fizetesGrid.setDataSource(torlesztesekDataSource);
		fizetesGrid.setAutoFetchData(true);

		ListGridField cedulaGridField = new ListGridField(
				TorlesztesekConstants.TORLESZTESEK_CEDULA);
		cedulaGridField.setWidth("10%");

		ListGridField fizetGridField = new ListGridField(
				TorlesztesekConstants.TORLESZTESEK_FIZET);
		fizetGridField.setWidth("30%");

		ListGridField fizeteurGridField = new ListGridField(
				TorlesztesekConstants.TORLESZTESEK_FIZETEUR);
		fizeteurGridField.setWidth("30%");

		ListGridField fizetusdGridField = new ListGridField(
				TorlesztesekConstants.TORLESZTESEK_FIZETUSD);
		fizetusdGridField.setWidth("30%");

		fizetesGrid.setFields(cedulaGridField, fizetGridField, fizeteurGridField,fizetusdGridField);

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

		HLayout selectButtonLayout = new HLayout();
		selectButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton selectIButton = new IButton(commonLabels.select());
		selectIButton.setDisabled(true);
		selectButtonLayout.setAlign(Alignment.CENTER);
		selectButtonLayout.addMember(selectIButton);

		buttonsLayout.addMember(refreshButtonLayout);
		buttonsLayout.addMember(selectButtonLayout);

		fizetesLayout.addMember(fizetesGrid);
		fizetesLayout.addMember(buttonsLayout);

		middleLayout.addMember(fizetesLayout);

		fizetesGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				selectIButton.setDisabled(false);
			}
		});

		refreshIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
			}
		});

		selectIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				
			}
		});

		return middleLayout;
	}

}
