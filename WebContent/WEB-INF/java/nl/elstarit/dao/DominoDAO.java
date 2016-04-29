package nl.elstarit.dao;

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.utils.XSPUtil;
import org.openntf.domino.xsp.XspOpenLogUtil;

public class DominoDAO {
    private static final String VIEW_WORDLISTITEMBYWORDLISTID = "wordlistitemByWordlistId";
    private static final String VIEW_WORDLISTITEMBYDOCID = "wordlistitemByDocId";
    private static final String VIEW_USERWORDLISTITEMBYWORDLISTID = "userwordlistitemByUserWordlistId";
    private static final String VIEW_USERS = "users";
    private static final String VIEW_USERS_BY_EMAIL = "usersByEmail";
    private static final String VIEW_USERS_BY_DOCID = "usersByDocId";
    private static final String VIEW_DOCUMENT_BY_DOCID = "documentByDocId";
    
    private static final String VIEW_USERS_WORDLISTS = "accountuserwordlists";
    private static final String VIEW_USERS_WORDLISTS_BY_USERID = "userwordlistsByUserId";
    private static final String VIEW_USERS_WORDLISTS_BY_DOCID = "userwordlistsByDocId";
    private static final String VIEW_USERS_WORDLISTS_BY_USERID_AND_WORDLISTID = "userwordlistsByUserIdAndWordListId";
    private static final String VIEW_USERS_WORDLISTS_BY_USERID_AND_WORDLISTID_RUN = "userwordlistitemByUserWordlistIdRun";
    private static final String VIEW_WORDLISTS = "wordlists";
    private static final String VIEW_WORDLIST_BY_DOCID = "wordlistsByDocid";
    private static final String VIEW_ACCOUNTS = "accounts";
    private static final String VIEW_ACCOUNTS_BY_DOCID = "accountsByDocId";
    
    private static final String VIEW_SETTINGS = "settings";
    
    
    
    
    public static Document findDocumentByID(String documentId){
        Document doc = null;
        Database db = null;
        try{
            db = XSPUtil.getCurrentDatabase();
            doc = db.getDocumentByID(documentId);
            
        }catch(Exception e){
            XspOpenLogUtil.logError(e);
        }
        return doc;
    }
    
    public static Document findDocumentByUNID(String documentId){
        Document doc = null;
        Database db = null;
        try{
            db = XSPUtil.getCurrentDatabase();
            doc = db.getDocumentByUNID(documentId);
            
        }catch(Exception e){
            XspOpenLogUtil.logError(e);
        }
        return doc;
    }
    
    
    public static void copyDocument(Document doc){
        Document docCopy = null;
        Database db = null;
        try{
            db = XSPUtil.getCurrentDatabase();
            docCopy = db.createDocument();
            doc.copyAllItems(docCopy, true);
            
            docCopy.replaceItemValue("action", "copied");
            docCopy.save();
            
        }catch(Exception e){
            XspOpenLogUtil.logError(e);
        }
    }
    
    public static void removeDocument(Document doc){
        try{
            doc.removePermanently(true);
        }catch(Exception e){
            XspOpenLogUtil.logError(e);
        }
    }
    
    public static void saveDocument(Document doc){
        try{
            doc.save();
        }catch(Exception e){
            XspOpenLogUtil.logError(e);
        }
    }
    
    
}
