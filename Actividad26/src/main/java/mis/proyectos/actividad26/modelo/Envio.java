/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mis.proyectos.actividad26.modelo;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author monte
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Envio {    
    @EqualsAndHashCode.Include
    private int id;
    private String direccionEntrega;
    private String poblacion;
    private Empleado empleado;
    private Date fechaEnvio;
    private Date fechaEntrega;
    private double coste;

}