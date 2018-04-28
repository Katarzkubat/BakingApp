package com.example.katarzkubat.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.katarzkubat.bakingapp.Model.Ingredients;
import com.example.katarzkubat.bakingapp.R;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder>  {

    private ArrayList<Ingredients> ingredients = new ArrayList<>();

    private final Context context;

    public IngredientsAdapter(Context context, ArrayList<Ingredients> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_details_ingredients_list_item,
                parent, false);
        return new IngredientsAdapter.IngredientsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsAdapterViewHolder holder, int position) {

        Ingredients singleIngredient = ingredients.get(position);

        holder.ingredientLabel.setText(singleIngredient.getIngredient());
        holder.measureLabel.setText(singleIngredient.getMeasure());
        holder.quantityLabel.setText(String.valueOf(singleIngredient.getQuantity()));
    }

    public class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient) TextView ingredientLabel;
        @BindView(R.id.ingredient_quantity) TextView quantityLabel;
        @BindView(R.id.ingredient_measure) TextView measureLabel;

        public IngredientsAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        if (null == ingredients) {
            return 0;
        }
        return ingredients.size();
    }

    public void setIngredients(ArrayList<Ingredients> ingredientsList) {
        ingredients = ingredientsList;
        notifyDataSetChanged();
    }
}
