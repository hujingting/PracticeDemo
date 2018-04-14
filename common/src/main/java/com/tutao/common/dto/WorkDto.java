package com.tutao.common.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xianrui on 16/7/6.
 */
public class WorkDto implements Parcelable {

    private String id;
    private String title;
    private String nick;
    private String uid;
    private String icon;
    private String view;
    private String collect;
    private Date created_at;
    private String user_icon;
    private int width;
    private int height;
    private boolean isLocal;
    private int localImageId;
    private String video_url;
    private String zipsize;
    private int discuss;
    private String comments;
    private int awards;
    private String price;
    private int edited_count;
    private String template_id;
    /**
     * 该用户是否收藏过
     */
    private boolean isCollect;
    private boolean isSelect;
    private int cloaking;

    public boolean isCloaking() {
        return cloaking == 1;
    }

    public void setCloaking(int cloaking) {
        this.cloaking = cloaking;
    }

    /**
     * 作品再创作权限
     * @return
     */
    private int open;

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getZipSize() {
        return zipsize;
    }

    public void setZipSize(String zipSize) {
        this.zipsize = zipSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getCreated_at() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(created_at);
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public int getLocalImageId() {
        return localImageId;
    }

    public void setLocalImageId(int localImageId) {
        this.localImageId = localImageId;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getDiscuss() {
        return discuss;
    }

    public void setDiscuss(int discuss) {
        this.discuss = discuss;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getAwards() {
        return awards;
    }

    public void setAwards(int awards) {
        this.awards = awards;
    }

    public int getEdited_count() {
        return edited_count;
    }

    public void setEdited_count(int edited_count) {
        this.edited_count = edited_count;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public WorkDto() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.nick);
        dest.writeString(this.uid);
        dest.writeString(this.icon);
        dest.writeString(this.view);
        dest.writeString(this.collect);
        dest.writeLong(this.created_at != null ? this.created_at.getTime() : -1);
        dest.writeString(this.user_icon);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeByte(this.isLocal ? (byte) 1 : (byte) 0);
        dest.writeInt(this.localImageId);
        dest.writeString(this.video_url);
        dest.writeString(this.zipsize);
        dest.writeInt(this.discuss);
        dest.writeString(this.comments);
        dest.writeInt(this.awards);
        dest.writeString(this.price);
        dest.writeInt(this.edited_count);
        dest.writeString(this.template_id);
        dest.writeByte(this.isCollect ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    protected WorkDto(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.nick = in.readString();
        this.uid = in.readString();
        this.icon = in.readString();
        this.view = in.readString();
        this.collect = in.readString();
        long tmpCreated_at = in.readLong();
        this.created_at = tmpCreated_at == -1 ? null : new Date(tmpCreated_at);
        this.user_icon = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.isLocal = in.readByte() != 0;
        this.localImageId = in.readInt();
        this.video_url = in.readString();
        this.zipsize = in.readString();
        this.discuss = in.readInt();
        this.comments = in.readString();
        this.awards = in.readInt();
        this.price = in.readString();
        this.edited_count = in.readInt();
        this.template_id = in.readString();
        this.isCollect = in.readByte() != 0;
        this.isSelect = in.readByte() != 0;
    }

    public static final Creator<WorkDto> CREATOR = new Creator<WorkDto>() {
        @Override
        public WorkDto createFromParcel(Parcel source) {
            return new WorkDto(source);
        }

        @Override
        public WorkDto[] newArray(int size) {
            return new WorkDto[size];
        }
    };
}
