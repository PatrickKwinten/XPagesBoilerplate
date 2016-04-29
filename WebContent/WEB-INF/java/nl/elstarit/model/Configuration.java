package nl.elstarit.model;


public class Configuration extends DataItem {
    private static final long serialVersionUID = -2324639401896131029L;
    
    private String openloglocation;
    private String apploglocation;
    private boolean debug=true;
    private boolean showLanguage=false;
    
    private String dateformat = new String("dd-MM-yyyy hh:mm");
    private String timeformat = new String("HH:mm:ss");
    
    public Configuration(){
        
    }
    
    public String getOpenloglocation() {
        return openloglocation;
    }
    
    public void setOpenloglocation(String openloglocation) {
        this.openloglocation = openloglocation;
    }
    
    public String getApploglocation() {
        return apploglocation;
    }
    
    public void setApploglocation(String apploglocation) {
        this.apploglocation = apploglocation;
    }
    
    public boolean isDebug() {
        return debug;
    }
    
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    public boolean isShowLanguage() {
        return showLanguage;
    }
    
    public void setShowLanguage(boolean showLanguage) {
        this.showLanguage = showLanguage;
    }
    
    
    public String getDateformat() {
        return dateformat;
    }
    
    public void setDateformat(String dateformat) {
        this.dateformat = dateformat;
    }
    
    public String getTimeformat() {
        return timeformat;
    }
    
    public void setTimeformat(String timeformat) {
        this.timeformat = timeformat;
    }
}
