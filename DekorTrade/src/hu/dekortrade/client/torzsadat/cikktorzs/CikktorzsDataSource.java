package hu.dekortrade.client.torzsadat.cikktorzs;

import hu.dekortrade.client.ClientConstants;
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

	private CikktorzsLabels ctorzsLabels = GWT.create(CikktorzsLabels.class);

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

		field = new DataSourceTextField(CikktorzsConstants.CIKK_FOTIPUS,
				ctorzsLabels.cikk_fotipus());
		field.setEditorProperties(fotipusSelectItem);
		field.setValueMap(fotipus);
		addField(field);

		field = new DataSourceTextField(CikktorzsConstants.CIKK_ALTIPUS,
				ctorzsLabels.cikk_altipus());
		field.setEditorProperties(altipusSelectItem);
		field.setValueMap(altipus);
		addField(field);

		field = new DataSourceTextField(CikktorzsConstants.CIKK_GYARTO,
				ctorzsLabels.cikk_gyarto());
		field.setEditorProperties(gyartoSelectItem);
		field.setValueMap(gyarto);
		addField(field);

		field = new DataSourceTextField(CikktorzsConstants.CIKK_GYARTOCIKKSZAM,
				ctorzsLabels.cikk_gyartocikkszam());
		field.setLength(30);
		addField(field);

		field = new DataSourceTextField(CikktorzsConstants.CIKK_CIKKSZAM,
				ctorzsLabels.cikk_cikkszam());
		field.setPrimaryKey(true);
		field.setLength(30);
		addField(field);

		field = new DataSourceTextField(CikktorzsConstants.CIKK_SZINKOD,
				ctorzsLabels.cikk_szinkod());
		field.setLength(10);
		addField(field);

		field = new DataSourceBooleanField(CikktorzsConstants.CIKK_AKCIOS,
				ctorzsLabels.cikk_akcios());
		addField(field);

		field = new DataSourceTextField(CikktorzsConstants.CIKK_MEGNEVEZES,
				ctorzsLabels.cikk_megnevezes());
		field.setLength(60);
		field.setEditorProperties(textItem);
		addField(field);

		field = new DataSourceTextField(CikktorzsConstants.CIKK_VAMTARIFASZAM,
				ctorzsLabels.cikk_vamtarifaszam());
		field.setLength(30);
		addField(field);

		field = new DataSourceDateField(CikktorzsConstants.CIKK_FELVITELTOL,
				ctorzsLabels.cikk_felviteltol());
		field.setEditorProperties(dateItem);
		addField(field);

		field = new DataSourceDateField(CikktorzsConstants.CIKK_FELVITELIG,
				ctorzsLabels.cikk_felvitelig());
		field.setEditorProperties(dateItem);
		addField(field);

		field = new DataSourceDateField(CikktorzsConstants.CIKK_LEJARATTOL,
				ctorzsLabels.cikk_lejarattol());
		field.setEditorProperties(dateItem);
		addField(field);

		field = new DataSourceDateField(CikktorzsConstants.CIKK_LEJARATIG,
				ctorzsLabels.cikk_lejaratig());
		field.setEditorProperties(dateItem);
		addField(field);

		field = new DataSourceFloatField(CikktorzsConstants.CIKK_FOB,
				ctorzsLabels.cikk_fob());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CikktorzsConstants.CIKK_UJFOB,
				ctorzsLabels.cikk_ujfob());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CikktorzsConstants.CIKK_SZALLITAS,
				ctorzsLabels.cikk_szallitas());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CikktorzsConstants.CIKK_UJSZALLITAS,
				ctorzsLabels.cikk_ujszallitas());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CikktorzsConstants.CIKK_DDU,
				ctorzsLabels.cikk_ddu());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CikktorzsConstants.CIKK_UJDDU,
				ctorzsLabels.cikk_ujddu());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CikktorzsConstants.CIKK_ERSZ,
				ctorzsLabels.cikk_ersz());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CikktorzsConstants.CIKK_UJERSZ,
				ctorzsLabels.cikk_ujersz());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CikktorzsConstants.CIKK_ELORAR,
				ctorzsLabels.cikk_elorar());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CikktorzsConstants.CIKK_UJELORAR,
				ctorzsLabels.cikk_ujelorar());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CikktorzsConstants.CIKK_AR,
				ctorzsLabels.cikk_ar());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CikktorzsConstants.CIKK_AREUR,
				ctorzsLabels.cikk_areur());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CikktorzsConstants.CIKK_ARSZORZO,
				ctorzsLabels.cikk_arszorzo());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceIntegerField(CikktorzsConstants.CIKK_KISKARTON,
				ctorzsLabels.cikk_kiskarton());
		field.setLength(10);
		field.setValidators(isIntegerValidator);
		addField(field);

		field = new DataSourceIntegerField(CikktorzsConstants.CIKK_DARAB,
				ctorzsLabels.cikk_darab());
		field.setLength(10);
		field.setValidators(isIntegerValidator);
		addField(field);

		field = new DataSourceTextField(CikktorzsConstants.CIKK_MERTEKEGYSEG,
				ctorzsLabels.cikk_mertekegyseg());
		field.setValueMap(CikktorzsConstants.getMertekegyseg());
		addField(field);

		field = new DataSourceFloatField(CikktorzsConstants.CIKK_TERFOGAT,
				ctorzsLabels.cikk_terfogat());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CikktorzsConstants.CIKK_TERFOGATLAB,
				ctorzsLabels.cikk_terfogatlab());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CikktorzsConstants.CIKK_BSULY,
				ctorzsLabels.cikk_bsuly());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CikktorzsConstants.CIKK_NSULY,
				ctorzsLabels.cikk_nsuly());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceTextField(CikktorzsConstants.CIKK_LEIRAS,
				ctorzsLabels.cikk_leiras());
		field.setLength(200);
		field.setEditorProperties(textAreaItem);
		addField(field);

		field = new DataSourceTextField(CikktorzsConstants.CIKK_MEGJEGYZES,
				ctorzsLabels.cikk_megjegyzes());
		field.setLength(200);
		field.setEditorProperties(textAreaItem);
		addField(field);

		field = new DataSourceIntegerField(CikktorzsConstants.CIKK_KEPEK,
				ctorzsLabels.cikk_kepek());
		field.setCanEdit(false);
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getCikk(
				request.getCriteria().getAttributeAsInt(
						CikktorzsConstants.CTORZS_PAGE),
				request.getCriteria().getAttributeAsString(
						CikktorzsConstants.CIKK_FOTIPUS),
				request.getCriteria().getAttributeAsString(
						CikktorzsConstants.CIKK_ALTIPUS),
				request.getCriteria().getAttributeAsString(
						CikktorzsConstants.CIKK_CIKKSZAM),
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
		to.setAttribute(CikktorzsConstants.CIKK_FOTIPUS, from.getFotipus());
		to.setAttribute(CikktorzsConstants.CIKK_ALTIPUS, from.getAltipus());
		to.setAttribute(CikktorzsConstants.CIKK_GYARTO, from.getGyarto());
		to.setAttribute(CikktorzsConstants.CIKK_GYARTOCIKKSZAM,
				from.getGyartocikkszam());
		to.setAttribute(CikktorzsConstants.CIKK_CIKKSZAM, from.getCikkszam());
		to.setAttribute(CikktorzsConstants.CIKK_SZINKOD, from.getSzinkod());
		to.setAttribute(CikktorzsConstants.CIKK_FELVITELTOL,
				from.getFelviteltol());
		to.setAttribute(CikktorzsConstants.CIKK_FELVITELIG,
				from.getFelvitelig());
		to.setAttribute(CikktorzsConstants.CIKK_LEJARATTOL,
				from.getLejarattol());
		to.setAttribute(CikktorzsConstants.CIKK_LEJARATIG, from.getLejaratig());
		to.setAttribute(CikktorzsConstants.CIKK_MEGNEVEZES,
				from.getMegnevezes());
		to.setAttribute(CikktorzsConstants.CIKK_VAMTARIFASZAM,
				from.getVamtarifaszam());
		to.setAttribute(CikktorzsConstants.CIKK_FOB, from.getFob());
		to.setAttribute(CikktorzsConstants.CIKK_SZALLITAS, from.getSzallitas());
		to.setAttribute(CikktorzsConstants.CIKK_DDU, from.getDdu());
		to.setAttribute(CikktorzsConstants.CIKK_ERSZ, from.getErsz());
		to.setAttribute(CikktorzsConstants.CIKK_ELORAR, from.getElorar());
		to.setAttribute(CikktorzsConstants.CIKK_UJFOB, from.getUjfob());
		to.setAttribute(CikktorzsConstants.CIKK_UJSZALLITAS,
				from.getUjszallitas());
		to.setAttribute(CikktorzsConstants.CIKK_UJDDU, from.getUjddu());
		to.setAttribute(CikktorzsConstants.CIKK_UJERSZ, from.getUjersz());
		to.setAttribute(CikktorzsConstants.CIKK_UJELORAR, from.getUjelorar());
		to.setAttribute(CikktorzsConstants.CIKK_AR, from.getAr());
		to.setAttribute(CikktorzsConstants.CIKK_AREUR, from.getAreur());
		to.setAttribute(CikktorzsConstants.CIKK_ARSZORZO, from.getArszorzo());
		to.setAttribute(CikktorzsConstants.CIKK_KISKARTON, from.getKiskarton());
		to.setAttribute(CikktorzsConstants.CIKK_DARAB, from.getDarab());
		to.setAttribute(CikktorzsConstants.CIKK_MERTEKEGYSEG,
				from.getMertekegyseg());
		to.setAttribute(CikktorzsConstants.CIKK_TERFOGAT, from.getTerfogat());
		to.setAttribute(CikktorzsConstants.CIKK_TERFOGATLAB,
				from.getTerfogatlab());
		to.setAttribute(CikktorzsConstants.CIKK_BSULY, from.getBsuly());
		to.setAttribute(CikktorzsConstants.CIKK_NSULY, from.getNsuly());
		to.setAttribute(CikktorzsConstants.CIKK_LEIRAS, from.getLeiras());
		to.setAttribute(CikktorzsConstants.CIKK_MEGJEGYZES,
				from.getMegjegyzes());
		to.setAttribute(CikktorzsConstants.CIKK_KEPEK, from.getKepek());
	}

	private static void copyValues(ListGridRecord from, CikkSer to) {
		to.setFotipus(from
				.getAttributeAsString(CikktorzsConstants.CIKK_FOTIPUS));
		to.setAltipus(from
				.getAttributeAsString(CikktorzsConstants.CIKK_ALTIPUS));
		to.setGyarto(from.getAttributeAsString(CikktorzsConstants.CIKK_GYARTO));
		to.setGyartocikkszam(from
				.getAttributeAsString(CikktorzsConstants.CIKK_GYARTOCIKKSZAM));
		to.setCikkszam(from
				.getAttributeAsString(CikktorzsConstants.CIKK_CIKKSZAM));
		to.setSzinkod(from
				.getAttributeAsString(CikktorzsConstants.CIKK_SZINKOD));
		to.setFelviteltol(from
				.getAttributeAsDate(CikktorzsConstants.CIKK_FELVITELTOL));
		to.setFelvitelig(from
				.getAttributeAsDate(CikktorzsConstants.CIKK_FELVITELIG));
		to.setLejarattol(from
				.getAttributeAsDate(CikktorzsConstants.CIKK_LEJARATTOL));
		to.setLejaratig(from
				.getAttributeAsDate(CikktorzsConstants.CIKK_LEJARATIG));
		to.setMegnevezes(from
				.getAttributeAsString(CikktorzsConstants.CIKK_MEGNEVEZES));
		to.setVamtarifaszam(from
				.getAttributeAsString(CikktorzsConstants.CIKK_VAMTARIFASZAM));
		to.setFob(from.getAttributeAsFloat(CikktorzsConstants.CIKK_FOB));
		to.setSzallitas(from
				.getAttributeAsFloat(CikktorzsConstants.CIKK_SZALLITAS));
		to.setDdu(from.getAttributeAsFloat(CikktorzsConstants.CIKK_DDU));
		to.setErsz(from.getAttributeAsFloat(CikktorzsConstants.CIKK_ERSZ));
		to.setElorar(from.getAttributeAsFloat(CikktorzsConstants.CIKK_ELORAR));
		to.setUjfob(from.getAttributeAsFloat(CikktorzsConstants.CIKK_UJFOB));
		to.setUjszallitas(from
				.getAttributeAsFloat(CikktorzsConstants.CIKK_UJSZALLITAS));
		to.setUjddu(from.getAttributeAsFloat(CikktorzsConstants.CIKK_UJDDU));
		to.setUjersz(from.getAttributeAsFloat(CikktorzsConstants.CIKK_UJERSZ));
		to.setUjelorar(from
				.getAttributeAsFloat(CikktorzsConstants.CIKK_UJELORAR));
		to.setAr(from.getAttributeAsFloat(CikktorzsConstants.CIKK_AR));
		to.setAreur(from.getAttributeAsFloat(CikktorzsConstants.CIKK_AREUR));
		to.setArszorzo(from
				.getAttributeAsFloat(CikktorzsConstants.CIKK_ARSZORZO));
		to.setKiskarton(from
				.getAttributeAsInt(CikktorzsConstants.CIKK_KISKARTON));
		to.setDarab(from.getAttributeAsInt(CikktorzsConstants.CIKK_DARAB));
		to.setMertekegyseg(from
				.getAttributeAsString(CikktorzsConstants.CIKK_MERTEKEGYSEG));
		to.setTerfogat(from
				.getAttributeAsFloat(CikktorzsConstants.CIKK_TERFOGAT));
		to.setTerfogatlab(from
				.getAttributeAsFloat(CikktorzsConstants.CIKK_TERFOGATLAB));
		to.setBsuly(from.getAttributeAsFloat(CikktorzsConstants.CIKK_BSULY));
		to.setNsuly(from.getAttributeAsFloat(CikktorzsConstants.CIKK_NSULY));
		to.setLeiras(from.getAttributeAsString(CikktorzsConstants.CIKK_LEIRAS));
		to.setMegjegyzes(from
				.getAttributeAsString(CikktorzsConstants.CIKK_MEGJEGYZES));
		to.setKepek(from.getAttributeAsInt(CikktorzsConstants.CIKK_KEPEK));
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
