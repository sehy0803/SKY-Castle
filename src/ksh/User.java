package ksh;

public class User {
    private String name;
    private String id;
    private String password;
    private String phone;
    private int seatNumber;
    private String reservationTime;

    public User(String name, String id, String password, String phone, int seatNumber, String reservationTime) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.phone = phone;
        this.seatNumber = seatNumber;
        this.reservationTime = reservationTime;
    }

    // Getter 메소드

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public String getReservationTime() {
        return reservationTime;
    }
}
