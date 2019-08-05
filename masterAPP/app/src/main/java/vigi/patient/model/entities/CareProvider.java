package vigi.patient.model.entities;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Model POJO (plain old java object)
 * for the care provider user
 */
public class CareProvider implements Serializable {

    //TODO: Add birthday date

    private String id;
    private String name;

    public URL getImage() {
        return image;
    }

    public void setImage(URL image) {
        this.image = image;
    }

    private URL image;

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getExpectedtime() {
        return expectedtime;
    }

    public void setExpectedtime(String expectedtime) {
        this.expectedtime = expectedtime;
    }

    private String email;
    private String job, price, expectedtime;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    private int rating;

    public List<HashMap<String, String>> getAgenda() {
        return agenda;
    }

    public void setAgenda(List<HashMap<String, String>> agenda) {
        this.agenda = agenda;
    }

    private List<HashMap<String,String>> agenda;


    private Position position;

    private String phoneNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position coordinates) {
        this.position = coordinates;
    }

    public class Position {
        private String country;
        private String city;
        private double x;
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