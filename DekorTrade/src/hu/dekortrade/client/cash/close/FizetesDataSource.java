package hu.dekortrade.client.cash.close;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.FizetesSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceEnumField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class FizetesDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private CashCloseLabels cashCloseLabels = GWT.create(CashCloseLabels.class);

	public FizetesDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(CashCloseConstants.FIZETES_CEDULA,
				cashCloseLabels.cedula());
		addField(field);		
		
		field = new DataSourceTextField(CashCloseConstants.FIZETES_VEVO,
				cashCloseLabels.vevo());
		addField(field);

		field = new DataSourceTextField(CashCloseConstants.FIZETES_VEVONEV,
				cashCloseLabels.vevonev());
		addField(field);

		field = new DataSourceEnumField(CashCloseConstants.FIZETES_TIPUS,
				cashCloseLabels.tipus());
		field.setValueMap(CashCloseConstants.getFizetesTipus());
		addField(field);
		
		field = new DataSourceTextField(CashCloseConstants.FIZETES_PENZTAROS,
				cashCloseLabels.penztaros());
		addField(field);

		field = new DataSourceTextField(CashCloseConstants.FIZETES_PENZTAROSNEV,
				cashCloseLabels.penztarosnev());
		addField(field);
		
		field = new DataSourceFloatField(CashCloseConstants.FIZETES_FIZET,
				cashCloseLabels.fizet());
		addField(field);

		field = new DataSourceFloatField(CashCloseConstants.FIZETES_FIZETEUR,
				cashCloseLabels.fizeteur());
		addField(field);
		
		field = new DataSourceFloatField(CashCloseConstants.FIZETES_FIZETUSD,
				cashCloseLabels.fizetusd());
		addField(field);
	
		field = new DataSourceDateField(CashCloseConstants.FIZETES_DATUM,
				cashCloseLabels.datum());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getFizetes(new AsyncCallback<List<FizetesSer>>() {
			public void onFailure(Throwable caught) {
				if (caught instanceof SQLExceptionSer)
					response.setAttribute(ClientConstants.SERVER_SQLERROR,
							caught.getMessage());
				else
					response.setAttribute(ClientConstants.SERVER_ERROR,
							ClientConstants.SERVER_ERROR);
				response.setStatus(DSResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			public void onSuccess(List<FizetesSer> result) {
				ListGridRecord[] list = new ListGridRecord[result.size()];
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

	private static void copyValues(FizetesSer from, ListGridRecord to) {
		to.setAttribute(CashCloseConstants.FIZETES_CEDULA, from.getCedula());
		to.setAttribute(CashCloseConstants.FIZETES_VEVO, from.getVevo());
		to.setAttribute(CashCloseConstants.FIZETES_VEVONEV, from.getVevonev());
		to.setAttribute(CashCloseConstants.FIZETES_TIPUS, from.getTipus());
		to.setAttribute(CashCloseConstants.FIZETES_PENZTAROS, from.getPenztaros());
		to.setAttribute(CashCloseConstants.FIZETES_PENZTAROSNEV, from.getPenztarosnev());
		to.setAttribute(CashCloseConstants.FIZETES_FIZET, from.getFizet());
		to.setAttribute(CashCloseConstants.FIZETES_FIZETEUR, from.getFizeteur());
		to.setAttribute(CashCloseConstants.FIZETES_FIZETUSD, from.getFizetusd());
		to.setAttribute(CashCloseConstants.FIZETES_DATUM, from.getDatum());
	}

}
