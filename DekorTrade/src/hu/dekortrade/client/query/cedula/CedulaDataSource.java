package hu.dekortrade.client.query.cedula;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.CedulaSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceEnumField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CedulaDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private CedulaLabels cedulaLabels = GWT.create(CedulaLabels.class);

	private String vevo  = null;
	private String menu = null;

	public CedulaDataSource(String vevo, String menu) {
		this.vevo  = vevo;
		this.menu  = menu;

		DataSourceField field;

		field = new DataSourceTextField(CedulaConstants.CEDULA_CEDULA,
				cedulaLabels.cedula());
		addField(field);

		field = new DataSourceEnumField(CedulaConstants.CEDULA_STATUS,
				cedulaLabels.status());
		field.setValueMap(ClientConstants.getCedulaTipus());
		addField(field);

		field = new DataSourceTextField(CedulaConstants.CEDULA_VEVO,
				cedulaLabels.vevo());
		addField(field);

		field = new DataSourceTextField(CedulaConstants.CEDULA_VEVONEV,
				cedulaLabels.vevonev());
		addField(field);

		field = new DataSourceTextField(CedulaConstants.CEDULA_ELADO,
				cedulaLabels.elado());
		addField(field);

		field = new DataSourceTextField(CedulaConstants.CEDULA_ELADONEV,
				cedulaLabels.eladonev());
		addField(field);

		field = new DataSourceDateField(CedulaConstants.CEDULA_DATUM,
				cedulaLabels.datum());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getCedula(vevo,menu,new AsyncCallback<List<CedulaSer>>() {
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

			public void onSuccess(List<CedulaSer> result) {
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

	private static void copyValues(CedulaSer from, ListGridRecord to) {
		to.setAttribute(CedulaConstants.CEDULA_CEDULA, from.getCedula());
		to.setAttribute(CedulaConstants.CEDULA_STATUS, from.getStatus());
		to.setAttribute(CedulaConstants.CEDULA_VEVO, from.getRovidnev());
		to.setAttribute(CedulaConstants.CEDULA_VEVONEV, from.getVevonev());
		to.setAttribute(CedulaConstants.CEDULA_ELADO, from.getElado());
		to.setAttribute(CedulaConstants.CEDULA_ELADONEV, from.getEladonev());
		to.setAttribute(CedulaConstants.CEDULA_DATUM, from.getDatum());
	}

}
