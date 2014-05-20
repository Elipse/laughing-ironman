/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package creabusqueda;

import mx.com.croer.entities.busqueda.Itembusq;
import mx.com.croer.entities.busqueda.ItembusqPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author elialva
 */

public interface ItembusqRepository extends JpaRepository<Itembusq, ItembusqPK>{
    
}
