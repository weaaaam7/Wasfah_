package com.example.wasfah;

import com.example.wasfah.model.IngredientModel;
import com.example.wasfah.model.StepModel;

import java.math.BigDecimal;
import java.util.HashMap;

public class IngridientModelConverter {


    public static IngredientModel getIngredientModel(Object obj)
    {
        if(obj instanceof IngredientModel )
        {
            return (IngredientModel)obj;
        }
        HashMap map = (HashMap)obj;
        IngredientModel model = new IngredientModel();

        model.setQuantity(castQuantity(map.get("quantity")));
        model.setName((String)map.get("name"));
        model.setModelId((String)map.get("modelId"));
        model.setUnitOfMeasure((String)map.get("unitOfMeasure"));
        return model;
    }

    public static double castQuantity(Object qty)
    {

        return new BigDecimal(qty!=null ? qty.toString() : "0").doubleValue();
    }

}