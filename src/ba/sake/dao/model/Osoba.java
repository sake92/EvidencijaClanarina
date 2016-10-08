package ba.sake.dao.model;

import java.time.LocalDate;

import javax.persistence.*;
import javafx.beans.property.*;

@Entity
public class Osoba {

	@Transient
	private final IntegerProperty id = new SimpleIntegerProperty();
	@Transient
	private final StringProperty ime = new SimpleStringProperty();
	@Transient
	private final StringProperty prezime = new SimpleStringProperty();
	@Transient
	private final ObjectProperty<LocalDate> datumRodjenja = new SimpleObjectProperty<>();

	protected Osoba() {
		// has to exist because of JPA reflection
	}

	public Osoba(String ime, String prezime, LocalDate datumRodjenja) {
		setIme(ime);
		setPrezime(prezime);
		setDatumRodjenja(datumRodjenja);
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

	/* IME */
	@Column
	public String getIme() {
		return this.ime.get();
	}

	public void setIme(String ime) {
		this.ime.set(ime);
	}

	public StringProperty imeProperty() {
		return this.ime;
	}

	/* PREZIME */
	@Column
	public String getPrezime() {
		return this.prezime.get();
	}

	public void setPrezime(String prezime) {
		this.prezime.set(prezime);
	}

	public StringProperty prezimeProperty() {
		return this.prezime;
	}

	/* DATUM RODJENJA */
	@Column
	public LocalDate getDatumRodjenja() {
		return this.datumRodjenja.get();
	}

	public void setDatumRodjenja(LocalDate datumRodjenja) {
		this.datumRodjenja.set(datumRodjenja);
	}

	public ObjectProperty<LocalDate> datumRodjenjaProperty() {
		return this.datumRodjenja;
	}

	/* OSTALO ... */
	@Override
	public String toString() {
		return getPrezime() + " " + getIme();
	};
}
