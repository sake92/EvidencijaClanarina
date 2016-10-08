package ba.sake.view;

import java.util.ArrayList;
import java.util.List;

import ba.sake.Main;
import ba.sake.dao.EvidencijaDAO;
import ba.sake.dao.GodinaDAO;
import ba.sake.dao.OsobaDAO;
import ba.sake.dao.model.Evidencija;
import ba.sake.dao.model.Godina;
import ba.sake.dao.model.Osoba;
import ba.sake.utils.DateUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class ListaEvidencijaController {
	
	private Main main;

	// DAO	
	private OsobaDAO daoOsobe = OsobaDAO.getOsobaDAO();
	private GodinaDAO daoGodine = GodinaDAO.getGodinaDAO();	
	private EvidencijaDAO daoEvidencije = EvidencijaDAO.getEvidencijaDAO();
	
	private ObservableList<Evidencija> evidencije =  EvidencijaDAO.getEvidencijaDAO().listEvidencija();
	
	@FXML
	private ChoiceBox<Godina> choiceGodina;
	private Godina trenutnaGodina;
	
	// tabela
	@FXML
	private TableView<Evidencija> evidencijeTable;
	@FXML
	private TableColumn<Evidencija, String> imeColumn;
	@FXML
	private TableColumn<Evidencija, String> prezimeColumn;
	@FXML
	private TableColumn<Evidencija, Boolean> studentColumn;
	@FXML
	private TableColumn<Evidencija, Boolean> platioColumn;
	@FXML
	private TableColumn<Evidencija, String> biljeskaColumn;

	// detalji osobe
	@FXML
	private Label imeLabel;
	@FXML
	private Label prezimeLabel;
	@FXML
	private Label datumRodjenjaLabel;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		
		/* inicijalizacija kolona */		
		imeColumn.setCellValueFactory(cellData -> cellData.getValue().getOsoba().imeProperty());
		
		prezimeColumn.setCellValueFactory(cellData -> cellData.getValue().getOsoba().prezimeProperty());
			
		studentColumn.setCellValueFactory(cellData -> cellData.getValue().studentProperty());		
		studentColumn.setCellFactory(new Callback<TableColumn<Evidencija,Boolean>, TableCell<Evidencija,Boolean>>() {
			
			@Override
			public TableCell<Evidencija, Boolean> call(TableColumn<Evidencija, Boolean> param) {
				final CheckBoxTableCell<Evidencija, Boolean> ctCell = new CheckBoxTableCell<>();
				ctCell.setSelectedStateCallback(new Callback<Integer, ObservableValue<Boolean>>() {
				    @Override
				    public ObservableValue<Boolean> call(Integer index) {
				        return evidencijeTable.getItems().get(index).studentProperty();
				    }
				});
				return ctCell;
			}
		});
		
		// je li platio
		platioColumn.setCellFactory(CheckBoxTableCell.forTableColumn(platioColumn));
		platioColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Evidencija, Boolean>, ObservableValue<Boolean>>() {
					@Override
					public ObservableValue<Boolean> call(CellDataFeatures<Evidencija, Boolean> param) {
						return param.getValue().platioProperty();
					}
				});
		
		// bilješka
		biljeskaColumn.setCellFactory(TextFieldTableCell.<Evidencija> forTableColumn());
		biljeskaColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Evidencija,String>>() {			
			@Override
			public void handle(CellEditEvent<Evidencija, String> event) {
				Evidencija e = event.getRowValue();
				e.setBiljeska(event.getNewValue());
				int i = evidencije.indexOf(e);
				evidencije.set(i, e);
			}
		});
		
		/* selekcija osobe u tabeli itd */	
		evidencijeTable.setEditable(true);
		evidencijeTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Evidencija>() {
			@Override
			public void changed(ObservableValue<? extends Evidencija> observable, Evidencija oldValue, Evidencija newValue) {
				showOsobaDetails(newValue);
			}
		});
		
		// kad se klikne choiceBox za godinu
		choiceGodina.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Godina>() {

			@Override
			public void changed(ObservableValue<? extends Godina> observable, Godina oldValue, Godina newValue) {
				trenutnaGodina = newValue;
				if (newValue != null) {
					daoEvidencije.setTrenutnaGodina(trenutnaGodina.getGodina());
					daoEvidencije.fillList();
				}				
			}
		});
		
		showOsobaDetails(null);	// oèisti detalje o osobi
	}
	
	/**
	 * Poziva se pri samom poèetku aplikacije, inicijalizacija
	 */
	public void setupListaEvidencija(Main main) {		
		this.main = main;		
		choiceGodina.setItems(daoGodine.listGodina());	// magija zvana data-binding
		choiceGodina.getSelectionModel().select(0);		
		evidencijeTable.setItems(daoEvidencije.listEvidencija()); // dadni tabeli podatke		
	}
	
	
	
	@FXML
	public void deleteEvidencija() {
		int selectedIndex = evidencijeTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex < 0 || evidencijeTable.getItems().isEmpty())
			return;
		Evidencija e = evidencijeTable.getSelectionModel().getSelectedItem();
		evidencije.remove(e);
	}

	// ispisuje detalje o osobi u labelama, nemam pojma što æe mi ovo
	private void showOsobaDetails(Evidencija e) {		
		Osoba osoba = (e == null? null :  e.getOsoba());		
		if (osoba != null) {
			// Fill the labels with info from the person object.
			imeLabel.setText(osoba.getIme());
			prezimeLabel.setText(osoba.getPrezime());
			datumRodjenjaLabel.setText(DateUtils.format(osoba.getDatumRodjenja()));			
		} else {
			imeLabel.setText("");
			prezimeLabel.setText("");
			datumRodjenjaLabel.setText("");
		}
	}
	
	@FXML
	public void dodajOsobeUEvidenciju() {		
		OdaberiOsobeController ctrlOdaberiOsobe = main.showOdaberiOsobe();
		
		List<Osoba> osobeNaEvidenciji = new ArrayList<>();
		for (Evidencija e : daoEvidencije.listEvidencija()) {
			osobeNaEvidenciji.add(e.getOsoba());
		}
		
		
		List<Osoba> newOsobe = new ArrayList<>();
		for (Osoba osoba : daoOsobe.listOsoba()) {
			if(!osobeNaEvidenciji.contains(osoba)) {
				newOsobe.add(osoba);
			}
		}
		ctrlOdaberiOsobe.setListaOsoba(newOsobe);		
	}
	
	// setuje se iz dijaloga za odabiranje osoba
	public void setZaDodat(List<Osoba> zaDodat) {		
		for (Osoba osoba : zaDodat) {
			Evidencija newEvidencija = new Evidencija(trenutnaGodina, osoba, false, false);
			evidencije.add(newEvidencija);
		}		
	}

}
