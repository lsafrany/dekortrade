package hu.dekortrade.client.kosarcikk;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.client.DekorTradeServiceAsync;
import hu.dekortrade.client.GwtRpcDataSource;
import hu.dekortrade.client.raktar.kesztlet.KeszletConstants;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.KosarSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class KosarCikkDataSource extends GwtRpcDataSource {

	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private KosarCikkLabels kosarCikkLabels = GWT.create(KosarCikkLabels.class);

	private String cedula = null;
	private String elado = null;
	private String vevo = null;
	private String menu = null;
	private String tipus = null;

	public KosarCikkDataSource(String cedula, String elado, String vevo,
			String menu) {
		this.cedula = cedula;
		this.elado = elado;
		this.vevo = vevo;
		this.menu = menu;

		if (menu.equals(Constants.MENU_RENDELES_ELORENDELES)) {
			this.tipus = Constants.CEDULA_STATUSZ_ELORENDELT;
		}
		if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)) {
			this.tipus = Constants.CEDULA_STATUSZ_VEGLEGESIT;
		}
		if (menu.equals(Constants.MENU_PENZTAR_FIZETES)) {
			this.tipus = "";
		}
		if (menu.equals(Constants.MENU_ELADAS)) {
			this.tipus = Constants.CEDULA_STATUSZ_ELADOTT;
		}
		if (menu.equals(Constants.MENU_RAKTAR_KIADAS)) {
			this.tipus = Constants.CEDULA_STATUSZ_KIADAS;
		}

		TextItem textItem = new TextItem();
		textItem.setWidth("200");

		DataSourceField field;

		field = new DataSourceTextField(KosarConstants.KOSAR_ELADO,
				kosarCikkLabels.kosar_elado());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(KosarConstants.KOSAR_VEVO,
				kosarCikkLabels.kosar_vevo());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(KosarConstants.KOSAR_TIPUS,
				kosarCikkLabels.kosar_tipus());
		field.setHidden(true);
		addField(field);

		field = new DataSourceTextField(KosarConstants.KOSAR_CIKKSZAM,
				kosarCikkLabels.kosar_cikkszam());
		field.setPrimaryKey(true);
		addField(field);

		field = new DataSourceTextField(KosarConstants.KOSAR_SZINKOD,
				kosarCikkLabels.kosar_szinkod());
		field.setPrimaryKey(true);
		addField(field);

		field = new DataSourceTextField(KosarConstants.KOSAR_MEGNEVEZES,
				kosarCikkLabels.kosar_megnevezes());
		field.setEditorProperties(textItem);
		addField(field);

		field = new DataSourceIntegerField(KosarConstants.KOSAR_EXPORTKARTON,
				kosarCikkLabels.kosar_exportkarton());
		addField(field);

		field = new DataSourceIntegerField(KosarConstants.KOSAR_KISKARTON,
				kosarCikkLabels.kosar_kiskarton());
		addField(field);

		field = new DataSourceIntegerField(KosarConstants.KOSAR_DARAB,
				kosarCikkLabels.kosar_darab());
		addField(field);

		field = new DataSourceFloatField(KosarConstants.KOSAR_AR,
				kosarCikkLabels.kosar_ar());
		addField(field);

		field = new DataSourceFloatField(KosarConstants.KOSAR_AREUR,
				kosarCikkLabels.kosar_areur());
		addField(field);

		field = new DataSourceFloatField(KosarConstants.KOSAR_ARUSD,
				kosarCikkLabels.kosar_arusd());
		addField(field);

		field = new DataSourceFloatField(KosarConstants.KOSAR_FIZET,
				kosarCikkLabels.kosar_fizet());
		addField(field);

		field = new DataSourceFloatField(KosarConstants.KOSAR_FIZETEUR,
				kosarCikkLabels.kosar_fizeteur());
		addField(field);

		field = new DataSourceFloatField(KosarConstants.KOSAR_FIZETUSD,
				kosarCikkLabels.kosar_fizetusd());
		addField(field);

		field = new DataSourceTextField(KosarConstants.KOSAR_RENDELES,
				kosarCikkLabels.kosar_rendeles());
		addField(field);

		field = new DataSourceTextField(KosarConstants.KOSAR_HELYKOD,
				kosarCikkLabels.kosar_hely());
		field.setValueMap(KeszletConstants.getHely());
		addField(field);

	}

	@Override
	protected void executeFetch(final String requestId,
			final DSRequest request, final DSResponse response) {
		dekorTradeService.getKosarCikk(this.elado, this.vevo, this.menu,
				new AsyncCallback<List<KosarSer>>() {
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

					public void onSuccess(List<KosarSer> result) {
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

		JavaScriptObject data = request.getData();
		ListGridRecord rec = new ListGridRecord(data);
		KosarSer kosarSer = new KosarSer();
		copyValues(rec, kosarSer);
		kosarSer.setCedula(this.cedula);
		kosarSer.setElado(this.elado);
		kosarSer.setVevo(this.vevo);
		kosarSer.setTipus(this.tipus);
		dekorTradeService.addKosarCikk(kosarSer, new AsyncCallback<KosarSer>() {
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

			public void onSuccess(KosarSer result) {
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

		JavaScriptObject data = request.getData();
		final ListGridRecord rec = new ListGridRecord(data);
		KosarSer kosarSer = new KosarSer();
		copyValues(rec, kosarSer);
		kosarSer.setCedula(this.cedula);
		kosarSer.setElado(this.elado);
		kosarSer.setVevo(this.vevo);
		kosarSer.setTipus(this.tipus);
		dekorTradeService.removeKosarCikk(kosarSer,
				new AsyncCallback<KosarSer>() {
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

					public void onSuccess(KosarSer result) {
						ListGridRecord[] list = new ListGridRecord[1];
						// We do not receive removed record from server.
						// Return record from request.
						setLastId(null);
						list[0] = rec;
						response.setData(list);
						processResponse(requestId, response);
					}

				});

	}

	private static void copyValues(KosarSer from, ListGridRecord to) {
		to.setAttribute(KosarConstants.KOSAR_ELADO, from.getElado());
		to.setAttribute(KosarConstants.KOSAR_VEVO, from.getVevo());
		to.setAttribute(KosarConstants.KOSAR_TIPUS, from.getTipus());
		to.setAttribute(KosarConstants.KOSAR_CIKKSZAM, from.getCikkszam());
		to.setAttribute(KosarConstants.KOSAR_SZINKOD, from.getSzinkod());
		to.setAttribute(KosarConstants.KOSAR_MEGNEVEZES, from.getMegnevezes());
		to.setAttribute(KosarConstants.KOSAR_EXPORTKARTON,
				from.getExportkarton());
		to.setAttribute(KosarConstants.KOSAR_KISKARTON, from.getKiskarton());
		to.setAttribute(KosarConstants.KOSAR_DARAB, from.getDarab());
		to.setAttribute(KosarConstants.KOSAR_AR, from.getAr());
		to.setAttribute(KosarConstants.KOSAR_AREUR, from.getAreur());
		to.setAttribute(KosarConstants.KOSAR_ARUSD, from.getArusd());
		to.setAttribute(KosarConstants.KOSAR_FIZET, from.getFizet());
		to.setAttribute(KosarConstants.KOSAR_FIZETEUR, from.getFizeteur());
		to.setAttribute(KosarConstants.KOSAR_FIZETUSD, from.getFizetusd());
		to.setAttribute(KosarConstants.KOSAR_RENDELES, from.getRendeles());
		to.setAttribute(KosarConstants.KOSAR_HELYKOD, from.getHelykod());
	}

	private static void copyValues(ListGridRecord from, KosarSer to) {
		to.setElado(from.getAttributeAsString(KosarConstants.KOSAR_ELADO));
		to.setVevo(from.getAttributeAsString(KosarConstants.KOSAR_VEVO));
		to.setTipus(from.getAttributeAsString(KosarConstants.KOSAR_TIPUS));
		to.setCikkszam(from.getAttributeAsString(KosarConstants.KOSAR_CIKKSZAM));
		to.setSzinkod(from.getAttributeAsString(KosarConstants.KOSAR_SZINKOD));
		to.setMegnevezes(from
				.getAttributeAsString(KosarConstants.KOSAR_MEGNEVEZES));
		to.setExportkarton(from
				.getAttributeAsInt(KosarConstants.KOSAR_EXPORTKARTON));
		to.setKiskarton(from.getAttributeAsInt(KosarConstants.KOSAR_KISKARTON));
		to.setDarab(from.getAttributeAsInt(KosarConstants.KOSAR_DARAB));
		to.setAr(from.getAttributeAsDouble(KosarConstants.KOSAR_AR));
		to.setAreur(from.getAttributeAsDouble(KosarConstants.KOSAR_AREUR));
		to.setArusd(from.getAttributeAsDouble(KosarConstants.KOSAR_ARUSD));
		to.setFizet(from.getAttributeAsDouble(KosarConstants.KOSAR_FIZET));
		to.setFizeteur(from.getAttributeAsDouble(KosarConstants.KOSAR_FIZETEUR));
		to.setFizetusd(from.getAttributeAsDouble(KosarConstants.KOSAR_FIZETUSD));
		to.setRendeles(from.getAttributeAsString(KosarConstants.KOSAR_RENDELES));
		to.setHelykod(from.getAttributeAsString(KosarConstants.KOSAR_HELYKOD));
	}

}
