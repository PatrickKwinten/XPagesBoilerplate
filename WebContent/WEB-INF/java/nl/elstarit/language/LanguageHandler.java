package nl.elstarit.language;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;

import nl.elstarit.bean.UserPreference;
import nl.elstarit.property.PropertyHandler;
import nl.elstarit.utils.JSFUtil;

import org.openntf.domino.Document;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.utils.XSPUtil;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.xsp.model.domino.wrapped.DominoDocument;

public class LanguageHandler implements Serializable {
    private static final String DEFAULT_LANGUAGE = "en_basic";
    private static final long serialVersionUID = 1L;
    private String bundleVar = "";
    private static final String BEAN_NAME = "language";
    private TreeMap<String,Language> languages = new TreeMap<String,Language>();
    private boolean loaded = false;
    
    public LanguageHandler(){
        
    }
    
    public void loadLanguage() {
        try {
            if(!isLoaded()){
                loadLanguagesFromView();
                this.setBundleVar(DEFAULT_LANGUAGE);
            }
        } catch (Exception e) {
            // XspOpenLogUtil.logError(e);
        }
    }
    
    public void loadLanguagesFromView(){
        try{
            if(languages != null){
                languages.clear();
            }else{
                languages = new TreeMap<String,Language>();
            }
            View vwProperties = XSPUtil.getCurrentDatabase().getView("languages");
            ViewEntryCollection collection = vwProperties.getAllEntries();
            for(ViewEntry entryProperty: collection){
                Document docProperty = entryProperty.getDocument();
                addLanguage(docProperty);
            }
            setLoaded(true);
        }catch(Exception e){
            XspOpenLogUtil.logError(e);
        }
    }
    
    public List<Language> findLanguages(){
        if(languages == null || languages.isEmpty()){
            loadLanguagesFromView();
        }
        return new ArrayList<Language>(languages.values());
    }
    
    public Language findLanguageById(String languageUnid){
        if(languages == null || languages.isEmpty()){
            loadLanguagesFromView();
        }
        for(Language language : languages.values()){
            if(languageUnid.equals(language.getUnid())){
                return language;
            }
        }
        
        return null;
    }
    
    public String getLanguageString(String key) {
        try {
            if (PropertyHandler.get().isPropertiesLoaded()) {
                // is loaded so get my label from the propertyHandler
                
                return PropertyHandler.get().getPropertyValue(key);
            } else {
                ResourceBundle bundle;
                bundle = JSFUtil.getXSPContext().bundle(getBundleVar());
                if (bundle != null) {
                    
                    return bundle.getString(key);
                }
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }
    
    public void addLanguage(Document docLanguage){
        String key = docLanguage.getItemValueString("code");
        if(!languages.containsKey(key)){
            Language language = new Language();
            language.setCode(key);
            language.setUnid(docLanguage.getUniversalID());
            language.setName(docLanguage.getItemValueString("name"));
            language.setDef(JSFUtil.parseBool(docLanguage.getItemValueString("isdefault")));
            language.setDataType(docLanguage.getItemValueString("form"));
            languages.put(key, language);
        }
    }
    
    public void addLanguage(String code, String name){
        Language language = new Language();
        language.setCode(code);
        language.setName(name);
        language.setDef(false);
        language.setDataType("language");
        languages.put(code, language);
    }
    
    public void addLanguage(Language language){
        languages.put(language.getCode(), language);
    }
    
    public void updateLanguage(DominoDocument dominodocument){
        Document docLanguage = XSPUtil.wrap(dominodocument.getDocument());
        updateLanguage(docLanguage);
    }
    
    public void updateLanguage(Document docLanguage){
        String key = docLanguage.getItemValueString("code");
        if(languages.containsKey(key)){
            languages.get(key).setName(docLanguage.getItemValueString("name"));
            languages.get(key).setDef(JSFUtil.parseBool(docLanguage.getItemValueString("isdefault")));
        }else{
            addLanguage(docLanguage);
        }
        
    }
    
    public void removeLanguage(Document docLanguage){
        String key = docLanguage.getItemValueString("code");
        if(languages.containsKey(key)){
            languages.remove(key);
        }
    }
    
    public String getLanguageCode(String languageName){
        for(Language language : languages.values()){
            if(languageName.toLowerCase().equals(language.getName().toLowerCase())){
                return language.getCode();
            }
        }
        return "";
    }
    
    public boolean checkIfLanguageExists(String code){
        return languages.containsKey(code);
    }
    
    public void createLanguageDocument(String code, String language){
        Document newLanguage = XSPUtil.getCurrentDatabase().createDocument();
        
        newLanguage.replaceItemValue("Form", "language");
        newLanguage.replaceItemValue("code", code);
        newLanguage.replaceItemValue("name", language);
        
        newLanguage.save();
    }
    
    public void addLanguageDocument(Language language){
        Document newLanguage = XSPUtil.getCurrentDatabase().createDocument();
        
        newLanguage.replaceItemValue("Form", "language");
        newLanguage.replaceItemValue("code", language.getCode());
        newLanguage.replaceItemValue("name", language.getName());
        
        newLanguage.save();
    }
    /**********************
     * Getter and Setter *
     **********************/
    public Language getDefaultLanguage(){
        for(Language language: languages.values()){
            if(language.isDef()){
                return language;
            }
        }
        return null;
    }
    
    public Language getLanguage(String languageCode){
        if(languages.containsKey(languageCode)){
            return languages.get(languageCode);
        }
        
        return getDefaultLanguage();
    }
    
    public boolean showMenuDropDown(){
        return languages.size()>0;
    }
    
    public List<Language> getLanguagesList(){
        return new ArrayList<Language>(languages.values());
    }
    
    public List<String> getLanguagesCodeAndNameList(){
        List<String> list = new ArrayList<String>();
        for(Language language : languages.values()){
            list.add(language.getCodeAndName());
        }
        return list;
    }
    
    @SuppressWarnings("static-access")
    public List<Language> getDropdownList(){
        List<Language> list = new ArrayList<Language>();
        String defaultLanguage = UserPreference.get().getUserLanguage();
        for(Language language: languages.values()){
            if(!defaultLanguage.equals(language.getCode())){
                list.add(language);
            }
        }
        
        return list;
    }
    //access to the bean
    public static LanguageHandler get() {
        return (LanguageHandler) JSFUtil.resolveVariable(BEAN_NAME);
    }
    
    public void setBundleVar(String bundleVar) {
        this.bundleVar = bundleVar;
    }
    
    public String getBundleVar() {
        return bundleVar;
    }
    
    public void setLanguages(TreeMap<String,Language> languages) {
        this.languages = languages;
    }
    
    public TreeMap<String,Language> getLanguages() {
        return languages;
    }
    
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
    
    public boolean isLoaded() {
        return loaded;
    }
}
