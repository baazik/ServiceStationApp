package mb.company.entities;

public class Station {

    private int id;
    private String name;
    private String address;
    private String country;

    public Station(int id, String name, String address, String country) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.country = country;
    }

    public Station(String name, String address, String country) {
        this.name = name;
        this.address = address;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("id: ").append(id)
                .append(" | ")
                .append("name: ").append(name)
                .append(" | " )
                .append("address: ").append(address)
                .append(" | ")
                .append("country: ").append(country)
                .toString()
                ;
    }
}
