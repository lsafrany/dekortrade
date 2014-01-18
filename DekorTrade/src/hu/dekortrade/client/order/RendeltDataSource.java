package hu.dekortrade.client.order;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.RendeltSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class RendeltDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private OrderLabels orderLabels = GWT.create(OrderLabels.class);

	public RendeltDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(OrderConstants.RENDELT_ROVIDNEV,
				orderLabels.rendelt_rovidnev());
		addField(field);

		field = new DataSourceTextField(OrderConstants.RENDELT_RENDELES,
				orderLabels.rendelt_rendeles());
		addField(field);

		field = new DataSourceTextField(OrderConstants.RENDELT_DATUM,
				orderLabels.rendelt_datum());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService
				.getRendelt(new AsyncCallback<ArrayList<RendeltSer>>() {
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

					public void onSuccess(ArrayList<RendeltSer> result) {
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

	private static void copyValues(RendeltSer from, ListGridRecord to) {
		to.setAttribute(OrderConstants.RENDELT_ROVIDNEV, from.getRovidnev());
		to.setAttribute(OrderConstants.RENDELT_RENDELES, from.getRendeles());
		to.setAttribute(OrderConstants.RENDELT_DATUM, from.getDatum());
	}

}
