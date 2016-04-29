package nl.elstarit.bean;

import java.io.Serializable;
import java.util.Vector;

import nl.elstarit.language.Language;
import nl.elstarit.language.LanguageHandler;
import nl.elstarit.utils.JSFUtil;

import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.XSPUtil;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.xsp.model.domino.wrapped.DominoDocument;

public class UserPreference implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String BEAN_NAME = "userpreference";
    private static DominoDocument userDocument;
    private boolean isLoaded = false;
    
    public UserPreference(){
        
    }
    
    public void initiliaze() {
        if (!isLoaded) {
            this.loadUserDocument();
        } else {
            if (Boolean.parseBoolean(JSFUtil
                    .getQueryStringParameter("forceReload"))) {
                this.loadUserDocument();
            }
        }
    }
    
    public void loadUserDocument(){
        View vw = null;
        Document doc = null;
        try{
            String username = XSPUtil.getCurrentSession().getEffectiveUserName();
            
            //if(!username.toLowerCase().equals("anonymous")){
            vw = XSPUtil.getCurrentDatabase().getView("userpreferences");
            doc = vw.getFirstDocumentByKey(username, true);
            if(doc==null){
                Language defaultLanguage = LanguageHandler.get().getDefaultLanguage();
                doc = XSPUtil.getCurrentDatabase().createDocument();
                doc.replaceItemValue("Form", "userpreference");
                doc.replaceItemValue("username", username);
                doc.replaceItemValue("language", defaultLanguage.getCode() );
                doc.save();
            }
            //}
            if(doc!=null){
                this.setUserDocument(DominoDocument.wrap(doc.getParentDatabase().getFilePath(),Factory.toLotus(doc),null,null,false,null,null));
            }
            setLoaded(true);
        }catch(Exception e){
            XspOpenLogUtil.logError(e);
        }finally{
        }
    }
    
    public void updateLanguage(Language language){
        try{
            Document document = XSPUtil.wrap(userDocument.getDocument());
            if(JSFUtil.isRecycled(document)) {
                userDocument.restoreWrappedDocument();
            }
            userDocument.setValue("language", language.getCode());
            userDocument.save();
        }catch(Exception e){
            XspOpenLogUtil.logError(e);
        }
    }
    
    public void updateUserDocument(String field, String value){
        try{
            Document document = XSPUtil.wrap(userDocument.getDocument());
            if(JSFUtil.isRecycled(document)) {
                userDocument.restoreWrappedDocument();
            }
            
            userDocument.setValue(field, value);
            userDocument.save();
        }catch(Exception e){
            XspOpenLogUtil.logError(e);
        }
    }
    
    public void updateUserDocument(String field, Vector<String> value){
        try{
            Document document = XSPUtil.wrap(userDocument.getDocument());
            if(JSFUtil.isRecycled(document)) {
                userDocument.restoreWrappedDocument();
            }
            userDocument.setValue(field, value);
            userDocument.save();
        }catch(Exception e){
            XspOpenLogUtil.logError(e);
        }
    }
    
    public static Object getFieldValue(String field){
        try{
            Document document = XSPUtil.wrap(userDocument.getDocument());
            if(JSFUtil.isRecycled(document)) {
                userDocument.restoreWrappedDocument();
            }
            if(userDocument.hasItem(field)){
                return userDocument.getValue(field);
            }
            
        }catch(Exception e){
            XspOpenLogUtil.logError(e);
        }
        return null;
    }
    
    public static String getUniversalId(){
        try{
            Document document = XSPUtil.wrap(userDocument.getDocument());
            if(JSFUtil.isRecycled(document)) {
                userDocument.restoreWrappedDocument();
            }
            return userDocument.getDocument().getUniversalID();
            
        }catch(Exception e){
            //XspOpenLogUtil.logError(e);
        }
        return "";
    }
    
    public static String getUserLanguage(){
        String userLanguage = (String) getFieldValue("language");
        if(userLanguage == null){
            userLanguage = LanguageHandler.get().getDefaultLanguage().getCode();
        }
        return userLanguage;
    }
    
    public static UserPreference get() {
        return (UserPreference) JSFUtil.resolveVariable(BEAN_NAME);
    }
    
    public void setUserDocument(DominoDocument userDocument) {
        UserPreference.userDocument = userDocument;
    }
    
    public DominoDocument getUserDocument() {
        return userDocument;
    }
    
    public void setLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }
    
    public boolean isLoaded() {
        return isLoaded;
    }
}
