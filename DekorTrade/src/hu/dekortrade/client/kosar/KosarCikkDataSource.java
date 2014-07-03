package hu.dekortrade.client.kosar;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.KosarSer;
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
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class KosarCikkDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private KosarLabels kosarLabels = GWT.create(KosarLabels.class);

	private String elado = null;
	private String vevo  = null;
	private String tipus = null;
	
	public KosarCikkDataSource(String elado, String vevo, String tipus) {
		this.elado  = elado;
		this.vevo  = vevo;
		this.tipus  = tipus;
		
		TextItem textItem = new TextItem();
		textItem.setWidth("200");

		DataSourceField field;

		field = new DataSourceTextField(KosarConstants.KOSAR_ELADO,
				kosarLabels.kosar_elado());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(KosarConstants.KOSAR_VEVO,
				kosarLabels.kosar_vevo());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(KosarConstants.KOSAR_TIPUS,
				kosarLabels.kosar_tipus());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(KosarConstants.KOSAR_CIKKSZAM,
				kosarLabels.kosar_cikkszam());
		field.setPrimaryKey(true);
		addField(field);

		field = new DataSourceTextField(KosarConstants.KOSAR_SZINKOD,
				kosarLabels.kosar_szinkod());
		field.setPrimaryKey(true);
		addField(field);
		
		field = new DataSourceTextField(KosarConstants.KOSAR_MEGNEVEZES,
				kosarLabels.kosar_megnevezes());
		field.setEditorProperties(textItem);
		addField(field);
	
		field = new DataSourceIntegerField(KosarConstants.KOSAR_EXPORTKARTON,
				kosarLabels.kosar_exportkarton());
		addField(field);

		field = new DataSourceIntegerField(KosarConstants.KOSAR_KISKARTON,
				kosarLabels.kosar_kiskarton());
		addField(field);
		
		field = new DataSourceIntegerField(KosarConstants.KOSAR_DARAB,
				kosarLabels.kosar_darab());
		addField(field);

		field = new DataSourceFloatField(KosarConstants.KOSAR_AR,
				kosarLabels.koasr_ar());
		addField(field);

		field = new DataSourceFloatField(KosarConstants.KOSAR_AREUR,
				kosarLabels.koasr_areur());
		addField(field);

		field = new DataSourceFloatField(KosarConstants.KOSAR_ARUSD,
				kosarLabels.koasr_arusd());
		addField(field);

		field = new DataSourceFloatField(KosarConstants.KOSAR_FIZET,
				kosarLabels.koasr_fizet());
		addField(field);

		field = new DataSourceFloatField(KosarConstants.KOSAR_FIZETEUR,
				kosarLabels.koasr_fizeteur());
		addField(field);

		field = new DataSourceFloatField(KosarConstants.KOSAR_FIZETUSD,
				kosarLabels.koasr_fizetusd());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService
				.getKosarCikk(this.elado,this.vevo,this.tipus,new AsyncCallback<List<KosarSer>>() {
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

					public void onSuccess(List<KosarSer> result) {
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

		JavaScriptObject data = request.getData();
		ListGridRecord rec = new ListGridRecord(data);
		KosarSer kosarSer = new KosarSer();
		copyValues(rec, kosarSer);
		kosarSer.setElado(this.elado);
		kosarSer.setVevo(this.vevo);
		kosarSer.setTipus(this.tipus);
		dekorTradeService.addKosarCikk(kosarSer, new AsyncCallback<KosarSer>() {
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

			public void onSuccess(KosarSer result) {
				ListGridRecord[] list = new ListGridRecord[1];
				ListGridRecord newRec = new ListGridRecord();
				copyValues(result, newRec);
				list[0] = newRec;
				response.setData(list);
				processResponse(requestId, response);
			}
		});

	}

	@Override
	protected void executeUpdate(final String requestId,
			final DSRequest request, final DSResponse response) {

		ListGridRecord rec = getEditedRecord(request);
		KosarSer kosarSer = new KosarSer();
		copyValues(rec, kosarSer);
		kosarSer.setElado(this.elado);
		kosarSer.setVevo(this.vevo);
		kosarSer.setTipus(this.tipus);
		dekorTradeService.updateKosarCikk(kosarSer, new AsyncCallback<KosarSer>() {
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

			public void onSuccess(KosarSer result) {
				ListGridRecord[] list = new ListGridRecord[1];
				ListGridRecord updRec = new ListGridRecord();
				copyValues(result, updRec);
				list[0] = updRec;
				response.setData(list);
				processResponse(requestId, response);
			}
		});

	}

	@Override
	protected void executeRemove(final String requestId,
			final DSRequest request, final DSResponse response) {

		JavaScriptObject data = request.getData();
		final ListGridRecord rec = new ListGridRecord(data);
		KosarSer kosarSer = new KosarSer();
		copyValues(rec, kosarSer);
		kosarSer.setElado(this.elado);
		kosarSer.setVevo(this.vevo);
		kosarSer.setTipus(this.tipus);
		dekorTradeService.removeKosarCikk(kosarSer, new AsyncCallback<KosarSer>() {
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

			public void onSuccess(KosarSer result) {
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

	private static void copyValues(KosarSer from, ListGridRecord to) {
		to.setAttribute(KosarConstants.KOSAR_ELADO, from.getElado());
		to.setAttribute(KosarConstants.KOSAR_VEVO, from.getVevo());
		to.setAttribute(KosarConstants.KOSAR_TIPUS, from.getTipus());
		to.setAttribute(KosarConstants.KOSAR_CIKKSZAM, from.getCikkszam());
		to.setAttribute(KosarConstants.KOSAR_SZINKOD, from.getSzinkod());
		to.setAttribute(KosarConstants.KOSAR_MEGNEVEZES, from.getMegnevezes());
		to.setAttribute(KosarConstants.KOSAR_EXPORTKARTON, from.getExportkarton());
		to.setAttribute(KosarConstants.KOSAR_KISKARTON, from.getKiskarton());
		to.setAttribute(KosarConstants.KOSAR_DARAB, from.getDarab());
		to.setAttribute(KosarConstants.KOSAR_AR, from.getAr());
		to.setAttribute(KosarConstants.KOSAR_AREUR, from.getAreur());
		to.setAttribute(KosarConstants.KOSAR_ARUSD, from.getArusd());
		to.setAttribute(KosarConstants.KOSAR_FIZET, from.getFizet());
		to.setAttribute(KosarConstants.KOSAR_FIZETEUR, from.getFizeteur());
		to.setAttribute(KosarConstants.KOSAR_FIZETUSD, from.getFizetusd());
	}

	private static void copyValues(ListGridRecord from, KosarSer to) {
		to.setElado(from.getAttributeAsString(KosarConstants.KOSAR_ELADO));
		to.setVevo(from.getAttributeAsString(KosarConstants.KOSAR_VEVO));
		to.setTipus(from.getAttributeAsString(KosarConstants.KOSAR_TIPUS));
		to.setCikkszam(from.getAttributeAsString(KosarConstants.KOSAR_CIKKSZAM));
		to.setSzinkod(from.getAttributeAsString(KosarConstants.KOSAR_SZINKOD));
		to.setMegnevezes(from.getAttributeAsString(KosarConstants.KOSAR_MEGNEVEZES));
		to.setExportkarton(from.getAttributeAsInt(KosarConstants.KOSAR_EXPORTKARTON));
		to.setKiskarton(from.getAttributeAsInt(KosarConstants.KOSAR_KISKARTON));
		to.setDarab(from.getAttributeAsInt(KosarConstants.KOSAR_DARAB));
		to.setAr(from.getAttributeAsFloat(KosarConstants.KOSAR_AR));
		to.setAreur(from.getAttributeAsFloat(KosarConstants.KOSAR_AREUR));
		to.setArusd(from.getAttributeAsFloat(KosarConstants.KOSAR_ARUSD));
		to.setFizet(from.getAttributeAsFloat(KosarConstants.KOSAR_FIZET));
		to.setFizeteur(from.getAttributeAsFloat(KosarConstants.KOSAR_FIZETEUR));
		to.setFizetusd(from.getAttributeAsFloat(KosarConstants.KOSAR_FIZETUSD));
	}

	private ListGridRecord getEditedRecord(DSRequest request) {

		// Retrieving values before edit
		JavaScriptObject oldValues = request
				.getAttributeAsJavaScriptObject("oldValues");
		// Creating new record for combining old values with changes
		ListGridRecord newRecord = new ListGridRecord();
		// Copying properties from old record
		JSOHelper.apply(oldValues, newRecord.getJsObj());
		// Retrieving changed values
		JavaScriptObject data = request.getData();
		// Apply changes
		JSOHelper.apply(data, newRecord.getJsObj());
		return newRecord;
	}

}
