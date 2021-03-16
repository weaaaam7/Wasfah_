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
import androidx.annotation.NonNull;
import com.example.wasfah.model.IngredientModel;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class IngredientsListAdapter extends ArrayAdapter<IngredientModel>  {


    private Context context;
    private int itemPosition;
    private List<IngredientModel> dataSource;

    public IngredientsListAdapter(@NonNull Context context, int resource, @NonNull List<IngredientModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.dataSource = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.row_add,parent,false);

        }
        this.itemPosition = position;

        IngredientModel model = getModelOrHashMap(position);

        if(model!=null)
        {
            EditText nameView = (EditText) convertView.findViewById(R.id.ingredient_name_lv);
            nameView.setText(model.getName());
            nameView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    model.setName(s.toString());
                }
            });

            EditText qtyView = (EditText) convertView.findViewById(R.id.ingredient_qty_lv);
            qtyView.setText(model.getQuantity() + "");
            qtyView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    //IngredientModel model = getItem(itemPosition);
                    try {
                        model.setQuantity(Double.parseDouble(s.toString()));
                    }catch(Exception ex)
                    {

                        model.setQuantity(0);
                    }
                }
            });

            Spinner sp = (Spinner) convertView.findViewById(R.id.ingredient_uom_lv);
            this.setSpinnerDataSource(sp,model);

            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // IngredientModel model = getItem(itemPosition);
                    model.setUnitOfMeasure(sp.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            ImageButton delete = (ImageButton) convertView.findViewById(R.id.image_remove);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    removeItemFromDS(model.getModelId());


                }
            });

        }
        return convertView;
    }
    private IngredientModel getModelOrHashMap(int position)
    {

        try{
            IngredientModel model = this.getItem(position);
            return model;
        }catch (ClassCastException exception)
        {
            return this.getIngredientModel((Object)this.getItem(position));
        }
    }

    private void removeItemFromDS(String modelId)
    {
        for(int i = 0; i<dataSource.size(); i++)
        {
            IngredientModel model = IngridientModelConverter.getIngredientModel(dataSource.get(i));
            if(modelId.equals(model.getModelId()))
            {
                this.dataSource.remove(model);
                notifyDataSetChanged();
                break;
            }
        }
    }

    private IngredientModel getIngredientModel(Object obj)
    {
        HashMap map = (HashMap)obj;
        IngredientModel model = new IngredientModel();
        model.setUnitOfMeasure((String)map.get("unitOfMeasure"));
        model.setQuantity(castQuantity(map.get("quantity")));
        model.setName((String)map.get("name"));
        model.setModelId((String)map.get("modelId"));
        return model;
    }

    private double castQuantity(Object qty)
    {
        if(qty instanceof  Long)
        {
            return (long)qty;
        }
        else if (qty instanceof  Double)
        {
            return (double)qty;
        }
        return new BigDecimal(qty.toString()).doubleValue();
    }

    private void setSpinnerDataSource(Spinner spinner, IngredientModel model)
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.context,
                R.array.uoms, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPrompt(model.getUnitOfMeasure());
        String[] vals = this.context.getResources().getStringArray(R.array.uoms);
        if(model.getUnitOfMeasure() == null)
        {
            model.setUnitOfMeasure(vals[0]);
            return;
        }
        for(int i = 0; i < vals.length; i++)
        {
            if(vals[i].equals(model.getUnitOfMeasure()))
            {
                spinner.setSelection(i);
                return;
            }
        }

    }

}
