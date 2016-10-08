package ba.sake.dao.model;

import javax.persistence.*;
import javafx.beans.property.*;

@Entity
public class Godina {
	// @Transient means: do not persist to DB

	@Transient
	private final IntegerProperty id = new SimpleIntegerProperty();
	@Transient
	private final IntegerProperty godina = new SimpleIntegerProperty();
	@Transient
	private final DoubleProperty cijena = new SimpleDoubleProperty();

	protected Godina() {
		// has to exist because of JPA reflection
	}

	public Godina(Integer godina, Double cijena) {
		setGodina(godina);
		setCijena(cijena);
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
	@Column
	public Integer getGodina() {
		return this.godina.get();
	}

	public void setGodina(Integer godina) {
		this.godina.set(godina);
	}

	public IntegerProperty godinaProperty() {
		return this.godina;
	}

	/* CIJENA */
	@Column
	public Double getCijena() {
		return this.cijena.get();
	}

	public void setCijena(Double cijena) {
		this.cijena.set(cijena);
	}

	public DoubleProperty cijenaProperty() {
		return this.cijena;
	}

	/* OSTALO */
	@Override
	public String toString() {
		return "" + getGodina();
	}

}
