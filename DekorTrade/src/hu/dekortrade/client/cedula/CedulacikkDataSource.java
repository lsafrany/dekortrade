package hu.dekortrade.client.cedula;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.CedulacikkSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CedulacikkDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private CedulaLabels cedulaLabels = GWT.create(CedulaLabels.class);

	public CedulacikkDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(CedulaConstants.CEDULACIKK_CEDULA,
				cedulaLabels.cedula());
		field.setHidden(true);
		addField(field);
		
		field = new DataSourceTextField(CedulaConstants.CEDULACIKK_STATUS,
				cedulaLabels.status());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(CedulaConstants.CEDULACIKK_CIKKSZAM,
				cedulaLabels.cikkszam());
		addField(field);
	
		field = new DataSourceTextField(CedulaConstants.CEDULACIKK_SZINKOD,
				cedulaLabels.szinkod());
		addField(field);
		
		field = new DataSourceTextField(CedulaConstants.CEDULACIKK_MEGNEVEZES,
				cedulaLabels.megnevezes());
		addField(field);

		field = new DataSourceTextField(CedulaConstants.CEDULACIKK_AR,
				cedulaLabels.ar());
		addField(field);

		field = new DataSourceTextField(CedulaConstants.CEDULACIKK_AREUR,
				cedulaLabels.areur());
		addField(field);

		field = new DataSourceTextField(CedulaConstants.CEDULACIKK_EXPORTKARTON,
				cedulaLabels.exportkarton());
		addField(field);

		field = new DataSourceTextField(CedulaConstants.CEDULACIKK_KISKARTON,
				cedulaLabels.kiskarton());
		addField(field);
		
		field = new DataSourceTextField(CedulaConstants.CEDULACIKK_DARAB,
				cedulaLabels.darab());
		addField(field);

		field = new DataSourceTextField(CedulaConstants.CEDULACIKK_FIZET,
				cedulaLabels.fizet());
		addField(field);

		field = new DataSourceTextField(CedulaConstants.CEDULACIKK_FIZETEUR,
				cedulaLabels.fizeteur());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getCedulacikk(
				request.getCriteria().getAttributeAsString(
						CedulaConstants.CEDULA_CEDULA),
				request.getCriteria().getAttributeAsString(
						CedulaConstants.CEDULA_STATUS),
				new AsyncCallback<List<CedulacikkSer>>() {
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

					public void onSuccess(List<CedulacikkSer> result) {
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

	private static void copyValues(CedulacikkSer from, ListGridRecord to) {
		to.setAttribute(CedulaConstants.CEDULACIKK_CEDULA, from.getCedula());
		to.setAttribute(CedulaConstants.CEDULACIKK_STATUS, from.getStatus());
		to.setAttribute(CedulaConstants.CEDULACIKK_CIKKSZAM, from.getCikkszam());
		to.setAttribute(CedulaConstants.CEDULACIKK_SZINKOD, from.getSzinkod());
		to.setAttribute(CedulaConstants.CEDULACIKK_MEGNEVEZES, from.getMegnevezes());
		to.setAttribute(CedulaConstants.CEDULACIKK_AR, from.getAr());		
		to.setAttribute(CedulaConstants.CEDULACIKK_AREUR, from.getAreur());
		to.setAttribute(CedulaConstants.CEDULACIKK_EXPORTKARTON, from.getExportkarton());
		to.setAttribute(CedulaConstants.CEDULACIKK_KISKARTON, from.getKiskarton());		
		to.setAttribute(CedulaConstants.CEDULACIKK_DARAB, from.getDarab());		
		to.setAttribute(CedulaConstants.CEDULACIKK_FIZET, from.getFizet());				
		to.setAttribute(CedulaConstants.CEDULACIKK_FIZETEUR, from.getFizeteur());			
	}

}
