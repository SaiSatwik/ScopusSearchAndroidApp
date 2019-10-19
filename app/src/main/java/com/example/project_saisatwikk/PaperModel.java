package com.example.project_saisatwikk;

import android.os.Parcel;
import android.os.Parcelable;


public class PaperModel implements Parcelable {
    private String ID, title, link;

    protected PaperModel(Parcel in) {
        ID = in.readString();
        title = in.readString();
        link = in.readString();
    }

    public PaperModel(String s, String s1) {
        link = s;
        title = s1;
    }

    public PaperModel() {
        ID = "";
        title = "";
        link = "";
    }

    public static final Creator<PaperModel> CREATOR = new Creator<PaperModel>() {
        @Override
        public PaperModel createFromParcel(Parcel in) {
            return new PaperModel(in);
        }

        @Override
        public PaperModel[] newArray(int size) {
            return new PaperModel[size];
        }
    };


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public static Creator<PaperModel> getCREATOR() {
        return CREATOR;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ID);
        parcel.writeString(title);
        parcel.writeString(link);

    }
}
