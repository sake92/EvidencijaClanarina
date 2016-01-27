package ba.sake.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Godina {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	public Integer id;
	@Column
	public Integer godina;
	@Column
	public Integer cijena;

	protected Godina() {
	}

	public Godina(Integer id, Integer godina, Integer cijena) {
		this.godina = godina;
		this.cijena = cijena;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGodina() {
		return godina;
	}

	public void setGodina(Integer godina) {
		this.godina = godina;
	}

	public Integer getCijena() {
		return cijena;
	}

	public void setCijena(Integer cijena) {
		this.cijena = cijena;
	}

	@Override
	public String toString() {
		return "" + godina;
	}

}
