package example;

import java.util.Calendar;
import java.util.Date;

public class LoginData {
    private String username;
    private String password;

    public Date getInitDate() {
        return initDate;
    }

    private final Date initDate;

    public LoginData() {
        username = "";
        password = "";
        initDate = Calendar.getInstance().getTime();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
