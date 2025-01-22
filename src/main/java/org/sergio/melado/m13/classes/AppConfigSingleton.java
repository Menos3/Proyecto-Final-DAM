package org.sergio.melado.m13.classes;

import com.sothawo.mapjfx.Projection;

public class AppConfigSingleton {

    private static AppConfigSingleton instancia;
    private Integer maxPassword;
    private int intents;
    private String assumpteMail;
    private String cosMail;
    private Projection anotacioMapa;
    private String influxDBHost;
    private Integer influxDBPort;
    private String influxDBUser;
    private String influxDBPassword;
    private boolean influxDBSsl;

    /**
     * @return la instancia creada
     * <p>
     * Metode per crear instancies de la classe AppConfigSingleton
     * Si la instancia a crear es diferent de null, es crea una nova instancia de la classe AppConfigSingleton
     */
    public static AppConfigSingleton getInstancia() {
        if (instancia == null)
            instancia = new AppConfigSingleton();
        return instancia;
    }

    private AppConfigSingleton() {

    }

    public static void setInstancia(AppConfigSingleton instancia) { AppConfigSingleton.instancia = instancia; }

    public Integer getMaxPassword() { return maxPassword; }

    public void setMaxPassword(Integer maxPassword) { this.maxPassword = maxPassword; }

    public int getIntents() { return intents; }

    public void setIntents(int intents) { this.intents = intents; }

    public String getAssumpteMail() { return assumpteMail; }

    public void setAssumpteMail(String assumpteMail) { this.assumpteMail = assumpteMail; }

    public String getCosMail() { return cosMail; }

    public void setCosMail(String cosMail) { this.cosMail = cosMail; }

    public Projection getAnotacioMapa() { return anotacioMapa; }

    public void setAnotacioMapa(Projection anotacioMapa) { this.anotacioMapa = anotacioMapa; }

    public String getInfluxDBHost() { return influxDBHost; }

    public void setInfluxDBHost(String influxDBHost) { this.influxDBHost = influxDBHost; }

    public Integer getInfluxDBPort() { return influxDBPort; }

    public void setInfluxDBPort(Integer influxDBPort) { this.influxDBPort = influxDBPort; }

    public String getInfluxDBUser() { return influxDBUser; }

    public void setInfluxDBUser(String influxDBUser) { this.influxDBUser = influxDBUser; }

    public String getInfluxDBPassword() { return influxDBPassword; }

    public void setInfluxDBPassword(String influxDBPassword) { this.influxDBPassword = influxDBPassword; }

    public boolean isInfluxDBSsl() { return influxDBSsl; }

    public void setInfluxDBSsl(boolean influxDBSsl) { this.influxDBSsl = influxDBSsl; }
}
