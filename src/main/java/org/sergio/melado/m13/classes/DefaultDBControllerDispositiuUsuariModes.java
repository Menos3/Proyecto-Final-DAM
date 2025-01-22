package org.sergio.melado.m13.classes;

import org.sergio.melado.m13.interfaces.IInicia;
import org.sergio.melado.m13.models.Dispositiu;

public abstract class DefaultDBControllerDispositiuUsuariModes extends DefaultDBControllerUsuariModes implements IInicia {

    private Dispositiu dispositiu;

    public Dispositiu getDispositiu() { return dispositiu; }

    public void setDispositiu(Dispositiu dispositiu) { this.dispositiu = dispositiu; }

}
