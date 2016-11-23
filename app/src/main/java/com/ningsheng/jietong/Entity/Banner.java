package com.ningsheng.jietong.Entity;

/**
 * Created by zhushunqing on 2016/2/25.
 */
public class Banner {
    private String id;
    private String links;
    private String sort;
    private String content;
    private String noticeTitle;
    private String image;

    @Override
    public String toString() {
        return "Banner{" +
                "id='" + id + '\'' +
                ", links='" + links + '\'' +
                ", sort='" + sort + '\'' +
                ", content='" + content + '\'' +
                ", noticeTitle='" + noticeTitle + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
