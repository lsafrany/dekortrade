package hu.dekortrade.client.lekerdezes.forgalom;

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
import com.smartgwt.client.widgets.form.fields.TextItem;
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

public class Forgalom {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private ForgalomLabels forgalomLabels = GWT.create(ForgalomLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	private int page = 0;

	private String sorszam = "";
	
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
				middleLayout.addMember(process(result));
			}
		});
		return middleLayout;

	}

	public Canvas process(CikkSelectsSer cikkSelectsSer) {
		DisplayRequest.counterInit();

		final HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

		VLayout ctorzsLayout = new VLayout();
		ctorzsLayout.setStyleName("middle");
		ctorzsLayout.setWidth("800px");
	
		final VLayout forgalomLayout = new VLayout();
		forgalomLayout.setStyleName("middle");
		forgalomLayout.setAlign(Alignment.CENTER);		
		
		HLayout ctorzsFormLayout = new HLayout();
		ctorzsFormLayout.setHeight("3%");
		ctorzsFormLayout.setAlign(Alignment.CENTER);
		ctorzsFormLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		final DynamicForm ctorzsForm = new DynamicForm();
		ctorzsForm.setNumCols(6);
		ctorzsForm.setColWidths("10%", "15%", "10%", "15%", "10%", "*");

		final SelectItem fotipusSelectItem = new SelectItem();
		fotipusSelectItem.setWidth("160");
		fotipusSelectItem.setAllowEmptyValue(true);
		fotipusSelectItem.setTitle(forgalomLabels.cikk_fotipus());
		fotipusSelectItem.setValueMap(cikkSelectsSer.getFotipus());

		final SelectItem altipusSelectItem = new SelectItem();
		altipusSelectItem.setWidth("160");
		altipusSelectItem.setAllowEmptyValue(true);
		altipusSelectItem.setTitle(forgalomLabels.cikk_altipus());
		altipusSelectItem.setValueMap(new LinkedHashMap<String, String>());

		final TextItem cikkszamItem = new TextItem();
		cikkszamItem.setTitle(forgalomLabels.cikk_cikkszam());
		cikkszamItem.setWidth("100");
		cikkszamItem.setLength(15);

		ctorzsForm
				.setFields(fotipusSelectItem, altipusSelectItem, cikkszamItem);

		final IButton szuresIButton = new IButton(commonLabels.filter());
		// szuresIButton.setDisabled(true);

		final IButton kepekIButton = new IButton(forgalomLabels.cikk_kepek());
		kepekIButton.setDisabled(true);

		ctorzsFormLayout.addMember(ctorzsForm);
		ctorzsFormLayout.addMember(szuresIButton);
		ctorzsFormLayout.addMember(kepekIButton);

		HLayout ctorzsGridLayout = new HLayout();
		ctorzsGridLayout.setAlign(Alignment.CENTER);
		ctorzsGridLayout.setHeight("75%");

		final CikktorzsDataSource ctorzsDataSource = new CikktorzsDataSource(
				cikkSelectsSer.getFotipus(), cikkSelectsSer.getAltipus(),
				cikkSelectsSer.getGyarto()) {

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

		final ListGrid ctorzsGrid = new ListGrid();
		ctorzsGrid.setTitle(forgalomLabels.ctorzs());
		ctorzsGrid.setWidth("100%");
		ctorzsGrid.setShowHeaderContextMenu(false);
		ctorzsGrid.setShowHeaderMenuButton(false);
		ctorzsGrid.setCanSort(false);
		ctorzsGrid.setShowAllRecords(true);
		ctorzsGrid.setDataSource(ctorzsDataSource);
		ctorzsGrid.setCanExpandRecords(true);
		ctorzsGrid.setExpansionMode(ExpansionMode.DETAILS);

		/*
		 * Criteria criteria = new Criteria();
		 * criteria.setAttribute(CtorzsConstants.CTORZS_PAGE, page);
		 * criteria.setAttribute(CtorzsConstants.CIKK_FOTIPUS,
		 * fotipusSelectItem.getValueAsString());
		 * criteria.setAttribute(CtorzsConstants.CIKK_ALTIPUS,
		 * altipusSelectItem.getValueAsString());
		 * criteria.setAttribute(CtorzsConstants.CIKK_CIKKSZAM,
		 * cikkszamItem.getValueAsString()); ctorzsGrid.fetchData(criteria);
		 */
		ListGridField cikkszamGridField = new ListGridField(
				ForgalomConstants.CIKK_CIKKSZAM);
		cikkszamGridField.setWidth("20%");

		ListGridField szinkodGridField = new ListGridField(
				ForgalomConstants.CIKK_SZINKOD);
		szinkodGridField.setWidth("10%");

		ListGridField megnevezesGridField = new ListGridField(
				ForgalomConstants.CIKK_MEGNEVEZES);

		ListGridField akciosGridField = new ListGridField(
				ForgalomConstants.CIKK_AKCIOS);
		akciosGridField.setWidth("10%");

		ListGridField kepekGridField = new ListGridField(
				ForgalomConstants.CIKK_KEPEK);
		kepekGridField.setWidth("10%");

		ctorzsGrid.setFields(cikkszamGridField, szinkodGridField,
				megnevezesGridField, akciosGridField, kepekGridField);

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
		nextIButton.setDisabled(true);
		
		prevnextLayout.addMember(previousIButton);
		prevnextLayout.addMember(pageLabel);
		prevnextLayout.addMember(nextIButton);

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("100%");

		HLayout eladasButtonLayout = new HLayout();
		eladasButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton eladasButton = new IButton(forgalomLabels.eladas());
		eladasButtonLayout.setAlign(Alignment.CENTER);
		eladasButtonLayout.addMember(eladasButton);

		buttonsLayout.addMember(eladasButtonLayout);

		ctorzsLayout.addMember(ctorzsFormLayout);
		ctorzsLayout.addMember(ctorzsGridLayout);
		ctorzsLayout.addMember(prevnextLayout);
		ctorzsLayout.addMember(buttonsLayout);

		szuresIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ctorzsGrid.invalidateCache();
				Criteria criteria = new Criteria();
				page = 0;
				criteria.setAttribute(ForgalomConstants.CTORZS_PAGE, page);
				criteria.setAttribute(ForgalomConstants.CIKK_FOTIPUS,
						fotipusSelectItem.getValueAsString());
				criteria.setAttribute(ForgalomConstants.CIKK_ALTIPUS,
						altipusSelectItem.getValueAsString());
				criteria.setAttribute(ForgalomConstants.CIKK_CIKKSZAM,
						cikkszamItem.getValueAsString());
				ctorzsGrid.fetchData(criteria);
				szuresIButton.setDisabled(true);
				previousIButton.setDisabled(true);
				nextIButton.setDisabled(true);
				kepekIButton.setDisabled(true);

				eladasButton.setDisabled(true);

				pageLabel.setContents("");
			}
		});

		previousIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ctorzsGrid.invalidateCache();
				Criteria criteria = new Criteria();
				page = page - 1;
				criteria.setAttribute(ForgalomConstants.CTORZS_PAGE, page);
				criteria.setAttribute(ForgalomConstants.CIKK_FOTIPUS,
						fotipusSelectItem.getValueAsString());
				criteria.setAttribute(ForgalomConstants.CIKK_ALTIPUS,
						altipusSelectItem.getValueAsString());
				criteria.setAttribute(ForgalomConstants.CIKK_CIKKSZAM,
						cikkszamItem.getValueAsString());
				ctorzsGrid.fetchData(criteria);
				szuresIButton.setDisabled(true);
				previousIButton.setDisabled(true);
				nextIButton.setDisabled(true);
				kepekIButton.setDisabled(true);

				eladasButton.setDisabled(true);
				
				pageLabel.setContents("");
			}
		});

		nextIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ctorzsGrid.invalidateCache();
				Criteria criteria = new Criteria();
				page = page + 1;
				criteria.setAttribute(ForgalomConstants.CTORZS_PAGE, page);
				criteria.setAttribute(ForgalomConstants.CIKK_FOTIPUS,
						fotipusSelectItem.getValueAsString());
				criteria.setAttribute(ForgalomConstants.CIKK_ALTIPUS,
						altipusSelectItem.getValueAsString());
				criteria.setAttribute(ForgalomConstants.CIKK_CIKKSZAM,
						cikkszamItem.getValueAsString());
				ctorzsGrid.fetchData(criteria);
				szuresIButton.setDisabled(true);
				previousIButton.setDisabled(true);
				nextIButton.setDisabled(true);
				kepekIButton.setDisabled(true);

				eladasButton.setDisabled(true);
				
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
				eladasButton.setDisabled(false);

				if (ctorzsGrid.getSelectedRecord().getAttributeAsInt(
						ForgalomConstants.CIKK_KEPEK) > 0)
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
				winModal.setTitle(ctorzsGrid.getSelectedRecord().getAttribute(
						ForgalomConstants.CIKK_CIKKSZAM)
						+ " - "
						+ ctorzsGrid.getSelectedRecord().getAttribute(
								ForgalomConstants.CIKK_SZINKOD)
						+ " - "
						+ ctorzsGrid.getSelectedRecord().getAttribute(
								ForgalomConstants.CIKK_MEGNEVEZES));
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
						dsRequest.setAttribute(
								ForgalomConstants.CIKK_CIKKSZAM,
								ctorzsGrid.getSelectedRecord().getAttribute(
										ForgalomConstants.CIKK_CIKKSZAM));
						dsRequest.setAttribute(
								ForgalomConstants.CIKK_SZINKOD,
								ctorzsGrid.getSelectedRecord().getAttribute(
										ForgalomConstants.CIKK_SZINKOD));
						dsRequest.setAttribute(ForgalomConstants.KEP_SORSZAM,
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
										+ event.getResponse()
												.getAttribute(
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

				DetailViewerField pictureField = new DetailViewerField(
						ForgalomConstants.KEP_KEP);
				pictureField.setImageWidth(650);
				pictureField.setImageHeight(450);

				tileGrid.setFields(pictureField);

				HLayout tiledeleteButtonLayout = new HLayout();
				tiledeleteButtonLayout
						.setDefaultLayoutAlign(VerticalAlignment.CENTER);
				tiledeleteButtonLayout.setAlign(Alignment.CENTER);
				final IButton tiledeleteButton = new IButton(commonLabels
						.delete());
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
									sorszam = tileGrid
											.getSelectedRecord()
											.getAttribute(
													ForgalomConstants.KEP_SORSZAM);
									tileGrid.removeSelectedData();
									tiledeleteButton.setDisabled(true);
									int kepek = ctorzsGrid
											.getSelectedRecord()
											.getAttributeAsInt(
													ForgalomConstants.CIKK_KEPEK);
									if (kepek > 0)
										kepek--;
									ctorzsGrid
											.getSelectedRecord()
											.setAttribute(
													ForgalomConstants.CIKK_KEPEK,
													kepek);
									ctorzsGrid.markForRedraw();
								}
							}
						});
					}
				});

				winModal.show();
			}
		});
			
		eladasButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				forgalomLayout.removeMembers(forgalomLayout.getMembers());
				forgalomLayout.addMember(eladas(ctorzsGrid.getSelectedRecord().getAttribute(ForgalomConstants.CIKK_CIKKSZAM),ctorzsGrid.getSelectedRecord().getAttribute(ForgalomConstants.CIKK_SZINKOD)));				
			}
		});
		
		middleLayout.addMember(ctorzsLayout);
		middleLayout.addMember(forgalomLayout);
		
		return middleLayout;

	}
	
	Canvas eladas (String cikkszam, String szinkod) {
	
		final VLayout eladasLayout = new VLayout();
		eladasLayout.setStyleName("middle");
		eladasLayout.setDefaultLayoutAlign(Alignment.CENTER);
		eladasLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		HLayout titleLayout = new HLayout();
		titleLayout.setDefaultLayoutAlign(Alignment.CENTER);
		titleLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		titleLayout.setStyleName("middle");
		titleLayout.setHeight("3%");

		Label titleLabel = new Label();
		titleLabel.setContents(forgalomLabels.eladas() + " : " + cikkszam + " - " + szinkod);
		titleLabel.setAlign(Alignment.CENTER);
		titleLabel.setWidth("80%");
		titleLayout.addMember(titleLabel);
		
		final EladasDataSource eladasDataSource = new EladasDataSource() {

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

		eladasDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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

		final ListGrid eladasGrid = new ListGrid();
		eladasGrid.setTitle(forgalomLabels.eladas());
		eladasGrid.setWidth("80%");
		eladasGrid.setShowHeaderContextMenu(false);
		eladasGrid.setShowHeaderMenuButton(false);
		eladasGrid.setCanSort(false);
		eladasGrid.setShowAllRecords(true);
		eladasGrid.setDataSource(eladasDataSource);
		Criteria criteria = new Criteria();
		criteria.setAttribute(ForgalomConstants.CIKK_CIKKSZAM, cikkszam);
		criteria.setAttribute(ForgalomConstants.CIKK_SZINKOD, szinkod);
		eladasGrid.fetchData(criteria);
		
		eladasLayout.addMember(titleLayout);
		eladasLayout.addMember(eladasGrid);
		
		return eladasLayout;
	}
	
}
