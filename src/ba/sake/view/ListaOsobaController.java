package ba.sake.view;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import ba.sake.Main;
import ba.sake.dao.OsobaDAO;
import ba.sake.dao.model.Osoba;
import ba.sake.utils.DateUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
	private OsobaDAO dao = OsobaDAO.getOsobaDAO();

	private ObservableList<Osoba> osobe = FXCollections.observableArrayList();

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

	public void setupListaOsoba(Main main) {
		this.main = main;
	}

	@FXML
	private void initialize() {

		// ime
		columnIme.setEditable(true);
		columnIme.setCellValueFactory( // vrijednost za prikazivanje, mijenjanje
				new Callback<TableColumn.CellDataFeatures<Osoba, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Osoba, String> param) {
						return new SimpleStringProperty(param.getValue().getIme());
					}
				});
		columnIme.setCellFactory(TextFieldTableCell.<Osoba> forTableColumn());
		columnIme.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Osoba, String>>() {
			@Override
			public void handle(CellEditEvent<Osoba, String> t) {
				Osoba osoba = ((Osoba) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				osoba.setIme(t.getNewValue());
				dao.updateOsoba(osoba);
			}
		});

		// prezime
		columnPrezime.setEditable(true);
		columnPrezime.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Osoba, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Osoba, String> param) {
						return new SimpleStringProperty(param.getValue().getPrezime());
					}
				});
		columnPrezime.setCellFactory(TextFieldTableCell.<Osoba> forTableColumn());
		columnPrezime.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Osoba, String>>() {
			@Override
			public void handle(CellEditEvent<Osoba, String> t) {
				Osoba osoba = ((Osoba) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				osoba.setPrezime(t.getNewValue());
				dao.updateOsoba(osoba);
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
					LocalDate noviDatum = LocalDate.parse(noviDatumStr);
					osoba.setDatumRodjenja(noviDatum);
					dao.updateOsoba(osoba);
				} catch (DateTimeParseException e) {
					System.out.println("ne valja ti datum jarane");
				}
			}
		});

		// tabela mora biti editabilna
		tableOsobe.setEditable(true);
		tableOsobe.setItems(osobe);
		updateOsobeList();

	}

	private void updateOsobeList() {
		osobe.clear();
		osobe.addAll(dao.listOsoba());
	}

	@FXML
	private void unesiOsobu() {
		if (isInputValid()) {
			Osoba nova = new Osoba(txtIme.getText(), txtPrezime.getText(), pickDatumRodjenja.getValue());
			dao.addOsoba(nova);
			updateOsobeList();
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
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}
}
