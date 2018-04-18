package com.example.katarzkubat.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recipes implements Parcelable {

    private String name;
    private int id;
    private ArrayList<Ingredients> ingredients;
    private String image;
    private ArrayList<Steps> steps;

    public Recipes(){}
    public Recipes(String name){
        this.name = name;
    }

    public Recipes(String name, int id, ArrayList<Ingredients> ingredients, String image, ArrayList<Steps> steps) {
        this.name = name;
        this.id = id;
        this.ingredients = ingredients;
        this.image = image;
        this.steps = steps;
    }

    protected Recipes(Parcel in) {
        name = in.readString();
        id = in.readInt();
        ingredients = in.createTypedArrayList(Ingredients.CREATOR);
        image = in.readString();
        steps = in.createTypedArrayList(Steps.CREATOR);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(id);
        parcel.writeTypedList(ingredients);
        parcel.writeString(image);
        parcel.writeTypedList(steps);
    }
}

/*
 "id":1,
      "name":"Nutella Pie",
      "ingredients":[  ],
      "steps":[  ],
      "servings":8,
      "image":""
 */