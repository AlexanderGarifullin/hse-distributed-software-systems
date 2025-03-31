package hse.dss.diss.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MsgGenerator {

    public static String getErrorMsg(String entity, Long id) {
        return String.format("%s with ID %d not found", entity, id);
    }
}
