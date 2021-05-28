import '@vaadin/vaadin-combo-box';
import '@vaadin/vaadin-date-picker';
import '@vaadin/vaadin-grid/all-imports';
import '@vaadin/vaadin-grid-pro';
import '@vaadin/vaadin-grid-pro/vaadin-grid-pro-edit-column';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-text-field';
import { customElement, html, LitElement } from 'lit-element';

@customElement('building-blocks-view')
export class BuildingBlocksView extends LitElement {
  createRenderRoot() {
    // Do not use a shadow root
    return this;
  }

  render() {
    return html`
<vaadin-vertical-layout theme="" style="justify-content: center; align-items: center; align-content: center; flex-direction: column; height: 100%; width: 100%;">
 <label style="width: 100%; margin: var(--lumo-space-m); margin-left: var(--lumo-space-xl);">Protocells</label>
 <vaadin-grid-pro id="protocells-grid" theme="no-border column-borders" style="width: 100%; min-width: 100%;" column-reordering-allowed></vaadin-grid-pro>
 <label style="width: 100%;  margin: var(--lumo-space-m); margin-left: var(--lumo-space-xl);">Molecules</label>
 <vaadin-grid-pro id="molecules-grid" theme="no-border column-borders" style="width: 100%; min-width: 100%;" column-reordering-allowed></vaadin-grid-pro>
</vaadin-vertical-layout>
`;
  }
}
