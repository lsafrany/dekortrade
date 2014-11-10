package hu.dekortrade.client.penztar.hazi;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.shared.serialized.FizetesSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class HaziDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private HaziLabels haziLabels = GWT.create(HaziLabels.class);

	public HaziDataSource() {

		TextItem textItem = new TextItem();
		textItem.setWidth("250");

		DataSourceField field;

		field = new DataSourceTextField(HaziConstants.HAZI_MEGJEGYZES,
				haziLabels.megjegyzes());
		field.setEditorProperties(textItem);
		addField(field);

		field = new DataSourceTextField(HaziConstants.HAZI_PENZTAROS,
				haziLabels.penztaros());
		addField(field);

		field = new DataSourceTextField(HaziConstants.HAZI_PENZTAROSNEV,
				haziLabels.penztarosnev());
		addField(field);

		field = new DataSourceFloatField(HaziConstants.HAZI_FIZET,
				haziLabels.fizet());
		addField(field);

		field = new DataSourceFloatField(HaziConstants.HAZI_FIZETEUR,
				haziLabels.fizeteur());
		addField(field);

		field = new DataSourceFloatField(HaziConstants.HAZI_FIZETUSD,
				haziLabels.fizetusd());
		addField(field);

		field = new DataSourceDateField(HaziConstants.HAZI_DATUM,
				haziLabels.datum());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getHazi(new AsyncCallback<List<FizetesSer>>() {
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

			public void onSuccess(List<FizetesSer> result) {
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
			FizetesSer fizetesSer = new FizetesSer();
			copyValues(rec, fizetesSer);
			dekorTradeService.addHazi(fizetesSer,
					new AsyncCallback<FizetesSer>() {
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

						public void onSuccess(FizetesSer result) {
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
	}

	@Override
	protected void executeRemove(final String requestId,
			final DSRequest request, final DSResponse response) {
	}

	private static void copyValues(FizetesSer from, ListGridRecord to) {
		to.setAttribute(HaziConstants.HAZI_MEGJEGYZES, from.getMegjegyzes());
		to.setAttribute(HaziConstants.HAZI_PENZTAROS, from.getPenztaros());
		to.setAttribute(HaziConstants.HAZI_PENZTAROSNEV,
				from.getPenztarosnev());
		to.setAttribute(HaziConstants.HAZI_FIZET, from.getFizet());
		to.setAttribute(HaziConstants.HAZI_FIZETEUR, from.getFizeteur());
		to.setAttribute(HaziConstants.HAZI_FIZETUSD, from.getFizetusd());
		to.setAttribute(HaziConstants.HAZI_DATUM, from.getDatum());
	}

	private static void copyValues(ListGridRecord from, FizetesSer to) {
		to.setMegjegyzes(from.getAttributeAsString(HaziConstants.HAZI_MEGJEGYZES));
		to.setPenztaros(UserInfo.userId);
		to.setFizet(from.getAttributeAsDouble(HaziConstants.HAZI_FIZET));
		to.setFizeteur(from.getAttributeAsDouble(HaziConstants.HAZI_FIZETEUR));
		to.setFizetusd(from.getAttributeAsDouble(HaziConstants.HAZI_FIZETUSD));
	}

}
