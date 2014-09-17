package hu.dekortrade99.client.order;

import hu.dekortrade99.client.ClientConstants;
import hu.dekortrade99.client.DekorTrade99Service;
import hu.dekortrade99.client.DekorTrade99ServiceAsync;
import hu.dekortrade99.client.GwtRpcDataSource;
import hu.dekortrade99.client.UserInfo;
import hu.dekortrade99.shared.serialized.KosarSer;
import hu.dekortrade99.shared.serialized.SQLExceptionSer;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class KosarDataSource extends GwtRpcDataSource {

	private final DekorTrade99ServiceAsync dekorTrade99Service = GWT
			.create(DekorTrade99Service.class);

	private OrderLabels orderLabels = GWT.create(OrderLabels.class);

	public KosarDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(OrderConstants.KOSAR_ROVIDNEV,
				orderLabels.kosar_rovidnev());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(OrderConstants.KOSAR_CIKKSZAM,
				orderLabels.kosar_cikkszam());
		field.setPrimaryKey(true);
		addField(field);

		field = new DataSourceTextField(OrderConstants.KOSAR_SZINKOD,
				orderLabels.kosar_szinkod());
		field.setPrimaryKey(true);
		addField(field);

		field = new DataSourceIntegerField(OrderConstants.KOSAR_EXPORTKARTON,
				orderLabels.kosar_exportkarton());
		field.setLength(10);
		addField(field);

		field = new DataSourceIntegerField(OrderConstants.KOSAR_KISKARTON,
				orderLabels.kosar_kiskarton());
		field.setLength(10);
		addField(field);

		field = new DataSourceIntegerField(OrderConstants.KOSAR_DARAB,
				orderLabels.kosar_darab());
		field.setLength(10);
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTrade99Service.getKosar(UserInfo.userId,
				new AsyncCallback<ArrayList<KosarSer>>() {
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

					public void onSuccess(ArrayList<KosarSer> result) {
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
		dekorTrade99Service.addKosar(kosarSer, new AsyncCallback<KosarSer>() {
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
				setLastId((result.getCikkszam() == null ? null : result
						.getCikkszam().toString()));
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
		dekorTrade99Service.updateKosar(kosarSer,
				new AsyncCallback<KosarSer>() {
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

					public void onSuccess(KosarSer result) {
						ListGridRecord[] list = new ListGridRecord[1];
						ListGridRecord updRec = new ListGridRecord();
						copyValues(result, updRec);
						list[0] = updRec;
						response.setData(list);
						setLastId((result.getCikkszam() == null ? null : result
								.getCikkszam().toString()));
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
		dekorTrade99Service.removeKosar(kosarSer,
				new AsyncCallback<KosarSer>() {
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

					public void onSuccess(KosarSer result) {
						ListGridRecord[] list = new ListGridRecord[1];
						// We do not receive removed record from server.
						// Return record from request.
						list[0] = rec;
						response.setData(list);
						processResponse(requestId, response);
					}
				});

	}

	private static void copyValues(ListGridRecord from, KosarSer to) {
		to.setRovidnev(UserInfo.userId);
		to.setCikkszam(from.getAttribute(OrderConstants.KOSAR_CIKKSZAM));
		to.setSzinkod(from.getAttribute(OrderConstants.KOSAR_SZINKOD));
		to.setExportkarton(from
				.getAttributeAsInt(OrderConstants.KOSAR_EXPORTKARTON));
		to.setKiskarton(from
				.getAttributeAsInt(OrderConstants.KOSAR_KISKARTON));
		to.setDarab(from
				.getAttributeAsInt(OrderConstants.KOSAR_DARAB));
	}

	private static void copyValues(KosarSer from, ListGridRecord to) {
		to.setAttribute(OrderConstants.KOSAR_ROVIDNEV, from.getRovidnev());
		to.setAttribute(OrderConstants.KOSAR_CIKKSZAM, from.getCikkszam());
		to.setAttribute(OrderConstants.KOSAR_SZINKOD, from.getSzinkod());
		to.setAttribute(OrderConstants.KOSAR_EXPORTKARTON,
				from.getExportkarton());
		to.setAttribute(OrderConstants.KOSAR_KISKARTON,
				from.getKiskarton());
		to.setAttribute(OrderConstants.KOSAR_DARAB,
				from.getDarab());
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
