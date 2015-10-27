package hu.dekortrade.client.lekerdezes.forgalom;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonUtils;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.CikkSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.IsFloatValidator;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CikktorzsDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private ForgalomLabels forgalomLabels = GWT.create(ForgalomLabels.class);

	public CikktorzsDataSource(final LinkedHashMap<String, String> fotipus,
			final LinkedHashMap<String, String> altipus,
			final LinkedHashMap<String, String> gyarto) {

		TextItem textItem = new TextItem();
		textItem.setWidth("300");

		TextAreaItem textAreaItem = new TextAreaItem();
		textAreaItem.setWidth("500");
		textAreaItem.setHeight("60");

		DateItem dateItem = new DateItem();
		dateItem.setUseTextField(true);

		final SelectItem fotipusSelectItem = new SelectItem();
		fotipusSelectItem.setWidth("200");

		final SelectItem altipusSelectItem = new SelectItem();
		altipusSelectItem.setWidth("200");

		final SelectItem gyartoSelectItem = new SelectItem();
		gyartoSelectItem.setWidth("300");

		IsFloatValidator isFloatValidator = new IsFloatValidator();
		IsIntegerValidator isIntegerValidator = new IsIntegerValidator();

		DataSourceField field;

		field = new DataSourceTextField(ForgalomConstants.CIKK_FOTIPUS,
				forgalomLabels.cikk_fotipus());
		field.setEditorProperties(fotipusSelectItem);
		field.setValueMap(fotipus);
		addField(field);

		field = new DataSourceTextField(ForgalomConstants.CIKK_ALTIPUS,
				forgalomLabels.cikk_altipus());
		field.setEditorProperties(altipusSelectItem);
		field.setValueMap(altipus);
		addField(field);

		field = new DataSourceTextField(ForgalomConstants.CIKK_GYARTO,
				forgalomLabels.cikk_gyarto());
		field.setEditorProperties(gyartoSelectItem);
		field.setValueMap(gyarto);
		addField(field);

		field = new DataSourceTextField(ForgalomConstants.CIKK_GYARTOCIKKSZAM,
				forgalomLabels.cikk_gyartocikkszam());
		field.setLength(30);
		addField(field);

		field = new DataSourceTextField(ForgalomConstants.CIKK_CIKKSZAM,
				forgalomLabels.cikk_cikkszam());
		field.setPrimaryKey(true);
		field.setLength(30);
		addField(field);

		field = new DataSourceTextField(ForgalomConstants.CIKK_SZINKOD,
				forgalomLabels.cikk_szinkod());
		field.setLength(10);
		addField(field);

		field = new DataSourceBooleanField(ForgalomConstants.CIKK_AKCIOS,
				forgalomLabels.cikk_akcios());
		addField(field);

		field = new DataSourceTextField(ForgalomConstants.CIKK_MEGNEVEZES,
				forgalomLabels.cikk_megnevezes());
		field.setLength(60);
		field.setEditorProperties(textItem);
		addField(field);

		field = new DataSourceTextField(ForgalomConstants.CIKK_VAMTARIFASZAM,
				forgalomLabels.cikk_vamtarifaszam());
		field.setLength(30);
		addField(field);

		field = new DataSourceDateField(ForgalomConstants.CIKK_FELVITELTOL,
				forgalomLabels.cikk_felviteltol());
		field.setEditorProperties(dateItem);
		addField(field);

		field = new DataSourceDateField(ForgalomConstants.CIKK_FELVITELIG,
				forgalomLabels.cikk_felvitelig());
		field.setEditorProperties(dateItem);
		addField(field);

		field = new DataSourceDateField(ForgalomConstants.CIKK_LEJARATTOL,
				forgalomLabels.cikk_lejarattol());
		field.setEditorProperties(dateItem);
		addField(field);

		field = new DataSourceDateField(ForgalomConstants.CIKK_LEJARATIG,
				forgalomLabels.cikk_lejaratig());
		field.setEditorProperties(dateItem);
		addField(field);

		field = new DataSourceFloatField(ForgalomConstants.CIKK_FOB,
				forgalomLabels.cikk_fob());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(ForgalomConstants.CIKK_UJFOB,
				forgalomLabels.cikk_ujfob());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(ForgalomConstants.CIKK_SZALLITAS,
				forgalomLabels.cikk_szallitas());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(ForgalomConstants.CIKK_UJSZALLITAS,
				forgalomLabels.cikk_ujszallitas());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(ForgalomConstants.CIKK_DDU,
				forgalomLabels.cikk_ddu());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(ForgalomConstants.CIKK_UJDDU,
				forgalomLabels.cikk_ujddu());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(ForgalomConstants.CIKK_ERSZ,
				forgalomLabels.cikk_ersz());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(ForgalomConstants.CIKK_UJERSZ,
				forgalomLabels.cikk_ujersz());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(ForgalomConstants.CIKK_ELORAR,
				forgalomLabels.cikk_elorar());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(ForgalomConstants.CIKK_UJELORAR,
				forgalomLabels.cikk_ujelorar());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(ForgalomConstants.CIKK_AR,
				forgalomLabels.cikk_ar());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(ForgalomConstants.CIKK_AREUR,
				forgalomLabels.cikk_areur());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(ForgalomConstants.CIKK_ARSZORZO,
				forgalomLabels.cikk_arszorzo());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceIntegerField(ForgalomConstants.CIKK_KISKARTON,
				forgalomLabels.cikk_kiskarton());
		field.setLength(10);
		field.setValidators(isIntegerValidator);
		addField(field);

		field = new DataSourceIntegerField(ForgalomConstants.CIKK_DARAB,
				forgalomLabels.cikk_darab());
		field.setLength(10);
		field.setValidators(isIntegerValidator);
		addField(field);

		field = new DataSourceTextField(ForgalomConstants.CIKK_MERTEKEGYSEG,
				forgalomLabels.cikk_mertekegyseg());
		field.setValueMap(ForgalomConstants.getMertekegyseg());
		addField(field);

		field = new DataSourceFloatField(ForgalomConstants.CIKK_TERFOGAT,
				forgalomLabels.cikk_terfogat());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(ForgalomConstants.CIKK_TERFOGATLAB,
				forgalomLabels.cikk_terfogatlab());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(ForgalomConstants.CIKK_BSULY,
				forgalomLabels.cikk_bsuly());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(ForgalomConstants.CIKK_NSULY,
				forgalomLabels.cikk_nsuly());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceTextField(ForgalomConstants.CIKK_LEIRAS,
				forgalomLabels.cikk_leiras());
		field.setLength(200);
		field.setEditorProperties(textAreaItem);
		addField(field);

		field = new DataSourceTextField(ForgalomConstants.CIKK_MEGJEGYZES,
				forgalomLabels.cikk_megjegyzes());
		field.setLength(200);
		field.setEditorProperties(textAreaItem);
		addField(field);

		field = new DataSourceIntegerField(ForgalomConstants.CIKK_KEPEK,
				forgalomLabels.cikk_kepek());
		field.setCanEdit(false);
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getCikk(
				request.getCriteria().getAttributeAsInt(
						ForgalomConstants.CTORZS_PAGE),
				request.getCriteria().getAttributeAsString(
						ForgalomConstants.CIKK_FOTIPUS),
				request.getCriteria().getAttributeAsString(
						ForgalomConstants.CIKK_ALTIPUS),
				request.getCriteria().getAttributeAsString(
						ForgalomConstants.CIKK_CIKKSZAM),
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
		dekorTradeService.updateCikk(cikkSer, new AsyncCallback<CikkSer>() {
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
		dekorTradeService.removeCikk(cikkSer, new AsyncCallback<CikkSer>() {
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
		to.setAttribute(ForgalomConstants.CIKK_FOTIPUS, from.getFotipus());
		to.setAttribute(ForgalomConstants.CIKK_ALTIPUS, from.getAltipus());
		to.setAttribute(ForgalomConstants.CIKK_GYARTO, from.getGyarto());
		to.setAttribute(ForgalomConstants.CIKK_GYARTOCIKKSZAM,
				from.getGyartocikkszam());
		to.setAttribute(ForgalomConstants.CIKK_CIKKSZAM, from.getCikkszam());
		to.setAttribute(ForgalomConstants.CIKK_SZINKOD, from.getSzinkod());
		to.setAttribute(ForgalomConstants.CIKK_FELVITELTOL,
				from.getFelviteltol());
		to.setAttribute(ForgalomConstants.CIKK_FELVITELIG,
				from.getFelvitelig());
		to.setAttribute(ForgalomConstants.CIKK_LEJARATTOL,
				from.getLejarattol());
		to.setAttribute(ForgalomConstants.CIKK_LEJARATIG, from.getLejaratig());
		to.setAttribute(ForgalomConstants.CIKK_MEGNEVEZES,
				from.getMegnevezes());
		to.setAttribute(ForgalomConstants.CIKK_VAMTARIFASZAM,
				from.getVamtarifaszam());
		to.setAttribute(ForgalomConstants.CIKK_FOB, from.getFob());
		to.setAttribute(ForgalomConstants.CIKK_SZALLITAS, from.getSzallitas());
		to.setAttribute(ForgalomConstants.CIKK_DDU, from.getDdu());
		to.setAttribute(ForgalomConstants.CIKK_ERSZ, from.getErsz());
		to.setAttribute(ForgalomConstants.CIKK_ELORAR, from.getElorar());
		to.setAttribute(ForgalomConstants.CIKK_UJFOB, from.getUjfob());
		to.setAttribute(ForgalomConstants.CIKK_UJSZALLITAS,
				from.getUjszallitas());
		to.setAttribute(ForgalomConstants.CIKK_UJDDU, from.getUjddu());
		to.setAttribute(ForgalomConstants.CIKK_UJERSZ, from.getUjersz());
		to.setAttribute(ForgalomConstants.CIKK_UJELORAR, from.getUjelorar());
		to.setAttribute(ForgalomConstants.CIKK_AR, from.getAr());
		to.setAttribute(ForgalomConstants.CIKK_AREUR, from.getAreur());
		to.setAttribute(ForgalomConstants.CIKK_ARSZORZO, from.getArszorzo());
		to.setAttribute(ForgalomConstants.CIKK_KISKARTON, from.getKiskarton());
		to.setAttribute(ForgalomConstants.CIKK_DARAB, from.getDarab());
		to.setAttribute(ForgalomConstants.CIKK_MERTEKEGYSEG,
				from.getMertekegyseg());
		to.setAttribute(ForgalomConstants.CIKK_TERFOGAT, from.getTerfogat());
		to.setAttribute(ForgalomConstants.CIKK_TERFOGATLAB,
				from.getTerfogatlab());
		to.setAttribute(ForgalomConstants.CIKK_BSULY, from.getBsuly());
		to.setAttribute(ForgalomConstants.CIKK_NSULY, from.getNsuly());
		to.setAttribute(ForgalomConstants.CIKK_LEIRAS, from.getLeiras());
		to.setAttribute(ForgalomConstants.CIKK_MEGJEGYZES,
				from.getMegjegyzes());
		to.setAttribute(ForgalomConstants.CIKK_KEPEK, from.getKepek());
	}

	private static void copyValues(ListGridRecord from, CikkSer to) {
		to.setFotipus(from
				.getAttributeAsString(ForgalomConstants.CIKK_FOTIPUS));
		to.setAltipus(from
				.getAttributeAsString(ForgalomConstants.CIKK_ALTIPUS));
		to.setGyarto(from.getAttributeAsString(ForgalomConstants.CIKK_GYARTO));
		to.setGyartocikkszam(from
				.getAttributeAsString(ForgalomConstants.CIKK_GYARTOCIKKSZAM));
		to.setCikkszam(from
				.getAttributeAsString(ForgalomConstants.CIKK_CIKKSZAM));
		to.setSzinkod(from
				.getAttributeAsString(ForgalomConstants.CIKK_SZINKOD));
		to.setFelviteltol(from
				.getAttributeAsDate(ForgalomConstants.CIKK_FELVITELTOL));
		to.setFelvitelig(from
				.getAttributeAsDate(ForgalomConstants.CIKK_FELVITELIG));
		to.setLejarattol(from
				.getAttributeAsDate(ForgalomConstants.CIKK_LEJARATTOL));
		to.setLejaratig(from
				.getAttributeAsDate(ForgalomConstants.CIKK_LEJARATIG));
		to.setMegnevezes(from
				.getAttributeAsString(ForgalomConstants.CIKK_MEGNEVEZES));
		to.setVamtarifaszam(from
				.getAttributeAsString(ForgalomConstants.CIKK_VAMTARIFASZAM));
		to.setFob(CommonUtils.doubleCheck(from.getAttributeAsDouble(ForgalomConstants.CIKK_FOB)));
		to.setSzallitas(CommonUtils.doubleCheck(from
				.getAttributeAsDouble(ForgalomConstants.CIKK_SZALLITAS)));
		to.setDdu(CommonUtils.doubleCheck(from.getAttributeAsDouble(ForgalomConstants.CIKK_DDU)));
		to.setErsz(CommonUtils.doubleCheck(from.getAttributeAsDouble(ForgalomConstants.CIKK_ERSZ)));
		to.setElorar(CommonUtils.doubleCheck(from.getAttributeAsDouble(ForgalomConstants.CIKK_ELORAR)));
		to.setUjfob(CommonUtils.doubleCheck(from.getAttributeAsDouble(ForgalomConstants.CIKK_UJFOB)));
		to.setUjszallitas(CommonUtils.doubleCheck(from
				.getAttributeAsDouble(ForgalomConstants.CIKK_UJSZALLITAS)));
		to.setUjddu(CommonUtils.doubleCheck(from.getAttributeAsDouble(ForgalomConstants.CIKK_UJDDU)));
		to.setUjersz(CommonUtils.doubleCheck(from.getAttributeAsDouble(ForgalomConstants.CIKK_UJERSZ)));
		to.setUjelorar(CommonUtils.doubleCheck(from
				.getAttributeAsDouble(ForgalomConstants.CIKK_UJELORAR)));
		to.setAr(CommonUtils.doubleCheck(from.getAttributeAsDouble(ForgalomConstants.CIKK_AR)));
		to.setAreur(CommonUtils.doubleCheck(from.getAttributeAsDouble(ForgalomConstants.CIKK_AREUR)));
		to.setArszorzo(CommonUtils.doubleCheck(from
				.getAttributeAsDouble(ForgalomConstants.CIKK_ARSZORZO)));
		to.setKiskarton(CommonUtils.intCheck(from
				.getAttributeAsInt(ForgalomConstants.CIKK_KISKARTON)));
		to.setDarab(CommonUtils.intCheck(from.getAttributeAsInt(ForgalomConstants.CIKK_DARAB)));
		to.setMertekegyseg(from
				.getAttributeAsString(ForgalomConstants.CIKK_MERTEKEGYSEG));
		to.setTerfogat(CommonUtils.doubleCheck(from
				.getAttributeAsDouble(ForgalomConstants.CIKK_TERFOGAT)));
		to.setTerfogatlab(CommonUtils.doubleCheck(from
				.getAttributeAsDouble(ForgalomConstants.CIKK_TERFOGATLAB)));
		to.setBsuly(CommonUtils.doubleCheck(from.getAttributeAsDouble(ForgalomConstants.CIKK_BSULY)));
		to.setNsuly(CommonUtils.doubleCheck(from.getAttributeAsDouble(ForgalomConstants.CIKK_NSULY)));
		to.setLeiras(from.getAttributeAsString(ForgalomConstants.CIKK_LEIRAS));
		to.setMegjegyzes(from
				.getAttributeAsString(ForgalomConstants.CIKK_MEGJEGYZES));
		to.setKepek(CommonUtils.intCheck(from.getAttributeAsInt(ForgalomConstants.CIKK_KEPEK)));
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
