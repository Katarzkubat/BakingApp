package com.example.katarzkubat.bakingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.katarzkubat.bakingapp.Model.Recipes;
import com.example.katarzkubat.bakingapp.R;
import com.example.katarzkubat.bakingapp.UI.IngredientsFragment;
import com.example.katarzkubat.bakingapp.UI.RecipeDetailsActivity;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder> {

    public static final String SINGLE_RECIPE = "singleRecipe" ;
    private ArrayList<Recipes> recipes = new ArrayList<>();

    private final Context context;

    public RecipesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecipesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.activity_main_recipes_list_item, parent, false);
        return new RecipesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesAdapterViewHolder holder, int position) {

        Recipes singleRecipe = recipes.get(position);
        holder.recipeLabel.setText(singleRecipe.getName());
    }

    public class RecipesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.main_recipe_label) TextView recipeLabel;

        public RecipesAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View recipe) {
            int position = getAdapterPosition();
            Recipes singleRecipe = recipes.get(position);
            Intent recipeDetail = new Intent(context, RecipeDetailsActivity.class);
            recipeDetail.putExtra(SINGLE_RECIPE, singleRecipe);
            context.startActivity(recipeDetail);
        }
    }

    @Override
    public int getItemCount() {
        if (null == recipes) {
            return 0;
        }
        return recipes.size();
    }

    public void setRecipes(ArrayList<Recipes> recipesList) {
        recipes = recipesList;
        notifyDataSetChanged();
    }
}
