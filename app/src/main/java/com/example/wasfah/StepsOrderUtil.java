package com.example.wasfah;

import com.example.wasfah.model.StepModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StepsOrderUtil {

    public static List<StepModel> removeFromSteps(List<StepModel> modelList, String id)
    {
        List<StepModel> temp = new ArrayList<>();
        if(modelList == null || modelList.size() <= 0)
        {
            return temp;
        }

        int removedOrder = 0;

        for(StepModel step : modelList)
        {
            if(step.getModelId().equals(id)) {
                removedOrder = step.getOrder();
                break;
            }
        }
        //assuming only one step is removed.
        for(StepModel step : modelList)
        {
            if(step.getOrder() < removedOrder)
            {
                temp.add(step);
            }
            else if(step.getOrder() > removedOrder)
            {
                step.setOrder(step.getOrder() - 1);
                temp.add(step);
            }
        }

        return temp;
    }

    public static int getNextStepOrder(List<StepModel> modelList)
    {
        if(modelList == null)
        {
            return 0;
        }

        return modelList.size() + 1;
    }
}
