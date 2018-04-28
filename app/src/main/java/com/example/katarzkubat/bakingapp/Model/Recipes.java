package com.example.katarzkubat.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recipes implements Parcelable {

    private int id;
    private String name;
    private ArrayList<Ingredients> ingredients;
    private ArrayList<Steps> steps;
    private int servings;
    private String image;


    public Recipes() {}

    public Recipes(int id, String name, ArrayList<Ingredients> ingredients, ArrayList<Steps> steps, int servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    private Recipes(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredients.CREATOR);
        steps = in.createTypedArrayList(Steps.CREATOR);
        servings = in.readInt();
        image = in.readString();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Ingredients> getIngredients() {
        return ingredients;
    }

    public String getImage() {
        return image;
    }

    public ArrayList<Steps> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public static final Creator<Recipes> CREATOR = new Creator<Recipes>() {
        @Override
        public Recipes createFromParcel(Parcel in) {
            return new Recipes(in);
        }

        @Override
        public Recipes[] newArray(int size) {
            return new Recipes[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
        parcel.writeInt(servings);
        parcel.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

