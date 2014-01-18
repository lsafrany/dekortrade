package hu.dekortrade.client.basedata.cikktorzs;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.CtorzsSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.math.BigDecimal;
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
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.IsFloatValidator;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CtorzsDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private CtorzsLabels ctorzsLabels = GWT.create(CtorzsLabels.class);

	public CtorzsDataSource() {

		TextItem textItem = new TextItem();
		textItem.setWidth("400");

		final SelectItem selectItem = new SelectItem();
		selectItem.setValueMap(ClientConstants.getJelek());
		selectItem.setWidth("200");

		IsFloatValidator isFloatValidator = new IsFloatValidator();
		IsIntegerValidator isIntegerValidator = new IsIntegerValidator();

		DataSourceField field;

		field = new DataSourceTextField(CtorzsConstants.CTORZS_CIKKSZAM,
				ctorzsLabels.ctorzs_cikkszam());
		field.setPrimaryKey(true);
		field.setLength(30);
		addField(field);

		field = new DataSourceTextField(CtorzsConstants.CTORZS_MEGNEVEZES,
				ctorzsLabels.ctorzs_megnevezes());
		field.setLength(60);
		field.setEditorProperties(textItem);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CTORZS_AR,
				ctorzsLabels.ctorzs_ar());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceIntegerField(CtorzsConstants.CTORZS_KISKARTON,
				ctorzsLabels.ctorzs_kiskarton());
		field.setLength(10);
		field.setValidators(isIntegerValidator);
		addField(field);

		field = new DataSourceIntegerField(CtorzsConstants.CTORZS_DARAB,
				ctorzsLabels.ctorzs_darab());
		field.setLength(10);
		field.setValidators(isIntegerValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CTORZS_TERFOGAT,
				ctorzsLabels.ctorzs_terfogat());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceTextField(CtorzsConstants.CTORZS_JEL,
				ctorzsLabels.ctorzs_jel());
		field.setEditorProperties(selectItem);
		field.setValueMap(ClientConstants.getJelek());
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CTORZS_BSULY,
				ctorzsLabels.ctorzs_bsuly());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CTORZS_NSULY,
				ctorzsLabels.ctorzs_nsuly());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CTORZS_KEPEK,
				ctorzsLabels.ctorzs_kepek());
		field.setHidden(true);
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getCtorzs(
				request.getCriteria().getAttributeAsInt(
						CtorzsConstants.CTORZS_PAGE),
				request.getCriteria().getAttributeAsString(
						CtorzsConstants.CTORZS_CIKKSZAM),
				request.getCriteria().getAttributeAsString(
						CtorzsConstants.CTORZS_JEL),
				new AsyncCallback<List<CtorzsSer>>() {
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

					public void onSuccess(List<CtorzsSer> result) {
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
		CtorzsSer ctorzsSer = new CtorzsSer();
		copyValues(rec, ctorzsSer);
		dekorTradeService.addCtorzs(ctorzsSer, new AsyncCallback<CtorzsSer>() {
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

			public void onSuccess(CtorzsSer result) {
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
		CtorzsSer ctorzsSer = new CtorzsSer();
		copyValues(rec, ctorzsSer);
		dekorTradeService.updateCtorzs(ctorzsSer,
				new AsyncCallback<CtorzsSer>() {
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

					public void onSuccess(CtorzsSer result) {
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
		CtorzsSer ctorzsSer = new CtorzsSer();
		copyValues(rec, ctorzsSer);
		dekorTradeService.removeCtorzs(ctorzsSer,
				new AsyncCallback<CtorzsSer>() {
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

					public void onSuccess(CtorzsSer result) {
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

	private static void copyValues(CtorzsSer from, ListGridRecord to) {
		to.setAttribute(CtorzsConstants.CTORZS_CIKKSZAM, from.getCikkszam());
		to.setAttribute(CtorzsConstants.CTORZS_MEGNEVEZES, from.getMegnevezes());
		to.setAttribute(CtorzsConstants.CTORZS_AR, from.getAr());
		to.setAttribute(CtorzsConstants.CTORZS_KISKARTON, from.getKiskarton());
		to.setAttribute(CtorzsConstants.CTORZS_DARAB, from.getDarab());
		to.setAttribute(CtorzsConstants.CTORZS_TERFOGAT, from.getTerfogat());
		to.setAttribute(CtorzsConstants.CTORZS_JEL, from.getJel());
		to.setAttribute(CtorzsConstants.CTORZS_BSULY, from.getBsuly());
		to.setAttribute(CtorzsConstants.CTORZS_NSULY, from.getNsuly());
		to.setAttribute(CtorzsConstants.CTORZS_KEPEK, from.getKepek());
	}

	private static void copyValues(ListGridRecord from, CtorzsSer to) {
		to.setCikkszam(from
				.getAttributeAsString(CtorzsConstants.CTORZS_CIKKSZAM));
		to.setMegnevezes(from
				.getAttributeAsString(CtorzsConstants.CTORZS_MEGNEVEZES));
		to.setAr(new BigDecimal(from
				.getAttributeAsDouble(CtorzsConstants.CTORZS_AR)));
		to.setKiskarton(from
				.getAttributeAsInt(CtorzsConstants.CTORZS_KISKARTON));
		to.setDarab(from.getAttributeAsInt(CtorzsConstants.CTORZS_DARAB));
		to.setTerfogat(new BigDecimal(from
				.getAttributeAsDouble(CtorzsConstants.CTORZS_TERFOGAT)));
		to.setJel(from.getAttributeAsString(CtorzsConstants.CTORZS_JEL));
		to.setBsuly(new BigDecimal(from
				.getAttributeAsDouble(CtorzsConstants.CTORZS_BSULY)));
		to.setNsuly(new BigDecimal(from
				.getAttributeAsDouble(CtorzsConstants.CTORZS_NSULY)));
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
