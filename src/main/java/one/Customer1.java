package one;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "CUSTOMER")
public class Customer1 {
	public Customer1() {
	};

	@Id
	/////////

	@Column(name = "txn_id", unique = true, nullable = false)
	@GeneratedValue(generator = "mygen")
	@GenericGenerator(name = "mygen", strategy = "foreign", parameters = {
			@Parameter(name = "property", value = "txnclass") })
	private long id;

	@OneToOne
	// @PrimaryKeyJoinColumn
	@JoinColumn(name = "txn_id")
	private Transaction1 txnclass;

	//////

	@Column(name = "cust_name")
	private String name;

	@Column(name = "cust_email")
	private String email;

	@Column(name = "cust_address")
	private String address;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Transaction1 getTxn() {
		return txnclass;
	}

	public void setTxn(Transaction1 txn) {
		this.txnclass = txn;
	}

}
