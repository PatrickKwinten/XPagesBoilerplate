package nl.elstarit.helper;

import java.io.Serializable;

import nl.elstarit.model.Configuration;
import nl.elstarit.utils.JSFUtil;

import org.openntf.domino.xsp.XspOpenLogUtil;

public class ConfigurationHandler implements Serializable {
    
    private static final long serialVersionUID = -1374960336981800423L;
    private static final String DEFAULT_OPENLOG_DB_PATH = "OpenLog.nsf";
    private static final String DEFAULT_SECUNDAIRNAB_DB_PATH = "secundaire_names.nsf";
    private static final String DEFAULT_DATE_FORMAT = "dd-MM-yyyy hh:mm";
    private static final String DEFAULT_TIME_FORMAT = "hh:mm:ss";
    private static final String BEAN_NAME = "configBean";
    
    private Configuration configuration = new Configuration();
    
    private boolean loadedCfg = false;
    
    /**********************
     * Constructor        *
     **********************/
    public ConfigurationHandler() {
        try {
            loadConfiguration();
            
        } catch (Exception e) {
            XspOpenLogUtil.logError(e);
        }
    }
    
    /**********************
     * Methods            *
     **********************/
    
    public void loadConfiguration(){
        try {
            if(!isLoadedCfg()){
                
                String logLocation = new String(DEFAULT_OPENLOG_DB_PATH);
                String appLocation = new String(DEFAULT_OPENLOG_DB_PATH);
                
                configuration.setApploglocation(appLocation);
                configuration.setOpenloglocation(logLocation);
                
                XspOpenLogUtil.getXspOpenLogItem().setLogDbName(configuration.getOpenloglocation());
                setLoadedCfg(true);
            }
        } catch (Exception e) {
            XspOpenLogUtil.logError(e);
        }
    }
    
    public Configuration findConfiguration(){
        loadConfiguration();
        return configuration;
    }
    
    /**********************
     * Getter and Setter *
     **********************/
    //access to the configuration bean
    public static ConfigurationHandler get() {
        return (ConfigurationHandler) JSFUtil.resolveVariable(BEAN_NAME);
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }
    
    public boolean isLoadedCfg() {
        return loadedCfg;
    }
    
    public void setLoadedCfg(boolean loadedCfg) {
        this.loadedCfg = loadedCfg;
    }
    
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
