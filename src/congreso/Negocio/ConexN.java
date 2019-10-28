/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package congreso.Negocio;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.swing.JOptionPane;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 *
 * @author anton
 */
public class ConexN {
    public final EntityManager entity(){
        try{
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("congresoPU");
        return emf.createEntityManager();
    }catch(PersistenceException|DatabaseException ex){
        JOptionPane.showMessageDialog(null, "No hay conexion a la base de datos");
        System.exit(0);
    }
        return null;
    }
}
