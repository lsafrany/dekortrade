package hu.dekortrade.client.lekerdezes.cedulak;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.client.penztar.fizetes.Fizetes;
import hu.dekortrade.client.rendeles.veglegites.Veglegesites;
import hu.dekortrade.client.torzsadat.vevo.Vevo;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
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
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Cedulak {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private CedulakLabels cedulaLabels = GWT.create(CedulakLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	public Canvas get(final String vevo, final String menu) {
		DisplayRequest.counterInit();

		final HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		final HLayout selectLayout = new HLayout();
		selectLayout.setAlign(Alignment.CENTER);
		selectLayout.setHeight("3%");
		selectLayout.setWidth("70%");

		final DynamicForm form = new DynamicForm();
		form.setHeight("40%");
		form.setNumCols(2);
		form.setColWidths("40%", "*");

		final SelectItem cedulatipus = new SelectItem();  
		cedulatipus.setTitle(cedulaLabels.cedulatipus());
		cedulatipus.setValueMap(ClientConstants.getCedulaTipus()); 
		
		form.setFields(cedulatipus);
		
		HLayout selectokButtonLayout = new HLayout();
		selectokButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton selectokIButton = new IButton(commonLabels.filter());
		selectokButtonLayout.setAlign(Alignment.CENTER);
		selectokButtonLayout.addMember(selectokIButton);
		
		selectLayout.addMember(form);
		selectLayout.addMember(selectokButtonLayout);
		
		VLayout cedulaLayout = new VLayout();
		cedulaLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final CedulakDataSource cedulaDataSource = new CedulakDataSource(vevo,
				menu) {

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
		cedulaGrid.setWidth("90%");
		cedulaGrid.setShowHeaderContextMenu(false);
		cedulaGrid.setShowHeaderMenuButton(false);
		cedulaGrid.setCanSort(false);
		cedulaGrid.setShowAllRecords(true);
		cedulaGrid.setDataSource(cedulaDataSource);
	
		ListGridField cedulaGridField = new ListGridField(
				CedulakConstants.CEDULA_CEDULA);
		cedulaGridField.setWidth("10%");

		ListGridField statusGridField = new ListGridField(
				CedulakConstants.CEDULA_STATUS);
		statusGridField.setWidth("20%");

		ListGridField vevoGridField = new ListGridField(
				CedulakConstants.CEDULA_VEVONEV);

		ListGridField eladoGridField = new ListGridField(
				CedulakConstants.CEDULA_ELADONEV);
		eladoGridField.setWidth("15%");

		ListGridField datumGridField = new ListGridField(
				CedulakConstants.CEDULA_DATUM);
		datumGridField.setWidth("15%");

		cedulaGrid.setFields(cedulaGridField, statusGridField, vevoGridField,
				eladoGridField, datumGridField);

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("60%");

		HLayout refreshButtonLayout = new HLayout();
		IButton refreshIButton = new IButton(commonLabels.refresh());

		if (menu.equals(Constants.MENU_LEKERDEZES_CEDULAK)
				|| menu.equals(Constants.MENU_PENZTAR_FIZETES)) {
			refreshButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
			refreshButtonLayout.setAlign(Alignment.CENTER);
			refreshButtonLayout.addMember(refreshIButton);
		}

		HLayout selectButtonLayout = new HLayout();
		selectButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton selectIButton = new IButton(commonLabels.select());
		selectIButton.setDisabled(true);
		selectButtonLayout.setAlign(Alignment.CENTER);
		selectButtonLayout.addMember(selectIButton);

		HLayout cancelButtonLayout = new HLayout();
		IButton cancelIButton = new IButton(commonLabels.cancel());

		if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)) {
			cancelButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
			cancelButtonLayout.setAlign(Alignment.CENTER);
			cancelButtonLayout.addMember(cancelIButton);
		}

		if (menu.equals(Constants.MENU_LEKERDEZES_CEDULAK)
				|| menu.equals(Constants.MENU_PENZTAR_FIZETES)) {
			buttonsLayout.addMember(refreshButtonLayout);
		}
		buttonsLayout.addMember(selectButtonLayout);

		if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)) {
			buttonsLayout.addMember(cancelButtonLayout);
		}

		cedulaLayout.addMember(selectLayout);
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
		cedulacikkGrid.setWidth("90%");
		cedulacikkGrid.setShowHeaderContextMenu(false);
		cedulacikkGrid.setShowHeaderMenuButton(false);
		cedulacikkGrid.setCanSort(false);
		cedulacikkGrid.setShowAllRecords(true);
		cedulacikkGrid.setDataSource(cedulacikkDataSource);

		ListGridField cikkszamGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_CIKKSZAM);

		ListGridField szinkodGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_SZINKOD);
		szinkodGridField.setWidth("10%");

		ListGridField exportkartonGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_EXPORTKARTON);
		exportkartonGridField.setWidth("20%");

		ListGridField kiskartonGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_KISKARTON);
		kiskartonGridField.setWidth("10%");

		ListGridField darabGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_DARAB);
		darabGridField.setWidth("10%");

		ListGridField arusdGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_ARUSD);
		arusdGridField.setWidth("10%");

		ListGridField fizetusdGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_FIZETUSD);
		fizetusdGridField.setWidth("10%");

		cedulacikkGrid.setFields(cikkszamGridField, szinkodGridField,
				exportkartonGridField, kiskartonGridField, darabGridField,
				arusdGridField, fizetusdGridField);

		cedulacikkLayout.addMember(cedulacikkGrid);

		HLayout fizetLayout = new HLayout();
		fizetLayout.setHeight("3%");
		fizetLayout.setWidth("90%");

		HLayout usdCurrLabelLayout = new HLayout();
		usdCurrLabelLayout.setWidth("70%");
		Label usdCurrLabel = new Label("USD :");
		usdCurrLabel.setAlign(Alignment.CENTER);
		usdCurrLabelLayout.addMember(usdCurrLabel);

		HLayout usdLabelLayout = new HLayout();
		usdLabelLayout.setWidth("30%");
		final Label usdLabel = new Label("0");
		usdLabel.setAlign(Alignment.CENTER);
		usdLabelLayout.addMember(usdLabel);

		fizetLayout.addMember(usdCurrLabelLayout);
		fizetLayout.addMember(usdLabelLayout);
		cedulacikkLayout.addMember(fizetLayout);

		middleLayout.addMember(cedulaLayout);
		middleLayout.addMember(cedulacikkLayout);

		selectokIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				cedulaGrid.invalidateCache();
				Criteria criteria = new Criteria();
				criteria.setAttribute(CedulakConstants.CEDULA_STATUS,cedulatipus.getValueAsString());
				cedulaGrid.fetchData(criteria);
				cedulacikkGrid.setData(new ListGridRecord[] {});
				selectIButton.setDisabled(true);
			}
		});

		if (menu.equals(Constants.MENU_LEKERDEZES_CEDULAK)
				|| menu.equals(Constants.MENU_PENZTAR_FIZETES)) {
			refreshIButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					cedulaGrid.invalidateCache();
					Criteria criteria = new Criteria();
					criteria.setAttribute(CedulakConstants.CEDULA_STATUS,cedulatipus.getValueAsString());
					cedulaGrid.fetchData(criteria);
					cedulacikkGrid.setData(new ListGridRecord[] {});
					selectIButton.setDisabled(true);
				}
			});
		}

		if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)) {
			cancelIButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					middleLayout.removeMembers(middleLayout.getMembers());
					Vevo vevo = new Vevo();
					middleLayout.addMember(vevo
							.get(Constants.MENU_RENDELES_VEGLEGESITES));
				}
			});
		}

		selectIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				if (menu.equals(Constants.MENU_LEKERDEZES_CEDULAK)) {
					middleLayout.removeMembers(middleLayout.getMembers());
					middleLayout.addMember(printCedula(
							cedulaGrid.getSelectedRecord().getAttribute(
									CedulakConstants.CEDULA_CEDULA),
							cedulaGrid.getSelectedRecord().getAttribute(
									CedulakConstants.CEDULA_STATUS),
							cedulaGrid.getSelectedRecord().getAttribute(
									CedulakConstants.CEDULA_VEVONEV), 									
							cedulaGrid.getSelectedRecord().getAttributeAsFloat(
									CedulakConstants.CEDULA_BEFIZETHUF), 
							cedulaGrid.getSelectedRecord().getAttributeAsFloat(
									CedulakConstants.CEDULA_BEFIZETEUR), 
							cedulaGrid.getSelectedRecord().getAttributeAsFloat(
									CedulakConstants.CEDULA_BEFIZETUSD), 
									menu));
				}
				if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)
						|| menu.equals(Constants.MENU_PENZTAR_FIZETES)) {

					SC.ask(commonLabels.sure(), new BooleanCallback() {
						public void execute(Boolean value) {
							if (value != null && value) {
								DisplayRequest.startRequest();
								dekorTradeService
										.cedulaToKosar(
												UserInfo.userId,
												cedulaGrid
														.getSelectedRecord()
														.getAttribute(
																CedulakConstants.CEDULA_VEVO),
												menu,
												cedulaGrid
														.getSelectedRecord()
														.getAttribute(
																CedulakConstants.CEDULA_CEDULA),
												new AsyncCallback<String>() {
													public void onFailure(
															Throwable caught) {
														DisplayRequest
																.serverResponse();
														if (caught instanceof SQLExceptionSer)
															SC.warn(commonLabels
																	.server_sqlerror()
																	+ " : "
																	+ caught.getMessage());
														else
															SC.warn(commonLabels
																	.server_error());
													}

													public void onSuccess(
															String result) {
														DisplayRequest
																.serverResponse();
														if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)) {
															middleLayout
																	.removeMembers(middleLayout
																			.getMembers());
															Veglegesites finalizeOrder = new Veglegesites();
															middleLayout
																	.addMember(finalizeOrder
																			.get());
														}
														if (menu.equals(Constants.MENU_PENZTAR_FIZETES)) {
															middleLayout
																	.removeMembers(middleLayout
																			.getMembers());
															Fizetes cashPay = new Fizetes();
															middleLayout
																	.addMember(cashPay
																			.get());
														}
													}
												});
							}
						}
					});

				}
			}
		});

		cedulaGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				Criteria criteria = new Criteria();
				criteria.setAttribute(
						CedulakConstants.CEDULA_CEDULA,
						cedulaGrid.getSelectedRecord().getAttribute(
								CedulakConstants.CEDULA_CEDULA));
				criteria.setAttribute(
						CedulakConstants.CEDULA_STATUS,
						cedulaGrid.getSelectedRecord().getAttribute(
								CedulakConstants.CEDULA_STATUS));
				cedulacikkGrid.fetchData(criteria);
				
				if ((menu.equals(Constants.MENU_LEKERDEZES_CEDULAK)) && (cedulaGrid.getSelectedRecord().getAttribute(CedulakConstants.CEDULA_STATUS).equals(Constants.CEDULA_STATUSZ_FIZETENDO_ELORENDELES))) {
					selectIButton.setDisabled(true);
				}
				else {
					selectIButton.setDisabled(false);
				}	
			}
		});

		cedulacikkGrid.addDataArrivedHandler(new DataArrivedHandler() {
			public void onDataArrived(DataArrivedEvent event) {

				float fizetusd = 0;
				for (int i = 0; i < cedulacikkGrid.getRecords().length; i++) {
					if (cedulacikkGrid.getRecord(i).getAttribute(
							CedulakConstants.CEDULACIKK_FIZETUSD) != null) {
						fizetusd = fizetusd
								+ cedulacikkGrid
										.getRecord(i)
										.getAttributeAsFloat(
												CedulakConstants.CEDULACIKK_FIZETUSD);
					}

				}
				usdLabel.setContents(NumberFormat.getFormat("#.0000").format(
						fizetusd));
			}
		});

		return middleLayout;

	}

	public Canvas printCedula(String cedula, String tipus, String vevonev, Float befizet, Float befizeteur, Float befizetusd,
			final String menu) {
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
		titleLayout.setWidth("100%");

		Label titleLabel = new Label(cedula + " - "
				+ ClientConstants.getCedulaTipus().get(tipus) + " : " + vevonev);
		titleLabel.setAlign(Alignment.CENTER);
		titleLabel.setWidth("100%");
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
		cedulacikkGrid.setWidth("80%");
		cedulacikkGrid.setShowHeaderContextMenu(false);
		cedulacikkGrid.setShowHeaderMenuButton(false);
		cedulacikkGrid.setCanSort(false);
		cedulacikkGrid.setShowAllRecords(true);
		cedulacikkGrid.setDataSource(cedulacikkDataSource);

		Criteria criteria = new Criteria();
		criteria.setAttribute(CedulakConstants.CEDULA_CEDULA, cedula);
		criteria.setAttribute(CedulakConstants.CEDULA_STATUS, tipus);
		cedulacikkGrid.fetchData(criteria);

		ListGridField cikkszamGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_CIKKSZAM);

		ListGridField szinkodGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_SZINKOD);
		szinkodGridField.setWidth("10%");

		ListGridField exportkartonGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_EXPORTKARTON);
		exportkartonGridField.setWidth("10%");

		ListGridField kiskartonGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_KISKARTON);
		kiskartonGridField.setWidth("10%");

		ListGridField darabGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_DARAB);
		darabGridField.setWidth("10%");

		ListGridField arusdGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_ARUSD);
		arusdGridField.setWidth("15%");

		ListGridField fizetusdGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_FIZETUSD);
		fizetusdGridField.setWidth("15%");

		cedulacikkGrid.setFields(cikkszamGridField, szinkodGridField,
				exportkartonGridField, kiskartonGridField, darabGridField,
				arusdGridField, fizetusdGridField);

		cedulacikkLayout.addMember(cedulacikkGrid);

		HLayout fizetLayout = new HLayout();
		fizetLayout.setHeight("3%");
		fizetLayout.setWidth("100%");

		HLayout usdCurrLabelLayout = new HLayout();
		usdCurrLabelLayout.setAlign(Alignment.CENTER);
		usdCurrLabelLayout.setWidth("50%");
		Label usdCurrLabel = new Label(cedulaLabels.fizetusd());
		usdCurrLabel.setAlign(Alignment.CENTER);
		usdCurrLabelLayout.addMember(usdCurrLabel);

		HLayout usdLabelLayout = new HLayout();
		usdLabelLayout.setAlign(Alignment.CENTER);
		usdLabelLayout.setWidth("50%");
		final Label usdLabel = new Label("0");
		usdLabel.setAlign(Alignment.CENTER);
		usdLabelLayout.addMember(usdLabel);

		fizetLayout.addMember(usdCurrLabelLayout);
		fizetLayout.addMember(usdLabelLayout);

		HLayout befizetLayout = new HLayout();
		befizetLayout.setHeight("3%");
		befizetLayout.setWidth("100%");

		HLayout beusdCurrLabelLayout = new HLayout();
		beusdCurrLabelLayout.setAlign(Alignment.CENTER);
		beusdCurrLabelLayout.setWidth("50%");
		Label beusdCurrLabel = new Label(cedulaLabels.befizetusd());
		beusdCurrLabel.setAlign(Alignment.CENTER);
		beusdCurrLabelLayout.addMember(beusdCurrLabel);

		HLayout beusdLabelLayout = new HLayout();
		beusdLabelLayout.setAlign(Alignment.CENTER);
		beusdLabelLayout.setWidth("50%");
		final Label beusdLabel = new Label(befizetusd == null ? "" : befizetusd.toString());
		beusdLabel.setAlign(Alignment.CENTER);
		beusdLabelLayout.addMember(beusdLabel);

		befizetLayout.addMember(beusdCurrLabelLayout);
		befizetLayout.addMember(beusdLabelLayout);
		
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
		middleLayout1.addMember(fizetLayout);
		
		if (tipus.equals(Constants.CEDULA_STATUSZ_FIZETETT_ELORENDELES)) {	
			middleLayout1.addMember(befizetLayout);
		}
		middleLayout.addMember(middleLayout1);
		middleLayout.addMember(buttonsLayout);

		cedulacikkGrid.addDataArrivedHandler(new DataArrivedHandler() {
			public void onDataArrived(DataArrivedEvent event) {

				float fizet = 0;
				for (int i = 0; i < cedulacikkGrid.getRecords().length; i++) {
					if (cedulacikkGrid.getRecord(i).getAttribute(
							CedulakConstants.CEDULACIKK_FIZETUSD) != null) {
						fizet = fizet
								+ cedulacikkGrid
										.getRecord(i)
										.getAttributeAsFloat(
												CedulakConstants.CEDULACIKK_FIZETUSD);
					}

				}
				usdLabel.setContents(NumberFormat.getFormat("#.0000").format(
						fizet));
			}
		});

		okIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				middleLayout.removeMembers(middleLayout.getMembers());

				if (menu.equals(Constants.MENU_RENDELES_ELORENDELES)) {
					Vevo vevo = new Vevo();
					middleLayout.addMember(vevo.get(menu));
				}
				if (menu.equals(Constants.MENU_LEKERDEZES_CEDULAK)) {
					middleLayout.addMember(get(null,
							Constants.MENU_LEKERDEZES_CEDULAK));
				}
				if (menu.equals(Constants.MENU_PENZTAR_FIZETES)) {
					Fizetes cashPay = new Fizetes();
					middleLayout.addMember(cashPay.get());
				}
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
