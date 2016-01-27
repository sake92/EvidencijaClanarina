package ba.sake.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import ba.sake.dao.model.Osoba;

@SuppressWarnings("unchecked")
public class OsobaDAO {

	private static OsobaDAO osobaDAO = new OsobaDAO();

	private OsobaDAO() {
	}

	// "singleton" lafo
	public static OsobaDAO getOsobaDAO() {
		return osobaDAO;
	}

	public void addOsoba(Osoba osoba) {
		Session s = DBUtils.getSession();
		s.beginTransaction();
		s.save(osoba);
		s.getTransaction().commit();
		s.close();
	}

	public List<Osoba> listOsoba() {
		List<Osoba> list = new ArrayList<>();
		Session s = DBUtils.getSession();
		s.beginTransaction();
		list = s.createQuery("from Osoba").list();
		s.getTransaction().commit();
		s.close();
		return list;
	}

	public void removeOsoba(Integer id) {
		Session s = DBUtils.getSession();
		s.beginTransaction();
		Osoba c = (Osoba) s.load(Osoba.class, id);
		s.delete(c);
		s.getTransaction().commit();
		s.close();
	}

	public void updateOsoba(Osoba osoba) {
		Session s = DBUtils.getSession();
		s.beginTransaction();
		s.update(osoba);
		s.getTransaction().commit();
		s.close();
	}
}
