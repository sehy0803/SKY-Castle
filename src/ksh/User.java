package ksh;

public class User {
    private String name;
    private String id;
    private String password;
    private String phone;
    private int seat;
    private String time;

    public User(String name, String id, String password, String phone, int seat, String time) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.phone = phone;
        this.seat = seat;
        this.time = time;
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

    public int getSeat() {
        return seat;
    }

    public String getTime() {
        return time;
    }
}
