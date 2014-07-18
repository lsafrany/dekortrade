package hu.dekortrade.client.lekerdezes.cedulak;

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
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CedulakDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private CedulakLabels cedulaLabels = GWT.create(CedulakLabels.class);

	private String vevo = null;
	private String menu = null;

	public CedulakDataSource(String vevo, String menu) {
		this.vevo = vevo;
		this.menu = menu;

		DataSourceField field;

		field = new DataSourceTextField(CedulakConstants.CEDULA_CEDULA,
				cedulaLabels.cedula());
		field.setPrimaryKey(true);
		addField(field);

		field = new DataSourceEnumField(CedulakConstants.CEDULA_STATUS,
				cedulaLabels.status());
		field.setValueMap(ClientConstants.getCedulaTipus());
		addField(field);

		field = new DataSourceTextField(CedulakConstants.CEDULA_VEVO,
				cedulaLabels.vevo());
		addField(field);

		field = new DataSourceTextField(CedulakConstants.CEDULA_VEVONEV,
				cedulaLabels.vevonev());
		addField(field);

		field = new DataSourceTextField(CedulakConstants.CEDULA_ELADO,
				cedulaLabels.elado());
		addField(field);

		field = new DataSourceTextField(CedulakConstants.CEDULA_ELADONEV,
				cedulaLabels.eladonev());
		addField(field);

		field = new DataSourceFloatField(CedulakConstants.CEDULA_BEFIZETHUF,
				cedulaLabels.befizet());
		addField(field);

		field = new DataSourceFloatField(CedulakConstants.CEDULA_BEFIZETEUR,
				cedulaLabels.befizeteur());
		addField(field);

		field = new DataSourceFloatField(CedulakConstants.CEDULA_BEFIZETUSD,
				cedulaLabels.befizetusd());
		addField(field);

		field = new DataSourceDateField(CedulakConstants.CEDULA_DATUM,
				cedulaLabels.datum());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getCedula(vevo, menu,request.getCriteria().getAttributeAsString(
				CedulakConstants.CEDULA_STATUS),
				new AsyncCallback<List<CedulaSer>>() {
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

					public void onSuccess(List<CedulaSer> result) {
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

	private static void copyValues(CedulaSer from, ListGridRecord to) {
		to.setAttribute(CedulakConstants.CEDULA_CEDULA, from.getCedula());
		to.setAttribute(CedulakConstants.CEDULA_STATUS, from.getStatus());
		to.setAttribute(CedulakConstants.CEDULA_VEVO, from.getRovidnev());
		to.setAttribute(CedulakConstants.CEDULA_VEVONEV, from.getVevonev());
		to.setAttribute(CedulakConstants.CEDULA_ELADO, from.getElado());
		to.setAttribute(CedulakConstants.CEDULA_ELADONEV, from.getEladonev());
		to.setAttribute(CedulakConstants.CEDULA_BEFIZETHUF, from.getBefizethuf());
		to.setAttribute(CedulakConstants.CEDULA_BEFIZETEUR, from.getBefizeteur());
		to.setAttribute(CedulakConstants.CEDULA_BEFIZETUSD, from.getBefizetusd());
		to.setAttribute(CedulakConstants.CEDULA_DATUM, from.getDatum());
	}

}
