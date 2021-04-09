package com.example.application.views.settings;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.main.MainView;

@Route(value = "settings", layout = MainView.class)
@PageTitle("Settings")
public class SettingsView extends Div {

    public SettingsView() {
        addClassName("settings-view");
        add(new Text("Content placeholder"));
    }

}
