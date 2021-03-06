package fi.pss.cleanbeach.ui.views.group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

import fi.pss.cleanbeach.data.Image;
import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.components.ConfirmPopover;
import fi.pss.cleanbeach.ui.components.ConfirmPopover.ConfirmListener;
import fi.pss.cleanbeach.ui.util.ImageUtil;
import fi.pss.cleanbeach.ui.util.Lang;
import fi.pss.cleanbeach.ui.views.events.EventPanel;

/**
 * @author denis
 * 
 */
public class GroupDetailsLayout extends NavigationView {

	private static final long serialVersionUID = -4152802888155514945L;

	private final GroupPresenter presenter;

	private UsersGroup group;

	Map<fi.pss.cleanbeach.data.Event, EventPanel> eventToPanel = new HashMap<>();
	private Component adminComponent;

	private CssLayout eventsLayout;

	GroupDetailsLayout(final GroupPresenter presenter, final UsersGroup group) {
		this.presenter = presenter;
		this.group = group;

		setCaption(Lang.get("Group.details.caption"));

		build();
	}

	public void build() {
		setRightButton(presenter, group);

		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		mainLayout.addStyleName("groupview-details");
		mainLayout.addComponent(createGroupInfoComponent());
		mainLayout.addComponent(createInvitationsInfo());
		if (presenter.canManage(group)) {
			adminComponent = createButtonsComponent();
			mainLayout.addComponent(adminComponent);
		}
		mainLayout.addComponent(createEventsComponent());

		setContent(mainLayout);
	}

	@Override
	public Layout getContent() {
		return (Layout) super.getContent();
	}

	public void showLeaveConfirmation() {

		ConfirmPopover pop = new ConfirmPopover(new ConfirmListener() {

			@Override
			public void confirmed() {
				presenter.leaveGroup(group);
			}
		}, Lang.get("Group.details.leave.message", group.getName()));

		pop.showRelativeTo(GroupDetailsLayout.this);
	}

	public void showJoinConfirmation() {

		ConfirmPopover pop = new ConfirmPopover(new ConfirmListener() {

			@Override
			public void confirmed() {
				presenter.joinGroup(group);
			}
		}, Lang.get("Group.details.join.message", group.getName()));

		pop.showRelativeTo(GroupDetailsLayout.this);
	}

	public void updateMembershipState(UsersGroup group) {
		this.group = group;
		if (!presenter.canManage(group) && adminComponent != null) {
			getContent().removeComponent(adminComponent);
		}
		setRightButton(presenter, group);
	}

