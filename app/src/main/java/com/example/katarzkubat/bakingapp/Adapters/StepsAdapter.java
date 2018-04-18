package com.example.katarzkubat.bakingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.example.katarzkubat.bakingapp.Model.Steps;
import com.example.katarzkubat.bakingapp.R;
import com.example.katarzkubat.bakingapp.UI.RecipeStepsFragment;
import com.example.katarzkubat.bakingapp.UI.StepDetailedActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends
        RecyclerView.Adapter<StepsAdapter.RecipeDetailsAdapterViewHolder> {

    private ArrayList<Steps> steps = new ArrayList<>();

    private final Context context;
    private final RecipeStepsFragment fragment;

    public StepsAdapter(Context context, RecipeStepsFragment fr, ArrayList<Steps> steps) {
        this.context = context;
        this.fragment = fr;

    }

    @Override
    public StepsAdapter.RecipeDetailsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recipe_details_steps_list_item, parent, false);
        return new StepsAdapter.RecipesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsAdapter.RecipeDetailsAdapterViewHolder holder, int position) {

        Steps singleStep = steps.get(position);
        holder.stepsLabel.setText(singleStep.getShortDescription());
    }

    public class RecipeDetailsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.detail_step_label) TextView stepsLabel;

        RecipeDetailsAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            fragment.click(position);
        }
    }

    public class RecipesAdapterViewHolder extends RecipeDetailsAdapterViewHolder {
        RecipesAdapterViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemCount() {
        if (null == steps) {
            return 0;
        }
        return steps.size();
    }

    public void setSteps(ArrayList<Steps> stepsList) {
        steps = stepsList;
        notifyDataSetChanged();
    }
}
