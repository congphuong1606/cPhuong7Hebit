package com.discovertodo.phone.android.ebook;

/**
 * Created by Ominext on 2/6/2018.
 */

public class Content {
    String name;
    String page;

    public Content() {
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Content(String name, String page) {

        this.name = name;
        this.page = page;
    }
}
