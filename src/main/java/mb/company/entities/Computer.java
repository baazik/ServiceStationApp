package mb.company.entities;

import mb.company.enums.ComputerType;

public class Computer {

    private int id;
    private String ipAdress;
    private ComputerType computerType;
    private Station station;

    public Computer(int id, String ipAdress, ComputerType computerType, Station station) {
        this.id = id;
        this.ipAdress = ipAdress;
        this.computerType = computerType;
        this.station = station;
    }

    public Computer(String ipAdress, ComputerType computerType, Station station) {
        this.ipAdress = ipAdress;
        this.computerType = computerType;
        this.station = station;
    }

    public int getId() {
        return id;
    }

    public ComputerType getComputerType() {
        return computerType;
    }

    public String getIpAddress() {
        return ipAdress;
    }

    public Station getStation() {
        return station;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setComputerType(ComputerType computerType) {
        this.computerType = computerType;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Computer{")
                .append("id: ").append(id)
                .append("|")
                .append("ip: ").append(ipAdress)
                .append("|")
                .append("type: ").append(computerType.getCode())
                .append("|")
                .append("station id/name: ").append(station.getId()).append("/").append(station.getName())
                .append("}")
                .toString()
                ;
    }
}
