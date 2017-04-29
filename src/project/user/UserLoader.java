/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.user;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import project.video.Video;
import project.video.VideoImpl;

/**
 *
 * @author LukeRobbins2112
 */
public class UserLoader {
    
    public static HashMap<String,User> loadUserXML(String fileName){
        
        NodeList entries;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            File xmlFile = new File(fileName);
            if (!xmlFile.exists()) {
                System.err.println("**** XML File '" + fileName + "' cannot be found");
                System.exit(-1);
            }

            Document doc = db.parse(xmlFile);
            doc.getDocumentElement().normalize();
            entries = doc.getDocumentElement().getChildNodes();

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }

        HashMap<String, User> users = new HashMap<>();  // Create a temporary collection to store objects (i.e., a HashMap<String, Employee>)
        
        try{
            // Parse the individual records from the XML file 
            for (int i = 0; i < entries.getLength(); i++) {
                if (entries.item(i).getNodeType() == Node.TEXT_NODE) {
                    continue;
                }

                String entryName = entries.item(i).getNodeName();

                //Checks to make sure each node is actually an Employee

                if (!entryName.equals("User")) {
                    System.err.println("Unexpected node found: " + entryName);
                    continue;
                }

                // Get all named nodes
                Element elem = (Element) entries.item(i);
                String username = elem.getElementsByTagName("Username").item(0).getTextContent();
                String password = elem.getElementsByTagName("Password").item(0).getTextContent();
                Video.Rating rating = Video.Rating.valueOf(elem.getElementsByTagName("Rating").item(0).getTextContent());

                // Create the domain object and add to temporary collection, then return collection
                User user = new UserImpl(username, password, rating);
                users.put(username, user);
            }
        } catch(InvalidParameterException e){
            System.out.println(e.getMessage());
            return null;
        }
        
        return users;
        
    }
    
}
