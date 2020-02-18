package org.vaadin.jonni;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.Command;

@Route("")
@CssImport("./styles/shared-styles.css")
public class DataEntryView extends VerticalLayout {

	private Accordion accordion;
	private Button saveButton;

	public DataEntryView() {
		accordion = new Accordion();
		populateAccordion();

		saveButton = new Button("Save", click -> {

		});
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		accordion.addOpenedChangeListener(event -> {
			Optional<AccordionPanel> openPanel = event.getOpenedPanel();
			if (openPanel.isPresent()) {
				((AccordionInputPanel) openPanel.get()).focusInput();
			}
		});
		createLayout();

		addShortcutListener();

	}

	private void createLayout() {
		Button saveButton = new Button("Save", new Icon(VaadinIcon.HARDDRIVE), click -> saveClicked());
		Button nextButton = new Button(new Icon(VaadinIcon.ARROW_CIRCLE_DOWN), click -> focusNextPanel());
		Button prevButton = new Button(new Icon(VaadinIcon.ARROW_CIRCLE_UP), click -> focusPreviousPanel());
		HorizontalLayout bottomButtons = new HorizontalLayout(prevButton, nextButton, saveButton);
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
		nextButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
		prevButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
		bottomButtons.setJustifyContentMode(JustifyContentMode.END);
		bottomButtons.addClassName("bottom-buttons");

		add(accordion, bottomButtons);
	}

	private void saveClicked() {
		String values = accordion.getChildren()

				.map(inputPanel -> (AccordionInputPanel) inputPanel)

				.filter(i -> i.getInputValue() != null)

				.map(i -> i.getName() + ": " + i.getInputValue())

				.collect(Collectors.joining("\n"));
		removeAll();
		TextArea textArea = new TextArea(null, "Datestamp: " + Instant.now().toString() + "\n" + values, "");
		textArea.setWidthFull();
		add(textArea);
	}

	private void addShortcutListener() {
		Command command = () -> {
			focusNextPanel();
		};
		Shortcuts.addShortcutListener(this, command, Key.ENTER).listenOn(accordion);
	}

	private void focusNextPanel() {
		int nextFocusIdx = 0;
		int idx = accordion.getOpenedIndex().getAsInt();
		if (idx < accordion.getChildren().count() - 1) {
			nextFocusIdx = idx + 1;
		}
		accordion.open(nextFocusIdx);
	}

	private void focusPreviousPanel() {
		int nextFocusIdx = (int) (accordion.getChildren().count() - 1);
		int idx = accordion.getOpenedIndex().getAsInt();
		if (idx > 0) {
			nextFocusIdx = idx - 1;
		}
		accordion.open(nextFocusIdx);
	}

	private void populateAccordion() {
		Reader in;
		try {
			in = new FileReader("src/main/resources/data_items.csv");
			Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);

			for (CSVRecord record : records) {
				accordion.add(new AccordionInputPanel(record));

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
