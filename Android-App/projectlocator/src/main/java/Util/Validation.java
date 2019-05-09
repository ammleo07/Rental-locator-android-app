package Util;

import java.util.List;

/**
 * Created by alber on 29/04/2019.
 */

public class Validation {

    public boolean isRequiredFieldsCompleted(List<String> list)
    {
        boolean result = true;

        for(String item:list)
        {
            if(item.equalsIgnoreCase(""))
            {
                result = false;
                break;
            }
        }

        return result;
    }
}
