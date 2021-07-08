package example;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.Put;
import com.google.gson.JsonObject;

import java.io.File;

public class TestMain {
    static DynamoDB database;
    public static void main(String[] args) {
        //File f = new File("D:\\Users\\rishabh\\IdeaProjects\\Username-Password-API\\src\\main\\resources\\sample.json");
        //JsonObject jo = AddLoginInfo.getJson(f);
        //System.out.println("Login Info - username:" + jo.get("username") + ", password:" + jo.get("password"));
/*
        initDB();
        Item newItem = new Item().withString("username", "user").withString("password", "pw");
        //PutItemOutcome pis = database.getTable("StoreLoginInfo-Table-StreamHandler").putItem(new PutItemSpec().withItem(newItem));
        PutItemOutcome pis = database.getTable("StoreLoginInfo-Table-StreamHandler").putItem(newItem);
        if(pis != null) {
            System.out.println(pis.toString());
        }*/
    }
}
