package com.protocircuits.views.buildingblocks;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

import com.protocircuits.views.simulations.Simulation;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
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
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.protocircuits.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;

@Route(value = "blocks", layout = MainView.class)
@PageTitle("Building Blocks")
@CssImport("./views/buildingblocks/building-blocks-view.css")
@Tag("building-blocks-view")
@JsModule("./views/buildingblocks/building-blocks-view.ts")
public class BuildingBlocksView extends LitTemplate {

    @Id("protocells-grid")
    private GridPro<Protocells> protocellsGrid;

    @Id("molecules-grid")
    private GridPro<Molecules> moleculesGrid;

    private ListDataProvider<Protocells> protocellsListDataProvider;
    private ListDataProvider<Molecules> moleculesListDataProvider;

    private Grid.Column<Protocells> nameColumn;
    private Grid.Column<Protocells> typeColumn;
    private Grid.Column<Protocells> gcColumn;
    private Grid.Column<Protocells> toehold1Column;
    private Grid.Column<Protocells> toehold2Column;
    private Grid.Column<Protocells> outputColumn;

    private Grid.Column<Molecules> nameMoleculeColumn;
    private Grid.Column<Molecules> subclassMoleculeColumn;
    private Grid.Column<Molecules> typeMoleculeColumn;
    private Grid.Column<Molecules> sequenceColumn;

    private String[] protocellTypes = new String[] {"Nanorobot"};
    private String[] moleculeTypes =    new String[] {"ssDNA", "RNA"};
    private String[] cellTypes =   new String[] { "Red Blood Cell", "Healthy cell", "Tumorous cell"};
    private String[] gcs = new String[] {"Amplifier", "Transducer", "AND-gate", "OR-gate", "NAND-gate", "XOR-gate", "Optogenetics"};
    private String[] ssdnas = new String[] {"ssDNA type 0", "ssDNA type 1", "ssDNA type 2"};


    public BuildingBlocksView() {
        createGridProtocells();
        createGridMolecules();
    }

    
    private void createGridProtocells() {
        createProtocellsGridComponent();
        
        createNameColumn();
        createTypeColumn();
        createGCColumn();
        createToehold1Column();
        createToehold2Column();
        createOutputColumn();
        
        addFiltersToProtocellsGrid();
    }

    private void createGridMolecules() {
        createMoleculesGridComponent();

        createMoleculeNameColumn();
        createMoleculeSublcassColumn();
        createSequenceColumn();
        createMoleculeTypeColumn();

        addFiltersToMoleculesGrid();
    }

    private void createProtocellsGridComponent() {
        protocellsGrid.setSelectionMode(SelectionMode.MULTI);
        protocellsGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);

