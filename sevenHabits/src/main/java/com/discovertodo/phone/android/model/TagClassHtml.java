package com.discovertodo.phone.android.model;

/**
 * Created by Ominext on 2/8/2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class TagClassHtml {

        @SerializedName("nameClass")
        @Expose
        private String nameClass;
        @SerializedName("position")
        @Expose
        private Integer position;

        public TagClassHtml(String nameClass, Integer position) {
            this.nameClass = nameClass;
            this.position = position;
        }

        public TagClassHtml() {
        }

        public String getNameClass() {
            return nameClass;
        }

        public void setNameClass(String nameClass) {
            this.nameClass = nameClass;
        }

        public Integer getPosition() {
            return position;
        }

        public void setCount(Integer position) {
            this.position = position;
        }


}
