package com.example.moviews;

public class Video {
    String thumbnail;
    String video;

    public Video(String thumbnail, String video) {
        this.thumbnail = thumbnail;
        this.video = video;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
