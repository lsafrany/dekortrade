package hu.dekortrade.client.rendelles.internet;

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

public class InternetRendeltcikkDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private InternetLabels internetorderLabels = GWT
			.create(InternetLabels.class);

	public InternetRendeltcikkDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(
				InternetConstants.INTERNETRENDELTCIKK_ROVIDNEV,
				internetorderLabels.rendelt_rovidnev());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(
				InternetConstants.INTERNETRENDELTCIKK_RENDELES,
				internetorderLabels.rendelt_rendeles());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(
				InternetConstants.INTERNETRENDELTCIKK_CIKKSZAM,
				internetorderLabels.rendelt_cikkszam());
		addField(field);

		field = new DataSourceTextField(
				InternetConstants.INTERNETRENDELTCIKK_SZINKOD,
				internetorderLabels.rendelt_szinkod());
		addField(field);

		field = new DataSourceIntegerField(
				InternetConstants.INTERNETRENDELTCIKK_EXPORTKARTON,
				internetorderLabels.rendelt_exportkarton());
		addField(field);

		field = new DataSourceIntegerField(
				InternetConstants.INTERNETRENDELTCIKK_KISKARTON,
				internetorderLabels.rendelt_kiskarton());
		addField(field);

		field = new DataSourceIntegerField(
				InternetConstants.INTERNETRENDELTCIKK_DARAB,
				internetorderLabels.rendelt_darab());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getRendeltcikk(
				request.getCriteria().getAttributeAsString(
						InternetConstants.INTERNETRENDELTCIKK_ROVIDNEV),
				request.getCriteria().getAttributeAsString(
						InternetConstants.INTERNETRENDELTCIKK_RENDELES),
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
		to.setAttribute(InternetConstants.INTERNETRENDELTCIKK_ROVIDNEV,
				from.getRovidnev());
		to.setAttribute(InternetConstants.INTERNETRENDELTCIKK_RENDELES,
				from.getRendeles());
		to.setAttribute(InternetConstants.INTERNETRENDELTCIKK_CIKKSZAM,
				from.getCikkszam());
		to.setAttribute(InternetConstants.INTERNETRENDELTCIKK_SZINKOD,
				from.getSzinkod());
		to.setAttribute(InternetConstants.INTERNETRENDELTCIKK_EXPORTKARTON,
				from.getExportkarton());
		to.setAttribute(InternetConstants.INTERNETRENDELTCIKK_KISKARTON,
				from.getKiskarton());
		to.setAttribute(InternetConstants.INTERNETRENDELTCIKK_DARAB,
				from.getDarab());

	}

}
