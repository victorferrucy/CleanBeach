package fi.pss.cleanbeach.ui.views.locations;

import java.util.Set;

import javax.inject.Inject;

import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.cdi.UIScoped;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.ui.mvp.AbstractView;
import fi.pss.cleanbeach.ui.views.locations.CreateLocationPopover.ConfirmListener;

@UIScoped
public class LocationView extends AbstractView<LocationPresenter> implements
		ILocation, MapPointSelectedListener {

	private static final long serialVersionUID = 6914178286159188531L;

	private LitterBaseMap lMap;

	private Layout actionButtons;
	private HorizontalLayout addButtons;

	private Location selected;

	@Override
	@Inject
	public void injectPresenter(LocationPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void attach() {
		super.attach();

		lMap.runPositioning();
		// add points
		presenter.readyForPoints(lMap.getLat(), lMap.getLong());

	}

	@Override
	protected ComponentContainer getMainContent() {

		setCaption("Törkykartal");

		lMap = new LitterBaseMap(presenter, this);
		lMap.setSizeFull();

		Button addLocation = new Button("add");
		addLocation.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 5940636967467650018L;

			@Override
			public void buttonClick(ClickEvent event) {

				CreateLocationPopover pop = new CreateLocationPopover(
						new ConfirmListener() {

							@Override
							public void confirm(String name) {
								Point p = lMap.getMarker().getPoint();
								presenter.addLocation(p.getLat(), p.getLon(),
										name);
							}
						});
				pop.showRelativeTo(actionButtons);

			}
		});

		Button createEvent = new Button("event");
		createEvent.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 682703780760294261L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.createEvent();
			}
		});

		Button markDirty = new Button("markAsDirty");
		markDirty.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 682703780760294261L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.createEvent();
			}
		});

		Button showTrends = new Button("trends");
		showTrends.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -9055053032611311553L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.showTrends();
			}
		});

		Button showEvents = new Button("events");
		showEvents.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -7853451135198225867L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.showEvents();
			}
		});

		actionButtons = new HorizontalLayout(createEvent, markDirty,
				showTrends, showEvents);
		actionButtons.setSizeUndefined();
		actionButtons.setVisible(false);

		addButtons = new HorizontalLayout(addLocation);
		addButtons.setSizeUndefined();
		addButtons.setVisible(false);

		VerticalLayout vl = new VerticalLayout(lMap, actionButtons, addButtons);
		vl.setSizeFull();
		vl.setExpandRatio(lMap, 1);
		vl.setComponentAlignment(actionButtons, Alignment.MIDDLE_CENTER);
		vl.setComponentAlignment(addButtons, Alignment.MIDDLE_CENTER);

		return vl;
	}

	@Override
	public void selected(Point point, Location loc) {

		actionButtons.setVisible(false);
		addButtons.setVisible(false);

		selected = loc;

		if (loc == null) {
			addButtons.setVisible(true);
		} else {
			actionButtons.setVisible(true);
		}
	}

	@Override
	public void addLocations(Set<Location> locs) {

		for (Location l : locs) {
			lMap.addPoint(l);
		}
	}
}
