package ba.sake.view;

import ba.sake.Main;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class GlavniPanelController {

	private Main main;
	private boolean showEvidencije = false; // prvo se prikazuju evidencije;

	@FXML
	private MenuItem btnOsobeEvidencije;

	public void setupGlavniPanel(Main main) {
		this.main = main;
	}

	@FXML
	public void prikaziOsobeEvidencije() {

		if (showEvidencije) {
			btnOsobeEvidencije.setText("Osobe");
			main.showListaEvidencija();
		} else {
			btnOsobeEvidencije.setText("Evidencije");
			main.showListaOsoba();
		}

		showEvidencije = !showEvidencije; // toogle, promijeni ...
	}

}
