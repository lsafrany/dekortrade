package hu.dekortrade99.client.archive;

import hu.dekortrade99.client.ClientConstants;
import hu.dekortrade99.client.DekorTrade99Service;
import hu.dekortrade99.client.DekorTrade99ServiceAsync;
import hu.dekortrade99.client.GwtRpcDataSource;
import hu.dekortrade99.shared.serialized.RendeltSer;
import hu.dekortrade99.shared.serialized.SQLExceptionSer;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class RendeltDataSource extends GwtRpcDataSource {

	private final DekorTrade99ServiceAsync dekorTrade99Service = GWT
			.create(DekorTrade99Service.class);

	private ArchiveLabels archiveLabels = GWT.create(ArchiveLabels.class);

	public RendeltDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(ArchiveConstants.RENDELT_ROVIDNEV,
				archiveLabels.rendelt_rovidnev());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(ArchiveConstants.RENDELT_RENDELES,
				archiveLabels.rendelt_rendeles());
		addField(field);

		field = new DataSourceTextField(ArchiveConstants.RENDELT_DATUM,
				archiveLabels.rendelt_datum());
		addField(field);

		field = new DataSourceTextField(ArchiveConstants.RENDELT_STATUSZ,
				archiveLabels.rendelt_statusz());
		field.setValueMap(ArchiveConstants.getStatusz());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTrade99Service.getRendelt(
				request.getCriteria().getAttributeAsString(
						ArchiveConstants.RENDELT_ROVIDNEV),
				new AsyncCallback<ArrayList<RendeltSer>>() {
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

					public void onSuccess(ArrayList<RendeltSer> result) {
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

	private static void copyValues(RendeltSer from, ListGridRecord to) {
		to.setAttribute(ArchiveConstants.RENDELT_ROVIDNEV, from.getRovidnev());
		to.setAttribute(ArchiveConstants.RENDELT_RENDELES, from.getRendeles());
		to.setAttribute(ArchiveConstants.RENDELT_DATUM, from.getDatum());
		to.setAttribute(ArchiveConstants.RENDELT_STATUSZ, from.getStatusz());
	}

}
