package org.sergio.melado.m13.classes;

import org.sergio.melado.m13.models.Usuari;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class UsuariHelper extends DAOHelper<Usuari> {

    public UsuariHelper (EntityManagerFactory emf, Class<Usuari> parameterClass) {
        super (emf, parameterClass);
    }

    public Usuari getUsuariByUsername(String username){
        EntityManager manager = this.emf.createEntityManager();
        CriteriaBuilder cb = emf.getCriteriaBuilder();

        CriteriaQuery<Usuari> cbQuery = cb.createQuery(parameterClass);
        Root<Usuari> c = cbQuery.from(parameterClass);
        cbQuery.select(c).where (cb.equal ((c.get ("username")),username));

        Query query = manager.createQuery (cbQuery);
        try {
            return (Usuari)query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

    }
}
