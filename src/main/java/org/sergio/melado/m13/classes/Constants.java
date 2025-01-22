package org.sergio.melado.m13.classes;

public class Constants {

    //Constants dels botons
    public static final String BTN_DACORD = "D'acord";
    public static final String BTN_SI = "Si";
    public static final String BTN_NO = "No";

    //Constants correu
    public static final String COS_CORREU_BLOQUEIG_ADMIN = "La seva conta ha siguit bloquejada per un administrador. " +
            "Contacti amb el seu administrador de sistema per desbloquejarla";
    public static final String ASSUMPTE_CORREU_CONTRASENYA = "Contrasenya canviada";
    public static final String COS_CORREU_CONTRASENYA = "La seva contrasenya ha sigut canviada. La contrasenya nova es: ";

    //Constants Alerta Camps Buits
    public static final String CAPCALERA_ALERTA_CAMPS_BUITS = "Camp Buit";
    public static final String MISSATGE_ALERTA_CAMPS_BUITS = "Els camps no poden estar buits. Introdueix un caracter com a minim";

    //Constants Alertas Salir de la app
    public static final String TITOL_ALERTA_SORTIR = "Sortir";
    public static final String CAPCALERA_ALERTA_SORTIR = "Esteu sortint de la aplicacio";
    public static final String MISSATGE_ALERTA_SORTIR = "Esteu segurs que voleu sortir de la aplicacio?";

    //Constants Alertas Login
    public static final String TITOL_ALERTA_LOGIN = "Login";
    public static final String CAPCALERA_ALERTA_DADES_INCORRECTES = "Acces incorrecte";
    public static final String CAPCALERA_ALERTA_BLOQUEIG = "Bloqueig";
    public static final String MISSATGE_ALERTA_BLOQUEIG = "El seu usuari es troba bloquejat, per favor contacti amb l'administrador del sistema, perquè siguin restablerts els drets d'accés.";

    //Constants Alertas Canvi Contrasenya
    public static final String TITOL_CANVI_CONTRASENYA = "Canvi de Contrasenya";
    public static final String CAPCALERA_ALERTA_LONGITUD = "Longitud maxima";
    public static final String CAPCALERA_ALERTA_CONTRASENYES_DIFERENTS = "Contrasenyes diferents";
    public static final String MISSATGE_CONTRASENYES_DIFERENTS = "La contrasenya nova i la confirmacio no coincideixen";
    public static final String MISSATGE_CONTRASENYES_NO_COINCIDEIXEN="Les contrasenyes no coincideixen";

    //Constants Alertas Personalitzacio Usuari
    public static final String TITOL_PERSONALITZACIO_USUARI = "Personalitzacio d'usuari";
    public static final String CAPCALERA_ERROR_TELEFON = "Telefon incorrecte ";
    public static final String CAPCALERA_CORREU_INCORRECTE = "Format de correu incorrecte";
    public static final String MISSATGE_NUMERO_TELEFON_INCORRECTE = "La allargada del numero  ha de ser 9 no  ";
    public static final String MISSATGE_CORREU_INCORRECTE ="El correu  no es correcte ";

    //Constants Alertes Configuració
    public static final String TITOL_CONFIGURACIO = "Configuracio del Sistema";

    //Constants Administracio d'Usuaris
    public static final int DOUBLE_CLICK = 2;
    public static final String TITOL_ADMINSTRACIO_USUARIS = "Administracio d'Usuaris";
    public static final String CONFIRMAR_ACCIO_ELIMINAR_USUARI = "Eliminar Usuari";
    public static final String SEGUR_QUE_VOLS_ELIMINAR_USUARI = "Segur que vols eliminar al usuari: ";

    //Constants Administracio de Dispositius
    public static final String TITOL_ADMINISTRACIO_DISPOSITIUS = "Administracio de Dispositius";
    public static final String CONFIRMAR_ACCIO_ELIMINAR_DISPOSITIU = "Eliminar Dispositiu";
    public static final String SEGUR_QUE_VOLS_ELIMINAR_DISPOSITIU = "Segur que vols eliminar el dispositiu amb la ID : ";

    //Constants Alta Dispositius
    public static final String TITOL_ALERTA_ALTA_DISPOSITIUS = "Alta/Modificacio de Dispositius";

    //Costants Mapa
    public static final double DEFAULT_LATITUD = 41.3461265;
    public static final double DEFAULT_LONGITUD = 1.6997939799999972;
    public static final int ZOOM_DEFAULT = 14;
    public static final int ZOOM_LOCATED = 17;

    //Constants Alerta dummyDataList buida
    public static final String TITOL_ALERTA_LLISTA_BUIDA = "ERROR";
    public static final String CAPCALERA_ALERTA_LLISTA_BUIDA = "No es troben dades";
    public static final String MISSATGE_ALERTA_LLISTA_BUIDA = "No hi han resultats en les dates que ha introduït";

    //Constants JasperReports
    public static final String CAPCALERA_VISTA_PREVIA = "Vista previa";

    //Constants Monitoritzacio de Dispositius
    public static final String ORDENATS_PER = "ORDER by time ASC";
    public static final String TITOL_MONITORITZACIO_DISPOSITIUS = "Monitoritzacio de Dispositius";
    public static final String CAPCALERA_ALERTA_SLIDER_NULL_REGISTRES = "No s'ha detectat moviment en la seleccio de registres a mostrar";
    public static final String MISSATGE_ALERTA_SLIDER_NULL_REGISTRES = "S'ha d'escollir com a minim 1 registre a mostrar en la grafica";
    public static final String CAPCALERA_ALERTA_SLIDER_NULL_TEMPS = "No s'ha detectat moviment en la seleccio de temps d'actualitzacio de la grafica";
    public static final String MISSATGE_ALERTA_SLIDER_NULL_TEMPS = "S'ha d'escollir com a minim 1 segon per a que s'actualitzi la grafica";
    public static final String CAPCALERA_ALERTA_DATEPICKERS_BUITS = "No s'ha seleccionat cap data";
    public static final String MISSATGE_ALERTA_DATEPICKERS_BUITS = "Les dates no poden estar buides";
    public static final String CAPCALERA_ALERTA_TEXTFIELD_DATA_BUIT = "Camps de text buits o incorrectes";
    public static final String MISSATGE_ALERTA_TEXTFIELD_DATA_BUIT = "Sisplau, posi una hora del dia en aquest format : HH:MM:SS";
    public static final String CAPCALERA_INFO_GRAFICA_ATURADA = "Grafica aturada";
    public static final String MISSATGE_INFO_GRAFICA_ATURADA = "La grafica ha deixat d'actualitzar-se";
    public static final String CAPCALERA_ALERTA_GRAFICA_ATURADA = "Planificador de la grafica aturat";
    public static final String MISSATGE_ALERTA_GRAFICA_ATURADA = "Si us plau, tenqui la finestra i torni a obrir-la per executar una nova grafica";


}
