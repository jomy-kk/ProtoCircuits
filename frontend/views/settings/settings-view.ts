import '@vaadin/vaadin-split-layout';
import '@vaadin/vaadin-grid';
import '@vaadin/vaadin-grid/vaadin-grid-column';
import '@vaadin/vaadin-form-layout';
import '@vaadin/vaadin-text-field';
import '@vaadin/vaadin-date-picker';
import '@vaadin/vaadin-button';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import { customElement, html, LitElement } from 'lit-element';

@customElement('settings-view')
export class SettingsView extends LitElement {
  createRenderRoot() {
    // Do not use a shadow root
    return this;
  }

  render() {
    return html`<vaadin-split-layout style="width: 100%; height: 100%;">
      <div style="flex-grow:1;width:100%;" id="grid-wrapper">
        <vaadin-grid id="grid"></vaadin-grid>
      </div>
      <div style="width:400px;display:flex;flex-direction:column;">
        <div style="padding:var(--lumo-space-l);flex-grow:1;">
          <vaadin-form-layout>
            <vaadin-text-field label="First name" id="firstName"></vaadin-text-field><vaadin-text-field label="Last name" id="lastName"></vaadin-text-field><vaadin-text-field label="Email" id="email"></vaadin-text-field><vaadin-text-field label="Phone" id="phone"></vaadin-text-field><vaadin-date-picker label="Date of birth" id="dateOfBirth"></vaadin-date-picker><vaadin-text-field label="Occupation" id="occupation"></vaadin-text-field><vaadin-checkbox id="important" style="padding-top: var(--lumo-space-m);"
            >Important</vaadin-checkbox
          >
          </vaadin-form-layout>
        </div>
        <vaadin-horizontal-layout
          style="flex-wrap:wrap;width:100%;background-color:var(--lumo-contrast-5pct);padding:var(--lumo-space-s) var(--lumo-space-l);"
          theme="spacing"
        >
          <vaadin-button theme="primary" id="save">Save</vaadin-button>
          <vaadin-button theme="tertiary" slot="" id="cancel">Cancel</vaadin-button>
        </vaadin-horizontal-layout>
      </div>
    </vaadin-split-layout>`;
  }
}
