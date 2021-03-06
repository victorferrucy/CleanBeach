package fi.pss.cleanbeach.ui.views.events;

import java.util.List;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.ui.util.Lang;
import fi.pss.cleanbeach.ui.views.eventdetails.CreateEventLayout;

public class MainEventsLayout extends NavigationView implements ClickListener {

	private static final long serialVersionUID = -5316615188726137687L;

	private final Button allEvents;
	private final Button joinedEvents;
	private final Button search;
	private final Button create;
	private final CssLayout content;

	private WallLayout allEventsLayout;
	private WallLayout joinedEventsLayout;
	private EventSearchLayout searchLayout;
	private CreateEventLayout createLayout;

	private final EventsPresenter presenter;

	public MainEventsLayout(EventsPresenter presenter) {

		this.presenter = presenter;

		setCaption(Lang.get("events.caption"));

		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);
		vl.setMargin(true);

		HorizontalLayout tabs = new HorizontalLayout();
		tabs.addStyleName("eventtabs");
		tabs.addStyleName("actionbuttons");
		tabs.setSpacing(true);
		tabs.setWidth("100%");

		allEvents = new Button(Lang.get("events.main.allevents"));
		allEvents.addClickListener(this);
		allEvents.setData(allEventsLayout = new WallLayout(presenter));
		allEvents.addStyleName("selected");
		allEvents.setIcon(FontAwesome.USERS);
		tabs.addComponent(allEvents);

		joinedEvents = new Button(Lang.get("events.main.joinedevents"));
		joinedEvents.addClickListener(this);
		joinedEvents.setData(joinedEventsLayout = new WallLayout(presenter));
		joinedEvents.setIcon(FontAwesome.STAR);
		tabs.addComponent(joinedEvents);

		search = new Button(Lang.get("events.main.search"));
		search.addClickListener(this);
		search.setData(searchLayout = new EventSearchLayout(presenter));
		search.setIcon(FontAwesome.SEARCH);
		tabs.addComponent(search);

		create = new Button(Lang.get("events.main.create"));
		create.addClickListener(this);
		create.setIcon(FontAwesome.PLUS);
		tabs.addComponent(create);

		content = new CssLayout((Component) allEvents.getData());
		content.setSizeFull();

		vl.addComponent(tabs);
		vl.addComponent(content);
		vl.setExpandRatio(content, 1);

		setContent(vl);

		allEventsLayout.runPositioning();
	}

	@Override
	public void buttonClick(ClickEvent event) {

		if (event.getButton() == create) {
			getNavigationManager().navigateTo(
					new CreateEventLayout(null, null, presenter));
			return;
		}

		allEvents.removeStyleName("selected");
		joinedEvents.removeStyleName("selected");
		search.removeStyleName("selected");

		content.removeAllComponents();
		content.addComponent((Component) event.getButton().getData());
		event.getButton().addStyleName("selected");

		if (event.getButton() == allEvents) {
			allEventsLayout.runPositioning();
			// presenter.loadAllEvents();
		}
		if (event.getButton() == joinedEvents) {
			presenter.loadJoinedEvents();
		}

	}

	@Override
	protected void onBecomingVisible() {
		super.onBecomingVisible();

		// clear navigation data
		getNavigationManager().setPreviousComponent(null);
	}

	public void showJoinedEvents(List<fi.pss.cleanbeach.data.Event> l) {
		joinedEventsLayout.update(l);
	}

	public void showAllEvents(List<fi.pss.cleanbeach.data.Event> l) {
		allEventsLayout.update(l);
	}

	public void populateSearchResults(List<fi.pss.cleanbeach.data.Event> l) {
		searchLayout.populate(l);
	}

	public void update(fi.pss.cleanbeach.data.Event e) {
		joinedEventsLayout.update(e);
		allEventsLayout.update(e);
		searchLayout.update(e);
	}

	public void remove(long eventId) {
		joinedEventsLayout.remove(eventId);
		allEventsLayout.remove(eventId);
		searchLayout.remove(eventId);
	}
}
