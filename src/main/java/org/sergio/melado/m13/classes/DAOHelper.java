package org.sergio.melado.m13.classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class DAOHelper<T> {

    protected EntityManagerFactory emf;
    protected final Class<T> parameterClass;

    public DAOHelper(EntityManagerFactory emf, Class<T> parameterClass) {
        this.emf = emf;
        this.parameterClass = parameterClass;
    }

    /**
     *
     * @param t Es el objecte que li passem a la funció per fer el Insert a la base de dades
     *
     * Aquesta funció s'encarrega de fer un INSERT (Afegir els valors) a la BBDD.
     */
    public void insert(T t) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(t);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    /**
     *
     * @param t Es el objecte que li passem a la funció per fer el Update a la base de dades
     *
     *  Aquesta funció s'encarrega de fer un UPDATE (actualitar els valors) a la BBDD.
     */
    public void update(T t) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(t);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    /**
     *
     * @param id Es un camp de la base de dades  que és únic per cada registre
     *
     *  Aquesta funció s'encarrega de eliminar un registre de la BBDD utilitzant la id del resgistre en concret.
     */

    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();
        T t = em.find((Class<T>) parameterClass, id);

        try {
            if (t != null) {
                em.getTransaction().begin();
                em.remove(t);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    /**
     *
     * @param id Es un camp de la base de dades  que és únic per cada registre
     * @return  registre de la BBDD segons la Id que es passa
     *
     * Aquesta funció s'encarrega de mostrar el valor de la BBDD que correspongui amb el Id.
     */
    public T getById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find((Class<T>) parameterClass, id);
        } finally {
            em.close();
        }
    }

    /**
     *
     * @return llista de tots els valors de la taula de la BBDD
     *
     *  Aquesta funció fa un SELECT d'una taula en concret i la retorna en format Llista.
     */
    public List<T> getAll() {
        CriteriaBuilder cb = this.emf.getCriteriaBuilder();
        EntityManager manager = this.emf.createEntityManager();

        CriteriaQuery<T> cbQuery = cb.createQuery((Class<T>) parameterClass);
        Root<T> c = cbQuery.from((Class<T>) parameterClass);
        cbQuery.select(c);

        Query query = manager.createQuery(cbQuery);

        return query.getResultList();
    }

    /**
     * Mostra tots els valors d'una llista.
     */
    public void printAll() {
        List<T> list = getAll();
        list.forEach(System.out::println);
    }

    /**
     * @return l'ultim registre d'una taula o d'una llista
     *
     * Metode per buscar i mostrar l'ultim registre d'una taula o d'una llista
     * Es crea una connexio a la base de dades i un criteri de cerca, s'aconsegueix la informacio que es busca i es tenca al connexio per alliberar recursos
     *
     */
    public T getLastItem() {

        CriteriaBuilder cb = this.emf.getCriteriaBuilder();
        EntityManager em = this.emf.createEntityManager();
        List<T> llista;

        try {

            CriteriaQuery<T> cbQuery = cb.createQuery(parameterClass);
            Root<T> c = cbQuery.from(parameterClass);
            cbQuery.select(c);

            Query query = em.createQuery(cbQuery);
            llista = query.getResultList();

        } finally {

            em.close();
        }

        return (!llista.isEmpty()) ? llista.get(llista.size() - 1) : null;
    }

    public ObservableList<T> getAllToObservableList() {

        CriteriaBuilder cb = this.emf.getCriteriaBuilder();
        EntityManager manager = this.emf.createEntityManager();

        CriteriaQuery<T> cbQuery = cb.createQuery(parameterClass);
        Root<T> c = cbQuery.from(parameterClass);
        cbQuery.select(c);

        Query query = manager.createQuery(cbQuery);

        return FXCollections.observableList(query.getResultList());
    }

    public T getFirst() {

        List<T> list = getAll();
        return getAll().isEmpty() ? null : list.iterator().next();
    }
}
