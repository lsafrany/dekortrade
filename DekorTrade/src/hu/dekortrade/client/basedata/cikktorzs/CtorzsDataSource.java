package hu.dekortrade.client.basedata.cikktorzs;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.CikkSer;
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

		field = new DataSourceTextField(CtorzsConstants.CIKK_CIKKSZAM,
				ctorzsLabels.cikk_cikkszam());
		field.setPrimaryKey(true);
		field.setLength(30);
		addField(field);

		field = new DataSourceTextField(CtorzsConstants.CIKK_MEGNEVEZES,
				ctorzsLabels.cikk_megnevezes());
		field.setLength(60);
		field.setEditorProperties(textItem);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_AR,
				ctorzsLabels.cikk_ar());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceIntegerField(CtorzsConstants.CIKK_KISKARTON,
				ctorzsLabels.cikk_kiskarton());
		field.setLength(10);
		field.setValidators(isIntegerValidator);
		addField(field);

		field = new DataSourceIntegerField(CtorzsConstants.CIKK_DARAB,
				ctorzsLabels.cikk_darab());
		field.setLength(10);
		field.setValidators(isIntegerValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_TERFOGAT,
				ctorzsLabels.cikk_terfogat());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceTextField(CtorzsConstants.CIKK_JEL,
				ctorzsLabels.cikk_jel());
		field.setEditorProperties(selectItem);
		field.setValueMap(ClientConstants.getJelek());
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_BSULY,
				ctorzsLabels.cikk_bsuly());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_NSULY,
				ctorzsLabels.cikk_nsuly());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceIntegerField(CtorzsConstants.CIKK_KEPEK,
				ctorzsLabels.cikk_kepek());
		field.setCanEdit(false);
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getCikk(
				request.getCriteria().getAttributeAsInt(
						CtorzsConstants.CTORZS_PAGE),
				request.getCriteria().getAttributeAsString(
						CtorzsConstants.CIKK_CIKKSZAM),
				request.getCriteria().getAttributeAsString(
						CtorzsConstants.CIKK_JEL),
				new AsyncCallback<List<CikkSer>>() {
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

					public void onSuccess(List<CikkSer> result) {
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
		CikkSer cikkSer = new CikkSer();
		copyValues(rec, cikkSer);
		dekorTradeService.addCikk(cikkSer, new AsyncCallback<CikkSer>() {
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

			public void onSuccess(CikkSer result) {
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
		CikkSer cikkSer = new CikkSer();
		copyValues(rec, cikkSer);
		dekorTradeService.updateCikk(cikkSer,
				new AsyncCallback<CikkSer>() {
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

					public void onSuccess(CikkSer result) {
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
		CikkSer cikkSer = new CikkSer();
		copyValues(rec, cikkSer);
		dekorTradeService.removeCikk(cikkSer,
				new AsyncCallback<CikkSer>() {
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

					public void onSuccess(CikkSer result) {
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

	private static void copyValues(CikkSer from, ListGridRecord to) {
		to.setAttribute(CtorzsConstants.CIKK_CIKKSZAM, from.getCikkszam());
		to.setAttribute(CtorzsConstants.CIKK_MEGNEVEZES, from.getMegnevezes());
		to.setAttribute(CtorzsConstants.CIKK_AR, from.getAr());
		to.setAttribute(CtorzsConstants.CIKK_KISKARTON, from.getKiskarton());
		to.setAttribute(CtorzsConstants.CIKK_DARAB, from.getDarab());
		to.setAttribute(CtorzsConstants.CIKK_TERFOGAT, from.getTerfogat());
		to.setAttribute(CtorzsConstants.CIKK_JEL, from.getJel());
		to.setAttribute(CtorzsConstants.CIKK_BSULY, from.getBsuly());
		to.setAttribute(CtorzsConstants.CIKK_NSULY, from.getNsuly());
		to.setAttribute(CtorzsConstants.CIKK_KEPEK, from.getKepek());
	}

	private static void copyValues(ListGridRecord from, CikkSer to) {
		to.setCikkszam(from
				.getAttributeAsString(CtorzsConstants.CIKK_CIKKSZAM));
		to.setMegnevezes(from
				.getAttributeAsString(CtorzsConstants.CIKK_MEGNEVEZES));
		to.setAr(from.getAttributeAsDouble(CtorzsConstants.CIKK_AR));
		to.setKiskarton(from
				.getAttributeAsInt(CtorzsConstants.CIKK_KISKARTON));
		to.setDarab(from.getAttributeAsInt(CtorzsConstants.CIKK_DARAB));
		to.setTerfogat(from
				.getAttributeAsDouble(CtorzsConstants.CIKK_TERFOGAT));
		to.setJel(from.getAttributeAsString(CtorzsConstants.CIKK_JEL));
		to.setBsuly(from
				.getAttributeAsDouble(CtorzsConstants.CIKK_BSULY));
		to.setNsuly(from
				.getAttributeAsDouble(CtorzsConstants.CIKK_NSULY));
		to.setKepek(from
				.getAttributeAsInt(CtorzsConstants.CIKK_KEPEK));	
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
