package example;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.google.gson.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.*;
import java.util.Calendar;

public class AddLoginInfo implements RequestStreamHandler {
    private String TABLE_NAME = "";
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
        JsonObject jsonMessage = new Gson().fromJson("{" + "\"message\":" + message + "}", JsonObject.class);
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

    public static JsonObject getJson(File f){
        JsonObject jo = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String jsonString = "";
            while(br.ready()){
                jsonString += br.readLine() + "\n";
            }
            br.close();

            System.out.println(jsonString);
            jo = new Gson().fromJson(jsonString, JsonObject.class);
            //JsonElement je = JsonParser.parseString(jsonString);
            //jo = je.getAsJsonObject();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return jo;
    }
}

/*public class AddLoginInfo implements RequestHandler<LoginData, PutResponse> {
    DynamoDB database;
    String tableName = "login-info-table";
    String message = "";

    @Override
    public PutResponse handleRequest(LoginData data, Context context){
        LambdaLogger logger = context.getLogger();
        // log execution details
        Gson gson = new Gson();
        logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
        logger.log("CONTEXT: " + gson.toJson(context));
        // process event
        logger.log("EVENT: " + gson.toJson(data));

        logger.log("handler entered");
        initDB(); //initializes dynamodb client in java
        logger.log("initDb called");
        putData(data); //posts username/password attributes from LoginData object to DynamoDB client
        logger.log("putData called");
        message = "Post Successful.";
        PutResponse pr = new PutResponse(message);
        return pr;
    }

    private PutItemOutcome putData(LoginData data) throws ConditionalCheckFailedException {
        Item newItem = new Item().withString("username", data.getUsername()).withString("password", data.getPassword()).withString("date", data.getInitDate().toString());
        PutItemSpec pIS = new PutItemSpec().withItem(newItem);
        return this.database.getTable(tableName).putItem(pIS);
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
}*/
