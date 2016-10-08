package ba.sake.dao.model;

import java.time.LocalDate;

import javax.persistence.*;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import ba.sake.dao.EvidencijaDAO;

@Entity
public class Evidencija {

	@Transient
	private final IntegerProperty id = new SimpleIntegerProperty();
	@Transient
	private final ObjectProperty<Godina> godina = new SimpleObjectProperty<>();
	@Transient
	private final ObjectProperty<Osoba> osoba = new SimpleObjectProperty<>();
	@Transient
	private final SimpleBooleanProperty student = new SimpleBooleanProperty();
	@Transient
	private final SimpleBooleanProperty platio = new SimpleBooleanProperty();
	@Transient
	private final StringProperty biljeska = new SimpleStringProperty();

	public Evidencija() {

	}

	public Evidencija(Godina godina, Osoba osoba, boolean student, boolean platio) {
		setGodina(godina);
		setOsoba(osoba);
		setStudent(student);
		setPlatio(platio);
		setBiljeska("");
		
		this.student.addListener(change -> {
			EvidencijaDAO.getEvidencijaDAO().updateOrInsert(this);
		});
		this.platio.addListener(change -> {
			EvidencijaDAO.getEvidencijaDAO().updateOrInsert(this);
		});
	}

	/* ID */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return this.id.get();
	}

	public void setId(Integer id) {
		this.id.set(id);
	}
	
	public IntegerProperty idProperty() {
		return this.id;
	}

	/* GODINA */
	@OneToOne(orphanRemoval = false)
	@JoinColumn
	public Godina getGodina() {
		return this.godina.get();
	}

	public void setGodina(Godina godina) {
		this.godina.set(godina);
	}
	
	public ObjectProperty<Godina> godinaProperty() {
		return this.godina;
	}

	/* OSOBA */
	@OneToOne(orphanRemoval = false)
	@JoinColumn
	public Osoba getOsoba() {
		return this.osoba.get();
	}

	public void setOsoba(Osoba osoba) {
		this.osoba.set(osoba);
	}
	
	public ObjectProperty<Osoba> osobaProperty() {
		return this.osoba;
	}

	// TODO je li oslobodjen placanja?.... :)
	/* STUDENT */
	@Column
	public boolean isStudent() {
		return this.student.get();
	}

	public void setStudent(boolean student) {
		this.student.set(student);
	}
	
	public BooleanProperty studentProperty() {
		return this.student;
	}

	/* PLATIO */
	@Column
	public boolean isPlatio() {
		return this.platio.get();
	}

	public void setPlatio(boolean platio) {
		this.platio.set(platio);
	}
	
	public BooleanProperty platioProperty() {
		return this.platio;
	}

	/* BILJESKA */
	@Column
	public String getBiljeska() {
		return this.biljeska.get();
	}

	public void setBiljeska(String biljeska) {
		this.biljeska.set(biljeska);
	}

}
