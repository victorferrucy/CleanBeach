package fi.pss.cleanbeach.ui.views.eventdetails;

import java.util.Collection;

import com.vaadin.ui.Component;

import fi.pss.cleanbeach.data.Invite;
import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.User;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.mvp.AbstractView;
import fi.pss.cleanbeach.ui.views.events.MainEventsLayout;
import fi.pss.cleanbeach.ui.views.group.GroupDetailsLayout;
import fi.pss.cleanbeach.ui.views.locations.HistoryLayout;

public abstract class EventDetailsCapableView<T extends EventDetailsPresenter<?>>
		extends AbstractView<T> implements IEventDetails {

	private static final long serialVersionUID = 5027141664466020875L;

	protected EventDetailLayout details;

	public EventDetailsCapableView() {
		super();
	}

	@Override
	public void showDetails(fi.pss.cleanbeach.data.Event e) {

		while (!isOkToNavigateToEventDetails()) {
			navigateBack();
		}
		navigateTo(details = new EventDetailLayout(e, presenter));
	}

	private boolean isOkToNavigateToEventDetails() {
		Component current = getCurrentComponent();
		boolean isGroupDetails = current instanceof GroupDetailsLayout;
		boolean isEventsList = current instanceof MainEventsLayout;
		boolean isMap = current instanceof HistoryLayout;
		return current == null || isGroupDetails || isEventsList || isMap;
	}

	@Override
	public void openThrashDetails(fi.pss.cleanbeach.data.Event e) {
		navigateTo(new ThrashInputEventLayout(e, presenter));
	}

	@Override
	public void openAddComment(fi.pss.cleanbeach.data.Event e) {
		navigateTo(new CommentInputLayout(e, presenter));
	}

	@Override
	public void updateEventDetails(fi.pss.cleanbeach.data.Event e) {
		details.update(e);
		eventDetailsUpdated(e);
	}

	protected abstract void eventDetailsUpdated(fi.pss.cleanbeach.data.Event e);

	@Override
	public void openInviteGroups(Collection<Invite> coll,
			fi.pss.cleanbeach.data.Event e, User u) {
		navigateTo(new InviteGroupsLayout(coll, e, u, presenter));
	}

	@Override
	public void navigateAndUpdate(fi.pss.cleanbeach.data.Event e) {
		while (getCurrentComponent() != null
				&& getCurrentComponent() != details) {
			navigateBack();
		}
		updateEventDetails(e);
	}

	@Override
	public void openEditEvent(fi.pss.cleanbeach.data.Event e) {
		navigateTo(new CreateEventLayout(e, presenter));
	}

	@Override
	public void showCreateEvent(UsersGroup selectedGroup,
			Location selectedLocation) {
		navigateTo(new CreateEventLayout(selectedGroup, selectedLocation,
				presenter));
	}

	@Override
	public void navigateBackAfterEdit(fi.pss.cleanbeach.data.Event edited) {
		navigateBack();
		updateEventDetails(edited);
	}

	@Override
	public void showThrashTotal(fi.pss.cleanbeach.data.Event selectedEvent) {
		navigateTo(new ThrashDetailLayout(selectedEvent, presenter));
	}
}