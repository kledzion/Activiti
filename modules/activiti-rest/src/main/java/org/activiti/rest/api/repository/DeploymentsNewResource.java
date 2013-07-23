/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.rest.api.repository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.DeploymentQueryProperty;
import org.activiti.engine.impl.ProcessInstanceQueryProperty;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.persistence.entity.ResourceEntityManager;
import org.activiti.engine.query.QueryProperty;
import org.activiti.engine.repository.*;
import org.activiti.explorer.reporting.ReportingUtil;
import org.activiti.rest.api.ActivitiUtil;
import org.activiti.rest.api.DataResponse;
import org.activiti.rest.api.SecuredResource;
import org.activiti.rest.application.ActivitiRestServicesApplication;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

/**
 * @author kledzion
 */
public class DeploymentsNewResource extends SecuredResource {
  
    protected transient RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
      
//    @Get
//    public String getDeploymentsNew() {
//        if(authenticate() == false) return null;
//       
//        ActivitiUtil.getRuntimeService().createNativeExecutionQuery().sql("update act_re_deployment set name_='heheh' where id_='5751'").singleResult();
//                
//        return "OK GET METHOD";
//    }

    @Post
    public DeploymentsNewResponse createDeploymentsNew(DeploymentsNewRequest request) {
        try {
            if(authenticate() == false) return null;
            
            
            Model modelData = repositoryService.getModel(request.getModelId());
            if (modelData == null) {
                getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
                
                return null;
            }
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);
       
            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment()
                .name(modelData.getName())
                .addString(processName, new String(bpmnBytes))
                .deploy();
            
            modelData.setDeploymentId(deployment.getId());
                    
            repositoryService.saveModel(modelData);

            getResponse().setStatus(Status.SUCCESS_CREATED);
            
            return new DeploymentsNewResponse(deployment.getId());
            
        } catch (Exception ex) {
            Logger.getLogger(DeploymentsNewResource.class.getName()).log(Level.SEVERE, null, ex);
            
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
        }
       
        return null;
    }
}
