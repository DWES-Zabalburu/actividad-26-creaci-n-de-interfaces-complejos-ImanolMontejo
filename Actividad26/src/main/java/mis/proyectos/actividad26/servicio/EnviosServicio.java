/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mis.proyectos.actividad26.servicio;

import java.util.Date;
import java.util.List;
import mis.proyectos.actividad26.dao.EmpleadoDAO;
import mis.proyectos.actividad26.dao.EnvioDAO;
import mis.proyectos.actividad26.modelo.Empleado;
import mis.proyectos.actividad26.modelo.Envio;

/**
 *
 * @author monte
 */
public class EnviosServicio {
    private static EmpleadoDAO empDAO = new EmpleadoDAO();
    private static EnvioDAO enviosDAO = new EnvioDAO();
    
    public List<Empleado> getEmpleados(){
        return empDAO.getEmpleados();
    }
    
    public Empleado getEmpleado(int id){
        return empDAO.getEmpleado(id);
    }
    
    public List<Envio> getEnvios(){
        return enviosDAO.getEnvios();
    }
    
    public Envio getEnvio(int id){
        return enviosDAO.getEnvio(id);
    }
    
    public Envio añadirEnvio(Envio e){
        return enviosDAO.añadirEnvio(e);
    }
    
    public void entregarEnvio(int idEnvio, Date fecha){
        enviosDAO.entregarEnvio(idEnvio, fecha);
    }
}
