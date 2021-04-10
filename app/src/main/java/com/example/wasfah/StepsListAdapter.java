package com.example.wasfah;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.wasfah.model.IngredientModel;
import com.example.wasfah.model.StepModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class StepsListAdapter extends ArrayAdapter<StepModel>  {


    private Context context;
    private int itemPosition;
    private List<StepModel> dataSource;

    public StepsListAdapter(@NonNull Context context, int resource, @NonNull List<StepModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.dataSource = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.steps_row,parent,false);

        }
        this.itemPosition = position;
        StepModel model = this.getStepModel(position);

        if(model!=null)
        {
            TextView orderTextView = (TextView) convertView.findViewById(R.id.step_order_tv);
            orderTextView.setText(model.getOrder() + "");
            EditText descEditText = (EditText) convertView.findViewById(R.id.step_desc_et);
            descEditText.setText(model.getDescription());
            descEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    model.setDescription(s.toString());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    model.setDescription(s.toString());
                }
            });

            ImageButton delete = (ImageButton) convertView.findViewById(R.id.image_remove_step);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItemFromDS(model.getModelId());
                }
            });

            ImageButton up = (ImageButton)convertView.findViewById(R.id.stepUpBtn);
            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moveItemUp(model.getModelId());

                }
            });

            ImageButton down = (ImageButton)convertView.findViewById(R.id.stepDownBtn);

            down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    moveItemDown(model.getModelId());

                }
            });

        }
        return convertView;
    }

    private StepModel getStepModel(int position)
    {
        StepModel model = null;
        try{
            model = this.getItem(position);
        }catch (ClassCastException ex)
        {
            model = StepModelConverter.getStepModel(getItem(position));
        }

        return  model;
    }
    private void removeItemFromDS(String modelId)
    {
        List<StepModel> temp = StepsOrderUtil.removeFromSteps(this.dataSource,modelId);
        this.dataSource.clear();
        this.dataSource.addAll(temp);
        notifyDataSetChanged();
    }

    private void moveItemUp(String modelId)
    {

        int index = -1;
        if(this.dataSource != null)
        {
            for(int i = 0; i < this.dataSource.size(); i++)
            {
                StepModel model = StepModelConverter.getStepModel(this.dataSource.get(i));
                if(model.getModelId().equals(modelId))
                {
                    index = i;
                    break;
                }
            }
        }
        if(index > 0)
        {
            List<StepModel> temps = new ArrayList<>();
            temps.addAll(this.dataSource);

            StepModel temp = StepModelConverter.getStepModel(temps.get(index));
            StepModel temp1 = StepModelConverter.getStepModel(temps.get(index - 1));

            int tempOrder = temp.getOrder();
            int temp1Order = temp1.getOrder();

            temp.setOrder(temp1Order);
            temp1.setOrder(tempOrder);

            temps.set(index, temp1);
            temps.set(index - 1, temp);

            this.dataSource.clear();
            this.dataSource.addAll(temps);
            notifyDataSetChanged();
        }

    }

    private void moveItemDown(String modelId)
    {

        int index = -1;
        if(this.dataSource != null)
        {

            for(int i = 0; i < this.dataSource.size(); i++)
            {
                StepModel model = StepModelConverter.getStepModel(this.dataSource.get(i));
                if(model.getModelId().equals(modelId))
                {
                    index = i;
                    break;
                }
            }
        }
        if(index < this.dataSource.size() - 1 && index >= 0)
        {
            List<StepModel> temps = new ArrayList<>();
            temps.addAll(this.dataSource);

            StepModel temp = StepModelConverter.getStepModel(temps.get(index));
            StepModel temp1 = StepModelConverter.getStepModel(temps.get(index + 1));

            int tempOrder = temp.getOrder();
            int temp1Order = temp1.getOrder();

            temp.setOrder(temp1Order);
            temp1.setOrder(tempOrder);

            temps.set(index, temp1);
            temps.set(index + 1, temp);

            this.dataSource.clear();
            this.dataSource.addAll(temps);
            notifyDataSetChanged();

        }

    }
}