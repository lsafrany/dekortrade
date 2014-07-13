package hu.dekortrade.client.srendszer;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.FelhasznaloSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceEnumField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class FelhasznaloDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private RendszerLabels systemLabels = GWT.create(RendszerLabels.class);

	public FelhasznaloDataSource() {

		TextItem textItem = new TextItem();
		textItem.setWidth("200");

		DataSourceField field;

		field = new DataSourceTextField(RendszerConstants.FELHASZNALO_ROVIDNEV,
				systemLabels.felhasznalo_rovidnev());
		field.setPrimaryKey(true);
		field.setLength(20);
		field.setRequired(true);
		addField(field);

		field = new DataSourceTextField(RendszerConstants.FELHASZNALO_NEV,
				systemLabels.felhasznalo_nev());
		field.setLength(40);
		field.setEditorProperties(textItem);
		field.setRequired(true);
		addField(field);

		field = new DataSourceEnumField(RendszerConstants.FELHASZNALO_MENU,
				systemLabels.felhasznalo_menu());
		field.setValueMap(ClientConstants.getMainMenu());
		field.setRequired(true);
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService
				.getFelhasznalo(new AsyncCallback<List<FelhasznaloSer>>() {
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

					public void onSuccess(List<FelhasznaloSer> result) {
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
		FelhasznaloSer felhasznaloSer = new FelhasznaloSer();
		copyValues(rec, felhasznaloSer);
		dekorTradeService.addFelhasznalo(felhasznaloSer,
				new AsyncCallback<FelhasznaloSer>() {
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

					public void onSuccess(FelhasznaloSer result) {
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
		FelhasznaloSer felhasznaloSer = new FelhasznaloSer();
		copyValues(rec, felhasznaloSer);
		dekorTradeService.updateFelhasznalo(felhasznaloSer,
				new AsyncCallback<FelhasznaloSer>() {
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

					public void onSuccess(FelhasznaloSer result) {
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
		FelhasznaloSer felhasznaloSer = new FelhasznaloSer();
		copyValues(rec, felhasznaloSer);
		dekorTradeService.removeFelhasznalo(felhasznaloSer,
				new AsyncCallback<FelhasznaloSer>() {
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

					public void onSuccess(FelhasznaloSer result) {
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

	private static void copyValues(FelhasznaloSer from, ListGridRecord to) {
		to.setAttribute(RendszerConstants.FELHASZNALO_ROVIDNEV,
				from.getRovidnev());
		to.setAttribute(RendszerConstants.FELHASZNALO_NEV, from.getNev());
		to.setAttribute(RendszerConstants.FELHASZNALO_MENU, from.getMenu());
	}

	private static void copyValues(ListGridRecord from, FelhasznaloSer to) {
		to.setRovidnev(from
				.getAttributeAsString(RendszerConstants.FELHASZNALO_ROVIDNEV));
		to.setNev(from.getAttributeAsString(RendszerConstants.FELHASZNALO_NEV));
		to.setMenu(from
				.getAttributeAsString(RendszerConstants.FELHASZNALO_MENU));
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
