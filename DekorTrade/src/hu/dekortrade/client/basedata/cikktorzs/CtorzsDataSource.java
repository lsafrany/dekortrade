package hu.dekortrade.client.basedata.cikktorzs;

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

public class CtorzsDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private CtorzsLabels ctorzsLabels = GWT.create(CtorzsLabels.class);

	public CtorzsDataSource(final LinkedHashMap<String, String> fotipus,
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

		field = new DataSourceTextField(CtorzsConstants.CIKK_FOTIPUS,
				ctorzsLabels.cikk_fotipus());
		field.setEditorProperties(fotipusSelectItem);
		field.setValueMap(fotipus);
		addField(field);

		field = new DataSourceTextField(CtorzsConstants.CIKK_ALTIPUS,
				ctorzsLabels.cikk_altipus());
		field.setEditorProperties(altipusSelectItem);
		field.setValueMap(altipus);
		addField(field);

		field = new DataSourceTextField(CtorzsConstants.CIKK_GYARTO,
				ctorzsLabels.cikk_gyarto());
		field.setEditorProperties(gyartoSelectItem);
		field.setValueMap(gyarto);
		addField(field);

		field = new DataSourceTextField(CtorzsConstants.CIKK_GYARTOCIKKSZAM,
				ctorzsLabels.cikk_gyartocikkszam());
		field.setLength(30);
		addField(field);

		field = new DataSourceTextField(CtorzsConstants.CIKK_CIKKSZAM,
				ctorzsLabels.cikk_cikkszam());
		field.setPrimaryKey(true);
		field.setLength(30);
		addField(field);

		field = new DataSourceTextField(CtorzsConstants.CIKK_SZINKOD,
				ctorzsLabels.cikk_szinkod());
		field.setLength(10);
		addField(field);

		field = new DataSourceBooleanField(CtorzsConstants.CIKK_AKCIOS,
				ctorzsLabels.cikk_akcios());
		addField(field);

		field = new DataSourceTextField(CtorzsConstants.CIKK_MEGNEVEZES,
				ctorzsLabels.cikk_megnevezes());
		field.setLength(60);
		field.setEditorProperties(textItem);
		addField(field);

		field = new DataSourceTextField(CtorzsConstants.CIKK_VAMTARIFASZAM,
				ctorzsLabels.cikk_vamtarifaszam());
		field.setLength(30);
		addField(field);

		field = new DataSourceDateField(CtorzsConstants.CIKK_FELVITELTOL,
				ctorzsLabels.cikk_felviteltol());
		field.setEditorProperties(dateItem);
		addField(field);	

		field = new DataSourceDateField(CtorzsConstants.CIKK_FELVITELIG,
				ctorzsLabels.cikk_felvitelig());
		field.setEditorProperties(dateItem);
		addField(field);	

		field = new DataSourceDateField(CtorzsConstants.CIKK_LEJARATTOL,
				ctorzsLabels.cikk_lejarattol());
		field.setEditorProperties(dateItem);
		addField(field);	

		field = new DataSourceDateField(CtorzsConstants.CIKK_LEJARATIG,
				ctorzsLabels.cikk_lejaratig());
		field.setEditorProperties(dateItem);
		addField(field);	
		
		field = new DataSourceFloatField(CtorzsConstants.CIKK_FOB,
				ctorzsLabels.cikk_fob());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_UJFOB,
				ctorzsLabels.cikk_ujfob());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_SZALLITAS,
				ctorzsLabels.cikk_szallitas());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_UJSZALLITAS,
				ctorzsLabels.cikk_ujszallitas());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_DDU,
				ctorzsLabels.cikk_ddu());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_UJDDU,
				ctorzsLabels.cikk_ujddu());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_ERSZ,
				ctorzsLabels.cikk_ersz());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_UJERSZ,
				ctorzsLabels.cikk_ujersz());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_ELORAR,
				ctorzsLabels.cikk_elorar());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_UJELORAR,
				ctorzsLabels.cikk_ujelorar());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_AR,
				ctorzsLabels.cikk_ar());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_AREUR,
				ctorzsLabels.cikk_areur());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_ARSZORZO,
				ctorzsLabels.cikk_arszorzo());
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

		field = new DataSourceTextField(CtorzsConstants.CIKK_MERTEKEGYSEG,
				ctorzsLabels.cikk_mertekegyseg());
		field.setValueMap(CtorzsConstants.getMertekegyseg());
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_TERFOGAT,
				ctorzsLabels.cikk_terfogat());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_TERFOGATLAB,
				ctorzsLabels.cikk_terfogatlab());
		field.setLength(10);
		field.setValidators(isFloatValidator);
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

		field = new DataSourceTextField(CtorzsConstants.CIKK_LEIRAS,
				ctorzsLabels.cikk_leiras());
		field.setLength(200);
		field.setEditorProperties(textAreaItem);
		addField(field);

		field = new DataSourceTextField(CtorzsConstants.CIKK_MEGJEGYZES,
				ctorzsLabels.cikk_megjegyzes());
		field.setLength(200);
		field.setEditorProperties(textAreaItem);
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
						CtorzsConstants.CIKK_FOTIPUS),
				request.getCriteria().getAttributeAsString(
						CtorzsConstants.CIKK_ALTIPUS), request.getCriteria()
						.getAttributeAsString(CtorzsConstants.CIKK_CIKKSZAM),
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
		to.setAttribute(CtorzsConstants.CIKK_FOTIPUS, from.getFotipus());
		to.setAttribute(CtorzsConstants.CIKK_ALTIPUS, from.getAltipus());
		to.setAttribute(CtorzsConstants.CIKK_GYARTO, from.getGyarto());
		to.setAttribute(CtorzsConstants.CIKK_GYARTOCIKKSZAM,
				from.getGyartocikkszam());
		to.setAttribute(CtorzsConstants.CIKK_CIKKSZAM, from.getCikkszam());
		to.setAttribute(CtorzsConstants.CIKK_SZINKOD, from.getSzinkod());		
		to.setAttribute(CtorzsConstants.CIKK_FELVITELTOL, from.getFelviteltol());
		to.setAttribute(CtorzsConstants.CIKK_FELVITELIG, from.getFelvitelig());
		to.setAttribute(CtorzsConstants.CIKK_LEJARATTOL, from.getLejarattol());
		to.setAttribute(CtorzsConstants.CIKK_LEJARATIG, from.getLejaratig());
		to.setAttribute(CtorzsConstants.CIKK_MEGNEVEZES, from.getMegnevezes());
		to.setAttribute(CtorzsConstants.CIKK_VAMTARIFASZAM,
				from.getVamtarifaszam());
		to.setAttribute(CtorzsConstants.CIKK_FOB, from.getFob());
		to.setAttribute(CtorzsConstants.CIKK_SZALLITAS, from.getSzallitas());
		to.setAttribute(CtorzsConstants.CIKK_DDU, from.getDdu());
		to.setAttribute(CtorzsConstants.CIKK_ERSZ, from.getErsz());
		to.setAttribute(CtorzsConstants.CIKK_ELORAR, from.getElorar());
		to.setAttribute(CtorzsConstants.CIKK_UJFOB, from.getUjfob());
		to.setAttribute(CtorzsConstants.CIKK_UJSZALLITAS, from.getUjszallitas());
		to.setAttribute(CtorzsConstants.CIKK_UJDDU, from.getUjddu());
		to.setAttribute(CtorzsConstants.CIKK_UJERSZ, from.getUjersz());
		to.setAttribute(CtorzsConstants.CIKK_UJELORAR, from.getUjelorar());
		to.setAttribute(CtorzsConstants.CIKK_AR, from.getAr());
		to.setAttribute(CtorzsConstants.CIKK_AREUR, from.getAreur());
		to.setAttribute(CtorzsConstants.CIKK_ARSZORZO, from.getArszorzo());
		to.setAttribute(CtorzsConstants.CIKK_KISKARTON, from.getKiskarton());
		to.setAttribute(CtorzsConstants.CIKK_DARAB, from.getDarab());
		to.setAttribute(CtorzsConstants.CIKK_MERTEKEGYSEG, from.getMertekegyseg());
		to.setAttribute(CtorzsConstants.CIKK_TERFOGAT, from.getTerfogat());
		to.setAttribute(CtorzsConstants.CIKK_TERFOGATLAB, from.getTerfogatlab());
		to.setAttribute(CtorzsConstants.CIKK_BSULY, from.getBsuly());
		to.setAttribute(CtorzsConstants.CIKK_NSULY, from.getNsuly());	
		to.setAttribute(CtorzsConstants.CIKK_LEIRAS, from.getLeiras());
		to.setAttribute(CtorzsConstants.CIKK_MEGJEGYZES, from.getMegjegyzes());
		to.setAttribute(CtorzsConstants.CIKK_KEPEK, from.getKepek());
	}

	private static void copyValues(ListGridRecord from, CikkSer to) {
		to.setFotipus(from.getAttributeAsString(CtorzsConstants.CIKK_FOTIPUS));
		to.setAltipus(from.getAttributeAsString(CtorzsConstants.CIKK_ALTIPUS));
		to.setGyarto(from
				.getAttributeAsString(CtorzsConstants.CIKK_GYARTO));
		to.setGyartocikkszam(from
				.getAttributeAsString(CtorzsConstants.CIKK_GYARTOCIKKSZAM));
		to.setCikkszam(from.getAttributeAsString(CtorzsConstants.CIKK_CIKKSZAM));
		to.setSzinkod(from.getAttributeAsString(CtorzsConstants.CIKK_SZINKOD));		
		to.setFelviteltol(from.getAttributeAsDate(CtorzsConstants.CIKK_FELVITELTOL));
		to.setFelvitelig(from.getAttributeAsDate(CtorzsConstants.CIKK_FELVITELIG));
		to.setLejarattol(from.getAttributeAsDate(CtorzsConstants.CIKK_LEJARATTOL));
		to.setLejaratig(from.getAttributeAsDate(CtorzsConstants.CIKK_LEJARATIG));
		to.setMegnevezes(from
				.getAttributeAsString(CtorzsConstants.CIKK_MEGNEVEZES));
		to.setVamtarifaszam(from
				.getAttributeAsString(CtorzsConstants.CIKK_VAMTARIFASZAM));
		to.setFob(from.getAttributeAsFloat(CtorzsConstants.CIKK_FOB));
		to.setSzallitas(from
				.getAttributeAsFloat(CtorzsConstants.CIKK_SZALLITAS));
		to.setDdu(from.getAttributeAsFloat(CtorzsConstants.CIKK_DDU));
		to.setErsz(from.getAttributeAsFloat(CtorzsConstants.CIKK_ERSZ));
		to.setElorar(from.getAttributeAsFloat(CtorzsConstants.CIKK_ELORAR));
		to.setUjfob(from.getAttributeAsFloat(CtorzsConstants.CIKK_UJFOB));
		to.setUjszallitas(from
				.getAttributeAsFloat(CtorzsConstants.CIKK_UJSZALLITAS));
		to.setUjddu(from.getAttributeAsFloat(CtorzsConstants.CIKK_UJDDU));
		to.setUjersz(from.getAttributeAsFloat(CtorzsConstants.CIKK_UJERSZ));
		to.setUjelorar(from.getAttributeAsFloat(CtorzsConstants.CIKK_UJELORAR));
		to.setAr(from.getAttributeAsFloat(CtorzsConstants.CIKK_AR));
		to.setAreur(from.getAttributeAsFloat(CtorzsConstants.CIKK_AREUR));
		to.setArszorzo(from.getAttributeAsFloat(CtorzsConstants.CIKK_ARSZORZO));
		to.setKiskarton(from.getAttributeAsInt(CtorzsConstants.CIKK_KISKARTON));
		to.setDarab(from.getAttributeAsInt(CtorzsConstants.CIKK_DARAB));
		to.setMertekegyseg(from.getAttributeAsString(CtorzsConstants.CIKK_MERTEKEGYSEG));
		to.setTerfogat(from.getAttributeAsFloat(CtorzsConstants.CIKK_TERFOGAT));
		to.setTerfogatlab(from.getAttributeAsFloat(CtorzsConstants.CIKK_TERFOGATLAB));
		to.setBsuly(from.getAttributeAsFloat(CtorzsConstants.CIKK_BSULY));
		to.setNsuly(from.getAttributeAsFloat(CtorzsConstants.CIKK_NSULY));
		to.setLeiras(from.getAttributeAsString(CtorzsConstants.CIKK_LEIRAS));
		to.setMegjegyzes(from.getAttributeAsString(CtorzsConstants.CIKK_MEGJEGYZES));
		to.setKepek(from.getAttributeAsInt(CtorzsConstants.CIKK_KEPEK));
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
