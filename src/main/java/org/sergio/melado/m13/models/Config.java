package org.sergio.melado.m13.models;

import com.sothawo.mapjfx.Projection;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tbl_config")
public class Config implements Serializable {

    private static final long serialVersionUID = 9038228626276209369L;

    //ATTRIBUTES
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "max_passwd", nullable = false)
    private Integer maxPassword;

    @Column(name = "intents", length = 1, nullable = false)
    private int intents;

    @Column(name = "assumpte_mail", length = 20, nullable = false)
    private String assumpteMail;

    @Column(name = "cos_mail", length = 100, nullable = false)
    private String cosMail;

    @Column(name = "anotacio_mapa",length = 30, nullable = false)
    private Projection anotacioMapa;

    @Column(name = "influxdb_host", length = 20, nullable = false)
    private String influxdbHost;

    @Column(name = "influxdb_port", length = 4, nullable = false)
    private Integer influxdbPort;

    @Column(name = "influxdb_user", length = 10, nullable = false)
    private String influxdbUser;

    @Column(name = "influxdb_password", length = 50, nullable = false)
    private String influxdbPassword;

    @Column(name = "influxdb_ssl")
    private boolean influxdbSsl;

    //GETTERS & SETTERS

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public Integer getMaxPassword() {
        return maxPassword;
    }

    public void setMaxPassword(Integer maxPassword) {
        this.maxPassword = maxPassword;
    }

    public int getIntents() {return intents;}

    public void setIntents(int intents) {this.intents = intents;}

    public String getAssumpteMail() {return assumpteMail;}

    public void setAssumpteMail(String assumpteMail) {this.assumpteMail = assumpteMail;}

    public String getCosMail() {return cosMail;}

    public void setCosMail(String cosMail) {this.cosMail = cosMail;}

    public Projection getAnotacioMapa() { return anotacioMapa; }

    public void setAnotacioMapa(Projection anotacioMapa) { this.anotacioMapa = anotacioMapa; }

    public String getInfluxdbHost() { return influxdbHost; }

    public void setInfluxdbHost(String influxdbHost) { this.influxdbHost = influxdbHost; }

    public Integer getInfluxdbPort() { return influxdbPort; }

    public void setInfluxdbPort(Integer influxdbPort) { this.influxdbPort = influxdbPort; }

    public String getInfluxdbUser() { return influxdbUser; }

    public void setInfluxdbUser(String influxdbUser) { this.influxdbUser = influxdbUser; }

    public String getInfluxdbPassword() { return influxdbPassword; }

    public void setInfluxdbPassword(String influxdbPassword) { this.influxdbPassword = influxdbPassword; }

    public boolean isInfluxdbSsl() { return influxdbSsl; }

    public void setInfluxdbSsl(boolean influxdbSsl) { this.influxdbSsl = influxdbSsl; }
}
