package hu.dekortrade.client.lekerdezes.forgalom;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceImageField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class KepDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private ForgalomLabels ctozsLabels = GWT.create(ForgalomLabels.class);

	private static String cikkszam = "";

	private static String szinkod = "";

	private static String sorszam = "";

	public KepDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(ForgalomConstants.KEP_SORSZAM,
				ctozsLabels.kepsorszam());
		field.setPrimaryKey(true);
		field.setHidden(true);
		addField(field);

		field = new DataSourceImageField(ForgalomConstants.KEP_KEP,
				ctozsLabels.kep());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		cikkszam = request
				.getAttributeAsString(ForgalomConstants.CIKK_CIKKSZAM);
		szinkod = request.getAttributeAsString(ForgalomConstants.CIKK_SZINKOD);
		dekorTradeService.getKep(cikkszam, szinkod,
				new AsyncCallback<List<String>>() {
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

					public void onSuccess(List<String> result) {
						ListGridRecord[] list = new ListGridRecord[result
								.size()];
						for (int i = 0; i < result.size(); i++) {
							ListGridRecord record = new ListGridRecord();
							copyValues(cikkszam, result.get(i), record);
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

		JavaScriptObject data = request.getData();
		final ListGridRecord rec = new ListGridRecord(data);

		cikkszam = request
				.getAttributeAsString(ForgalomConstants.CIKK_CIKKSZAM);

		szinkod = request.getAttributeAsString(ForgalomConstants.CIKK_SZINKOD);

		sorszam = request.getAttributeAsString(ForgalomConstants.KEP_SORSZAM);

		dekorTradeService.removeKep(cikkszam, szinkod, sorszam,
				new AsyncCallback<String>() {
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

					public void onSuccess(String result) {
						ListGridRecord[] list = new ListGridRecord[1];
						// We do not receive removed record from server.
						// Return record from request.
						setLastId(null);
						list[0] = rec;
						response.setData(list);
						processResponse(requestId, response);
					}

				});

	}

	private static void copyValues(String cikkszam, String from,
			ListGridRecord to) {
		to.setAttribute(ForgalomConstants.KEP_SORSZAM, from);
		to.setAttribute(ForgalomConstants.KEP_KEP, GWT.getModuleBaseURL()
				+ "download?cikkszam=" + cikkszam + "&szinkod=" + szinkod
				+ "&sorszam=" + from);
	}

}
