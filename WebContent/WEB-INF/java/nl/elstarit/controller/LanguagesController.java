package nl.elstarit.controller;

import java.util.Iterator;

import nl.elstarit.dao.DominoDAO;
import nl.elstarit.language.LanguageHandler;
import nl.elstarit.property.PropertyHandler;
import nl.elstarit.utils.JSFUtil;

import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.utils.XSPUtil;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.xsp.component.UIViewPanel;

public class LanguagesController extends BaseViewController {
    private static final long serialVersionUID = 1L;
    
    public LanguagesController(){
        
    }
    
    public void remove() {
        String[] docIDArray = ((UIViewPanel) JSFUtil.findComponent("viewPanel1")).getSelectedIds();
        try {
            if (docIDArray.length > 0) {
                for (int i = 0; i < docIDArray.length; i++) {
                    String docId = docIDArray[i];
                    Document doc = DominoDAO.findDocumentByID(docId);
                    if (doc != null) {
                        //remove language from language list
                        LanguageHandler.get().removeLanguage(doc);
                        
                        //remove all the properties associated with this language
                        removePropertiesAssociatedWithLanguage(doc.getItemValueString("code"));
                        //now remove language
                        DominoDAO.removeDocument(doc);
                    }
                }
                XSPUtil.getCurrentDatabase().getView("languages").refresh();
            }
            
        } catch (Exception e) {
            XspOpenLogUtil.logError(e);
        } finally {
            
        }
    }
    
    public void makeDefault(){
        String[] docIDArray = ((UIViewPanel) JSFUtil.findComponent("viewPanel1"))
        .getSelectedIds();
        try {
            if (docIDArray.length > 0) {
                String docId = docIDArray[0];
                View vwLanguage = XSPUtil.getCurrentDatabase().getView("languages");
                for(ViewEntry entry : vwLanguage.getAllEntries()){
                    Document doc = entry.getDocument();
                    if(doc.getNoteID().equals(docId)){
                        doc.replaceItemValue("isdefault", "1");
                    }else{
                        doc.replaceItemValue("isdefault", "0");
                    }
                    doc.save();
                    
                    LanguageHandler.get().updateLanguage(doc);
                }
                vwLanguage.refresh();
            }
            
            
        } catch (Exception e) {
            XspOpenLogUtil.logError(e);
        } finally {
            
        }
    }
    
    private void removePropertiesAssociatedWithLanguage(String languageCode){
        try{Document docProperty = null;
        View vw = XSPUtil.getCurrentDatabase().getView("propertiesByCode");
        ViewNavigator nav = vw.createViewNavFromCategory(languageCode);
        if(nav.getCount()>0){
            for (Iterator<ViewEntry> it = nav.iterator(); it.hasNext();){
                docProperty = it.next().getDocument();
                String propertyKey = docProperty.getItemValueString("key")+"."+ languageCode.toLowerCase();
                //remove property from property list
                PropertyHandler.get().removeProperty(propertyKey);
                // now remove property
                DominoDAO.removeDocument(docProperty);
            }
        }
        }catch (Exception e) {
            XspOpenLogUtil.logError(e);
        }
    }
    
}
