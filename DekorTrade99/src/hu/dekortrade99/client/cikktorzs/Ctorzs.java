package hu.dekortrade99.client.cikktorzs;

import hu.dekortrade99.client.cikktorzs.CtorzsConstants;
import hu.dekortrade99.client.cikktorzs.KepDataSource;
import hu.dekortrade99.client.ClientConstants;
import hu.dekortrade99.client.CommonLabels;
import hu.dekortrade99.client.DisplayRequest;
import hu.dekortrade99.shared.Constants;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.Criteria;
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

public class Ctorzs {

	private CtorzsLabels ctorzsLabels = GWT.create(CtorzsLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	private int page = 0;

	private String cikkszam = "";

	public Canvas get(final IButton addIButton) {

		DisplayRequest.counterInit();
		VLayout ctorzsLayout = new VLayout();
		ctorzsLayout.setStyleName("middle");
		ctorzsLayout.setWidth("1100px");

		HLayout ctorzsFormLayout = new HLayout();
		ctorzsFormLayout.setHeight("3%");
		ctorzsFormLayout.setAlign(Alignment.CENTER);
		ctorzsFormLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		final DynamicForm ctorzsForm = new DynamicForm();
		ctorzsForm.setNumCols(4);
		ctorzsForm.setColWidths("10%", "25%", "10%", "*");

		final TextItem cikkszamItem = new TextItem();
		cikkszamItem.setTitle(ctorzsLabels.cikk_cikkszam());
		cikkszamItem.setLength(15);

		final SelectItem jelSelectItem = new SelectItem();
		jelSelectItem.setWidth("250");
		jelSelectItem.setTitle(ctorzsLabels.cikk_jel());
		jelSelectItem.setValueMap(ClientConstants.getJelek());

		ctorzsForm.setFields(cikkszamItem, jelSelectItem);

		final IButton szuresIButton = new IButton(commonLabels.filter());
		szuresIButton.setDisabled(true);

		final IButton kepekIButton = new IButton(ctorzsLabels.cikk_kepek());
		kepekIButton.setDisabled(true);

		ctorzsFormLayout.addMember(ctorzsForm);
		ctorzsFormLayout.addMember(szuresIButton);
		ctorzsFormLayout.addMember(kepekIButton);

		HLayout ctorzsGridLayout = new HLayout();
		ctorzsGridLayout.setAlign(Alignment.CENTER);

		final CtorzsDataSource ctorzsDataSource = new CtorzsDataSource() {

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
						SC.warn(commonLabels.server_sqlerror()
								+ " : "
								+ event.getResponse().getAttribute(
										ClientConstants.SERVER_SQLERROR));
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
		Criteria criteria = new Criteria();
		criteria.setAttribute(CtorzsConstants.CTORZS_PAGE, page);
		criteria.setAttribute(CtorzsConstants.CIKK_CIKKSZAM,
				cikkszamItem.getValueAsString());
		criteria.setAttribute(CtorzsConstants.CIKK_JEL,
				jelSelectItem.getValueAsString());
		ctorzsGrid.fetchData(criteria);

		ListGridField cikkszamGridField = new ListGridField(
				CtorzsConstants.CIKK_CIKKSZAM);
		cikkszamGridField.setWidth("15%");

		ListGridField megnevezesGridField = new ListGridField(
				CtorzsConstants.CIKK_MEGNEVEZES);

		ListGridField arGridField = new ListGridField(CtorzsConstants.CIKK_AR);
		arGridField.setWidth("5%");

		ListGridField kiskartonGridField = new ListGridField(
				CtorzsConstants.CIKK_KISKARTON);
		kiskartonGridField.setWidth("8%");

		ListGridField darabGridField = new ListGridField(
				CtorzsConstants.CIKK_DARAB);
		darabGridField.setWidth("5%");

		ListGridField terfogatGridField = new ListGridField(
				CtorzsConstants.CIKK_TERFOGAT);
		terfogatGridField.setWidth("5%");

		ListGridField jelGridField = new ListGridField(
				CtorzsConstants.CIKK_JEL);
		jelGridField.setWidth("18%");

		ListGridField bsulyGridField = new ListGridField(
				CtorzsConstants.CIKK_BSULY);
		bsulyGridField.setWidth("5%");

		ListGridField nsulyGridField = new ListGridField(
				CtorzsConstants.CIKK_NSULY);
		nsulyGridField.setWidth("5%");

		ListGridField kepekGridField = new ListGridField(
				CtorzsConstants.CIKK_KEPEK);
		kepekGridField.setWidth("5%");

		ctorzsGrid
				.setFields(cikkszamGridField, megnevezesGridField, arGridField,
						kiskartonGridField, darabGridField, terfogatGridField,
						jelGridField, bsulyGridField, nsulyGridField, kepekGridField);

		ctorzsGridLayout.addMember(ctorzsGrid);

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("100%");

		final IButton previousIButton = new IButton("&lt;&lt;");
		previousIButton.setDisabled(true);
		final Label pageLabel = new Label();
		pageLabel.setContents("");
		pageLabel.setAlign(Alignment.CENTER);
		pageLabel.setWidth("100px");

		final IButton nextIButton = new IButton("&gt;&gt;");

		buttonsLayout.addMember(previousIButton);
		buttonsLayout.addMember(pageLabel);
		buttonsLayout.addMember(nextIButton);

		ctorzsLayout.addMember(ctorzsFormLayout);
		ctorzsLayout.addMember(ctorzsGridLayout);
		ctorzsLayout.addMember(buttonsLayout);

		szuresIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ctorzsGrid.invalidateCache();
				Criteria criteria = new Criteria();
				page = 0;
				criteria.setAttribute(CtorzsConstants.CTORZS_PAGE, page);
				criteria.setAttribute(CtorzsConstants.CIKK_CIKKSZAM,
						cikkszamItem.getValueAsString());
				criteria.setAttribute(CtorzsConstants.CIKK_JEL,
						jelSelectItem.getValueAsString());
				ctorzsGrid.fetchData(criteria);
				szuresIButton.setDisabled(true);
				previousIButton.setDisabled(true);
				nextIButton.setDisabled(true);
				kepekIButton.setDisabled(true);
				if (addIButton != null)
					addIButton.setDisabled(true);
				pageLabel.setContents("");
			}
		});

		previousIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ctorzsGrid.invalidateCache();
				Criteria criteria = new Criteria();
				page = page - 1;
				criteria.setAttribute(CtorzsConstants.CTORZS_PAGE, page);
				criteria.setAttribute(CtorzsConstants.CIKK_CIKKSZAM,
						cikkszamItem.getValueAsString());
				criteria.setAttribute(CtorzsConstants.CIKK_JEL,
						jelSelectItem.getValueAsString());
				ctorzsGrid.fetchData(criteria);
				szuresIButton.setDisabled(true);
				previousIButton.setDisabled(true);
				nextIButton.setDisabled(true);
				kepekIButton.setDisabled(true);
				if (addIButton != null)
					addIButton.setDisabled(true);
				pageLabel.setContents("");
			}
		});

		nextIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ctorzsGrid.invalidateCache();
				Criteria criteria = new Criteria();
				page = page + 1;
				criteria.setAttribute(CtorzsConstants.CTORZS_PAGE, page);
				criteria.setAttribute(CtorzsConstants.CIKK_CIKKSZAM,
						cikkszamItem.getValueAsString());
				criteria.setAttribute(CtorzsConstants.CIKK_JEL,
						jelSelectItem.getValueAsString());
				ctorzsGrid.fetchData(criteria);
				szuresIButton.setDisabled(true);
				previousIButton.setDisabled(true);
				nextIButton.setDisabled(true);
				kepekIButton.setDisabled(true);
				if (addIButton != null)
					addIButton.setDisabled(true);
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
				kepekIButton.setDisabled(false);
				if (addIButton != null) {
					cikkszam = ctorzsGrid.getSelectedRecord().getAttribute(
							CtorzsConstants.CIKK_CIKKSZAM);
					addIButton.setDisabled(false);
				}

				if (ctorzsGrid.getSelectedRecord().getAttributeAsInt(CtorzsConstants.CIKK_KEPEK) > 0)
					kepekIButton.setDisabled(false);
				else 
					kepekIButton.setDisabled(true);

			}
		});

		kepekIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
								
				Window winModal = new Window();
				winModal.setWidth(800);
				winModal.setHeight(800);
				winModal.setTitle(cikkszam
						+ " - "
						+ ctorzsGrid.getSelectedRecord().getAttribute(
								CtorzsConstants.CIKK_MEGNEVEZES));
				winModal.setShowMinimizeButton(false);
				winModal.setIsModal(true);
				winModal.setShowModalMask(true);
				winModal.centerInPage();
				
				final KepDataSource kepDataSource = new KepDataSource() {

					protected Object transformRequest(DSRequest dsRequest) {
						DisplayRequest.startRequest();
						dsRequest.setAttribute(CtorzsConstants.CIKK_CIKKSZAM,
								cikkszam);
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
		        tileGrid.setID("boundList");  
		        tileGrid.setShowAllRecords(true);  
		        tileGrid.setDataSource(kepDataSource);  
		        tileGrid.setAutoFetchData(true);  
			  				
				DetailViewerField pictureField = new DetailViewerField(CtorzsConstants.KEP_KEP);  			
			    pictureField.setImageWidth(650);
			    pictureField.setImageHeight(450);

				tileGrid.setFields(pictureField);
				
				winModal.addItem(tileGrid);
				
				winModal.show();							
			}
		});

		return ctorzsLayout;

	}

	public String getCikkszam() {
		return cikkszam;
	}

}
