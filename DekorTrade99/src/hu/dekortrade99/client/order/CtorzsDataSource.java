package hu.dekortrade99.client.order;

import hu.dekortrade99.client.ClientConstants;
import hu.dekortrade99.client.DekorTrade99Service;
import hu.dekortrade99.client.DekorTrade99ServiceAsync;
import hu.dekortrade99.client.GwtRpcDataSource;
import hu.dekortrade99.shared.serialized.CikkSer;
import hu.dekortrade99.shared.serialized.SQLExceptionSer;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceImageField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CtorzsDataSource extends GwtRpcDataSource {

	private final DekorTrade99ServiceAsync dekorTrade99Service = GWT
			.create(DekorTrade99Service.class);

	private OrderLabels orderLabels = GWT.create(OrderLabels.class);

	public CtorzsDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(OrderConstants.CIKK_FOTIPUS,
				orderLabels.cikk_fotipus());
		addField(field);

		field = new DataSourceTextField(OrderConstants.CIKK_ALTIPUS,
				orderLabels.cikk_altipus());
		addField(field);

		field = new DataSourceTextField(OrderConstants.CIKK_CIKKSZAM,
				orderLabels.cikk_cikkszam());
		field.setPrimaryKey(true);
		addField(field);

		field = new DataSourceTextField(OrderConstants.CIKK_MEGNEVEZES,
				orderLabels.cikk_megnevezes());
		addField(field);

		field = new DataSourceFloatField(OrderConstants.CIKK_AR,
				orderLabels.cikk_ar());
		addField(field);

		field = new DataSourceIntegerField(OrderConstants.CIKK_KISKARTON,
				orderLabels.cikk_kiskarton());
		addField(field);

		field = new DataSourceIntegerField(OrderConstants.CIKK_DARAB,
				orderLabels.cikk_darab());
		addField(field);

		field = new DataSourceFloatField(OrderConstants.CIKK_TERFOGAT,
				orderLabels.cikk_terfogat());
		addField(field);

		field = new DataSourceFloatField(OrderConstants.CIKK_BSULY,
				orderLabels.cikk_bsuly());
		addField(field);

		field = new DataSourceFloatField(OrderConstants.CIKK_NSULY,
				orderLabels.cikk_nsuly());
		addField(field);

		field = new DataSourceFloatField(OrderConstants.CIKK_KEPEK,
				orderLabels.cikk_kepek());
		addField(field);

		field = new DataSourceImageField(OrderConstants.CIKK_KEP,orderLabels.kep());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTrade99Service.getCikk(
				request.getCriteria().getAttributeAsInt(
						OrderConstants.CTORZS_PAGE),
				request.getCriteria().getAttributeAsString(
						OrderConstants.CIKK_FOTIPUS),
				request.getCriteria().getAttributeAsString(
								OrderConstants.CIKK_ALTIPUS),
				new AsyncCallback<ArrayList<CikkSer>>() {
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

					public void onSuccess(ArrayList<CikkSer> result) {
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
	}

	@Override
	protected void executeRemove(final String requestId,
			final DSRequest request, final DSResponse response) {
	}

	private static void copyValues(CikkSer from, ListGridRecord to) {
//		to.setAttribute(OrderConstants.CIKK_FOTIPUS, from.getFotipus());
//		to.setAttribute(OrderConstants.CIKK_ALTIPUS, from.getAltipus());
		to.setAttribute(OrderConstants.CIKK_CIKKSZAM, from.getCikkszam());
//		to.setAttribute(OrderConstants.CIKK_MEGNEVEZES, from.getMegnevezes());
		to.setAttribute(OrderConstants.CIKK_AR, from.getAr());
		to.setAttribute(OrderConstants.CIKK_KISKARTON, from.getKiskarton());
		to.setAttribute(OrderConstants.CIKK_DARAB, from.getDarab());
		to.setAttribute(OrderConstants.CIKK_TERFOGAT, from.getTerfogat());
		to.setAttribute(OrderConstants.CIKK_BSULY, from.getBsuly());
		to.setAttribute(OrderConstants.CIKK_NSULY, from.getNsuly());
//		to.setAttribute(OrderConstants.CIKK_KEPEK, from.getKepek());
		if (from.getKepek() > 0)
			to.setAttribute(OrderConstants.CIKK_KEP, GWT.getModuleBaseURL()+ "download?ctorzs=" + from.getCikkszam());
	}

}
