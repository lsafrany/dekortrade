package hu.dekortrade.client.raktar.kesztlet;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.CikkSelectsSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.events.ErrorEvent;
import com.smartgwt.client.data.events.HandleErrorHandler;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ExpansionMode;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Keszlet {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private KeszletLabels keszletLabels = GWT.create(KeszletLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);
	
	private int page = 0;

	private ListGridRecord selectedRecord = null;

	private String rovancs = "";
	
	public Canvas get(final String menu, final IButton extIButton) {
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
		dekorTradeService.getCikkSelects(new AsyncCallback<CikkSelectsSer>() {
			public void onFailure(Throwable caught) {
				DisplayRequest.serverResponse();
				if (caught instanceof SQLExceptionSer)
					SC.warn(commonLabels.server_sqlerror() + " : "
							+ caught.getMessage());
				else
					SC.warn(commonLabels.server_error());
			}

			public void onSuccess(final CikkSelectsSer result) {
				DisplayRequest.serverResponse();
				middleLayout.removeMembers(middleLayout.getMembers());
				middleLayout.addMember(process(menu,extIButton,result));
			}
		});
		return middleLayout;
	}

	public Canvas process(final String menu, final IButton extIButton, final CikkSelectsSer cikkSelectsSer) {

		DisplayRequest.counterInit();

		HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		VLayout keszletLayout = new VLayout();
		keszletLayout.setStyleName("middle");
		keszletLayout.setWidth("800px");

		HLayout keszletFormLayout = new HLayout();
		keszletFormLayout.setHeight("3%");
		keszletFormLayout.setAlign(Alignment.CENTER);
		keszletFormLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		final DynamicForm keszletForm = new DynamicForm();
		keszletForm.setNumCols(6);
		keszletForm.setColWidths("10%", "15%", "10%", "15%", "10%", "*");

		final SelectItem fotipusSelectItem = new SelectItem();
		fotipusSelectItem.setWidth("160");
		fotipusSelectItem.setAllowEmptyValue(true);
		fotipusSelectItem.setTitle(keszletLabels.raktar_fotipus());
		fotipusSelectItem.setValueMap(cikkSelectsSer.getFotipus());

		final SelectItem altipusSelectItem = new SelectItem();
		altipusSelectItem.setWidth("160");
		altipusSelectItem.setAllowEmptyValue(true);
		altipusSelectItem.setTitle(keszletLabels.raktar_altipus());
		altipusSelectItem.setValueMap(new LinkedHashMap<String, String>());

		final TextItem cikkszamItem = new TextItem();
		cikkszamItem.setTitle(keszletLabels.raktar_cikkszam());
		cikkszamItem.setWidth("100");
		cikkszamItem.setLength(15);

		keszletForm
				.setFields(fotipusSelectItem, altipusSelectItem, cikkszamItem);

		final IButton szuresIButton = new IButton(commonLabels.filter());
		// szuresIButton.setDisabled(true);

		keszletFormLayout.addMember(keszletForm);
		keszletFormLayout.addMember(szuresIButton);

		HLayout keszletGridLayout = new HLayout();
		keszletGridLayout.setAlign(Alignment.CENTER);
		keszletGridLayout.setHeight("75%");

		final RaktarDataSource raktarDataSource = new RaktarDataSource(
				cikkSelectsSer.getFotipus(), cikkSelectsSer.getAltipus()) {

			protected Object transformRequest(DSRequest dsRequest) {
				DisplayRequest.startRequest();
				dsRequest.setAttribute(KeszletConstants.RAKTAR_ROVANCS, rovancs);
				return super.transformRequest(dsRequest);
			}

			protected void transformResponse(DSResponse response,
					DSRequest request, Object data) {
				DisplayRequest.serverResponse();
				super.transformResponse(response, request, data);
			}
		};

		raktarDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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

		final ListGrid keszletGrid = new ListGrid();
		keszletGrid.setTitle(keszletLabels.raktar());
		keszletGrid.setWidth("100%");
		keszletGrid.setShowHeaderContextMenu(false);
		keszletGrid.setShowHeaderMenuButton(false);
		keszletGrid.setCanSort(false);
		keszletGrid.setShowAllRecords(true);
		keszletGrid.setDataSource(raktarDataSource);
		keszletGrid.setCanExpandRecords(true);
		keszletGrid.setExpansionMode(ExpansionMode.DETAILS);

		ListGridField cikkszamGridField = new ListGridField(
				KeszletConstants.KESZLET_CIKKSZAM );
		cikkszamGridField.setWidth("20%");

		ListGridField szinkodGridField = new ListGridField(
				KeszletConstants.KESZLET_SZINKOD);
		szinkodGridField.setWidth("10%");

		ListGridField megnevezesGridField = new ListGridField(
				KeszletConstants.KESZLET_MEGNEVEZES);

		ListGridField exportkartonGridField = new ListGridField(
				KeszletConstants.KESZLET_KEXPORTKARTON);
		exportkartonGridField.setWidth("10%");

		ListGridField kiskartonGridField = new ListGridField(
				KeszletConstants.KESZLET_KKISKARTON);
		kiskartonGridField.setWidth("10%");
		
		ListGridField darabGridField = new ListGridField(
				KeszletConstants.KESZLET_KDARAB);
		darabGridField.setWidth("10%");
		
		keszletGrid.setFields(cikkszamGridField, szinkodGridField,
				megnevezesGridField,exportkartonGridField,kiskartonGridField,darabGridField);

		keszletGridLayout.addMember(keszletGrid);

		HLayout prevnextLayout = new HLayout();
		prevnextLayout.setAlign(Alignment.CENTER);
		prevnextLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		prevnextLayout.setHeight("3%");
		prevnextLayout.setWidth("100%");

		final IButton previousIButton = new IButton("&lt;&lt;");
		previousIButton.setDisabled(true);
		final Label pageLabel = new Label();
		pageLabel.setContents("");
		pageLabel.setAlign(Alignment.CENTER);
		pageLabel.setWidth("100px");

		final IButton nextIButton = new IButton("&gt;&gt;");
		nextIButton.setDisabled(true);
		
		prevnextLayout.addMember(previousIButton);
		prevnextLayout.addMember(pageLabel);
		prevnextLayout.addMember(nextIButton);

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("100%");

		HLayout rovancsButtonLayout = new HLayout();
		rovancsButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		rovancsButtonLayout.setAlign(Alignment.CENTER);
		final IButton rovancsButton = new IButton(keszletLabels.rovancs());
		rovancsButton.disable();
		rovancsButtonLayout.addMember(rovancsButton);

		HLayout helykodButtonLayout = new HLayout();
		helykodButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		helykodButtonLayout.setAlign(Alignment.CENTER);
		final IButton helykodButton = new IButton(keszletLabels.raktar_hely());
		helykodButton.disable();
		helykodButtonLayout.addMember(helykodButton);

		buttonsLayout.addMember(helykodButtonLayout);
		buttonsLayout.addMember(rovancsButtonLayout);

		keszletLayout.addMember(keszletFormLayout);
		keszletLayout.addMember(keszletGridLayout);
		keszletLayout.addMember(prevnextLayout);
		if (menu.equals(Constants.MENU_RAKTAR_KESZLET)) {
			keszletLayout.addMember(buttonsLayout);
		}

		middleLayout.addMember(keszletLayout);

		szuresIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				keszletGrid.invalidateCache();
				Criteria criteria = new Criteria();
				page = 0;
				criteria.setAttribute(KeszletConstants.RAKTAR_PAGE, page);
				criteria.setAttribute(KeszletConstants.KESZLET_FOTIPUS,
						fotipusSelectItem.getValueAsString());
				criteria.setAttribute(KeszletConstants.KESZLET_ALTIPUS,
						altipusSelectItem.getValueAsString());
				criteria.setAttribute(KeszletConstants.KESZLET_CIKKSZAM,
						cikkszamItem.getValueAsString());
				keszletGrid.fetchData(criteria);
				szuresIButton.setDisabled(true);
				previousIButton.setDisabled(true);
				nextIButton.setDisabled(true);

				rovancsButton.setDisabled(true);

				pageLabel.setContents("");
			}
		});

		previousIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				keszletGrid.invalidateCache();
				Criteria criteria = new Criteria();
				page = page - 1;
				criteria.setAttribute(KeszletConstants.RAKTAR_PAGE, page);
				criteria.setAttribute(KeszletConstants.KESZLET_FOTIPUS,
						fotipusSelectItem.getValueAsString());
				criteria.setAttribute(KeszletConstants.KESZLET_ALTIPUS,
						altipusSelectItem.getValueAsString());
				criteria.setAttribute(KeszletConstants.KESZLET_CIKKSZAM,
						cikkszamItem.getValueAsString());
				keszletGrid.fetchData(criteria);
				szuresIButton.setDisabled(true);
				previousIButton.setDisabled(true);
				nextIButton.setDisabled(true);
	
				rovancsButton.setDisabled(true);

				pageLabel.setContents("");
			}
		});

		nextIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				keszletGrid.invalidateCache();
				Criteria criteria = new Criteria();
				page = page + 1;
				criteria.setAttribute(KeszletConstants.RAKTAR_PAGE, page);
				criteria.setAttribute(KeszletConstants.KESZLET_FOTIPUS,
						fotipusSelectItem.getValueAsString());
				criteria.setAttribute(KeszletConstants.KESZLET_ALTIPUS,
						altipusSelectItem.getValueAsString());
				criteria.setAttribute(KeszletConstants.KESZLET_CIKKSZAM,
						cikkszamItem.getValueAsString());
				keszletGrid.fetchData(criteria);
				szuresIButton.setDisabled(true);
				previousIButton.setDisabled(true);
				nextIButton.setDisabled(true);

				rovancsButton.setDisabled(true);

				pageLabel.setContents("");
			}
		});

		keszletGrid.addDataArrivedHandler(new DataArrivedHandler() {
			public void onDataArrived(DataArrivedEvent event) {

				if (keszletGrid.getRecords().length == Constants.FETCH_SIZE) {
					nextIButton.setDisabled(false);
					pageLabel.setContents(Integer.valueOf(
							(page * Constants.FETCH_SIZE) + 1).toString()
							+ " - "
							+ Integer
									.valueOf((page + 1) * Constants.FETCH_SIZE)
									.toString());
				} else {
					pageLabel.setContents(Integer.valueOf(
							(page * Constants.FETCH_SIZE) + 1).toString()
							+ " - "
							+ Integer.valueOf(
									(page * Constants.FETCH_SIZE)
											+ keszletGrid.getRecords().length)
									.toString());
				}
				if (page > 0)
					previousIButton.setDisabled(false);
				szuresIButton.setDisabled(false);
			}

		});

		keszletGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				rovancsButton.setDisabled(false);
				helykodButton.setDisabled(false);
				if (extIButton != null) extIButton.setDisabled(false);
				selectedRecord = keszletGrid.getSelectedRecord();
			}
		});

		rovancsButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				rovancs = KeszletConstants.RAKTAR_ROVANCS;
				final Window winModal = new Window();
				winModal.setWidth(450);
				winModal.setHeight(300);
				winModal.setTitle(keszletGrid.getSelectedRecord().getAttribute(KeszletConstants.KESZLET_CIKKSZAM) + "- " + keszletGrid.getSelectedRecord().getAttribute(KeszletConstants.KESZLET_SZINKOD));
				winModal.setShowMinimizeButton(false);
				winModal.setShowCloseButton(false);
				winModal.setIsModal(true);
				winModal.setShowModalMask(true);
				winModal.centerInPage();

				final DynamicForm editForm = new DynamicForm();
				editForm.setNumCols(2);
				editForm.setColWidths("40%", "*");
				editForm.setDataSource(raktarDataSource);
				editForm.setUseAllDataSourceFields(true);

				editForm.getField(KeszletConstants.KESZLET_FOTIPUS).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_ALTIPUS).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_CIKKSZAM).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_SZINKOD).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_MEGNEVEZES).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_KISKARTON).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_DARAB).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_ELORAR).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_AR).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_AREUR).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_TERFOGAT).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_TERFOGATLAB).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_BSULY).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_NSULY).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_LEIRAS).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_MEGJEGYZES).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_MERTEKEGYSEG).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_HELYKOD).setVisible(false);
				
				editForm.editSelectedData(keszletGrid);
				
				HLayout buttonsLayout = new HLayout();
				buttonsLayout.setAlign(Alignment.CENTER);
				buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
				buttonsLayout.setWidth("100%");

				HLayout saveLayout = new HLayout();
				saveLayout.setAlign(Alignment.CENTER);
				saveLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
				IButton saveIButton = new IButton(commonLabels.save());
				saveIButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						editForm.saveData(new DSCallback() {
							public void execute(DSResponse response, Object rawData,
									DSRequest request) {
								if (response.getStatus() == DSResponse.STATUS_SUCCESS)
									winModal.destroy();
							}
						});
					}
				});
				saveLayout.addMember(saveIButton);
				buttonsLayout.addMember(saveLayout);

				HLayout cancelLayout = new HLayout();
				cancelLayout.setAlign(Alignment.CENTER);
				cancelLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
				IButton cancelIButton = new IButton(commonLabels.cancel());
				cancelIButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						winModal.destroy();
					}
				});
				cancelLayout.addMember(cancelIButton);
				buttonsLayout.addMember(cancelLayout);

				winModal.addItem(editForm);
				winModal.addItem(buttonsLayout);
				winModal.show();			
			}
		});

		helykodButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				rovancs = "";
				final Window winModal = new Window();
				winModal.setWidth(450);
				winModal.setHeight(200);
				winModal.setTitle(keszletGrid.getSelectedRecord().getAttribute(KeszletConstants.KESZLET_CIKKSZAM) + "- " + keszletGrid.getSelectedRecord().getAttribute(KeszletConstants.KESZLET_SZINKOD));
				winModal.setShowMinimizeButton(false);
				winModal.setShowCloseButton(false);
				winModal.setIsModal(true);
				winModal.setShowModalMask(true);
				winModal.centerInPage();

				final DynamicForm editForm = new DynamicForm();
				editForm.setNumCols(2);
				editForm.setColWidths("40%", "*");
				editForm.setDataSource(raktarDataSource);
				editForm.setUseAllDataSourceFields(true);

				editForm.getField(KeszletConstants.KESZLET_FOTIPUS).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_ALTIPUS).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_CIKKSZAM).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_SZINKOD).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_MEGNEVEZES).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_KISKARTON).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_DARAB).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_ELORAR).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_AR).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_AREUR).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_TERFOGAT).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_TERFOGATLAB).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_BSULY).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_NSULY).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_LEIRAS).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_MEGJEGYZES).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_MERTEKEGYSEG).setVisible(false);

				editForm.getField(KeszletConstants.KESZLET_KEXPORTKARTON).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_KKISKARTON).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_KDARAB).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_MEXPORTKARTON).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_MKISKARTON).setVisible(false);
				editForm.getField(KeszletConstants.KESZLET_MDARAB).setVisible(false);

				editForm.editSelectedData(keszletGrid);
				
				HLayout buttonsLayout = new HLayout();
				buttonsLayout.setAlign(Alignment.CENTER);
				buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
				buttonsLayout.setWidth("100%");

				HLayout saveLayout = new HLayout();
				saveLayout.setAlign(Alignment.CENTER);
				saveLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
				IButton saveIButton = new IButton(commonLabels.save());
				saveIButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						editForm.saveData(new DSCallback() {
							public void execute(DSResponse response, Object rawData,
									DSRequest request) {
								if (response.getStatus() == DSResponse.STATUS_SUCCESS)
									winModal.destroy();
							}
						});
					}
				});
				saveLayout.addMember(saveIButton);
				buttonsLayout.addMember(saveLayout);

				HLayout cancelLayout = new HLayout();
				cancelLayout.setAlign(Alignment.CENTER);
				cancelLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
				IButton cancelIButton = new IButton(commonLabels.cancel());
				cancelIButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						winModal.destroy();
					}
				});
				cancelLayout.addMember(cancelIButton);
				buttonsLayout.addMember(cancelLayout);

				winModal.addItem(editForm);
				winModal.addItem(buttonsLayout);
				winModal.show();			
			}
		});
		
		return middleLayout;
	
	}

	public ListGridRecord getSelectedRecord() {
		return selectedRecord;
	}

	public void setSelectedRecord(ListGridRecord selectedRecord) {
		this.selectedRecord = selectedRecord;
	}
	
}
