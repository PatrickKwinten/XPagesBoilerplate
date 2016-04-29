package nl.elstarit.property;

public class Property {
    
    private String unid;
    private String label;
    private String code;
    private String value;
    private String dataType;
    
    public Property(){
        
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
    
    @Override
    public String toString(){
        return "label: " + getLabel()+", code: "+ getCode()+", value: "+getValue();
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
