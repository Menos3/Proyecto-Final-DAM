package org.sergio.melado.m13.junit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.sergio.melado.m13.enums.LoginStates;
import org.sergio.melado.m13.utils.DBUtils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.assertEquals;

@DisplayName("Test: Avaluador de login ")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class DBTestUtils {

    private static EntityManagerFactory emf;
    private static DBUtils DBUtils;

    @BeforeAll
    static void beforeAll() {

       emf = Persistence.createEntityManagerFactory ("mariaDB");
       DBUtils = new DBUtils(emf);

    }

    @AfterAll
    static void afterAll() {

        if(emf.isOpen ()) {

            emf.close ();
        }

        emf = null;

    }

    @ParameterizedTest
    @DisplayName("L' usuari introdueix les dades correctament")
    @Order(1)
    @CsvSource ({"bgates,pass","pnorton,pass","emusk,pass"})
    void usuari_introdueix_les_dades_correctament(ArgumentsAccessor argumentsAccessor) {

        // Sabem que entra

        String usuari = argumentsAccessor.getString(0);
        String password = argumentsAccessor.getString(1);

        //Sabem el que surt
        LoginStates esperat = LoginStates.GRANTED;


        //Provem el nostre metode
        LoginStates resultat = DBUtils.checkPassword(usuari,password);

        assertEquals(String.format("Utils.checkPassword(%s:%s) falla", usuari, password), esperat, resultat);
    }


    @ParameterizedTest
    @DisplayName("L' usuari introdueix les dades incorrectament")
    @Order(2)
    @CsvSource ({"bgates,fail","pnorton,fail","emusk,fail"})
    void usuari_introdueix_les_dades_incorrectament(ArgumentsAccessor argumentsAccessor) {

        // Sabem que entra

        String usuari = argumentsAccessor.getString(0);
        String password = argumentsAccessor.getString(1);

        //Sabem el que surt
        LoginStates esperat = LoginStates.FAILED;


        //Provem el nostre metode
        LoginStates resultat = DBUtils.checkPassword(usuari,password);

        assertEquals(String.format("Utils.checkPassword(%s:%s) falla", usuari, password), esperat, resultat);
    }

    @ParameterizedTest
    @DisplayName("L' usuari introdueix les dades incorrectament i es bloqueja el compte")
    @Order(3)
    @CsvSource ({"bgates,fail","pnorton,fail","emusk,fail"})
    void usuari_introdueix_les_dades_incorrectament_i_es_bloqueja_el_compte(ArgumentsAccessor argumentsAccessor) {

        // Sabem que entra

        String usuari = argumentsAccessor.getString(0);
        String password = argumentsAccessor.getString(1);

        //Sabem el que surt
        LoginStates esperat = LoginStates.LOCKED;


        //Provem el nostre metode
        LoginStates resultat = DBUtils.checkPassword(usuari,password);

        assertEquals(String.format("Utils.checkPassword(%s:%s) falla", usuari, password), esperat, resultat);
    }

}