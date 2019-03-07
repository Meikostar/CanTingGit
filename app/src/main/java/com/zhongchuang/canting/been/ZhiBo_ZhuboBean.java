package com.zhongchuang.canting.been;

/**
 * Created by Administrator on 2017/11/16.
 */

public class ZhiBo_ZhuboBean {


    /**
     * status : 301
     * message : 成功
     * data : {"dirRoomLng":113.51667,"pushFlowAddress":"http://wsdfsa","roomNumber":"00002","playAddress":"http://wsdfsa","roomImage":"https://ss3.baidu.com/-rVXeDTa2gU2pMbgoY3K/it/u=331642910,199523927&fm=202&mola=new&crop=v1","dirRoomLat":22.3,"roomInfoId":"riid926350725875761152","liveId":"15289_00001"}
     */

    private int status;
    private String message;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * dirRoomLng : 113.51667
         * pushFlowAddress : http://wsdfsa
         * roomNumber : 00002
         * playAddress : http://wsdfsa
         * roomImage : https://ss3.baidu.com/-rVXeDTa2gU2pMbgoY3K/it/u=331642910,199523927&fm=202&mola=new&crop=v1
         * dirRoomLat : 22.3
         * roomInfoId : riid926350725875761152
         * liveId : 15289_00001
         */

        private double dirRoomLng;
        private String pushFlowAddress;
        private String roomNumber;
        private String playAddress;
        private String roomImage;
        private double dirRoomLat;
        private String roomInfoId;
        private String liveId;

        public double getDirRoomLng() {
            return dirRoomLng;
        }

        public void setDirRoomLng(double dirRoomLng) {
            this.dirRoomLng = dirRoomLng;
        }

        public String getPushFlowAddress() {
            return pushFlowAddress;
        }

        public void setPushFlowAddress(String pushFlowAddress) {
            this.pushFlowAddress = pushFlowAddress;
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        public void setRoomNumber(String roomNumber) {
            this.roomNumber = roomNumber;
        }

        public String getPlayAddress() {
            return playAddress;
        }

        public void setPlayAddress(String playAddress) {
            this.playAddress = playAddress;
        }

        public String getRoomImage() {
            return roomImage;
        }

        public void setRoomImage(String roomImage) {
            this.roomImage = roomImage;
        }

        public double getDirRoomLat() {
            return dirRoomLat;
        }

        public void setDirRoomLat(double dirRoomLat) {
            this.dirRoomLat = dirRoomLat;
        }

        public String getRoomInfoId() {
            return roomInfoId;
        }

        public void setRoomInfoId(String roomInfoId) {
            this.roomInfoId = roomInfoId;
        }

        public String getLiveId() {
            return liveId;
        }

        public void setLiveId(String liveId) {
            this.liveId = liveId;
        }
    }
}
