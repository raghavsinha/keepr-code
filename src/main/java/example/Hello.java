package example;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;

import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Hello implements RequestStreamHandler
{

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient(); //add region
        DynamoDB dynamoDB = new DynamoDB(client);


        JSONParser parser = new JSONParser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        JSONObject responseJson = new JSONObject();
        JSONObject body = new JSONObject();
        JSONObject header = new JSONObject();
        body.put("test", "hello!");

        do{
            try {
                JSONObject event = (JSONObject)parser.parse(reader);
                //JSONObject requestBody = (JSONObject)parser.parse((String)event.get("body"));
                body.put("eventJSON", event);

                //String user = (String)requestBody.get("username");
                //String password = (String)requestBody.get("password");
                String user = (String)event.get("username");
                String password = (String)event.get("password");

                body.put("1", "1");

                //HashMap<String, String> userObj = new HashMap<>();
                //userObj.put("username", user);
                //userObj.put("password", password);

                body.put("2", "2");

                //body.put("x", (String) requestBody.get("user"));
                //body.put("y", (String) requestBody.get("password"));

                PutItemOutcome dataOutcome = dynamoDB.getTable("MyTable").putItem(
                        new PutItemSpec()
                                .withItem(new Item()
                                        .withString("UserName", user)
                                        .withString("Password", password)
                                        .withString("Token", "MyToken")));

                body.put("3", "3");

                body.put("dynamoOutcome", dataOutcome.toString());

                body.put("4", "4");

                header.put("x-custom-test-header", "test-header-value");
                responseJson.put("statusCode", 200);

            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                responseJson.put("statusCode", 500);
                body.put("error", e.getMessage() + "\n" + exceptionAsString);
                context.getLogger().log(e.getMessage());
            }
        } while (false);


        responseJson.put("headers", header);
        responseJson.put("body", body.toJSONString()); //body has to be a string, cant be a json


        OutputStreamWriter writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
        writer.write(responseJson.toJSONString());
        writer.close();
    }



}