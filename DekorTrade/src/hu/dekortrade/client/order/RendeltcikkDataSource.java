package hu.dekortrade.client.order;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.RendeltcikkSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class RendeltcikkDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private OrderLabels orderLabels = GWT.create(OrderLabels.class);

	public RendeltcikkDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(OrderConstants.RENDELTCIKK_ROVIDNEV,
				orderLabels.rendelt_rovidnev());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(OrderConstants.RENDELTCIKK_RENDELES,
				orderLabels.rendelt_rendeles());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(OrderConstants.RENDELTCIKK_CIKKSZAM,
				orderLabels.rendelt_cikkszam());
		addField(field);

		field = new DataSourceTextField(OrderConstants.RENDELTCIKK_EXPORTKARTON,
				orderLabels.rendelt_exportkarton());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getRendeltcikk(
				request.getCriteria().getAttributeAsString(
						OrderConstants.RENDELTCIKK_ROVIDNEV),
				request.getCriteria().getAttributeAsString(
						OrderConstants.RENDELTCIKK_RENDELES),						
				new AsyncCallback<ArrayList<RendeltcikkSer>>() {
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

					public void onSuccess(ArrayList<RendeltcikkSer> result) {
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

	private static void copyValues(RendeltcikkSer from, ListGridRecord to) {
		to.setAttribute(OrderConstants.RENDELTCIKK_ROVIDNEV, from.getRovidnev());
		to.setAttribute(OrderConstants.RENDELTCIKK_RENDELES, from.getRendeles());
		to.setAttribute(OrderConstants.RENDELTCIKK_CIKKSZAM, from.getCikkszam());
		to.setAttribute(OrderConstants.RENDELTCIKK_EXPORTKARTON, from.getExportkarton());
	}

}
