package ba.sake.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import ba.sake.dao.model.Osoba;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

@SuppressWarnings("unchecked")
public class OsobaDAO {

	private static OsobaDAO osobaDAO = new OsobaDAO();

	// observable, kad se promijeni ova lista,
	// promijeniæe se i lista evidencija, vidi MAIN!
	private ObservableList<Osoba> osobe;

	private OsobaDAO() {
		osobe = FXCollections.observableArrayList();
		
		updateOrInsert(new Osoba("Sake1", "Hadžija", LocalDate.of(1992, 3, 10)));
		updateOrInsert(new Osoba("Sake2", "Hadžija", LocalDate.of(1992, 3, 10)));
		updateOrInsert(new Osoba("Sake3", "Hadžija", LocalDate.of(1992, 3, 10)));
		
		fillList();
		
		osobe.addListener((ListChangeListener.Change<? extends Osoba> change) -> {
			// this code AUTOMATICALLY reflects changes in ObservableList to database ^_^
			// special thanks to this guy(for explaining mapping JFX props to JPA):
			// http://svanimpe.be/blog/properties-jpa.html
			while (change.next()) {
				if (change.wasAdded()) {
					List<Osoba> added = (List<Osoba>) change.getAddedSubList();
					for (Osoba osoba : added) {
						updateOrInsert(osoba);
					}
				}
				if (change.wasRemoved()) {
					List<Osoba> removed = (List<Osoba>) change.getRemoved();
					for (Osoba osoba : removed) {
						remove(osoba.getId());
					}
				}
			}
		});
	}

	// "singleton" lafo
	public static OsobaDAO getOsobaDAO() {
		return osobaDAO;
	}

	// GLAVNO!
	public ObservableList<Osoba> listOsoba() {
		return osobe;
	}

	// poèetno popunjavanje liste osoba :)
	private void fillList() {
		List<Osoba> list = new ArrayList<>();
		Session s = DBUtils.getSession();
		s.beginTransaction();
		list = s.createQuery("from Osoba").list();
		s.getTransaction().commit();
		s.close();

		osobe.clear();
		osobe.addAll(list);
	}

	public List<Osoba> listOsobaNisuNaListi(Integer godina) {
		List<Osoba> list = new ArrayList<>();
		Session s = DBUtils.getSession();
		s.beginTransaction();
		
		list = s.createQuery("FROM Osoba o WHERE o.id NOT IN " + "(SELECT e.osoba FROM Evidencija e WHERE e.godina = "
				+ godina + " )").list();
		s.getTransaction().commit();
		s.close();
		return list;
	}

	private void remove(Integer id) {
		Session s = DBUtils.getSession();
		s.beginTransaction();
		Osoba c = (Osoba) s.load(Osoba.class, id);
		s.delete(c);
		s.getTransaction().commit();
		s.close();
	}
	
	private void updateOrInsert(Osoba osoba) {
		Session s = DBUtils.getSession();
		s.beginTransaction();
		s.saveOrUpdate(osoba);
		s.getTransaction().commit();
		s.close();
	}
}
