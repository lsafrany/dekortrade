package hu.dekortrade.client.eladas;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.client.kosarraktar.KosarRaktar;
import hu.dekortrade.client.torzsadat.vevo.Vevo;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.VevoKosarSer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.events.ErrorEvent;
import com.smartgwt.client.data.events.HandleErrorHandler;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ExpansionMode;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Eladas {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private EladasLabels eladasLabels = GWT.create(EladasLabels.class);
	
	private CommonLabels commonLabels = GWT.create(CommonLabels.class);
	
	public Canvas get() {
		DisplayRequest.counterInit();

		final HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		final VLayout loadLayout = new VLayout();
		loadLayout.setAlign(Alignment.CENTER);

		Label loadLabel = new Label(commonLabels.loading());
		loadLabel.setAlign(Alignment.CENTER);
		loadLayout.addMember(loadLabel);

		middleLayout.addMember(loadLayout);

		DisplayRequest.startRequest();
		dekorTradeService.getVevoKosar(UserInfo.userId,
				Constants.MENU_ELADAS,
				new AsyncCallback<VevoKosarSer>() {
					public void onFailure(Throwable caught) {
						DisplayRequest.serverResponse();
						if (caught instanceof SQLExceptionSer)
							SC.warn(commonLabels.server_sqlerror() + " : "
									+ caught.getMessage());
						else
							SC.warn(commonLabels.server_error());
					}

					public void onSuccess(final VevoKosarSer result) {
						DisplayRequest.serverResponse();
						middleLayout.removeMembers(middleLayout.getMembers());
						middleLayout.addMember(process(result));
					}
				});
		return middleLayout;

	}

	public Canvas process(VevoKosarSer result) {

		DisplayRequest.counterInit();

		final HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		if (result == null) {
			Vevo vevo = new Vevo();
			middleLayout.addMember(vevo
					.get(Constants.MENU_ELADAS));
		} else {
			KosarRaktar kosarRaktar = new KosarRaktar();
			middleLayout
					.addMember(kosarRaktar.get("", UserInfo.userId,
							result.getVevo(), result.getVevonev(),
							result.getVevotipus(),
							Constants.MENU_ELADAS));
		}

		return middleLayout;

	}

	public Canvas rendeles (final String vevo) {

		DisplayRequest.counterInit();

		final VLayout middleLayout = new VLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		middleLayout.setStyleName("middle");

		final RendelesDataSource rendelesDataSource = new RendelesDataSource(
				vevo) {

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

		rendelesDataSource.addHandleErrorHandler(new HandleErrorHandler() {
			public void onHandleError(ErrorEvent event) {

				if (event.getResponse().getStatus() == DSResponse.STATUS_FAILURE) {
					if (event.getResponse().getAttribute(
							ClientConstants.SERVER_ERROR) != null)
						SC.warn(commonLabels.server_error());
					else if (event.getResponse().getAttribute(
							ClientConstants.SERVER_SQLERROR) != null)
						if (event.getResponse()
								.getAttribute(ClientConstants.SERVER_SQLERROR)
								.equals(Constants.EXISTSID)) {
							SC.warn(commonLabels.existingid());
						} else {
							SC.warn(commonLabels.server_sqlerror()
									+ " : "
									+ event.getResponse().getAttribute(
											ClientConstants.SERVER_SQLERROR));
						}
					event.cancel();
				}
			}
		});

		final ListGrid rendelesGrid = new ListGrid();
		rendelesGrid.setTitle(eladasLabels.rendeles());
		rendelesGrid.setWidth("60%");
		rendelesGrid.setShowHeaderContextMenu(false);
		rendelesGrid.setShowHeaderMenuButton(false);
		rendelesGrid.setCanSort(false);
		rendelesGrid.setShowAllRecords(true);
		rendelesGrid.setDataSource(rendelesDataSource);
		rendelesGrid.setCanExpandRecords(true);
		rendelesGrid.setExpansionMode(ExpansionMode.DETAILS);

		ListGridField rendelesGridField = new ListGridField(
				EladasConstants.RENDELES_RENDELES);
		rendelesGridField.setWidth("10%");

		ListGridField cikkszamGridField = new ListGridField(
				EladasConstants.RENDELES_CIKKSZAM);

		ListGridField szinkodGridField = new ListGridField(
				EladasConstants.RENDELES_SZINKOD);
		szinkodGridField.setWidth("10%");

		ListGridField exportkartonGridField = new ListGridField(
				EladasConstants.RENDELES_EXPORTKARTON);
		exportkartonGridField.setWidth("10%");

		ListGridField kiskartonGridField = new ListGridField(
				EladasConstants.RENDELES_KISKARTON);
		kiskartonGridField.setWidth("10%");
		
		ListGridField darabGridField = new ListGridField(
				EladasConstants.RENDELES_DARAB);
		darabGridField.setWidth("10%");
		
		rendelesGrid.setFields(cikkszamGridField, szinkodGridField,exportkartonGridField,kiskartonGridField,darabGridField);

		middleLayout.addMember(rendelesGrid);

		return middleLayout;
	}
}
