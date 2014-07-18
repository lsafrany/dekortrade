package hu.dekortrade.client.penztar.torlesztes;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.shared.serialized.FizetesSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class TorlesztesDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private TorlesztesLabels torlesztesLabels = GWT.create(TorlesztesLabels.class);

	private String cedula = "";
	public TorlesztesDataSource(String cedula) {
		
		this.cedula = cedula;
		
		DataSourceField field;

		field = new DataSourceTextField(TorlesztesConstants.TORLESZTES_CEDULA,
				torlesztesLabels.cedula());
		field.setHidden(true);
		addField(field);

		field = new DataSourceFloatField(TorlesztesConstants.TORLESZTES_FIZET,
				torlesztesLabels.torleszt());
		addField(field);

		field = new DataSourceFloatField(TorlesztesConstants.TORLESZTES_FIZETEUR,
				torlesztesLabels.torleszteur());
		addField(field);

		field = new DataSourceFloatField(TorlesztesConstants.TORLESZTES_FIZETUSD,
				torlesztesLabels.torlesztusd());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getTorlesztes(cedula,new AsyncCallback<List<FizetesSer>>() {
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
		to.setAttribute(TorlesztesConstants.TORLESZTES_CEDULA, from.getCedula());
		to.setAttribute(TorlesztesConstants.TORLESZTES_FIZET, from.getFizet());
		to.setAttribute(TorlesztesConstants.TORLESZTES_FIZETEUR, from.getFizeteur());
		to.setAttribute(TorlesztesConstants.TORLESZTES_FIZETUSD, from.getFizetusd());
	}

}
