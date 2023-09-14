package util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class LocatorReader {

    private Document doc;
    //private PropertyReader propObj;

    public LocatorReader(String xmlName) {
        SAXReader reader = new SAXReader();
        try {
            String localPath = System.getProperty("user.dir");
            localPath = localPath+"/src/test/resources/";
            System.out.println("file path >>> "+localPath+xmlName);
            doc = reader.read(localPath+xmlName);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getLocator(String locator){
        return doc.selectSingleNode("//" + locator.replace('.', '/')).getText();

    }
}
