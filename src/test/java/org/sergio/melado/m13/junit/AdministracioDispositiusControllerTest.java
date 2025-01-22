package org.sergio.melado.m13.junit;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.sergio.melado.m13.classes.Constants;
import org.sergio.melado.m13.classes.DAOHelper;
import org.sergio.melado.m13.models.Dispositiu;
import org.sergio.melado.m13.models.Usuari;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdministracioDispositiusControllerTest {

    static EntityManagerFactory emf;
    static EntityManager manager;
    static DAOHelper<Dispositiu> helperDispositiu;
    static Dispositiu d = new Dispositiu();
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

        d.setId(4);
        d.setDescripcio("dispositiuTest");
        d.setUsuari(u);
        d.setLatitud(String.valueOf(Constants.DEFAULT_LATITUD));
        d.setLongitud(String.valueOf(Constants.DEFAULT_LONGITUD));
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
        helperDispositiu = new DAOHelper<>(emf, Dispositiu.class);
    }

    @After
    public void tearDown() {

        System.out.println("tearDown");
        if (manager.isOpen()) {
            manager.close();
        }

        helperDispositiu = null;
        manager = null;
    }

    @Test
    public void testA_delete() {
        Dispositiu dispositiu = helperDispositiu.getLastItem();
        assertEquals(d.getId(), dispositiu.getId());
        helperDispositiu.delete(d.getId());
    }

    @Test
    public void testB_getAll() {
        List<Dispositiu> lDispositius = helperDispositiu.getAll();
        lDispositius.forEach(System.out::println);
    }
}
