import { customElement, html, LitElement } from 'lit-element';
import '@vaadin/vaadin-radio-button/src/vaadin-radio-group.js';
import '@vaadin/vaadin-radio-button/src/vaadin-radio-button.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';
import '@polymer/iron-icon/iron-icon.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-text-field/src/vaadin-text-field.js';
import '@vaadin/vaadin-charts/src/vaadin-chart.js';

@customElement('running-view')
export class RunningView extends LitElement {
  createRenderRoot() {
    // Do not use a shadow root
    return this;
  }
  render() {
    return html`
<div>
 <div style="width: 100%; height: 100%; background-color: rgb(230,230,230); padding: var(--lumo-space-xs);">
  <vaadin-horizontal-layout theme="spacing">
   <vaadin-horizontal-layout style="flex-direction: row; align-items: center; justify-content: flex-start;">
    <vaadin-radio-group value="foo" style="padding-left: var(--lumo-space-s); flex-grow: 0; flex-shrink: 1; align-self: center; margin-bottom: -3pt;" id="environmentSelection">
      Environment: 
     <vaadin-radio-button name="foo" style="margin-left: 6pt;" checked id="petriDishOptionButton">
       Petri Dish 
     </vaadin-radio-button>
     <vaadin-radio-button name="bar" id="bloodVesselOptionButton" tabindex="-1">
       Blood Vessel 
     </vaadin-radio-button>
     <vaadin-radio-button name="baz" id="gutOptionButton" tabindex="-1">
       Gut 
     </vaadin-radio-button>
    </vaadin-radio-group>
   </vaadin-horizontal-layout>
   <vaadin-horizontal-layout theme="spacing" style="flex-grow: 1; flex-shrink: 0; justify-content: flex-end; align-items: center; padding-right: var(--lumo-space-m);" id="vaadinHorizontalLayout">
    <span style="margin-right: -4pt;">Each iteration:</span>
    <vaadin-text-field value="500" theme="align-right" style="width: 70pt; flex-grow: 0; flex-shrink: 0;" placeholder="500" autoselect has-value id="stepInput">
     <div slot="suffix">
       ms 
     </div>
    </vaadin-text-field>
    <vaadin-button theme="primary" id="startButton">
      Start 
    </vaadin-button>
    <vaadin-button id="stopButton">
      Stop 
    </vaadin-button>
    <vaadin-button theme="icon" aria-label="Refresh" tabindex="" id="restartButton">
     <iron-icon icon="lumo:arrow-left"></iron-icon>
    </vaadin-button>
   </vaadin-horizontal-layout>
  </vaadin-horizontal-layout>
 </div>
 <vaadin-horizontal-layout theme="spacing-l" style="width: 100%; padding: var(--lumo-space-xl); min-height: 400pt;">
  <div id="simulation-area" style="width: 400pt; height: 400pt; border-radius:8pt; min-width: 400pt; min-height: 400pt; padding: var(--lumo-space-s);
    " class="no-env"></div>
  <vaadin-chart type="area" subtitle="Graphic A" categories="" stacking="normal" no-legend="" tooltip="" style="height: 400pt; align-self: center;" id="topChart"></vaadin-chart>
 </vaadin-horizontal-layout>
</div>
`;
  }
}
