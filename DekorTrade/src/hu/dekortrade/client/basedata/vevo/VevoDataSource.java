package hu.dekortrade.client.basedata.vevo;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.VevoSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.IsFloatValidator;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class VevoDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private VevoLabels vevoLabels = GWT.create(VevoLabels.class);

	public VevoDataSource() {

		IsFloatValidator isFloatValidator = new IsFloatValidator();
		
		TextItem textItem = new TextItem();
		textItem.setWidth("400");

		TextItem textItem1 = new TextItem();
		textItem1.setWidth("300");

		TextAreaItem textAreaItem = new TextAreaItem();
		textAreaItem.setWidth("500");
		textAreaItem.setHeight("60");

		DataSourceField field;

		field = new DataSourceTextField(VevoConstants.VEVO_ROVIDNEV,
				vevoLabels.vevo_rovidnev());
		field.setPrimaryKey(true);
		field.setLength(20);
		field.setRequired(true);
		addField(field);

		field = new DataSourceTextField(VevoConstants.VEVO_TIPUS,
				vevoLabels.vevo_tipus());
		field.setValueMap(VevoConstants.getTipus());
		addField(field);

		field = new DataSourceTextField(VevoConstants.VEVO_NEV,
				vevoLabels.vevo_nev());
		field.setLength(60);
		field.setEditorProperties(textItem);
		field.setRequired(true);
		addField(field);

		field = new DataSourceTextField(VevoConstants.VEVO_CIM,
				vevoLabels.vevo_cim());
		field.setLength(100);
		field.setEditorProperties(textItem);
		addField(field);

		field = new DataSourceTextField(VevoConstants.VEVO_ELERHETOSEG,
				vevoLabels.vevo_elerhetoseg());
		field.setLength(150);
		field.setEditorProperties(textAreaItem);
		addField(field);

		field = new DataSourceFloatField(VevoConstants.VEVO_EGYENLEG_USD,
				vevoLabels.vevo_egyenleg_usd());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(VevoConstants.VEVO_EGYENLEG_EUR,
				vevoLabels.vevo_egyenleg_eur());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);
		
		field = new DataSourceFloatField(VevoConstants.VEVO_EGYENLEG_HUF,
				vevoLabels.vevo_egyenleg_huf());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(VevoConstants.VEVO_TAROLASIDIJ,
				vevoLabels.vevo_tarolasidij());
		field.setLength(8);
		field.setValidators(isFloatValidator);
		addField(field);

		field = new DataSourceFloatField(VevoConstants.VEVO_ELOLEG,
				vevoLabels.vevo_eloleg());
		field.setLength(12);
		field.setValidators(isFloatValidator);
		addField(field);
	
		field = new DataSourceTextField(VevoConstants.VEVO_BANKSZAMLASZAM,
				vevoLabels.vevo_bankszamlaszam());
		field.setLength(20);
		field.setEditorProperties(textItem1);
		addField(field);
	
		field = new DataSourceTextField(VevoConstants.VEVO_EUADOSZAM,
				vevoLabels.vevo_euadoszam());
		field.setLength(20);
		field.setEditorProperties(textItem1);
		addField(field);
		
		field = new DataSourceFloatField(VevoConstants.VEVO_ELORARKEDVEZMENY,
				vevoLabels.vevo_elorarkedvezmeny());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);
		
		field = new DataSourceFloatField(VevoConstants.VEVO_AJANLOTTARKEDVEZMENY,
				vevoLabels.vevo_ajanlottarkedvezmeny());
		field.setLength(10);
		field.setValidators(isFloatValidator);
		addField(field);
	
		field = new DataSourceTextField(VevoConstants.VEVO_ORSZAG,
				vevoLabels.vevo_orszag());
		field.setValueMap(VevoConstants.getOrszag());
		addField(field);

		field = new DataSourceTextField(VevoConstants.VEVO_MEGJEGYZES,
				vevoLabels.vevo_megjegyzes());
		field.setLength(200);
		field.setEditorProperties(textAreaItem);
		addField(field);
		
		field = new DataSourceBooleanField(VevoConstants.VEVO_INTERNET,
				vevoLabels.vevo_internet());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getVevo(new AsyncCallback<List<VevoSer>>() {
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

			public void onSuccess(List<VevoSer> result) {
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
		VevoSer vevoSer = new VevoSer();
		copyValues(rec, vevoSer);
		dekorTradeService.addVevo(vevoSer, new AsyncCallback<VevoSer>() {
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

			public void onSuccess(VevoSer result) {
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
		VevoSer vevoSer = new VevoSer();
		copyValues(rec, vevoSer);
		dekorTradeService.updateVevo(vevoSer, new AsyncCallback<VevoSer>() {
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

			public void onSuccess(VevoSer result) {
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
		VevoSer vevoSer = new VevoSer();
		copyValues(rec, vevoSer);
		dekorTradeService.removeVevo(vevoSer, new AsyncCallback<VevoSer>() {
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

			public void onSuccess(VevoSer result) {
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

	private static void copyValues(VevoSer from, ListGridRecord to) {
		to.setAttribute(VevoConstants.VEVO_ROVIDNEV, from.getRovidnev());
		to.setAttribute(VevoConstants.VEVO_TIPUS, from.getTipus());
		to.setAttribute(VevoConstants.VEVO_NEV, from.getNev());
		to.setAttribute(VevoConstants.VEVO_CIM, from.getCim());
		to.setAttribute(VevoConstants.VEVO_ELERHETOSEG, from.getElerhetoseg());
		to.setAttribute(VevoConstants.VEVO_EGYENLEG_USD, from.getEgyenlegusd());
		to.setAttribute(VevoConstants.VEVO_EGYENLEG_EUR, from.getEgyenlegeur());
		to.setAttribute(VevoConstants.VEVO_EGYENLEG_HUF, from.getEgyenleghuf());
		to.setAttribute(VevoConstants.VEVO_TAROLASIDIJ, from.getTarolasidij());
		to.setAttribute(VevoConstants.VEVO_ELOLEG, from.getEloleg());
		to.setAttribute(VevoConstants.VEVO_BANKSZAMLASZAM, from.getBankszamlaszam());
		to.setAttribute(VevoConstants.VEVO_EUADOSZAM, from.getEuadoszam());
		to.setAttribute(VevoConstants.VEVO_ELORARKEDVEZMENY, from.getElorarkedvezmeny());
		to.setAttribute(VevoConstants.VEVO_AJANLOTTARKEDVEZMENY, from.getAjanlottarkedvezmeny());
		to.setAttribute(VevoConstants.VEVO_ORSZAG, from.getOrszag());	
		to.setAttribute(VevoConstants.VEVO_MEGJEGYZES, from.getMegjegyzes());		
		to.setAttribute(VevoConstants.VEVO_INTERNET, from.getInternet());
	}

	private static void copyValues(ListGridRecord from, VevoSer to) {

		to.setRovidnev(from.getAttributeAsString(VevoConstants.VEVO_ROVIDNEV));
		to.setNev(from.getAttributeAsString(VevoConstants.VEVO_NEV));
		to.setTipus(from.getAttributeAsString(VevoConstants.VEVO_TIPUS));
		to.setCim(from.getAttributeAsString(VevoConstants.VEVO_CIM));
		to.setElerhetoseg(from
				.getAttributeAsString(VevoConstants.VEVO_ELERHETOSEG));
		to.setEgyenlegusd(from
				.getAttributeAsFloat(VevoConstants.VEVO_EGYENLEG_USD));
		to.setEgyenlegeur(from
				.getAttributeAsFloat(VevoConstants.VEVO_EGYENLEG_EUR));
		to.setEgyenleghuf(from
				.getAttributeAsFloat(VevoConstants.VEVO_EGYENLEG_HUF));
		to.setTarolasidij(from
				.getAttributeAsFloat(VevoConstants.VEVO_TAROLASIDIJ));
		to.setEloleg(from
				.getAttributeAsFloat(VevoConstants.VEVO_ELOLEG));
		to.setBankszamlaszam(from
				.getAttributeAsString(VevoConstants.VEVO_BANKSZAMLASZAM));
		to.setEuadoszam(from
				.getAttributeAsString(VevoConstants.VEVO_EUADOSZAM));
		to.setElorarkedvezmeny(from
				.getAttributeAsFloat(VevoConstants.VEVO_ELORARKEDVEZMENY));
		to.setAjanlottarkedvezmeny(from
				.getAttributeAsFloat(VevoConstants.VEVO_AJANLOTTARKEDVEZMENY));
		to.setOrszag(from
				.getAttributeAsString(VevoConstants.VEVO_ORSZAG));
		to.setMegjegyzes(from
				.getAttributeAsString(VevoConstants.VEVO_MEGJEGYZES));
		to.setInternet(from.getAttributeAsBoolean(VevoConstants.VEVO_INTERNET));
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
