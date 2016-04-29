package nl.elstarit.property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;

import nl.elstarit.bean.UserPreference;
import nl.elstarit.language.Language;
import nl.elstarit.language.LanguageHandler;
import nl.elstarit.utils.JSFUtil;

import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.utils.XSPUtil;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.xsp.model.domino.wrapped.DominoDocument;

public class PropertyHandler implements Serializable {
    private static final long serialVersionUID = 1L;
    private static String bundleVar = "en_basic";
    private static final String BEAN_NAME = "propertyHandler";
    private TreeMap<String,Property> properties = new TreeMap<String,Property>();
    private boolean propertiesLoaded = false;
    
    public PropertyHandler(){
        
    }
    
    public void initiliaze(){
        if(!propertiesLoaded){
            loadPropertiesFromView();
        }
    }
    
    public static PropertyHandler get() {
        return (PropertyHandler) JSFUtil.resolveVariable(BEAN_NAME);
    }
    
    public void loadPropertiesFromView(){
        try{
            if(properties != null){
                properties.clear();
            }else{
                properties = new TreeMap<String,Property>();
            }
            View vwProperties = XSPUtil.getCurrentDatabase().getView("properties");
            ViewEntryCollection collection = vwProperties.getAllEntries();
            for(ViewEntry entryProperty: collection){
                Document docProperty = entryProperty.getDocument();
                String key = docProperty.getItemValueString("key")+"."+docProperty.getItemValueString("code").toLowerCase();
                
                if(!properties.containsKey(key)){
                    addProperty(docProperty, key);
                }
            }
            setPropertiesLoaded(true);
        }catch(Exception e){
            XspOpenLogUtil.logError(e);
        }
    }
    
    public void loadPropertiesFromFile(){
        try{
            ResourceBundle bundle;
            bundle = JSFUtil.getXSPContext().bundle(bundleVar);
            if (bundle != null) {
                for(String key: bundle.keySet()){
                    for(Language language : LanguageHandler.get().getLanguages().values()){
                        String languageKey = key+"."+language.getCode().toLowerCase();
                        if(!properties.containsKey(languageKey)){
                            createPropertyDocument(key, language.getCode(), bundle.getString(key));
                            addProperty(key, bundle.getString(key), language.getCode());
                        }
                    }
                }
                XSPUtil.getCurrentDatabase().getView("properties").refresh();
            }
        }catch(Exception e){
            XspOpenLogUtil.logError(e);
        }
    }
    
    public boolean checkIfPropertyExists(String key){
        return properties.containsKey(key);
    }
    
    public void createPropertyDocument(String key, String code, String value){
        Document docProperty = XSPUtil.getCurrentDatabase().createDocument();
        docProperty.replaceItemValue("Form","property");
        docProperty.replaceItemValue("key", key);
        docProperty.replaceItemValue("code", code);
        docProperty.replaceItemValue("value", value);
        
        docProperty.save();
    }
    
    public void createPropertyDocument(Property property){
        Document docProperty = XSPUtil.getCurrentDatabase().createDocument();
        docProperty.replaceItemValue("Form","property");
        docProperty.replaceItemValue("key", property.getLabel());
        docProperty.replaceItemValue("code", property.getCode());
        docProperty.replaceItemValue("value", property.getValue());
        
        docProperty.save();
    }
    
    public List<Property> findProperties(){
        if(properties == null || properties.isEmpty()){
            loadPropertiesFromView();
        }
        return new ArrayList<Property>(properties.values());
    }
    
    public Property findPropertyById(String propertyUnid){
        if(properties == null || properties.isEmpty()){
            loadPropertiesFromView();
        }
        for(Property property : properties.values()){
            if(propertyUnid.equals(property.getUnid())){
                return property;
            }
        }
        
        return null;
    }
    
    public void addNewLanguageProperties(DominoDocument dominodocument){
        try {
            Document docLanguage = XSPUtil.wrap(dominodocument.getDocument());
            ResourceBundle bundle = JSFUtil.getXSPContext().bundle(bundleVar);
            if (bundle != null) {
                for(String key: bundle.keySet()){
                    String languageKey = key+"."+docLanguage.getItemValueString("code").toLowerCase();
                    if(!properties.containsKey(languageKey)){
                        createPropertyDocument(key, docLanguage.getItemValueString("code"), bundle.getString(key));
                        addProperty(key, bundle.getString(key), docLanguage.getItemValueString("code").toLowerCase());
                    }
                }
            }
        } catch (Exception e) {
            XspOpenLogUtil.logError(e);
        }
    }
    
    private void addProperty(Document docProperty, String key){
        String value = docProperty.getItemValueString("value");
        Property property = new Property();
        property.setUnid(docProperty.getUniversalID());
        property.setLabel(key);
        property.setValue(value);
        property.setCode(docProperty.getItemValueString("code"));
        property.setDataType(docProperty.getItemValueString("form"));
        properties.put(key, property);
    }
    
    public void addProperty(Property property){
        properties.put(property.getLabel(), property);
    }
    
    public void addProperty(String key, String value, String code){
        String languageKey = key+"."+code.toLowerCase();
        Property property = new Property();
        property.setLabel(key);
        property.setValue(value);
        property.setCode(code);
        property.setDataType("property");
        properties.put(languageKey, property);
    }
    
    public boolean hasProperty(String key, String code){
        return properties.containsKey(key+"."+code.toLowerCase());
    }
    
    public void updateProperty(DominoDocument dominodocument){
        Document docProperty = XSPUtil.wrap(dominodocument.getDocument());
        String key = docProperty.getItemValueString("key")+"."+docProperty.getItemValueString("code").toLowerCase();
        if(properties.containsKey(key)){
            String value = docProperty.getItemValueString("value");
            properties.get(key).setValue(value);
        }
    }
    
    public void removeProperty(String key){
        if(properties.containsKey(key)){
            properties.remove(key);
        }
    }
    
    public String getPropertyValue(String subkey){
        String key = subkey +"."+UserPreference.getUserLanguage().toLowerCase();
        if(properties.containsKey(key)){
            return properties.get(key).getValue();
        }
        return "";
    }
    
    public void setProperties(TreeMap<String,Property> properties) {
        this.properties = properties;
    }
    
    public TreeMap<String,Property> getProperties() {
        return properties;
    }
    
    public void setPropertiesLoaded(boolean propertiesLoaded) {
        this.propertiesLoaded = propertiesLoaded;
    }
    
    public boolean isPropertiesLoaded() {
        return propertiesLoaded;
    }
}