        protocellsListDataProvider = new ListDataProvider<Protocells>(getProtocells());
        protocellsGrid.setDataProvider(protocellsListDataProvider);
    }

    private void createMoleculesGridComponent() {
        moleculesGrid.setSelectionMode(SelectionMode.MULTI);
        moleculesGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);

        moleculesListDataProvider = new ListDataProvider<Molecules>(getMolecules());
        moleculesGrid.setDataProvider(moleculesListDataProvider);
    }
    

    private void createNameColumn() {
        nameColumn = protocellsGrid.addEditColumn(Protocells::getName, new TextRenderer<>(protolocells -> protolocells.getName()))
                .text((item, newValue) -> item.setName(newValue))
                        .setComparator(protolocells -> protolocells.getName()).setHeader("Name");
    }

    private void createMoleculeNameColumn() {
        nameMoleculeColumn = moleculesGrid.addEditColumn(Molecules::getName, new TextRenderer<>(molecules -> molecules.getName()))
                .text((item, newValue) -> item.setName(newValue))
                .setComparator(molecules -> molecules.getName()).setHeader("Name");
    }

    private void createTypeColumn() {
        typeColumn = protocellsGrid.addEditColumn(Protocells::getType, new ComponentRenderer<>(protocells -> {
            Span span = new Span();
            span.setText(protocells.getType());
            return span;
        })).select((item, newValue) -> item.setType(newValue), Arrays.asList(protocellTypes))
                .setComparator(protocells -> protocells.getType()).setHeader("Agent type");
    }

    private void createMoleculeSublcassColumn() {
        subclassMoleculeColumn = moleculesGrid.addEditColumn(Molecules::getSublcass, new ComponentRenderer<>(molecules -> {
            Span span = new Span();
            span.setText(molecules.getSublcass());
            return span;
        })).select((item, newValue) -> item.setSublcass(newValue), Arrays.asList(moleculeTypes))
                .setComparator(molecules -> molecules.getSublcass()).setHeader("Agent type");
    }

    private void createGCColumn() {
        gcColumn = protocellsGrid.addEditColumn(Protocells::getGC, new ComponentRenderer<>(protolocells -> {
            Span span = new Span();
            span.setText(protolocells.getGC());
            return span;
        })).select((item, newValue) -> item.setGC(newValue), Arrays.asList(gcs))
                .setComparator(protolocells -> protolocells.getGC()).setHeader("Genetic Circuit");
    }

    private void createToehold1Column() {
        toehold1Column = protocellsGrid.addEditColumn(Protocells::getToehold1, new TextRenderer<>(protolocells -> protolocells.getToehold1()))
                .text((item, newValue) -> item.setToehold1(newValue))
                        .setComparator(protolocells -> protolocells.getToehold1()).setHeader("Toehold 1 Sequence");
    }

    private void createToehold2Column() {
        toehold2Column = protocellsGrid.addEditColumn(Protocells::getToehold2, new TextRenderer<>(protolocells -> protolocells.getToehold2()))
                .text((item, newValue) -> item.setToehold2(newValue))
                        .setComparator(protolocells -> protolocells.getToehold2()).setHeader("Toehold 2 Sequence");
    }

    private void createOutputColumn() {
        outputColumn = protocellsGrid.addEditColumn(Protocells::getGC, new ComponentRenderer<>(protolocells -> {
            Span span = new Span();
            span.setText(protolocells.getOutput());
            return span;
        })).select((item, newValue) -> item.setOutput(newValue), Arrays.asList(ssdnas))
                .setComparator(protolocells -> protolocells.getOutput()).setHeader("Output");
    }

    
    private void createSequenceColumn() {
        sequenceColumn = moleculesGrid.addEditColumn(Molecules::getSequence, new TextRenderer<>(molecules -> molecules.getSequence()))
                .text((item, newValue) -> item.setSequence(newValue))
                        .setComparator(protolocells -> protolocells.getSequence()).setHeader("Strand Sequence");
    }


    private void createMoleculeTypeColumn() {
        typeMoleculeColumn = moleculesGrid
                .addEditColumn(Molecules::getType,
                        new NumberRenderer<>(molecules -> molecules.getType(), NumberFormat.getIntegerInstance()))
                .text((item, newValue) -> item.setType(Integer.parseInt(newValue)))
                .setComparator(molecules -> molecules.getType()).setHeader("Type Number").setWidth("50px");
    }


    private void addFiltersToMoleculesGrid() {
        HeaderRow filterRow = moleculesGrid.appendHeaderRow();

        TextField nameFilter = new TextField();
        nameFilter.setPlaceholder("Filter");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setWidth("100%");
        nameFilter.setValueChangeMode(ValueChangeMode.EAGER);
        nameFilter.addValueChangeListener(event -> moleculesListDataProvider
                .addFilter(molecules -> StringUtils.containsIgnoreCase(molecules.getName(), nameFilter.getValue())));
        filterRow.getCell(nameMoleculeColumn).setComponent(nameFilter);

        TextField sequenceFilter = new TextField();
        sequenceFilter.setPlaceholder("Filter");
        sequenceFilter.setClearButtonVisible(true);
        sequenceFilter.setWidth("100%");
        sequenceFilter.setValueChangeMode(ValueChangeMode.EAGER);
        sequenceFilter.addValueChangeListener(event -> moleculesListDataProvider.addFilter(molecules -> StringUtils
                .containsIgnoreCase(molecules.getSequence(), sequenceFilter.getValue())));
        filterRow.getCell(sequenceColumn).setComponent(sequenceFilter);

        ComboBox<String> sublcassFilter = new ComboBox<>();
        sublcassFilter.setItems(Arrays.asList(moleculeTypes));
        sublcassFilter.setPlaceholder("Filter");
        sublcassFilter.setClearButtonVisible(true);
        sublcassFilter.setWidth("100%");
        sublcassFilter.addValueChangeListener(
                event -> moleculesListDataProvider.addFilter(molecules -> areMoleculesEqual(molecules, sublcassFilter)));
        filterRow.getCell(subclassMoleculeColumn).setComponent(sublcassFilter);

        TextField typeFilter = new TextField();
        typeFilter.setPlaceholder("Filter");
        typeFilter.setClearButtonVisible(true);
        typeFilter.setWidth("100%");
        typeFilter.setValueChangeMode(ValueChangeMode.EAGER);
        typeFilter.addValueChangeListener(event -> moleculesListDataProvider.addFilter(molecules -> StringUtils
                .containsIgnoreCase(Double.toString(molecules.getType()), typeFilter.getValue())));
        filterRow.getCell(typeMoleculeColumn).setComponent(typeFilter);
    }
    

    private void addFiltersToProtocellsGrid() {
        HeaderRow filterRow = protocellsGrid.appendHeaderRow();

        TextField nameFilter = new TextField();
        nameFilter.setPlaceholder("Filter");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setWidth("100%");
        nameFilter.setValueChangeMode(ValueChangeMode.EAGER);
        nameFilter.addValueChangeListener(event -> protocellsListDataProvider
                .addFilter(protocells -> StringUtils.containsIgnoreCase(protocells.getName(), nameFilter.getValue())));
        filterRow.getCell(nameColumn).setComponent(nameFilter);

        TextField toehold1Filter = new TextField();
        toehold1Filter.setPlaceholder("Filter");
        toehold1Filter.setClearButtonVisible(true);
        toehold1Filter.setWidth("100%");
        toehold1Filter.setValueChangeMode(ValueChangeMode.EAGER);
        toehold1Filter.addValueChangeListener(event -> protocellsListDataProvider.addFilter(protocells -> StringUtils
                .containsIgnoreCase(protocells.getToehold1(), toehold1Filter.getValue())));
        filterRow.getCell(toehold1Column).setComponent(toehold1Filter);

        TextField toehold2Filter = new TextField();
        toehold2Filter.setPlaceholder("Filter");
        toehold2Filter.setClearButtonVisible(true);
        toehold2Filter.setWidth("100%");
        toehold2Filter.setValueChangeMode(ValueChangeMode.EAGER);
        toehold2Filter.addValueChangeListener(event -> protocellsListDataProvider.addFilter(protocells -> StringUtils
                .containsIgnoreCase(protocells.getToehold2(), toehold2Filter.getValue())));
        filterRow.getCell(toehold2Column).setComponent(toehold2Filter);

        ComboBox<String> outputFilter = new ComboBox<>();
        outputFilter.setItems(Arrays.asList(ssdnas));
        outputFilter.setPlaceholder("Filter");
        outputFilter.setClearButtonVisible(true);
        outputFilter.setWidth("100%");
        outputFilter.addValueChangeListener(
                event -> protocellsListDataProvider.addFilter(protocells -> areSSDNAsEqual(protocells, outputFilter)));
        filterRow.getCell(outputColumn).setComponent(outputFilter);

        ComboBox<String> gcsFilter = new ComboBox<>();
        gcsFilter.setItems(Arrays.asList(gcs));
        gcsFilter.setPlaceholder("Filter");
        gcsFilter.setClearButtonVisible(true);
        gcsFilter.setWidth("100%");
        gcsFilter.addValueChangeListener(
                event -> protocellsListDataProvider.addFilter(protocells -> areGCsEqual(protocells, gcsFilter)));
        filterRow.getCell(gcColumn).setComponent(gcsFilter);


        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.setItems(Arrays.asList(protocellTypes));
        typeFilter.setPlaceholder("Filter");
        typeFilter.setClearButtonVisible(true);
        typeFilter.setWidth("100%");
        typeFilter.addValueChangeListener(
                event -> protocellsListDataProvider.addFilter(protocells -> areTypesEqual(protocells, typeFilter)));
        filterRow.getCell(typeColumn).setComponent(typeFilter);
    }

    private boolean areGCsEqual(Protocells protocells, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return StringUtils.equals(protocells.getGC(), statusFilterValue);
        }
        return true;
    }

    private boolean areSSDNAsEqual(Protocells protocells, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return StringUtils.equals(protocells.getOutput(), statusFilterValue);
        }
        return true;
    }

    private boolean areTypesEqual(Protocells protocells, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return StringUtils.equals(protocells.getType(), statusFilterValue);
        }
        return true;
    }

    private boolean areMoleculesEqual(Molecules molecules, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return StringUtils.equals(molecules.getSublcass(), statusFilterValue);
        }
        return true;
    }



    private List<Protocells> getProtocells() {
        return Arrays.asList(
                createProtocell("Protocell A", "Nanorobot", "Amplifier", "AAC", "", "ssDNA type 0"),
                createProtocell("Protocell T", "Nanorobot", "Transducer", "AAC", "", "ssDNA type 1"),
                createProtocell("Protocell X", "Nanorobot", "AND-gate", "AAC", "GGC", "ssDNA type 2")
                );
    }

    private List<Molecules> getMolecules() {
        return Arrays.asList(
                createMolecule("ssDNA type 0", "ssDNA", "TTGACTGTTAC", 0),
                createMolecule("ssDNA type 1", "ssDNA", "CCGCTACGAGCG", 1),
                createMolecule("ssDNA type 2", "ssDNA", "ACTCGATGTC", 2)
        );
    }

    private Protocells createProtocell(String name, String type, String gc, String t1, String t2, String output) {
        Protocells c = new Protocells();
        c.setName(name);
        c.setType(type);
        c.setGC(gc);
        c.setToehold1(t1);
        c.setToehold2(t2);
        c.setOutput(output);

        return c;
    }

    private Molecules createMolecule(String name, String subclass, String sequence, int type) {
        Molecules c = new Molecules();
        c.setName(name);
        c.setSublcass(subclass);
        c.setSequence(sequence);
        c.setType(type);

        return c;
    }
};
