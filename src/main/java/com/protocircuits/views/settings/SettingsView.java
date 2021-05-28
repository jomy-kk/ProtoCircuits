package com.protocircuits.views.settings;

import java.util.Optional;

import com.protocircuits.data.entity.SamplePerson;
import com.protocircuits.data.service.SamplePersonService;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import org.vaadin.artur.helpers.CrudServiceDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.protocircuits.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.data.renderer.TemplateRenderer;

@Route(value = "settings", layout = MainView.class)
@PageTitle("Settings")
@CssImport("./views/settings/settings-view.css")
@Tag("settings-view")
@JsModule("./views/settings/settings-view.ts")
public class SettingsView extends LitTemplate {

    // This is the Java companion file of a design
    // You can find the design file inside /frontend/views/
    // The design can be easily edited by using Vaadin Designer
    // (vaadin.com/designer)

    @Id
    private Grid<SamplePerson> grid;

    @Id
    private TextField firstName;
    @Id
    private TextField lastName;
    @Id
    private TextField email;
    @Id
    private TextField phone;
    @Id
    private DatePicker dateOfBirth;
    @Id
    private TextField occupation;
    @Id
    private Checkbox important;

    @Id
    private Button cancel;
    @Id
    private Button save;

    private BeanValidationBinder<SamplePerson> binder;

    private SamplePerson samplePerson;

    public SettingsView(@Autowired SamplePersonService samplePersonService) {
        grid.addColumn(SamplePerson::getFirstName).setHeader("First Name").setAutoWidth(true);
        grid.addColumn(SamplePerson::getLastName).setHeader("Last Name").setAutoWidth(true);
        grid.addColumn(SamplePerson::getEmail).setHeader("Email").setAutoWidth(true);
        grid.addColumn(SamplePerson::getPhone).setHeader("Phone").setAutoWidth(true);
        grid.addColumn(SamplePerson::getDateOfBirth).setHeader("Date Of Birth").setAutoWidth(true);
        grid.addColumn(SamplePerson::getOccupation).setHeader("Occupation").setAutoWidth(true);
        TemplateRenderer<SamplePerson> importantRenderer = TemplateRenderer.<SamplePerson>of(
                "<iron-icon hidden='[[!item.important]]' icon='vaadin:check' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'></iron-icon><iron-icon hidden='[[item.important]]' icon='vaadin:minus' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'></iron-icon>")
                .withProperty("important", SamplePerson::isImportant);
        grid.addColumn(importantRenderer).setHeader("Important").setAutoWidth(true);

        grid.setDataProvider(new CrudServiceDataProvider<>(samplePersonService));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                Optional<SamplePerson> samplePersonFromBackend = samplePersonService.get(event.getValue().getId());
                // when a row is selected but the data is no longer available, refresh grid
                if (samplePersonFromBackend.isPresent()) {
                    populateForm(samplePersonFromBackend.get());
                } else {
                    refreshGrid();
                }
            } else {
                clearForm();
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(SamplePerson.class);

        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.samplePerson == null) {
                    this.samplePerson = new SamplePerson();
                }
                binder.writeBean(this.samplePerson);

                samplePersonService.update(this.samplePerson);
                clearForm();
                refreshGrid();
                Notification.show("SamplePerson details stored.");
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the samplePerson details.");
            }
        });
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(SamplePerson value) {
        this.samplePerson = value;
        binder.readBean(this.samplePerson);

    }
}
