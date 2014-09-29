package hu.dekortrade.client.raktar.kesztlet;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.shared.serialized.RaktarSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.LinkedHashMap;
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
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.IsFloatValidator;
import com.smartgwt.client.widgets.form.validator.IsIntegerValidator;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class RaktarDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private KeszletLabels keszletLabels = GWT.create(KeszletLabels.class);

	public RaktarDataSource(final LinkedHashMap<String, String> fotipus,
			final LinkedHashMap<String, String> altipus) {

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

		field = new DataSourceTextField(KeszletConstants.KESZLET_FOTIPUS,
				keszletLabels.raktar_fotipus());
		field.setEditorProperties(fotipusSelectItem);
		field.setValueMap(fotipus);
		addField(field);

		field = new DataSourceTextField(KeszletConstants.KESZLET_ALTIPUS,
				keszletLabels.raktar_altipus());
		field.setEditorProperties(altipusSelectItem);
		field.setValueMap(altipus);
		addField(field);

		field = new DataSourceTextField(KeszletConstants.KESZLET_CIKKSZAM,
				keszletLabels.raktar_cikkszam());
		field.setPrimaryKey(true);
		field.setLength(30);
		addField(field);

		field = new DataSourceTextField(KeszletConstants.KESZLET_SZINKOD,
				keszletLabels.raktar_szinkod());
		field.setLength(10);
		addField(field);

		field = new DataSourceTextField(KeszletConstants.KESZLET_MEGNEVEZES,
				keszletLabels.raktar_megnevezes());
		field.setLength(60);
		field.setEditorProperties(textItem);
		addField(field);

		field = new DataSourceFloatField(KeszletConstants.KESZLET_ELORAR,
				keszletLabels.raktar_elorar());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(KeszletConstants.KESZLET_AR,
				keszletLabels.raktar_ar());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(KeszletConstants.KESZLET_AREUR,
				keszletLabels.raktar_areur());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceIntegerField(KeszletConstants.KESZLET_KISKARTON,
				keszletLabels.raktar_kiskarton());
		field.setLength(10);
		field.setValidators(isIntegerValidator);
		addField(field);

		field = new DataSourceIntegerField(KeszletConstants.KESZLET_DARAB,
				keszletLabels.raktar_darab());
		field.setLength(10);
		field.setValidators(isIntegerValidator);
		addField(field);

		field = new DataSourceTextField(KeszletConstants.KESZLET_MERTEKEGYSEG,
				keszletLabels.raktar_mertekegyseg());
		field.setValueMap(KeszletConstants.getMertekegyseg());
		addField(field);

		field = new DataSourceFloatField(KeszletConstants.KESZLET_TERFOGAT,
				keszletLabels.raktar_terfogat());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(KeszletConstants.KESZLET_TERFOGATLAB,
				keszletLabels.raktar_terfogatlab());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(KeszletConstants.KESZLET_BSULY,
				keszletLabels.raktar_bsuly());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(KeszletConstants.KESZLET_NSULY,
				keszletLabels.raktar_nsuly());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceTextField(KeszletConstants.KESZLET_LEIRAS,
				keszletLabels.raktar_leiras());
		field.setLength(200);
		field.setEditorProperties(textAreaItem);
		addField(field);

		field = new DataSourceTextField(KeszletConstants.KESZLET_MEGJEGYZES,
				keszletLabels.raktar_megjegyzes());
		field.setLength(200);
		field.setEditorProperties(textAreaItem);
		addField(field);

		field = new DataSourceIntegerField(KeszletConstants.KESZLET_KEXPORTKARTON,
				keszletLabels.raktar_kexportkarton());
		field.setLength(10);
		field.setValidators(isIntegerValidator);
		addField(field);

		field = new DataSourceIntegerField(KeszletConstants.KESZLET_KKISKARTON,
				keszletLabels.raktar_kkiskarton());
		field.setLength(10);
		field.setValidators(isIntegerValidator);
		addField(field);

		field = new DataSourceIntegerField(KeszletConstants.KESZLET_KDARAB,
				keszletLabels.raktar_kdarab());
		field.setLength(10);
		field.setValidators(isIntegerValidator);
		addField(field);

		field = new DataSourceIntegerField(KeszletConstants.KESZLET_MEXPORTKARTON,
				keszletLabels.raktar_mexportkarton());
		field.setLength(10);
		field.setValidators(isIntegerValidator);
		addField(field);

		field = new DataSourceIntegerField(KeszletConstants.KESZLET_MKISKARTON,
				keszletLabels.raktar_mkiskarton());
		field.setLength(10);
		field.setValidators(isIntegerValidator);
		addField(field);

		field = new DataSourceIntegerField(KeszletConstants.KESZLET_MDARAB,
				keszletLabels.raktar_mdarab());
		field.setLength(10);
		field.setValidators(isIntegerValidator);
		addField(field);

		field = new DataSourceTextField(KeszletConstants.KESZLET_HELYKOD,
				keszletLabels.raktar_hely());
		field.setValueMap(KeszletConstants.getHely());
		addField(field);
		
	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getRaktar(
				request.getCriteria().getAttributeAsInt(
						KeszletConstants.RAKTAR_PAGE),
				request.getCriteria().getAttributeAsString(
						KeszletConstants.KESZLET_FOTIPUS),
				request.getCriteria().getAttributeAsString(
						KeszletConstants.KESZLET_ALTIPUS),
				request.getCriteria().getAttributeAsString(
						KeszletConstants.KESZLET_CIKKSZAM),
				new AsyncCallback<List<RaktarSer>>() {
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

					public void onSuccess(List<RaktarSer> result) {
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
	}

	@Override
	protected void executeUpdate(final String requestId,
			final DSRequest request, final DSResponse response) {
		
		ListGridRecord rec = getEditedRecord(request);
		RaktarSer raktarSer = new RaktarSer();
		copyValues(rec, raktarSer);
		dekorTradeService.updateRaktar(request.getAttribute(KeszletConstants.RAKTAR_ROVANCS), UserInfo.userId, raktarSer, new AsyncCallback<RaktarSer>() {
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

			public void onSuccess(RaktarSer result) {
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

	private static void copyValues(RaktarSer from, ListGridRecord to) {
		to.setAttribute(KeszletConstants.KESZLET_FOTIPUS, from.getFotipus());
		to.setAttribute(KeszletConstants.KESZLET_ALTIPUS, from.getAltipus());
		to.setAttribute(KeszletConstants.KESZLET_CIKKSZAM, from.getCikkszam());
		to.setAttribute(KeszletConstants.KESZLET_SZINKOD, from.getSzinkod());
		to.setAttribute(KeszletConstants.KESZLET_MEGNEVEZES,
				from.getMegnevezes());
		to.setAttribute(KeszletConstants.KESZLET_ELORAR, from.getElorar());
		to.setAttribute(KeszletConstants.KESZLET_AR, from.getAr());
		to.setAttribute(KeszletConstants.KESZLET_AREUR, from.getAreur());
		to.setAttribute(KeszletConstants.KESZLET_KISKARTON, from.getKiskarton());
		to.setAttribute(KeszletConstants.KESZLET_DARAB, from.getDarab());
		to.setAttribute(KeszletConstants.KESZLET_MERTEKEGYSEG,
				from.getMertekegyseg());
		to.setAttribute(KeszletConstants.KESZLET_TERFOGAT, from.getTerfogat());
		to.setAttribute(KeszletConstants.KESZLET_TERFOGATLAB,
				from.getTerfogatlab());
		to.setAttribute(KeszletConstants.KESZLET_BSULY, from.getBsuly());
		to.setAttribute(KeszletConstants.KESZLET_NSULY, from.getNsuly());
		to.setAttribute(KeszletConstants.KESZLET_LEIRAS, from.getLeiras());
		to.setAttribute(KeszletConstants.KESZLET_MEGJEGYZES,
				from.getMegjegyzes());
		to.setAttribute(KeszletConstants.KESZLET_KEXPORTKARTON,
				from.getKexportkarton());
		to.setAttribute(KeszletConstants.KESZLET_KKISKARTON,
				from.getKkiskarton());
		to.setAttribute(KeszletConstants.KESZLET_KDARAB,
				from.getKdarab());
		to.setAttribute(KeszletConstants.KESZLET_MEXPORTKARTON,
				from.getMexportkarton());
		to.setAttribute(KeszletConstants.KESZLET_MKISKARTON,
				from.getMkiskarton());
		to.setAttribute(KeszletConstants.KESZLET_MDARAB,
				from.getMdarab());
		to.setAttribute(KeszletConstants.KESZLET_HELYKOD,
				from.getHelykod());

	}

	private static void copyValues(ListGridRecord from, RaktarSer to) {
		to.setFotipus(from
				.getAttributeAsString(KeszletConstants.KESZLET_FOTIPUS));
		to.setAltipus(from
				.getAttributeAsString(KeszletConstants.KESZLET_ALTIPUS));
		to.setCikkszam(from
				.getAttributeAsString(KeszletConstants.KESZLET_CIKKSZAM));
		to.setSzinkod(from
				.getAttributeAsString(KeszletConstants.KESZLET_SZINKOD));
		to.setMegnevezes(from
				.getAttributeAsString(KeszletConstants.KESZLET_MEGNEVEZES));
		to.setElorar(from.getAttributeAsFloat(KeszletConstants.KESZLET_ELORAR));
		to.setAr(from.getAttributeAsFloat(KeszletConstants.KESZLET_AR));
		to.setAreur(from.getAttributeAsFloat(KeszletConstants.KESZLET_AREUR));
		to.setKiskarton(from
				.getAttributeAsInt(KeszletConstants.KESZLET_KISKARTON));
		to.setDarab(from.getAttributeAsInt(KeszletConstants.KESZLET_DARAB));
		to.setMertekegyseg(from
				.getAttributeAsString(KeszletConstants.KESZLET_MERTEKEGYSEG));
		to.setTerfogat(from
				.getAttributeAsFloat(KeszletConstants.KESZLET_TERFOGAT));
		to.setTerfogatlab(from
				.getAttributeAsFloat(KeszletConstants.KESZLET_TERFOGATLAB));
		to.setBsuly(from.getAttributeAsFloat(KeszletConstants.KESZLET_BSULY));
		to.setNsuly(from.getAttributeAsFloat(KeszletConstants.KESZLET_NSULY));
		to.setLeiras(from.getAttributeAsString(KeszletConstants.KESZLET_LEIRAS));
		to.setMegjegyzes(from
				.getAttributeAsString(KeszletConstants.KESZLET_MEGJEGYZES));
		to.setKexportkarton(from
				.getAttributeAsLong(KeszletConstants.KESZLET_KEXPORTKARTON));
		to.setKkiskarton(from
				.getAttributeAsLong(KeszletConstants.KESZLET_KKISKARTON));
		to.setKdarab(from
				.getAttributeAsLong(KeszletConstants.KESZLET_KDARAB));
		to.setMexportkarton(from
				.getAttributeAsLong(KeszletConstants.KESZLET_MEXPORTKARTON));
		to.setMkiskarton(from
				.getAttributeAsLong(KeszletConstants.KESZLET_MKISKARTON));
		to.setMdarab(from
				.getAttributeAsLong(KeszletConstants.KESZLET_MDARAB));

		to.setHelykod(from
				.getAttributeAsString(KeszletConstants.KESZLET_HELYKOD));

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
