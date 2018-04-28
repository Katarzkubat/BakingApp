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

public class RecipeStepsFragment extends Fragment {

    private ArrayList<Steps> steps;
    private boolean twoPane;
    private static final String CHOSEN_STEP_POSITION = "position";
    public static final String STEPS = "steps";
    private final static String TWO_PANE = "twoPane";
    private final static String SINGLE_RECIPE = "singleRecipe";

    @BindView(R.id.recipe_steps_recycler) RecyclerView stepsRecycler;

    private OnItemClickListener mCallback;

    public RecipeStepsFragment() {}

    public interface OnItemClickListener {
        void onItemSelected(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recipe_details_steps_fragment, container, false);
        Unbinder unbinder = ButterKnife.bind(this, rootView);

        StepsAdapter stepsAdapter = new StepsAdapter(getContext(), this, steps);

        Recipes recipes = getArguments().getParcelable(SINGLE_RECIPE);
        twoPane = getArguments().getBoolean(TWO_PANE);

        stepsRecycler.setAdapter(stepsAdapter);

        steps = recipes.getSteps();

        stepsAdapter.setSteps(steps);
        return rootView;
    }

    @SuppressWarnings("ConstantConditions")
    public void click(int position){
        Log.d("recipeStepsFragmentClik","click");
        if(!twoPane) {
Log.d("recipeStepsFragmentClik","not two pane");
Log.d("recipeStepsFragmentClik","step.size: "+steps.size());

            Intent openStepDetail = new Intent(getContext(), StepDetailedActivity.class);
            openStepDetail.putParcelableArrayListExtra("steps", steps);
            openStepDetail.putExtra(CHOSEN_STEP_POSITION, position);
            getContext().startActivity(openStepDetail);
        }
            mCallback.onItemSelected(position);

        }
}
