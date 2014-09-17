package hu.dekortrade.client.rendeles.megrendeles;

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
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Megrendeles {

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);
	
	private MegrendelesLabels megrendelesLabels = GWT
			.create(MegrendelesLabels.class);

	public Canvas get() {
		final VLayout middleLayout = new VLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");
		
		final HLayout selectLayout = new HLayout();
		selectLayout.setAlign(Alignment.CENTER);
		selectLayout.setHeight("3%");
		selectLayout.setWidth("100%");
		
		final DynamicForm form = new DynamicForm();
		form.setWidth("60%");
		form.setNumCols(2);
		form.setColWidths("50%", "*");

		final SelectItem rendelesstatus = new SelectItem();  
		rendelesstatus.setTitle(megrendelesLabels.rendelesstatus());
		rendelesstatus.setValueMap(ClientConstants.getRendelesstatus()); 
		rendelesstatus.setAllowEmptyValue(false);
		rendelesstatus.setValue(Constants.ELORENDELT_VEGLEGESITETT);  
		rendelesstatus.setWidth("200");
		form.setFields(rendelesstatus);
		
		HLayout selectokButtonLayout = new HLayout();
		selectokButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		final IButton selectokIButton = new IButton(commonLabels.filter());
		selectokButtonLayout.setAlign(Alignment.LEFT);
		selectokButtonLayout.addMember(selectokIButton);
		
		selectLayout.addMember(form);
		selectLayout.addMember(selectokButtonLayout);
			
		VLayout rendelesSzamoltLayout = new VLayout();
		rendelesSzamoltLayout.setDefaultLayoutAlign(Alignment.CENTER);
		rendelesSzamoltLayout.setWidth("100%");
		
		final RendeltCikkSzamoltDataSource rendeltCikkSzamoltDataSource = new RendeltCikkSzamoltDataSource() {

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

		rendeltCikkSzamoltDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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
	
		final RendeltCikkDataSource rendeltCikkDataSource = new RendeltCikkDataSource() {

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

		rendeltCikkDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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

		final ListGrid rendelesSzamoltGrid  = new ListGrid() {    
	    
            @Override    
            protected Canvas getExpansionComponent(final ListGridRecord record) {    
       
                VLayout rendeleslayout = new VLayout();
                  
                final ListGrid rendelesGrid = new ListGrid();    
                rendelesGrid.setHeight(224);    
                rendelesGrid.setCellHeight(22);    
                rendelesGrid.setDataSource(rendeltCikkDataSource); 
    
          		ListGridField nevGridField = new ListGridField(
        				MegrendelesConstants.MEGRENDELES_NEV);

          		ListGridField rendelesGridField = new ListGridField(
        				MegrendelesConstants.MEGRENDELES_RENDELES);
          		rendelesGridField.setWidth("10%");
          		
        		ListGridField cikkszamGridField = new ListGridField(
        				MegrendelesConstants.MEGRENDELES_CIKKSZAM);
        		cikkszamGridField.setWidth("30%");
        		
        		ListGridField szinkodGridField = new ListGridField(
        				MegrendelesConstants.MEGRENDELES_SZINKOD);
        		szinkodGridField.setWidth("10%");

        		ListGridField exportkartonGridField = new ListGridField(
        				MegrendelesConstants.MEGRENDELES_EXPORTKARTON);
        		exportkartonGridField.setWidth("10%");
        		
        		ListGridField kiskartonGridField = new ListGridField(
        				MegrendelesConstants.MEGRENDELES_KISKARTON);
        		kiskartonGridField.setWidth("10%");

        		ListGridField darabGridField = new ListGridField(
        				MegrendelesConstants.MEGRENDELES_DARAB);
        		darabGridField.setWidth("10%");

        		rendelesGrid.setFields(nevGridField,rendelesGridField,cikkszamGridField, szinkodGridField, exportkartonGridField, kiskartonGridField, darabGridField);

                Criteria criteria = new Criteria();  
                criteria.setAttribute(MegrendelesConstants.MEGRENDELES_STATUS, rendelesstatus.getValueAsString());
                criteria.setAttribute(MegrendelesConstants.MEGRENDELES_CIKKSZAM, record.getAttribute(MegrendelesConstants.MEGRENDELES_CIKKSZAM));
                criteria.setAttribute(MegrendelesConstants.MEGRENDELES_SZINKOD, record.getAttribute(MegrendelesConstants.MEGRENDELES_SZINKOD));               
                rendelesGrid.fetchData(criteria);
                
                rendeleslayout.addMember(rendelesGrid);    
    	                                                   
                return rendeleslayout;    
            }    
	    };    		
		
	    rendelesSzamoltGrid.setTitle(megrendelesLabels.megrendeles());
	    rendelesSzamoltGrid.setWidth("70%");
	    rendelesSzamoltGrid.setShowHeaderContextMenu(false);
	    rendelesSzamoltGrid.setShowHeaderMenuButton(false);
	    rendelesSzamoltGrid.setCanSort(false);
	    rendelesSzamoltGrid.setShowAllRecords(true);
	    rendelesSzamoltGrid.setDataSource(rendeltCikkSzamoltDataSource);
		rendelesSzamoltGrid.setAutoFetchData(false);
		Criteria criteria = new Criteria();
		criteria.setAttribute(MegrendelesConstants.MEGRENDELES_STATUS, rendelesstatus.getValue());
		rendelesSzamoltGrid.fetchData(criteria);
		rendelesSzamoltGrid.setCanExpandRecords(true); 
		
		ListGridField cikkszamGridField = new ListGridField(
				MegrendelesConstants.MEGRENDELES_CIKKSZAM);

		ListGridField szinkodGridField = new ListGridField(
				MegrendelesConstants.MEGRENDELES_SZINKOD);
		szinkodGridField.setWidth("10%");

		ListGridField exportkartonGridField = new ListGridField(
				MegrendelesConstants.MEGRENDELES_EXPORTKARTON);
		exportkartonGridField.setWidth("10%");
		
		ListGridField kiskartonGridField = new ListGridField(
				MegrendelesConstants.MEGRENDELES_KISKARTON);
		kiskartonGridField.setWidth("10%");

		ListGridField darabGridField = new ListGridField(
				MegrendelesConstants.MEGRENDELES_DARAB);
		darabGridField.setWidth("10%");

		rendelesSzamoltGrid.setFields(cikkszamGridField, szinkodGridField, exportkartonGridField, kiskartonGridField, darabGridField);

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("80%");

		HLayout rendelesButtonLayout = new HLayout();
		final IButton rendelesIButton = new IButton(megrendelesLabels.megrendeles());
		rendelesIButton.setDisabled(true);
		rendelesButtonLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		rendelesButtonLayout.setAlign(Alignment.CENTER);
		rendelesButtonLayout.addMember(rendelesIButton);

		buttonsLayout.addMember(rendelesButtonLayout);
		
		rendelesSzamoltLayout.addMember(rendelesSzamoltGrid);
		rendelesSzamoltLayout.addMember(buttonsLayout);

		middleLayout.addMember(selectLayout);
		middleLayout.addMember(rendelesSzamoltLayout);

		selectokIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				rendelesSzamoltGrid.invalidateCache();
				Criteria criteria = new Criteria();
				criteria.setAttribute(MegrendelesConstants.MEGRENDELES_STATUS, rendelesstatus.getValue());
				rendelesSzamoltGrid.fetchData(criteria);
				rendelesIButton.setDisabled(true);
			}
		});
		
		rendelesSzamoltGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				if (rendelesstatus.getValueAsString().equals(Constants.ELORENDELT_VEGLEGESITETT)) {
					rendelesIButton.setDisabled(false);
				}
				else {
					rendelesIButton.setDisabled(true);
				}
			}
		});

		rendelesIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask(commonLabels.sure(), new BooleanCallback() {
					public void execute(Boolean value) {
						if (value != null && value) {
							rendelesSzamoltGrid.removeSelectedData();
							rendelesIButton.setDisabled(true);
						}
					}
				});

			}
		});
		
		return middleLayout;
	}

}
