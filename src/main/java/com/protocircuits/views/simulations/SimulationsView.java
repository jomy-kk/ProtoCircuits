package com.protocircuits.views.simulations;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.protocircuits.simulations.Environment;
import com.protocircuits.views.running.RunningView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.protocircuits.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;

@Route(value = "simulations", layout = MainView.class)
@PageTitle("Simulations")
@CssImport("./views/simulations/simulations-view.css")
@Tag("simulations-view")
@JsModule("./views/simulations/simulations-view.ts")
public class SimulationsView extends LitTemplate {

    @Id("mygrid")
    private GridPro<Simulation> grid;

    private ListDataProvider<Simulation> dataProvider;

    private Grid.Column<Simulation> idColumn;
    private Grid.Column<Simulation> nameColumn;
    private Grid.Column<Simulation> population1Column;
    private Grid.Column<Simulation> population1Amount;
    private Grid.Column<Simulation> population2Column;
    private Grid.Column<Simulation> population2Amount;
    private Grid.Column<Simulation> population3Column;
    private Grid.Column<Simulation> population3Amount;
    private Grid.Column<Simulation> ssdnaColumn;
    private Grid.Column<Simulation> ssdnaAmountColumn;
    private Grid.Column<Simulation> runColumn;

    private String[] protocellsPopulationAvailable = new String[] {"None", "Protocell A", "Protocell T", "Protocell X", "Protocell R"};
    private String[] ssDNAPopulationAvailable = new String[] {"None", "SSDNA type 0", "SSDNA type 1", "SSDNA type 2"};

    public SimulationsView() {
        createGrid();
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }

    private void createGridComponent() {
        grid.setSelectionMode(SelectionMode.SINGLE);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        dataProvider = new ListDataProvider<Simulation>(getClients());
        grid.setDataProvider(dataProvider);
    }

    private void addColumnsToGrid() {
        createIdColumn();
        //createNameColumn();
        createPopulation1Column();
        createAmount1Column();
        createPopulation2Column();
        createAmount2Column();
        createPopulation3Column();
        createAmount3Column();
        createSSDNAColumn();
        createSSDNAAmountColumn();
        createRunColumn();
    }

    private void createIdColumn() {
        idColumn = grid.addColumn(Simulation::getId, "id").setHeader("ID").setWidth("60px").setFlexGrow(0);
    }

    private void createNameColumn() {
        nameColumn = grid.addColumn(Simulation::getName, "name").setHeader("Simulation Name");
    }

    private void createPopulation1Column() {

        population1Column = grid.addEditColumn(Simulation::getName, new ComponentRenderer<>(simulation -> {
            Span span = new Span();
            span.setText(simulation.getPopulation1());
            return span;
        })).select((item, newValue) -> item.setPopulation1(newValue), Arrays.asList("Protocell A", "Protocell T", "Protocell X", "Protocell R"))
                .setComparator(simulation -> simulation.getPopulation1()).setHeader("Population 1");

    }

    private void createAmount1Column() {
        population1Amount = grid
                .addEditColumn(Simulation::getPopulation1Amount,
                        new NumberRenderer<>(simulation -> simulation.getPopulation1Amount(), NumberFormat.getIntegerInstance()))
                .text((item, newValue) -> item.setPopulation1Amount(Integer.parseInt(newValue)))
                .setComparator(simulation -> simulation.getPopulation1Amount()).setHeader("# Agents");
    }

    private void createPopulation2Column() {

        population2Column = grid.addEditColumn(Simulation::getName, new ComponentRenderer<>(simulation -> {
            Span span = new Span();
            span.setText(simulation.getPopulation2());
            return span;
        })).select((item, newValue) -> item.setPopulation2(newValue), Arrays.asList("Protocell A", "Protocell T", "Protocell X", "Protocell R"))
                .setComparator(simulation -> simulation.getPopulation2()).setHeader("Population 2");

    }

    private void createAmount2Column() {
        population2Amount = grid
                .addEditColumn(Simulation::getPopulation2Amount,
                        new NumberRenderer<>(simulation -> simulation.getPopulation2Amount(), NumberFormat.getIntegerInstance()))
                .text((item, newValue) -> item.setPopulation2Amount(Integer.parseInt(newValue)))
                .setComparator(simulation -> simulation.getPopulation2Amount()).setHeader("# Agents");
    }

    private void createPopulation3Column() {

        population3Column = grid.addEditColumn(Simulation::getName, new ComponentRenderer<>(simulation -> {
            Span span = new Span();
            span.setText(simulation.getPopulation3());
            return span;
        })).select((item, newValue) -> item.setPopulation3(newValue), Arrays.asList(protocellsPopulationAvailable))
                .setComparator(simulation -> simulation.getPopulation3()).setHeader("Population 3");

    }

