package hu.dekortrade.client.basedata;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.VevoSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class VevoDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private BasedataLabels basedataLabels = GWT.create(BasedataLabels.class);

	public VevoDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(BasedataConstants.VEVO_ROVIDNEV,
				basedataLabels.vevo_rovidnev());
		field.setPrimaryKey(true);
		addField(field);

		field = new DataSourceTextField(BasedataConstants.VEVO_NEV,
				basedataLabels.vevo_nev());
		addField(field);
		
		field = new DataSourceTextField(BasedataConstants.VEVO_CIM,
				basedataLabels.vevo_cim());
		addField(field);

		field = new DataSourceTextField(BasedataConstants.VEVO_ELERHETOSEG,
				basedataLabels.vevo_elerhetoseg());
		addField(field);

		field = new DataSourceBooleanField(BasedataConstants.VEVO_INTERNET,
				basedataLabels.vevo_internet());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getVevo(				
				new AsyncCallback<List<VevoSer>>() {
					public void onFailure(Throwable caught) {
						if (caught instanceof SQLExceptionSer)
							response.setAttribute(
									ClientConstants.SERVER_SQLERROR,
									caught.getMessage());
						else
							response.setAttribute(ClientConstants.SERVER_ERROR,
									ClientConstants.SERVER_ERROR);
						response.setStatus(DSResponse.STATUS_FAILURE);
						processResponse(requestId, response);
					}

					public void onSuccess(List<VevoSer> result) {
						ListGridRecord[] list = new ListGridRecord[result
								.size()];
						for (int i = 0; i < result.size(); i++) {
							ListGridRecord record = new ListGridRecord();
							copyValues(result.get(i), record);
							list[i] = record;
						}
						setLastId(null);
						response.setData(list);
						processResponse(requestId, response);
					}

				});

	}

	@Override
	protected void executeAdd(final String requestId, final DSRequest request,
			final DSResponse response) {
	}

	@Override
	protected void executeUpdate(final String requestId,
			final DSRequest request, final DSResponse response) {
	}

	@Override
	protected void executeRemove(final String requestId,
			final DSRequest request, final DSResponse response) {
	}

	private static void copyValues(VevoSer from, ListGridRecord to) {
		to.setAttribute(BasedataConstants.VEVO_ROVIDNEV, from.getRovidnev());
		to.setAttribute(BasedataConstants.VEVO_NEV, from.getNev());
		to.setAttribute(BasedataConstants.VEVO_CIM, from.getCim());
		to.setAttribute(BasedataConstants.VEVO_ELERHETOSEG, from.getElerhetoseg());		
	}

}
