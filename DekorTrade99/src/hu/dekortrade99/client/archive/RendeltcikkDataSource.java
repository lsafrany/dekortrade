package hu.dekortrade99.client.archive;

import hu.dekortrade99.client.ClientConstants;
import hu.dekortrade99.client.DekorTrade99Service;
import hu.dekortrade99.client.DekorTrade99ServiceAsync;
import hu.dekortrade99.client.GwtRpcDataSource;
import hu.dekortrade99.shared.serialized.RendeltcikkSer;
import hu.dekortrade99.shared.serialized.SQLExceptionSer;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class RendeltcikkDataSource extends GwtRpcDataSource {

	private final DekorTrade99ServiceAsync dekorTrade99Service = GWT
			.create(DekorTrade99Service.class);

	private ArchiveLabels archiveLabels = GWT.create(ArchiveLabels.class);

	public RendeltcikkDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(ArchiveConstants.RENDELTCIKK_ROVIDNEV,
				archiveLabels.rendelt_rovidnev());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(ArchiveConstants.RENDELTCIKK_RENDELES,
				archiveLabels.rendelt_rendeles());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(ArchiveConstants.RENDELTCIKK_CIKKSZAM,
				archiveLabels.rendelt_cikkszam());
		addField(field);

		field = new DataSourceTextField(ArchiveConstants.RENDELTCIKK_EXPORTKARTON,
				archiveLabels.rendelt_exportkarton());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTrade99Service.getRendeltcikk(
				request.getCriteria().getAttributeAsString(
						ArchiveConstants.RENDELTCIKK_ROVIDNEV),
				request.getCriteria().getAttributeAsString(
						ArchiveConstants.RENDELTCIKK_RENDELES),						
				new AsyncCallback<ArrayList<RendeltcikkSer>>() {
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

					public void onSuccess(ArrayList<RendeltcikkSer> result) {
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

	private static void copyValues(RendeltcikkSer from, ListGridRecord to) {
		to.setAttribute(ArchiveConstants.RENDELTCIKK_ROVIDNEV, from.getRovidnev());
		to.setAttribute(ArchiveConstants.RENDELTCIKK_RENDELES, from.getRendeles());
		to.setAttribute(ArchiveConstants.RENDELTCIKK_CIKKSZAM, from.getCikkszam());
		to.setAttribute(ArchiveConstants.RENDELTCIKK_EXPORTKARTON, from.getExportkarton());
	}

}
