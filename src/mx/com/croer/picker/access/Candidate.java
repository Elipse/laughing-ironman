/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.access;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

/**
 *
 * @author elialva
 */
//@Entity
//@SqlResultSetMapping(name = "CandidateResult",
//        entities = {
//            @EntityResult(entityClass = mx.com.croer.picker.access.Candidate.class, fields = {
//                @FieldResult(name = "idItem", column = "idItem"),
//                @FieldResult(name = "context", column = "contexto")})}
//)
public class Candidate implements Serializable {

    private String context;
//    @Id
    private String type;
    private String idItem;

    /**
     * @return the context
     */
    public String getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(String context) {
        this.context = context;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the idItem
     */
    public String getIdItem() {
        return idItem;
    }

    /**
     * @param idItem the idItem to set
     */
    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    

}
