package hu.dekortrade.client.eladas;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.RendeltcikkSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class RendelesDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private EladasLabels eladasLabels = GWT.create(EladasLabels.class);

	private String vevo = null;

	public RendelesDataSource(String vevo) {
		this.vevo = vevo;

		DataSourceField field;

		field = new DataSourceTextField(EladasConstants.RENDELES_RENDELES,
				eladasLabels.rendeles());
		addField(field);
		field.setPrimaryKey(true);
		
		field = new DataSourceTextField(EladasConstants.RENDELES_CIKKSZAM,
				eladasLabels.rendeles_cikkszam());
		field.setPrimaryKey(true);
		addField(field);

		field = new DataSourceTextField(EladasConstants.RENDELES_SZINKOD,
				eladasLabels.rendeles_szinkod());
		field.setPrimaryKey(true);
		addField(field);

		field = new DataSourceIntegerField(EladasConstants.RENDELES_EXPORTKARTON,
				eladasLabels.rendeles_exportkarton());
		addField(field);

		field = new DataSourceIntegerField(EladasConstants.RENDELES_KISKARTON,
				eladasLabels.rendeles_kiskarton());
		addField(field);

		field = new DataSourceIntegerField(EladasConstants.RENDELES_DARAB,
				eladasLabels.rendeles_darab());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getRendeles(this.vevo,
				new AsyncCallback<List<RendeltcikkSer>>() {
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

					public void onSuccess(List<RendeltcikkSer> result) {
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
		to.setAttribute(EladasConstants.RENDELES_RENDELES, from.getRendeles());
		to.setAttribute(EladasConstants.RENDELES_CIKKSZAM, from.getCikkszam());
		to.setAttribute(EladasConstants.RENDELES_SZINKOD, from.getSzinkod());
		to.setAttribute(EladasConstants.RENDELES_EXPORTKARTON,
				from.getExportkarton());
		to.setAttribute(EladasConstants.RENDELES_KISKARTON, from.getKiskarton());
		to.setAttribute(EladasConstants.RENDELES_DARAB, from.getDarab());
	}
}
