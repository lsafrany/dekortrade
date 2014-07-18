package hu.dekortrade.client;

import hu.dekortrade.client.eladas.Eladas;
import hu.dekortrade.client.lekerdezes.Lekerdezes;
import hu.dekortrade.client.penztar.Penztar;
import hu.dekortrade.client.raktar.Raktar;
import hu.dekortrade.client.rendeles.Rendeles;
import hu.dekortrade.client.rendszer.Rendszer;
import hu.dekortrade.client.torzsadat.Torzsadat;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.LoginExceptionSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.UserSer;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.MatchesFieldValidator;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DekorTrade implements EntryPoint {
	/**
	 * Create a remote service proxy to talk to the server-side DekorTrade
	 * service.
	 */
	private final DekorTradeServiceAsync dekorTradeService = GWT
			.create(DekorTradeService.class);

	private DekorTradeLabels dekorTradeLabels = GWT
			.create(DekorTradeLabels.class);

	private CommonLabels commonLabels = GWT.create(CommonLabels.class);

	private final Label statusLabel = new Label();

	private final HLayout topLayoutRight = new HLayout();
	private final VLayout middleLayout = new VLayout();
	private final HLayout topLayoutMiddleBottom = new HLayout();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		VLayout mainLayout = new VLayout();
		mainLayout.setTitle("DekorTrade");
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");

		HLayout topLayout = new HLayout();
		topLayout.setStyleName("top");
		topLayout.setWidth("100%");
		topLayout.setHeight("80px");

		HLayout topLayoutLeft = new HLayout();
		topLayoutLeft.setStyleName("top");
		topLayoutLeft.setWidth("200px");
		topLayoutLeft.setHeight("100%");
		topLayoutLeft.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		Img companyImg = new Img("company_logo.png", 200, 62);
		topLayoutLeft.addMember(companyImg);

		VLayout topLayoutMiddle = new VLayout();
		topLayoutMiddle.setStyleName("top");
		topLayoutMiddle.setHeight("100%");
		topLayoutMiddle.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		HLayout topLayoutMiddleTop = new HLayout();
		topLayoutMiddleTop.setStyleName("top");

		topLayoutMiddleTop.setHeight("50%");
		topLayoutMiddleTop.setAlign(Alignment.CENTER);

		Label titleLabel = new Label();
		titleLabel.setContents("<h1>DekorTrade</h1>");
		titleLabel.setAlign(Alignment.CENTER);
		titleLabel.setWidth("100%");
		topLayoutMiddleTop.addMember(titleLabel);

		topLayoutMiddleBottom.setStyleName("top");
		topLayoutMiddleBottom.setHeight("50%");

		topLayoutMiddle.addMember(topLayoutMiddleTop);

		Label versionLabel = new Label();
		versionLabel.setContents(dekorTradeLabels.version() + " : 0.1");
		versionLabel.setStyleName("version_label");
		versionLabel.setAlign(Alignment.CENTER);
		versionLabel.setWidth("100%");
		topLayoutMiddleBottom.addMember(versionLabel);
		topLayoutMiddle.addMember(topLayoutMiddleBottom);

		topLayoutRight.setStyleName("top");
		topLayoutRight.setWidth("330px");
		topLayoutRight.setHeight("100%");
		topLayoutRight.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		topLayout.addMember(topLayoutLeft);
		topLayout.addMember(topLayoutMiddle);
		topLayout.addMember(topLayoutRight);

		middleLayout.setDefaultLayoutAlign(VerticalAlignment.TOP);
		middleLayout.setWidth("100%");
		middleLayout.setHeight("75%");
		middleLayout.setStyleName("middle");

		HLayout bottomLayout = new HLayout();
		bottomLayout.setWidth("100%");
		bottomLayout.setHeight("30px");

		HLayout bottomLayoutLeft = new HLayout();
		bottomLayoutLeft.setStyleName("bottom");
		bottomLayoutLeft.setHeight("100%");
		bottomLayoutLeft.setDefaultLayoutAlign(VerticalAlignment.CENTER);

		statusLabel.setWidth100();
		bottomLayoutLeft.addMember(statusLabel);

		HLayout bottomLayoutRight = new HLayout();
		bottomLayoutRight.setStyleName("bottom");
		bottomLayoutRight.setWidth("180px");
		bottomLayoutRight.setHeight("100%");

		DisplayRequest.getProgressBar().setVertical(false);
		bottomLayoutRight.addMember(DisplayRequest.getProgressBar());

		bottomLayout.addMember(bottomLayoutLeft);
		bottomLayout.addMember(bottomLayoutRight);

		mainLayout.addMember(topLayout);
		mainLayout.addMember(middleLayout);
		mainLayout.addMember(bottomLayout);

		mainLayout.draw();

		DisplayRequest.counterInit();

		RootPanel.getBodyElement().removeChild(
				RootPanel.get("loadingWrapper").getElement());

		if (Cookies.getCookie(ClientConstants.COOKIE) != null)
			UserInfo.userId = (Cookies.getCookie(ClientConstants.COOKIE));
		if (UserInfo.userId.equals(""))
			topLayoutRight.addMember(getLogin());
		else
			getUser(UserInfo.userId, null, null);

		new Timer() {
			public void run() {
				if (DisplayRequest.getProgressBarValue() >= 0) {
					DisplayRequest.setProgressBarValue(DisplayRequest
							.getProgressBarValue() + 10);
					if (DisplayRequest.getProgressBarValue() > 100)
						DisplayRequest
								.setProgressBarValue(ClientConstants.PROGRESS_START);
					DisplayRequest.getProgressBar().setPercentDone(
							DisplayRequest.getProgressBarValue());
				}
				schedule(ClientConstants.PROGRESS_SCHEDULE);
			}
		}.schedule(ClientConstants.PROGRESS_SCHEDULE);

	}

	private HLayout getLogin() {
		topLayoutRight.removeMembers(topLayoutRight.getMembers());
		final DynamicForm form = new DynamicForm();
		form.setPadding(5);
		form.setLayoutAlign(VerticalAlignment.CENTER);

		final TextItem userIdItem = new TextItem();
		userIdItem.setTitle(dekorTradeLabels.login_usedId());
		userIdItem.setLength(15);
		userIdItem.setRequired(true);

		final PasswordItem passwordItem = new PasswordItem();
		passwordItem.setTitle(dekorTradeLabels.login_password());
		passwordItem.setLength(15);
		passwordItem.setRequired(true);

		final IButton loginButtonItem = new IButton(
				dekorTradeLabels.login_login());
		loginButtonItem.setDisabled(true);

		final CheckboxItem passwordCheckboxItem = new CheckboxItem();
		passwordCheckboxItem.setTitle(dekorTradeLabels.newPassword());

		userIdItem.addChangedHandler(new ChangedHandler() {
			public void onChanged(ChangedEvent event) {
				if ((userIdItem.getValueAsString() != null)
						&& (passwordItem.getValueAsString() != null))
					loginButtonItem.setDisabled(false);
				else
					loginButtonItem.setDisabled(true);
			}
		});

		passwordItem.addChangedHandler(new ChangedHandler() {
			public void onChanged(ChangedEvent event) {
				if ((userIdItem.getValueAsString() != null)
						&& (passwordItem.getValueAsString() != null))
					loginButtonItem.setDisabled(false);
				else
					loginButtonItem.setDisabled(true);
			}
		});

		loginButtonItem.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if ((userIdItem.getValue() != null)
						&& (passwordItem.getValue()) != null) {
					if ((passwordCheckboxItem.getValue() == null)
							|| !passwordCheckboxItem.getValueAsBoolean())
						getUser(userIdItem.getValue().toString(), passwordItem
								.getValue().toString(), null);
					else
						getUser(userIdItem.getValue().toString(), passwordItem
								.getValue().toString(), passwordCheckboxItem
								.getValue().toString());
				}
			}
		});

		userIdItem.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getKeyName() != null) {
					if (event.getKeyName() != null) {
						String keyName = event.getKeyName();
						if (keyName.equals("Enter")) {
							if ((userIdItem.getValue() != null)
									&& (passwordItem.getValue()) != null) {
								if (passwordCheckboxItem.getValue() == null)
									getUser(userIdItem.getValue().toString(),
											passwordItem.getValue().toString(),
											null);
								else
									getUser(userIdItem.getValue().toString(),
											passwordItem.getValue().toString(),
											passwordCheckboxItem.getValue()
													.toString());
							}
						}
					}
				}
			}
		});

		passwordItem.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getKeyName() != null) {
					if (event.getKeyName() != null) {
						String keyName = event.getKeyName();
						if (keyName.equals("Enter")) {
							if ((userIdItem.getValue() != null)
									&& (passwordItem.getValue()) != null) {
								if (passwordCheckboxItem.getValue() == null)
									getUser(userIdItem.getValue().toString(),
											passwordItem.getValue().toString(),
											null);
								else
									getUser(userIdItem.getValue().toString(),
											passwordItem.getValue().toString(),
											passwordCheckboxItem.getValue()
													.toString());
							}
						}
					}
				}
			}
		});

		form.setFields(userIdItem, passwordItem, passwordCheckboxItem);
		HLayout hLayout = new HLayout();
		hLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		hLayout.addMember(form);
		hLayout.addMember(loginButtonItem);
		return hLayout;
	}

	private HLayout getLogout(String name) {
		topLayoutRight.removeMembers(topLayoutRight.getMembers());
		Label nameLabel = new Label(name);
		IButton logoutButtonItem = new IButton(dekorTradeLabels.logout_logout());

		logoutButtonItem.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Cookies.removeCookie(ClientConstants.COOKIE);
				topLayoutRight.addMember(getLogin());
				middleLayout.removeMembers(middleLayout.getMembers());
				UserInfo.clearValues();
			}
		});

		HLayout hLayout = new HLayout();
		hLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		hLayout.addMember(nameLabel);
		hLayout.addMember(logoutButtonItem);
		return hLayout;
	}

	private void getUser(final String userId, final String password,
			final String passwordSetting) {

		DisplayRequest.startRequest();
		dekorTradeService.getUser(userId, password,
				new AsyncCallback<UserSer>() {
					public void onFailure(Throwable caught) {
						DisplayRequest.serverResponse();
						Cookies.removeCookie(ClientConstants.COOKIE);
						if (caught instanceof LoginExceptionSer)
							SC.warn(dekorTradeLabels.login_error());
						else if (caught instanceof SQLExceptionSer)
							SC.warn(commonLabels.server_sqlerror() + " : "
									+ caught.getMessage());
						else
							SC.warn(commonLabels.server_error());
					}

					public void onSuccess(final UserSer userSer) {
						DisplayRequest.serverResponse();
						Date now = new Date();
						Date expire = new Date(now.getTime()
								- (ClientConstants.COOKIE_EXPIRE * 86400000));
						Cookies.setCookie(ClientConstants.COOKIE, userId,
								expire);

						UserInfo.userId = userSer.getUserId();

						UserInfo.menu = userSer.getMenu();

						UserInfo.defaultTab = userSer.getDefultTab();

						if ((password != null)
								&& (password.equals(Constants.INIT_PASSWORD))) {
							topLayoutRight.addMember(getPassword(userSer
									.getName()));
						} else {
							if ((passwordSetting == null)
									|| ((password != null) && (!password
											.equals(Constants.INIT_PASSWORD))))
								topLayoutRight.addMember(getLogout(userSer
										.getName()));
							else
								topLayoutRight.addMember(getPassword(userSer
										.getName()));
						}

						middleLayout.removeMembers(middleLayout.getMembers());

						final TabSet tabSet = new TabSet();
						tabSet.setTabBarPosition(Side.TOP);
						tabSet.setTabBarAlign(Side.LEFT);
						tabSet.setWidth100();
						tabSet.setHeight100();
						middleLayout.addMember(tabSet);

						for (int i = 0; i < userSer.getTabList().size(); i++) {

							if (userSer.getTabList().get(i).getName()
									.equals(Constants.MENU_RENDSZER)) {

								final Tab tab = new Tab(commonLabels
										.menu_rendszer());
								tabSet.addTab(tab);
								final Rendszer rendszer = new Rendszer();

								if (userSer.getTabList().get(i).getId() == userSer
										.getDefultTab()) {

									tab.setPane(rendszer.get());
									tabSet.selectTab(i);
								}
								tab.addTabSelectedHandler(new TabSelectedHandler() {

									@Override
									public void onTabSelected(
											TabSelectedEvent event) {

										tab.setPane(rendszer.get());
									}

								});
							}

							if (userSer.getTabList().get(i).getName()
									.equals(Constants.MENU_TORZSADAT)) {

								final Tab tab = new Tab(commonLabels
										.menu_torzsadat());
								tabSet.addTab(tab);
								final Torzsadat torzsadat = new Torzsadat();

								if (userSer.getTabList().get(i).getId() == userSer
										.getDefultTab()) {

									tab.setPane(torzsadat.get());
									tabSet.selectTab(i);
								}
								tab.addTabSelectedHandler(new TabSelectedHandler() {

									@Override
									public void onTabSelected(
											TabSelectedEvent event) {

										tab.setPane(torzsadat.get());
									}

								});
							}

							if (userSer.getTabList().get(i).getName()
									.equals(Constants.MENU_RENDELES)) {

								final Tab tab = new Tab(commonLabels
										.menu_rendeles());
								tabSet.addTab(tab);
								final Rendeles rendeles = new Rendeles();

								if (userSer.getTabList().get(i).getId() == userSer
										.getDefultTab()) {

									tab.setPane(rendeles.get());
									tabSet.selectTab(i);
								}
								tab.addTabSelectedHandler(new TabSelectedHandler() {

									@Override
									public void onTabSelected(
											TabSelectedEvent event) {

										tab.setPane(rendeles.get());
									}

								});
							}

							if (userSer.getTabList().get(i).getName()
									.equals(Constants.MENU_RAKTAR)) {

								final Tab tab = new Tab(commonLabels
										.menu_raktar());
								tabSet.addTab(tab);
								final Raktar raktar = new Raktar();

								if (userSer.getTabList().get(i).getId() == userSer
										.getDefultTab()) {

									tab.setPane(raktar.get());
									tabSet.selectTab(i);
								}
								tab.addTabSelectedHandler(new TabSelectedHandler() {

									@Override
									public void onTabSelected(
											TabSelectedEvent event) {

										tab.setPane(raktar.get());
									}

								});
							}

							if (userSer.getTabList().get(i).getName()
									.equals(Constants.MENU_ELADAS)) {

								final Tab tab = new Tab(commonLabels
										.menu_eladas());
								tabSet.addTab(tab);
								final Eladas eladas = new Eladas();

								if (userSer.getTabList().get(i).getId() == userSer
										.getDefultTab()) {

									tab.setPane(eladas.get());
									tabSet.selectTab(i);
								}
								tab.addTabSelectedHandler(new TabSelectedHandler() {

									@Override
									public void onTabSelected(
											TabSelectedEvent event) {

										tab.setPane(eladas.get());
									}

								});
							}

							if (userSer.getTabList().get(i).getName()
									.equals(Constants.MENU_PENZTAR)) {

								final Tab tab = new Tab(commonLabels
										.menu_penztar());
								tabSet.addTab(tab);
								final Penztar penztar = new Penztar();

								if (userSer.getTabList().get(i).getId() == userSer
										.getDefultTab()) {

									tab.setPane(penztar.get());
									tabSet.selectTab(i);
								}
								tab.addTabSelectedHandler(new TabSelectedHandler() {

									@Override
									public void onTabSelected(
											TabSelectedEvent event) {

										tab.setPane(penztar.get());
									}

								});
							}

							if (userSer.getTabList().get(i).getName()
									.equals(Constants.MENU_LEKERDEZES)) {

								final Tab tab = new Tab(commonLabels
										.menu_lekerdezes());
								tabSet.addTab(tab);
								final Lekerdezes lekerdezes = new Lekerdezes();

								if (userSer.getTabList().get(i).getId() == userSer
										.getDefultTab()) {

									tab.setPane(lekerdezes.get());
									tabSet.selectTab(i);
								}
								tab.addTabSelectedHandler(new TabSelectedHandler() {

									@Override
									public void onTabSelected(
											TabSelectedEvent event) {

										tab.setPane(lekerdezes.get());
									}

								});
							}

						}
					}
				});
		;
	}

	private HLayout getPassword(final String name) {

		topLayoutRight.removeMembers(topLayoutRight.getMembers());

		final DynamicForm form = new DynamicForm();
		form.setPadding(5);
		form.setLayoutAlign(VerticalAlignment.CENTER);

		final PasswordItem passwordItem = new PasswordItem();
		passwordItem.setName("password");
		passwordItem.setTitle(dekorTradeLabels.newPassword());
		passwordItem.setRequired(true);
		passwordItem.setLength(20);

		final PasswordItem passwordItem2 = new PasswordItem();
		passwordItem2.setName("password2");
		passwordItem2.setTitle(dekorTradeLabels.password_passwordAgain());
		passwordItem2.setRequired(true);
		passwordItem2.setLength(20);

		MatchesFieldValidator matchesValidator = new MatchesFieldValidator();
		matchesValidator.setOtherField("password");
		matchesValidator.setErrorMessage(dekorTradeLabels.password_error1());

		CustomValidator passwordValidator = new CustomValidator() {
			@Override
			protected boolean condition(Object value) {
				if (value == null)
					return false;
				String password = value.toString();
				if (password.equals(Constants.INIT_PASSWORD))
					return false;
				return true;
			}
		};
		passwordValidator.setErrorMessage(dekorTradeLabels.password_error2());
		passwordItem2.setValidators(passwordValidator, matchesValidator);

		form.setFields(passwordItem, passwordItem2);
		final IButton changeButtonItem = new IButton(
				dekorTradeLabels.password_change());
		changeButtonItem.setDisabled(true);

		passwordItem.addChangedHandler(new ChangedHandler() {
			public void onChanged(ChangedEvent event) {
				if ((passwordItem.getValueAsString() != null)
						&& (passwordItem2.getValueAsString() != null))
					changeButtonItem.setDisabled(false);
				else
					changeButtonItem.setDisabled(true);
			}
		});

		passwordItem2.addChangedHandler(new ChangedHandler() {
			public void onChanged(ChangedEvent event) {
				if ((passwordItem.getValueAsString() != null)
						&& (passwordItem2.getValueAsString() != null))
					changeButtonItem.setDisabled(false);
				else
					changeButtonItem.setDisabled(true);
			}
		});

		topLayoutRight.addMember(form);
		topLayoutRight.addMember(changeButtonItem);

		changeButtonItem.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form.validate(false)) {
					DisplayRequest.startRequest();
					dekorTradeService.setPassword(UserInfo.userId, passwordItem
							.getValue().toString(),
							new AsyncCallback<String>() {
								public void onFailure(Throwable caught) {
									DisplayRequest.serverResponse();
									if (caught instanceof SQLExceptionSer)
										SC.warn(commonLabels.server_sqlerror()
												+ " : " + caught.getMessage());
									else
										SC.warn(commonLabels.server_error());
								}

								public void onSuccess(String result) {
									DisplayRequest.serverResponse();
									topLayoutRight.addMember(getLogout(name));
								}
							});
				}
			}
		});

		passwordItem.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getKeyName() != null) {
					if (event.getKeyName() != null) {
						String keyName = event.getKeyName();
						if (keyName.equals("Enter")) {
							if (form.validate(false)) {
								DisplayRequest.startRequest();
								dekorTradeService.setPassword(UserInfo.userId,
										passwordItem.getValue().toString(),
										new AsyncCallback<String>() {
											public void onFailure(
													Throwable caught) {
												DisplayRequest.serverResponse();
												if (caught instanceof SQLExceptionSer)
													SC.warn(commonLabels
															.server_sqlerror()
															+ " : "
															+ caught.getMessage());
												else
													SC.warn(commonLabels
															.server_error());
											}

											public void onSuccess(String result) {
												DisplayRequest.serverResponse();
												topLayoutRight
														.addMember(getLogout(name));
											}
										});
							}
						}
					}
				}
			}
		});

		passwordItem2.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getKeyName() != null) {
					if (event.getKeyName() != null) {
						String keyName = event.getKeyName();
						if (keyName.equals("Enter")) {
							if (form.validate(false)) {
								DisplayRequest.startRequest();
								dekorTradeService.setPassword(UserInfo.userId,
										passwordItem.getValue().toString(),
										new AsyncCallback<String>() {
											public void onFailure(
													Throwable caught) {
												DisplayRequest.serverResponse();
												if (caught instanceof SQLExceptionSer)
													SC.warn(commonLabels
															.server_sqlerror()
															+ " : "
															+ caught.getMessage());
												else
													SC.warn(commonLabels
															.server_error());
											}

											public void onSuccess(String result) {
												DisplayRequest.serverResponse();
												topLayoutRight
														.addMember(getLogout(name));
											}
										});
							}
						}
					}
				}
			}
		});

		HLayout hLayout = new HLayout();
		hLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		hLayout.addMember(form);
		hLayout.addMember(changeButtonItem);
		return hLayout;
	}
}