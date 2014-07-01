package hu.dekortrade.client;

import hu.dekortrade.shared.serialized.CedulaSer;
import hu.dekortrade.shared.serialized.CedulacikkSer;
import hu.dekortrade.shared.serialized.CikkSelectsSer;
import hu.dekortrade.shared.serialized.CikkSer;
import hu.dekortrade.shared.serialized.CikkaltipusSer;
import hu.dekortrade.shared.serialized.CikkfotipusSer;
import hu.dekortrade.shared.serialized.FelhasznaloSer;
import hu.dekortrade.shared.serialized.GyartoSer;
import hu.dekortrade.shared.serialized.JogSer;
import hu.dekortrade.shared.serialized.KosarSer;
import hu.dekortrade.shared.serialized.LoginExceptionSer;
import hu.dekortrade.shared.serialized.RendeltSer;
import hu.dekortrade.shared.serialized.RendeltcikkSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.SzinkronSer;
import hu.dekortrade.shared.serialized.UploadSer;
import hu.dekortrade.shared.serialized.UserSer;
import hu.dekortrade.shared.serialized.VevoKosarSer;
import hu.dekortrade.shared.serialized.VevoSer;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("service")
public interface DekorTradeService extends RemoteService {

	UserSer getUser(String user, String password)
			throws IllegalArgumentException, SQLExceptionSer, LoginExceptionSer;

	void setPassword(String user, String password)
			throws IllegalArgumentException, SQLExceptionSer;

	List<FelhasznaloSer> getFelhasznalo() throws IllegalArgumentException,
			SQLExceptionSer;

	FelhasznaloSer addFelhasznalo(FelhasznaloSer felhasznaloSer)
			throws IllegalArgumentException, SQLExceptionSer;

	FelhasznaloSer updateFelhasznalo(FelhasznaloSer felhasznaloSer)
			throws IllegalArgumentException, SQLExceptionSer;

	String setFelhasznaloJelszo(String rovidnev)
			throws IllegalArgumentException, SQLExceptionSer;

	FelhasznaloSer removeFelhasznalo(FelhasznaloSer felhasznaloSer)
			throws IllegalArgumentException, SQLExceptionSer;

	JogSer updateJog(String rovidnev, JogSer jogSer)
			throws IllegalArgumentException, SQLExceptionSer;

	List<JogSer> getJog(String rovidnev) throws IllegalArgumentException,
			SQLExceptionSer;

	List<GyartoSer> getGyarto() throws IllegalArgumentException,
			SQLExceptionSer;

	GyartoSer addGyarto(GyartoSer szallitoSer) throws IllegalArgumentException,
			SQLExceptionSer;

	GyartoSer updateGyarto(GyartoSer szallitoSer)
			throws IllegalArgumentException, SQLExceptionSer;

	GyartoSer removeGyarto(GyartoSer szallitoSer)
			throws IllegalArgumentException, SQLExceptionSer;

	VevoSer addVevo(VevoSer vevoSer) throws IllegalArgumentException,
			SQLExceptionSer;

	VevoSer updateVevo(VevoSer vevoSer) throws IllegalArgumentException,
			SQLExceptionSer;

	String setVevoJelszo(String rovidnev) throws IllegalArgumentException,
			SQLExceptionSer;

	VevoSer removeVevo(VevoSer vevoSer) throws IllegalArgumentException,
			SQLExceptionSer;

	List<VevoSer> getVevo() throws Exception, SQLExceptionSer;

	List<CikkSer> getCikk(int page, String fotipus, String altipus,
			String cikkszam) throws IllegalArgumentException, SQLExceptionSer;

	CikkSer addCikk(CikkSer cikkSer) throws IllegalArgumentException,
			SQLExceptionSer;

	CikkSer updateCikk(CikkSer cikkSer) throws IllegalArgumentException,
			SQLExceptionSer;

	CikkSer removeCikk(CikkSer ctorzsSer) throws IllegalArgumentException,
			SQLExceptionSer;

	List<RendeltSer> getRendelt() throws IllegalArgumentException,
			SQLExceptionSer;

	List<RendeltcikkSer> getRendeltcikk(String rovidnev, String rendeles)
			throws IllegalArgumentException, SQLExceptionSer;

	SzinkronSer szinkron() throws IllegalArgumentException, SQLExceptionSer;

	String teljesszinkron() throws IllegalArgumentException, SQLExceptionSer;

	String initUploadFileStatus() throws IllegalArgumentException;

	UploadSer getUploadFileStatus() throws IllegalArgumentException;

	List<String> getKep(String cikkszam) throws IllegalArgumentException,
			SQLExceptionSer;

	String removeKep(String cikkszam, String rorszam)
			throws IllegalArgumentException, SQLExceptionSer;

	List<CikkfotipusSer> getCikkfotipus() throws IllegalArgumentException,
			SQLExceptionSer;

	CikkfotipusSer addCikkfotipus(CikkfotipusSer rcikkfotipusSe)
			throws IllegalArgumentException, SQLExceptionSer;

	CikkfotipusSer updateCikkfotipus(CikkfotipusSer cikkfotipusSer)
			throws IllegalArgumentException, SQLExceptionSer;

	List<CikkaltipusSer> getCikkaltipus(String fokod)
			throws IllegalArgumentException, SQLExceptionSer;

	CikkaltipusSer addCikkaltipus(CikkaltipusSer cikktipusSer)
			throws IllegalArgumentException, SQLExceptionSer;

	CikkaltipusSer updateCikkaltipus(CikkaltipusSer cikktipusSer)
			throws IllegalArgumentException, SQLExceptionSer;

	CikkSelectsSer getCikkSelects() throws IllegalArgumentException,
			SQLExceptionSer;

	List<KosarSer> getKosarCikk(String elado, String vevo, String tipus)
			throws IllegalArgumentException, SQLExceptionSer;

	VevoKosarSer getVevoKosar(String elado, String tipus)
			throws IllegalArgumentException, SQLExceptionSer;

	String addKosar(String elado, String vevo, String tipus)
			throws IllegalArgumentException, SQLExceptionSer;

	String removeKosar(String elado, String vevo, String tipus)
			throws IllegalArgumentException, SQLExceptionSer;

	KosarSer addKosarCikk(KosarSer kosarSer)
			throws IllegalArgumentException, SQLExceptionSer;

	KosarSer updateKosarCikk(KosarSer kosarSer)
			throws IllegalArgumentException, SQLExceptionSer;

	KosarSer removeKosarCikk(KosarSer kosarSer)
			throws IllegalArgumentException, SQLExceptionSer;
	
	String importInternet(String elado, String vevo, String rendeles)
			throws IllegalArgumentException, SQLExceptionSer;

	String createCedula(String elado, String vevo, String tipus)
			throws IllegalArgumentException, SQLExceptionSer;

	List<CedulaSer> getCedula() throws IllegalArgumentException,
			SQLExceptionSer;

	List<CedulacikkSer> getCedulacikk(String cedula, String tipus) throws IllegalArgumentException,
			SQLExceptionSer;

}
