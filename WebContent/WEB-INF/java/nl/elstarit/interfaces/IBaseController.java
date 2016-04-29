package nl.elstarit.interfaces;

import java.util.Vector;

import javax.faces.component.UIComponent;

import org.openntf.domino.Document;

public interface IBaseController {
    
    public abstract Object getUIComponentValue(String compId);
    
    public abstract void copyDocuments();
    
    public abstract void setViewPanelComponent(UIComponent viewPanelComponent);
    
    public abstract UIComponent getViewPanelComponent();
    
}