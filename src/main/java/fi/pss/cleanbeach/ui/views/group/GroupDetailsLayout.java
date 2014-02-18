package fi.pss.cleanbeach.ui.views.group;

import java.util.List;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.BaseTheme;

import fi.pss.cleanbeach.data.Image;
import fi.pss.cleanbeach.data.UsersGroup;

/**
 * @author denis
 * 
 */
class GroupDetailsLayout extends NavigationView {

    private final GroupPresenter presenter;

    private final UsersGroup group;

    private final boolean isAdmin;

    GroupDetailsLayout(final GroupPresenter presenter, final UsersGroup group,
            boolean adminView) {
        this.presenter = presenter;
        this.group = group;
        isAdmin = adminView;

        setCaption(getMessage("Group.details.caption"));

        Button button = new Button(getMessage("Group.details.leave.group"));
        button.setStyleName(BaseTheme.BUTTON_LINK);
        button.addStyleName("groupview-details-leave");
        button.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                presenter.leaveGroup(group);
            }
        });
        getNavigationBar().setRightComponent(button);

        CssLayout mainLayout = new CssLayout();
        mainLayout.addStyleName("groupview-details");
        mainLayout.addComponent(createGroupInfoComponent());
        mainLayout.addComponent(createInvitationsInfo());
        if (isAdmin) {
            mainLayout.addComponent(createButtonsComponent());
        }
        mainLayout.addComponent(createEventsComponent());

        setContent(mainLayout);
    }

    private Component createInvitationsInfo() {
        CssLayout layout = new CssLayout();
        layout.addStyleName("groupview-details-invitations");

        Label prefix = new Label(getMessage("Group.details.invitations.prefix"));
        prefix.setSizeUndefined();
        prefix.addStyleName("invitations-prefix");
        layout.addComponent(prefix);

        String link = presenter.getPendingEventInvitations(group);
        Button invitations = new Button(link);
        invitations.setStyleName(BaseTheme.BUTTON_LINK);
        invitations.addStyleName("invitations-link");
        layout.addComponent(invitations);

        return layout;
    }

    private Component createEventsComponent() {
        CssLayout layout = new CssLayout();
        layout.addStyleName("groupview-details-events");

        Label header = new Label(getMessage("Group.details.events.caption"));
        header.addStyleName("groupview-details-events-caption");
        layout.addComponent(header);

        List<fi.pss.cleanbeach.data.Event> events = group.getEvents();
        for (fi.pss.cleanbeach.data.Event event : events) {
            layout.addComponent(createEventComponent(event));
        }

        return layout;
    }

    private Component createEventComponent(fi.pss.cleanbeach.data.Event event) {
        CssLayout layout = new CssLayout();
        layout.addStyleName("groupview-details-event");
        // TODO
        return layout;
    }

    private Component createButtonsComponent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidth(100, Unit.PERCENTAGE);
        layout.addStyleName("groupview-details-admin-buttons");

        Button createButton = new Button(
                getMessage("Group.details.create.event"));
        createButton.addStyleName("group-details-create-event");
        createButton.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                presenter.createEvent();
            }
        });
        layout.addComponent(createButton);

        Button manage = new Button(getMessage("Group.details.manage.admin"));
        manage.addStyleName("group-details-manage-admins");
        manage.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                presenter.showManageAdmins();
            }
        });
        layout.addComponent(manage);
        return layout;
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
            rightLayout.addComponent(GroupsLayout.createLogoComponent(logo
                    .getContent()));
        }

        String members = presenter.getMembers(group);
        Button membersButton = new Button(members);
        membersButton.setStyleName(BaseTheme.BUTTON_LINK);
        membersButton.addStyleName("group-details-members");
        membersButton.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                presenter.showMembers(group);
            }
        });
        rightLayout.addComponent(membersButton);

        return layout;
    }

    @Override
    public CssLayout getContent() {
        return (CssLayout) super.getContent();
    }

    private String getMessage(String key) {
        return presenter.getMessage(key);
    }
}
