package org.sergio.melado.m13.enums;

import com.sothawo.mapjfx.Projection;

public enum TipusMapa {

    AMERICANA(0, "Americana", Projection.WGS_84),
    EUROPEA(1, "Europea", Projection.WEB_MERCATOR);

    private int codiMapa;
    private String nom;
    private Projection projection;

    TipusMapa(int codiMapa, String nom, Projection projection) {
        this.codiMapa = codiMapa;
        this.nom = nom;
        this.projection = projection;
    }

    public int getCodiMapa() { return codiMapa; }

    public void setCodiMapa(int codiMapa) { this.codiMapa = codiMapa; }

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public Projection getProjection() { return projection; }

    public void setProjection(Projection projection) { this.projection = projection; }
}
