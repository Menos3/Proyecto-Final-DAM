package org.sergio.melado.m13.junit;

import org.junit.*;
import org.sergio.melado.m13.classes.DAOHelper;
import org.sergio.melado.m13.models.Dispositiu;
import org.sergio.melado.m13.models.Usuari;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.assertEquals;

public class AltaDispositiusControllerTest {

    static EntityManagerFactory emf;
    static EntityManager manager;
    static DAOHelper<Dispositiu> helper;
    static Dispositiu d = new Dispositiu();
    static Usuari u = new Usuari();

    @BeforeClass
    public static void setUpClass() {

        System.out.println("setUpClass");
        emf = Persistence.createEntityManagerFactory("test");

        u.setId(4);
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

        d.setDescripcio("dispositiuTest");
        d.setUsuari(u);
        d.setLatitud("478221");
        d.setLongitud("874551");

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
        helper = new DAOHelper<>(emf, Dispositiu.class);
    }

    @After
    public void tearDown() {

        System.out.println("tearDown");
        if (manager.isOpen()) {
            manager.close();
        }

        manager = null;
        helper = null;
    }

    @Test
    public void testA_insert() {
        try {
            helper.insert(d);
        }catch (Exception e) {
            e.printStackTrace();
        }

        Dispositiu result = helper.getLastItem();

        assertEquals(d.getDescripcio(), result.getDescripcio());
        assertEquals(d.getUsuari(), result.getUsuari());
        assertEquals(d.getLatitud(), result.getLatitud());
        assertEquals(d.getLongitud(), result.getLongitud());
    }

    @Test
    public void testB_update() {

        d = helper.getLastItem();

        Usuari usuari = new Usuari();
        usuari.setId(3);
        usuari.setUsername("kkk");
        usuari.setPasswd("10cf82d70d834cb3bd49af7514f6abf");
        usuari.setNom("testA");
        usuari.setCognom("testA");
        usuari.setEmail("email@example.com");
        usuari.setIntents(0);
        usuari.setTelefon("939415271");
        usuari.setAdreca("testA");
        usuari.setCodiPostal("08731");
        usuari.setPoblacio("testA");
        usuari.setPais("testA");
        usuari.setAdmin(false);
        usuari.setBloquejat(false);

        d.setUsuari(usuari);
        d.setDescripcio("dispositiuTestA");
        d.setLatitud("741255");
        d.setLongitud("102544");

        helper.update(d);
        assertEquals(d.getUsuari(), usuari);
        assertEquals(d.getDescripcio(), "dispositiuTestA");
        assertEquals(d.getLatitud(), "741255");
        assertEquals(d.getLongitud(), "102544");
    }

}
