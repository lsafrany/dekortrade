package hu.dekortrade99.client.cikktorzs;

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
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CtorzsDataSource extends GwtRpcDataSource {

	private final DekorTrade99ServiceAsync dekorTrade99Service = GWT
			.create(DekorTrade99Service.class);

	private CtorzsLabels ctorzsLabels = GWT.create(CtorzsLabels.class);

	public CtorzsDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(CtorzsConstants.CIKK_FOTIPUS,
				ctorzsLabels.cikk_fotipus());
		addField(field);

		field = new DataSourceTextField(CtorzsConstants.CIKK_ALTIPUS,
				ctorzsLabels.cikk_altipus());
		addField(field);

		field = new DataSourceTextField(CtorzsConstants.CIKK_CIKKSZAM,
				ctorzsLabels.cikk_cikkszam());
		field.setPrimaryKey(true);
		addField(field);

		field = new DataSourceTextField(CtorzsConstants.CIKK_MEGNEVEZES,
				ctorzsLabels.cikk_megnevezes());
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_AR,
				ctorzsLabels.cikk_ar());
		addField(field);

		field = new DataSourceIntegerField(CtorzsConstants.CIKK_KISKARTON,
				ctorzsLabels.cikk_kiskarton());
		addField(field);

		field = new DataSourceIntegerField(CtorzsConstants.CIKK_DARAB,
				ctorzsLabels.cikk_darab());
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_TERFOGAT,
				ctorzsLabels.cikk_terfogat());
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_BSULY,
				ctorzsLabels.cikk_bsuly());
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_NSULY,
				ctorzsLabels.cikk_nsuly());
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CIKK_KEPEK,
				ctorzsLabels.cikk_kepek());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTrade99Service.getCikk(
				request.getCriteria().getAttributeAsInt(
						CtorzsConstants.CTORZS_PAGE),
				request.getCriteria().getAttributeAsString(
						CtorzsConstants.CIKK_CIKKSZAM),
				request.getCriteria().getAttributeAsString(
						CtorzsConstants.CIKK_FOTIPUS),
				request.getCriteria().getAttributeAsString(
								CtorzsConstants.CIKK_ALTIPUS),
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
		to.setAttribute(CtorzsConstants.CIKK_FOTIPUS, from.getFotipus());
		to.setAttribute(CtorzsConstants.CIKK_ALTIPUS, from.getAltipus());
		to.setAttribute(CtorzsConstants.CIKK_CIKKSZAM, from.getCikkszam());
		to.setAttribute(CtorzsConstants.CIKK_MEGNEVEZES, from.getMegnevezes());
		to.setAttribute(CtorzsConstants.CIKK_AR, from.getAr());
		to.setAttribute(CtorzsConstants.CIKK_KISKARTON, from.getKiskarton());
		to.setAttribute(CtorzsConstants.CIKK_DARAB, from.getDarab());
		to.setAttribute(CtorzsConstants.CIKK_TERFOGAT, from.getTerfogat());
		to.setAttribute(CtorzsConstants.CIKK_BSULY, from.getBsuly());
		to.setAttribute(CtorzsConstants.CIKK_NSULY, from.getNsuly());
		to.setAttribute(CtorzsConstants.CIKK_KEPEK, from.getKepek());
	}

}
