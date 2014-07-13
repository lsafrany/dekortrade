package hu.dekortrade.client.torzsadat.gyarto;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.GyartoSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.IsFloatValidator;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class GyartoDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private GyartoLabels gyartoLabels = GWT.create(GyartoLabels.class);

	public GyartoDataSource() {

		IsFloatValidator isFloatValidator = new IsFloatValidator();

		TextItem textItem = new TextItem();
		textItem.setWidth("400");

		TextItem textItem1 = new TextItem();
		textItem1.setWidth("200");

		TextAreaItem textAreaItem = new TextAreaItem();
		textAreaItem.setWidth("500");
		textAreaItem.setHeight("60");

		DataSourceField field;

		field = new DataSourceTextField(GyartoConstants.GYARTO_KOD,
				gyartoLabels.gyarto_kod());
		field.setPrimaryKey(true);
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(GyartoConstants.GYARTO_NEV,
				gyartoLabels.gyarto_nev());
		field.setLength(40);
		field.setEditorProperties(textItem);
		field.setRequired(true);
		addField(field);

		field = new DataSourceTextField(GyartoConstants.GYARTO_CIM,
				gyartoLabels.gyarto_cim());
		field.setLength(50);
		field.setEditorProperties(textItem);
		addField(field);

		field = new DataSourceTextField(GyartoConstants.GYARTO_ELERHETOSEG,
				gyartoLabels.gyarto_elerhetoseg());
		field.setLength(200);
		field.setEditorProperties(textAreaItem);
		addField(field);

		field = new DataSourceTextField(GyartoConstants.GYARTO_SWIFTKOD,
				gyartoLabels.gyarto_swiftkod());
		field.setLength(50);
		field.setEditorProperties(textItem1);
		addField(field);

		field = new DataSourceTextField(GyartoConstants.GYARTO_BANKADAT,
				gyartoLabels.gyarto_bankadat());
		field.setLength(100);
		field.setEditorProperties(textItem);
		addField(field);

		field = new DataSourceTextField(GyartoConstants.GYARTO_SZAMLASZAM,
				gyartoLabels.gyarto_szamlaszam());
		field.setLength(50);
		field.setEditorProperties(textItem1);
		addField(field);

		field = new DataSourceFloatField(GyartoConstants.GYARTO_EGYENLEG,
				gyartoLabels.gyarto_egyenleg());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceTextField(GyartoConstants.GYARTO_KEDVEZMENY,
				gyartoLabels.gyarto_kedvezmeny());
		field.setValueMap(GyartoConstants.getKedvezmeny());
		addField(field);

		field = new DataSourceTextField(GyartoConstants.GYARTO_MEGJEGYZES,
				gyartoLabels.gyarto_megjegyzes());
		field.setLength(200);
		field.setEditorProperties(textAreaItem);
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getGyarto(new AsyncCallback<List<GyartoSer>>() {
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

			public void onSuccess(List<GyartoSer> result) {
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
		GyartoSer szallitoSer = new GyartoSer();
		copyValues(rec, szallitoSer);
		dekorTradeService.addGyarto(szallitoSer,
				new AsyncCallback<GyartoSer>() {
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

					public void onSuccess(GyartoSer result) {
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
		GyartoSer szallitoSer = new GyartoSer();
		copyValues(rec, szallitoSer);
		dekorTradeService.updateGyarto(szallitoSer,
				new AsyncCallback<GyartoSer>() {
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

					public void onSuccess(GyartoSer result) {
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
		GyartoSer szallitoSer = new GyartoSer();
		copyValues(rec, szallitoSer);
		dekorTradeService.removeGyarto(szallitoSer,
				new AsyncCallback<GyartoSer>() {
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

					public void onSuccess(GyartoSer result) {
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

	private static void copyValues(GyartoSer from, ListGridRecord to) {
		to.setAttribute(GyartoConstants.GYARTO_KOD, from.getKod());
		to.setAttribute(GyartoConstants.GYARTO_NEV, from.getNev());
		to.setAttribute(GyartoConstants.GYARTO_CIM, from.getCim());
		to.setAttribute(GyartoConstants.GYARTO_ELERHETOSEG,
				from.getElerhetoseg());
		to.setAttribute(GyartoConstants.GYARTO_SWIFTKOD, from.getSwifkod());
		to.setAttribute(GyartoConstants.GYARTO_BANKADAT, from.getBankadat());
		to.setAttribute(GyartoConstants.GYARTO_SZAMLASZAM, from.getSzamlaszam());
		to.setAttribute(GyartoConstants.GYARTO_EGYENLEG, from.getEgyenleg());
		to.setAttribute(GyartoConstants.GYARTO_KEDVEZMENY, from.getKedvezmeny());
		to.setAttribute(GyartoConstants.GYARTO_MEGJEGYZES, from.getMegjegyzes());
	}

	private static void copyValues(ListGridRecord from, GyartoSer to) {

		to.setKod(from.getAttributeAsString(GyartoConstants.GYARTO_KOD));
		to.setNev(from.getAttributeAsString(GyartoConstants.GYARTO_NEV));
		to.setCim(from.getAttributeAsString(GyartoConstants.GYARTO_CIM));
		to.setElerhetoseg(from
				.getAttributeAsString(GyartoConstants.GYARTO_ELERHETOSEG));
		to.setSwifkod(from
				.getAttributeAsString(GyartoConstants.GYARTO_SWIFTKOD));
		to.setBankadat(from
				.getAttributeAsString(GyartoConstants.GYARTO_BANKADAT));
		to.setSzamlaszam(from
				.getAttributeAsString(GyartoConstants.GYARTO_SZAMLASZAM));
		to.setEgyenleg(from
				.getAttributeAsFloat(GyartoConstants.GYARTO_EGYENLEG));
		to.setKedvezmeny(from
				.getAttributeAsString(GyartoConstants.GYARTO_KEDVEZMENY));
		to.setMegjegyzes(from
				.getAttributeAsString(GyartoConstants.GYARTO_MEGJEGYZES));
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
