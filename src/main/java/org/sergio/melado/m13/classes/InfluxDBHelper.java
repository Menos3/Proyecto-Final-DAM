package org.sergio.melado.m13.classes;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

public class InfluxDBHelper {

    private static InfluxDB client;

    public InfluxDBHelper() { client = createClient(); }

    /**
     * Crea una connexio a InfluxDB
     * @return
     */

    private InfluxDB createClient() {

        String protocol = AppConfigSingleton.getInstancia().isInfluxDBSsl() ? "https" : "http";
        String influxUrl = protocol + "://" + AppConfigSingleton.getInstancia().getInfluxDBHost() + ":" + AppConfigSingleton.getInstancia().getInfluxDBPort();

        return InfluxDBFactory.connect(influxUrl, AppConfigSingleton.getInstancia().getInfluxDBUser(), AppConfigSingleton.getInstancia().getInfluxDBPassword());
    }

    public void closeClient() {
        client.close();
    }

    public QueryResult query(Query query) {
        return client.query(query);
    }

}
