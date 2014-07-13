package hu.dekortrade.client.lekerdezes.zarasok;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.ZarasSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ZarasokDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private ZarasokLabels zarasLabels = GWT.create(ZarasokLabels.class);

	public ZarasokDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(ZarasokConstants.ZARAS_ZARAS,
				zarasLabels.zaras());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(ZarasokConstants.ZARAS_PENZTAROS,
				zarasLabels.penztaros());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(ZarasokConstants.ZARAS_PENZTAROSNEV,
				zarasLabels.penztaros());
		addField(field);

		field = new DataSourceTextField(ZarasokConstants.ZARAS_DATUM,
				zarasLabels.datum());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getZaras(new AsyncCallback<List<ZarasSer>>() {
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

			public void onSuccess(List<ZarasSer> result) {
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

	private static void copyValues(ZarasSer from, ListGridRecord to) {
		to.setAttribute(ZarasokConstants.ZARAS_ZARAS, from.getZaras());
		to.setAttribute(ZarasokConstants.ZARAS_PENZTAROS, from.getPenztaros());
		to.setAttribute(ZarasokConstants.ZARAS_PENZTAROSNEV,
				from.getPenztarosnev());
		to.setAttribute(ZarasokConstants.ZARAS_DATUM, from.getDatum());
	}
}
