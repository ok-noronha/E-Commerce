package org.products;

import org.json.simple.parser.ParseException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    String id;
    HashMap<Product,Integer> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<Product, Integer> getList() {
        return list;
    }

    public void setList(HashMap<Product, Integer> list) {
        this.list = list;
    }
    public Cart(String id) throws IOException, XMLStreamException, ParseException {
        this.id = id;
        this.list = new HashMap<>();
        readFromXML();
    }

    public void addProduct(Product prod, int quantity) throws IOException {
        if (list.containsKey(prod)) {
            int currentQuantity = list.get(prod);
            list.put(prod, currentQuantity + quantity);
        } else {
            list.put(prod, quantity);
        }
        this.writeToXML();
    }

    public void removeProduct(Product prod, int quantity) throws IOException {
        if (list.containsKey(prod)) {
            int currentQuantity = list.get(prod);
            if (currentQuantity > quantity) {
                list.put(prod, currentQuantity - quantity);
            } else {
                list.remove(prod);
            }
        }
        this.writeToXML();
    }
    public void writeToXML() throws IOException {
        StringBuilder xmlContent = new StringBuilder();
        xmlContent.append("\n");
        xmlContent.append("<cart id=").append(id).append("\n");
        for (Product prod : list.keySet()) {
            int quantity = list.get(prod);
            xmlContent.append("<product id=").append(prod.getId()).append("quantity=").append(quantity).append("\n");
        }

        File file = new File("cart_" + id + ".xml");
        FileWriter writer = new FileWriter(file);
        writer.write(xmlContent.toString());
        writer.close();
    }
    private void readFromXML() throws IOException, XMLStreamException, ParseException {
        File file = new File("cart_" + id + ".xml");
        if (!file.exists()) {
            return;
        }

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(file));
        while (xmlStreamReader.hasNext()) {
            int eventType = xmlStreamReader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                String elementName = xmlStreamReader.getLocalName();
                if (elementName.equals("product")) {
                    String productId = xmlStreamReader.getAttributeValue(null, "id");
                    int quantity = Integer.parseInt(xmlStreamReader.getAttributeValue(null, "quantity"));
                    list.put(new Product(productId), quantity);
                }
            }
        }
        xmlStreamReader.close();
    }
}
