package org.sergio.melado.m13.junit;

import org.junit.*;
import org.sergio.melado.m13.classes.DAOHelper;
import org.sergio.melado.m13.models.Config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.assertEquals;

public class ConfigControllerTest {

    static EntityManagerFactory emf;
    static EntityManager manager;
    static DAOHelper helper;
    static Config config = new Config();

    @BeforeClass
    public static void setUpClass() {

        System.out.println("setUpClass");
        emf = Persistence.createEntityManagerFactory("test");

        config.setMaxPassword(4);
        config.setIntents(6);
        config.setAssumpteMail("Assumpte");
        config.setCosMail("Aquest es el cos del correu");

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
        helper = new DAOHelper(emf, Config.class);
    }

    @After
    public void tearDown() {

        System.out.println("tearDown");
        if (manager.isOpen())
            manager.close();

        helper = null;
        manager = null;
    }

    @Test
    public void testA_insert() {
        try {
            helper.insert(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Config result = (Config) helper.getLastItem();

        assertEquals(config.getId(), result.getId());
        assertEquals(config.getMaxPassword(), result.getMaxPassword());
        assertEquals(config.getIntents(), result.getIntents());
        assertEquals(config.getAssumpteMail(), result.getAssumpteMail());
        assertEquals(config.getCosMail(), result.getCosMail());
    }

    @Test
    public void testB_update() {

        config = (Config) helper.getLastItem();
        config.setMaxPassword(8);
        config.setIntents(12);
        config.setAssumpteMail("Assumpt");
        config.setCosMail("body");

        helper.update(config);
        assertEquals((Integer) (config.getMaxPassword()), (Integer) 8);
        assertEquals(config.getIntents(), 12);
        assertEquals(config.getAssumpteMail(), "Assumpt");
        assertEquals(config.getCosMail(), "body");
    }




}
