package org.sergio.melado.m13.classes;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.HashMap;
import java.util.Map;

public class DispositiuReportInput {

    private String titolInforme;
    private Integer idDispositiu;
    private String descripcioDispositiu;
    private String dataDesdeConsulta;
    private String dataFinsAConsulta;
    private JRBeanCollectionDataSource dummyDataDataSource;

    public String getTitolInforme() {
        return titolInforme;
    }

    public void setTitolInforme(String titolInforme) {
        this.titolInforme = titolInforme;
    }

    public Integer getIdDispositiu() {
        return idDispositiu;
    }

    public void setIdDispositiu(Integer idDispositiu) {
        this.idDispositiu = idDispositiu;
    }

    public String getDescripcioDispositiu() {
        return descripcioDispositiu;
    }

    public void setDescripcioDispositiu(String descripcioDispositiu) { this.descripcioDispositiu = descripcioDispositiu; }

    public String getDataDesdeConsulta() { return dataDesdeConsulta; }

    public void setDataDesdeConsulta(String dataDesdeConsulta) { this.dataDesdeConsulta = dataDesdeConsulta; }

    public String getDataFinsAConsulta() { return dataFinsAConsulta; }

    public void setDataFinsAConsulta(String dataFinsAConsulta) { this.dataFinsAConsulta = dataFinsAConsulta; }

    public JRBeanCollectionDataSource getDummyDataDataSource() { return dummyDataDataSource; }

    public void setDummyDataDataSource(JRBeanCollectionDataSource dummyDataDataSource) { this.dummyDataDataSource = dummyDataDataSource; }

    public Map<String, Object> getParameters() {
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("P_TITOL_INFORME", getTitolInforme());
        parameters.put("P_ID_DISPOSITIU", getIdDispositiu());
        parameters.put("P_DESC_DISPOSITIU", getDescripcioDispositiu());
        parameters.put("P_DATA_DESDE", getDataDesdeConsulta());
        parameters.put("P_DATA_FINSA", getDataFinsAConsulta());
        return parameters;
    }

    public Map<String, Object> getDataSources() {
        Map<String,Object> dataSources = new HashMap<>();
        dataSources.put("dummyDataDataSource", dummyDataDataSource);
        return dataSources;
    }
}