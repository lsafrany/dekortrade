package hu.dekortrade.client.lekerdezes.forgalom;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.EladasSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class EladasDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private ForgalomLabels forgalomLabels = GWT.create(ForgalomLabels.class);

	public EladasDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(ForgalomConstants.ELADAS_CEDULA,
				forgalomLabels.eladas_cedula());
		addField(field);

		field = new DataSourceTextField(ForgalomConstants.ELADAS_EXPORTKARTON,
				forgalomLabels.eladas_exportkarton());
		addField(field);

		field = new DataSourceTextField(ForgalomConstants.ELADAS_KISKARTON,
				forgalomLabels.eladas_kiskarton());
		addField(field);

		field = new DataSourceTextField(ForgalomConstants.ELADAS_DARAB,
				forgalomLabels.eladas_darab());
		addField(field);

	
	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getEladas(
				request.getCriteria().getAttributeAsString(
						ForgalomConstants.CIKK_CIKKSZAM),
				request.getCriteria().getAttributeAsString(
						ForgalomConstants.CIKK_SZINKOD),
				new AsyncCallback<List<EladasSer>>() {
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

					public void onSuccess(List<EladasSer> result) {
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

	private static void copyValues(EladasSer from, ListGridRecord to) {
		to.setAttribute(ForgalomConstants.ELADAS_CEDULA, from.getCedula());
		to.setAttribute(ForgalomConstants.ELADAS_EXPORTKARTON, from.getExportkarton());
		to.setAttribute(ForgalomConstants.ELADAS_KISKARTON, from.getKiskarton());
		to.setAttribute(ForgalomConstants.ELADAS_DARAB, from.getDarab());
	}

}
