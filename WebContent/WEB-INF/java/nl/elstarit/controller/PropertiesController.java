package nl.elstarit.controller;

import nl.elstarit.dao.DominoDAO;
import nl.elstarit.property.PropertyHandler;
import nl.elstarit.utils.JSFUtil;

import org.openntf.domino.Document;
import org.openntf.domino.utils.XSPUtil;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.xsp.component.UIViewPanel;

public class PropertiesController extends BaseViewController {
    private static final long serialVersionUID = 1L;
    
    public PropertiesController(){
        
    }
    
    public void removeProperties() {
        String[] docIDArray = ((UIViewPanel) JSFUtil.findComponent("viewPanel1"))
        .getSelectedIds();
        try {
            if (docIDArray.length > 0) {
                for (int i = 0; i < docIDArray.length; i++) {
                    String docId = docIDArray[i];
                    Document docProperty = DominoDAO.findDocumentByID(docId);
                    if (docProperty != null) {
                        //remove property from property list
                        String propertyKey = docProperty.getItemValueString("key")+"."+ docProperty.getItemValueString("code").toLowerCase();
                        PropertyHandler.get().removeProperty(propertyKey);
                        // now remove property
                        DominoDAO.removeDocument(docProperty);
                    }
                }
                XSPUtil.getCurrentDatabase().getView("properties").refresh();
            }
            
        } catch (Exception e) {
            XspOpenLogUtil.logError(e);
        } finally {
            
        }
    }
    
}
