package org.sergio.melado.m13.classes;

import org.sergio.melado.m13.enums.Modes;
import org.sergio.melado.m13.interfaces.IInicia;

public abstract class DefaultDBControllerUsuariModes extends DefaultDBControllerUsuari implements IInicia {

    private Modes mode;

    public Modes getMode() { return mode; }

    public void setMode(Modes mode) { this.mode = mode; }

}
