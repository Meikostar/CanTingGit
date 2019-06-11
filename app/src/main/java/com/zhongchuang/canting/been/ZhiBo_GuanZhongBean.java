package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/11/16.
 */

public class ZhiBo_GuanZhongBean extends BaseResponse{


    /**
     * status : 301
     * message : 成功
     * data : [{"roomNumber":"00002","roomImage":"https://ss3.baidu.com/-rVXeDTa2gU2pMbgoY3K/it/u=331642910,199523927&fm=202&mola=new&crop=v1","nickname":"落叶知秋","directSeeName":"小叶专用直播间","roomInfoId":"riid926350725875761152","playAddress":"播放地址","onlineNumber":"在线人数"},{"roomNumber":"00001","roomImage":"https://ss3.baidu.com/-rVXeDTa2gU2pMbgoY3K/it/u=331642910,199523927&fm=202&mola=new&crop=v1","nickname":"防守打法","directSeeName":"叶直播间","roomInfoId":"riid92635072587576112","playAddress":"播放地址","onlineNumber":"在线人数"}]
     */


    private List<DataBean> data;



    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * roomNumber : 00002
         * roomImage : https://ss3.baidu.com/-rVXeDTa2gU2pMbgoY3K/it/u=331642910,199523927&fm=202&mola=new&crop=v1
         * nickname : 落叶知秋
         * directSeeName : 小叶专用直播间
         * roomInfoId : riid926350725875761152
         * playAddress : 播放地址
         * onlineNumber : 在线人数
         */

        public String roomNumber;
        public String roomImage;
        public String nickname;
        public String leave_massege;
        public String headImage;
        public String directSeeName;
        public String roomInfoId;
        public String playAddress;
        public String direct_see_name;
        public String room_image;
        public String room_number;
        public int is_enabled;
        public String direct_overview;
        public String dir_type_id;
        public String user_info_nickname;
        public String room_info_id;
        public String fans_num;
        public String user_info_id;
        public String onlineNumber;
        public String create_time;
        public String id;
        public String cover_image;
        public String type;
        public String video_type;
        public String new_type;
        public String request_id;
        public String video_name;
        public String video_url;


        public String getRoomNumber() {
            return roomNumber;
        }

        public void setRoomNumber(String roomNumber) {
            this.roomNumber = roomNumber;
        }

        public String getRoomImage() {
            return roomImage;
        }

        public void setRoomImage(String roomImage) {
            this.roomImage = roomImage;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getDirectSeeName() {
            return directSeeName;
        }

        public void setDirectSeeName(String directSeeName) {
            this.directSeeName = directSeeName;
        }

        public String getRoomInfoId() {
            return roomInfoId;
        }

        public void setRoomInfoId(String roomInfoId) {
            this.roomInfoId = roomInfoId;
        }

        public String getPlayAddress() {
            return playAddress;
        }

        public void setPlayAddress(String playAddress) {
            this.playAddress = playAddress;
        }

        public String getOnlineNumber() {
            return onlineNumber;
        }

        public void setOnlineNumber(String onlineNumber) {
            this.onlineNumber = onlineNumber;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "roomNumber='" + roomNumber + '\'' +
                    ", roomImage='" + roomImage + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", directSeeName='" + directSeeName + '\'' +
                    ", roomInfoId='" + roomInfoId + '\'' +
                    ", playAddress='" + playAddress + '\'' +
                    ", onlineNumber='" + onlineNumber + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ZhiBo_GuanZhongBean{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
