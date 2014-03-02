package hu.dekortrade.client.basedata.cikktorzs;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.CikkSelectsSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.UploadSer;

import java.util.LinkedHashMap;
import java.util.Random;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.events.ErrorEvent;
import com.smartgwt.client.data.events.HandleErrorHandler;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.ExpansionMode;
import com.smartgwt.client.types.FormMethod;
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
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

public class Ctorzs {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private CtorzsLabels ctorzsLabels = GWT.create(CtorzsLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	private int page = 0;

	private String cikkszam = "";

	private String sorszam = "";
	
	public Canvas get(final IButton extIButton) {
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
				middleLayout.addMember(process(extIButton,result));
			}
		});
		return middleLayout;

	}
	
	public Canvas process(final IButton extIButton,final CikkSelectsSer cikkSelectsSer) {

		DisplayRequest.counterInit();

		HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		VLayout ctorzsLayout = new VLayout();
		ctorzsLayout.setStyleName("middle");
		ctorzsLayout.setWidth("900px");

		HLayout ctorzsFormLayout = new HLayout();
		ctorzsFormLayout.setHeight("3%");
		ctorzsFormLayout.setAlign(Alignment.CENTER);
		ctorzsFormLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		final DynamicForm ctorzsForm = new DynamicForm();
		ctorzsForm.setNumCols(6);
		ctorzsForm.setColWidths("10%", "15%", "10%", "15%", "10%", "*");

		final SelectItem fotipusSelectItem = new SelectItem();
		fotipusSelectItem.setWidth("180");
		fotipusSelectItem.setAllowEmptyValue(true);
		fotipusSelectItem.setTitle(ctorzsLabels.cikk_fotipus());
		fotipusSelectItem.setValueMap(cikkSelectsSer.getFotipus());

		final SelectItem altipusSelectItem = new SelectItem();
		altipusSelectItem.setWidth("180");
		altipusSelectItem.setAllowEmptyValue(true);
		altipusSelectItem.setTitle(ctorzsLabels.cikk_altipus());
		altipusSelectItem.setValueMap(new LinkedHashMap<String, String>());

		final TextItem cikkszamItem = new TextItem();
		cikkszamItem.setTitle(ctorzsLabels.cikk_cikkszam());
		cikkszamItem.setLength(15);

		ctorzsForm.setFields(fotipusSelectItem, altipusSelectItem, cikkszamItem);

		final IButton szuresIButton = new IButton(commonLabels.filter());
		szuresIButton.setDisabled(true);

		final IButton kepekIButton = new IButton(ctorzsLabels.cikk_kepek());
		kepekIButton.setDisabled(true);

		ctorzsFormLayout.addMember(ctorzsForm);
		ctorzsFormLayout.addMember(szuresIButton);
		ctorzsFormLayout.addMember(kepekIButton);

		HLayout ctorzsGridLayout = new HLayout();
		ctorzsGridLayout.setAlign(Alignment.CENTER);
		ctorzsGridLayout.setHeight("75%");

		final CtorzsDataSource ctorzsDataSource = new CtorzsDataSource(cikkSelectsSer.getFotipus(),cikkSelectsSer.getAltipus()) {

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

		ctorzsDataSource.addHandleErrorHandler(new HandleErrorHandler() {
			public void onHandleError(ErrorEvent event) {

				if (event.getResponse().getStatus() == DSResponse.STATUS_FAILURE) {
					if (event.getResponse().getAttribute(
							ClientConstants.SERVER_ERROR) != null)
						SC.warn(commonLabels.server_error());
					else if (event.getResponse().getAttribute(
							ClientConstants.SERVER_SQLERROR) != null)
						if (event.getResponse().getAttribute(
								ClientConstants.SERVER_SQLERROR).equals(Constants.EXISTSID)) {
							SC.warn(commonLabels.letezoid());							
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

		final ListGrid ctorzsGrid = new ListGrid();
		ctorzsGrid.setTitle(ctorzsLabels.ctorzs());
		ctorzsGrid.setWidth("100%");
		ctorzsGrid.setShowHeaderContextMenu(false);
		ctorzsGrid.setShowHeaderMenuButton(false);
		ctorzsGrid.setCanSort(false);
		ctorzsGrid.setShowAllRecords(true);
		ctorzsGrid.setDataSource(ctorzsDataSource);
		ctorzsGrid.setCanExpandRecords(true);
		ctorzsGrid.setExpansionMode(ExpansionMode.DETAILS);
		Criteria criteria = new Criteria();
		criteria.setAttribute(CtorzsConstants.CTORZS_PAGE, page);
		criteria.setAttribute(CtorzsConstants.CIKK_FOTIPUS,
				fotipusSelectItem.getValueAsString());
		criteria.setAttribute(CtorzsConstants.CIKK_ALTIPUS,
				altipusSelectItem.getValueAsString());
		criteria.setAttribute(CtorzsConstants.CIKK_CIKKSZAM,
				cikkszamItem.getValueAsString());
		ctorzsGrid.fetchData(criteria);

		ListGridField cikkszamGridField = new ListGridField(
				CtorzsConstants.CIKK_CIKKSZAM);
		cikkszamGridField.setWidth("20%");

		ListGridField megnevezesGridField = new ListGridField(
				CtorzsConstants.CIKK_MEGNEVEZES);
		
		ListGridField kiskartonGridField = new ListGridField(
				CtorzsConstants.CIKK_KISKARTON);
		kiskartonGridField.setWidth("15%");

		ListGridField darabGridField = new ListGridField(
				CtorzsConstants.CIKK_DARAB);
		darabGridField.setWidth("15%");

		ListGridField arGridField = new ListGridField(CtorzsConstants.CIKK_AR);
		arGridField.setWidth("10%");

		ListGridField kepekGridField = new ListGridField(
				CtorzsConstants.CIKK_KEPEK);
		kepekGridField.setWidth("10%");

		ctorzsGrid
				.setFields(cikkszamGridField, megnevezesGridField, arGridField, kiskartonGridField, darabGridField,
						kepekGridField);

		ctorzsGridLayout.addMember(ctorzsGrid);

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

		prevnextLayout.addMember(previousIButton);
		prevnextLayout.addMember(pageLabel);
		prevnextLayout.addMember(nextIButton);

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("100%");

		HLayout addButtonLayout = new HLayout();
		addButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		IButton addButton = new IButton(commonLabels.add());
		addButtonLayout.setAlign(Alignment.CENTER);
		addButtonLayout.addMember(addButton);

		HLayout modifyButtonLayout = new HLayout();
		modifyButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		modifyButtonLayout.setAlign(Alignment.CENTER);
		final IButton modifyButton = new IButton(commonLabels.modify());
		modifyButton.disable();
		modifyButtonLayout.addMember(modifyButton);

		HLayout loadButtonLayout = new HLayout();
		loadButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		loadButtonLayout.setAlign(Alignment.CENTER);
		final IButton loadButton = new IButton(ctorzsLabels.kepfeltoltes());
		loadButton.disable();
		loadButtonLayout.addMember(loadButton);		
		
		HLayout deleteButtonLayout = new HLayout();
		deleteButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		deleteButtonLayout.setAlign(Alignment.CENTER);
		final IButton deleteButton = new IButton(commonLabels.delete());
		deleteButton.disable();
		deleteButtonLayout.addMember(deleteButton);

		buttonsLayout.addMember(addButtonLayout);
		buttonsLayout.addMember(modifyButtonLayout);
		buttonsLayout.addMember(loadButtonLayout);
		buttonsLayout.addMember(deleteButtonLayout);

		ctorzsLayout.addMember(ctorzsFormLayout);
		ctorzsLayout.addMember(ctorzsGridLayout);
		ctorzsLayout.addMember(prevnextLayout);
		ctorzsLayout.addMember(buttonsLayout);

		szuresIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ctorzsGrid.invalidateCache();
				Criteria criteria = new Criteria();
				page = 0;
				criteria.setAttribute(CtorzsConstants.CTORZS_PAGE, page);
				criteria.setAttribute(CtorzsConstants.CIKK_FOTIPUS,
						fotipusSelectItem.getValueAsString());
				criteria.setAttribute(CtorzsConstants.CIKK_ALTIPUS,
						altipusSelectItem.getValueAsString());
				criteria.setAttribute(CtorzsConstants.CIKK_CIKKSZAM,
						cikkszamItem.getValueAsString());
				ctorzsGrid.fetchData(criteria);
				szuresIButton.setDisabled(true);
				previousIButton.setDisabled(true);
				nextIButton.setDisabled(true);
				kepekIButton.setDisabled(true);

				modifyButton.setDisabled(true);
				loadButton.setDisabled(true);
				deleteButton.setDisabled(true);

				if (extIButton != null)
					extIButton.setDisabled(true);
				pageLabel.setContents("");
			}
		});

		previousIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ctorzsGrid.invalidateCache();
				Criteria criteria = new Criteria();
				page = page - 1;
				criteria.setAttribute(CtorzsConstants.CTORZS_PAGE, page);
				criteria.setAttribute(CtorzsConstants.CIKK_FOTIPUS,
						fotipusSelectItem.getValueAsString());
				criteria.setAttribute(CtorzsConstants.CIKK_ALTIPUS,
						altipusSelectItem.getValueAsString());
				criteria.setAttribute(CtorzsConstants.CIKK_CIKKSZAM,
						cikkszamItem.getValueAsString());
				ctorzsGrid.fetchData(criteria);
				szuresIButton.setDisabled(true);
				previousIButton.setDisabled(true);
				nextIButton.setDisabled(true);
				kepekIButton.setDisabled(true);

				modifyButton.setDisabled(true);
				loadButton.setDisabled(true);
				deleteButton.setDisabled(true);

				if (extIButton != null)
					extIButton.setDisabled(true);
				pageLabel.setContents("");
			}
		});

		nextIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ctorzsGrid.invalidateCache();
				Criteria criteria = new Criteria();
				page = page + 1;
				criteria.setAttribute(CtorzsConstants.CTORZS_PAGE, page);
				criteria.setAttribute(CtorzsConstants.CIKK_FOTIPUS,
						fotipusSelectItem.getValueAsString());
				criteria.setAttribute(CtorzsConstants.CIKK_ALTIPUS,
						altipusSelectItem.getValueAsString());
				criteria.setAttribute(CtorzsConstants.CIKK_CIKKSZAM,
						cikkszamItem.getValueAsString());
				ctorzsGrid.fetchData(criteria);
				szuresIButton.setDisabled(true);
				previousIButton.setDisabled(true);
				nextIButton.setDisabled(true);
				kepekIButton.setDisabled(true);

				modifyButton.setDisabled(true);
				loadButton.setDisabled(true);
				deleteButton.setDisabled(true);

				if (extIButton != null)
					extIButton.setDisabled(true);
				pageLabel.setContents("");
			}
		});

		ctorzsGrid.addDataArrivedHandler(new DataArrivedHandler() {
			public void onDataArrived(DataArrivedEvent event) {

				if (ctorzsGrid.getRecords().length == Constants.FETCH_SIZE) {
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
											+ ctorzsGrid.getRecords().length)
									.toString());
				}
				if (page > 0)
					previousIButton.setDisabled(false);
				szuresIButton.setDisabled(false);
			}

		});

		ctorzsGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				modifyButton.setDisabled(false);
				loadButton.setDisabled(false);
				deleteButton.setDisabled(false);
				if (extIButton != null) {
					extIButton.setDisabled(false);
				}
				
				cikkszam = ctorzsGrid.getSelectedRecord().getAttribute(
						CtorzsConstants.CIKK_CIKKSZAM);

				if (ctorzsGrid.getSelectedRecord().getAttributeAsInt(CtorzsConstants.CIKK_KEPEK) > 0)
					kepekIButton.setDisabled(false);
				else 
					kepekIButton.setDisabled(true);
			}
		});

		kepekIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
					
				Window winModal = new Window();
				winModal.setWidth(900);
				winModal.setHeight(800);
				winModal.setTitle(cikkszam
						+ " - "
						+ ctorzsGrid.getSelectedRecord().getAttribute(
								CtorzsConstants.CIKK_MEGNEVEZES));
				winModal.setShowMinimizeButton(false);
				winModal.setIsModal(true);
				winModal.setShowModalMask(true);
				winModal.centerInPage();
				
				HLayout layout = new HLayout();
				layout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
				layout.setAlign(Alignment.CENTER);
				
				final KepDataSource kepDataSource = new KepDataSource() {

					protected Object transformRequest(DSRequest dsRequest) {
						DisplayRequest.startRequest();
						dsRequest.setAttribute(CtorzsConstants.CIKK_CIKKSZAM,
								cikkszam);
						dsRequest.setAttribute(CtorzsConstants.KEP_SORSZAM,
								sorszam);
						return super.transformRequest(dsRequest);
					}

					protected void transformResponse(DSResponse response,
							DSRequest request, Object data) {
						DisplayRequest.serverResponse();
						super.transformResponse(response, request, data);
					}
				};

				kepDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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

				final TileGrid tileGrid = new TileGrid();  
				tileGrid.setHeight(750);
			    tileGrid.setWidth(750);
			    tileGrid.setTileWidth(700);
		        tileGrid.setTileHeight(500);  
		        tileGrid.setShowAllRecords(true);  
		        tileGrid.setDataSource(kepDataSource);  
		        tileGrid.setAutoFetchData(true);  
			  				
				DetailViewerField pictureField = new DetailViewerField(CtorzsConstants.KEP_KEP);  			
			    pictureField.setImageWidth(650);
			    pictureField.setImageHeight(450);

				tileGrid.setFields(pictureField);
								
				HLayout tiledeleteButtonLayout = new HLayout();
				tiledeleteButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
				tiledeleteButtonLayout.setAlign(Alignment.CENTER);
				final IButton tiledeleteButton = new IButton(commonLabels.delete());
				tiledeleteButton.disable();
				tiledeleteButtonLayout.addMember(tiledeleteButton);

				layout.addMember(tileGrid);
				layout.addMember(tiledeleteButtonLayout);
				
				winModal.addItem(layout);
				
				tileGrid.addRecordClickHandler(new com.smartgwt.client.widgets.tile.events.RecordClickHandler() {
						
					public void onRecordClick(
							com.smartgwt.client.widgets.tile.events.RecordClickEvent event) {
						tiledeleteButton.setDisabled(false);
					}
				});
				
				tiledeleteButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						SC.ask(commonLabels.sure(), new BooleanCallback() {
							public void execute(Boolean value) {
								if (value != null && value) {
									sorszam = tileGrid.getSelectedRecord().getAttribute(CtorzsConstants.KEP_SORSZAM);
									tileGrid.removeSelectedData();
									tiledeleteButton.setDisabled(true);
									int kepek = ctorzsGrid.getSelectedRecord().getAttributeAsInt(CtorzsConstants.CIKK_KEPEK);
									if (kepek > 0) kepek--;
									ctorzsGrid.getSelectedRecord().setAttribute(CtorzsConstants.CIKK_KEPEK, kepek);
									ctorzsGrid.markForRedraw();
								}
							}
						});
					}
				});
								
				winModal.show();							
			}
		});

		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ctorzsEdit(ctorzsDataSource, ctorzsGrid, Boolean.TRUE, cikkSelectsSer);
			}
		});

		modifyButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ctorzsEdit(ctorzsDataSource, ctorzsGrid, Boolean.FALSE, cikkSelectsSer);
			}
		});

		deleteButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask(commonLabels.sure(), new BooleanCallback() {
					public void execute(Boolean value) {
						if (value != null && value) {
							ctorzsGrid.removeSelectedData();
							modifyButton.setDisabled(true);
							loadButton.setDisabled(true);
							deleteButton.setDisabled(true);
						}
					}
				});

			}
		});
		
		loadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				upload(ctorzsGrid);
			}
		});
		
		fotipusSelectItem.addChangedHandler(new ChangedHandler() {
			public void onChanged(ChangedEvent event) {
				altipusSelectItem.clearValue();
				LinkedHashMap<String, String> altipus = cikkSelectsSer.getTipus().get(event.getValue());
				if (altipus == null) altipusSelectItem.setValueMap(new LinkedHashMap<String, String>());
				else altipusSelectItem.setValueMap(altipus);
			}
		});
		
		middleLayout.addMember(ctorzsLayout);

		return middleLayout;

	}

	public String getCikkszam() {
		return cikkszam;
	}

	void ctorzsEdit(final CtorzsDataSource dataSource, final ListGrid listGrid, boolean uj,final CikkSelectsSer cikkSelectsSer) {

		final Window winModal = new Window();
		winModal.setWidth(600);
		winModal.setHeight(350);
		winModal.setTitle(ctorzsLabels.cikk());
		winModal.setShowMinimizeButton(false);
		winModal.setShowCloseButton(false);
		winModal.setIsModal(true);
		winModal.setShowModalMask(true);
		winModal.centerInPage();

		final DynamicForm editForm = new DynamicForm();
		editForm.setNumCols(2);
		editForm.setColWidths("15%", "*");
		editForm.setDataSource(dataSource);
		editForm.setUseAllDataSourceFields(true);

		if (uj)  {
			editForm.editNewRecord();
			editForm.getField(CtorzsConstants.CIKK_FOTIPUS).setDefaultValue("1");
			editForm.getField(CtorzsConstants.CIKK_ALTIPUS).setDefaultValue("1");
		}
		else {
			editForm.getField(CtorzsConstants.CIKK_CIKKSZAM).setCanEdit(false);	
			editForm.editSelectedData(listGrid);
		}
		
		LinkedHashMap<String, String> altipus = cikkSelectsSer.getTipus().get(editForm.getField(CtorzsConstants.CIKK_FOTIPUS).getValue());
		if (altipus == null) editForm.getField(CtorzsConstants.CIKK_ALTIPUS).setValueMap(new LinkedHashMap<String, String>());
		else editForm.getField(CtorzsConstants.CIKK_ALTIPUS).setValueMap(altipus);		
	
		editForm.getField(CtorzsConstants.CIKK_FOTIPUS).addChangedHandler(new ChangedHandler() {
			public void onChanged(ChangedEvent event) {
				editForm.getField(CtorzsConstants.CIKK_ALTIPUS).clearValue();
				LinkedHashMap<String, String> altipus = cikkSelectsSer.getTipus().get(event.getValue());
				if (altipus == null) editForm.getField(CtorzsConstants.CIKK_ALTIPUS).setValueMap(new LinkedHashMap<String, String>());
				else editForm.getField(CtorzsConstants.CIKK_ALTIPUS).setValueMap(altipus);
			}
		});
		
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

	void upload(final ListGrid ctorzsGrid) {

		final Window winModal = new Window();
		winModal.setWidth(600);
		winModal.setHeight(150);
		winModal.setTitle(ctorzsLabels.kepfeltoltes());
		winModal.setShowMinimizeButton(false);
		winModal.setShowCloseButton(false);
		winModal.setIsModal(true);
		winModal.setShowModalMask(true);
		winModal.centerInPage();
			
		final HLayout formLayout = new HLayout();
		formLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		formLayout.setAlign(Alignment.CENTER);
		formLayout.setWidth("100%");
		formLayout.setHeight("30%");

		final DynamicForm uploadForm = new DynamicForm();
		uploadForm.setEncoding(Encoding.MULTIPART);
		uploadForm.setMethod(FormMethod.POST);
		uploadForm.setWidth100();
		uploadForm.setNumCols(2);
		uploadForm.setColWidths("20%", "*");
		uploadForm.setTarget("fileUploadFrame"); 
		Random generator = new Random();
		final String random = Double.toString(generator.nextDouble()); 
		uploadForm.setAction(GWT.getModuleBaseURL()+ "upload?cikkszam=" + cikkszam + "&random=" + random);
		
		final UploadItem uploadItem = new UploadItem();
		uploadItem.setWidth(300);
		uploadItem.setName("kep");
		uploadItem.setTitle(cikkszam);
		uploadItem.setRequired(true);

		uploadForm.setFields(uploadItem);
		
		formLayout.addMember(uploadForm);
		
		final HLayout labelLayout = new HLayout();
		labelLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		labelLayout.setAlign(Alignment.CENTER);
		labelLayout.setWidth("100%");
		labelLayout.setHeight("20%");

		final Label label = new Label();
		label.setWidth("100%");
		label.setContents("");
		label.setAlign(Alignment.CENTER);
	
		labelLayout.addMember(label);

		final HLayout formButtonsLayout = new HLayout();
		formButtonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		formButtonsLayout.setAlign(Alignment.CENTER);
		formButtonsLayout.setWidth("100%");

		HLayout saveLayout = new HLayout();
		saveLayout.setAlign(Alignment.CENTER);
		saveLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		IButton saveIButton = new IButton(commonLabels.save());
	
		HLayout cancelLayout = new HLayout();
		cancelLayout.setAlign(Alignment.CENTER);
		cancelLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		IButton cancelIButton = new IButton(commonLabels.cancel());
		
		saveLayout.addMember(saveIButton);
		cancelLayout.addMember(cancelIButton);
		formButtonsLayout.addMember(saveLayout);
		formButtonsLayout.addMember(cancelLayout);
			
		winModal.addItem(formLayout);			
		winModal.addItem(labelLayout);	
		winModal.addItem(formButtonsLayout);		
			
		saveIButton.addClickHandler(new ClickHandler() {			
			public void onClick(ClickEvent event) {	
				if ((uploadItem.getValueAsString() != null) && (uploadItem.getValueAsString().contains(".jpg"))) {	
					dekorTradeService.initUploadFileStatus(
        				new AsyncCallback<String>() {
        					public void onFailure(Throwable caught) {
        						
        					}
        					public void onSuccess(String result) {          
        						uploadForm.submitForm();
        						uploadForm.setDisabled(true);	
        						formButtonsLayout.setDisabled(true);
        						label.setContents(ctorzsLabels.toltes());
        						new Timer() {
        				            int timesWaited = 0;  
        				            int maxTimes = 100;
        				            
        							public void run() {				
        				                timesWaited++; 
        				                if (timesWaited >= maxTimes){
        	        						uploadForm.setDisabled(false);	
        	        						formButtonsLayout.setDisabled(false);
        				                	label.setContents("");
        				                	SC.say(ctorzsLabels.idotullepes()); 
        				                } 
        				                else {
        				                	dekorTradeService.getUploadFileStatus( 
        				        				new AsyncCallback<UploadSer>() {
        				        					public void onFailure(Throwable caught) {
        				        						winModal.destroy();
        				        						SC.say(ctorzsLabels.tolteshiba());
        				         					}
        				        					public void onSuccess(UploadSer result) {        				        						      				        					
        				        						if (result.getStatus().equals(Constants.LOADING)) {        				        						
        				        							schedule(ClientConstants.PROGRESS_SCHEDULE);
        				        						}
        				        						else  {
        				        							if (result.getStatus().equals(Constants.ERROR)) {  
        				      	        						uploadForm.setDisabled(false);	
        			        	        						formButtonsLayout.setDisabled(false);
           				        								label.setContents("");
           				        								if (result.getError().equals(Constants.FILE_SAVE_ERROR)) SC.say(ctorzsLabels.tolteshiba());     	
           				        								else SC.say(ctorzsLabels.tulnagyfile());     				        								
        				        							}
         				        							else  {
         				        								ctorzsGrid.getSelectedRecord().setAttribute(CtorzsConstants.CIKK_KEPEK, 
         				        										ctorzsGrid.getSelectedRecord().getAttributeAsInt(
         				        										CtorzsConstants.CIKK_KEPEK)+1); 
         				        								ctorzsGrid.updateData(ctorzsGrid.getSelectedRecord());
             				        							winModal.destroy();       				        								
        				        							}
        				        						}
        				         					}
        				        				});
        				                }
        							}
        						}.schedule(ClientConstants.PROGRESS_SCHEDULE);
        					        						        						
          					}
        				});	
				}
				else SC.say(ctorzsLabels.jpg());  
			}			
		});		
			
		cancelIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				winModal.destroy();
			}
		});
	
		winModal.show();

	}

}
