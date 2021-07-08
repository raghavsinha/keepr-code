package example;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CompareLoginInfo implements RequestStreamHandler {
    private String TABLE_NAME = "StoreLoginInfo-Table-StreamHandler";
    private DynamoDB database;
    private String message = "Post Successful!";

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        try {
            JsonObject jo = getJsonFromInputStream(inputStream);
            initDB(); //initializes dynamodb client
            putData(jo); //puts json data from input stream into db
        } catch(Exception e) {
            message = "Post Failed. Internal Server Error.";
        }
        JsonObject jsonMessage = new Gson().fromJson("{" + "\"message\":\"" + message + "\"}", JsonObject.class);
        oos.writeObject(jsonMessage);
    }

    private PutItemOutcome putData(JsonObject data) throws ConditionalCheckFailedException {
        Item newItem = new Item().withString("username", data.get("username").getAsString()).withString("password", data.get("password").getAsString()).withString("date", Calendar.getInstance().getTime().toString());
        PutItemSpec pIS = new PutItemSpec().withItem(newItem);
        return this.database.getTable(TABLE_NAME).putItem(pIS);
    }

    private boolean initDB(){
        boolean noProblems = true;
        try {
            AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
            this.database = new DynamoDB(client);
        } catch(Exception e){
            noProblems = false;
        }
        return noProblems;
    }

    private JsonObject getJsonFromInputStream(InputStream is){
        JsonObject jo = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String jsonString = "";
            while(br.ready()){
                jsonString += br.readLine() + "\n";
            }
            br.close();
            jo = new Gson().fromJson(jsonString, JsonObject.class);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return jo;
    }
}
