/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.access;

import java.util.List;
import java.util.Map;

/**
 *
 * @author elialva
 */
public interface Candidate {

    public List<Map<String,Object>> delimit(String input, String entity);

}
