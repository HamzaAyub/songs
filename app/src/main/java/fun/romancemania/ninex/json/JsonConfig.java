package fun.romancemania.ninex.json;

import java.io.Serializable;

public class JsonConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String YOUTUBE_IMAGE_FRONT = "http://img.youtube.com/vi/";
    public static final String YOUTUBE_IMAGE_BACK = "/hqdefault.jpg";
    public static final String YOUTUBE_SMALL_IMAGE_BACK = "/default.jpg";

    public static final String LATEST_ARRAY_NAME = "YourVideosChannel";
    public static final String LATEST_ID = "id";
    public static final String LATEST_CATID = "cat_id";
    public static final String LATEST_CAT_NAME = "category_name";
    public static final String LATEST_VIDEO_URL = "video_url";
    public static final String LATEST_VIDEO_ID = "video_id";
    public static final String LATEST_VIDEO_DURATION = "video_duration";
    public static final String LATEST_VIDEO_NAME = "video_title";
    public static final String LATEST_VIDEO_DESCRIPTION = "video_description";
    public static final String LATEST_IMAGE_URL = "video_thumbnail";

    public static final String CATEGORY_ARRAY_NAME = "YourVideosChannel";
    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_CID = "cid";
    public static final String CATEGORY_IMAGE = "category_image";
    public static final String CATEGORY_TYPE = "cat_type";
    public static final String CATEGORY_URL = "cat_url";

    public static final String CATEGORY_ITEM_ARRAY_NAME = "YourVideosChannel";
    public static final String CATEGORY_ITEM_ID = "id";
    public static final String CATEGORY_ITEM_CATID = "cat_id";
    public static final String CATEGORY_ITEM_CAT_NAME = "category_name";
    public static final String CATEGORY_ITEM_VIDEO_URL = "video_url";
    public static final String CATEGORY_ITEM_VIDEO_ID = "video_id";
    public static final String CATEGORY_ITEM_VIDEO_DURATION = "video_duration";
    public static final String CATEGORY_ITEM_VIDEO_NAME = "video_title";
    public static final String CATEGORY_ITEM_VIDEO_DESCRIPTION = "video_description";
    public static final String CATEGORY_ITEM_IMAGE_URL = "video_thumbnail";

    public static final String SLIDER_URL = "/api.php?slider";

    public static final String FEATURE_CAT_URL = "/api.php?featured_category";

    public static final String SLIDER_CAT_URL = "/api.php?slider_video";

    public static final String SLIDER_ARRAY = "YourVideosChannel";
    public static final String SLIDER_ID = "sid";
    public static final String SLIDER_NAME = "video_thumbnail";
    public static final String SLIDER_VIDEO_LINK = "link";
    public static final String SLIDER_IMAGE = "image";

    public static final String HOME_CATEGORY_NAME = "category_name";
    public static final String HOME_CATEGORY_CID = "cid";
    public static final String HOME_CATEGORY_IMAGE = "category_image";
    public static final String HOME_CATEGORY_TYPE = "cat_type";
    public static final String HOME_CATEGORY_URL = "cat_url";

    public static final String HOME_FEA_CATEGORY_NAME = "category_name";
    public static final String HOME_FEA_CATEGORY_CID = "cid";
    public static final String HOME_FEA_CATEGORY_IMAGE = "category_image";
    public static final String HOME_FEA_CATEGORY_TYPE = "cat_type";
    public static final String HOME_FEA_CATEGORY_URL = "cat_url";

    public static final String HOME_LATEST_TITLE = "title";
    public static final String HOME_LATEST_IMAGE = "image";
    public static final String HOME_LATEST_DESC = "description";
    public static final String HOME_LATEST_TIME = "time";
    public static final String HOME_LATEST_PLAYID = "youtube_id";

    public static final String SLIDER_V_CATEGORY_NAME = "slider_name";
    public static final String SLIDER_V_CATEGORY_CID = "cid";
    public static final String SLIDER_V_CATEGORY_IMAGE = "slider_image";
    public static final String SLIDER_V_CATEGORY_TYPE = "cat_type";
    public static final String SLIDER_V_CATEGORY_URL = "cat_url";

    public static String CATEGORY_TITLE;
    public static String VIDEO_ITEMIDD;
    public static int CATEGORY_ID;
    public static final String VLATEST_VTYPE="video_type";

}
