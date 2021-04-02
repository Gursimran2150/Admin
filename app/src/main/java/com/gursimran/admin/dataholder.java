package com.gursimran.admin;

public class dataholder {
    String AppName,Category,ImageUrl;

    public dataholder(String appName, String category, String imageUrl) {
        AppName = appName;
        Category = category;
        ImageUrl = imageUrl;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
