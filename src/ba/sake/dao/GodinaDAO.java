package ba.sake.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import ba.sake.dao.model.Godina;
import ba.sake.dao.model.Osoba;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/** 
 * DAO za Godina objekte.
 * @since 0.0.0
 * @author Sake
 */
@SuppressWarnings("unchecked")
public class GodinaDAO {

	private static GodinaDAO godinaDAO = new GodinaDAO();

	// observable, kad se promijeni ova lista,
	// promijeniæe se i lista evidencija, vidi MAIN!
	private ObservableList<Godina> godine = null;

	private GodinaDAO() {		
		godine = FXCollections.observableArrayList();		
		updateOrInsert(new Godina(2015, 29.9));		
		fillList();
		
		godine.addListener((ListChangeListener.Change<? extends Godina> change) -> {
			// this code AUTOMATICALLY reflects changes in ObservableList to database ^_^
			// special thanks to this guy(for explaining mapping JFX props to JPA):
			// http://svanimpe.be/blog/properties-jpa.html
			while (change.next()) {				
				if(change.wasAdded()) {
					List<Godina> added = (List<Godina>) change.getAddedSubList();
					for (Godina godina : added) {
						updateOrInsert(godina);
					}
				} else if(change.wasRemoved()) {
					List<Godina> removed = (List<Godina>) change.getRemoved();
					for (Godina godina : removed) {
						remove(godina.getId());
					}
				}
			}
		});
	}

	// "singleton", no multiple thread access..
	public static GodinaDAO getGodinaDAO() {
		return godinaDAO;
	}
	
	public ObservableList<Godina> listGodina() {
		return godine;
	}

	private void fillList() {
		List<Godina> list = new ArrayList<>();
		Session s = DBUtils.getSession();
		s.beginTransaction();
		list = s.createQuery("from Godina").list();
		s.getTransaction().commit();
		s.close();

		godine.clear();
		godine.addAll(list);
	}

	private void remove(Integer id) {
		Session s = DBUtils.getSession();
		s.beginTransaction();
		Godina c = (Godina) s.load(Godina.class, id);
		s.delete(c);
		s.getTransaction().commit();
		s.close();
	}

	private void updateOrInsert(Godina newGodina) {
		Session s = DBUtils.getSession();
		s.beginTransaction();
		s.saveOrUpdate(newGodina);
		s.getTransaction().commit();
		s.close();
	}
}
