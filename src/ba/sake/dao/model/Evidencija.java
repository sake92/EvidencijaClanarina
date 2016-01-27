package ba.sake.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Evidencija {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	public Integer id;

	@OneToOne
	@JoinColumn
	public Godina godina;

	@OneToOne
	@JoinColumn
	public Osoba osoba;

	@Column
	public boolean student;

	@Column
	public boolean platio;

	@Column
	public String biljeska;

	public Evidencija(Godina godina, Osoba osoba, boolean student, boolean platio) {
		this.godina = godina;
		this.osoba = osoba;
		this.student = student;
		this.platio = platio;
		this.biljeska = "";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Godina getGodina() {
		return godina;
	}

	public void setGodina(Godina godina) {
		this.godina = godina;
	}

	public Osoba getOsoba() {
		return osoba;
	}

	public void setOsoba(Osoba osoba) {
		this.osoba = osoba;
	}

	public boolean isStudent() {
		return student;
	}

	public void setStudent(boolean student) {
		this.student = student;
	}

	public boolean isPlatio() {
		return platio;
	}

	public void setPlatio(boolean platio) {
		this.platio = platio;
	}

	public String getBiljeska() {
		return biljeska;
	}

	public void setBiljeska(String biljeska) {
		this.biljeska = biljeska;
	}

}
