package org.products;
import java.io.FileReader;
import java.io.IOException;

import org.constants.Files_;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Product {
    private String id;
    private Double price;
    private String name;

    public Product(String id) throws IOException, ParseException {
        this.id = id;
        readFromJSON();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void readFromJSON() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(Files_.productsJson);
        Object obj = jsonParser.parse(reader);
        JSONObject jsonObject = (JSONObject) obj;
        JSONObject product = (JSONObject) jsonObject.get(id);
        name = (String) product.get("name");
        price = (Double) product.get("price");
    }
}