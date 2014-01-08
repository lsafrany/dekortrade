package hu.dekortrade99.client.order;

import hu.dekortrade99.client.ClientConstants;
import hu.dekortrade99.client.CommonLabels;
import hu.dekortrade99.client.DisplayRequest;
import hu.dekortrade99.client.UserInfo;
import hu.dekortrade99.client.cikktorzs.Ctorzs;

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
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Order {

	private OrderLabels orderLabels = GWT.create(OrderLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	public Canvas get() {
		DisplayRequest.counterInit();
		
		HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");
		
		VLayout kosarLayout = new VLayout();
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
		kosarGrid.setWidth("50%");
		kosarGrid.setShowHeaderContextMenu(false);
		kosarGrid.setShowHeaderMenuButton(false);
		kosarGrid.setCanSort(false);
		kosarGrid.setShowAllRecords(true);
		kosarGrid.setDataSource(kosarDataSource);
		Criteria criteria = new Criteria();
		criteria.setAttribute(OrderConstants.KOSAR_ROVIDNEV, UserInfo.userId);
		kosarGrid.fetchData(criteria);

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
		
		IButton kosarRemoveIButton = new IButton(orderLabels.torles());
		kosarRemoveIButton.setDisabled(true);

		buttons1Layout.addMember(kosarAddIButton);
		buttons1Layout.addMember(emptyLabel);
		buttons1Layout.addMember(kosarRemoveIButton);

		HLayout buttons2Layout = new HLayout();
		buttons2Layout.setAlign(Alignment.CENTER);
		buttons2Layout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		buttons2Layout.setHeight("3%");
		buttons2Layout.setWidth("100%");

		IButton kosarCommitIButton = new IButton(orderLabels.veglegesites());
		kosarCommitIButton.setWidth("250px");
		kosarCommitIButton.setDisabled(true);

		buttons2Layout.addMember(kosarCommitIButton);
		
		kosarLayout.addMember(buttons1Layout);
		kosarLayout.addMember(kosarGrid);
		kosarLayout.addMember(buttons2Layout);

		final Ctorzs ctorzs = new Ctorzs();
		
		middleLayout.addMember(ctorzs.get(kosarAddIButton));
		middleLayout.addMember(kosarLayout);
				
		kosarAddIButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				final Window winModal = new Window();
				winModal.setWidth(400);
				winModal.setHeight(400);
				winModal.setTitle(ctorzs.getCikkszam());
				winModal.setShowMinimizeButton(false);
				winModal.setIsModal(true);
				winModal.setShowModalMask(true);
				winModal.centerInPage();

				winModal.show();

			}
		});
		
		return middleLayout;

	}
}
