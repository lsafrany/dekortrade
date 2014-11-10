package hu.dekortrade.client.lekerdezes.cedulak;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.client.eladas.Eladas;
import hu.dekortrade.client.kosarraktar.KosarRaktar;
import hu.dekortrade.client.penztar.fizetes.Fizetes;
import hu.dekortrade.client.raktar.kiadas.Kiadas;
import hu.dekortrade.client.rendeles.veglegites.Veglegesites;
import hu.dekortrade.client.torzsadat.vevo.Vevo;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.events.ErrorEvent;
import com.smartgwt.client.data.events.HandleErrorHandler;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ExpansionMode;
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
		form.setWidth("40%");
		form.setNumCols(2);
		form.setColWidths("40%", "*");

		final SelectItem cedulatipus = new SelectItem();  
		cedulatipus.setTitle(cedulaLabels.cedulatipus());
		cedulatipus.setValueMap(ClientConstants.getCedulaTipus()); 
		cedulatipus.setAllowEmptyValue(true);
		form.setFields(cedulatipus);
		
		HLayout selectokButtonLayout = new HLayout();
		selectokButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton selectokIButton = new IButton(commonLabels.filter());
		selectokButtonLayout.setAlign(Alignment.CENTER);
		selectokButtonLayout.addMember(selectokIButton);
		
		selectLayout.addMember(form);
		selectLayout.addMember(selectokButtonLayout);
		
		VLayout cedulaLayout = new VLayout();
		cedulaLayout.setWidth("35%");
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
		cedulaGrid.setCanExpandRecords(true);
		cedulaGrid.setExpansionMode(ExpansionMode.DETAILS);

		if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)) {
			Criteria criteria = new Criteria();
			criteria.setAttribute(CedulakConstants.CEDULA_STATUS,Constants.CEDULA_STATUSZ_ELORENDELT);
			cedulaGrid.fetchData(criteria);
		}
		if ((menu.equals(Constants.MENU_PENZTAR_FIZETES)) || (menu.equals(Constants.MENU_RAKTAR_KIADAS))) {
			cedulaGrid.setAutoFetchData(true);
		}
		
		ListGridField cedulaGridField = new ListGridField(
				CedulakConstants.CEDULA_CEDULA);
		cedulaGridField.setWidth("10%");

		ListGridField vevoGridField = new ListGridField(
				CedulakConstants.CEDULA_VEVONEV);

		ListGridField datumGridField = new ListGridField(
				CedulakConstants.CEDULA_DATUM);
		datumGridField.setWidth("15%");

		cedulaGrid.setFields(cedulaGridField, vevoGridField,
				datumGridField);			

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("60%");

		HLayout refreshButtonLayout = new HLayout();
		IButton refreshIButton = new IButton(commonLabels.refresh());

		if (menu.equals(Constants.MENU_LEKERDEZES_CEDULAK)
				|| menu.equals(Constants.MENU_PENZTAR_FIZETES) || menu.equals(Constants.MENU_RAKTAR_KIADAS)) {
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
				|| menu.equals(Constants.MENU_PENZTAR_FIZETES) || menu.equals(Constants.MENU_RAKTAR_KIADAS)) {
			buttonsLayout.addMember(refreshButtonLayout);
		}
		buttonsLayout.addMember(selectButtonLayout);

		if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)) {
			buttonsLayout.addMember(cancelButtonLayout);
		}

		if (menu.equals(Constants.MENU_LEKERDEZES_CEDULAK)) {
			cedulaLayout.addMember(selectLayout);
		}
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
		cedulacikkGrid.setCanExpandRecords(true);
		cedulacikkGrid.setExpansionMode(ExpansionMode.DETAILS);
	
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

		ListGridField fizetusdGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_FIZETUSD);
		fizetusdGridField.setWidth("10%");

		ListGridField fizeteurGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_FIZETEUR);
		fizeteurGridField.setWidth("10%");

		ListGridField fizetGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_FIZET);
		fizetGridField.setWidth("10%");

		cedulacikkGrid.setFields(cikkszamGridField, szinkodGridField,
				exportkartonGridField, kiskartonGridField, darabGridField,
				fizetusdGridField, fizeteurGridField, fizetGridField);

		cedulacikkLayout.addMember(cedulacikkGrid);

		HLayout fizetLayout = new HLayout();
		fizetLayout.setHeight("3%");
		fizetLayout.setWidth("90%");

		HLayout usdCurrLabelLayout = new HLayout();
		usdCurrLabelLayout.setWidth("15%");
		Label usdCurrLabel = new Label("USD :");
		usdCurrLabel.setAlign(Alignment.CENTER);
		usdCurrLabelLayout.addMember(usdCurrLabel);

		HLayout usdLabelLayout = new HLayout();
		usdLabelLayout.setWidth("15%");
		final Label usdLabel = new Label("0");
		usdLabel.setAlign(Alignment.CENTER);
		usdLabelLayout.addMember(usdLabel);

		HLayout eurCurrLabelLayout = new HLayout();
		eurCurrLabelLayout.setWidth("15%");
		Label eurCurrLabel = new Label("EUR :");
		eurCurrLabel.setAlign(Alignment.CENTER);
		eurCurrLabelLayout.addMember(eurCurrLabel);

		HLayout eurLabelLayout = new HLayout();
		eurLabelLayout.setWidth("15%");
		final Label eurLabel = new Label("0");
		eurLabel.setAlign(Alignment.CENTER);
		eurLabelLayout.addMember(eurLabel);

		HLayout ftCurrLabelLayout = new HLayout();
		ftCurrLabelLayout.setWidth("15%");
		Label ftCurrLabel = new Label("Ft :");
		ftCurrLabel.setAlign(Alignment.CENTER);
		ftCurrLabelLayout.addMember(ftCurrLabel);

		HLayout ftLabelLayout = new HLayout();
		ftLabelLayout.setWidth("15%");
		final Label ftLabel = new Label("0");
		ftLabel.setAlign(Alignment.CENTER);
		ftLabelLayout.addMember(ftLabel);

		fizetLayout.addMember(usdCurrLabelLayout);
		fizetLayout.addMember(usdLabelLayout);

		fizetLayout.addMember(eurCurrLabelLayout);
		fizetLayout.addMember(eurLabelLayout);

		fizetLayout.addMember(ftCurrLabelLayout);
		fizetLayout.addMember(ftLabelLayout);

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
				|| menu.equals(Constants.MENU_PENZTAR_FIZETES) || menu.equals(Constants.MENU_RAKTAR_KIADAS)) {
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
							cedulaGrid.getSelectedRecord().getAttributeAsDouble(
									CedulakConstants.CEDULA_BEFIZETHUF), 
							cedulaGrid.getSelectedRecord().getAttributeAsDouble(
									CedulakConstants.CEDULA_BEFIZETEUR), 
							cedulaGrid.getSelectedRecord().getAttributeAsDouble(
									CedulakConstants.CEDULA_BEFIZETUSD), 
									menu));
				}
				if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)
						|| menu.equals(Constants.MENU_PENZTAR_FIZETES) || menu.equals(Constants.MENU_RAKTAR_KIADAS)) {

					SC.ask(commonLabels.sure(), new BooleanCallback() {
						public void execute(Boolean value) {
							if (value != null && value) {
								DisplayRequest.startRequest();
								
								String tipus = "";						
								if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)) {
									tipus = Constants.CEDULA_STATUSZ_ELORENDELT;
								}
								if (menu.equals(Constants.MENU_PENZTAR_FIZETES)) {
									tipus = Constants.CEDULA_STATUSZ_FIZETENDO_ELORENDELES;
								}

								if (menu.equals(Constants.MENU_RAKTAR_KIADAS)) {
									tipus = Constants.CEDULA_STATUSZ_ELADOTT;
								}
					
								if (menu.equals(Constants.MENU_PENZTAR_FIZETES)) {
									tipus = cedulaGrid
											.getSelectedRecord()
											.getAttribute(
													CedulakConstants.CEDULA_STATUS);
								}
							
								dekorTradeService
										.cedulaToKosar(
												UserInfo.userId,
												cedulaGrid
														.getSelectedRecord()
														.getAttribute(
																CedulakConstants.CEDULA_VEVO),
												menu,
												tipus,
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
														if (menu.equals(Constants.MENU_RAKTAR_KIADAS)) {
															middleLayout
																	.removeMembers(middleLayout
																			.getMembers());
															KosarRaktar kosarRaktar = new KosarRaktar();
															middleLayout
																	.addMember(kosarRaktar.get(cedulaGrid.getSelectedRecord().getAttribute(CedulakConstants.CEDULA_CEDULA),UserInfo.userId,
																			cedulaGrid.getSelectedRecord().getAttribute(CedulakConstants.CEDULA_VEVO), 
																			cedulaGrid.getSelectedRecord().getAttribute(CedulakConstants.CEDULA_VEVONEV),
																			cedulaGrid.getSelectedRecord().getAttribute(CedulakConstants.CEDULA_VEVOTIPUS),
																			Constants.MENU_RAKTAR_KIADAS));
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
				float fizeteur = 0;
				float fizet = 0;
				for (int i = 0; i < cedulacikkGrid.getRecords().length; i++) {
					if (cedulacikkGrid.getRecord(i).getAttribute(
							CedulakConstants.CEDULACIKK_FIZETUSD) != null) {
						fizetusd = fizetusd
								+ cedulacikkGrid
										.getRecord(i)
										.getAttributeAsFloat(
												CedulakConstants.CEDULACIKK_FIZETUSD);
					}
					if (cedulacikkGrid.getRecord(i).getAttribute(
							CedulakConstants.CEDULACIKK_FIZETEUR) != null) {
						fizeteur = fizeteur
								+ cedulacikkGrid
										.getRecord(i)
										.getAttributeAsFloat(
												CedulakConstants.CEDULACIKK_FIZETEUR);
					}
					if (cedulacikkGrid.getRecord(i).getAttribute(
							CedulakConstants.CEDULACIKK_FIZET) != null) {
						fizet = fizet
								+ cedulacikkGrid
										.getRecord(i)
										.getAttributeAsFloat(
												CedulakConstants.CEDULACIKK_FIZET);
					}
				}

				usdLabel.setContents(Float.valueOf(fizetusd).toString());		
				eurLabel.setContents(Float.valueOf(fizeteur).toString());		
				ftLabel.setContents(Float.valueOf(fizet).toString());		
				
			}
		});

		return middleLayout;

	}

	public Canvas printCedula(String cedula, String tipus, String vevonev, Double befizet, Double befizeteur, Double befizetusd,
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
		arusdGridField.setWidth("10%");

		ListGridField fizetusdGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_FIZETUSD);
		fizetusdGridField.setWidth("10%");

		ListGridField fizeteurGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_FIZETEUR);
		fizeteurGridField.setWidth("10%");

		ListGridField fizetGridField = new ListGridField(
				CedulakConstants.CEDULACIKK_FIZET);
		fizetGridField.setWidth("10%");

		cedulacikkGrid.setFields(cikkszamGridField, szinkodGridField,
				exportkartonGridField, kiskartonGridField, darabGridField,
				arusdGridField, fizetusdGridField, fizeteurGridField, fizetGridField);

		cedulacikkLayout.addMember(cedulacikkGrid);

		HLayout fizetLayout = new HLayout();
		fizetLayout.setAlign(Alignment.CENTER);
		fizetLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		fizetLayout.setHeight("3%");
		fizetLayout.setWidth("100%");

		HLayout usdCurrLabelLayout = new HLayout();
		usdCurrLabelLayout.setAlign(Alignment.CENTER);
		usdCurrLabelLayout.setWidth("15%");
		Label usdCurrLabel = new Label(cedulaLabels.fizetusd());
		usdCurrLabel.setAlign(Alignment.CENTER);
		usdCurrLabelLayout.addMember(usdCurrLabel);

		HLayout usdLabelLayout = new HLayout();
		usdLabelLayout.setAlign(Alignment.CENTER);
		usdLabelLayout.setWidth("15%");
		final Label usdLabel = new Label("0");
		usdLabel.setAlign(Alignment.CENTER);
		usdLabelLayout.addMember(usdLabel);

		HLayout eurCurrLabelLayout = new HLayout();
		eurCurrLabelLayout.setAlign(Alignment.CENTER);
		eurCurrLabelLayout.setWidth("15%");
		Label eurCurrLabel = new Label(cedulaLabels.fizeteur());
		eurCurrLabel.setAlign(Alignment.CENTER);
		eurCurrLabelLayout.addMember(eurCurrLabel);

		HLayout eurLabelLayout = new HLayout();
		eurLabelLayout.setAlign(Alignment.CENTER);
		eurLabelLayout.setWidth("15%");
		final Label eurLabel = new Label("0");
		eurLabel.setAlign(Alignment.CENTER);
		eurLabelLayout.addMember(eurLabel);
		
		HLayout ftCurrLabelLayout = new HLayout();
		ftCurrLabelLayout.setAlign(Alignment.CENTER);
		ftCurrLabelLayout.setWidth("15%");
		Label ftCurrLabel = new Label(cedulaLabels.fizet());
		ftCurrLabel.setAlign(Alignment.CENTER);
		ftCurrLabelLayout.addMember(ftCurrLabel);

		HLayout ftLabelLayout = new HLayout();
		ftLabelLayout.setAlign(Alignment.CENTER);
		ftLabelLayout.setWidth("15%");
		final Label ftLabel = new Label("0");
		ftLabel.setAlign(Alignment.CENTER);
		ftLabelLayout.addMember(ftLabel);		
		
		fizetLayout.addMember(usdCurrLabelLayout);
		fizetLayout.addMember(usdLabelLayout);
		
		fizetLayout.addMember(eurCurrLabelLayout);
		fizetLayout.addMember(eurLabelLayout);

		fizetLayout.addMember(ftCurrLabelLayout);
		fizetLayout.addMember(ftLabelLayout);

		HLayout befizetLayout = new HLayout();
		befizetLayout.setHeight("3%");
		befizetLayout.setWidth("100%");

		HLayout beusdCurrLabelLayout = new HLayout();
		beusdCurrLabelLayout.setAlign(Alignment.CENTER);
		beusdCurrLabelLayout.setWidth("15%");
		Label beusdCurrLabel = new Label(cedulaLabels.befizetusd());
		beusdCurrLabel.setAlign(Alignment.CENTER);
		beusdCurrLabelLayout.addMember(beusdCurrLabel);

		HLayout beusdLabelLayout = new HLayout();
		beusdLabelLayout.setAlign(Alignment.CENTER);
		beusdLabelLayout.setWidth("15%");
		final Label beusdLabel = new Label(befizetusd == null ? "0" : befizetusd.toString());    			
		beusdLabel.setAlign(Alignment.CENTER);
		beusdLabelLayout.addMember(beusdLabel);

		HLayout beeurCurrLabelLayout = new HLayout();
		beeurCurrLabelLayout.setAlign(Alignment.CENTER);
		beeurCurrLabelLayout.setWidth("15%");
		Label beeurCurrLabel = new Label(cedulaLabels.befizeteur());
		beeurCurrLabel.setAlign(Alignment.CENTER);
		beeurCurrLabelLayout.addMember(beeurCurrLabel);

		HLayout beeurLabelLayout = new HLayout();
		beeurLabelLayout.setAlign(Alignment.CENTER);
		beeurLabelLayout.setWidth("15%");
		final Label beeurLabel = new Label(befizeteur == null ? "0" : befizeteur.toString());   				
		beeurLabel.setAlign(Alignment.CENTER);
		beeurLabelLayout.addMember(beeurLabel);
		
		HLayout beftCurrLabelLayout = new HLayout();
		beftCurrLabelLayout.setAlign(Alignment.CENTER);
		beftCurrLabelLayout.setWidth("15%");
		Label beftCurrLabel = new Label(cedulaLabels.befizet());
		beftCurrLabel.setAlign(Alignment.CENTER);
		beftCurrLabelLayout.addMember(beftCurrLabel);

		HLayout beftLabelLayout = new HLayout();
		beftLabelLayout.setAlign(Alignment.CENTER);
		beftLabelLayout.setWidth("15%");
		final Label beftLabel = new Label(befizet == null ?  "0" : befizet.toString());   
		beftLabel.setAlign(Alignment.CENTER);
		beftLabelLayout.addMember(beftLabel);

		befizetLayout.addMember(beusdCurrLabelLayout);
		befizetLayout.addMember(beusdLabelLayout);

		befizetLayout.addMember(beeurCurrLabelLayout);
		befizetLayout.addMember(beeurLabelLayout);

		befizetLayout.addMember(beftCurrLabelLayout);
		befizetLayout.addMember(beftLabelLayout);
				
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
		
		if (tipus.equals(Constants.CEDULA_STATUSZ_FIZETETT_ELORENDELES) || tipus.equals(Constants.CEDULA_STATUSZ_FIZETETT)) {	
			middleLayout1.addMember(befizetLayout);
		}
		middleLayout.addMember(middleLayout1);
		middleLayout.addMember(buttonsLayout);

		cedulacikkGrid.addDataArrivedHandler(new DataArrivedHandler() {
			public void onDataArrived(DataArrivedEvent event) {

				float fizetusd = 0;
				float fizeteur = 0;
				float fizet = 0;
				for (int i = 0; i < cedulacikkGrid.getRecords().length; i++) {
					if (cedulacikkGrid.getRecord(i).getAttribute(
							CedulakConstants.CEDULACIKK_FIZETUSD) != null) {
						fizetusd = fizetusd
								+ cedulacikkGrid
										.getRecord(i)
										.getAttributeAsFloat(
												CedulakConstants.CEDULACIKK_FIZETUSD);
					}
					if (cedulacikkGrid.getRecord(i).getAttribute(
							CedulakConstants.CEDULACIKK_FIZETEUR) != null) {
						fizetusd = fizetusd
								+ cedulacikkGrid
										.getRecord(i)
										.getAttributeAsFloat(
												CedulakConstants.CEDULACIKK_FIZETEUR);
					}
					if (cedulacikkGrid.getRecord(i).getAttribute(
							CedulakConstants.CEDULACIKK_FIZET) != null) {
						fizet = fizet
								+ cedulacikkGrid
										.getRecord(i)
										.getAttributeAsFloat(
												CedulakConstants.CEDULACIKK_FIZET);
					}
				}
			
				usdLabel.setContents(Float.valueOf(fizetusd).toString());
				eurLabel.setContents(Float.valueOf(fizeteur).toString());
				ftLabel.setContents(Float.valueOf(fizet).toString());
				
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
				if (menu.equals(Constants.MENU_ELADAS)) {
					Eladas eladas = new Eladas();
					middleLayout.addMember(eladas.get());
				}
				if (menu.equals(Constants.MENU_RAKTAR_KIADAS)) {
					Kiadas kiadas = new Kiadas();
					middleLayout.addMember(kiadas.get());
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