    private void createAmount3Column() {
        population3Amount = grid
                .addEditColumn(Simulation::getPopulation3Amount,
                        new NumberRenderer<>(simulation -> simulation.getPopulation3Amount(), NumberFormat.getIntegerInstance()))
                .text((item, newValue) -> item.setPopulation3Amount(Integer.parseInt(newValue)))
                .setComparator(simulation -> simulation.getPopulation3Amount()).setHeader("# Agents");
    }

    private void createSSDNAColumn() {
        ssdnaColumn = grid.addEditColumn(Simulation::getName, new ComponentRenderer<>(simulation -> {
            Span span = new Span();
            span.setText(simulation.getInitialSSDNA());
            return span;
        })).select((item, newValue) -> item.setInitialSSDNA(newValue), Arrays.asList(ssDNAPopulationAvailable))
                .setComparator(simulation -> simulation.getInitialSSDNA()).setHeader("Initial SSDNA");

    }

    private void createSSDNAAmountColumn() {
        ssdnaAmountColumn = grid
                .addEditColumn(Simulation::getInitialSSDNAAmount,
                        new NumberRenderer<>(simulation -> simulation.getInitialSSDNAAmount(), NumberFormat.getIntegerInstance()))
                .text((item, newValue) -> item.setInitialSSDNAAmount(Integer.parseInt(newValue)))
                .setComparator(simulation -> simulation.getInitialSSDNAAmount()).setHeader("# Agents");
    }


    private void createRunColumn() {
        runColumn = grid.addColumn(new ComponentRenderer<>(simulation -> {
            Button bt = new Button("Run");
            bt.setEnabled(true);
            bt.setThemeName("primary");
            bt.addClickListener(this::startSimulation);
            return bt;
        })).setHeader("").setWidth("50px");
    }



    private void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();

        TextField idFilter = new TextField();
        idFilter.setPlaceholder("Filter");
        idFilter.setClearButtonVisible(true);
        idFilter.setWidth("100%");
        idFilter.setValueChangeMode(ValueChangeMode.EAGER);
        idFilter.addValueChangeListener(event -> dataProvider.addFilter(
                simulation -> StringUtils.containsIgnoreCase(Integer.toString(simulation.getId()), idFilter.getValue())));
        filterRow.getCell(idColumn).setComponent(idFilter);

        /*TextField nameFilter = new TextField();
        nameFilter.setPlaceholder("Filter");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setWidth("100%");
        nameFilter.setValueChangeMode(ValueChangeMode.EAGER);
        nameFilter.addValueChangeListener(event -> dataProvider
                .addFilter(simulation -> StringUtils.containsIgnoreCase(simulation.getClient(), nameFilter.getValue())));
        filterRow.getCell(nameColumn).setComponent(nameFilter);*/

        ComboBox<String> populationFilter1 = new ComboBox<>();
        populationFilter1.setItems(Arrays.asList(protocellsPopulationAvailable));
        populationFilter1.setPlaceholder("Filter");
        populationFilter1.setClearButtonVisible(true);
        populationFilter1.setWidth("100%");
        populationFilter1.addValueChangeListener(
                event -> dataProvider.addFilter(simulation -> arePopulations1Equal(simulation, populationFilter1)));
        filterRow.getCell(population1Column).setComponent(populationFilter1);

        TextField amountFilter1 = new TextField();
        amountFilter1.setPlaceholder("Filter");
        amountFilter1.setClearButtonVisible(true);
        amountFilter1.setWidth("100%");
        amountFilter1.setValueChangeMode(ValueChangeMode.EAGER);
        amountFilter1.addValueChangeListener(event -> dataProvider.addFilter(simulation -> StringUtils
                .containsIgnoreCase(Double.toString(simulation.getPopulation1Amount()), amountFilter1.getValue())));
        filterRow.getCell(population1Amount).setComponent(amountFilter1);

        ComboBox<String> populationFilter2 = new ComboBox<>();
        populationFilter2.setItems(Arrays.asList(protocellsPopulationAvailable));
        populationFilter2.setPlaceholder("Filter");
        populationFilter2.setClearButtonVisible(true);
        populationFilter2.setWidth("100%");
        populationFilter2.addValueChangeListener(
                event -> dataProvider.addFilter(simulation -> arePopulations2Equal(simulation, populationFilter2)));
        filterRow.getCell(population2Column).setComponent(populationFilter2);

        TextField amountFilter2 = new TextField();
        amountFilter2.setPlaceholder("Filter");
        amountFilter2.setClearButtonVisible(true);
        amountFilter2.setWidth("100%");
        amountFilter2.setValueChangeMode(ValueChangeMode.EAGER);
        amountFilter2.addValueChangeListener(event -> dataProvider.addFilter(simulation -> StringUtils
                .containsIgnoreCase(Double.toString(simulation.getPopulation2Amount()), amountFilter2.getValue())));
        filterRow.getCell(population2Amount).setComponent(amountFilter2);

