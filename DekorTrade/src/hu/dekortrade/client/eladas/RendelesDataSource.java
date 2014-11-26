package hu.dekortrade.client.eladas;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.RendeltcikkSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
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

		field = new DataSourceFloatField(EladasConstants.RENDELES_ARUSD,
				eladasLabels.rendeles_arusd());
		addField(field);

		field = new DataSourceFloatField(EladasConstants.RENDELES_FIZETUSD,
				eladasLabels.rendeles_fizetusd());
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

		
		JavaScriptObject data = request.getData();
		final ListGridRecord rec = new ListGridRecord(data);
		RendeltcikkSer rendeltcikkSer = new RendeltcikkSer();
		copyValues(rec, rendeltcikkSer);
		dekorTradeService.removeRendeles(rendeltcikkSer, new AsyncCallback<RendeltcikkSer>() {
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

	private static void copyValues(RendeltcikkSer from, ListGridRecord to) {
		to.setAttribute(EladasConstants.RENDELES_RENDELES, from.getRendeles());
		to.setAttribute(EladasConstants.RENDELES_CIKKSZAM, from.getCikkszam());
		to.setAttribute(EladasConstants.RENDELES_SZINKOD, from.getSzinkod());
		to.setAttribute(EladasConstants.RENDELES_EXPORTKARTON,
				from.getExportkarton());
		to.setAttribute(EladasConstants.RENDELES_KISKARTON, from.getKiskarton());
		to.setAttribute(EladasConstants.RENDELES_DARAB, from.getDarab());
		to.setAttribute(EladasConstants.RENDELES_ARUSD, from.getArusd());
		to.setAttribute(EladasConstants.RENDELES_FIZETUSD, from.getFizetusd());
	}
	
	private static void copyValues(ListGridRecord from, RendeltcikkSer to) {
		to.setRendeles(from
				.getAttributeAsString(EladasConstants.RENDELES_RENDELES));
		to.setCikkszam(from
				.getAttributeAsString(EladasConstants.RENDELES_CIKKSZAM));
		to.setSzinkod(from
				.getAttributeAsString(EladasConstants.RENDELES_SZINKOD));
		to.setExportkarton(from
				.getAttributeAsInt(EladasConstants.RENDELES_EXPORTKARTON));
		to.setKiskarton(from
				.getAttributeAsInt(EladasConstants.RENDELES_KISKARTON));
		to.setDarab(from
				.getAttributeAsInt(EladasConstants.RENDELES_DARAB));	
		to.setArusd(from
				.getAttributeAsDouble(EladasConstants.RENDELES_ARUSD));				
		to.setFizetusd(from
				.getAttributeAsDouble(EladasConstants.RENDELES_FIZETUSD));		
	}
}
