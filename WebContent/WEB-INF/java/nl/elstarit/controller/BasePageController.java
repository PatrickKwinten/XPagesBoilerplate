package nl.elstarit.controller;

import java.util.List;
import java.util.UUID;
import java.util.Vector;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

import nl.elstarit.utils.JSFUtil;

import com.ibm.xsp.model.domino.wrapped.DominoDocument;

public class BasePageController extends BaseController{
    private static final long serialVersionUID = 1L;
    protected DominoDocument uidoc = null;
    protected String datasourceName = "";
    private String action = "";
    private String documentId = "";

    public BasePageController(){
        action = (String) JSFUtil.getQueryStringParameter("action");
        documentId = (String) JSFUtil.getQueryStringParameter("documentId");
    }
    
    public DominoDocument getCurrentDocument() {
        return getCurrentDocument("currentDocument");
    }
    
    public DominoDocument getCurrentDocument(String datasourceName) {
            DominoDocument dominoDoc = (DominoDocument) JSFUtil
                            .getVariableValue(datasourceName);
            return dominoDoc;
    }

    public String getUniqueID(){
        return UUID.randomUUID().toString();
    }
    
    /* (non-Javadoc)
     * @see nl.elstarit.controller.BaseControllerInterface#getUIComponentValue(java.lang.String)
     */
    public Object getUIComponentValue(String compId){
        if(JSFUtil.getSubmittedValue(compId)!=null){
            return JSFUtil.getSubmittedValue(compId);
        }
        return new String("");
    }

    public void clearFields(Vector<String> fieldIds){
        List<String> fieldIdsList = JSFUtil.convertVectorToList(fieldIds);
        for(String fieldId : fieldIdsList){
            UIComponent fldToClear = JSFUtil.findComponent(fieldId);
            if(fldToClear!= null){
                ((UIOutput) fldToClear).setValue("");
            }
            
        }
    }

    public boolean isEditMode(String action){
        if("openDocument".equals(action)){
            return false;
        }
        return true;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

}
