package example;

import com.google.gson.JsonObject;

import java.io.File;

public class TestMain {
    public static void main(String[] args) {
        File f = new File("D:\\Users\\rishabh\\IdeaProjects\\Username-Password-API\\src\\main\\resources\\sample.json");
        JsonObject jo = AddLoginInfo.getJson(f);
        //System.out.println("Login Info - username:" + jo.get("username") + ", password:" + jo.get("password"));
    }
}
