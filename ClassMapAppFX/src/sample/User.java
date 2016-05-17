package sample;

import java.sql.Timestamp;

/**
 * Created by acous on 12/22/2015.
 */
public class User {
    private String first_name;
    private String last_name;
    private String user_name;
    private int user_id;
    private Timestamp last_log;
    private String account;

    public User(String name, String lastName, String username, int userId, Timestamp lastLog, String permissions) {
        this.first_name = name;
        this.last_name = lastName;
        this.user_name = username;
        this.user_id = userId;
        this.last_log = lastLog;
        this.account = permissions;

        this.printUser();
    }

    public String getFirst() { return this.first_name; }

    public String getLast() { return this.last_name; }

    public String getUser() { return this.user_name; }

    public int getId() { return this.user_id; }

    public Timestamp getSQLLog() { return this.last_log; }

    public String getAccount() { return this.account; }

    private void printUser() {
        System.out.println(first_name + " " + last_name + " " + user_name + " " + user_id + " " + last_log.toString()
            + " " + account);
    }
}
