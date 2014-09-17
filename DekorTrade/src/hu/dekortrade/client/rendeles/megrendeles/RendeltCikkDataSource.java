package hu.dekortrade.client.rendeles.megrendeles;

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
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class RendeltCikkDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private MegrendelesLabels megrendelesLabels = GWT.create(MegrendelesLabels.class);

	public RendeltCikkDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(MegrendelesConstants.MEGRENDELES_NEV,
				megrendelesLabels.megrendeles_vevo());
		field.setPrimaryKey(true);
		addField(field);
		
		field = new DataSourceTextField(MegrendelesConstants.MEGRENDELES_RENDELES,
				megrendelesLabels.megrendeles_cedula());
		field.setPrimaryKey(true);
		addField(field);
		
		field = new DataSourceTextField(MegrendelesConstants.MEGRENDELES_CIKKSZAM,
				megrendelesLabels.megrendeles_cikkszam());
		addField(field);

		field = new DataSourceTextField(MegrendelesConstants.MEGRENDELES_SZINKOD,
				megrendelesLabels.megrendeles_szinkod());
		addField(field);

		field = new DataSourceTextField(MegrendelesConstants.MEGRENDELES_EXPORTKARTON,
				megrendelesLabels.megrendeles_exportkarton());
		addField(field);
	
		field = new DataSourceTextField(MegrendelesConstants.MEGRENDELES_KISKARTON,
				megrendelesLabels.megrendeles_kiskarton());
		addField(field);

		field = new DataSourceTextField(MegrendelesConstants.MEGRENDELES_DARAB,
				megrendelesLabels.megrendeles_darab());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getRendeles(request.getCriteria().getAttributeAsString(
				MegrendelesConstants.MEGRENDELES_STATUS),request.getCriteria().getAttributeAsString(
				MegrendelesConstants.MEGRENDELES_CIKKSZAM),request.getCriteria().getAttributeAsString(
						MegrendelesConstants.MEGRENDELES_SZINKOD), new AsyncCallback<List<RendeltcikkSer>>() {
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

			public void onSuccess(List<RendeltcikkSer> result) {
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

	private static void copyValues(RendeltcikkSer  from, ListGridRecord to) {
		to.setAttribute(MegrendelesConstants.MEGRENDELES_NEV, from.getNev());
		to.setAttribute(MegrendelesConstants.MEGRENDELES_RENDELES, from.getRendeles());
		to.setAttribute(MegrendelesConstants.MEGRENDELES_CIKKSZAM, from.getCikkszam());
		to.setAttribute(MegrendelesConstants.MEGRENDELES_SZINKOD, from.getSzinkod());
		to.setAttribute(MegrendelesConstants.MEGRENDELES_EXPORTKARTON, from.getExportkarton());
		to.setAttribute(MegrendelesConstants.MEGRENDELES_KISKARTON, from.getKiskarton());
		to.setAttribute(MegrendelesConstants.MEGRENDELES_DARAB, from.getDarab());
	}

}
