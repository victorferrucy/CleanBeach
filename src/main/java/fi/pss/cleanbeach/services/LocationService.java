package fi.pss.cleanbeach.services;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fi.pss.cleanbeach.data.Location;
import fi.pss.cleanbeach.data.Location.STATUS;

@Stateless
public class LocationService {

	@PersistenceContext(unitName = "cleanbeach")
	private EntityManager em;

	public Set<Location> getLocationsNear(double latitude, double longitude) {
		Query q = em
				.createQuery("Select l From Location l where (latitude<? AND latitude>?) AND (longitude<? AND longitude>?)");
		q.setParameter(1, latitude + 1);
		q.setParameter(2, latitude - 1);
		q.setParameter(3, longitude + 1);
		q.setParameter(4, longitude - 1);

		@SuppressWarnings("unchecked")
		java.util.List<Location> list = q.getResultList();

		return list == null ? new HashSet<Location>() : new HashSet<Location>(
				list);
	}

	public Location createLocation(double latitude, double longitude,
			String name) {

		Location l = new Location();
		l.setLongitude(longitude);
		l.setLatitude(latitude);
		l.setName(name);
		l.setStatus(STATUS.NO_DATA);
		em.persist(l);
		return l;
	}

	public Location save(Location selected) {
		em.merge(selected);
		return selected;
	}
}