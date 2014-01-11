package hu.dekortrade99.client.cikktorzs;

import hu.dekortrade99.client.ClientConstants;
import hu.dekortrade99.client.DekorTrade99Service;
import hu.dekortrade99.client.DekorTrade99ServiceAsync;
import hu.dekortrade99.client.GwtRpcDataSource;
import hu.dekortrade99.shared.serialized.CtorzsSer;
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

		field = new DataSourceTextField(CtorzsConstants.CTORZS_CIKKSZAM,
				ctorzsLabels.ctorzs_cikkszam());
		field.setPrimaryKey(true);
		addField(field);

		field = new DataSourceTextField(CtorzsConstants.CTORZS_MEGNEVEZES,
				ctorzsLabels.ctorzs_megnevezes());
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CTORZS_AR,
				ctorzsLabels.ctorzs_ar());
		addField(field);

		field = new DataSourceIntegerField(CtorzsConstants.CTORZS_KISKARTON,
				ctorzsLabels.ctorzs_kiskarton());
		addField(field);

		field = new DataSourceIntegerField(CtorzsConstants.CTORZS_DARAB,
				ctorzsLabels.ctorzs_darab());
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CTORZS_TERFOGAT,
				ctorzsLabels.ctorzs_terfogat());
		addField(field);

		field = new DataSourceTextField(CtorzsConstants.CTORZS_JEL,
				ctorzsLabels.ctorzs_jel());
		field.setValueMap(ClientConstants.getJelek());
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CTORZS_BSULY,
				ctorzsLabels.ctorzs_bsuly());
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CTORZS_NSULY,
				ctorzsLabels.ctorzs_nsuly());
		addField(field);

		field = new DataSourceFloatField(CtorzsConstants.CTORZS_KEPEK,
				ctorzsLabels.ctorzs_kepek());
		field.setHidden(true);
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTrade99Service.getCtorzs(
				request.getCriteria().getAttributeAsInt(
						CtorzsConstants.CTORZS_PAGE),
				request.getCriteria().getAttributeAsString(
						CtorzsConstants.CTORZS_CIKKSZAM),
				request.getCriteria().getAttributeAsString(
						CtorzsConstants.CTORZS_JEL),
				new AsyncCallback<ArrayList<CtorzsSer>>() {
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

					public void onSuccess(ArrayList<CtorzsSer> result) {
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

}
