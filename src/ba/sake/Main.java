package ba.sake;

import java.io.IOException;

import ba.sake.dao.DBUtils;
import ba.sake.view.GlavniPanelController;
import ba.sake.view.ListaEvidencijaController;
import ba.sake.view.ListaGodinaController;
import ba.sake.view.ListaOsobaController;
import ba.sake.view.OdaberiOsobeController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
	/*
	 * TODO
	 * 
	 * button delete na evidencijama
	 * 
	 * checkiranje evidencija - update
	 * 
	 * izvještaji...
	 * 
	 * about
	 */

	private Stage primaryStage;
	private BorderPane rootLayout;

	private BorderPane listaOsoba;
	private BorderPane listaGodina;
	
	private BorderPane listaEvidencija;
	ListaEvidencijaController ctrlListaEvidencija;	// moram nekako refreshovat.... :/
	
	private BorderPane odaberiOsobe;		// ovo sam trebao uraditi kao modalni dijalog al de šta æeš..
	private OdaberiOsobeController ctrlOdaberiOsobe;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Evidencije");

		initRootLayout();
		initViews();

		// prvo se prikazuje lista evidencija po godinama
		showListaEvidencija();

		this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				DBUtils.closeSessionFactory();
			}
		});

	}
	
	/**
	 * inicijalizacija glavnog panela
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/GlavniPanel.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Give the controller access to the main app.
			GlavniPanelController controller = loader.getController();
			controller.setupGlavniPanel(this);

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setMinWidth(600);
			primaryStage.setMinHeight(450);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initViews() {
		try {
			
			FXMLLoader loaderListeOsoba = new FXMLLoader();
			loaderListeOsoba.setLocation(Main.class.getResource("view/ListaOsoba.fxml"));
			listaOsoba = (BorderPane) loaderListeOsoba.load();
			ListaOsobaController ctrlListaOsoba = loaderListeOsoba.getController();
			ctrlListaOsoba.setupListaOsoba(this);
			
			FXMLLoader loaderListeGodina = new FXMLLoader();
			loaderListeGodina.setLocation(Main.class.getResource("view/ListaGodina.fxml"));
			listaGodina = (BorderPane) loaderListeGodina.load();
			ListaGodinaController ctrlListaGodina = loaderListeGodina.getController();
			ctrlListaGodina.setupListaGodina(this);
			
			FXMLLoader loaderListeEvidencija = new FXMLLoader();
			loaderListeEvidencija.setLocation(Main.class.getResource("view/ListaEvidencija.fxml"));
			listaEvidencija = (BorderPane) loaderListeEvidencija.load();
			ctrlListaEvidencija = loaderListeEvidencija.getController();
			ctrlListaEvidencija.setupListaEvidencija(this);
			
			FXMLLoader loaderOdaberiOsobe = new FXMLLoader();
			loaderOdaberiOsobe.setLocation(Main.class.getResource("view/OdaberiOsobe.fxml"));
			odaberiOsobe = (BorderPane) loaderOdaberiOsobe.load();
			ctrlOdaberiOsobe = loaderOdaberiOsobe.getController();
			ctrlOdaberiOsobe.setupOdaberiOsobe(this, ctrlListaEvidencija);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* JAVNE METODE */
	public void showListaOsoba() {
		rootLayout.setCenter(listaOsoba);
	}

	public void showListaGodina() {
		rootLayout.setCenter(listaGodina);
	}
	
	public void showListaEvidencija() {
		rootLayout.setCenter(listaEvidencija);
	}
	
	public OdaberiOsobeController showOdaberiOsobe() {
		rootLayout.setCenter(odaberiOsobe);		
		return this.ctrlOdaberiOsobe;
	}

	/* NE DIRAJ */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}