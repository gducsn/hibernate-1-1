# Hibernate - one to one

Hibernate ci permette di gestire tabelle nel database usando classi Java e 
i loro attributi.

La classe diventa un’entità, cioè una tabella nel database. Ogni istanza 
di questa classe diventa una riga del database. Hibernate è un framework 
che ci aiuta a persistere dati. Persistere un dato significa ‘continuare 
ad avere un effetto quando la causa è rimossa’. Questo significa che 
quando la nostra applicazione smette di funzionare i dati continueranno ad 
esistere.

Uno degli aspetti più importanti riguarda il modo in cui possiamo far 
interagire varie entità, quindi varie tabelle. In questo primo esempio 
utilizzeremo il tipo di relazione ‘one to one’. Questa relazione riguarda 
due tabelle, una principale e una secondaria. La principale è detta tale 
se gestisce la chiave primaria.
Quando tra due entità solo una ha un campo di relazione nell’altra la 
direzione della relazione viene detta unidirezionale. Quando entrambe le 
entità hanno un campo o attributo che fanno riferimento ad un’altra 
[entità](https://docs.oracle.com/cd/E19798-01/821-1841/bnbqi/index.html) 
viene detto bidirezionale.

Nel nostro esempio abbiamo due tabelle che si collegano tra loro secondo 
una relazione one-to-one in modo bidirezionale.

L’esempio di per se è semplice, quello che risulta più ostico è capire le 
notazioni JPA.

Capire meglio come funziona un database 
[qui](https://opentextbc.ca/dbdesign01/chapter/chapter-8-entity-relationship-model/).

---

Struttura:

- pom.xml
- hibernate.cfg.xml
- Transaction1.java
- Customer1.java
- HibernateUtils.java
- DAO.java
- App.java

Il file pom ci serve per le nostre dipendenze, il secondo per configurare 
la connessione al db, la classe Transaction è la prima entità, la 
principale. Customer è la seconda entità. Hibernate utils gestisce la 
connessione vera e propria. La classe DAO contiene i metodi per persistere 
i dati nel db ed infine la classe app per avviare il tutto.

---

pom.xml

```java
<project xmlns="http://maven.apache.org/POM/4.0.0" 
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.exe</groupId>
	<artifactId>Student_01</artifactId>
	<version>0.0.1-SNAPSHOT</version>

	<properties>
		<maven.compiler.source>1.8</maven.compiler.source>
		<maven.compiler.target>1.8</maven.compiler.target>
	</properties>

	<dependencies>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>8.0.28</version>
		</dependency>

		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-core</artifactId>
			<version>5.6.5.Final</version>
		</dependency>

	</dependencies>
</project>
```

Il file pom.xml descrive il nostro progetto, le sue dipendenze e tutte le 
configurazioni più utili per ogni evenienza.

La parte iniziale del pom definisce alcuni degli aspetti principali del 
nostro progetto. Ad esempio il ‘gropupID’ identica in modo univoco il 
nostro progetto tra tutti i progetti. Un ID gruppo dovrebbe seguire le 
regole del nome come per i pacchetti di Java. Questo significa che 
dovrebbe seguire il nome di dominio invertito (com.example).

Abbiamo anche ‘artifactId’ che definisce il nome del nostro file finale e 
‘version’ in cui definiamo la versione del nostro progetto.

Inoltre abbiamo la possibilità di decidere quale versione del compilatore 
usare nel nostro progetto, in questo caso la versione 1.8.

Il file pom.xml contiene principalmente la definizione delle nostre 
dipendenze, cioè tutte quelle librerie esterne che ci permettono di far 
funzionare il nostro progetto.

In questo caso sono installate due: mysql e hibernate.

In entrambe sono definiti alcuni aspetti, come il groupID, artifact e la 
versione. Ogni aspetto è importate.

---

hibernate.cfg.xml

```java
<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE hibernate-configuration PUBLIC
    "-//Hibernate/Hibernate Configuration DTD//EN"
    "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">

<hibernate-configuration>

	<session-factory>
		<property 
name="dialect">org.hibernate.dialect.MySQL5Dialect</property>
		<property 
name="connection.driver_class">com.mysql.cj.jdbc.Driver</property>
		<property 
name="connection.url">jdbc:mysql://localhost:3306/hibernate_crud</property>
		<property name="connection.username">root</property>
		<property name="connection.password">root</property>
		<property name="show_sql">true</property>
		<property name="format_sql">true</property>
		<property name="hbm2ddl.auto">create-drop</property>
		<mapping class="hibernateCrud.Student"></mapping>

	</session-factory>

</hibernate-configuration>
```

Il file hibernate.cfg.xml ci permette di configurare ogni parametro per la 
corretta connessione al database.

Per poterci connettere con hibernate al nostro database c’è bisogno prima 
di tutto di definire il dialect, il quale specifica che tipo di database 
stiamo usando e che tipo di query dovrà fare hibernate.

Successivamente c’è bisogno di selezione il giusto driver il quale cambia 
in base alla versione di mysql in uso.

Fatto questo dobbiamo definire l’url del database, username e password.

Possiamo anche decidere se far comparire in consolle la query fatta da 
hibernate al database con la proprietà 
‘[show_sql](https://mkyong.com/hibernate/hibernate-display-generated-sql-to-console-show_sql-format_sql-and-use_sql_comments/)’ 
attiva. E’ possibile anche formattare la query con una seconda proprietà 
‘format_sql’ attiva.

Per questa applicazione abbiamo dichiarato, tramite la proprietà 
“hbm2ddl.auto”, in che modo viene gestito lo schema durante il ciclo di 
vita del SessionFactory.

Sono varie le proprietà:

- create: crea lo schema, distruggendo i dati precedenti.
- validate: valida e non cambia nulla nel DB.
- update: aggiorna lo schema nel DB.
- create-drop: svuota lo schema quando il SessionFactory è chiuso 
esplicitamente, di solito quando l’applicazione viene chiusa.

Nel nostro caso abbiamo utilizzando il valore ‘create-drop’ così quando la 
sessione viene esplicitamente chiusa lo schema viene svuotato.

Infine dobbiamo mappare il nostro database collegandolo alla classe 
specifica per questo compito.

---

Transaction1.java

```java
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TRANSACTION")
public class Transaction1 {
	public Transaction1() {
	};

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "txn_id")
	private long id;

	@Column(name = "txn_date")
	private Date date;

	@Column(name = "txn_total")
	private double total;

	////////////////////
	@OneToOne(mappedBy = "txnclass", cascade = CascadeType.ALL)
	private Customer1 customer;

	////////////////////

}

// getter/setter omitted
```

```java
@Entity
@Table(name = "TRANSACTION")
public class Transaction1 {
	public Transaction1() {
	};
```

Dobbiamo rendere la nostra classe un’entità, lo facciamo utilizzando 
l’apposita annotazione ‘@Entity’. 

Adesso colleghiamo l’entità al nostro table nel database usando la 
annotazione apposita.

Con solo due annotazione abbiamo reso la nostra classe un’entità e 
collegata al giusto table nel database.

```java
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "txn_id")
	private long id;

	@Column(name = "txn_date")
	private Date date;

	@Column(name = "txn_total")
	private double total;
```

In questa sezione della classe dobbiamo collegare le proprietà alle 
colonne del table. La prima cosa da è utilizzare le giuste annotazioni 
sopra ogni proprietà della classe.

ID sarà la nostra primary key. Utilizziamo le annotazioni per dichiararla 
tale. Inoltre nell’annotazione ‘@GeneratedValue’ decidiamo che tipo di 
strategia utilizzare. ‘IDENTITY’ fa in modo che il nostro ID abbia un 
incremento automatico ad ogni riga.

Infine le annotazioni ‘@Column’ collegano una proprietà alla colonna del 
database.

```java
	
	@OneToOne(mappedBy = "txnclass", cascade = CascadeType.ALL)
	private Customer1 customer;

```

La annotazione ‘@OneToOne’ specifica che tipo di relazione deve esserci 
tra le entità. Questa annotazione ha anche degli attributi. 
Quando abbiamo un tipo di direzione bidirezionale dobbiamo definire quale 
sia l’entità principale, quella che gestisce la chiave principale. Usando 
l’attributo ‘mappedby’ specifichiamo quale entità è la principale.

Il nome che scriviamo fa riferimento ad un parametro nell’entità figlia 
che vedremo dopo.

Infine dobbiamo collegare queste annotazioni ad una reference dell’entità.

Dopo aver definito tutte le annotazioni dobbiamo generare i getter e 
setter per tutte le proprietà presenti, anche per la reference istanziata.

---

Costumer1.java

```java
@Entity
@Table(name = "CUSTOMER")
public class Customer1 {
	public Customer1() {
	};

	@Id
	@Column(name = "txn_id", unique = true, nullable = false)
	@GeneratedValue(generator = "mygen")
	@GenericGenerator(name = "mygen", strategy = "foreign", parameters 
= {
			@Parameter(name = "property", value = "txnclass") 
})
	private long id;

	@OneToOne
	@JoinColumn(name = "txn_id")
	private Transaction1 txnclass;

	//////

	@Column(name = "cust_name")
	private String name;

	@Column(name = "cust_email")
	private String email;

	@Column(name = "cust_address")
	private String address;
}

// getter/setter omitted
```

Questa è l’entità figlia, cioè quella tabella che dipende in qualche modo 
da un’altra. Dipende dal fatto che la sua primary key è definita 
dall’altra entità.

In questo caso la sua colonna ID è di tipo ‘Foreign Key’.

<< A *foreign key (FK)* is an attribute in a table that references the 
primary key in another table OR it can be null. Both foreign and primary 
keys must be of the same data type. >>

**Una chiave esterna è un attributo in un table che si riferisce alla 
chiave primaria in un altro table**.

Come faccio, utilizzando JPA, a mappare l’entità?

```java
	@Id
	@Column(name = "txn_id", unique = true, nullable = false)
	@GeneratedValue(generator = "mygen")
	@GenericGenerator(name = "mygen", strategy = "foreign", parameters 
= {
			@Parameter(name = "property", value = "txnclass") 
})
	private long id;
```

Come per l’altra entità utilizziamo l’annotazione ‘@ID’.

In questo caso voglio che questa colonna, essendo generata da noi, abbia 
determinati attributi. Voglio che il suo nome sia uguale a quello della 
primary key dell’entità principale, che sia univoca e che non sia null.

Adesso dobbiamo definire in che modo si deve comportare questo ID per ogni 
istanza dell’entità.

Creiamo un nostro ‘@GenericGenerator’ e definiamo alcuni attributi. Gli 
diamo un nome da poter utilizzare. Definiamo la strategia dichiarando 
questa colonna come ‘foreign key’.

Un GenericGenerator che utilizza la strategia "foreign" si aspetta un 
parametro chiamato "proprietà" e il valore atteso è un nome di entità. Ciò 
significa che l'ID di questa entità sarà lo stesso dell'entità collegata.

```java
	@OneToOne
	@JoinColumn(name = "txn_id")
	private Transaction1 txnclass;
```

Con l’annotazione ‘@OneToOne’ abbiamo definito il tipo di relazione, 
mentre con ‘@JoinColumn(name = "txn_id")’ abbiamo dichiarato: uniamo i 
valori delle righe perché in entrambe le tabelle ci sono colonne uguali.

‘mappedby’ nell’entità principale si riferisce proprio a questa istanza.

---

HibernateUtils.java

```java
public class HibernateUtils {

	private static SessionFactory sessionFactory = 
buildSessionFactory();

	
private static SessionFactory buildSessionFactory() {

		try {
			if (sessionFactory == null) {
				StandardServiceRegistry standardRegistry = 
new StandardServiceRegistryBuilder().configure().build();

				Metadata metadata = new 
MetadataSources(standardRegistry).getMetadataBuilder().build();

				sessionFactory = 
metadata.getSessionFactoryBuilder().build();
			}
			return sessionFactory;
		} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	};

	
public static SessionFactory getSessionFactory() {
		return sessionFactory;
	};

}
```

C’è bisogno di poter utilizzare il file di configurazione per creare una 
nuova sessione.

È necessario creare uno StandardServiceRegistry, creare un oggetto 
Metadata e utilizzarlo per creare un'istanza di SessionFactory.

Utilizziamo StandardServiceRegistryBuilder per creare un'istanza di 
StandardServiceRegistry, successivamente carichiamo una configurazione da 
un file di risorse, quindi il nostro hibernate.cfg.xml e infine invochiamo 
il metodo build() per ottenere un'istanza di StandardServiceRegistry.

In questo modo l'implementazione è semplificata e ci consente di 
modificare la configurazione senza modificare il codice sorgente. 
Hibernate carica automaticamente il file di configurazione dal percorso di 
classe quando chiama il metodo configure su 
StandardServiceRegistryBuilder.

Dopo aver creato un'istanza di un ServiceRegistry configurato, è 
necessario creare una rappresentazione dei metadati del modello di 
dominio.

Per prima cosa utilizziamo ServiceRegistry creato nel passaggio precedente 
per creare un'istanza di un nuovo oggetto MetadataSources.

Una volta istanziato un nuovo oggetto possiamo utilizzarlo per istanziare 
una nuova sessione.

In breve:

Abbiamo creato un nuovo servizio al quale colleghiamo il nostro file di 
configurazione.

Una volta creato dobbiamo inserirlo nell’oggetto metadata. Questo oggetto 
ci permetterà di istanziare una nuova sessione e avere l’effettiva 
connessione.

Infine possiamo creare un nuovo metodo che ci permette di ritornare la 
sessione quando ne abbiamo bisogno:

```java
public static SessionFactory getSessionFactory() {
		return sessionFactory;
	};
```

---

DAO.java

```java
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DAO {

	static void saveData(Transaction1 obj) {

		Transaction tx = null;
		try (Session session = 
HibernateUtils.getSessionFactory().openSession()) {

			System.out.println("Session created using 
annotations configuration");
			// start transaction
			tx = session.beginTransaction();
			// Save the Model object
			session.save(obj);
			// Commit transaction
			tx.commit();
			System.out.println("Annotation Example. 
Transaction ID=" + obj.getId());

			// Get Saved Trasaction Data
			printTransactionData(obj.getId());
		} catch (HibernateException e) {
			System.out.println("Exception occured. " + 
e.getMessage());
			e.printStackTrace();
		}
	};

	private static void printTransactionData(long id) {
		Transaction tx = null;
		try (Session session = 
HibernateUtils.getSessionFactory().openSession()) {
			// Get Session
			// start transaction
			tx = session.beginTransaction();

			// Save the Model object
			Transaction1 txn = (Transaction1) 
session.get(Transaction1.class, id);
			// Commit transaction
			tx.commit();
			System.out.println("Annotation Example. 
Transaction Details=\n" + txn);

		} catch (Exception e) {
			System.out.println("Exception occured. " + 
e.getMessage());
			e.printStackTrace();
		}
	}

}
```

Analizziamo ogni metodo. Prime le cose in comune tra loro.

Ogni connessione è gestita dal blocco try/catch. In questo caso si dice 
‘try with resources’. Questo vuol dire che il blocco try gestisce una 
risorsa, la nostra connessione. Quando non abbiamo più bisogno della 
nostra connessione il blocco la chiude automaticamente.

Tutti i metodi sono gestiti in questo modo.

L’interfaccia ‘Transaction’ rappresenta un’unità di lavoro ed è associata 
alla sessione. .beginTransaction() ci permette di iniziare una nuova 
transazione e restituire il proprio oggetto.

Per iniziare un’effettiva ‘collaborazione’ tra il nostro database e le 
nostre intenzioni c’è bisogno quindi di avere un oggetto Session e uno 
Transaction.

[Session](https://docs.jboss.org/hibernate/orm/3.5/javadocs/org/hibernate/Session.html) 
è la principale interfaccia tra Java e Hibernate.

Dunque il metodo saveData non fa altro che aprire una connessione, 
persistere nel database il nostro oggetto e stampare l’oggetto 
riprendendolo tramite il suo id. Il metodo che riprende e stampa il nostro 
oggetto nel database è ‘printTransactionData’.

Per recuperarlo istanziamo un nuovo oggetto di tipo Transaction1 e con il 
metodo .get dell’interfaccia Session lo recuperiamo. Il metodo get prevede 
due argomenti, il tipo di oggetto da recuperare (il nostro Transaction1) e 
l’id associato.

---

App.java

```java
import java.util.Date;

public class App {

	public static Transaction1 buildData() {

		Transaction1 txn = new Transaction1();
		txn.setDate(new Date());
		txn.setTotal(1);

		Customer1 cust = new Customer1();
		cust.setAddress("address_value");
		cust.setEmail("email_value");
		cust.setName("name_value");
		cust.setTxn(txn);
		txn.setCustomer(cust);

		return txn;

	};

	public static void main(String[] args) {

		Transaction1 txn = buildData();
		DAO.saveData(txn);

	}

}
```

La classe App contiene il metodo main che ci permette di avviare 
l’applicazione.

```java
	public static Transaction1 buildData() {

		Transaction1 txn = new Transaction1();
		txn.setDate(new Date());
		txn.setTotal(1);

		Customer1 cust = new Customer1();
		cust.setAddress("address_value");
		cust.setEmail("email_value");
		cust.setName("name_value");
		cust.setTxn(txn);
		txn.setCustomer(cust);

		return txn;

	};
```

Il metodo buildData ritorna un oggetto di tipo Transaction1. Questo verrà 
utilizzato nel metodo save per persisterlo all’interno del database.

Istanziamo i nostri due oggetti e definiamo tutte le proprietà. 
Quello che davvero ci permette di collegare le due entità è inserire una 
reference reciprocamente.

<< JPA uses the Java concept of class reference, a class must maintain a 
reference to another one if there will be a join between them. JPA will 
not create a relationship automatically; to have the relationship in both 
sides it is needed to do like above. >>

JPA usa il concetto di Java riguardo la referenza di classe. Una classe 
deve mantenere una referenza verso un’altra se dovrà esserci un JOIN tra 
di loro.
JPA non crea questa relazione automaticamente: per averla dobbiamo fare 
questo:

```java
		cust.setTxn(txn);
		txn.setCustomer(cust);
```
