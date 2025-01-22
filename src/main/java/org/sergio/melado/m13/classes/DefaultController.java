package org.sergio.melado.m13.classes;

import javax.persistence.EntityManagerFactory;

public abstract class DefaultController {

    private EntityManagerFactory emf;

    public EntityManagerFactory getEmf () {
        return emf;
    }

    public void setEmf (EntityManagerFactory emf) {
        this.emf = emf;
    }
}
