package hu.dekortrade.client.penztar.torlesztes;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.torzsadat.vevo.Vevo;
import hu.dekortrade.shared.Constants;

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
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Torlesztes {

	private TorlesztesLabels torlesztesLabels = GWT.create(TorlesztesLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);
	
	public Canvas get() {
		DisplayRequest.counterInit();

		final HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		Vevo vevo = new Vevo();
		middleLayout.addMember(vevo.get(Constants.MENU_PENZTAR_TORLESZTES));

		return middleLayout;
	}

	public Canvas printTorlesztes(final String cedula, String vevonev, final String menu) {
		DisplayRequest.counterInit();

		final VLayout middleLayout = new VLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		HLayout titleLayout = new HLayout();
		titleLayout.setDefaultLayoutAlign(Alignment.CENTER);
		titleLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		titleLayout.setStyleName("middle");
		titleLayout.setHeight("3%");
		titleLayout.setWidth("100%");

		Label titleLabel = new Label(cedula + " - " + torlesztesLabels.torlesztes() + " - " + vevonev);
		titleLabel.setAlign(Alignment.CENTER);
		titleLabel.setWidth("100%");
		titleLayout.addMember(titleLabel);

		VLayout fizetesLayout = new VLayout();
		fizetesLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final TorlesztesDataSource torlesztesDataSource = new TorlesztesDataSource(
				cedula) {

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

		torlesztesDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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
		fizetesGrid.setTitle(torlesztesLabels.torlesztes());
		fizetesGrid.setWidth("60%");
		fizetesGrid.setShowHeaderContextMenu(false);
		fizetesGrid.setShowHeaderMenuButton(false);
		fizetesGrid.setCanSort(false);
		fizetesGrid.setShowAllRecords(true);
		fizetesGrid.setDataSource(torlesztesDataSource);
		fizetesGrid.setAutoFetchData(true);

		ListGridField fizetGridField = new ListGridField(
				TorlesztesConstants.TORLESZTES_FIZET);
		fizetGridField.setWidth("33%");

		ListGridField fizeteurGridField = new ListGridField(
				TorlesztesConstants.TORLESZTES_FIZETEUR);
		fizeteurGridField.setWidth("34%");

		ListGridField fizetusdGridField = new ListGridField(
				TorlesztesConstants.TORLESZTES_FIZETUSD);
		fizetusdGridField.setWidth("33%");

		fizetesGrid.setFields(fizetGridField, fizeteurGridField,fizetusdGridField);

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("80%");

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

		fizetesLayout.addMember(titleLayout);
		fizetesLayout.addMember(fizetesGrid);
		fizetesLayout.addMember(buttonsLayout);

		middleLayout.addMember(fizetesLayout);

		okIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				middleLayout.removeMembers(middleLayout.getMembers());

				if (menu.equals(Constants.MENU_PENZTAR_TORLESZTES)) {
					Torlesztes torlesztes = new Torlesztes();
					middleLayout.addMember(torlesztes.get());
				}
			}
		});

		printIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Canvas.showPrintPreview(middleLayout);
			}
		});

		return middleLayout;

	}

}
