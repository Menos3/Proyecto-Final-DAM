package org.sergio.melado.m13.models;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.io.Serializable;
import java.time.Instant;

@Measurement(name = "dummyData", database = "iwinedb")
public class DummyData implements Serializable {

    private static final long serialVersionUID = -1497034550418278038L;

    @Column(name = "time")
    private Instant time;

    @Column(name = "airtime")
    private Double airtime;

    @Column(name = "counter")
    private Long comptador;

    @Column(name = "humidity")
    private Double humetat;

    @Column(name = "pressure")
    private Double presio;

    @Column(name = "random")
    private Long valorAleatori;

    @Column(name = "temperature")
    private Double temperatura;

    @Column(name = "uptime")
    private Long tempsFuncionant;

    //GETTERS && SETTERS

    public Instant getTime() { return time; }

    public void setTime(Instant time) { this.time = time; }

    public Double getAirtime() { return airtime; }

    public void setAirtime(Double airtime) { this.airtime = airtime; }

    public Long getComptador() { return comptador; }

    public void setComptador(Long comptador) { this.comptador = comptador; }

    public Double getHumetat() { return humetat; }

    public void setHumetat(Double humetat) { this.humetat = humetat; }

    public Double getPresio() { return presio; }

    public void setPresio(Double presio) { this.presio = presio; }

    public Long getValorAleatori() { return valorAleatori; }

    public void setValorAleatori(Long valorAleatori) { this.valorAleatori = valorAleatori; }

    public Double getTemperatura() { return temperatura; }

    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }

    public Long getTempsFuncionant() { return tempsFuncionant; }

    public void setTempsFuncionant(Long tempsFuncionant) { this.tempsFuncionant = tempsFuncionant; }

    @Override
    public String toString() {
        return "DummyData{" +
                "time=" + time +
                ", airtime=" + airtime +
                ", comptador=" + comptador +
                ", humetat=" + humetat +
                ", presio=" + presio +
                ", valorAleatori=" + valorAleatori +
                ", temperatura=" + temperatura +
                ", tempsFuncionant=" + tempsFuncionant +
                '}';
    }

}
