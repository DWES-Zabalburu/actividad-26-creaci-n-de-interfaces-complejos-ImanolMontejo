/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package mis.proyectos.actividad26;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import mis.proyectos.actividad26.modelo.Empleado;
import mis.proyectos.actividad26.modelo.Envio;
import mis.proyectos.actividad26.servicio.EnviosServicio;

/**
 *
 * @author monte
 */
public class Actividad26 {

    private static final EnviosServicio servicio = new EnviosServicio();
    private static final DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
    private static final DateFormat dfMedio = DateFormat.getDateInstance();
    private static final NumberFormat nf = NumberFormat.getInstance();
    private static final NumberFormat nfMoneda = NumberFormat.getCurrencyInstance();
    
    public static void main(String[] args) {
        int opc = 0;
        do {
            String resp = JOptionPane.showInputDialog(
            """
            Gestión de Envíos
            ------------------------
            1. Añadir Envío
            2. Listado Envíos
            3. Envíos Entregados
            4. Generar Resumen Empleados
            5. Salir
            
            Opción [1-4]:
            """);
            opc = Integer.parseInt(resp);
            switch (opc) {
                case 1:
                    añadirEnvio();
                    break;
                case 2:
                    listadoEnvios();
                    break;
                case 3:
                    enviosEntregados();
                    break;
                case 4:
                    generarResumen();
                    break;
            }
        } while (opc !=5);
    }

    private static void añadirEnvio() {
        do {
            String destino = JOptionPane.showInputDialog("Dirección de Entrega");
            String poblacion = JOptionPane.showInputDialog("Población");
            Empleado e = pedirEmpleado();
            while (e == null){
                JOptionPane.showMessageDialog(null, "Introduzca el ID de un empleado válido");
                e = pedirEmpleado();
            }
            Date fechaEnvio = new Date();
            String resp = JOptionPane.showInputDialog("Fecha Envío (dd/mm/aaaa):");
            try {
                fechaEnvio = df.parse(resp);
            } catch (ParseException ex) {
                
            }
            double coste = 0;
            resp = JOptionPane.showInputDialog("Coste:");
            try {
                Number n = nf.parse(resp);
                coste = n.doubleValue();
            } catch (ParseException ex) {
                
            }
            Envio nuevo = new Envio(0, destino, poblacion, e, fechaEnvio, null, coste);
            nuevo = servicio.añadirEnvio(nuevo);
            JOptionPane.showMessageDialog(null, 
                "Añadido Envío con ID : " + nuevo.getId());
        } while (JOptionPane.showConfirmDialog(null, 
             "Añadir Otro Envío",
             "Más Envíos",
             JOptionPane.YES_NO_OPTION)
             == 
             JOptionPane.YES_OPTION);
    }
    
    private static Empleado pedirEmpleado(){ 
        String pregunta =
            JOptionPane.showInputDialog(
            """
            <html>
            <table border=1 bgcolor=white>
            <tr><td>Id</td><td>Empleado</td></tr>
            """ +
            servicio.getEmpleados()
                .stream()
                .map(e -> "<tr><td>%d</td><td>%s</td></tr>"
                    .formatted(e.getIdEmpleado(), e.getApellidos() + ", " + e.getNombre()))
                .reduce((t,s) -> t+s).get()
            +
            """
            </table>
            <h3>Introduzca el Id del Usuario:</h3>
            """);
        int id = Integer.parseInt(pregunta);
        return servicio.getEmpleado(id);
    }

