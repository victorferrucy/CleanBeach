package fi.pss.cleanbeach.ui.views.login;

import java.io.Serializable;

import com.vaadin.cdi.UIScoped;

import fi.pss.cleanbeach.ui.mvp.AbstractPresenter;

@UIScoped
public class LoginPresenter extends AbstractPresenter<ILogin> implements
		Serializable {

	private static final long serialVersionUID = 3951507517016979359L;

	protected LoginPresenter() {
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

}
