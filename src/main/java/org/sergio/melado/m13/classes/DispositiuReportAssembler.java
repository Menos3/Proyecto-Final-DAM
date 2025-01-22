package org.sergio.melado.m13.classes;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.influxdb.dto.BoundParameterQuery;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.sergio.melado.m13.models.DispositiuAdaptat;
import org.sergio.melado.m13.models.DummyData;

import java.util.List;

public class DispositiuReportAssembler {

    // Clàusula d'ordenació per la columna time
    private static final String ORDENAT_PER = "ORDER by time ASC";

    public static org.sergio.melado.m13.classes.DispositiuReportInput assemble(DispositiuAdaptat dispositiuAdaptat, int maxRegistres) {
        org.sergio.melado.m13.classes.DispositiuReportInput dispositiuReportInput = new org.sergio.melado.m13.classes.DispositiuReportInput();
        dispositiuReportInput.setTitolInforme("Dades del Dispositiu");
        dispositiuReportInput.setIdDispositiu(dispositiuAdaptat.getId());
        dispositiuReportInput.setDescripcioDispositiu(dispositiuAdaptat.getDescripcio());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(getDades(dispositiuAdaptat.getId(), maxRegistres), false);

        dispositiuReportInput.setDummyDataDataSource(dataSource);

        return dispositiuReportInput;
    }

    public static org.sergio.melado.m13.classes.DispositiuReportInput assemble(DispositiuAdaptat dispositiuAdaptat, String desde, String finsA, int maxRegistres) {
        org.sergio.melado.m13.classes.DispositiuReportInput dispositiuReportInput = new org.sergio.melado.m13.classes.DispositiuReportInput();
        dispositiuReportInput.setTitolInforme("Dades del Dispositiu");
        dispositiuReportInput.setIdDispositiu(dispositiuAdaptat.getId());
        dispositiuReportInput.setDescripcioDispositiu(dispositiuAdaptat.getDescripcio());
        dispositiuReportInput.setDataDesdeConsulta(desde);
        dispositiuReportInput.setDataFinsAConsulta(finsA);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(getDades(dispositiuAdaptat.getId(), desde, finsA, maxRegistres), false);

        dispositiuReportInput.setDummyDataDataSource(dataSource);

        return dispositiuReportInput;
    }

    private static List<DummyData> getDades(Integer id, int maxRows) {

        // IMPORTANT
        // Com que actualment no envia la id, s'utilitza la columna counter per simular-la
        InfluxDBHelper influxDBHelper = new InfluxDBHelper();
        Query query = BoundParameterQuery.QueryBuilder.newQuery("SELECT * FROM dummyData WHERE counter = $id " + ORDENAT_PER + " LIMIT " + maxRows)
                .forDatabase("iwinedb")
                .bind("id", id)
                .create();

        QueryResult queryResult = influxDBHelper.query(query);

        if (queryResult.hasError()) {

            return null;

        } else {

            InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
            return resultMapper.toPOJO(queryResult, DummyData.class);
        }
    }

    private static List<DummyData> getDades(Integer id, String desde, String finsA, int maxRegistres) {

        InfluxDBHelper influxDBHelper = new InfluxDBHelper();

        Query query = BoundParameterQuery.QueryBuilder.newQuery("SELECT * FROM dummyData WHERE time >= $desde AND time <= $finsA AND counter = $id " + ORDENAT_PER + " LIMIT " + maxRegistres)
                .forDatabase("iwinedb")
                .bind("desde", desde)
                .bind("finsA", finsA)
                .bind("id", id)
                .create();

        QueryResult queryResult = influxDBHelper.query(query);

        influxDBHelper.closeClient();

        if (queryResult.hasError()) {

            return null;

        } else {

            InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
            return resultMapper.toPOJO(queryResult, DummyData.class);
        }
    }
}
