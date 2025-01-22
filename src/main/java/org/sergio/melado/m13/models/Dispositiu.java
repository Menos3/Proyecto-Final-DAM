package org.sergio.melado.m13.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table (name = "tbl_dispositiu")
public class Dispositiu implements Serializable {

    private static final long serialVersionUID = 9038228626276209369L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "usuari_propietari")
    private Usuari usuari;

    @Column(name = "descripcio", nullable = false, length = 100)
    private String descripcio;

    @Column(name = "latitud", nullable = false, length = 30)
    private String latitud;

    @Column(name = "longitud", nullable = false, length = 30)
    private String longitud;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public Usuari getUsuari() { return usuari; }

    public void setUsuari(Usuari usuari) { this.usuari = usuari; }

    public String getDescripcio() { return descripcio; }

    public void setDescripcio(String descripcio) { this.descripcio = descripcio; }

    public String getLatitud() { return latitud; }

    public void setLatitud(String latitud) { this.latitud = latitud; }

    public String getLongitud() { return longitud; }

    public void setLongitud(String longitud) { this.longitud = longitud; }
}
