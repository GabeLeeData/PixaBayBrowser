package gabriellee.project.pixabaybrowser.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Hit implements Parcelable {

    private String user;
    private String tags;
    private String userImageUrl;
    private String imageUrl;
    private int views;
    private int likes;
    private int download;

    public Hit(String user, String tags, String userImageUrl, String imageUrl, int views, int likes, int download) {
        this.user = user;
        this.tags = tags;
        this.userImageUrl = userImageUrl;
        this.imageUrl = imageUrl;
        this.views = views;
        this.likes = likes;
        this.download = download;
    }

    public Hit() {
    }

    protected Hit(Parcel in) {
        user = in.readString();
        tags = in.readString();
        userImageUrl = in.readString();
        imageUrl = in.readString();
        views = in.readInt();
        likes = in.readInt();
        download = in.readInt();
    }

    public static final Creator<Hit> CREATOR = new Creator<Hit>() {
        @Override
        public Hit createFromParcel(Parcel in) {
            return new Hit(in);
        }

        @Override
        public Hit[] newArray(int size) {
            return new Hit[size];
        }
    };

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    @Override
    public String toString() {
        return "Hit{" +
                "user='" + user + '\'' +
                ", tags='" + tags + '\'' +
                ", userImageUrl='" + userImageUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", views=" + views +
                ", likes=" + likes +
                ", download=" + download +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user);
        dest.writeString(tags);
        dest.writeString(userImageUrl);
        dest.writeString(imageUrl);
        dest.writeInt(views);
        dest.writeInt(likes);
        dest.writeInt(download);
    }
}
