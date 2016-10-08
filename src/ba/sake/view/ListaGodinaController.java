package ba.sake.view;

import ba.sake.Main;
import ba.sake.dao.GodinaDAO;
import ba.sake.dao.model.Godina;
import ba.sake.dao.model.Osoba;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class ListaGodinaController {

	private Main main;
	private ObservableList<Godina> godine = GodinaDAO.getGodinaDAO().listGodina();

	@FXML
	private TableView<Godina> tableGodine;
	@FXML
	private TableColumn<Godina, String> columnGodina;
	@FXML
	private TableColumn<Godina, String> columnCijena;

	@FXML
	private Spinner<Integer> spinGodina;
	@FXML
	private Spinner<Double> spinCijena;
	@FXML
	private Button btnDodaj;
	@FXML
	private Button btnObrisi;

	public void setupListaGodina(Main main) {
		this.main = main;
		tableGodine.setItems(godine);
	}

	@FXML
	private void initialize() {
		
		columnGodina.setEditable(true);
		columnGodina.setCellValueFactory(cellData -> cellData.getValue().godinaProperty().asString());
		columnGodina.setCellFactory(TextFieldTableCell.<Godina> forTableColumn());
		columnGodina.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Godina, String>>() {
			@Override
			public void handle(CellEditEvent<Godina, String> t) {
				Godina godina = ((Godina) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				try {
					Integer newValue = Integer.parseInt(t.getNewValue());
					godina.setGodina(newValue);					
					int i = godine.indexOf(godina);
					godine.set(i, godina);
				} catch (NumberFormatException e) {
					// TODO
				}
			}
		});
		
		columnCijena.setEditable(true);
		columnCijena.setCellValueFactory(cellData -> cellData.getValue().cijenaProperty().asString());
		columnCijena.setCellFactory(TextFieldTableCell.<Godina> forTableColumn());
		columnCijena.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Godina, String>>() {
			@Override
			public void handle(CellEditEvent<Godina, String> t) {
				Godina godina = ((Godina) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				try {
					Double newValue = Double.parseDouble(t.getNewValue());
					godina.setCijena(newValue);
					int i = godine.indexOf(godina);
					godine.set(i, godina);
				} catch (NumberFormatException e) {
					// TODO
				}
			}
		});
		
		SpinnerValueFactory<Integer> svfGod = new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2100);
		spinGodina.setValueFactory(svfGod);
		
		SpinnerValueFactory<Double> svfCij = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 200);
		spinCijena.setValueFactory(svfCij);

		// tabela mora biti editabilna
		tableGodine.setEditable(true);

		if(godine.isEmpty()) btnObrisi.setDisable(true);
		godine.addListener(new ListChangeListener<Godina>(){
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Godina> c) {
				if(godine.isEmpty()) {
					btnObrisi.setDisable(true);
				} else {
					btnObrisi.setDisable(false);
				}
			}			
		});
	}

	@FXML
	private void unesiGodinu() {
		if (isInputValid()) {			
			Integer godina = spinGodina.getValue();
			Double cijena = spinCijena.getValue();			
			Godina nova = new Godina(godina, cijena);			
			godine.add(nova);
		}
	}

	private boolean isInputValid() {

		String errorMessage = "";
		
		try {
			Integer godina = spinGodina.getValue();
		} catch (NumberFormatException e) {
			// TODO provjerit je li >1900 itd
			errorMessage = "Unesena godina nije u ispravnom formatu";
		}
		try {
			Double cijena = spinCijena.getValue();
		} catch (NumberFormatException e) {
			// TODO provjerit je li >1900 itd
			errorMessage = "Unesena cijena nije u ispravnom formatu";
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
	private void obrisiGodinu() {
	    int selectedIndex = tableGodine.getSelectionModel().getSelectedIndex();
	    if (selectedIndex >= 0) {
	    	Godina godina = tableGodine.getSelectionModel().getSelectedItem();
	    	godine.remove(godina);
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
