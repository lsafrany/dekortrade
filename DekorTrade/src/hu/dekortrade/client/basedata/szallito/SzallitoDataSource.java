package hu.dekortrade.client.basedata.szallito;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.SzallitoSer;

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

public class SzallitoDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private SzallitoLabels szallitoLabels = GWT.create(SzallitoLabels.class);

	public SzallitoDataSource() {

		TextItem textItem = new TextItem();
		textItem.setWidth("400");

		DataSourceField field;

		field = new DataSourceTextField(SzallitoConstants.SZALLITO_KOD,
				szallitoLabels.szallito_kod());
		field.setPrimaryKey(true);
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(SzallitoConstants.SZALLITO_NEV,
				szallitoLabels.szallito_nev());
		field.setLength(40);
		field.setEditorProperties(textItem);
		addField(field);

		field = new DataSourceTextField(SzallitoConstants.SZALLITO_CIM,
				szallitoLabels.szallito_cim());
		field.setLength(50);
		field.setEditorProperties(textItem);
		addField(field);

		field = new DataSourceTextField(SzallitoConstants.SZALLITO_ELERHETOSEG,
				szallitoLabels.szallito_elerhetoseg());
		field.setLength(150);
		field.setEditorProperties(textItem);
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getSzallito(new AsyncCallback<List<SzallitoSer>>() {
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

			public void onSuccess(List<SzallitoSer> result) {
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

		JavaScriptObject data = request.getData();
		ListGridRecord rec = new ListGridRecord(data);
		SzallitoSer szallitoSer = new SzallitoSer();
		copyValues(rec, szallitoSer);
		dekorTradeService.addSzallito(szallitoSer,
				new AsyncCallback<SzallitoSer>() {
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

					public void onSuccess(SzallitoSer result) {
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
		SzallitoSer szallitoSer = new SzallitoSer();
		copyValues(rec, szallitoSer);
		dekorTradeService.updateSzallito(szallitoSer,
				new AsyncCallback<SzallitoSer>() {
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

					public void onSuccess(SzallitoSer result) {
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
		SzallitoSer szallitoSer = new SzallitoSer();
		copyValues(rec, szallitoSer);
		dekorTradeService.removeSzallito(szallitoSer,
				new AsyncCallback<SzallitoSer>() {
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

					public void onSuccess(SzallitoSer result) {
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

	private static void copyValues(SzallitoSer from, ListGridRecord to) {
		to.setAttribute(SzallitoConstants.SZALLITO_KOD, from.getKod());
		to.setAttribute(SzallitoConstants.SZALLITO_NEV, from.getNev());
		to.setAttribute(SzallitoConstants.SZALLITO_CIM, from.getCim());
		to.setAttribute(SzallitoConstants.SZALLITO_ELERHETOSEG,
				from.getElerhetoseg());
	}

	private static void copyValues(ListGridRecord from, SzallitoSer to) {

		to.setKod(from.getAttributeAsString(SzallitoConstants.SZALLITO_KOD));
		to.setNev(from.getAttributeAsString(SzallitoConstants.SZALLITO_NEV));
		to.setCim(from.getAttributeAsString(SzallitoConstants.SZALLITO_CIM));
		to.setElerhetoseg(from
				.getAttributeAsString(SzallitoConstants.SZALLITO_ELERHETOSEG));
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