        ComboBox<String> populationFilter3 = new ComboBox<>();
        populationFilter3.setItems(Arrays.asList(protocellsPopulationAvailable));
        populationFilter3.setPlaceholder("Filter");
        populationFilter3.setClearButtonVisible(true);
        populationFilter3.setWidth("100%");
        populationFilter3.addValueChangeListener(
                event -> dataProvider.addFilter(simulation -> arePopulations3Equal(simulation, populationFilter3)));
        filterRow.getCell(population3Column).setComponent(populationFilter3);

        TextField amountFilter3 = new TextField();
        amountFilter3.setPlaceholder("Filter");
        amountFilter3.setClearButtonVisible(true);
        amountFilter3.setWidth("100%");
        amountFilter3.setValueChangeMode(ValueChangeMode.EAGER);
        amountFilter3.addValueChangeListener(event -> dataProvider.addFilter(simulation -> StringUtils
                .containsIgnoreCase(Double.toString(simulation.getPopulation3Amount()), amountFilter3.getValue())));
        filterRow.getCell(population3Amount).setComponent(amountFilter3);

        ComboBox<String> ssDNAFilter = new ComboBox<>();
        ssDNAFilter.setItems(Arrays.asList(ssDNAPopulationAvailable));
        ssDNAFilter.setPlaceholder("Filter");
        ssDNAFilter.setClearButtonVisible(true);
        ssDNAFilter.setWidth("100%");
        ssDNAFilter.addValueChangeListener(
                event -> dataProvider.addFilter(simulation -> areSSDNAPopulationsEqual(simulation, ssDNAFilter)));
        filterRow.getCell(ssdnaColumn).setComponent(ssDNAFilter);

        TextField ssDNAAmountFilter = new TextField();
        ssDNAAmountFilter.setPlaceholder("Filter");
        ssDNAAmountFilter.setClearButtonVisible(true);
        ssDNAAmountFilter.setWidth("100%");
        ssDNAAmountFilter.setValueChangeMode(ValueChangeMode.EAGER);
        ssDNAAmountFilter.addValueChangeListener(event -> dataProvider.addFilter(simulation -> StringUtils
                .containsIgnoreCase(Double.toString(simulation.getInitialSSDNAAmount()), ssDNAAmountFilter.getValue())));
        filterRow.getCell(ssdnaAmountColumn).setComponent(ssDNAAmountFilter);
    }

    private boolean arePopulations1Equal(Simulation simulation, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return StringUtils.equals(simulation.getPopulation1(), statusFilterValue);
        }
        return true;
    }

    private boolean arePopulations2Equal(Simulation simulation, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return StringUtils.equals(simulation.getPopulation2(), statusFilterValue);
        }
        return true;
    }

    private boolean arePopulations3Equal(Simulation simulation, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return StringUtils.equals(simulation.getPopulation3(), statusFilterValue);
        }
        return true;
    }

    private boolean areSSDNAPopulationsEqual(Simulation simulation, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return StringUtils.equals(simulation.getInitialSSDNA(), statusFilterValue);
        }
        return true;
    }

    private List<Simulation> getClients() {

        // Recall...
        // protocellsPopulationAvailable = {"None", "Protocell A", "Protocell T", "Protocell X", "Protocell R"};
        // ssDNAPopulationAvailable = {"None", "SSDNA type 0", "SSDNA type 1", "SSDNA type 2"};


        return Arrays.asList(
                createSimulation(1, "Protocell A", 15,
                        "None", 0,
                        "None", 0,
                        "SSDNA type 0", 100,
                        "Petri Dish"),
                createSimulation(2, "Protocell A", 10,
                        "Protocell T", 10,
                        "None", 0,
                        "SSDNA type 0", 100,
                        "Petri Dish"),
                createSimulation(3, "Protocell A", 10,
                        "Protocell T", 10,
                        "Protocell X", 10,
                        "SSDNA type 0", 60,
                        "Petri Dish"),
                createSimulation(4, "Protocell A", 15,
                        "None", 0,
                        "Protocell X", 20,
                        "SSDNA type 0", 100,
                        "Petri Dish"));
    }

    private Simulation createSimulation(int id, String pop1, int pop1Amount, String pop2, int pop2Amount,
                                        String pop3, int pop3Amount, String ssdna, int ssdnaAmount,
                                        String environment) {
        Simulation c = new Simulation();
        c.setId(id);
        c.setPopulation1(pop1);
        c.setPopulation1Amount(pop1Amount);
        c.setPopulation2(pop2);
        c.setPopulation2Amount(pop2Amount);
        c.setPopulation3(pop3);
        c.setPopulation3Amount(pop3Amount);
        c.setInitialSSDNA(ssdna);
        c.setInitialSSDNAAmount(ssdnaAmount);
        c.setEnvironment(environment);

        return c;
    }

    private final static Logger logger = Logger.getLogger(SimulationsView.class.getName());

    private void startSimulation(ClickEvent<Button> buttonClickEvent) {
        Environment.prepared_simulation = buttonClickEvent.getButton(); // returns an int
        logger.log(Level.INFO, "Simulation integer " + Integer.toString(buttonClickEvent.getButton()));
        getUI().ifPresent(ui ->
                ui.navigate("run"));
    }
};