    private static void listadoEnvios() {
        DateFormat dfAñoMes = new SimpleDateFormat("yyyy-MM");
        DoubleSummaryStatistics dbPendientes = servicio.getEnvios()
                .stream()
                .filter(e -> e.getFechaEntrega() == null)
                .collect(Collectors.summarizingDouble(Envio::getCoste));
        DoubleSummaryStatistics dbEntregados = servicio.getEnvios()
                .stream()
                .filter(e -> e.getFechaEntrega() != null)
                .collect(Collectors.summarizingDouble(Envio::getCoste));
        Map<String,Double> rdo = servicio.getEnvios().stream()
                .collect(Collectors.groupingBy(e -> dfAñoMes.format(e.getFechaEnvio()), Collectors.summingDouble(Envio::getCoste) ));
        SortedMap<String,Double> rdoSorted = new TreeMap<>(rdo);
        for(String s : rdoSorted.keySet()){
            System.out.println(s+"->"+rdoSorted.get(s));
        }
        String listado = 
          """
          <html>
          <h1>Listado de Envíos</h1>
          <table border=1 bgcolor=white>
          <tr><td>Id</td><td>Dirección</td><td>Población</td><td>Empleado</td><td>Fecha Envío</td><td>Coste</td><td>Fecha Entrega</td></tr>
          """
          +
          servicio.getEnvios().stream()
           .map(e -> "<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>"
                   .formatted(e.getId(), e.getDireccionEntrega(), e.getPoblacion(), e.getEmpleado().getApellidos()+", "+e.getEmpleado().getNombre(),
                    dfMedio.format(e.getFechaEnvio()), nfMoneda.format(e.getCoste()), 
                    (e.getFechaEntrega()!=null)?dfMedio.format(e.getFechaEntrega()):"")
           ).collect(Collectors.joining())
          + 
          """
          </table>
          <h3>Resumen</h3>
          <table border=1 bgcolor=white>
          <tr><td>Tipo de Envío</td><td>Nº Envíos</td><td>Total Costes</td></tr>
          <tr><td>Entregados</td><td>%d</td><td>%s</td></tr>
          <tr><td>No Entregados</td><td>%d</td><td>%s</td></tr>
          </table>
          """.formatted(dbEntregados.getCount(), nfMoneda.format(dbEntregados.getSum()),
                  dbPendientes.getCount(), nfMoneda.format(dbPendientes.getSum()))
          +
          """
          <h3>Resúmen por Mes y Año</h3>
          <table border=1 bgcolor=white>
          <tr><td>Año / Mes</td><td>Coste Total</td></tr>
          """
          +
          rdoSorted.keySet().stream()
            .map(k -> "<tr><td>%s</td><td>%s</td></tr>".formatted(
            k, nfMoneda.format(rdoSorted.get(k))))
            .collect(Collectors.joining())
          +
          """
          </table>
          </html>
          """
          ;
        JOptionPane.showMessageDialog(null, listado);
    }

    private static void enviosEntregados() {
        String resp = JOptionPane.showInputDialog(
           """
           <html>
           <table border=1 bgcolor=white>
           <tr><td>Id</td><td>Dirección</td><td>Población</td><td>Empleado</td><td>Fecha Envío</td><td>Coste</td></tr>
           """
           +
           servicio.getEnvios()
           .stream()
           .filter(e-> e.getFechaEntrega() == null)
           .map(e -> "<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>"
                   .formatted(e.getId(), e.getDireccionEntrega(), e.getPoblacion(), e.getEmpleado().getApellidos()+", "+e.getEmpleado().getNombre(),
                    dfMedio.format(e.getFechaEnvio()), nfMoneda.format(e.getCoste()))
           ).collect(Collectors.joining())
           +
           """
           </table>
           <h3>Introduzca el ID de envío entregado
           """
        );
        int id = Integer.parseInt(resp);
        Envio e = servicio.getEnvio(id);
        if (e == null){
            JOptionPane.showMessageDialog(null, "No existe ningún envío con ese ID",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } else if (e.getFechaEntrega() != null){
            JOptionPane.showMessageDialog(null, "Ese envío ya está ENTREGADO",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } else {
            Date fechaEntrega = new Date();
            resp = JOptionPane.showInputDialog("Fecha (dd/mm/aaaa)", df.format(fechaEntrega));
            try {
                fechaEntrega =df.parse(resp);
            } catch (ParseException ex) {
            }
            servicio.entregarEnvio(id, fechaEntrega);
            JOptionPane.showMessageDialog(null, "Envío ENTREGADO CORRECTAMENTE",
                "Entregado",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void generarResumen() {
        try {
            PrintWriter pw = new PrintWriter(
                new BufferedWriter(
                new FileWriter("EnviosEmpleados.csv")));
            pw.println("Empleado;Pedidos;Entregados;Pendientes;Total Coste Entregados;Total Coste Pendientes");
            for(Empleado e : servicio.getEmpleados()){
                String empleado = e.getApellidos()+", " + e.getNombre();
                Map<Boolean, DoubleSummaryStatistics> rdo = servicio.getEnvios().stream()
                        .filter(emp -> emp.getEmpleado().equals(e))
                        .collect(Collectors.partitioningBy(env -> env.getFechaEntrega() == null, Collectors.summarizingDouble(Envio::getCoste)));
                pw.println("%s;%d;%d;%d;%s;%s".formatted(
                    empleado,
                    (int) (rdo.get(true).getCount() + rdo.get(false).getCount()),
                    (int) rdo.get(false).getCount(), 
                    (int) rdo.get(true).getCount(),
                    nfMoneda.format(rdo.get(false).getSum()),
                    nfMoneda.format(rdo.get(true).getSum())));
            }
            pw.flush();
            pw.close();
            JOptionPane.showMessageDialog(null, "Fichero GENERADO CORRECTAMENTE",
                "Fichero Creado",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(Actividad26.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}