package com.example.application.views.simulation;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.main.MainView;

@Route(value = "simulations", layout = MainView.class)
@PageTitle("Simulation")
public class SimulationView extends Div {

    public SimulationView() {
        addClassName("simulation-view");
        add(new Text("Content placeholder"));
    }

}
