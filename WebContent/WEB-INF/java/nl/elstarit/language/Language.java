package nl.elstarit.language;

import java.io.Serializable;

public class Language implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String unid;
    private String code;
    private String name;
    private boolean def;
    private String dataType;
    
    public Language(){
        
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setDef(boolean def) {
        this.def = def;
    }
    
    public boolean isDef() {
        return def;
    }
    
    @Override
    public String toString(){
        return "code: "+getCode()+", name: "+ getName()+", default: "+ isDef();
    }
    
    public String getCodeAndName(){
        return getName() + "|"+getCode();
    }
    
    public void setUnid(String unid) {
        this.unid = unid;
    }
    
    public String getUnid() {
        return unid;
    }
    
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    
    public String getDataType() {
        return dataType;
    }
}
