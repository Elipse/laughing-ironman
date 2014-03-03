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
//                @FieldResult(name = "idBean", column = "idBean"),
//                @FieldResult(name = "context", column = "contexto")})}
//)
public class Candidate implements Serializable {

    private String context;
//    @Id
    private Integer idBean;

    public void setIdBean(Integer idBean) {
        this.idBean = idBean;
    }

    public void setContext(String context) {
        this.context = context;

    }

    public String getContext() {
        return context;
    }

    public Object getIdBean() {
        return idBean;
    }

}
