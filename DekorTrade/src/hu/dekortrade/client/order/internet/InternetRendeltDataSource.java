package hu.dekortrade.client.order.internet;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.RendeltSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class InternetRendeltDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private InternetOrderLabels internetorderLabels = GWT.create(InternetOrderLabels.class);

	public InternetRendeltDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(InternetOrderConstants.INTERNETRENDELT_ROVIDNEV,
				internetorderLabels.rendelt_rovidnev());
		addField(field);

		field = new DataSourceTextField(InternetOrderConstants.INTERNETRENDELT_RENDELES,
				internetorderLabels.rendelt_rendeles());
		addField(field);

		field = new DataSourceTextField(InternetOrderConstants.INTERNETRENDELT_DATUM,
				internetorderLabels.rendelt_datum());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getRendelt(new AsyncCallback<List<RendeltSer>>() {
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

			public void onSuccess(List<RendeltSer> result) {
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

	private static void copyValues(RendeltSer from, ListGridRecord to) {
		to.setAttribute(InternetOrderConstants.INTERNETRENDELT_ROVIDNEV, from.getRovidnev());
		to.setAttribute(InternetOrderConstants.INTERNETRENDELT_RENDELES, from.getRendeles());
		to.setAttribute(InternetOrderConstants.INTERNETRENDELT_DATUM, from.getDatum());
	}

}
