package one;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DAO {

	static void saveData(Transaction1 obj) {

		Transaction tx = null;
		try (Session session = HibernateUtils.getSessionFactory().openSession()) {

			System.out.println("Session created using annotations configuration");
			// start transaction
			tx = session.beginTransaction();
			// Save the Model object
			session.save(obj);
			// Commit transaction
			tx.commit();
			System.out.println("Annotation Example. Transaction ID=" + obj.getId());

			// Get Saved Trasaction Data
			printTransactionData(obj.getId());
		} catch (HibernateException e) {
			System.out.println("Exception occured. " + e.getMessage());
			e.printStackTrace();
		}
	};

	private static void printTransactionData(long id) {
		Transaction tx = null;
		try (Session session = HibernateUtils.getSessionFactory().openSession()) {
			// Get Session
			// start transaction
			tx = session.beginTransaction();

			// Save the Model object
			Transaction1 txn = (Transaction1) session.get(Transaction1.class, id);
			// Commit transaction
			tx.commit();
			System.out.println("Annotation Example. Transaction Details=\n" + txn);

		} catch (Exception e) {
			System.out.println("Exception occured. " + e.getMessage());
			e.printStackTrace();
		}
	}

}
