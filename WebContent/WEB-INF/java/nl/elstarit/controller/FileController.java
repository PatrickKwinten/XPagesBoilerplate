package nl.elstarit.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import nl.elstarit.language.LanguageHandler;
import nl.elstarit.property.PropertyHandler;
import nl.elstarit.utils.JSFUtil;

import org.apache.commons.lang.StringUtils;
import org.openntf.domino.Document;
import org.openntf.domino.utils.XSPUtil;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.xsp.component.UIFileuploadEx.UploadedFile;
import com.ibm.xsp.webapp.XspHttpServletResponse;

public class FileController  extends BaseController {
    private static final long serialVersionUID = 1L;
    private static String DEFAULT_PROPERTIES_FILE = "en_basic";
    private UploadedFile uploadFile;
    
    public FileController(){
        
    }
    
    
    public void importPropertyFile(){
        BufferedReader br = null;
        FileInputStream fis = null;
        try {
            if(uploadFile != null){
                String attachmentName = uploadFile.getUploadedFile().getClientFileName();
                
                attachmentName = attachmentName.substring(0, attachmentName.length()-4).trim();
                String[] attachmentNameSplit = attachmentName.split("_");
                String code = attachmentNameSplit[0].toUpperCase();
                String language = StringUtils.capitalize(attachmentNameSplit[1]);
                
                handleLanguage(language, code);
                fis = new FileInputStream(uploadFile.getUploadedFile().getServerFile());
                br = new BufferedReader(new InputStreamReader(fis));
                String line = null;
                int row = 0;
                while ((line = br.readLine()) != null) {
                    System.out.print(line);
                    if(!"".equals(line)){
                        if(!line.startsWith("#")){
                            String[] lineSplit = line.split("=");
                            String key = lineSplit[0];
                            String value = lineSplit[1];
                            if(!PropertyHandler.get().hasProperty(key, code)){
                                //is not present yet, so create and add property
                                //this way we can run in it multiple time without running in trouble
                                
                                Document docProperty = XSPUtil.getCurrentDatabase().createDocument();
                                docProperty.replaceItemValue("form", "property");
                                docProperty.replaceItemValue("code", code);
                                docProperty.replaceItemValue("language", language);
                                docProperty.replaceItemValue("key", key);
                                docProperty.replaceItemValue("value", value);
                                
                                docProperty.save();
                                
                                //add to Properties list
                                PropertyHandler.get().addProperty(key, value, code);
                                
                                row += 1;
                            }
                        }
                    }
                }
                JSFUtil.addMessage(FacesMessage.SEVERITY_INFO, LanguageHandler.get().getLanguageString("message.import.properties.total") + (row));
            }
            
        } catch (Exception e) {
            XspOpenLogUtil.logError(e);
        } finally{
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    XspOpenLogUtil.logError(e);
                }
            }
            
        }
    }
    
    public void exportDefaultPropertyFile(){
        List<String> lines = new ArrayList<String>();
        XspHttpServletResponse response = null;
        PrintWriter pw = null;
        StringBuilder sb = new StringBuilder();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try{
            ExternalContext extCon = facesContext.getExternalContext();
            response = (XspHttpServletResponse) extCon.getResponse();
            pw = response.getWriter();
            
            //set up output object
            response.setContentType("text/plain");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Content-Disposition", "attachment; filename=default_properties.txt");
            response.setDateHeader("Expires", -1);
            
            int row = 0;
            ResourceBundle bundle = JSFUtil.getXSPContext().bundle(DEFAULT_PROPERTIES_FILE);
            if (bundle != null) {
                Enumeration <String> keys = bundle.getKeys();
                while (keys.hasMoreElements()) {
                    String key = keys.nextElement();
                    String value = bundle.getString(key);
                    lines.add(key+"="+value);
                    row += 1;
                }
            }
            
            //sort
            Collections.sort(lines, new Comparator<String>() {
                public int compare(String s1, String s2) {
                    return s1.compareToIgnoreCase(s2);
                }
            });
            
            //put together
            for(String line : lines){
                sb.append(line);
                sb.append(System.getProperty("line.separator"));
            }
            
            pw.print(sb.toString());
            
            response.commitResponse();
        } catch (Exception e) {
            XspOpenLogUtil.logError(e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            
            pw.print("Error:"+e.getMessage());
            
            try {
                response.commitResponse();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally{
            facesContext.responseComplete();
        }
        
    }
    
    private void handleLanguage(String language, String code){
        try{
            if(!LanguageHandler.get().checkIfLanguageExists(code)){
                //create language document
                LanguageHandler.get().createLanguageDocument(code, language);
                //add language to the Languages list
                LanguageHandler.get().addLanguage(code, language);
            }
        } catch (Exception e) {
            XspOpenLogUtil.logError(e);
        } finally{
            
        }
        
    }
    
    public void setUploadFile(UploadedFile uploadFile) {
        this.uploadFile = uploadFile;
    }
    public UploadedFile getUploadFile() {
        return uploadFile;
    }
    
}
