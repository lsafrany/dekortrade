package hu.dekortrade.client.basedata;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.SzallitoSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class SzallitoDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private BasedataLabels basedataLabels = GWT.create(BasedataLabels.class);

	public SzallitoDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(BasedataConstants.SZALLITO_KOD,
				basedataLabels.szallito_kod());
		field.setPrimaryKey(true);
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(BasedataConstants.SZALLITO_NEV,
				basedataLabels.szallito_nev());
		addField(field);
		
		field = new DataSourceTextField(BasedataConstants.SZALLITO_CIM,
				basedataLabels.szallito_cim());
		addField(field);

		field = new DataSourceTextField(BasedataConstants.SZALLITO_ELERHETOSEG,
				basedataLabels.szallito_elerhetoseg());
		addField(field);
		
	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getSzallito(				
				new AsyncCallback<List<SzallitoSer>>() {
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

					public void onSuccess(List<SzallitoSer> result) {
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

	private static void copyValues(SzallitoSer from, ListGridRecord to) {
		to.setAttribute(BasedataConstants.SZALLITO_KOD, from.getKod());
		to.setAttribute(BasedataConstants.SZALLITO_NEV, from.getNev());
		to.setAttribute(BasedataConstants.SZALLITO_CIM, from.getCim());
		to.setAttribute(BasedataConstants.SZALLITO_ELERHETOSEG, from.getElerhetoseg());		
	}

}
