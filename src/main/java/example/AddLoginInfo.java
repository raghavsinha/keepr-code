package example;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AddLoginInfo implements RequestHandler<LoginData, PutResponse> {
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
}
