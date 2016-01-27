package ba.sake.dao;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DBUtils {

	private static String SERVER_PORT = "9092"; // H2

	public static SessionFactory sessionFactory; // hibernate
	private static StandardServiceRegistry registry;

	static {
		registry = new StandardServiceRegistryBuilder().configure().build();
		try {
			// startovanje H2 servera baze u TCP modu
			Server.createTcpServer(new String[] { "-tcpPort", SERVER_PORT, "-tcpAllowOthers" }).start();
			// HIBERNATE konfiguracija
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (SQLException e) {
			StandardServiceRegistryBuilder.destroy(registry);
			e.printStackTrace();

			// TODO napravit da izbaci popup!!! pa da zatvori app
			System.exit(0);
		}
	}

	// ne smije se instancirati...
	private DBUtils() {
	}

	public static Session getSession() {
		return sessionFactory.openSession();
	}

	public static void closeSessionFactory() {
		StandardServiceRegistryBuilder.destroy(sessionFactory.getSessionFactoryOptions().getServiceRegistry());
		try {
			Server.shutdownTcpServer("tcp://localhost:" + SERVER_PORT, "", true, true);
		} catch (SQLException e) {
			System.out.println("NE MEREM SERVER ZASTAVIT");
			e.printStackTrace();
		}
	}

}
