/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.activiti.rest.api.repository;

/**
 *
 * @author kledzion
 */
public class DeploymentsNewRequest {
    
    protected String modelId;

    public DeploymentsNewRequest() {
    }

    public DeploymentsNewRequest(String modelId) {
        this.modelId = modelId;
    }
    
    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelId() {
        return modelId;
    }
    
}
