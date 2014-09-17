package hu.dekortrade.client.lekerdezes.zarasok;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.client.penztar.zaras.ZarasConstants;
import hu.dekortrade.client.penztar.zaras.ZarasLabels;
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

public class ZarasokFizetesDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private ZarasLabels cashCloseLabels = GWT.create(ZarasLabels.class);

	public ZarasokFizetesDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(ZarasConstants.ZARASFIZETES_CEDULA,
				cashCloseLabels.cedula());
		addField(field);

		field = new DataSourceTextField(ZarasConstants.ZARASFIZETES_VEVO,
				cashCloseLabels.vevo());
		addField(field);

		field = new DataSourceTextField(ZarasConstants.ZARASFIZETES_VEVONEV,
				cashCloseLabels.vevonev());
		addField(field);

		field = new DataSourceEnumField(ZarasConstants.ZARASFIZETES_TIPUS,
				cashCloseLabels.tipus());
		field.setValueMap(ZarasConstants.getFizetesTipus());
		addField(field);

		field = new DataSourceTextField(ZarasConstants.ZARASFIZETES_MEGJEGYZES,
				cashCloseLabels.megjegyzes());
		addField(field);

		field = new DataSourceTextField(ZarasConstants.ZARASFIZETES_PENZTAROS,
				cashCloseLabels.penztaros());
		addField(field);

		field = new DataSourceTextField(
				ZarasConstants.ZARASFIZETES_PENZTAROSNEV,
				cashCloseLabels.penztarosnev());
		addField(field);

		field = new DataSourceFloatField(ZarasConstants.ZARASFIZETES_FIZET,
				cashCloseLabels.fizet());
		addField(field);

		field = new DataSourceFloatField(ZarasConstants.ZARASFIZETES_FIZETEUR,
				cashCloseLabels.fizeteur());
		addField(field);

		field = new DataSourceFloatField(ZarasConstants.ZARASFIZETES_FIZETUSD,
				cashCloseLabels.fizetusd());
		addField(field);

		field = new DataSourceDateField(ZarasConstants.ZARASFIZETES_DATUM,
				cashCloseLabels.datum());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getZarasFizetes(request.getCriteria()
				.getAttributeAsString(ZarasokConstants.ZARAS_ZARAS),
				new AsyncCallback<List<FizetesSer>>() {
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

					public void onSuccess(List<FizetesSer> result) {
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

	private static void copyValues(FizetesSer from, ListGridRecord to) {
		to.setAttribute(ZarasConstants.ZARASFIZETES_CEDULA, from.getCedula());
		to.setAttribute(ZarasConstants.ZARASFIZETES_VEVO, from.getVevo());
		to.setAttribute(ZarasConstants.ZARASFIZETES_VEVONEV, from.getVevonev());
		to.setAttribute(ZarasConstants.ZARASFIZETES_TIPUS, from.getTipus());
		to.setAttribute(ZarasConstants.ZARASFIZETES_MEGJEGYZES, from.getMegjegyzes());
		to.setAttribute(ZarasConstants.ZARASFIZETES_PENZTAROS,
				from.getPenztaros());
		to.setAttribute(ZarasConstants.ZARASFIZETES_PENZTAROSNEV,
				from.getPenztarosnev());
		to.setAttribute(ZarasConstants.ZARASFIZETES_FIZET, from.getFizet());
		to.setAttribute(ZarasConstants.ZARASFIZETES_FIZETEUR,
				from.getFizeteur());
		to.setAttribute(ZarasConstants.ZARASFIZETES_FIZETUSD,
				from.getFizetusd());
		to.setAttribute(ZarasConstants.ZARASFIZETES_DATUM, from.getDatum());
	}

}
