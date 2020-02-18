package org.vaadin.jonni;

import org.apache.commons.csv.CSVRecord;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class AccordionInputPanel extends AccordionPanel {

	private final CSVRecord record;
	private final VerticalLayout content;
	private final NumberField numberField;

	public AccordionInputPanel(CSVRecord record) {
		super();
		this.record = record;
		content = new VerticalLayout();
		setSummaryText(record.get(0) + " " + getName());
		setContent(content);

		numberField = new NumberField();
		numberField.setValueChangeMode(ValueChangeMode.EAGER);
		numberField.setSuffixComponent(new Span(getInputUnit()));
		content.add(numberField);
	}

	public String getName() {
		return record.get(1);
	}

	public String getInputUnit() {
		return record.get(2);
	}

	public Integer getInputValue() {
		return numberField.getValue() != null ? numberField.getValue().intValue() : null;
	}

	public void focusInput() {
		numberField.focus();
	}

}
