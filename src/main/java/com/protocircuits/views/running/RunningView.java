package com.protocircuits.views.running;


import com.protocircuits.simulations.*;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.protocircuits.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.protocircuits.simulations.LivingCell.LivingCellCondition;


@Route(value = "run", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Running")
@CssImport("./views/running/running-view.css")
@Tag("running-view")
@JsModule("./views/running/running-view.ts")
public class RunningView extends LitTemplate {

    // This is the Java companion file of a design
    // You can find the design file inside /frontend/views/

    private final static Logger logger = Logger.getLogger(RunningView.class.getName());

    // SIMULATION CONTROLS
    @Id("environmentSelection")
    private RadioButtonGroup<String> environmentSelection;
    @Id("stepInput")
    private TextField stepInput;
    @Id("startButton")
    private Button startButton;
    @Id("stopButton")
    private Button stopButton;
    @Id("restartButton")
    private Button restartButton;
    @Id("simulation-area")
    private Div simulationArea;
    private boolean _running = false;
    private int _step;

    // ENVIRONMENT LABELS
    public static final String PETRI_DISH = "Petri Dish";
    public static final String BLOOD_VESSEL = "Blood Vessel";
    public static final String GUT = "Gut";

    // ENVIRONMENT GRID
    public static final int N_ROWS      = 100;
    public static final int N_COLUMNS   = 100;
    private VerticalLayout squares_petri_dish;


    public RunningView() {

        logger.log(Level.INFO, "Loading Running view...");

        // Setting simulation backend
        Environment.nX = N_ROWS;
        Environment.nY = N_COLUMNS;
        Environment.envType = Environment.EnvironmentType.PETRI_DISH;
        Environment.associateGUI(this);
        Environment.initialize();
        //setUpAgents();

        // Setting UI
        environmentSelection.setLabel("Environment: ");
        environmentSelection.setItems(PETRI_DISH, BLOOD_VESSEL, GUT);
        environmentSelection.addValueChangeListener(event -> reset());
        environmentSelection.setValue(PETRI_DISH); // select automatically the Petri Dish environment
        startButton.addClickListener(this::startSimulation);
        stopButton.setEnabled(false);
        stopButton.addClickListener(this::stopSimulation);
        restartButton.addClickListener(this::restartSimulation);

        // Setting simulation area and squares

        // insert squares
        if (squares_petri_dish == null) { // do the same
            // set width of each square
            String width = this.simulationArea.getWidth();
            float width_value = Unit.getSize(width);
            int width_each_div = (int) width_value/N_ROWS;  // in points

            // set height of each square
            String height = this.simulationArea.getHeight();
            float height_value = Unit.getSize(height);
            int height_each_div = (int) height_value/N_COLUMNS;  // in points

            logger.log(Level.INFO, "Size of each square = (" + width_each_div + "," + height_each_div + ")");

            squares_petri_dish = new VerticalLayout();
            squares_petri_dish.setSpacing(false);
            squares_petri_dish.setPadding(false);
            for (int i = 0; i < N_ROWS; ++i) {
                HorizontalLayout columns_of_this_row = new HorizontalLayout();
                columns_of_this_row.setSpacing(false);
                columns_of_this_row.setPadding(false);
                for (int j = 0; j < N_COLUMNS; ++j) {
                    Div d = new Div();
                    d.setHeight(height_each_div, Unit.POINTS);
                    d.setWidth(width_each_div, Unit.POINTS);
                    d.setMinWidth(width_each_div, Unit.POINTS);
                    d.setMinHeight(height_each_div, Unit.POINTS);
                    if (Environment.environment[i][j] != null) { // && Environment.environment[i][j].shape == Block.Shape.FREE)
                        d.setClassName("test-color");
                        Environment.environment[i][j].setUISquare(d);
                    }
                    else if(i==0 && j==0) {
                        d.setClassName("resolution-color");
                    }

                    columns_of_this_row.add(d);
                }
                squares_petri_dish.add(columns_of_this_row);
            }
        }

        this.simulationArea.removeAll();
        this.simulationArea.add(squares_petri_dish);

        // Setting charts
        topChartDataSeries = new TreeMap<>();
        setupChart(topChart, _step);

        logger.log(Level.INFO, "Running view loaded successfully!");
    }

    /****************  LOADING ENVIRONMENTS  ***************/

    private void load_petri_dish_environment() {
        logger.log(Level.INFO, "Loading " + PETRI_DISH + " environment...");

        // sets background
        this.simulationArea.removeClassNames("blood-vessel-env", "gut-env");
        this.simulationArea.addClassName("petri-dish-env");

        logger.log(Level.INFO, PETRI_DISH + " environment loaded successfully!");

    }

    private void load_blood_vessel_environment() {
        // sets background
        this.simulationArea.removeClassNames("petri-dish-env", "gut-env");
        this.simulationArea.addClassName("blood-vessel-env");

        logger.log(Level.INFO, "Loading " + BLOOD_VESSEL + " environment...");
    }

    private void load_gut_environment() {
        // sets background
        this.simulationArea.removeClassNames("petri-dish-env", "blood-vessel-env");
        this.simulationArea.addClassName("gut-env");

        logger.log(Level.INFO, "Loading " + GUT + " environment...");
    }

    /****************  SIMULATION CONTROLS  ***************/

    private void startSimulation(ClickEvent<Button> buttonClickEvent) {
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        try {
            _step = Integer.parseInt(stepInput.getValue());
        }
        catch (NumberFormatException e){
            _step = Integer.parseInt(stepInput.getPlaceholder());
        }
        Environment.run(_step);
    }

    private void stopSimulation(ClickEvent<Button> buttonClickEvent) {
        Environment.stop();
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    private void restartSimulation(ClickEvent<Button> buttonClickEvent) {
        Environment.stop();
        reset();
        topChartDataSeries.clear();
    }

    private void reset(){
        String value = environmentSelection.getValue();
        if (environmentSelection.getValue() == null) {
            logger.log(Level.SEVERE, "No environment selected");
        } else {
            if (value.equals(PETRI_DISH))
                load_petri_dish_environment();
            if (value.equals(BLOOD_VESSEL))
                load_blood_vessel_environment();
            if (value.equals(GUT))
                load_gut_environment();
        }
        Environment.reset();
    }

    public void update() {
        getUI().ifPresent(ui -> ui.access(() -> { // lock thread session; this is mandatory by Vaadin
            for (int i = 0; i < N_ROWS; i++) {
                for (int j = 0; j < N_ROWS; j++) {
                    if (Environment.environment[i][j] != null) { // in petri dish, this automatically ignores blocks outside the circle

                        Div square = Environment.environment[i][j].UISquare;

                        // update entities
                        if (Environment.objects[i][j] != null) { // are there entities occupying this square?

                            if (Environment.objects[i][j] instanceof Protocell){
                                square.setClassName("PROTOCELL");
                                if (Environment.objects[i][j] instanceof Nanorobot) {
                                    if( ((Nanorobot) Environment.objects[i][j]).circuit instanceof AmplifierGeneticCircuit) {
                                        square.setClassName("PROTOCELL");
                                    }
                                    if( ((Nanorobot) Environment.objects[i][j]).circuit instanceof TransducerGeneticCircuit) {
                                        square.setClassName("TRANSDUCER");
                                    }
                                    if( ((Nanorobot) Environment.objects[i][j]).circuit instanceof AndGeneticCircuit) {
                                        square.setClassName("AND-GATE");
                                    }
                                }
                            }
                            if (Environment.objects[i][j] instanceof LivingCell) {
                                switch (((LivingCell) Environment.objects[i][j]).condition) {
                                    case HEALTHY:
                                        square.setClassName("CELL");
                                        break;
                                    case TUMOROUS:
                                        square.setClassName("TUMOROUS-CELL");
                                        break;
                                    case DEAD:
                                        square.setClassName("DEAD-CELL");
                                        break;
                                }
                            }


                            if (Environment.objects[i][j] instanceof SSDNA) {
                                square.setClassName("SSDNA-" + ((SSDNA) Environment.objects[i][j]).type);
                                // set colors in CSS file as classes .SSDNA-{n}
                            }
                        }
                        else { // nothing occupying this square
                            square.setClassName("FREE");

                            // update background blocks
                            //Block.Shape shape = Environment.environment[i][j].shape;
                            //Environment.environment[i][j].UISquare.setClassName(shape.name()); // see CSS for color styling
                        }
                    }
                }
            }

            // Charts
            for (Map.Entry<Integer, DataSeries> entry : topChartDataSeries.entrySet()) {
                updateChartSeries(entry.getValue(), Environment.ssDNACounters.get(entry.getKey()));
            }

            ui.push(); // push changes to the client-side
        }));
    }

    /****************  ADDING AGENTS  ***************/
    public void setUpAgents() {
        // TODO: get from Simulations tab
    }



    /****************  CHARTS  ***************/

    @Id("topChart")
    private Chart topChart;
    private Map<Integer, DataSeries> topChartDataSeries;
    private long _currentMilliseconds;


    public void setupChart(Chart chart, int timeInterval) {
        Configuration conf = chart.getConfiguration();
        //conf.getChart().setType(ChartType.AREASPLINE);
        conf.getChart().setType(ChartType.AREA);
        conf.getSubTitle().setText("Molecules concentration over simulation time");

        // X axis
        XAxis xAxis = conf.getxAxis();
        xAxis.setType(AxisType.DATETIME);
        xAxis.setTickPixelInterval(timeInterval);
        _currentMilliseconds = System.currentTimeMillis();

        // Y axis
        YAxis yAxis = conf.getyAxis();
        yAxis.setTitle(new AxisTitle("Concentration [mg/mL]"));

        conf.getTooltip().setEnabled(true); // allows pointer to show exact value
        conf.getLegend().setEnabled(true); // allows a visible legend of the series names
        chart.setTimeline(false); // allow for zoom timeline

        for (Map.Entry<Integer, Integer> entry: Environment.ssDNACounters.entrySet()) {

            String seriesName = "ssDNA type " + entry.getKey();
            int initialValue = entry.getValue();

            final DataSeries series = new DataSeries();
            //PlotOptionsAreaspline opts = new PlotOptionsAreaspline();
            PlotOptionsArea opts = new PlotOptionsArea();
            opts.setColorIndex(entry.getKey()); // set colors in CSS file as parameters --vaadin-charts-color-{n}
            series.setPlotOptions(opts);
            series.setName(seriesName);
            conf.addSeries(series);
            for (int i=-1; i < 0; ++i)
                series.add(new DataSeriesItem(_currentMilliseconds+500*i, initialValue));
            topChartDataSeries.put(entry.getKey(), series);
        }
    }

    public void updateChartSeries(DataSeries series, int value) {
        final long x = System.currentTimeMillis();
        _currentMilliseconds = _currentMilliseconds + _step;
        series.add(new DataSeriesItem(_currentMilliseconds, value), true, false);
    }


}




