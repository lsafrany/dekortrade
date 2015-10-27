package hu.dekortrade.client.raktar.beszallitas;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.CommonUtils;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.client.UserInfo;
import hu.dekortrade.shared.serialized.BeszallitottcikkSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class BeszallitottCikkDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private BeszallitasLabels beszallitasLabels = GWT.create(BeszallitasLabels.class);

	public BeszallitottCikkDataSource() {

		DataSourceField field;

		field = new DataSourceTextField(BeszallitasConstants.BESZALLITAS_CIKKSZAM,
				beszallitasLabels.beszallitas_cikkszam());
		addField(field);
		
		field = new DataSourceTextField(BeszallitasConstants.BESZALLITAS_SZINKOD,
				beszallitasLabels.beszallitas_szinkod());
		addField(field);
		
		field = new DataSourceIntegerField(BeszallitasConstants.BESZALLITAS_EXPORTKARTON,
				beszallitasLabels.beszallitas_exportkarton());
		addField(field);

		field = new DataSourceIntegerField(BeszallitasConstants.BESZALLITAS_KISKARTON,
				beszallitasLabels.beszallitas_kiskarton());
		addField(field);

		field = new DataSourceIntegerField(BeszallitasConstants.BESZALLITAS_DARAB,
				beszallitasLabels.beszallitas_darab());
		addField(field);

		field = new DataSourceIntegerField(BeszallitasConstants.BESZALLITAS_MEGRENDEXPORTKARTON,
				beszallitasLabels.beszallitas_megrendexportkarton());
		addField(field);

		field = new DataSourceIntegerField(BeszallitasConstants.BESZALLITAS_MEGRENDKISKARTON,
				beszallitasLabels.beszallitas_megrendkiskarton());
		addField(field);

		field = new DataSourceIntegerField(BeszallitasConstants.BESZALLITAS_MEGRENDDARAB,
				beszallitasLabels.beszallitas_megrenddarab());
		addField(field);

		field = new DataSourceTextField(BeszallitasConstants.BESZALLITAS_ROGZITO,
				beszallitasLabels.beszallitas_rogzito());
		addField(field);

		field = new DataSourceDateField(BeszallitasConstants.BESZALLITAS_DATUM,
				beszallitasLabels.beszallitas_datum());
		addField(field);

		field = new DataSourceBooleanField(BeszallitasConstants.BESZALLITAS_ROVANCS,
				beszallitasLabels.beszallitas_rovancs());
		field.setHidden(true);
		addField(field);
	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getBeszallitottcikk(request.getCriteria().getAttributeAsString(
				BeszallitasConstants.BESZALLITAS_CIKKSZAM),request.getCriteria().getAttributeAsString(
						BeszallitasConstants.BESZALLITAS_SZINKOD), new AsyncCallback<List<BeszallitottcikkSer>>() {
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

			public void onSuccess(List<BeszallitottcikkSer> result) {
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
		BeszallitottcikkSer beszallitottcikkSer = new BeszallitottcikkSer();
		beszallitottcikkSer.setRogzito(UserInfo.userId);
		beszallitottcikkSer.setRovancs(Boolean.FALSE);
		copyValues(rec, beszallitottcikkSer);
		dekorTradeService.addBeszallitottcikk(beszallitottcikkSer, new AsyncCallback<BeszallitottcikkSer>() {
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

			public void onSuccess(BeszallitottcikkSer result) {
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

	private static void copyValues(BeszallitottcikkSer  from, ListGridRecord to) {
		to.setAttribute(BeszallitasConstants.BESZALLITAS_CIKKSZAM, from.getCikkszam());
		to.setAttribute(BeszallitasConstants.BESZALLITAS_SZINKOD, from.getSzinkod());
		to.setAttribute(BeszallitasConstants.BESZALLITAS_EXPORTKARTON, from.getExportkarton());
		to.setAttribute(BeszallitasConstants.BESZALLITAS_KISKARTON, from.getKiskarton());
		to.setAttribute(BeszallitasConstants.BESZALLITAS_DARAB, from.getDarab());
		to.setAttribute(BeszallitasConstants.BESZALLITAS_MEGRENDEXPORTKARTON, from.getMegrendexportkarton());
		to.setAttribute(BeszallitasConstants.BESZALLITAS_MEGRENDKISKARTON, from.getMegrendkiskarton());
		to.setAttribute(BeszallitasConstants.BESZALLITAS_MEGRENDDARAB, from.getMegrenddarab());
		to.setAttribute(BeszallitasConstants.BESZALLITAS_ROGZITO, from.getRogzito());
		to.setAttribute(BeszallitasConstants.BESZALLITAS_DATUM, from.getDatum());		
		to.setAttribute(BeszallitasConstants.BESZALLITAS_ROVANCS, from.getRovancs());		
		to.setAttribute(BeszallitasConstants.BESZALLITAS_ROGZITO, from.getRogzito());	
		if (from.getRovancs().booleanValue()) {
			to.set_baseStyle("myRedGridCell");			
		}

	}

	private static void copyValues(ListGridRecord from, BeszallitottcikkSer to) {
		to.setCikkszam(from.getAttributeAsString(BeszallitasConstants.BESZALLITAS_CIKKSZAM));
		to.setSzinkod(from.getAttributeAsString(BeszallitasConstants.BESZALLITAS_SZINKOD));
		to.setExportkarton(CommonUtils.intCheck(from.getAttributeAsInt(BeszallitasConstants.BESZALLITAS_EXPORTKARTON)));
		to.setKiskarton(CommonUtils.intCheck(from.getAttributeAsInt(BeszallitasConstants.BESZALLITAS_KISKARTON)));	
		to.setDarab(CommonUtils.intCheck(from.getAttributeAsInt(BeszallitasConstants.BESZALLITAS_DARAB)));	
		to.setMegrendexportkarton(CommonUtils.intCheck(from.getAttributeAsInt(BeszallitasConstants.BESZALLITAS_MEGRENDEXPORTKARTON)));	
		to.setMegrendkiskarton(CommonUtils.intCheck(from.getAttributeAsInt(BeszallitasConstants.BESZALLITAS_MEGRENDKISKARTON)));	
		to.setMegrenddarab(CommonUtils.intCheck(from.getAttributeAsInt(BeszallitasConstants.BESZALLITAS_MEGRENDDARAB)));	
		to.setRogzito(from.getAttributeAsString(BeszallitasConstants.BESZALLITAS_ROGZITO));	
		to.setDatum(from.getAttributeAsDate(BeszallitasConstants.BESZALLITAS_DATUM));	
		to.setRovancs(from.getAttributeAsBoolean(BeszallitasConstants.BESZALLITAS_ROVANCS));	
		to.setRogzito(from.getAttributeAsString(BeszallitasConstants.BESZALLITAS_ROGZITO));	
	}

}
