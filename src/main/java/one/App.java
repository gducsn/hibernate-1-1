package one;

import java.util.Date;

public class App {

	public static Transaction1 buildData() {

		Transaction1 txn = new Transaction1();
		txn.setDate(new Date());
		txn.setTotal(1);

		Customer1 cust = new Customer1();
		cust.setAddress("address_value");
		cust.setEmail("email_value");
		cust.setName("name_value_");
		cust.setTxn(txn);
		txn.setCustomer(cust);

		return txn;

	};

	public static void main(String[] args) {

		Transaction1 txn = buildData();
		DAO.saveData(txn);

	}

}
