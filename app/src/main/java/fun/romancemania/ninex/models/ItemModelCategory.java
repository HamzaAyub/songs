package fun.romancemania.ninex.models;

public class ItemModelCategory {

    private int CategoryId;
    private String CategoryName;
    private String CategoryImageUrl;
    private String CategoryType;
    private String CategoryLink;

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryid) {
        this.CategoryId = categoryid;
    }


    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryname) {
        this.CategoryName = categoryname;
    }

    public String getCategoryImageurl() {
        return CategoryImageUrl;

    }
     public void setCategoryImageurl(String catimageurl) {
        this.CategoryImageUrl = catimageurl;
    }

    public String getCategoryType() {
        return CategoryType;

    }
    public void setCategoryType(String CategoryType) {
        this.CategoryType = CategoryType;
    }

    public String getCategoryLink() {
        return CategoryLink;

    }
    public void setCategoryLink(String CategoryLink) {
        this.CategoryLink = CategoryLink;
    }

}
