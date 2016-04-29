package nl.elstarit.model;

import java.io.Serializable;

public abstract class DataItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private String _id;
    private String _rev;
    private String unid;
    private String docId;
    private String comments;
    private String status;
    private boolean editable = true;
    private boolean newNote = true;
    private String dataType;
    
    public void DateItem(){
        
    }
    
    public boolean isEditable() {
        return editable;
    }
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    public void setUnid(String unid) {
        this.unid = unid;
    }
    public String getUnid() {
        return unid;
    }
    public void setDocId(String docId) {
        this.docId = docId;
    }
    public String getDocId() {
        return docId;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public String getComments() {
        return comments;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    public boolean isNewNote() {
        return newNote;
    }
    public void setNewNote(boolean newNote) {
        this.newNote = newNote;
    }
    
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    
    public String getDataType() {
        return dataType;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    public String get_rev() {
        return _rev;
    }
    
}
