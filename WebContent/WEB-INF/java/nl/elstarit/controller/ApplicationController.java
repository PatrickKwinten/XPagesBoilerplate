package nl.elstarit.controller;

import java.io.Serializable;

import nl.elstarit.bean.UserPreference;
import nl.elstarit.helper.ConfigurationHandler;
import nl.elstarit.language.LanguageHandler;
import nl.elstarit.property.PropertyHandler;
import nl.elstarit.utils.JSFUtil;

public class ApplicationController implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String BEAN_NAME = "app";
    private transient String searchValue = "";
    
    public ApplicationController(){
        
    }
    
    public void loader(){
        //step 1 - load configuration
        ConfigurationHandler.get().loadConfiguration();
        
        //step 2 - load languages from view
        LanguageHandler.get().loadLanguage();
        
        //step 3 - load userpreferences
        UserPreference.get().initiliaze();
        
        //step 4 - load the propertiesFromView in the PropertyHandler
        PropertyHandler.get().initiliaze();
    }
    
    public static ApplicationController get() {
        return (ApplicationController) JSFUtil.resolveVariable(BEAN_NAME);
    }
    
    public void clearSearchValue(){
        setSearchValue("");
    }
    
    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }
    
    public String getSearchValue() {
        return searchValue;
    }
    
}
