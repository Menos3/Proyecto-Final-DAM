package org.sergio.melado.m13.models;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.List;

public class DispositiuAdaptat {

    //Model per adaptar un model de base de dades a un informe
    private Integer id;
    private String descripcio;
    private List<DummyData> dummyData;
    private JRBeanCollectionDataSource dummyDataDataSource;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public List<DummyData> getDummyData() {
        return dummyData;
    }

    public void setDummyData(List<DummyData> dummyData) {
        this.dummyData = dummyData;
    }

    public JRBeanCollectionDataSource getDummyDataDataSource() {
        return new JRBeanCollectionDataSource(dummyData, false);
    }
}
