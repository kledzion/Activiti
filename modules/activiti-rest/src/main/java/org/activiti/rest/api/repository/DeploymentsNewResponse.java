/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.activiti.rest.api.repository;

/**
 *
 * @author kledzion
 */
public class DeploymentsNewResponse {
    
    protected String id;

    public DeploymentsNewResponse() {
    }

    public DeploymentsNewResponse(String id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    
    
}
