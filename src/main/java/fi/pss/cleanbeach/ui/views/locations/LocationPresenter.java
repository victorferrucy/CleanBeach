package fi.pss.cleanbeach.ui.views.locations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.vaadin.cdi.UIScoped;

import fi.pss.cleanbeach.data.Event;
import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.ThrashType;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.services.EventService;
import fi.pss.cleanbeach.services.LocationService;
import fi.pss.cleanbeach.ui.views.eventdetails.EventDetailsPresenter;

@UIScoped
public class LocationPresenter extends EventDetailsPresenter<ILocation> {

	@Inject
	private LocationService locService;

	@Inject
	private EventService eService;

	@Override
	public void init(User currentUser) {

	}

	public void addLocation(double latitude, double longitude, String name) {
		Location l = locService.createLocation(latitude, longitude, name);

		if (l != null) {
			view.updateMarker(l);
			view.selectMarker(l);
		} else {
			view.showErrorNotification("* Could not create location; it might be too close to an existing one.");
		}
	}

	public void showEvents(Location selected) {
		view.showEvents(selected, getEvents(selected));
	}

	public void createEvent(Location selected) {
		view.showCreateEvent(null, selected);
	}

	public void showTrends(Location selected) {
		view.showTrends(selected, new ArrayList<Event>());
	}

	public void readyForPoints(Double lat, Double long1) {
		Set<Location> locs = locService.getLocationsNear(lat, long1);
		view.addLocations(locs);
	}

	public void markBeachDirty(Location selected, String desc) {
		selected = locService.setDirty(selected, desc);

		view.updateMarker(selected);
	}

	public void showTrash(Location selected) {
		view.showTrashInput(selected,
				locService.getThrash(selected, view.getUser()));
	}

	@Override
	public List<ThrashType> getThrashTypes() {
		return eService.getThrashTypes();
	}

	public void setNumThrash(Integer value, ThrashType t, Location l) {
		locService.setNumThrash(value, t, l, view.getUser());
	}

	public void addOtherDesc(ThrashType t, String value, Location loc) {
		locService.setDescription(t, view.getUser(), value, null, loc);
	}

	@Override
	public void createEvent(UsersGroup creator, String desc, Date start,
			Location loc) {

		Event e = eService.createEvent(start, loc, creator, desc);
		view.showDetails(e);
		view.updateEventList(loc, e);
	}

	private Collection<Event> getEvents(Location selected) {
		return eService.getEvents(selected);
	}
}
