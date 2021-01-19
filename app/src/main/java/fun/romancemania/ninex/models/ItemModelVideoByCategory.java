package fun.romancemania.ninex.models;

public class ItemModelVideoByCategory {

    private String id;
    private String CategoryId;
    private String CategoryName;
    private String VideoUrl;
    private String VideoId;
    private String Duration;
    private String VideoName;
    private String Description;
    private String ImageUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryid) {
        this.CategoryId = categoryid;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryname) {
        this.CategoryName = categoryname;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videourl) {
        this.VideoUrl = videourl;
    }


    public String getVideoId() {
        return VideoId;
    }

    public void setVideoId(String videoid) {
        this.VideoId = videoid;
    }

    public String getVideoName() {
        return VideoName;
    }

    public void setVideoName(String videoname) {
        this.VideoName = videoname;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        this.Duration = duration;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String desc) {
        this.Description = desc;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageurl) {
        this.ImageUrl = imageurl;
    }

}
