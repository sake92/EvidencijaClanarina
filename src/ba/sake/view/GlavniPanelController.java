package ba.sake.view;

import ba.sake.Main;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class GlavniPanelController {

	private Main main;

	@FXML
	private MenuItem btnOsobe;
	@FXML
	private MenuItem btnEvidencije;
	@FXML
	private MenuItem btnGodine;

	public void setupGlavniPanel(Main main) {
		this.main = main;
	}

	@FXML
	public void prikaziOsobe() {
		main.showListaOsoba();
	}

	@FXML
	public void prikaziEvidencije() {
		main.showListaEvidencija();
	}

	@FXML
	public void prikaziGodine() {
		main.showListaGodina();
	}

}
