package hu.dekortrade.client.kosarcikk;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.lekerdezes.cedulak.Cedulak;
import hu.dekortrade.client.rendeles.elorendeles.Elorendeles;
import hu.dekortrade.client.rendeles.veglegites.Veglegesites;
import hu.dekortrade.client.rendelles.internet.Internet;
import hu.dekortrade.client.torzsadat.cikktorzs.Cikktorzs;
import hu.dekortrade.client.torzsadat.cikktorzs.CikktorzsConstants;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSCallback;
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
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class KosarCikk {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private KosarLabels kosarLabels = GWT.create(KosarLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	private Internet internetOrder = new Internet();

	private Cikktorzs ctorzs = new Cikktorzs();

	private boolean ctorzsSelect = true;

	public Canvas get(final String cedula, final String elado,
			final String vevo, final String vevonev, final String vevotipus,
			final String menu) {
		DisplayRequest.counterInit();

		final VLayout middleLayout = new VLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		middleLayout.setStyleName("middle");

		HLayout titleLayout = new HLayout();
		titleLayout.setDefaultLayoutAlign(Alignment.CENTER);
		titleLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		titleLayout.setStyleName("middle");
		titleLayout.setHeight("3%");

		Label titleLabel = new Label();
		if (menu.equals(Constants.MENU_RENDELES_ELORENDELES)) {
			titleLabel.setContents(vevonev);
		}
		if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)) {
			titleLabel.setContents(cedula + " : " + vevonev);
		}
		titleLabel.setAlign(Alignment.CENTER);
		titleLabel.setWidth("80%");
		titleLayout.addMember(titleLabel);

		final HLayout middleLayout1 = new HLayout();
		middleLayout1.setAlign(Alignment.CENTER);
		middleLayout1.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		middleLayout1.setStyleName("middle");

		final HLayout leftLayout = new HLayout();
		leftLayout.setAlign(Alignment.CENTER);
		leftLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		leftLayout.setStyleName("middle");
		leftLayout.setWidth("35%");

		VLayout selectLayout = new VLayout();
		selectLayout.setAlign(Alignment.CENTER);
		selectLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		selectLayout.setStyleName("middle");
		selectLayout.setWidth("5%");

		final HLayout rightLayout = new HLayout();
		rightLayout.setAlign(Alignment.CENTER);
		rightLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		rightLayout.setStyleName("middle");

		VLayout kosarLayout = new VLayout();
		kosarLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final KosarCikkDataSource kosarCikkDataSource = new KosarCikkDataSource(
				cedula, elado, vevo, menu) {

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
		kosarGrid.setTitle(kosarLabels.kosar());
		kosarGrid.setWidth("90%");
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

		kosarCikkDataSource.getField(KosarConstants.KOSAR_AR).setCanView(false);
		kosarCikkDataSource.getField(KosarConstants.KOSAR_AREUR).setCanView(
				false);
		kosarCikkDataSource.getField(KosarConstants.KOSAR_FIZET).setCanView(
				false);
		kosarCikkDataSource.getField(KosarConstants.KOSAR_FIZETEUR).setCanView(
				false);

		kosarGrid.setFields(cikkszamGridField, szinkodGridField,
				exportkartonGridField, kiskartonGridField, darabGridField);

		HLayout ctorzsButtonLayout = new HLayout();
		ctorzsButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton ctorzsButton = new IButton(kosarLabels.kosar_ctorzs());
		ctorzsButton.setDisabled(true);
		ctorzsButtonLayout.setAlign(Alignment.CENTER);
		ctorzsButtonLayout.addMember(ctorzsButton);

		HLayout internetButtonLayout = new HLayout();
		internetButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton internetButton = new IButton(kosarLabels.kosar_internet());
		internetButtonLayout.setAlign(Alignment.CENTER);
		internetButtonLayout.addMember(internetButton);

		HLayout addButtonLayout = new HLayout();
		addButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton addButton = new IButton("<");
		addButton.setDisabled(true);
		addButtonLayout.setAlign(Alignment.CENTER);
		addButtonLayout.addMember(addButton);

		selectLayout.addMember(ctorzsButtonLayout);
		selectLayout.addMember(internetButtonLayout);
		selectLayout.addMember(addButtonLayout);

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

		HLayout buttons1Layout = new HLayout();
		buttons1Layout.setHeight("3%");
		buttons1Layout.setWidth("90%");

		HLayout deleteButtonLayout = new HLayout();
		deleteButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton deleteButton = new IButton(commonLabels.delete());
		deleteButton.setDisabled(true);
		deleteButtonLayout.setAlign(Alignment.CENTER);
		deleteButtonLayout.addMember(deleteButton);

		HLayout okButtonLayout = new HLayout();
		okButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		IButton okButton = new IButton(commonLabels.ok());
		okButtonLayout.setAlign(Alignment.CENTER);
		okButtonLayout.addMember(okButton);

		HLayout elvetButtonLayout = new HLayout();
		IButton elvetButton = new IButton(commonLabels.cancel());
		if (menu.equals(Constants.MENU_RENDELES_ELORENDELES)) {
			elvetButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
			elvetButtonLayout.setAlign(Alignment.CENTER);
			elvetButtonLayout.addMember(elvetButton);
		}

		buttons1Layout.addMember(okButtonLayout);
		buttons1Layout.addMember(deleteButtonLayout);
		if (menu.equals(Constants.MENU_RENDELES_ELORENDELES)) {
			buttons1Layout.addMember(elvetButtonLayout);
		}

		middleLayout.addMember(middleLayout1);
		middleLayout1.addMember(leftLayout);
		middleLayout1.addMember(selectLayout);
		middleLayout1.addMember(rightLayout);

		leftLayout.addMember(kosarLayout);

		kosarLayout.addMember(titleLayout);
		kosarLayout.addMember(kosarGrid);
		kosarLayout.addMember(fizetLayout);
		kosarLayout.addMember(buttons1Layout);

		rightLayout.addMember(ctorzs.get(addButton));

		internetButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ctorzsSelect = false;
				internetButton.setDisabled(true);
				ctorzsButton.setDisabled(false);
				addButton.setDisabled(true);
				rightLayout.removeMembers(rightLayout.getMembers());
				rightLayout.addMember(internetOrder.get(addButton));
			}
		});

		ctorzsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ctorzsSelect = true;
				internetButton.setDisabled(false);
				ctorzsButton.setDisabled(true);
				addButton.setDisabled(true);
				rightLayout.removeMembers(rightLayout.getMembers());
				rightLayout.addMember(ctorzs.get(addButton));
			}
		});

		if (menu.equals(Constants.MENU_RENDELES_ELORENDELES)) {
			elvetButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					SC.ask(commonLabels.sure(), new BooleanCallback() {
						public void execute(Boolean value) {
							if (value != null && value) {
								DisplayRequest.startRequest();
								dekorTradeService.removeKosar(elado, vevo,
										menu, cedula,
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

											public void onSuccess(String result) {
												DisplayRequest.serverResponse();
												middleLayout
														.removeMembers(middleLayout
																.getMembers());
												Elorendeles preOrder = new Elorendeles();
												middleLayout.addMember(preOrder
														.get());
											}
										});
							}
						}
					});

				}
			});
		}

		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				if (ctorzsSelect) {
					final Window winModal = new Window();
					winModal.setWidth(500);
					winModal.setHeight(230);
					winModal.setTitle(kosarLabels.kosar_cikkszam());
					winModal.setShowMinimizeButton(false);
					winModal.setShowCloseButton(false);
					winModal.setIsModal(true);
					winModal.setShowModalMask(true);
					winModal.centerInPage();

					final DynamicForm kosarEditForm = new DynamicForm();
					kosarEditForm.setNumCols(2);
					kosarEditForm.setColWidths("40%", "*");
					kosarEditForm.setDataSource(kosarCikkDataSource);
					kosarEditForm.setUseAllDataSourceFields(true);

					int found = 0;
					for (int i = 0; i < kosarGrid.getRecords().length; i++) {
						if ((kosarGrid.getRecord(i).getAttribute(
								KosarConstants.KOSAR_CIKKSZAM).equals(ctorzs
								.getSelectedRecord().getAttribute(
										CikktorzsConstants.CIKK_CIKKSZAM)))
								&& (kosarGrid.getRecord(i).getAttribute(
										KosarConstants.KOSAR_SZINKOD)
										.equals(ctorzs
												.getSelectedRecord()
												.getAttribute(
														CikktorzsConstants.CIKK_SZINKOD)))) {
							found = i;
							i = kosarGrid.getRecords().length;
						}
					}

					if (found > 0)
						kosarEditForm.editRecord(kosarGrid.getRecord(found));
					else
						kosarEditForm.editNewRecord();

					kosarEditForm.getField(KosarConstants.KOSAR_CIKKSZAM)
							.setValue(
									ctorzs.getSelectedRecord().getAttribute(
											CikktorzsConstants.CIKK_CIKKSZAM));
					kosarEditForm.getField(KosarConstants.KOSAR_CIKKSZAM)
							.setCanEdit(false);
					kosarEditForm.getField(KosarConstants.KOSAR_SZINKOD)
							.setValue(
									ctorzs.getSelectedRecord().getAttribute(
											CikktorzsConstants.CIKK_SZINKOD));
					kosarEditForm.getField(KosarConstants.KOSAR_SZINKOD)
							.setCanEdit(false);
					kosarEditForm
							.getField(KosarConstants.KOSAR_MEGNEVEZES)
							.setValue(
									ctorzs.getSelectedRecord().getAttribute(
											CikktorzsConstants.CIKK_MEGNEVEZES));
					kosarEditForm.getField(KosarConstants.KOSAR_MEGNEVEZES)
							.setCanEdit(false);
					kosarEditForm.getField(KosarConstants.KOSAR_EXPORTKARTON)
							.setValidateOnChange(true);
					kosarEditForm.getField(KosarConstants.KOSAR_EXPORTKARTON)
							.setValidators(new IsIntegerValidator());
					kosarEditForm.getField(KosarConstants.KOSAR_KISKARTON)
							.setValidators(new IsIntegerValidator());
					kosarEditForm.getField(KosarConstants.KOSAR_DARAB)
							.setValidators(new IsIntegerValidator());

					kosarEditForm
							.getField(KosarConstants.KOSAR_ARUSD)
							.setValue(
									ctorzs.getSelectedRecord()
											.getAttributeAsFloat(
													CikktorzsConstants.CIKK_ELORAR));
					kosarEditForm.getField(KosarConstants.KOSAR_ARUSD)
							.setVisible(false);

					kosarEditForm.getField(KosarConstants.KOSAR_FIZETUSD)
							.setVisible(false);

					HLayout buttonsLayout = new HLayout();
					buttonsLayout.setWidth100();

					HLayout saveLayout = new HLayout();
					IButton saveIButton = new IButton(commonLabels.ok());

					saveLayout.addMember(saveIButton);
					buttonsLayout.addMember(saveLayout);

					HLayout cancelLayout = new HLayout();
					cancelLayout.setAlign(Alignment.RIGHT);
					final IButton cancelIButton = new IButton(commonLabels
							.cancel());
					cancelLayout.addMember(cancelIButton);
					buttonsLayout.addMember(cancelLayout);

					saveIButton.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							cancelIButton.setDisabled(true);

							int exp = 0;
							int kk = 0;
							int db = 0;
							if (kosarEditForm.getField(
									KosarConstants.KOSAR_EXPORTKARTON)
									.getValue() == null)
								kosarEditForm.getField(
										KosarConstants.KOSAR_EXPORTKARTON)
										.setValue("0");
							if (kosarEditForm.getField(
									KosarConstants.KOSAR_KISKARTON).getValue() == null)
								kosarEditForm.getField(
										KosarConstants.KOSAR_KISKARTON)
										.setValue("0");
							if (kosarEditForm.getField(
									KosarConstants.KOSAR_DARAB).getValue() == null)
								kosarEditForm.getField(
										KosarConstants.KOSAR_DARAB).setValue(
										"0");

							exp = new Integer((String) kosarEditForm.getField(
									KosarConstants.KOSAR_EXPORTKARTON)
									.getValue());
							kk = new Integer((String) kosarEditForm.getField(
									KosarConstants.KOSAR_KISKARTON).getValue());
							db = new Integer((String) kosarEditForm.getField(
									KosarConstants.KOSAR_DARAB).getValue());

							int ckk = 0;
							int cdb = 0;
							if (ctorzs.getSelectedRecord().getAttributeAsInt(
									CikktorzsConstants.CIKK_KISKARTON) != null)
								ckk = ctorzs
										.getSelectedRecord()
										.getAttributeAsInt(
												CikktorzsConstants.CIKK_KISKARTON);
							if (ctorzs.getSelectedRecord().getAttributeAsInt(
									CikktorzsConstants.CIKK_DARAB) != null)
								cdb = ctorzs.getSelectedRecord()
										.getAttributeAsInt(
												CikktorzsConstants.CIKK_DARAB);

							float elarar = 0;
							if (ctorzs.getSelectedRecord().getAttributeAsFloat(
									CikktorzsConstants.CIKK_ELORAR) != null)
								elarar = ctorzs.getSelectedRecord()
										.getAttributeAsFloat(
												CikktorzsConstants.CIKK_ELORAR);

							float fizet = (float) (((((ckk * cdb) * exp)
									+ (cdb * kk) + db) * elarar) * 0.2);

							kosarEditForm.getField(
									KosarConstants.KOSAR_FIZETUSD).setValue(
									fizet);

							kosarEditForm.saveData(new DSCallback() {
								public void execute(DSResponse response,
										Object rawData, DSRequest request) {
									if (response.getStatus() == DSResponse.STATUS_SUCCESS)
										winModal.destroy();
									else
										cancelIButton.setDisabled(false);
								}
							});
						}
					});

					cancelIButton.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							winModal.destroy();
						}
					});

					winModal.addItem(kosarEditForm);
					winModal.addItem(buttonsLayout);
					winModal.show();
				} else {
					SC.ask(commonLabels.sure(), new BooleanCallback() {
						public void execute(Boolean value) {
							if (value != null && value) {
								DisplayRequest.startRequest();
								dekorTradeService.importInternet(elado, vevo,
										internetOrder.getRendeles(),
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

											public void onSuccess(String result) {
												DisplayRequest.serverResponse();

												kosarGrid.invalidateCache();
												kosarGrid.fetchData();

												rightLayout
														.removeMembers(rightLayout
																.getMembers());
												rightLayout
														.addMember(internetOrder
																.get(addButton));
											}
										});
							}
						}
					});
				}
			}
		});

		kosarGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				deleteButton.setDisabled(false);
			}
		});

		kosarGrid.addDataArrivedHandler(new DataArrivedHandler() {
			public void onDataArrived(DataArrivedEvent event) {

				float fizetusd = 0;
				for (int i = 0; i < kosarGrid.getRecords().length; i++) {
					if (kosarGrid.getRecord(i).getAttribute(
							KosarConstants.KOSAR_FIZETUSD) != null) {
						fizetusd = fizetusd
								+ kosarGrid.getRecord(i).getAttributeAsFloat(
										KosarConstants.KOSAR_FIZETUSD);
					}

				}
				usdLabel.setContents(NumberFormat.getFormat("#.0000").format(
						fizetusd));
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

		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask(commonLabels.sure(), new BooleanCallback() {
					public void execute(Boolean value) {
						if (value != null && value) {
							DisplayRequest.startRequest();

							if (menu.equals(Constants.MENU_RENDELES_ELORENDELES)) {
								dekorTradeService.createCedula(elado, vevo,
										menu, new AsyncCallback<String>() {
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

											public void onSuccess(String result) {
												DisplayRequest.serverResponse();

												middleLayout
														.removeMembers(middleLayout
																.getMembers());
												Cedulak cedula = new Cedulak();
												middleLayout.addMember(cedula
														.printCedula(
																result,
																Constants.CEDULA_STATUSZ_ELORENDELT,
																vevonev, menu));
											}
										});
							}

							if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)) {
								dekorTradeService.kosarToCedula(elado, vevo,
										menu, cedula,
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

											public void onSuccess(String result) {
												DisplayRequest.serverResponse();

												middleLayout
														.removeMembers(middleLayout
																.getMembers());
												Veglegesites finalizeOrder = new Veglegesites();
												middleLayout
														.addMember(finalizeOrder
																.get());
											}
										});
							}

						}

					}
				});
			}
		});

		return middleLayout;
	}

}
