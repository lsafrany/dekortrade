package hu.dekortrade.client.order.finalize;

import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.DisplayRequest;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.client.basedata.vevo.Vevo;
import hu.dekortrade.client.kosar.KosarCikk;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.VevoKosarSer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class FinalizeOrder {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

//	private PreOrderLabels preorderLabels = GWT.create(PreOrderLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

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
		dekorTradeService.getVevoKosar(UserInfo.userId,Constants.MENU_ORDER_FINALIZE,new AsyncCallback<VevoKosarSer>() {
			public void onFailure(Throwable caught) {
				DisplayRequest.serverResponse();
				if (caught instanceof SQLExceptionSer)
					SC.warn(commonLabels.server_sqlerror() + " : "
							+ caught.getMessage());
				else
					SC.warn(commonLabels.server_error());
			}

			public void onSuccess(final VevoKosarSer result) {
				DisplayRequest.serverResponse();
				middleLayout.removeMembers(middleLayout.getMembers());
				middleLayout.addMember(process(result));
			}
		});
		return middleLayout;
		
	}
	
	public Canvas process(VevoKosarSer result)  {

		DisplayRequest.counterInit();

		final HLayout middleLayout = new HLayout();
		middleLayout.setAlign(Alignment.CENTER);
		middleLayout.setStyleName("middle");
		
		if (result == null) {
			Vevo vevo = new Vevo();
			middleLayout.addMember(vevo.get(Constants.MENU_ORDER_FINALIZE));
		}	
		else {
			KosarCikk kosarCikk = new KosarCikk();
			middleLayout.addMember(kosarCikk.get(result.getCedula(),UserInfo.userId,result.getVevo(),result.getVevonev(),result.getVevotipus(),Constants.MENU_ORDER_FINALIZE));	
		}
			
		return middleLayout;

	}
		
}
