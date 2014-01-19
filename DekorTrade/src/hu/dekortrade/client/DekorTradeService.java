package hu.dekortrade.client;

import hu.dekortrade.shared.serialized.CikkSer;
import hu.dekortrade.shared.serialized.FelhasznaloSer;
import hu.dekortrade.shared.serialized.JogSer;
import hu.dekortrade.shared.serialized.LoginExceptionSer;
import hu.dekortrade.shared.serialized.RendeltSer;
import hu.dekortrade.shared.serialized.RendeltcikkSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.SzallitoSer;
import hu.dekortrade.shared.serialized.UserSer;
import hu.dekortrade.shared.serialized.VevoSer;

import java.util.ArrayList;
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

	List<SzallitoSer> getSzallito() throws IllegalArgumentException,
			SQLExceptionSer;

	SzallitoSer addSzallito(SzallitoSer szallitoSer)
			throws IllegalArgumentException, SQLExceptionSer;

	SzallitoSer updateSzallito(SzallitoSer szallitoSer)
			throws IllegalArgumentException, SQLExceptionSer;

	SzallitoSer removeSzallito(SzallitoSer szallitoSer)
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

	List<CikkSer> getCikk(int page, String cikkszam, String jel)
			throws IllegalArgumentException, SQLExceptionSer;

	CikkSer addCikk(CikkSer cikkSer) throws IllegalArgumentException,
			SQLExceptionSer;

	CikkSer updateCikk(CikkSer cikkSer)
			throws IllegalArgumentException, SQLExceptionSer;

	CikkSer removeCikk(CikkSer ctorzsSer)
			throws IllegalArgumentException, SQLExceptionSer;

	ArrayList<RendeltSer> getRendelt() throws IllegalArgumentException,
			SQLExceptionSer;

	ArrayList<RendeltcikkSer> getRendeltcikk(String rovidnev, String rendeles)
			throws IllegalArgumentException, SQLExceptionSer;

	String szinkron() throws IllegalArgumentException, SQLExceptionSer;

}
