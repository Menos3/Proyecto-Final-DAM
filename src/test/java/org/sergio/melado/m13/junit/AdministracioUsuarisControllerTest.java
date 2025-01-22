package org.sergio.melado.m13.junit;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.sergio.melado.m13.classes.DAOHelper;
import org.sergio.melado.m13.models.Usuari;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdministracioUsuarisControllerTest {

    static EntityManagerFactory emf;
    static EntityManager manager;
    static DAOHelper helper;
    static Usuari u = new Usuari();

    @BeforeClass
    public static void setUpClass() {

        System.out.println("setUpClass");
        emf = Persistence.createEntityManagerFactory("test");

        u.setId(3);
        u.setUsername("kk");
        u.setPasswd("10cf82d70d834cb3bd49af7514f6abf");
        u.setNom("test");
        u.setCognom("test");
        u.setEmail("email@example.com");
        u.setIntents(0);
        u.setTelefon("938415271");
        u.setAdreca("test");
        u.setCodiPostal("08731");
        u.setPoblacio("test");
        u.setPais("test");
        u.setAdmin(false);
        u.setBloquejat(false);

    }

    @AfterClass
    public static void tearDownClass() {

        System.out.println("tearDownClass");
        if (emf.isOpen()) {
            emf.close();
        }

        emf = null;
    }

    @Before
    public void setUp() {

        System.out.println("setUp");
        manager = emf.createEntityManager();
        helper = new DAOHelper<>(emf, Usuari.class);
    }

    @After
    public void tearDown() {

        System.out.println("tearDown");
        if (manager.isOpen()) {
            manager.close();
        }

        helper = null;
        manager = null;
    }

    @Test
    public void testA_delete() {
        Usuari usuari = (Usuari) helper.getLastItem();
        assertEquals(u.getId(), usuari.getId());
        helper.delete(u.getId());
    }

    @Test
    public void testB_getAll() {
        List<Usuari> lUsuaris = helper.getAll();
        lUsuaris.forEach(System.out::println);
    }
}
