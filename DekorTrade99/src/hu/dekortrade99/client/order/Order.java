package hu.dekortrade99.client.order;

import hu.dekortrade99.client.ClientConstants;
import hu.dekortrade99.client.CommonLabels;
import hu.dekortrade99.client.DekorTrade99Service;
import hu.dekortrade99.client.DekorTrade99ServiceAsync;
import hu.dekortrade99.client.DisplayRequest;
import hu.dekortrade99.client.UserInfo;
import hu.dekortrade99.shared.serialized.SQLExceptionSer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSCallback;
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
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
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
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

public class Order {

	private final DekorTrade99ServiceAsync dekorTrade99Service = GWT
			.create(DekorTrade99Service.class);

	private OrderLabels orderLabels = GWT.create(OrderLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	private int page = 0;

	private String cikkszam = "";

	private VLayout ctorzsLayout = new VLayout();
	
	public Canvas get() {
		DisplayRequest.counterInit();

		HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");

//		final VLayout ctorzsLayout = new VLayout();
		ctorzsLayout.setStyleName("middle");
		ctorzsLayout.setWidth("80%");
			
		getFotipus();
		
		VLayout kosarLayout = new VLayout();
		kosarLayout.setWidth("20%");
		kosarLayout.setDefaultLayoutAlign(Alignment.CENTER);

		final KosarDataSource kosarDataSource = new KosarDataSource() {

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

		kosarDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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
		kosarGrid.setTitle(orderLabels.order());
		kosarGrid.setWidth("90%");
		kosarGrid.setShowHeaderContextMenu(false);
		kosarGrid.setShowHeaderMenuButton(false);
		kosarGrid.setCanSort(false);
		kosarGrid.setShowAllRecords(true);
		kosarGrid.setDataSource(kosarDataSource);
		kosarGrid.setAutoFetchData(true);

		ListGridField cikkszamGridField = new ListGridField(
				OrderConstants.KOSAR_CIKKSZAM);

		ListGridField exportkartonGridField = new ListGridField(
				OrderConstants.KOSAR_EXPORTKARTON);
		exportkartonGridField.setWidth("30%");

		kosarGrid.setFields(cikkszamGridField, exportkartonGridField);

		HLayout buttons1Layout = new HLayout();
		buttons1Layout.setAlign(Alignment.CENTER);
		buttons1Layout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		buttons1Layout.setHeight("3%");
		buttons1Layout.setWidth("100%");

		IButton kosarAddIButton = new IButton(orderLabels.rendeles());
		kosarAddIButton.setDisabled(true);

		final Label emptyLabel = new Label();
		emptyLabel.setContents("");
		emptyLabel.setAlign(Alignment.CENTER);
		emptyLabel.setWidth("50px");

		final IButton kosarRemoveIButton = new IButton(orderLabels.torles());
		kosarRemoveIButton.setDisabled(true);

		buttons1Layout.addMember(kosarAddIButton);
		buttons1Layout.addMember(emptyLabel);
		buttons1Layout.addMember(kosarRemoveIButton);

		HLayout buttons2Layout = new HLayout();
		buttons2Layout.setAlign(Alignment.CENTER);
		buttons2Layout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		buttons2Layout.setHeight("3%");
		buttons2Layout.setWidth("100%");

		final IButton kosarCommitIButton = new IButton(
				orderLabels.veglegesites());
		kosarCommitIButton.setWidth("250px");
		kosarCommitIButton.setDisabled(true);

		buttons2Layout.addMember(kosarCommitIButton);

		kosarLayout.addMember(buttons1Layout);
		kosarLayout.addMember(kosarGrid);
		kosarLayout.addMember(buttons2Layout);

		middleLayout.addMember(ctorzsLayout);
		middleLayout.addMember(kosarLayout);

		kosarAddIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				final Window winModal = new Window();
				winModal.setWidth(400);
				winModal.setHeight(110);
				winModal.setTitle(orderLabels.rendeles());
				winModal.setShowMinimizeButton(false);
				winModal.setShowCloseButton(false);
				winModal.setIsModal(true);
				winModal.setShowModalMask(true);
				winModal.centerInPage();

				final DynamicForm kosarEditForm = new DynamicForm();
				kosarEditForm.setNumCols(2);
				kosarEditForm.setColWidths("20%", "35%");
				kosarEditForm.setDataSource(kosarDataSource);
				kosarEditForm.setUseAllDataSourceFields(true);

				int found = 0;
				for (int i = 0; i < kosarGrid.getRecords().length; i++) {
					if (kosarGrid.getRecord(i)
							.getAttribute(OrderConstants.KOSAR_CIKKSZAM)
							.equals(getCikkszam())) {
						found = i;
						i = kosarGrid.getRecords().length;
					}
				}

				if (found > 0)
					kosarEditForm.editRecord(kosarGrid.getRecord(found));
				else
					kosarEditForm.editNewRecord();

				kosarEditForm.getField(OrderConstants.KOSAR_CIKKSZAM).setValue(
						getCikkszam());
				kosarEditForm.getField(OrderConstants.KOSAR_CIKKSZAM)
						.setCanEdit(false);
				kosarEditForm.getField(OrderConstants.KOSAR_EXPORTKARTON)
						.setValidateOnChange(true);
				kosarEditForm.getField(OrderConstants.KOSAR_EXPORTKARTON)
						.setValidators(new IsIntegerValidator());

				HLayout buttonsLayout = new HLayout();
				buttonsLayout.setWidth100();

				HLayout saveLayout = new HLayout();
				IButton saveIButton = new IButton(commonLabels.ok());

				saveLayout.addMember(saveIButton);
				buttonsLayout.addMember(saveLayout);
				// buttonsLayout.setHeight("5%");

				HLayout cancelLayout = new HLayout();
				cancelLayout.setAlign(Alignment.RIGHT);
				final IButton cancelIButton = new IButton(commonLabels.cancel());
				cancelLayout.addMember(cancelIButton);
				buttonsLayout.addMember(cancelLayout);

				saveIButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						cancelIButton.setDisabled(true);
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

			}
		});

		kosarRemoveIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask(commonLabels.sure(), new BooleanCallback() {
					public void execute(Boolean value) {
						if (value != null && value) {
							kosarRemoveIButton.setDisabled(true);
							kosarGrid.removeSelectedData();
						}
					}
				});
			}
		});

		kosarGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				kosarRemoveIButton.setDisabled(false);
			}
		});

		kosarGrid.addDataArrivedHandler(new DataArrivedHandler() {
			public void onDataArrived(DataArrivedEvent event) {
				if (kosarGrid.getRecordList().getLength() > 0)
					kosarCommitIButton.setDisabled(false);
				else
					kosarCommitIButton.setDisabled(true);
			}
		});

		kosarCommitIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask(commonLabels.sure(), new BooleanCallback() {
					public void execute(Boolean value) {
						if (value != null && value) {
							kosarRemoveIButton.setDisabled(true);
							kosarCommitIButton.setDisabled(true);
							DisplayRequest.startRequest();
							dekorTrade99Service.commitKosar(UserInfo.userId,
									new AsyncCallback<String>() {
										public void onFailure(Throwable caught) {
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
											kosarGrid
													.setData(new ListGridRecord[] {});
											SC.say(result
													+ " "
													+ orderLabels
															.veglegesitve());
										}
									});

						}
					}
				});
			}
		});

		return middleLayout;
	}
	
	public void getFotipus() {
		
		DisplayRequest.counterInit();
		
		final CikkfotipusDataSource cikkfotipusDataSource = new CikkfotipusDataSource() {

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

		cikkfotipusDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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
	    tileGrid.setTileWidth(250);  
	    tileGrid.setTileHeight(250);  
//	    tileGrid.setHeight(400);  
	    tileGrid.setCanReorderTiles(true);  
	    tileGrid.setShowAllRecords(true);  
	    tileGrid.setDataSource(cikkfotipusDataSource);  
	    tileGrid.setAutoFetchData(true);  
	    tileGrid.setAnimateTileChange(true);  
	  	  				
		DetailViewerField pictureField = new DetailViewerField(OrderConstants.CIKKFOTIPUS_KEP);  
	    pictureField.setImageWidth(250);
	    pictureField.setImageHeight(200);
		
		DetailViewerField nameField = new DetailViewerField(OrderConstants.CIKKFOTIPUS_NEV);  			
		
		tileGrid.setFields(pictureField,nameField);

		ctorzsLayout.addMember(tileGrid);
		
		tileGrid.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ctorzsLayout.removeMembers(ctorzsLayout.getMembers());
				getAltipus(tileGrid.getSelectedRecord().getAttribute(OrderConstants.CIKKFOTIPUS_KOD));
			}		
		});		
	}
	
	public void getAltipus(final String fotipus) {

		DisplayRequest.counterInit();
			
		final CikkaltipusDataSource cikkaltipusDataSource = new CikkaltipusDataSource() {

			protected Object transformRequest(DSRequest dsRequest) {
				dsRequest.setAttribute(OrderConstants.CIKKFOTIPUS_KOD,
						fotipus);
				DisplayRequest.startRequest();
				return super.transformRequest(dsRequest);
			}

			protected void transformResponse(DSResponse response,
					DSRequest request, Object data) {
				DisplayRequest.serverResponse();
				super.transformResponse(response, request, data);
			}
		};

		cikkaltipusDataSource.addHandleErrorHandler(new HandleErrorHandler() {
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
	    tileGrid.setTileWidth(250);  
	    tileGrid.setTileHeight(250);  
//	    tileGrid.setHeight(400);  
	    tileGrid.setCanReorderTiles(true);  
	    tileGrid.setShowAllRecords(true);  
	    tileGrid.setDataSource(cikkaltipusDataSource);  
	    tileGrid.setAutoFetchData(true);  
	    tileGrid.setAnimateTileChange(true);  
	  	  				
		DetailViewerField pictureField = new DetailViewerField(OrderConstants.CIKKALTIPUS_KEP);  
	    pictureField.setImageWidth(250);
	    pictureField.setImageHeight(200);
		
		DetailViewerField nameField = new DetailViewerField(OrderConstants.CIKKALTIPUS_NEV);  			
		
		tileGrid.setFields(pictureField,nameField);

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("100%");

		final IButton backIButton = new IButton(commonLabels.back());
		buttonsLayout.addMember(backIButton);
		
		ctorzsLayout.addMember(tileGrid);
		ctorzsLayout.addMember(buttonsLayout);
		
		tileGrid.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ctorzsLayout.removeMembers(ctorzsLayout.getMembers());
				getCikk(tileGrid.getSelectedRecord().getAttribute(OrderConstants.CIKKALTIPUS_FOKOD),tileGrid.getSelectedRecord().getAttribute(OrderConstants.CIKKALTIPUS_KOD));
			}
			
		});
		
		backIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ctorzsLayout.removeMembers(ctorzsLayout.getMembers());		
				getFotipus();	
			}
		});
		
	}

	
	public void getCikk(final String fotipus, final String altipus) {

		DisplayRequest.counterInit();

		HLayout buttonsLayout = new HLayout();
		buttonsLayout.setAlign(Alignment.CENTER);
		buttonsLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		buttonsLayout.setHeight("3%");
		buttonsLayout.setWidth("100%");

		final IButton backIButton = new IButton(commonLabels.back());
		buttonsLayout.addMember(backIButton);

		ctorzsLayout.addMember(buttonsLayout);
		
		backIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ctorzsLayout.removeMembers(ctorzsLayout.getMembers());		
				getAltipus(fotipus);	
			}
		});

	}
	
	public String getCikkszam() {
		return cikkszam;
	}

}
