package com.zhongchuang.canting.been;

/**
 * Created by Administrator on 2017/11/25.
 */

public class CodeCheckBean extends BaseResponse{

    /**
     * status : 301
     * message : 成功
     * data : {"userInfoId":"urio934250097863032832","token":"396596","nickname":null,"ringLetterName":"urio934250097863032832","ringLetterPwd":"934250097863032832"}
     */


    private DataBean data;



    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userInfoId : urio934250097863032832
         * token : 396596
         * nickname : null
         * ringLetterName : urio934250097863032832
         * ringLetterPwd : 934250097863032832
         */

        private String userInfoId;
        private String token;
        private Object nickname;
        private String ringLetterName;
        private String ringLetterPwd;

        public String getUserInfoId() {
            return userInfoId;
        }

        public void setUserInfoId(String userInfoId) {
            this.userInfoId = userInfoId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Object getNickname() {
            return nickname;
        }

        public void setNickname(Object nickname) {
            this.nickname = nickname;
        }

        public String getRingLetterName() {
            return ringLetterName;
        }

        public void setRingLetterName(String ringLetterName) {
            this.ringLetterName = ringLetterName;
        }

        public String getRingLetterPwd() {
            return ringLetterPwd;
        }

        public void setRingLetterPwd(String ringLetterPwd) {
            this.ringLetterPwd = ringLetterPwd;
        }
    }
}
