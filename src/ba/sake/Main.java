package ba.sake;

import java.io.IOException;

import ba.sake.dao.DBUtils;
import ba.sake.view.GlavniPanelController;
import ba.sake.view.ListaEvidencijaController;
import ba.sake.view.ListaOsobaController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	private BorderPane listaEvidencija;
	private BorderPane listaOsoba;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("AddressApp");

		initRootLayout();
		initListaEvidencija();
		initListaOsoba();

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
	 * Initializes the root layout.
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

	/**
	 * Prikazuje listu evidencija za godine...
	 */
	private void initListaEvidencija() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/ListaEvidencija.fxml"));
			listaEvidencija = (BorderPane) loader.load();

			// Give the controller access to the main app.
			ListaEvidencijaController controller = loader.getController();
			controller.setupListaEvidencija(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initListaOsoba() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/ListaOsoba.fxml"));
			listaOsoba = (BorderPane) loader.load();

			ListaOsobaController controller = loader.getController();
			controller.setupListaOsoba(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* JAVNE METODE */
	public void showListaEvidencija() {
		rootLayout.setCenter(listaEvidencija);
	}

	public void showListaOsoba() {
		rootLayout.setCenter(listaOsoba);
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}

/*
 * @SuppressWarnings({ "unchecked", "rawtypes" }) public static void
 * main(String[] args) {
 * 
 * Session session = DBUtils.getSession();
 * 
 * // spremanje u bazu session.beginTransaction(); Osoba user1 = new
 * Osoba("Sake1", "Hadzija"); Osoba user2 = new Osoba("Sake2", "Pake"); Osoba
 * user3 = new Osoba("Sake3", "Nake"); session.save(user1); session.save(user2);
 * session.save(user3); session.getTransaction().commit();
 * 
 * // dobijanje podataka iz baze session = DBUtils.getSession();
 * session.beginTransaction();
 * 
 * List result = session.createQuery("from Osoba").list();
 * System.out.println("OSOBE:"); for (Osoba event : (List<Osoba>) result) {
 * System.out.println(event.getIme()); }
 * 
 * Criteria criteria = session.createCriteria(Osoba.class);
 * criteria.add(Restrictions.eq("ime", "Sake2")); result = criteria.list();
 * System.out.println("OSOBA:"); for (Osoba o : (List<Osoba>) result) {
 * System.out.println(o.getIme()); }
 * 
 * criteria = session.createCriteria(Osoba.class);
 * criteria.addOrder(Order.desc("ime")); result = criteria.list();
 * System.out.println("OSOBA:"); for (Osoba o : (List<Osoba>) result) {
 * System.out.println(o.getIme()); }
 * 
 * // konaèno zatvaranje konekcije, servera, aplikacije
 * session.getTransaction().commit(); session.close();
 * 
 * System.exit(0); }
 */
