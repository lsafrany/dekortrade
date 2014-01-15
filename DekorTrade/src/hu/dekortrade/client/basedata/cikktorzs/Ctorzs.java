package hu.dekortrade.client.basedata.cikktorzs;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.shared.Constants;

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

public class Ctorzs {

	private CtorzsLabels ctorzsLabels = GWT.create(CtorzsLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	private int page = 0;

	private String cikkszam = "";
	
	public Canvas get(final IButton addIButton) {

		DisplayRequest.counterInit();
		
		HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");
	
		VLayout ctorzsLayout = new VLayout();
		ctorzsLayout.setStyleName("middle");
		ctorzsLayout.setWidth("1200px");
		
		HLayout ctorzsFormLayout = new HLayout();
		ctorzsFormLayout.setHeight("3%");
		ctorzsFormLayout.setAlign(Alignment.CENTER);
		ctorzsFormLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		final DynamicForm ctorzsForm = new DynamicForm();
		ctorzsForm.setNumCols(4);
		ctorzsForm.setColWidths("10%", "25%", "10%", "*");

		final TextItem cikkszamItem = new TextItem();	
		cikkszamItem.setTitle(ctorzsLabels.ctorzs_cikkszam());
		cikkszamItem.setLength(15);

		final SelectItem jelSelectItem = new SelectItem();
		jelSelectItem.setWidth("250");
		jelSelectItem.setTitle(ctorzsLabels.ctorzs_jel());
		jelSelectItem.setValueMap(ClientConstants.getJelek());

		ctorzsForm.setFields(cikkszamItem, jelSelectItem);

		final IButton szuresIButton = new IButton(commonLabels.filter());
		szuresIButton.setDisabled(true);

		final IButton kepekIButton = new IButton(ctorzsLabels.ctorzs_kepek());
		kepekIButton.setDisabled(true);

		ctorzsFormLayout.addMember(ctorzsForm);
		ctorzsFormLayout.addMember(szuresIButton);
		ctorzsFormLayout.addMember(kepekIButton);

		HLayout ctorzsGridLayout = new HLayout();
		ctorzsGridLayout.setAlign(Alignment.CENTER);
		ctorzsGridLayout.setHeight("75%");

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
		criteria.setAttribute(CtorzsConstants.CTORZS_CIKKSZAM,
				cikkszamItem.getValueAsString());
		criteria.setAttribute(CtorzsConstants.CTORZS_JEL,
				jelSelectItem.getValueAsString());
		ctorzsGrid.fetchData(criteria);

		ListGridField cikkszamGridField = new ListGridField(
				CtorzsConstants.CTORZS_CIKKSZAM);
		cikkszamGridField.setWidth("15%");

		ListGridField megnevezesGridField = new ListGridField(
				CtorzsConstants.CTORZS_MEGNEVEZES);

		ListGridField arGridField = new ListGridField(CtorzsConstants.CTORZS_AR);
		arGridField.setWidth("8%");

		ListGridField kiskartonGridField = new ListGridField(
				CtorzsConstants.CTORZS_KISKARTON);
		kiskartonGridField.setWidth("8%");

		ListGridField darabGridField = new ListGridField(
				CtorzsConstants.CTORZS_DARAB);
		darabGridField.setWidth("8%");

		ListGridField terfogatGridField = new ListGridField(
				CtorzsConstants.CTORZS_TERFOGAT);
		terfogatGridField.setWidth("8%");

		ListGridField jelGridField = new ListGridField(
				CtorzsConstants.CTORZS_JEL);
		jelGridField.setWidth("18%");

		ListGridField bsulyGridField = new ListGridField(
				CtorzsConstants.CTORZS_BSULY);
		bsulyGridField.setWidth("8%");

		ListGridField nsulyGridField = new ListGridField(
				CtorzsConstants.CTORZS_NSULY);
		nsulyGridField.setWidth("8%");

		ctorzsGrid
				.setFields(cikkszamGridField, megnevezesGridField, arGridField,
						kiskartonGridField, darabGridField, terfogatGridField,
						jelGridField, bsulyGridField, nsulyGridField);

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
		pageLabel.setWidth("50px");

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
		modifyButtonLayout
				.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		modifyButtonLayout.setAlign(Alignment.CENTER);
		final IButton modifyButton = new IButton(commonLabels.modify());
		modifyButton.disable();
		modifyButtonLayout.addMember(modifyButton);

		HLayout deleteButtonLayout = new HLayout();
		deleteButtonLayout
				.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		deleteButtonLayout.setAlign(Alignment.CENTER);
		final IButton deleteButton = new IButton(commonLabels.delete());
		deleteButton.disable();
		deleteButtonLayout.addMember(deleteButton);
		
		buttonsLayout.addMember(addButtonLayout);
		buttonsLayout.addMember(modifyButtonLayout);
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
				criteria.setAttribute(CtorzsConstants.CTORZS_CIKKSZAM,
						cikkszamItem.getValueAsString());
				criteria.setAttribute(CtorzsConstants.CTORZS_JEL,
						jelSelectItem.getValueAsString());
				ctorzsGrid.fetchData(criteria);
				szuresIButton.setDisabled(true);
				previousIButton.setDisabled(true);
				nextIButton.setDisabled(true);
				kepekIButton.setDisabled(true);
				if (addIButton != null) addIButton.setDisabled(true);  
				pageLabel.setContents("");
			}
		});

		previousIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ctorzsGrid.invalidateCache();
				Criteria criteria = new Criteria();
				page = page - 1;
				criteria.setAttribute(CtorzsConstants.CTORZS_PAGE, page);
				criteria.setAttribute(CtorzsConstants.CTORZS_CIKKSZAM,
						cikkszamItem.getValueAsString());
				criteria.setAttribute(CtorzsConstants.CTORZS_JEL,
						jelSelectItem.getValueAsString());
				ctorzsGrid.fetchData(criteria);
				szuresIButton.setDisabled(true);
				previousIButton.setDisabled(true);
				nextIButton.setDisabled(true);
				kepekIButton.setDisabled(true);
				if (addIButton != null) addIButton.setDisabled(true); 
				pageLabel.setContents("");
			}
		});

		nextIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ctorzsGrid.invalidateCache();
				Criteria criteria = new Criteria();
				page = page + 1;
				criteria.setAttribute(CtorzsConstants.CTORZS_PAGE, page);
				criteria.setAttribute(CtorzsConstants.CTORZS_CIKKSZAM,
						cikkszamItem.getValueAsString());
				criteria.setAttribute(CtorzsConstants.CTORZS_JEL,
						jelSelectItem.getValueAsString());
				ctorzsGrid.fetchData(criteria);
				szuresIButton.setDisabled(true);
				previousIButton.setDisabled(true);
				nextIButton.setDisabled(true);
				kepekIButton.setDisabled(true);
				if (addIButton != null) addIButton.setDisabled(true); 
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
							CtorzsConstants.CTORZS_CIKKSZAM);
					addIButton.setDisabled(false); 
				}
			}
		});

		kepekIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				final Window winModal = new Window();
				winModal.setWidth(800);
				winModal.setHeight(800);
				winModal.setTitle(ctorzsGrid.getSelectedRecord().getAttribute(
						CtorzsConstants.CTORZS_CIKKSZAM)
						+ " - "
						+ ctorzsGrid.getSelectedRecord().getAttribute(
								CtorzsConstants.CTORZS_MEGNEVEZES));
				winModal.setShowMinimizeButton(false);
				winModal.setIsModal(true);
				winModal.setShowModalMask(true);
				winModal.centerInPage();

				winModal.show();

			}
		});

		middleLayout.addMember(ctorzsLayout);
		
		return middleLayout;

	}

	public String getCikkszam() {
		return cikkszam;
	}
	
	
}