package mb.company.entities;

import java.util.List;
import java.util.Optional;

public class Station {

    private int id;
    private String name;
    private String address;
    private String country;
    private List<Computer> computers;

    public Station(int id, String name, String address, String country, List<Computer> computers) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.country = country;
        this.computers = computers;
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

    public List<Computer> getComputers() {
        return computers;
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

    public void setComputers(List<Computer> computers) {
        this.computers = computers;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Service Station{")
                .append("id: ").append(id)
                .append("|")
                .append("name: ").append(name)
                .append("|")
                .append("address: ").append(address)
                .append("|")
                .append("country: ").append(country)
                .append("|")
                .append("number of computers: ").append((long) computers.size())
                .append("}")
                .toString()
                ;
    }
}
