package hu.dekortrade.client.raktar.beszallitas;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.RendeltcikkSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class MegRendeltCikkDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private BeszallitasLabels beszallitasLabels = GWT.create(BeszallitasLabels.class);

	public MegRendeltCikkDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(BeszallitasConstants.MEGRENDELT_NEV,
				beszallitasLabels.megrendelt_vevo());
		field.setPrimaryKey(true);
		addField(field);
		
		field = new DataSourceTextField(BeszallitasConstants.MEGRENDELT_RENDELES,
				beszallitasLabels.megrendelt_cedula());
		field.setPrimaryKey(true);
		addField(field);
		
		field = new DataSourceTextField(BeszallitasConstants.MEGRENDELT_CIKKSZAM,
				beszallitasLabels.megrendelt_cikkszam());
		addField(field);

		field = new DataSourceTextField(BeszallitasConstants.MEGRENDELT_SZINKOD,
				beszallitasLabels.megrendelt_szinkod());
		addField(field);

		field = new DataSourceTextField(BeszallitasConstants.MEGRENDELT_EXPORTKARTON,
				beszallitasLabels.megrendelt_exportkarton());
		addField(field);
	
		field = new DataSourceTextField(BeszallitasConstants.MEGRENDELT_KISKARTON,
				beszallitasLabels.megrendelt_kiskarton());
		addField(field);

		field = new DataSourceTextField(BeszallitasConstants.MEGRENDELT_DARAB,
				beszallitasLabels.megrendelt_darab());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getMegrendelt(Constants.ELORENDELT_MEGRENDELT,request.getCriteria().getAttributeAsString(
						BeszallitasConstants.MEGRENDELT_CIKKSZAM),request.getCriteria().getAttributeAsString(
								BeszallitasConstants.MEGRENDELT_SZINKOD), new AsyncCallback<List<RendeltcikkSer>>() {
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
		
		JavaScriptObject data = request.getData();
		final ListGridRecord rec = new ListGridRecord(data);
		RendeltcikkSer rendeltcikkSer = new RendeltcikkSer();
		copyValues(rec, rendeltcikkSer);
		dekorTradeService.updateRendeltcikk(rendeltcikkSer,
				new AsyncCallback<RendeltcikkSer>() {
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

					public void onSuccess(RendeltcikkSer result) {
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

	private static void copyValues(RendeltcikkSer  from, ListGridRecord to) {
		to.setAttribute(BeszallitasConstants.MEGRENDELT_NEV, from.getNev());
		to.setAttribute(BeszallitasConstants.MEGRENDELT_RENDELES, from.getRendeles());
		to.setAttribute(BeszallitasConstants.MEGRENDELT_CIKKSZAM, from.getCikkszam());
		to.setAttribute(BeszallitasConstants.MEGRENDELT_SZINKOD, from.getSzinkod());
		to.setAttribute(BeszallitasConstants.MEGRENDELT_EXPORTKARTON, from.getExportkarton());
		to.setAttribute(BeszallitasConstants.MEGRENDELT_KISKARTON, from.getKiskarton());
		to.setAttribute(BeszallitasConstants.MEGRENDELT_DARAB, from.getDarab());
	}

	private static void copyValues(ListGridRecord from, RendeltcikkSer to) {
		to.setNev(from.getAttributeAsString(BeszallitasConstants.MEGRENDELT_NEV));
		to.setRendeles(from.getAttributeAsString(BeszallitasConstants.MEGRENDELT_RENDELES));
		to.setCikkszam(from.getAttributeAsString(BeszallitasConstants.MEGRENDELT_CIKKSZAM));
		to.setSzinkod(from.getAttributeAsString(BeszallitasConstants.MEGRENDELT_SZINKOD));
		to.setExportkarton(from.getAttributeAsInt(BeszallitasConstants.MEGRENDELT_EXPORTKARTON));
		to.setKiskarton(from.getAttributeAsInt(BeszallitasConstants.MEGRENDELT_KISKARTON));
		to.setDarab(from.getAttributeAsInt(BeszallitasConstants.MEGRENDELT_DARAB));
	}

}
