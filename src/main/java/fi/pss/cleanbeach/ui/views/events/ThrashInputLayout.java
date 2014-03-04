package fi.pss.cleanbeach.ui.views.events;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

import fi.pss.cleanbeach.data.ThrashType;
import fi.pss.cleanbeach.ui.MyTouchKitUI;
import fi.pss.cleanbeach.ui.components.Stepper;

public class ThrashInputLayout extends NavigationView {

	private static final long serialVersionUID = -8058030751028066774L;

	private final GridLayout gl;

	public ThrashInputLayout(fi.pss.cleanbeach.data.Event e,
			EventsPresenter presenter) {

		addStyleName("thrashinput");

		gl = new GridLayout(2, 1);
		gl.setSpacing(true);
		gl.setMargin(true);
		gl.setColumnExpandRatio(0, 1);
		gl.setWidth("100%");
		setContent(gl);

		Label caption = new Label("Mark your collected thrash here:");
		caption.addStyleName("caption");
		caption.setHeight("30px");
		gl.addComponent(caption, 0, 0, 1, 0);

		for (ThrashType t : presenter.getThrashTypes()) {
			addRow(t, e, presenter);
		}
	}

	private void addRow(final ThrashType t,
			final fi.pss.cleanbeach.data.Event e,
			final EventsPresenter presenter) {
		Label name = new Label(t.getName() + ":");
		name.addStyleName("typename");
		gl.addComponent(name);
		gl.setComponentAlignment(name, Alignment.MIDDLE_LEFT);

		final Stepper s = new Stepper(e.getThrash().getOfTypeForUser(t,
				MyTouchKitUI.getCurrentUser()));
		gl.addComponent(s);
		s.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -993524115746739524L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				presenter.setNumThrash(s.getValue(), t, e,
						MyTouchKitUI.getCurrentUser());
			}
		});
	}
}
