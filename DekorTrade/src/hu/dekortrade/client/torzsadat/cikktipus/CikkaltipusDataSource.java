package hu.dekortrade.client.torzsadat.cikktipus;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.CikkaltipusSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CikkaltipusDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private CikktipusLabels cikktipusLabels = GWT.create(CikktipusLabels.class);

	public CikkaltipusDataSource() {

		TextItem textItem = new TextItem();
		textItem.setWidth("400");

		DataSourceField field;

		field = new DataSourceTextField(CikktipusConstants.CIKKALTIPUS_FOKOD,
				cikktipusLabels.fokod());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(CikktipusConstants.CIKKALTIPUS_KOD,
				cikktipusLabels.kod());
		field.setPrimaryKey(true);
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(CikktipusConstants.CIKKALTIPUS_NEV,
				cikktipusLabels.nev());
		field.setLength(30);
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getCikkaltipus(request.getCriteria()
				.getAttributeAsString(CikktipusConstants.CIKKALTIPUS_FOKOD),
				new AsyncCallback<List<CikkaltipusSer>>() {
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

					public void onSuccess(List<CikkaltipusSer> result) {
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
		CikkaltipusSer cikkaltipusSer = new CikkaltipusSer();
		copyValues(rec, cikkaltipusSer);
		dekorTradeService.addCikkaltipus(cikkaltipusSer,
				new AsyncCallback<CikkaltipusSer>() {
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

					public void onSuccess(CikkaltipusSer result) {
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
		CikkaltipusSer cikkaltipusSer = new CikkaltipusSer();
		copyValues(rec, cikkaltipusSer);
		dekorTradeService.updateCikkaltipus(cikkaltipusSer,
				new AsyncCallback<CikkaltipusSer>() {
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

					public void onSuccess(CikkaltipusSer result) {
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

	}

	private static void copyValues(CikkaltipusSer from, ListGridRecord to) {
		to.setAttribute(CikktipusConstants.CIKKALTIPUS_FOKOD, from.getFokod());
		to.setAttribute(CikktipusConstants.CIKKALTIPUS_KOD, from.getKod());
		to.setAttribute(CikktipusConstants.CIKKALTIPUS_NEV, from.getNev());
	}

	private static void copyValues(ListGridRecord from, CikkaltipusSer to) {
		to.setFokod(from
				.getAttributeAsString(CikktipusConstants.CIKKALTIPUS_FOKOD));
		to.setKod(from.getAttributeAsString(CikktipusConstants.CIKKALTIPUS_KOD));
		to.setNev(from.getAttributeAsString(CikktipusConstants.CIKKALTIPUS_NEV));
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
