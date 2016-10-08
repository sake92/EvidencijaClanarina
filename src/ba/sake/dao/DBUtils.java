package ba.sake.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DBUtils {

	public static SessionFactory sessionFactory; // hibernate
	private static StandardServiceRegistry registry;

	static {
		registry = new StandardServiceRegistryBuilder().configure().build();
		sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	}

	// ne smije se instancirati izvana...
	private DBUtils() {
	}

	public static Session getSession() {
		return sessionFactory.openSession();
	}

	public static void closeSessionFactory() {
		StandardServiceRegistryBuilder.destroy(sessionFactory.getSessionFactoryOptions().getServiceRegistry());
	}

}
