package ba.sake.view;

import java.util.List;

import ba.sake.Main;
import ba.sake.dao.model.Osoba;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class OdaberiOsobeController {

	private Main main;
	private ObservableList<Osoba> osobe = FXCollections.observableArrayList();
	ListaEvidencijaController ctrlEvidencije;

	@FXML
	private ListView<Osoba> listOsoba;
	@FXML
	private Button btnDodaj;
	@FXML
	private Button btnOdustani;

	public void setupOdaberiOsobe(Main main, ListaEvidencijaController ctrlEvidencije) {
		this.main = main;
		this.ctrlEvidencije = ctrlEvidencije;
	}

	@FXML
	private void initialize() {
		listOsoba.setItems(osobe);
		listOsoba.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	public void setListaOsoba(List<Osoba> newOsobe) {
		this.osobe.clear();
		this.osobe.addAll(newOsobe);
	}

	@FXML
	public void dodaj() {
		List<Osoba> osobeZaDodat = listOsoba.getSelectionModel().getSelectedItems();
		ctrlEvidencije.setZaDodat(osobeZaDodat);
		main.showListaEvidencija();
	}

	@FXML
	public void odustani() {
		main.showListaEvidencija();
	}
}
