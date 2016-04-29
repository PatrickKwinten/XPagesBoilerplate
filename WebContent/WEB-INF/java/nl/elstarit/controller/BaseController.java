package nl.elstarit.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

import nl.elstarit.utils.JSFUtil;

public class BaseController implements Serializable{
    private static final long serialVersionUID = 1L;
    
    public BaseController(){
        ApplicationController.get().clearSearchValue();
    }
    
    public String getUniqueID(){
        return JSFUtil.getUniqueID();
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
    
}
