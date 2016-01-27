package ba.sake.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ba.sake.Main;
import ba.sake.dao.OsobaDAO;
import ba.sake.dao.model.Evidencija;
import ba.sake.dao.model.Godina;
import ba.sake.dao.model.Osoba;
import ba.sake.utils.DateUtils;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class ListaEvidencijaController {
	
	private Main main;

	// DAO
	//private OsobaDAO dao = OsobaDAO.getOsobaDAO();
	private ObservableList<Evidencija> evidencije = FXCollections.observableArrayList();
	private List<Evidencija> sveEvidencije = new ArrayList<>();
	
	@FXML
	private ChoiceBox<Godina> choiceGodina;
	private List<Godina> godine;
	private int trenutnaGodina = 0;
	
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
		
		// ime
		imeColumn.setEditable(true);
		imeColumn.setCellValueFactory(	// vrijednost za prikazivanje, mijenjanje
				new Callback<TableColumn.CellDataFeatures<Evidencija, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Evidencija, String> param) {
						return new SimpleStringProperty(param.getValue().getOsoba().getIme());
					}
				});
		imeColumn.setCellFactory(TextFieldTableCell.<Evidencija> forTableColumn());	// editable textBox, da se može mijenjat
		imeColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Evidencija, String>>() {
			@Override
			public void handle(CellEditEvent<Evidencija, String> t) {
				Evidencija e = ((Evidencija) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				e.getOsoba().setIme(t.getNewValue());
			}
		});
		
		// prezime
		prezimeColumn.setEditable(true);		
		prezimeColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Evidencija, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Evidencija, String> param) {
						return new SimpleStringProperty(param.getValue().getOsoba().getPrezime());
					}
				});
		prezimeColumn.setCellFactory(TextFieldTableCell.<Evidencija> forTableColumn());
		prezimeColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Evidencija, String>>() {
			@Override
			public void handle(CellEditEvent<Evidencija, String> t) {
				Evidencija e = ((Evidencija) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				e.getOsoba().setIme(t.getNewValue());
			}
		});
		
		// je li student
		studentColumn.setEditable(true);
		studentColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Evidencija,Boolean>, ObservableValue<Boolean>>() {			
			@Override
			public ObservableValue<Boolean> call(CellDataFeatures<Evidencija, Boolean> param) {
				return new SimpleBooleanProperty(param.getValue().isStudent());
			}
		});
		studentColumn.setCellFactory(CheckBoxTableCell.forTableColumn(platioColumn));
		
		// je li platio
		platioColumn.setEditable(true);
		platioColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Evidencija, Boolean>, ObservableValue<Boolean>>() {

					@Override
					public ObservableValue<Boolean> call(CellDataFeatures<Evidencija, Boolean> param) {
						return new SimpleBooleanProperty(param.getValue().isPlatio());
					}
				});
		platioColumn.setCellFactory(CheckBoxTableCell.forTableColumn(platioColumn));
		
		
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
				trenutnaGodina = newValue.getGodina();
				if (newValue != null) {
					evidencijeTable.setItems(getOsobaList());
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
		// osobaList.setAll(dao.listOsoba()); // popuni observable list podacima iz baze

		/*
		// osobe
		Osoba o1 = new Osoba(1, "ime1", "prezime1", LocalDate.of(1991, 1, 1));
		Osoba o2 = new Osoba(2, "ime2", "prezime2", LocalDate.of(1992, 1, 1));
		Osoba o3 = new Osoba(3, "ime3", "prezime3", LocalDate.of(1993, 1, 1));
		
		// godine
		Godina g1 = new Godina(1, 2015, 10);
		Godina g2 = new Godina(2, 2016, 15);
		
		Evidencija e1 = new Evidencija(1, g1, o1, false, false);
		Evidencija e2 = new Evidencija(2, g1, o2, false, true);
		Evidencija e3 = new Evidencija(3, g1, o3, false, false);
		Evidencija e4 = new Evidencija(4, g2, o1, true, false);
		Evidencija e5 = new Evidencija(5, g2, o2, false, false);
		Evidencija e6 = new Evidencija(6, g2, o3, false, true);
		
		
		sveEvidencije.add(e1);
		sveEvidencije.add(e2);
		sveEvidencije.add(e3);
		sveEvidencije.add(e4);
		sveEvidencije.add(e5);
		sveEvidencije.add(e6);
		
		godine = new ArrayList<>();
		godine.add(g1);
		godine.add(g2);
		
		godine.sort(new Comparator<Godina>() {
			@Override
			public int compare(Godina o1, Godina o2) {
				return o2.getGodina() - o1.getGodina();
			}
		});
		*/
		godine = new ArrayList<>();
		choiceGodina.setItems(FXCollections.observableArrayList(godine));
		choiceGodina.getSelectionModel().select(0);

		evidencijeTable.setItems(evidencije); // dadni tabeli podatke
	}
	
	@FXML
	public void dodajEvidencija() {
		
		
		
		// TODO dao
		// dao.addOsoba(osoba);
	}

	public ObservableList<Evidencija> getOsobaList() {
		if (!evidencije.isEmpty())
			evidencije.clear();
		
		for (Evidencija evidencija : sveEvidencije) {
			if(evidencija.getGodina().getGodina() == trenutnaGodina) {
				evidencije.add(evidencija);
			}
		}
		// osobaList = FXCollections.observableList((List<Osoba>)
		// dao.listOsoba());
		return evidencije;
	}
	
	@FXML
	public void deleteEvidencija() {
		int selectedIndex = evidencijeTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex < 0 || evidencijeTable.getItems().isEmpty())
			return;

		evidencijeTable.getItems().remove(selectedIndex);
		// TODO baza
	}

	// ispisuje detalje o osobi u labelama, nemam pojma što æe mi ovo
	private void showOsobaDetails(Evidencija e) {
		
		Osoba osoba = (e == null? null :  e.getOsoba());
		
		if (osoba != null) {
			// Fill the labels with info from the person object.
			imeLabel.setText(osoba.getIme());
			prezimeLabel.setText(osoba.getPrezime());
			datumRodjenjaLabel.setText(DateUtils.format(osoba.getDatumRodjenja()));
			

			// TODO: We need a way to convert the birthday into a String!
			// birthdayLabel.setText(...);
		} else {
			imeLabel.setText("");
			prezimeLabel.setText("");
			datumRodjenjaLabel.setText("");
		}
	}

}
