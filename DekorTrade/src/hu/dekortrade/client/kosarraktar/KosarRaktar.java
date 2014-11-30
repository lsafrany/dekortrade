package hu.dekortrade.client.kosarraktar;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.client.eladas.Eladas;
import hu.dekortrade.client.eladas.EladasConstants;
import hu.dekortrade.client.kosarcikk.KosarCikkDataSource;
import hu.dekortrade.client.kosarcikk.KosarConstants;
import hu.dekortrade.client.lekerdezes.cedulak.Cedulak;
import hu.dekortrade.client.raktar.kesztlet.Keszlet;
import hu.dekortrade.client.raktar.kesztlet.KeszletConstants;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import com.google.gwt.core.client.GWT;
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
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class KosarRaktar {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private KosarRaktarLabels kosarRaktarLabels = GWT.create(KosarRaktarLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	private Keszlet keszlet = new Keszlet();

	private Eladas eladas = new Eladas();
	
	private boolean keszletSelect = true;
	
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
		if (menu.equals(Constants.MENU_ELADAS)) {
			titleLabel.setContents(vevonev);
		}
		if (menu.equals(Constants.MENU_RAKTAR_KIADAS)) {
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
		kosarGrid.setTitle(kosarRaktarLabels.kosar());
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

		if (!menu.equals(Constants.MENU_RAKTAR_KIADAS)) {
			kosarGrid.setFields(cikkszamGridField, szinkodGridField,
					exportkartonGridField, kiskartonGridField, darabGridField);
		}
		else {
			ListGridField helyGridField = new ListGridField(
					KosarConstants.KOSAR_HELYKOD);
			darabGridField.setWidth("20%");
			kosarGrid.setFields(cikkszamGridField, szinkodGridField,
					exportkartonGridField, kiskartonGridField, darabGridField, helyGridField);			
		}
		HLayout keszletButtonLayout = new HLayout();
		keszletButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton keszletButton = new IButton(kosarRaktarLabels.kosar_keszlet());
		keszletButton.setDisabled(true);
		keszletButtonLayout.setAlign(Alignment.CENTER);
		keszletButtonLayout.addMember(keszletButton);

		HLayout rendelesButtonLayout = new HLayout();
		rendelesButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton rendelesButton = new IButton(kosarRaktarLabels.kosar_rendeles());
		rendelesButtonLayout.setAlign(Alignment.CENTER);
		rendelesButtonLayout.addMember(rendelesButton);
		
		HLayout addButtonLayout = new HLayout();
		addButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton addButton = new IButton("<");
		addButton.setDisabled(true);
		addButtonLayout.setAlign(Alignment.CENTER);
		addButtonLayout.addMember(addButton);

		if (menu.equals(Constants.MENU_ELADAS)) {
			selectLayout.addMember(keszletButtonLayout);
			selectLayout.addMember(rendelesButtonLayout);
		}
		if (!menu.equals(Constants.MENU_RAKTAR_KIADAS)) selectLayout.addMember(addButtonLayout);

		HLayout fizetLayout = new HLayout();
		fizetLayout.setHeight("3%");
		fizetLayout.setWidth("90%");

		HLayout usdCurrLabelLayout = new HLayout();
		Label usdCurrLabel = new Label("USD :");
		usdCurrLabel.setWidth("5%");
		usdCurrLabel.setAlign(Alignment.CENTER);
		usdCurrLabelLayout.addMember(usdCurrLabel);

		HLayout usdLabelLayout = new HLayout();
		final Label usdLabel = new Label("0");
		usdLabel.setWidth("25%");
		usdLabel.setAlign(Alignment.RIGHT);
		usdLabelLayout.addMember(usdLabel);

		HLayout eurCurrLabelLayout = new HLayout();
		Label eurCurrLabel = new Label("EUR :");
		eurCurrLabel.setWidth("5%");
		eurCurrLabel.setAlign(Alignment.CENTER);
		eurCurrLabelLayout.addMember(eurCurrLabel);

		HLayout eurLabelLayout = new HLayout();
		final Label eurLabel = new Label("0");
		eurLabel.setWidth("25%");
		eurLabel.setAlign(Alignment.RIGHT);
		eurLabelLayout.addMember(eurLabel);

		HLayout ftCurrLabelLayout = new HLayout();
		Label ftCurrLabel = new Label("Ft :");
		ftCurrLabel.setWidth("5%");
		ftCurrLabel.setAlign(Alignment.CENTER);
		ftCurrLabelLayout.addMember(ftCurrLabel);

		HLayout ftLabelLayout = new HLayout();
		final Label ftLabel = new Label("0");
		ftLabel.setWidth("25%");
		ftLabel.setAlign(Alignment.RIGHT);
		ftLabelLayout.addMember(ftLabel);
	
		if (!menu.equals(Constants.MENU_RAKTAR_KIADAS)) {
			fizetLayout.addMember(usdCurrLabelLayout);
			fizetLayout.addMember(usdLabelLayout);
			fizetLayout.addMember(eurCurrLabelLayout);
			fizetLayout.addMember(eurLabelLayout);
			fizetLayout.addMember(ftCurrLabelLayout);
			fizetLayout.addMember(ftLabelLayout);
		}
	
		HLayout buttons1Layout = new HLayout();
		buttons1Layout.setHeight("3%");
		buttons1Layout.setWidth("90%");

		HLayout okButtonLayout = new HLayout();
		okButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		IButton okButton = new IButton(commonLabels.ok());
		okButtonLayout.setAlign(Alignment.CENTER);
		okButtonLayout.addMember(okButton);

		HLayout deleteButtonLayout = new HLayout();
		deleteButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton deleteButton = new IButton(commonLabels.delete());
		deleteButton.setDisabled(true);
		deleteButtonLayout.setAlign(Alignment.CENTER);
		deleteButtonLayout.addMember(deleteButton);

		buttons1Layout.addMember(okButtonLayout);
		buttons1Layout.addMember(deleteButtonLayout);
		
		middleLayout.addMember(middleLayout1);
		middleLayout1.addMember(leftLayout);
		if (!menu.equals(Constants.MENU_RAKTAR_KIADAS))  middleLayout1.addMember(selectLayout);
		if (!menu.equals(Constants.MENU_RAKTAR_KIADAS))  middleLayout1.addMember(rightLayout);

		leftLayout.addMember(kosarLayout);

		kosarLayout.addMember(titleLayout);
		kosarLayout.addMember(kosarGrid);
		if (!menu.equals(Constants.MENU_RAKTAR_KIADAS)) kosarLayout.addMember(fizetLayout);
		kosarLayout.addMember(buttons1Layout);

		if (!menu.equals(Constants.MENU_RAKTAR_KIADAS)) rightLayout.addMember(keszlet.get(menu,addButton));
		
		rendelesButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				keszletSelect = false;
				addButton.setDisabled(true);
				rendelesButton.setDisabled(true);
				keszletButton.setDisabled(false);
				rightLayout.removeMembers(rightLayout.getMembers());
				rightLayout.addMember(eladas.rendeles(vevo,addButton));
			}
		});

		keszletButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				keszletSelect = true;
				addButton.setDisabled(true);
				rendelesButton.setDisabled(false);
				keszletButton.setDisabled(true);
				rightLayout.removeMembers(rightLayout.getMembers());
				rightLayout.addMember(keszlet.get(menu,addButton));
			}
		});

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
							deleteButton.setDisabled(true);
							kosarGrid.removeSelectedData((new DSCallback() {
								public void execute(DSResponse response, Object rawData,
										DSRequest request) {
									if (response.getStatus() == DSResponse.STATUS_SUCCESS)
										eladas.refreshRendelesGrid();
								}	
							}));
						}
					}
				});
			}
		});

		kosarGrid.addDataArrivedHandler(new DataArrivedHandler() {
			public void onDataArrived(DataArrivedEvent event) {

				float fizetusd = 0;
				float fizeteur = 0;
				float fizetft = 0;
				for (int i = 0; i < kosarGrid.getRecords().length; i++) {
					if (kosarGrid.getRecord(i).getAttribute(
							KosarConstants.KOSAR_FIZETUSD) != null) {
						fizetusd = fizetusd
								+ kosarGrid.getRecord(i).getAttributeAsFloat(
										KosarConstants.KOSAR_FIZETUSD);
					}
					if (kosarGrid.getRecord(i).getAttribute(
							KosarConstants.KOSAR_FIZETEUR) != null) {
						fizeteur = fizeteur
								+ kosarGrid.getRecord(i).getAttributeAsFloat(
										KosarConstants.KOSAR_FIZETEUR);
					}
					if (kosarGrid.getRecord(i).getAttribute(
							KosarConstants.KOSAR_FIZET) != null) {
						fizetft = fizetft
								+ kosarGrid.getRecord(i).getAttributeAsFloat(
										KosarConstants.KOSAR_FIZET);
					}
				}
				usdLabel.setContents(Float.valueOf(fizetusd).toString());
				eurLabel.setContents(Float.valueOf(fizeteur).toString());
				ftLabel.setContents(Float.valueOf(fizetft).toString());
			}
		});
		
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				if (keszletSelect) {
						
					final Window winModal = new Window();
					winModal.setWidth(500);
					winModal.setHeight(250);
					winModal.setTitle(kosarRaktarLabels.kosar_cikkszam());
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
					
					kosarEditForm.editNewRecord();

					kosarEditForm.getField(KosarConstants.KOSAR_CIKKSZAM)
							.setValue(
									keszlet.getSelectedRecord().getAttribute(
											KeszletConstants.KESZLET_CIKKSZAM));
					kosarEditForm.getField(KosarConstants.KOSAR_CIKKSZAM)
							.setCanEdit(false);
					kosarEditForm.getField(KosarConstants.KOSAR_SZINKOD)
							.setValue(
									keszlet.getSelectedRecord().getAttribute(
											KeszletConstants.KESZLET_SZINKOD));
					kosarEditForm.getField(KosarConstants.KOSAR_SZINKOD)
							.setCanEdit(false);
					kosarEditForm
							.getField(KosarConstants.KOSAR_MEGNEVEZES)
							.setValue(
									keszlet.getSelectedRecord().getAttribute(
											KeszletConstants.KESZLET_MEGNEVEZES));
					kosarEditForm.getField(KosarConstants.KOSAR_MEGNEVEZES)
							.setCanEdit(false);
					kosarEditForm.getField(KosarConstants.KOSAR_EXPORTKARTON)
							.setValidateOnChange(true);
					kosarEditForm.getField(KosarConstants.KOSAR_EXPORTKARTON)
							.setValidators(new IsIntegerValidator());
					kosarEditForm.getField(KosarConstants.KOSAR_KISKARTON)
							.setValidators(new IsIntegerValidator());
					kosarEditForm.getField(KosarConstants.KOSAR_KISKARTON)
							.setValidateOnChange(true);
					kosarEditForm.getField(KosarConstants.KOSAR_DARAB)
							.setValidators(new IsIntegerValidator());
					kosarEditForm.getField(KosarConstants.KOSAR_DARAB)
							.setValidateOnChange(true);
					
					kosarEditForm
							.getField(KosarConstants.KOSAR_ARUSD)
							.setValue(
									keszlet.getSelectedRecord()
											.getAttributeAsFloat(
													KeszletConstants.KESZLET_ELORAR));
					kosarEditForm
							.getField(KosarConstants.KOSAR_AREUR)
							.setValue(
									keszlet.getSelectedRecord()
											.getAttributeAsFloat(
													KeszletConstants.KESZLET_AREUR));
					kosarEditForm
							.getField(KosarConstants.KOSAR_AR)
							.setValue(
									keszlet.getSelectedRecord()
										.getAttributeAsFloat(
												KeszletConstants.KESZLET_AR));

					kosarEditForm.getField(KosarConstants.KOSAR_ARUSD)
							.setVisible(false);

					kosarEditForm.getField(KosarConstants.KOSAR_FIZETUSD)
							.setVisible(false);

					kosarEditForm.getField(KosarConstants.KOSAR_AREUR)
							.setVisible(false);

					kosarEditForm.getField(KosarConstants.KOSAR_FIZETEUR)
							.setVisible(false);

					kosarEditForm.getField(KosarConstants.KOSAR_AR)
							.setVisible(false);

					kosarEditForm.getField(KosarConstants.KOSAR_FIZET)
							.setVisible(false);

					kosarEditForm.getField(KosarConstants.KOSAR_RENDELES)
							.setVisible(false);

					final DynamicForm arForm = new DynamicForm();
					arForm.setNumCols(2);
					arForm.setColWidths("40%", "*");
					
					final SelectItem arselect = new SelectItem();
					arselect.setTitle(kosarRaktarLabels.artipus());
					arselect.setValueMap(KosarRaktarConstants.getArtipus());
					arselect.setDefaultValue(KosarRaktarConstants.PIACI_AR);
					
					arForm.setFields(arselect);

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
										KosarConstants.KOSAR_DARAB).setValue("0");
							
							exp = new Integer((String) kosarEditForm.getField(
									KosarConstants.KOSAR_EXPORTKARTON)
									.getValue());
							kk = new Integer((String) kosarEditForm.getField(
									KosarConstants.KOSAR_KISKARTON).getValue());
							db = new Integer((String) kosarEditForm.getField(
									KosarConstants.KOSAR_DARAB).getValue());
							
							int ckk = 0;
							int cdb = 0;
							if (keszlet.getSelectedRecord().getAttributeAsInt(
									KeszletConstants.KESZLET_KISKARTON) != null)
								ckk = keszlet
										.getSelectedRecord()
										.getAttributeAsInt(
												KeszletConstants.KESZLET_KISKARTON);
							if (keszlet.getSelectedRecord().getAttributeAsInt(
									KeszletConstants.KESZLET_DARAB) != null)
								cdb = keszlet.getSelectedRecord()
										.getAttributeAsInt(
												KeszletConstants.KESZLET_DARAB);

							if (arselect.getValue().equals(KosarRaktarConstants.ELORENDELO_AR)) {
								double elarar = 0;
								if (keszlet.getSelectedRecord().getAttributeAsDouble(
										KeszletConstants.KESZLET_ELORAR) != null)
									elarar = keszlet.getSelectedRecord()
										.getAttributeAsFloat(
												KeszletConstants.KESZLET_ELORAR);						
								double fizetusd = (double) (((((ckk * cdb) * exp)
										+ (cdb * kk) + db) * elarar));
			
								kosarEditForm.getField(
										KosarConstants.KOSAR_ARUSD).setValue(keszlet.getSelectedRecord()
												.getAttributeAsFloat(
														KeszletConstants.KESZLET_ELORAR));

								kosarEditForm.getField(
										KosarConstants.KOSAR_FIZETUSD).setValue(
										Constants.round(fizetusd,5));
							}
							
							if (arselect.getValue().equals(KosarRaktarConstants.EXPORT_AR)) {
								double exportar = 0;
								if (keszlet.getSelectedRecord().getAttributeAsDouble(
										KeszletConstants.KESZLET_ELORAR) != null)
									exportar = keszlet.getSelectedRecord()
										.getAttributeAsFloat(
												KeszletConstants.KESZLET_AREUR);						
								double fizeteur = (double) (((((ckk * cdb) * exp)
										+ (cdb * kk) + db) * exportar));

								kosarEditForm.getField(
										KosarConstants.KOSAR_AREUR).setValue(keszlet.getSelectedRecord()
												.getAttributeAsFloat(
														KeszletConstants.KESZLET_AREUR));
								
								kosarEditForm.getField(
										KosarConstants.KOSAR_FIZETEUR).setValue(
										Constants.round(fizeteur,5));
							}

							if (arselect.getValue().equals(KosarRaktarConstants.PIACI_AR)) {
								double piaciar = 0;
								if (keszlet.getSelectedRecord().getAttributeAsDouble(
										KeszletConstants.KESZLET_AR) != null)
									piaciar = keszlet.getSelectedRecord()
										.getAttributeAsFloat(
												KeszletConstants.KESZLET_AR);						
								double fizetft = (double) (((((ckk * cdb) * exp)
										+ (cdb * kk) + db) * piaciar));
	
								kosarEditForm.getField(
										KosarConstants.KOSAR_AR).setValue(keszlet.getSelectedRecord()
												.getAttributeAsFloat(
														KeszletConstants.KESZLET_AR));

								kosarEditForm.getField(
										KosarConstants.KOSAR_FIZET).setValue(
										Constants.round(fizetft,2));
							}
											
							int rexp = 0;	
							if (keszlet.getSelectedRecord().getAttributeAsInt(
									KeszletConstants.KESZLET_KEXPORTKARTON) != null)
								rexp = keszlet.getSelectedRecord()
										.getAttributeAsInt(
												KeszletConstants.KESZLET_KEXPORTKARTON);
							int rkk = 0;	  
							if (keszlet.getSelectedRecord().getAttributeAsInt(
									KeszletConstants.KESZLET_KKISKARTON) != null)
								rkk = keszlet.getSelectedRecord()
										.getAttributeAsInt(
												KeszletConstants.KESZLET_KKISKARTON);

							int rdb = 0;	  
							if (keszlet.getSelectedRecord().getAttributeAsInt(
									KeszletConstants.KESZLET_KDARAB) != null)
								rdb = keszlet.getSelectedRecord()
										.getAttributeAsInt(
												KeszletConstants.KESZLET_KDARAB);
														
							int mexp = 0;	
							if (keszlet.getSelectedRecord().getAttributeAsInt(
									KeszletConstants.KESZLET_MEXPORTKARTON) != null)
								mexp = keszlet.getSelectedRecord()
										.getAttributeAsInt(
												KeszletConstants.KESZLET_MEXPORTKARTON);
							int mkk = 0;	  
							if (keszlet.getSelectedRecord().getAttributeAsInt(
									KeszletConstants.KESZLET_MKISKARTON) != null)
								mkk = keszlet.getSelectedRecord()
										.getAttributeAsInt(
												KeszletConstants.KESZLET_MKISKARTON);

							int mdb = 0;	  
							if (keszlet.getSelectedRecord().getAttributeAsInt(
									KeszletConstants.KESZLET_MDARAB) != null)
								mdb = keszlet.getSelectedRecord()
										.getAttributeAsInt(
												KeszletConstants.KESZLET_MDARAB);
							
							long formkeszlet = 
									((exp * ckk * cdb) + (kk * cdb) + db);													
							
							long raktar = 
									((rexp * ckk * cdb) + (rkk * cdb) + rdb);							

							long rendelt = 
									((mexp * ckk * cdb) + (mkk * cdb) + mdb);							
							
							if (formkeszlet > raktar) {
								event.cancel();
							}
							else {
							
								if (formkeszlet > (raktar - rendelt)) {
									
									SC.ask(kosarRaktarLabels.rendeltkeszlet(), new BooleanCallback() {
										public void execute(Boolean value) {
											if (value != null && value) {
												kosarEditForm.saveData(new DSCallback() {													
													public void execute(DSResponse response,
															Object rawData, DSRequest request) {
														if (response.getStatus() == DSResponse.STATUS_SUCCESS)
															winModal.destroy();
														else
															cancelIButton.setDisabled(false);
													}
												});
											} else cancelIButton.setDisabled(false);
										}
									});

								}
								else {
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
							}
						}
					});

					cancelIButton.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							winModal.destroy();
						}
					});

					winModal.addItem(kosarEditForm);
					winModal.addItem(arForm);
					winModal.addItem(buttonsLayout);
					winModal.show();
				
				} else {
														
					ListGridRecord record = new ListGridRecord();
					record.setAttribute(KosarConstants.KOSAR_CIKKSZAM, eladas.getRendelesGrid().getSelectedRecord().getAttributeAsString(EladasConstants.RENDELES_CIKKSZAM));
					record.setAttribute(KosarConstants.KOSAR_SZINKOD, eladas.getRendelesGrid().getSelectedRecord().getAttributeAsString(EladasConstants.RENDELES_SZINKOD));
					record.setAttribute(KosarConstants.KOSAR_EXPORTKARTON, eladas.getRendelesGrid().getSelectedRecord().getAttributeAsInt(EladasConstants.RENDELES_EXPORTKARTON));
					record.setAttribute(KosarConstants.KOSAR_KISKARTON, eladas.getRendelesGrid().getSelectedRecord().getAttributeAsInt(EladasConstants.RENDELES_KISKARTON));
					record.setAttribute(KosarConstants.KOSAR_DARAB, eladas.getRendelesGrid().getSelectedRecord().getAttributeAsInt(EladasConstants.RENDELES_DARAB));
					record.setAttribute(KosarConstants.KOSAR_RENDELES, eladas.getRendelesGrid().getSelectedRecord().getAttributeAsString(EladasConstants.RENDELES_RENDELES));
					record.setAttribute(KosarConstants.KOSAR_ARUSD, eladas.getRendelesGrid().getSelectedRecord().getAttributeAsDouble(EladasConstants.RENDELES_ARUSD));
					record.setAttribute(KosarConstants.KOSAR_FIZETUSD, eladas.getRendelesGrid().getSelectedRecord().getAttributeAsDouble(EladasConstants.RENDELES_FIZETUSD));
										
					kosarGrid.addData(record);
					
					eladas.getRendelesGrid().removeSelectedData();								
					
					addButton.setDisabled(true);
						
				}
			}
		});

		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask(commonLabels.sure(), new BooleanCallback() {
					public void execute(Boolean value) {
						if (value != null && value) {
							DisplayRequest.startRequest();

							if (menu.equals(Constants.MENU_ELADAS)) {
								
								final Double tmpbefizet = new Double(ftLabel.getContents());
								final Double tmpbefizeteur = new Double(eurLabel.getContents());
								final Double tmpbefizetusd = new Double(usdLabel.getContents());

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
												Cedulak cedulak = new Cedulak();
												middleLayout.addMember(cedulak
														.printCedula(
																result,
																Constants.CEDULA_STATUSZ_ELADOTT,
																vevonev, tmpbefizet,
																tmpbefizeteur,
																tmpbefizetusd, menu));
											}
										});
							}

							if (menu.equals(Constants.MENU_RAKTAR_KIADAS)) {
								
								final Double tmpbefizet = new Double(ftLabel.getContents());
								final Double tmpbefizeteur = new Double(eurLabel.getContents());
								final Double tmpbefizetusd = new Double(usdLabel.getContents());

								dekorTradeService.kosarToCedula(
										UserInfo.userId, vevo,
										Constants.MENU_RAKTAR_KIADAS, Constants.CEDULA_STATUSZ_KIADAS, Constants.CEDULA_STATUSZ_KIADOTT,
										cedula,tmpbefizet,
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
												middleLayout
														.removeMembers(middleLayout
																.getMembers());
												Cedulak cedulak = new Cedulak();
												middleLayout.addMember(cedulak.printCedula(
														cedula,
														Constants.CEDULA_STATUSZ_KIADOTT,
														vevonev,
														tmpbefizet,
														tmpbefizeteur,
														tmpbefizetusd,
														Constants.MENU_RAKTAR_KIADAS));
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
