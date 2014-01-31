package hu.dekortrade99.client.archive;

import hu.dekortrade99.client.ClientConstants;
import hu.dekortrade99.client.CommonLabels;
import hu.dekortrade99.client.DisplayRequest;
import hu.dekortrade99.client.UserInfo;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
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
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Archive {

	private ArchiveLabels archiveLabels = GWT.create(ArchiveLabels.class);

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
		rendeltGrid.setTitle(archiveLabels.rendeles());
		rendeltGrid.setWidth("70%");
		rendeltGrid.setShowHeaderContextMenu(false);
		rendeltGrid.setShowHeaderMenuButton(false);
		rendeltGrid.setCanSort(false);
		rendeltGrid.setShowAllRecords(true);
		rendeltGrid.setDataSource(rendeltDataSource);
		Criteria criteria = new Criteria();
		criteria.setAttribute(ArchiveConstants.RENDELT_ROVIDNEV,
				UserInfo.userId);
		rendeltGrid.fetchData(criteria);

		ListGridField rendelesGridField = new ListGridField(
				ArchiveConstants.RENDELT_RENDELES);

		ListGridField datumGridField = new ListGridField(
				ArchiveConstants.RENDELT_DATUM);
		datumGridField.setWidth("40%");

		ListGridField statuszGridField = new ListGridField(
				ArchiveConstants.RENDELT_STATUSZ);
		statuszGridField.setWidth("15%");

		rendeltGrid.setFields(rendelesGridField, datumGridField,
				statuszGridField);

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("100%");

		IButton frissitIButton = new IButton(archiveLabels.frissit());

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
		rendeltcikkGrid.setTitle(archiveLabels.rendeltcikk());
		rendeltcikkGrid.setWidth("50%");
		rendeltcikkGrid.setShowHeaderContextMenu(false);
		rendeltcikkGrid.setShowHeaderMenuButton(false);
		rendeltcikkGrid.setCanSort(false);
		rendeltcikkGrid.setShowAllRecords(true);
		rendeltcikkGrid.setDataSource(rendeltcikkDataSource);

		ListGridField cikkszamGridField = new ListGridField(
				ArchiveConstants.RENDELTCIKK_CIKKSZAM);

		ListGridField exportkartonGridField = new ListGridField(
				ArchiveConstants.RENDELTCIKK_EXPORTKARTON);
		exportkartonGridField.setWidth("30%");

		rendeltcikkGrid.setFields(cikkszamGridField, exportkartonGridField);

		rendeltcikkLayout.addMember(rendeltcikkGrid);

		middleLayout.addMember(rendeltLayout);
		middleLayout.addMember(rendeltcikkLayout);

		frissitIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Criteria criteria = new Criteria();
				criteria.setAttribute(ArchiveConstants.RENDELT_ROVIDNEV,
						UserInfo.userId);
				rendeltGrid.invalidateCache();
				rendeltGrid.fetchData(criteria);
				rendeltcikkGrid.setData(new ListGridRecord[] {});
			}
		});

		rendeltGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				Criteria criteria = new Criteria();
				criteria.setAttribute(ArchiveConstants.RENDELTCIKK_ROVIDNEV,
						UserInfo.userId);
				criteria.setAttribute(
						ArchiveConstants.RENDELTCIKK_RENDELES,
						rendeltGrid.getSelectedRecord().getAttribute(
								ArchiveConstants.RENDELT_RENDELES));
				rendeltcikkGrid.fetchData(criteria);
			}
		});

		return middleLayout;

	}
}
