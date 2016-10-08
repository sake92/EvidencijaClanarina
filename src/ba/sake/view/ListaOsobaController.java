package ba.sake.view;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import ba.sake.Main;
import ba.sake.dao.OsobaDAO;
import ba.sake.dao.model.Osoba;
import ba.sake.utils.DateUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class ListaOsobaController {

	private Main main;

	private ObservableList<Osoba> osobe = OsobaDAO.getOsobaDAO().listOsoba();

	@FXML
	private TableView<Osoba> tableOsobe;
	@FXML
	private TableColumn<Osoba, String> columnIme;
	@FXML
	private TableColumn<Osoba, String> columnPrezime;
	@FXML
	private TableColumn<Osoba, String> columnDatumRodjenja;

	@FXML
	private TextField txtIme;
	@FXML
	private TextField txtPrezime;
	@FXML
	private DatePicker pickDatumRodjenja;
	@FXML
	private Button btnDodaj;
	@FXML
	private Button btnObrisi;

	public void setupListaOsoba(Main main) {
		this.main = main;
		tableOsobe.setItems(osobe);
	}

	@FXML
	private void initialize() {

		// ime
		columnIme.setEditable(true);
		columnIme.setCellValueFactory(cellData -> cellData.getValue().imeProperty());
		columnIme.setCellFactory(TextFieldTableCell.<Osoba> forTableColumn());
		columnIme.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Osoba, String>>() {
			@Override
			public void handle(CellEditEvent<Osoba, String> t) {
				Osoba o = t.getRowValue();
				o.setIme(t.getNewValue());
				int i = osobe.indexOf(o);
				osobe.set(i, o);
			}
		});

		// prezime
		columnPrezime.setEditable(true);
		columnPrezime.setCellValueFactory(cellData -> cellData.getValue().prezimeProperty());
		columnPrezime.setCellFactory(TextFieldTableCell.<Osoba> forTableColumn());
		columnPrezime.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Osoba, String>>() {
			@Override
			public void handle(CellEditEvent<Osoba, String> t) {
				Osoba o = t.getRowValue();
				o.setPrezime(t.getNewValue());
				int i = osobe.indexOf(o);
				osobe.set(i, o);
			}
		});

		// datum roðenja
		columnDatumRodjenja.setEditable(true);
		columnDatumRodjenja.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Osoba, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Osoba, String> param) {
						return new SimpleStringProperty(DateUtils.format(param.getValue().getDatumRodjenja()));
					}
				});
		columnDatumRodjenja.setCellFactory(TextFieldTableCell.<Osoba> forTableColumn());
		columnDatumRodjenja.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Osoba, String>>() {
			@Override
			public void handle(CellEditEvent<Osoba, String> t) {
				Osoba osoba = ((Osoba) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				String noviDatumStr = t.getNewValue();
				try {
					LocalDate noviDatum = DateUtils.parse(noviDatumStr);
					osoba.setDatumRodjenja(noviDatum);
					int i = osobe.indexOf(osoba);
					osobe.set(i, osoba);
				} catch (DateTimeParseException e) {
					System.out.println("Pogrešan format datuma!");
				}
			}
		});

		// tabela mora biti editabilna
		tableOsobe.setEditable(true);

		// disable Delete button when no items
		if (osobe.isEmpty())
			btnObrisi.setDisable(true);
		osobe.addListener((ListChangeListener.Change<? extends Osoba> change) -> {
			if (osobe.isEmpty()) {
				btnObrisi.setDisable(true);
			} else {
				btnObrisi.setDisable(false);
			}
		});
	}

	@FXML
	private void unesiOsobu() {
		if (isInputValid()) {
			Osoba newPerson = new Osoba(txtIme.getText(), txtPrezime.getText(), pickDatumRodjenja.getValue());
			osobe.add(newPerson);
			// clear inputs
			txtIme.setText("");
			txtPrezime.setText("");
			pickDatumRodjenja.setValue(null);
		}
	}

	private boolean isInputValid() {

		String errorMessage = "";

		if (txtIme.getText() == null || txtIme.getText().length() == 0) {
			errorMessage += "Ime nije pravilno uneseno!\n";
		}
		if (txtPrezime.getText() == null || txtPrezime.getText().length() == 0) {
			errorMessage += "Prezime nije pravilno uneseno!\n";
		}
		if (pickDatumRodjenja.getValue() == null) {
			errorMessage += "Datum roðenja nije unesen!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("Polja nisu validna");
			alert.setHeaderText("Molim popunite pravilno sva polja.");
			alert.setContentText(errorMessage);
			alert.showAndWait();
			return false;
		}
	}

	@FXML
	private void obrisiOsobu() {
		int selectedIndex = tableOsobe.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Osoba o = tableOsobe.getSelectionModel().getSelectedItem();
			osobe.remove(o);
		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("Nije selektovano");
			alert.setHeaderText("Osoba nije selektovana");
			alert.setContentText("Molim odaberite osobu u tabeli.");
			alert.showAndWait();
		}
	}
}
