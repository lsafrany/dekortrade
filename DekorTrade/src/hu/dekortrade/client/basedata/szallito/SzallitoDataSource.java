package hu.dekortrade.client.basedata.szallito;

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

	private SzallitoLabels szallitoLabels = GWT.create(SzallitoLabels.class);

	public SzallitoDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(SzallitoConstants.SZALLITO_KOD,
				szallitoLabels.szallito_kod());
		field.setPrimaryKey(true);
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(SzallitoConstants.SZALLITO_NEV,
				szallitoLabels.szallito_nev());
		addField(field);
		
		field = new DataSourceTextField(SzallitoConstants.SZALLITO_CIM,
				szallitoLabels.szallito_cim());
		addField(field);

		field = new DataSourceTextField(SzallitoConstants.SZALLITO_ELERHETOSEG,
				szallitoLabels.szallito_elerhetoseg());
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
		to.setAttribute(SzallitoConstants.SZALLITO_KOD, from.getKod());
		to.setAttribute(SzallitoConstants.SZALLITO_NEV, from.getNev());
		to.setAttribute(SzallitoConstants.SZALLITO_CIM, from.getCim());
		to.setAttribute(SzallitoConstants.SZALLITO_ELERHETOSEG, from.getElerhetoseg());		
	}

}
