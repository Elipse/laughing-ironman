/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package creabusqueda.newpackage;

import creabusqueda.ItembusqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author elialva
 */
@Service
public class ServiCua {

    private final ItembusqRepository itembusqRepository;

    @Autowired
    public ServiCua(ItembusqRepository itembusqRepository) {
        this.itembusqRepository = itembusqRepository;
    }

    /**
     * @return the itembusqRepository
     */
    public ItembusqRepository getItembusqRepository() {

        return itembusqRepository;
    }

}
