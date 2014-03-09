package hu.dekortrade99.client.order;

import hu.dekortrade99.client.ClientConstants;
import hu.dekortrade99.client.DekorTrade99Service;
import hu.dekortrade99.client.DekorTrade99ServiceAsync;
import hu.dekortrade99.client.GwtRpcDataSource;
import hu.dekortrade99.shared.serialized.CikkfotipusSer;
import hu.dekortrade99.shared.serialized.SQLExceptionSer;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceImageField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CikkfotipusDataSource extends GwtRpcDataSource {

	private final DekorTrade99ServiceAsync dekorTrade99Service = GWT
			.create(DekorTrade99Service.class);

	private OrderLabels orderLabels = GWT.create(OrderLabels.class);

	public CikkfotipusDataSource() {

		DataSourceField field;
  		
		field = new DataSourceTextField(OrderConstants.CIKKFOTIPUS_KOD,
				orderLabels.cikkfotipus_kod());
		field.setPrimaryKey(true);
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(OrderConstants.CIKKFOTIPUS_NEV,
				orderLabels.cikkfotipus_nev());
		addField(field);

		field = new DataSourceImageField(OrderConstants.CIKKFOTIPUS_KEP,orderLabels.kep());
		addField(field);
			
	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
			dekorTrade99Service.getCikkfotipus(new AsyncCallback<ArrayList<CikkfotipusSer>>() {
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

			public void onSuccess(ArrayList<CikkfotipusSer> result) {
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

	}

	@Override
	protected void executeUpdate(final String requestId,
			final DSRequest request, final DSResponse response) {


	}

	@Override
	protected void executeRemove(final String requestId,
			final DSRequest request, final DSResponse response) {

	}

	private static void copyValues(CikkfotipusSer from, ListGridRecord to) {
		to.setAttribute(OrderConstants.CIKKFOTIPUS_KOD, from.getKod());
		to.setAttribute(OrderConstants.CIKKFOTIPUS_NEV, from.getNev());
		to.setAttribute(OrderConstants.CIKKFOTIPUS_KEP, GWT.getModuleBaseURL()+ "download?cikkfotipus=" + from.getKod());
	}

}
