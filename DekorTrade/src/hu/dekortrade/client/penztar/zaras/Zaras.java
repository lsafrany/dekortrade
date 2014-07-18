package hu.dekortrade.client.penztar.zaras;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.client.lekerdezes.zarasok.Zarasok;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.ZarasEgyenlegSer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.events.ErrorEvent;
import com.smartgwt.client.data.events.HandleErrorHandler;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.IsFloatValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Zaras {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	private ZarasLabels zarasLabels = GWT.create(ZarasLabels.class);

	private IsFloatValidator isFloatValidator = new IsFloatValidator();
	
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
		dekorTradeService.getElozoZaras(new AsyncCallback<ZarasEgyenlegSer>() {
			public void onFailure(Throwable caught) {
				DisplayRequest.serverResponse();
				if (caught instanceof SQLExceptionSer)
					SC.warn(commonLabels.server_sqlerror() + " : "
							+ caught.getMessage());
				else
					SC.warn(commonLabels.server_error());
			}

			public void onSuccess(final ZarasEgyenlegSer result) {
				DisplayRequest.serverResponse();
				middleLayout.removeMembers(middleLayout.getMembers());
				middleLayout.addMember(process(result));
			}
		});
		return middleLayout;

	}

	public Canvas process(final ZarasEgyenlegSer zarasEgyenlegSer) {
		DisplayRequest.counterInit();

		final HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		HLayout elozoLayout = new HLayout();
		elozoLayout.setHeight("3%");
		elozoLayout.setWidth("80%");

		HLayout elozousdCurrLabelLayout = new HLayout();
		elozousdCurrLabelLayout.setWidth("80%");
		Label elozousdCurrLabel = new Label("USD :");
		elozousdCurrLabel.setAlign(Alignment.CENTER);
		elozousdCurrLabelLayout.addMember(elozousdCurrLabel);

		HLayout elozousdLabelLayout = new HLayout();
		elozousdLabelLayout.setWidth("20%");
		final Label elozousdLabel = new Label(zarasEgyenlegSer.getEgyenlegusd().toString());
		elozousdLabel.setAlign(Alignment.CENTER);
		elozousdLabelLayout.addMember(elozousdLabel);

		elozoLayout.addMember(elozousdCurrLabelLayout);
		elozoLayout.addMember(elozousdLabelLayout);

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
		fizetesGrid.setTitle(zarasLabels.fizetes());
		fizetesGrid.setWidth("80%");
		fizetesGrid.setShowHeaderContextMenu(false);
		fizetesGrid.setShowHeaderMenuButton(false);
		fizetesGrid.setCanSort(false);
		fizetesGrid.setShowAllRecords(true);
		fizetesGrid.setDataSource(fizetesDataSource);
		fizetesGrid.setAutoFetchData(true);

		ListGridField cedulaGridField = new ListGridField(
				ZarasConstants.FIZETES_CEDULA);
		cedulaGridField.setWidth("8%");

		ListGridField vevoGridField = new ListGridField(
				ZarasConstants.FIZETES_VEVONEV);

		ListGridField tipusGridField = new ListGridField(
				ZarasConstants.FIZETES_TIPUS);
		tipusGridField.setWidth("15%");

		ListGridField penztarosGridField = new ListGridField(
				ZarasConstants.FIZETES_PENZTAROSNEV);
		penztarosGridField.setWidth("20%");

		ListGridField fizetGridField = new ListGridField(
				ZarasConstants.FIZETES_FIZET);
		fizetGridField.setWidth("8%");

		ListGridField fizeteurGridField = new ListGridField(
				ZarasConstants.FIZETES_FIZETEUR);
		fizeteurGridField.setWidth("8%");

		ListGridField fizetusdGridField = new ListGridField(
				ZarasConstants.FIZETES_FIZETUSD);
		fizetusdGridField.setWidth("8%");

		ListGridField datumGridField = new ListGridField(
				ZarasConstants.FIZETES_DATUM);
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
		okIButton.setDisabled(true);
		okButtonLayout.setAlign(Alignment.CENTER);
		okButtonLayout.addMember(okIButton);

		buttonsLayout.addMember(refreshButtonLayout);
		buttonsLayout.addMember(okButtonLayout);

		fizetesLayout.addMember(elozoLayout);
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
				if (fizetesGrid.getRecords().length != 0) {
					for (int i = 0; i < fizetesGrid.getRecords().length; i++) {
						if (fizetesGrid.getRecord(i).getAttribute(
								ZarasConstants.FIZETES_FIZETUSD) != null) {
							fizetusd = fizetusd
									+ fizetesGrid
											.getRecord(i)
											.getAttributeAsFloat(
													ZarasConstants.FIZETES_FIZETUSD);
						}
					}
					okIButton.setDisabled(false);
				} else {
					okIButton.setDisabled(true);
				}
				usdLabel.setContents(NumberFormat.getFormat("#.0000").format(
						fizetusd));
			}
		});

		okIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
							
				final Window winModal = new Window();
				winModal.setWidth(300);
				winModal.setHeight(150);
				winModal.setTitle(zarasLabels.zaraskivet());
				winModal.setShowMinimizeButton(false);
				winModal.setShowCloseButton(false);
				winModal.setIsModal(true);
				winModal.setShowModalMask(true);
				winModal.centerInPage();
				
				final DynamicForm form = new DynamicForm();
				form.setHeight("40%");
				form.setNumCols(2);
				form.setColWidths("40%", "*");
		
				final TextItem kivet = new TextItem();   
				kivet.setTitle(zarasLabels.kivet());
				kivet.setValue("0");
				kivet.setValidators(isFloatValidator);

				final TextItem kiveteur = new TextItem();   
				kiveteur.setTitle(zarasLabels.kiveteur());
				kiveteur.setValue("0");
				kiveteur.setValidators(isFloatValidator);

				final TextItem kivetusd = new TextItem();   
				kivetusd.setTitle(zarasLabels.kivetusd());
				kivetusd.setValue(usdLabel.getContents());
				kivetusd.setValidators(isFloatValidator);
				
				form.setFields(kivet,kiveteur,kivetusd);		
		
				final HLayout buttonsLayout = new HLayout();
				buttonsLayout.setWidth100();

				HLayout okLayout = new HLayout();
				okLayout.setAlign(Alignment.CENTER);
				IButton okIButton = new IButton(commonLabels.ok());
				okIButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
							
						SC.ask(commonLabels.sure(), new BooleanCallback() {
							public void execute(Boolean value) {
								if (value != null && value) {
									DisplayRequest.startRequest();
									dekorTradeService.createZaras(UserInfo.userId,zarasEgyenlegSer.getEgyenleghuf(),
											zarasEgyenlegSer.getEgyenlegeur(),
											zarasEgyenlegSer.getEgyenlegusd(),
											new Float(kivet.getValueAsString()),
											new Float(kiveteur.getValueAsString()),
											new Float(kivetusd.getValueAsString()),
											new AsyncCallback<String>() {
												public void onFailure(Throwable caught) {
													DisplayRequest.serverResponse();
													if (caught instanceof SQLExceptionSer)
														SC.warn(commonLabels
																.server_sqlerror()
																+ " : "
																+ caught.getMessage());
													else
														SC.warn(commonLabels
																.server_error());
												}
	
												public void onSuccess(String result) {
													DisplayRequest.serverResponse();
													winModal.destroy();
													middleLayout
															.removeMembers(middleLayout
																	.getMembers());
													middleLayout
															.addMember(printZaras(
																	result,
																	Constants.MENU_PENZTAR_ZARAS,new Float(kivet.getValueAsString()),
																	new Float(kiveteur.getValueAsString()),
																	new Float(kivetusd.getValueAsString())));
												}
											});
								}
							}
						});
					}
				});
				okLayout.addMember(okIButton);
				
				HLayout cancelLayout = new HLayout();
				cancelLayout.setAlign(Alignment.CENTER);
				IButton cancelIButton = new IButton(commonLabels.cancel());
				cancelIButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						winModal.destroy();
					}
				});
				cancelLayout.addMember(cancelIButton);

				buttonsLayout.addMember(okLayout);
				buttonsLayout.addMember(cancelLayout);
				
				winModal.addItem(form);
				winModal.addItem(buttonsLayout);
				
				winModal.show();
			
				
			}
		});

		return middleLayout;

	}

	public Canvas printZaras(final String zaras, final String menu, Float kivethuf,Float kiveteur, Float kivetusd) {
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

		Label titleLabel = new Label(zaras + " - " + zarasLabels.zaras());
		titleLabel.setAlign(Alignment.CENTER);
		titleLabel.setWidth("100%");
		titleLayout.addMember(titleLabel);

		VLayout fizetesLayout = new VLayout();
		fizetesLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final ZarasFizetesDataSource zarasfizetesDataSource = new ZarasFizetesDataSource(
				zaras) {

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

		zarasfizetesDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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
		fizetesGrid.setTitle(zarasLabels.fizetes());
		fizetesGrid.setWidth("80%");
		fizetesGrid.setShowHeaderContextMenu(false);
		fizetesGrid.setShowHeaderMenuButton(false);
		fizetesGrid.setCanSort(false);
		fizetesGrid.setShowAllRecords(true);
		fizetesGrid.setDataSource(zarasfizetesDataSource);
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

		ListGridField datumGridField = new ListGridField(
				ZarasConstants.ZARASFIZETES_DATUM);
		datumGridField.setWidth("10%");

		fizetesGrid.setFields(cedulaGridField, vevoGridField, tipusGridField,
				penztarosGridField, fizetGridField, fizeteurGridField,
				fizetusdGridField, datumGridField);

		HLayout fizetLayout = new HLayout();
		fizetLayout.setHeight("3%");
		fizetLayout.setWidth("80%");

		HLayout usdCurrLabelLayout = new HLayout();
		usdCurrLabelLayout.setWidth("80%");
		Label usdCurrLabel = new Label(zarasLabels.fizetusd());
		usdCurrLabel.setAlign(Alignment.CENTER);
		usdCurrLabelLayout.addMember(usdCurrLabel);

		HLayout usdLabelLayout = new HLayout();
		usdLabelLayout.setWidth("20%");
		final Label usdLabel = new Label("0");
		usdLabel.setAlign(Alignment.CENTER);
		usdLabelLayout.addMember(usdLabel);

		fizetLayout.addMember(usdCurrLabelLayout);
		fizetLayout.addMember(usdLabelLayout);

		HLayout kivetLayout = new HLayout();
		kivetLayout.setHeight("3%");
		kivetLayout.setWidth("80%");

		HLayout kivetusdCurrLabelLayout = new HLayout();
		kivetusdCurrLabelLayout.setWidth("80%");
		Label kivetusdCurrLabel = new Label(zarasLabels.kivetusd());
		kivetusdCurrLabel.setAlign(Alignment.CENTER);
		kivetusdCurrLabelLayout.addMember(kivetusdCurrLabel);

		HLayout kivetusdLabelLayout = new HLayout();
		kivetusdLabelLayout.setWidth("20%");
		final Label kivetusdLabel = new Label(kivetusd.toString());
		kivetusdLabel.setAlign(Alignment.CENTER);
		kivetusdLabelLayout.addMember(kivetusdLabel);

		kivetLayout.addMember(kivetusdCurrLabelLayout);
		kivetLayout.addMember(kivetusdLabelLayout);

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
		fizetesLayout.addMember(fizetLayout);
		fizetesLayout.addMember(kivetLayout);
		fizetesLayout.addMember(buttonsLayout);

		middleLayout.addMember(fizetesLayout);

		fizetesGrid.addDataArrivedHandler(new DataArrivedHandler() {
			public void onDataArrived(DataArrivedEvent event) {

				float fizet = 0;
				for (int i = 0; i < fizetesGrid.getRecords().length; i++) {
					if (fizetesGrid.getRecord(i).getAttribute(
							ZarasConstants.ZARASFIZETES_FIZETUSD) != null) {
						fizet = fizet
								+ fizetesGrid.getRecord(i).getAttributeAsFloat(
										ZarasConstants.ZARASFIZETES_FIZETUSD);
					}

				}
				usdLabel.setContents(NumberFormat.getFormat("#.0000").format(
						fizet));
			}
		});

		okIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				middleLayout.removeMembers(middleLayout.getMembers());

				if (menu.equals(Constants.MENU_PENZTAR_ZARAS)) {
					Zaras zaras = new Zaras();
					middleLayout.addMember(zaras.get());
				}
				if (menu.equals(Constants.MENU_LEKERDEZES_ZARASOK)) {
					Zarasok zarasok = new Zarasok();
					middleLayout.addMember(zarasok.get());
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
