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
        StepModel model = this.getItem(position);
        if(model!=null)
        {
            TextView orderTextView = (TextView) convertView.findViewById(R.id.step_order_tv);
            orderTextView.setText(model.getOrder() + "");

            EditText descEditText = (EditText) convertView.findViewById(R.id.step_desc_et);
            descEditText.setText(model.getDescription());
            descEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    StepModel model = getItem(itemPosition);
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

        }
        return convertView;
    }
    private void removeItemFromDS(String modelId)
    {

        List<StepModel> temp = StepsOrderUtil.removeFromSteps(this.dataSource,modelId);
        this.dataSource.clear();
        this.dataSource.addAll(temp);

        notifyDataSetChanged();
    }


}
