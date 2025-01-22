package org.sergio.melado.m13.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="tbl_usuari")
public class Usuari implements Serializable {

    private static final long serialVersionUID = 9038228626276209369L;
    //ATRIBUTOS

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Integer id;

    @Column(name = "username" ,length = 50, nullable = false)
    private String username;

    @Column (name = "passwd",length = 64, nullable = false)
    private String passwd;

    @Column (name = "nom", length = 20, nullable = false)
    private String nom;

    @Column (name = "cognom", length = 20, nullable = false)
    private String cognom;

    @Column (name = "email", length = 100, nullable = false)
    private String email;

    @Column (name = "intents")
    private Integer intents;

    @Column (name = "telefon", length = 9)
    private String telefon;

    @Column (name = "adreca", length = 50)
    private String adreca;

    @Column (name = "codi_postal", length = 5)
    private String codiPostal;

    @Column (name = "poblacio", length = 50)
    private String poblacio;

    @Column (name = "pais", length = 30)
    private String pais;

    @Column (name = "isAdmin")
    private boolean isAdmin;

    @Column (name = "bloquejat")
    private boolean bloquejat;

    @OneToMany(mappedBy = "usuari", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dispositiu> dispositiuList = new ArrayList<>();

    //GETTERS & SETTERS

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPasswd() { return passwd; }

    public void setPasswd(String passwd) { this.passwd = passwd; }

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public String getCognom() { return cognom; }

    public void setCognom(String cognom) { this.cognom = cognom; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Integer getIntents() { return intents; }

    public void setIntents(Integer intents) { this.intents = intents; }

    public String getTelefon() { return telefon; }

    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getAdreca() { return adreca; }

    public void setAdreca(String adreca) { this.adreca = adreca; }

    public String getCodiPostal() { return codiPostal; }

    public void setCodiPostal(String codiPostal) { this.codiPostal = codiPostal; }

    public String getPoblacio() { return poblacio; }

    public void setPoblacio(String poblacio) { this.poblacio = poblacio; }

    public String getPais() { return pais; }

    public void setPais(String pais) { this.pais = pais; }

    public boolean isAdmin() { return isAdmin; }

    public void setAdmin(boolean admin) { isAdmin = admin; }

    public boolean isBloquejat() { return bloquejat; }

    public void setBloquejat(boolean bloquejat) { this.bloquejat = bloquejat; }

    public List<Dispositiu> getDispositiuList() { return dispositiuList; }

    public void setDispositiuList(List<Dispositiu> dispositiuList) { this.dispositiuList = dispositiuList; }

    @Override
    public String toString() { return nom; }
}
