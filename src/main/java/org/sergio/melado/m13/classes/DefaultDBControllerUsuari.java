package org.sergio.melado.m13.classes;

import org.sergio.melado.m13.interfaces.IInicia;
import org.sergio.melado.m13.models.Usuari;

public abstract class DefaultDBControllerUsuari extends DefaultController implements IInicia {

    private Usuari usuari;

    public Usuari getUsuari() {
        return usuari;
    }

    public void setUsuari(Usuari usuari) {
        this.usuari = usuari;
    }
}
