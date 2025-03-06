package mb.company.enums;

import java.util.Arrays;

public enum ComputerType {

    BACK_OFFICE("BOS"),

    POINT_OF_SALE("POS"),

    FORECOURT_SERVER("FCS"),

    UNKNOWN("UNK")

    ;

    private final String code;

    ComputerType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ComputerType fromCode(String code) {
        return Arrays.stream(ComputerType.values())
                .filter(eItem -> eItem.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(ComputerType.UNKNOWN)
                ;
    }

}
