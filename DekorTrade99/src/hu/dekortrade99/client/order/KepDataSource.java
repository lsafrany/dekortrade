package hu.dekortrade99.client.order;

import hu.dekortrade99.client.ClientConstants;
import hu.dekortrade99.client.DekorTrade99Service;
import hu.dekortrade99.client.DekorTrade99ServiceAsync;
import hu.dekortrade99.client.GwtRpcDataSource;
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

public class KepDataSource extends GwtRpcDataSource {

	private final DekorTrade99ServiceAsync dekorTrade99Service = GWT
			.create(DekorTrade99Service.class);

	private OrderLabels orderLabels = GWT.create(OrderLabels.class);

	private static String cikkszam = "";

	private static String szinkod = "";
	
	public KepDataSource() {

		DataSourceField field;
  		
		field = new DataSourceTextField(OrderConstants.KEP_SORSZAM,
				orderLabels.kepsorszam());
		field.setPrimaryKey(true);
		field.setHidden(true);
		addField(field);

		field = new DataSourceImageField(OrderConstants.KEP_KEP,orderLabels.kep());
		addField(field);
			
	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
			cikkszam =  request.getAttributeAsString(
					OrderConstants.CIKK_CIKKSZAM);
			szinkod =  request.getAttributeAsString(
					OrderConstants.CIKK_SZINKOD);
			dekorTrade99Service.getKep(cikkszam,szinkod
					,new AsyncCallback<ArrayList<String>>() {
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

			public void onSuccess(ArrayList<String> result) {
				ListGridRecord[] list = new ListGridRecord[result.size()];
				for (int i = 0; i < result.size(); i++) {
					ListGridRecord record = new ListGridRecord();
					copyValues(cikkszam,result.get(i), record);
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

	private static void copyValues(String cikkszam,String from, ListGridRecord to) {
		to.setAttribute(OrderConstants.KEP_SORSZAM, from);
		to.setAttribute(OrderConstants.KEP_KEP, GWT.getModuleBaseURL()+ "download?cikkszam=" + cikkszam + "&szinkod=" + szinkod + "&sorszam=" + from);
	}

}
