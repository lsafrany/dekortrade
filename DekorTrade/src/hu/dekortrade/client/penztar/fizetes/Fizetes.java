package hu.dekortrade.client.penztar.fizetes;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.client.kosarcikk.KosarCikkDataSource;
import hu.dekortrade.client.kosarcikk.KosarConstants;
import hu.dekortrade.client.lekerdezes.cedulak.Cedulak;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.VevoKosarSer;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Fizetes {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private FizetesLabels fizetesLabels = GWT.create(FizetesLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

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
		dekorTradeService.getVevoKosar(UserInfo.userId,
				Constants.MENU_PENZTAR_FIZETES,
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

	public Canvas process(final VevoKosarSer result) {

		DisplayRequest.counterInit();

		final HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		if (result == null) {
			Cedulak cedula = new Cedulak();
			middleLayout.addMember(cedula.get(null,
					Constants.MENU_PENZTAR_FIZETES));
		} else {

			final VLayout cashLayout = new VLayout();
			cashLayout.setAlign(Alignment.CENTER);
			cashLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
			cashLayout.setStyleName("middle");

			HLayout titleLayout = new HLayout();
			titleLayout.setDefaultLayoutAlign(Alignment.CENTER);
			titleLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
			titleLayout.setStyleName("middle");
			titleLayout.setHeight("3%");

			Label titleLabel = new Label(result.getCedula() + " : "
					+ result.getVevonev());
			titleLabel.setAlign(Alignment.CENTER);
			titleLabel.setWidth("60%");
			titleLayout.addMember(titleLabel);

			VLayout kosarLayout = new VLayout();
			kosarLayout.setDefaultLayoutAlign(Alignment.CENTER);

			final KosarCikkDataSource kosarCikkDataSource = new KosarCikkDataSource(
					result.getCedula(), UserInfo.userId, result.getVevo(),
					Constants.MENU_PENZTAR_FIZETES) {

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

			kosarCikkDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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

			final ListGrid kosarGrid = new ListGrid();
			kosarGrid.setTitle(fizetesLabels.kosar());
			kosarGrid.setWidth("60%");
			kosarGrid.setShowHeaderContextMenu(false);
			kosarGrid.setShowHeaderMenuButton(false);
			kosarGrid.setCanSort(false);
			kosarGrid.setShowAllRecords(true);
			kosarGrid.setDataSource(kosarCikkDataSource);
			kosarGrid.setAutoFetchData(true);
			kosarGrid.setCanExpandRecords(true);
			kosarGrid.setExpansionMode(ExpansionMode.DETAILS);

			ListGridField cikkszamGridField = new ListGridField(
					KosarConstants.KOSAR_CIKKSZAM);

			ListGridField szinkodGridField = new ListGridField(
					KosarConstants.KOSAR_SZINKOD);
			szinkodGridField.setWidth("20%");

			ListGridField exportkartonGridField = new ListGridField(
					KosarConstants.KOSAR_EXPORTKARTON);
			exportkartonGridField.setWidth("10%");

			ListGridField kiskartonGridField = new ListGridField(
					KosarConstants.KOSAR_KISKARTON);
			kiskartonGridField.setWidth("10%");

			ListGridField darabGridField = new ListGridField(
					KosarConstants.KOSAR_DARAB);
			darabGridField.setWidth("10%");

			kosarCikkDataSource.getField(KosarConstants.KOSAR_AR).setCanView(
					false);
			kosarCikkDataSource.getField(KosarConstants.KOSAR_AREUR)
					.setCanView(false);
			kosarCikkDataSource.getField(KosarConstants.KOSAR_FIZET)
					.setCanView(false);
			kosarCikkDataSource.getField(KosarConstants.KOSAR_FIZETEUR)
					.setCanView(false);

			kosarGrid.setFields(cikkszamGridField, szinkodGridField,
					exportkartonGridField, kiskartonGridField, darabGridField);

			kosarLayout.addMember(kosarGrid);

			HLayout fizetLayout = new HLayout();
			fizetLayout.setHeight("3%");
			fizetLayout.setWidth("60%");

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

			HLayout buttons1Layout = new HLayout();
			buttons1Layout.setHeight("3%");
			buttons1Layout.setWidth("60%");

			HLayout deleteButtonLayout = new HLayout();
			deleteButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
			final IButton deleteButton = new IButton(commonLabels.delete());
			deleteButton.setDisabled(true);
			deleteButtonLayout.setAlign(Alignment.CENTER);
			deleteButtonLayout.addMember(deleteButton);
			buttons1Layout.addMember(deleteButtonLayout);

			HLayout okButtonLayout = new HLayout();
			okButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
			IButton okButton = new IButton(commonLabels.ok());
			okButtonLayout.setAlign(Alignment.CENTER);
			okButtonLayout.addMember(okButton);

			buttons1Layout.addMember(okButtonLayout);

			cashLayout.addMember(titleLayout);
			cashLayout.addMember(kosarLayout);
			cashLayout.addMember(fizetLayout);
			cashLayout.addMember(buttons1Layout);

			middleLayout.addMember(cashLayout);

			kosarGrid.addRecordClickHandler(new RecordClickHandler() {
				public void onRecordClick(RecordClickEvent event) {
					deleteButton.setDisabled(false);
				}
			});

			deleteButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					SC.ask(commonLabels.sure(), new BooleanCallback() {
						public void execute(Boolean value) {
							if (value != null && value) {
								kosarGrid.removeSelectedData();
								deleteButton.setDisabled(true);
							}
						}
					});
				}
			});

			kosarGrid.addDataArrivedHandler(new DataArrivedHandler() {
				public void onDataArrived(DataArrivedEvent event) {

					float fizet = 0;
					for (int i = 0; i < kosarGrid.getRecords().length; i++) {
						if (kosarGrid.getRecord(i).getAttribute(
								KosarConstants.KOSAR_FIZETUSD) != null) {
							fizet = fizet
									+ kosarGrid
											.getRecord(i)
											.getAttributeAsFloat(
													KosarConstants.KOSAR_FIZETUSD);
						}

					}
					usdLabel.setContents(NumberFormat.getFormat("#.0000")
							.format(fizet).replaceAll(",", "."));
				}
			});

			okButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
								
					final Window winModal = new Window();
					winModal.setWidth(300);
					winModal.setHeight(150);
					winModal.setTitle(fizetesLabels.befizetes());
					winModal.setShowMinimizeButton(false);
					winModal.setShowCloseButton(false);
					winModal.setIsModal(true);
					winModal.setShowModalMask(true);
					winModal.centerInPage();
					
					final DynamicForm form = new DynamicForm();
					form.setHeight("40%");
					form.setNumCols(2);
					form.setColWidths("40%", "*");
			
					final TextItem befizet = new TextItem();   
					befizet.setTitle(fizetesLabels.befizet());
					befizet.setValue("0");
					befizet.setValidators(isFloatValidator);

					final TextItem befizeteur = new TextItem();   
					befizeteur.setTitle(fizetesLabels.befizeteur());
					befizeteur.setValue("0");
					befizeteur.setValidators(isFloatValidator);

					final TextItem befizetusd = new TextItem();   
					befizetusd.setTitle(fizetesLabels.befizetusd());
					befizetusd.setValue(usdLabel.getContents());
					befizetusd.setValidators(isFloatValidator);
					
					form.setFields(befizet,befizeteur,befizetusd);
			
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
													
										final Float tmpbefizet = new Float(befizet.getValueAsString());
										final Float tmpbefizeteur = new Float(befizeteur.getValueAsString());
										final Float tmpbefizetusd = new Float(befizetusd.getValueAsString());
										
										dekorTradeService.kosarToCedula(
												UserInfo.userId, result.getVevo(),
												Constants.MENU_PENZTAR_FIZETES,
												result.getCedula(),tmpbefizet,
												tmpbefizeteur,tmpbefizetusd,
												new AsyncCallback<String>() {
													public void onFailure(
															Throwable caught) {
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

													public void onSuccess(String result1) {
														DisplayRequest.serverResponse();
														winModal.destroy();
														middleLayout
																.removeMembers(middleLayout
																		.getMembers());
														Cedulak cedula = new Cedulak();
														middleLayout.addMember(cedula.printCedula(
																result.getCedula(),
																Constants.CEDULA_STATUSZ_FIZETETT_ELORENDELES,
																result.getVevonev(),
																tmpbefizet,
																tmpbefizeteur,
																tmpbefizetusd,
																Constants.MENU_PENZTAR_FIZETES));
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

		}
	
		return middleLayout;

	}

}
