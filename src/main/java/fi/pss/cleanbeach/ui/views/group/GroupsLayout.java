/**
 *
 */
package fi.pss.cleanbeach.ui.views.group;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.UsersGroup;
import fi.pss.cleanbeach.ui.MainAppUI;
import fi.pss.cleanbeach.ui.util.Lang;

/**
 * @author denis
 * 
 */
class GroupsLayout extends NavigationView {

	private static final long serialVersionUID = -7525394606644662421L;

	private final GroupPresenter presenter;

	private final Map<UsersGroup, GroupComponent> groupToComponent = new HashMap<>();

	private VerticalLayout mainLayout;

	GroupsLayout(GroupPresenter presenter) {
		this.presenter = presenter;

		setCaption(Lang.get("Groups.view.caption"));
		build();
	}

	public void build() {
		mainLayout = new VerticalLayout();
		mainLayout.setWidth(100, Unit.PERCENTAGE);
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.addStyleName("groups");

		Label title = new Label(Lang.get("Groups.view.title"));
		mainLayout.addComponent(title);
		title.addStyleName("groups-title");

		HorizontalLayout groupActionsLayout = new HorizontalLayout();
		groupActionsLayout.setWidth(100, Unit.PERCENTAGE);
		groupActionsLayout.addStyleName("groups-buttons");
		groupActionsLayout.addStyleName("actionbuttons");
		groupActionsLayout.setSpacing(true);
		mainLayout.addComponent(groupActionsLayout);

		groupActionsLayout.addComponent(createSearchButton());
		groupActionsLayout.addComponent(createGroupButton());

		setContent(mainLayout);
	}

	@Override
	public Layout getContent() {
		return (Layout) super.getContent();
	}

	public void showAdminGroups(Set<UsersGroup> groups) {
		if (!groups.isEmpty()) {
			Label adminTitle = new Label(Lang.get("Groups.view.admin.label"));
			adminTitle.addStyleName("admin-title");
			getContent().addComponent(adminTitle);
			for (UsersGroup group : groups) {
				getContent().addComponent(createGroupComponent(group, true));
			}
		}
	}

	public void showMemberGroups(Set<UsersGroup> groups) {
		if (!groups.isEmpty()) {
			Label memberTitle = new Label(Lang.get("Groups.view.member.label"));
			memberTitle.addStyleName("member-title");
			getContent().addComponent(memberTitle);
			for (UsersGroup group : groups) {
				if (!group.getAdmins().contains(MainAppUI.getCurrentUser())) {
					getContent().addComponent(
							createGroupComponent(group, false));
				}
			}
		}
	}

	private GroupComponent createGroupComponent(final UsersGroup group,
			boolean isAdmin) {
		GroupComponent component = new GroupComponent(group, presenter, isAdmin);
		groupToComponent.put(group, component);
		return component;
	}

	private Button createSearchButton() {
		Button button = new Button(Lang.get("Groups.view.search.button"));
		button.addStyleName("search-button");
		button.setIcon(FontAwesome.SEARCH);
		button.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 841184184854521982L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.searchGroups();
			}
		});
		return button;
	}

	private Button createGroupButton() {
		Button button = new Button(Lang.get("Groups.view.create.button"));
		button.addStyleName("create-button");
		button.setIcon(FontAwesome.PLUS);
		button.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -6331201462849263479L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.createGroup();
			}
		});
		return button;
	}

	public void update(UsersGroup group) {
		groupToComponent.get(group).build(group, presenter,
				group.getAdmins().contains(MainAppUI.getCurrentUser()));
	}

}
