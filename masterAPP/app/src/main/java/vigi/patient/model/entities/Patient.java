package vigi.patient.model.entities;


/**
 * Model POJO (plain old java object)
 * for the patient user
 */
public class Patient {

    //TODO: Add birthday date

    private String tokenid;
    private String name;
    private String image;
    private String email;
    private String payment;

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    private Position position;

    private String phoneNumber;

    public String getId() {
        return tokenid;
    }

    public void setId(String tokenid) {
        this.tokenid = tokenid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position coordinates) {
        this.position = coordinates;
    }

    public static class Position {
        private String country;
        private String city;
        private int number;
        private String street;
        private double x;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        private double y;
        private double z;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }
    }
}