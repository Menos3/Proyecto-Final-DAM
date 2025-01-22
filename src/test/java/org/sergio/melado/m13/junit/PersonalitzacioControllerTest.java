package org.sergio.melado.m13.junit;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.sergio.melado.m13.classes.DAOHelper;
import org.sergio.melado.m13.models.Usuari;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersonalitzacioControllerTest {

    static EntityManagerFactory emf;
    static EntityManager manager;
    static DAOHelper helper;
    static Usuari u = new Usuari();

    @BeforeClass
    public static void setUpClass() {

        System.out.println("setUpClass");
        emf = Persistence.createEntityManagerFactory("test");
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
        helper = new DAOHelper(emf, Usuari.class);
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
    public void testA_update() {

        u = (Usuari) helper.getLastItem();
        u.setNom("TestA");
        u.setCognom("TestA");
        u.setEmail("testa@test.com");
        u.setTelefon("674551122");
        u.setAdreca("adrecaTest");
        u.setCodiPostal("08080");
        u.setPoblacio("poblacioTest");
        u.setPais("paisTest");
        u.setAdmin(true);
        u.setBloquejat(false);
        helper.update(u);
        assertEquals(u.getNom(), "TestA");
        assertEquals(u.getCognom(), "TestA");
        assertEquals(u.getEmail(), "testa@test.com");
        assertEquals(u.getTelefon(), "674551122");
        assertEquals(u.getAdreca(), "adrecaTest");
        assertEquals(u.getCodiPostal(), "08080");
        assertEquals(u.getPoblacio(), "poblacioTest");
        assertEquals(u.getPais(), "paisTest");
        assertTrue(u.isAdmin());
        assertFalse(u.isBloquejat());

    }

}
