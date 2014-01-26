package hu.dekortrade.server;

import hu.dekortrade.server.jdo.Cikk;
import hu.dekortrade.server.jdo.PMF;
import hu.dekortrade.server.jdo.Rendelt;
import hu.dekortrade.server.jdo.Rendeltcikk;
import hu.dekortrade.server.jdo.Vevo;
import hu.dekortrade.server.sync.CikkSzinkron;
import hu.dekortrade.server.sync.KepSzinkron;
import hu.dekortrade.server.sync.RendeltSzinkron;
import hu.dekortrade.server.sync.RendeltcikkSzinkron;
import hu.dekortrade.server.sync.VevoSzinkron;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.SzinkronSer;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class SzinkronObject {

	private String uri = "http://127.0.0.1:8888/dekortrade99";

	SzinkronSer feldolgozas() throws Exception {

		SzinkronSer szinkronSer = new SzinkronSer();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query vevoquery = pm.newQuery(Vevo.class);
			vevoquery.setFilter("this.szinkron == false");
			vevoquery.setRange(1, 50);
			@SuppressWarnings("unchecked")
			List<Vevo> vevolist = (List<Vevo>) pm.newQuery(vevoquery).execute();
			List<VevoSzinkron> vevolistszinkron = new ArrayList<VevoSzinkron>();
			if ((vevolist != null) && (!vevolist.isEmpty())) {
				for (Vevo l : vevolist) {
					VevoSzinkron vevoSzinkron = new VevoSzinkron(
							l.getRovidnev(), l.getNev(), l.getTorolt());
					vevolistszinkron.add(vevoSzinkron);
					l.setSzinkron(Boolean.TRUE);
				}

				szinkronSer.setUploadcikk(vevolistszinkron.size());

				ObjectMapper vevomapper = new ObjectMapper();

				String vevoJSON = vevomapper
						.writeValueAsString(vevolistszinkron);

				Client vevoclient = Client.create();

				WebResource vevowebResource = vevoclient.resource(uri);

				ClientResponse vevoresponse = vevowebResource.path("syncron")
						.queryParam("table", "vevo").accept("application/json")
						.type("application/json")
						.post(ClientResponse.class, vevoJSON);

				// check response status code
				if (vevoresponse.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ vevoresponse.getStatus());
				}

				// display response
				String output = vevoresponse.getEntity(String.class);
				System.out.println("Output from Server .... ");
				System.out.println(output + "\n");

			}

			Query cikkquery = pm.newQuery(Cikk.class);
			cikkquery.setFilter("this.szinkron == false");
			cikkquery.setRange(1, 50);
			@SuppressWarnings("unchecked")
			List<Cikk> cikklist = (List<Cikk>) pm.newQuery(cikkquery).execute();
			List<CikkSzinkron> cikklistszinkron = new ArrayList<CikkSzinkron>();
			if ((cikklist != null) && (!cikklist.isEmpty())) {
				for (Cikk l : cikklist) {
					CikkSzinkron cikkSzinkron = new CikkSzinkron(
							l.getCikkszam(), l.getMegnevezes(), l.getAr(),
							l.getKiskarton(), l.getDarab(), l.getTerfogat(),
							l.getJel(), l.getBsuly(), l.getNsuly(),
							l.getKepek(), l.getTorolt());
					cikklistszinkron.add(cikkSzinkron);
					l.setSzinkron(Boolean.TRUE);
				}

				szinkronSer.setUploadcikk(cikklistszinkron.size());

				ObjectMapper cikkmapper = new ObjectMapper();

				String cikkJSON = cikkmapper
						.writeValueAsString(cikklistszinkron);

				Client cikkclient = Client.create();

				WebResource cikkwebResource = cikkclient.resource(uri);

				ClientResponse cikkresponse = cikkwebResource.path("syncron")
						.queryParam("table", "cikk").accept("application/json")
						.type("application/json")
						.post(ClientResponse.class, cikkJSON);

				// check response status code
				if (cikkresponse.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ cikkresponse.getStatus());
				}

				// display response
				String output = cikkresponse.getEntity(String.class);
				System.out.println("Output from Server .... ");
				System.out.println(output + "\n");
				
			}

			Query kepquery = pm.newQuery(Kep.class);
			vevoquery.setFilter("this.szinkron == false");
			vevoquery.setRange(1, 50);
			@SuppressWarnings("unchecked")
			List<Kep> keplist = (List<Kep>) pm.newQuery(kepquery).execute();
			List<KepSzinkron> keplistszinkron = new ArrayList<KepSzinkron>();
			if ((keplist != null) && (!keplist.isEmpty())) {
				for (Kep l : keplist) {
					KepSzinkron kepSzinkron = new KepSzinkron(l.getCikkszam(),
							l.getSorszam(), l.getBlob(), l.getTorolt());
					keplistszinkron.add(kepSzinkron);
					l.setSzinkron(Boolean.TRUE);
				}

				szinkronSer.setUploadkep(keplistszinkron.size());

				ObjectMapper kepmapper = new ObjectMapper();

				String kepJSON = kepmapper.writeValueAsString(vevolistszinkron);

				Client kepclient = Client.create();

				WebResource kepwebResource = kepclient.resource(uri);

				ClientResponse kepresponse = kepwebResource.path("syncron")
						.queryParam("table", "kep").accept("application/json")
						.type("application/json")
						.post(ClientResponse.class, kepJSON);

				// check response status code
				if (kepresponse.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ kepresponse.getStatus());
				}

				// display response
				String output = kepresponse.getEntity(String.class);
				System.out.println("Output from Server .... ");
				System.out.println(output + "\n");

			}
					
			Client rendeltclient = Client.create();

			WebResource rendeltwebResource = rendeltclient.resource(uri);

			ClientResponse rendeltresponse = rendeltwebResource.path("syncron")
					.queryParam("akcio", "rendel").type("application/json")
					.get(ClientResponse.class);

			if (rendeltresponse.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ rendeltresponse.getStatus());
			}

			String rendeltoutput = rendeltresponse.getEntity(String.class);

			ObjectMapper rendeltmapper = new ObjectMapper();

			List<RendeltSzinkron> rendeltSzinkronList = rendeltmapper.readValue(rendeltoutput,
					new TypeReference<List<RendeltSzinkron>>() {
					});

			
			for (int i = 0; i < rendeltSzinkronList.size(); i++) {
				RendeltSzinkron rendeltSzinkron = rendeltSzinkronList.get(i);
				Rendelt rendelt = new Rendelt(rendeltSzinkron.getRovidnev(),rendeltSzinkron.getRendeles(),rendeltSzinkron.getDatum());
				pm.makePersistent(rendelt);
				
				List<RendeltcikkSzinkron> rendeltcikkSzinkroList = rendeltSzinkron.getRendeltCikkszam();
				
				for (int j = 0; i < rendeltcikkSzinkroList.size(); j++) {
					RendeltcikkSzinkron rendeltcikkSzinkron = rendeltcikkSzinkroList.get(j);
					Rendeltcikk rendeltcikk = new Rendeltcikk(rendeltcikkSzinkron.getRovidnev(),rendeltcikkSzinkron.getRendeles(),rendeltcikkSzinkron.getCikkszam(),rendeltcikkSzinkron.getExportkarton());
					pm.makePersistent(rendeltcikk);
				}
			}
			
			szinkronSer.setDownloadrendelt(rendeltSzinkronList.size());
			
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return szinkronSer;
	}
}
