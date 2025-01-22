package org.sergio.melado.m13.classes;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.sergio.melado.m13.jasper.JasperViewerFX;
import org.sergio.melado.m13.models.DispositiuAdaptat;

import java.io.IOException;
import java.io.InputStream;

public class DispositiuReportGenerator {

    private DispositiuAdaptat dispositiuAdaptat;

    public DispositiuReportGenerator(DispositiuAdaptat d) {
        this.dispositiuAdaptat = d;
    }

    public void generateReport(String desde, String finsA, int maxRegistres) throws IOException {

        DispositiuReportInput dispositiuReportInput = DispositiuReportAssembler.assemble(dispositiuAdaptat, desde, finsA, maxRegistres);

        try {
            JRMapArrayDataSource dataSource = new JRMapArrayDataSource(new Object[]{dispositiuReportInput.getDataSources()});
            InputStream inputStream = getClass().getResourceAsStream("/reports/dades_dispositiu_parametritzat.jrxml");
            JasperDesign jdesign = JRXmlLoader.load(inputStream);
            JasperReport jreport = JasperCompileManager.compileReport(jdesign);
            JasperPrint jprint = JasperFillManager.fillReport(jreport, dispositiuReportInput.getParameters(), dataSource);
            new JasperViewerFX().viewReport(Constants.CAPCALERA_VISTA_PREVIA, jprint);

        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    public DispositiuAdaptat getDispositiuAdaptat() {
        return dispositiuAdaptat;
    }

    public void setDispositiuAdaptat(DispositiuAdaptat dispositiuAdaptat) {
        this.dispositiuAdaptat = dispositiuAdaptat;
    }
}