	private Component createInvitationsInfo() {
		CssLayout layout = new CssLayout();
		layout.addStyleName("groupview-details-invitations");

		Label prefix = new Label(Lang.get("Group.details.invitations.prefix"));
		prefix.setSizeUndefined();
		prefix.addStyleName("invitations-prefix");
		layout.addComponent(prefix);

		String link = presenter.getPendingEventInvitations(group);
		Button invitations = new Button(link);
		invitations.setStyleName(BaseTheme.BUTTON_LINK);
		invitations.addStyleName("invitations-link");
		layout.addComponent(invitations);

		invitations.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 6094928833388740281L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.showInvitations(group);
			}
		});

		return layout;
	}

	private Component createEventsComponent() {
		eventsLayout = new CssLayout();
		eventsLayout.addStyleName("groupview-details-events");

		Label header = new Label(Lang.get("Group.details.events.caption"));
		header.addStyleName("groupview-details-events-caption");
		eventsLayout.addComponent(header);

		eventToPanel.clear();
		List<fi.pss.cleanbeach.data.Event> events = presenter.getEvents(group);
		for (fi.pss.cleanbeach.data.Event event : events) {
			EventPanel eventPanel = new EventPanel(event, presenter);
			eventsLayout.addComponent(eventPanel);
			eventToPanel.put(event, eventPanel);
		}

		return eventsLayout;
	}

	private Component createButtonsComponent() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth(100, Unit.PERCENTAGE);
		layout.setSpacing(true);
		layout.addStyleName("groupview-details-admin-buttons");
		layout.addStyleName("actionbuttons");

		Button createButton = new Button(Lang.get("Group.details.create.event"));
		createButton.setIcon(FontAwesome.PLUS);
		createButton.addStyleName("group-details-create-event");
		createButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 6094928833388740281L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.showCreateEvent(group);
			}
		});
		layout.addComponent(createButton);

		Button manage = new Button(Lang.get("Group.details.manage.admin"));
		manage.setIcon(FontAwesome.USER);
		manage.addStyleName("group-details-manage-admins");
		manage.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 2574122824910031111L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.showManageAdmins(group);
			}
		});
		layout.addComponent(manage);

		Button edit = new Button(Lang.get("Group.details.manage.edit"));
		edit.setIcon(FontAwesome.PENCIL);
		edit.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 2574122824910031111L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.showEdit(group);
			}
		});
		layout.addComponent(edit);
		return layout;
	}

	private void setRightButton(final GroupPresenter presenter,
			final UsersGroup group) {
		if (presenter.canJoin(group)) {
			getNavigationBar().setRightComponent(
					createJoinButton(presenter, group));
		} else if (presenter.canLeave(group)) {
			getNavigationBar().setRightComponent(createLeaveButton(group));
		} else if (presenter.canDelete(group)) {
			getNavigationBar().setRightComponent(createDeleteButton(group));
		} else {
			getNavigationBar().setRightComponent(null);
		}
	}

	private Component createDeleteButton(final UsersGroup group) {
		final Button button = new Button(Lang.get("Group.details.delete.group"));
		button.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 4414454544724918833L;

			@Override
			public void buttonClick(ClickEvent event) {

				ConfirmPopover pop = new ConfirmPopover(new ConfirmListener() {

					@Override
					public void confirmed() {
						presenter.delete(group);
					}
				}, Lang.get("Group.details.delete.confirmation"));
				pop.showRelativeTo(button);
			}
		});
		return button;
	}

	private Component createGroupInfoComponent() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth(100, Unit.PERCENTAGE);
		layout.addStyleName("group-details-info");
		CssLayout details = new CssLayout();
		details.addStyleName("group-details-text-info");
		layout.addComponent(details);
		layout.setExpandRatio(details, 1);

		Label name = new Label(group.getName());
		name.addStyleName("group-details-name");
		details.addComponent(name);

		Label description = new Label(group.getDescription());
		description.addStyleName("group-details-description");
		details.addComponent(description);

		CssLayout rightLayout = new CssLayout();
		rightLayout.addStyleName("group-details-right-panel");
		layout.addComponent(rightLayout);
		Image logo = group.getLogo();
		if (logo != null && logo.getContent() != null
				&& logo.getContent().length > 0) {
			com.vaadin.ui.Image groupLogo = ImageUtil.getGroupLogo(group);
			groupLogo.addStyleName("logobig");
			rightLayout.addComponent(groupLogo);
		}

		String members = presenter.getMembers(group);
		Button membersButton = new Button(members);
		membersButton.setStyleName(BaseTheme.BUTTON_LINK);
		membersButton.addStyleName("group-details-members");
		membersButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -2525777814596352093L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.showMembers(group);
			}
		});
		rightLayout.addComponent(membersButton);

		return layout;
	}

	private Button createLeaveButton(final UsersGroup group) {
		Button button = new Button(Lang.get("Group.details.leave.group"));
		button.addStyleName("groupview-details-leave");
		button.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 4414454544724918833L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.requestLeaveGroup(group);
			}
		});
		return button;
	}

	private Component createJoinButton(final GroupPresenter presenter,
			final UsersGroup group) {
		Button button = new Button(Lang.get("Group.details.join.group"));
		button.addStyleName("groupview-details-join");
		button.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 2636577754413406778L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.requestJoinGroup(group);
			}
		});
		return button;
	}

	public void remove(long eventId) {
		for (fi.pss.cleanbeach.data.Event e : eventToPanel.keySet()) {
			if (e.getId() == eventId) {
				eventsLayout.removeComponent(eventToPanel.get(e));
				break;
			}
		}
	}

	public void update(fi.pss.cleanbeach.data.Event e) {
		if (eventToPanel.containsKey(e)) {
			eventToPanel.get(e).update(e);
		}
	}

}
