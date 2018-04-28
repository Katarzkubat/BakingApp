package com.example.katarzkubat.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Steps implements Parcelable{


    public Steps() {}

    public Steps(String shortDescription, int stepId, String description, String videoURL, String thumbnailURL) {
        this.shortDescription = shortDescription;
        this.stepId = stepId;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideo() {
        return videoURL;
    }

    public void setVideo(String video) {
        this.videoURL = video;
    }

    public String getThumbnailUrl() {
        return thumbnailURL;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailURL = thumbnailUrl;
    }

    public static Creator<Steps> getCREATOR() {
        return CREATOR;
    }

    private String shortDescription;
    private int stepId;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    private Steps(Parcel in) {
        shortDescription = in.readString();
        stepId = in.readInt();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shortDescription);
        parcel.writeInt(stepId);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);
    }

}

