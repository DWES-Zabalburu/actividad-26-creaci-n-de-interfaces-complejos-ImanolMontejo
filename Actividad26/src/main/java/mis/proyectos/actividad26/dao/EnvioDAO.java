/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mis.proyectos.actividad26.dao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mis.proyectos.actividad26.modelo.Envio;

/**
 *
 * @author monte
 */
public class EnvioDAO {
    private static List<Envio> envios;
    private static final String FICH_ENVIOS = "envios.dat";
    
    private static final DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
    
    private static EmpleadoDAO empDAO = new EmpleadoDAO();
    
    public EnvioDAO(){
        envios = new ArrayList<>();
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(
                    new BufferedInputStream(
                            new FileInputStream(FICH_ENVIOS)));
            while (true){
                int id = dis.readInt();
                String direccionEntrega = dis.readUTF();
                String poblacion = dis.readUTF();
                int idEmpleado = dis.readInt();
                Date fechaEnvio = new Date();
                try {
                    fechaEnvio = df.parse(dis.readUTF());
                } catch (ParseException ex) {
                    Logger.getLogger(EnvioDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
                Date fechaEntrega = null;
                try {
                    fechaEntrega = df.parse(dis.readUTF());
                } catch (ParseException ex) {
                    
                }
                double coste = dis.readDouble();
                Envio e = new Envio(id, direccionEntrega, poblacion, empDAO.getEmpleado(idEmpleado), 
                        fechaEnvio, fechaEntrega, coste);
                envios.add(e);
            }
        } catch (FileNotFoundException ex) {
            
        } catch (EOFException ex){
            try {
                dis.close();
            } catch (IOException ex1) {
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
    
    public List<Envio> getEnvios(){
        return envios.stream()
                .sorted((e1,e2) -> e2.getFechaEnvio().compareTo(e1.getFechaEnvio()))
                .toList();
    }
    
    public Envio getEnvio(int id){
        return envios.stream()
                .filter(e -> e.getId()==id)
                .findFirst()
                .orElse(null);
    }
    
    public Envio añadirEnvio(Envio e){
        // Obtener el ID
        int id = envios.stream()
            .map(Envio::getId)
            .max((id1,id2) -> id1 - id2)
            .orElse(0)
            + 1;
        e.setId(id);
        envios.add(e);
        guardarEnvios();
        return e;
    }
    
    public void entregarEnvio(int idEnvio, Date fechaEntrega){
        Envio e = getEnvio(idEnvio);
        if (e != null){
            e.setFechaEntrega(fechaEntrega);
            guardarEnvios();
        }
    }
    
    private void guardarEnvios(){
        try {
            DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(FICH_ENVIOS)));
            for(Envio e : envios){
                dos.writeInt(e.getId());
                dos.writeUTF(e.getDireccionEntrega());
                dos.writeUTF(e.getPoblacion());
                dos.writeInt(e.getEmpleado().getIdEmpleado());
                dos.writeUTF(df.format(e.getFechaEnvio()));
                if (e.getFechaEntrega() == null){
                   dos.writeUTF("null");
                } else {
                    dos.writeUTF(df.format(e.getFechaEntrega()));
                }
                dos.writeDouble(e.getCoste());
            }
            dos.flush();
            dos.close();
        } catch (FileNotFoundException ex) {
            
        } catch (IOException ex) {
            Logger.getLogger(EnvioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public static void main(String[] args) {
        EnvioDAO dao = new EnvioDAO();
        EmpleadoDAO empDao = new EmpleadoDAO();
        //System.out.println(dao.getEnvios());
        Envio e = new Envio(0, "Dirección Catro", "Población Uno", empDao.getEmpleado(2),
            new GregorianCalendar(2024,11, 20).getTime(), null, 24.56);
        dao.añadirEnvio(e);
        e = new Envio(0, "Dirección Cinco", "Población Dos", empDao.getEmpleado(3),
            new GregorianCalendar(2024,11, 1).getTime(), null, 55.6);
        dao.añadirEnvio(e);
        e = new Envio(0, "Dirección Seis", "Población Cuatro", empDao.getEmpleado(1),
            new GregorianCalendar(2025,0, 24).getTime(), null, 75);
        e = new Envio(0, "Dirección Siete", "Población Tres", empDao.getEmpleado(2),
            new GregorianCalendar(2024,0, 1).getTime(), null, 55.6);
        dao.añadirEnvio(e);
        e = new Envio(0, "Dirección Ocho", "Población Cuatrp", empDao.getEmpleado(3),
            new GregorianCalendar(2025,1, 14).getTime(), null, 75);
        e = new Envio(0, "Dirección Nueveo", "Población Cuatrp", empDao.getEmpleado(3),
            new GregorianCalendar(2025,1, 23).getTime(), null, 75);
        dao.añadirEnvio(e);
        dao.entregarEnvio(2, new Date());
        for(Envio env :envios){ 
            System.out.println(env);
        }
    }
    
}
