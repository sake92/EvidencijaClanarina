package ba.sake.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import ba.sake.dao.model.Evidencija;
import ba.sake.dao.model.Osoba;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

@SuppressWarnings("unchecked")
public class EvidencijaDAO {

	private static EvidencijaDAO evidencijaDAO = new EvidencijaDAO();

	private ObservableList<Evidencija> evidencije = null;
	
	private int trenutnaGodina = 0;

	private EvidencijaDAO() {
		evidencije = FXCollections.observableArrayList();
		fillList();
		
		evidencije.addListener((ListChangeListener.Change<? extends Evidencija> change) -> {
			// this code AUTOMATICALLY reflects changes in ObservableList to database ^_^
			// special thanks to this guy(for explaining mapping JFX props to JPA):
			// http://svanimpe.be/blog/properties-jpa.html
			while (change.next()) {
				if (change.wasAdded()) {
					List<Evidencija> added = (List<Evidencija>) change.getAddedSubList();
					for (Evidencija e : added) {
						updateOrInsert(e);
					}
				}
				if (change.wasRemoved()) {
					List<Evidencija> removed = (List<Evidencija>) change.getRemoved();
					for (Evidencija e : removed) {
						remove(e.getId());
					}
				}
			}
		});
	}
		

	// "singleton" lafo
	public static EvidencijaDAO getEvidencijaDAO() {
		return evidencijaDAO;
	}

	public ObservableList<Evidencija> listEvidencija() {
		return evidencije;
	}
	
	public void setTrenutnaGodina(int trenutnaGodina) {
		this.trenutnaGodina = trenutnaGodina;
	}

	public void fillList() {
		
		List<Evidencija> listAll = new ArrayList<>();
		Session s = DBUtils.getSession();
		s.beginTransaction();
		listAll = s.createQuery("FROM Evidencija").list();
		s.getTransaction().commit();
		s.close();
		
		List<Evidencija> list = new ArrayList<>();
		for (Iterator<Evidencija> iterator = listAll.iterator(); iterator.hasNext();) {
			Evidencija evidencija = (Evidencija) iterator.next();
			if(evidencija.getGodina().getGodina() == trenutnaGodina) {
				list.add(evidencija);
			}
		}

		evidencije.clear();
		evidencije.addAll(list);
	}
	
	public void remove(Integer id) {
		Session s = DBUtils.getSession();
		s.beginTransaction();
		Evidencija e = (Evidencija) s.load(Evidencija.class, id);
		s.delete(e);
		s.getTransaction().commit();
		s.close();
	}

	public void updateOrInsert(Evidencija newEvidencija) {
		Session s = DBUtils.getSession();
		s.beginTransaction();
		s.saveOrUpdate(newEvidencija);
		s.getTransaction().commit();
		s.close();
	}
}
