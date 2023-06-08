package ksh;

public class User {
    private String name;
    private String id;
    private String password;
    private String phone;

    public User(String name, String id, String password, String phone) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.phone = phone;
    }

    // Getter 메소드

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPw() {
        return password;
    }

    public String getPhone() {
        return phone;
    }
}
