package fi.pss.cleanbeach.data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Thrash extends AbstractEntity {

	@ManyToOne
	private User reporter;

	@ManyToOne
	private Location location;

	@ManyToOne
	private ThrashType type;

	private int num;
}