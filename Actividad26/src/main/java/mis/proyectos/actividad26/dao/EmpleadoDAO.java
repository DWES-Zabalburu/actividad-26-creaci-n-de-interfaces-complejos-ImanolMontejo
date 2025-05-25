/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mis.proyectos.actividad26.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mis.proyectos.actividad26.modelo.Empleado;

/**
 *
 * @author monte
 */
public class EmpleadoDAO {
    private static List<Empleado> empleados;
    
    private static final String FICH_EMPLEADOS = "empleados.csv";
    
    public EmpleadoDAO(){
        empleados = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(FICH_EMPLEADOS));
            String linea = br.readLine();
            linea = br.readLine();
            while (linea != null){
                String[] datos = linea.split(";");
                Integer id = Integer.parseInt(datos[0]);
                String nombre = datos[1];
                String apellidos = datos[2];
                Empleado e = new Empleado(id, nombre, apellidos);
                empleados.add(e);
                linea = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException ex) {
            
        } catch (IOException ex) {
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public List<Empleado> getEmpleados(){
        return empleados.stream()
                .sorted((e1,e2) -> (e1.getApellidos()+e1.getNombre())
                        .compareToIgnoreCase((e2.getApellidos()+e2.getNombre())))
                .toList();
    }
    
    public Empleado getEmpleado(int id){
        return empleados.stream()
                .filter(e -> e.getIdEmpleado() == id)
                .findFirst()
                .orElse(null);
    }
 
    public static void main(String[] args) {
        EmpleadoDAO dao = new EmpleadoDAO();
        for(Empleado e : dao.getEmpleados()){
            System.out.println(e);
        }
    }
}
