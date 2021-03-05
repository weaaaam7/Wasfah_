package com.example.wasfah;

import com.example.wasfah.model.StepModel;

import java.math.BigDecimal;
import java.util.HashMap;

public class StepModelConverter {


    public static StepModel getStepModel(Object obj)
    {

        HashMap map = (HashMap)obj;
        StepModel model = new StepModel();

        model.setOrder(castOrder(map.get("quantity")));
        model.setDescription((String)map.get("description"));
        model.setModelId((String)map.get("modelId"));
        return model;
    }

    public static int castOrder(Object qty)
    {
        if(qty instanceof  Long)
        {
            return (int)qty;
        }
        else if (qty instanceof  Integer)
        {
            return (int)qty;
        }
        return new BigDecimal(qty!=null ? qty.toString() : "1").intValue();
    }
}
