package tree.util;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import tree.model.Node;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = getSessionFactory();

    public static SessionFactory getSessionFactory() {
	if (sessionFactory == null) {
	    try {
		StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

		Map<String, Object> settings = new HashMap<>();
		settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
		settings.put(Environment.URL,
			"jdbc:mysql://localhost:3306/treasy?createDatabaseIfNotExist=true&useTimezone=true&serverTimezone=UTC");
		settings.put(Environment.USER, "root");
		settings.put(Environment.PASS, "root");
		settings.put(Environment.HBM2DDL_AUTO, "update");
		settings.put(Environment.SHOW_SQL, true);

		// c3p0 configuration
		settings.put(Environment.C3P0_MIN_SIZE, 5); // Minimum size of pool
		settings.put(Environment.C3P0_MAX_SIZE, 20); // Maximum size of pool
		settings.put(Environment.C3P0_ACQUIRE_INCREMENT, 1);// Number of connections acquired at a time when
								    // pool is exhausted
		settings.put(Environment.C3P0_TIMEOUT, 1800); // Connection idle time
		settings.put(Environment.C3P0_MAX_STATEMENTS, 150); // PreparedStatement cache size

		registryBuilder.applySettings(settings);

		StandardServiceRegistry registry = registryBuilder.build();

		MetadataSources sources = new MetadataSources(registry).addAnnotatedClass(Node.class);
		return sources.getMetadataBuilder().build().getSessionFactoryBuilder().build();
	    } catch (Throwable ex) {
		System.err.println("SessionFactory creation failed." + ex);
		throw new ExceptionInInitializerError(ex);
	    }
	}

	return sessionFactory;
    }
}