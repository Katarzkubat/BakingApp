package com.example.katarzkubat.bakingapp.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.katarzkubat.bakingapp.Adapters.StepsAdapter;
import com.example.katarzkubat.bakingapp.Model.Recipes;
import com.example.katarzkubat.bakingapp.Model.Steps;
import com.example.katarzkubat.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.katarzkubat.bakingapp.Adapters.RecipesAdapter.SINGLE_RECIPE;


public class RecipeStepsFragment extends Fragment {

    ArrayList<Steps> steps;
    Recipes recipes;
    StepsAdapter stepsAdapter;
    public static final String CHOSEN_STEP_POSITION = "position";
    public static final String STEPS = "steps";
    @BindView(R.id.recipe_steps_recycler) RecyclerView stepsRecycler;


    public RecipeStepsFragment() {}

    public interface OnItemClickListener {
        void onItemSelected(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recipe_details_steps_fragment, container, false);
        Unbinder unbinder = ButterKnife.bind(this, rootView);

        stepsAdapter = new StepsAdapter(getContext(), this, steps);

        recipes = getArguments().getParcelable(SINGLE_RECIPE);

        stepsRecycler.setAdapter(stepsAdapter);

        steps = recipes.getSteps();

        stepsAdapter.setSteps(steps);
        return rootView;
    }

    public void click(int position){

        Intent openStepDetail = new Intent(getContext(), StepDetailedActivity.class);
        openStepDetail.putParcelableArrayListExtra("steps", steps);
        openStepDetail.putExtra(CHOSEN_STEP_POSITION, position);
        getContext().startActivity(openStepDetail);
    }
}
