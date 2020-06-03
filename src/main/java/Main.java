import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
    try {
    File file = createxml();
    parseDoc(file);
    queryxml(file);
    File newfile = modifyxml(file);

    }catch (Exception e)
    {
    e.printStackTrace();
    }
    }

    public static void parseDoc(File file)
    {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("student");
            System.out.println("-----------------------------");

            for (int i = 0; i <nList.getLength(); i++)
            {
                Node nNode = nList.item(i);
                System.out.println("\nCurrent Element: "+ nNode.getNodeName());

                if(nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) nNode;
                    System.out.println("Student roll no : " + eElement.getAttribute("rollno"));
                    System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
                    System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
                    System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
                    System.out.println("Marks : " + eElement.getElementsByTagName("marks").item(0).getTextContent());
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void queryxml(File file)
    {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            System.out.print("Root element : ");
            System.out.println(doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("supercars");
            System.out.println("--------------------------------");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                System.out.println("\nCurrent Element : ");
                System.out.println(nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nNode;
                    System.out.print("Company: ");
                    System.out.println(element.getAttribute("company"));
                    NodeList carNameList = element.getElementsByTagName("carname");

                    for (int count = 0; count < carNameList.getLength(); count++) {
                        Node node1 = carNameList.item(count);

                        if (node1.getNodeType() == node1.ELEMENT_NODE) {
                            Element car = (Element) node1;
                            System.out.print("Car name : ");
                            System.out.println(car.getTextContent());
                            System.out.print("Car type : ");
                            System.out.println(car.getAttribute("type"));
                        }
                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static File createxml()
    {
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            //root element
            Element rootElement = doc.createElement("cars");
            doc.appendChild(rootElement);

            //supercars element
            Element supercar = doc.createElement("supercars");
            rootElement.appendChild(supercar);

            //supercars element
            Attr attr = doc.createAttribute("company");
            attr.setValue("Ferrari");
            supercar.setAttributeNode(attr);

            //carname element
            Element carname = doc.createElement("carname");
            Attr attrType = doc.createAttribute("type");
            attrType.setValue("formula one");
            carname.setAttributeNode(attrType);
            carname.appendChild(doc.createTextNode("Ferrari 101"));
            supercar.appendChild(carname);

            Element carname2 = doc.createElement("carname");
            Attr attrType2 = doc.createAttribute("type");
            attrType2.setValue("sports");
            carname2.setAttributeNode(attrType2);
            carname2.appendChild(doc.createTextNode("Ferrari 202"));
            supercar.appendChild(carname2);

            //write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File theFile = new File("cars.xml");
            StreamResult result = new StreamResult(theFile);
            transformer.transform(source, result);
            return theFile;

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public static File modifyxml(File file) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);

        Node cars = doc.getFirstChild();
        Node supercar = doc.getElementsByTagName("supercars").item(0);

        //update supercare attribute
        NamedNodeMap attr = supercar.getAttributes();
        Node nodeAttr = attr.getNamedItem("company");
        nodeAttr.setTextContent("Lamborgini");

        //loop the supercar child node
        NodeList list = supercar.getChildNodes();

        for (int i=0;i<list.getLength();i++)
        {
            Node node = list.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                if("carname".equals(element.getNodeName())) {
                    if ("Ferrari 101".equals(element.getTextContent())) {
                        element.setTextContent("Lamborgini 001");
                    }
                    if ("Ferrari 202".equals(element.getTextContent()))
                    {
                        element.setTextContent("Lamborgini 202");
                    }
                }
            }
        }
        NodeList childNodes = cars.getChildNodes();

        for(int i=0;i<childNodes.getLength();i++)
        {
            Node node = childNodes.item(i);
            if ("luxurycars".equals(node.getNodeName()))
                cars.removeChild(node);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        File theFile = new File("modifiedcars.xml");
        StreamResult result = new StreamResult(theFile);
        transformer.transform(source, result);
        return theFile;
    }
    public static void converttojson(File file)
    {

    }
}
