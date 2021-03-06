package fi.pss.cleanbeach.ui.views.locations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.ui.util.Lang;
import fi.pss.cleanbeach.ui.views.events.EventPanel;

/**
 * TODO
 * 
 * @author thomas
 * 
 */
public class HistoryLayout extends NavigationView {

	private static final long serialVersionUID = -98682367317399067L;
	private final LocationPresenter presenter;

	private final Map<Long, EventPanel> eventToPanel = new HashMap<>();
	private final VerticalLayout root;

	public HistoryLayout(final Location selected,
			Collection<fi.pss.cleanbeach.data.Event> events,
			final LocationPresenter presenter) {

		this.presenter = presenter;
		root = new VerticalLayout();
		root.setSpacing(true);
		root.setMargin(true);
		setContent(root);

		setCaption(Lang.get("locations.events.caption"));

		Button createEvent = new Button(Lang.get("locations.map.createevent"));
		createEvent.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 682703780760294261L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.createEvent(selected);
			}
		});

		root.addComponent(createEvent);

		Label allEvents = new Label(Lang.get("locations.events.allevents"));
		root.addComponent(allEvents);

		for (fi.pss.cleanbeach.data.Event e : events) {
			EventPanel panel = new EventPanel(e, presenter);
			root.addComponent(panel);

			eventToPanel.put(e.getId(), panel);
		}
	}

	public void add(fi.pss.cleanbeach.data.Event e) {
		EventPanel p = new EventPanel(e, presenter);
		// add on top of rest
		root.addComponent(p, 2);

		eventToPanel.put(e.getId(), p);
	}

	public void remove(long eventId) {
		root.removeComponent(eventToPanel.get(eventId));
	}

	public void update(fi.pss.cleanbeach.data.Event e) {
		if (eventToPanel.containsKey(e.getId())) {
			eventToPanel.get(e.getId()).update(e);
		}
	}
}
