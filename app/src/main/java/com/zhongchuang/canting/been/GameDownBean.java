package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/11/29.
 */

public class GameDownBean extends BaseResponse{


    /**
     * status : 301
     * message : 成功
     * data : {"gameId":"935782502251888640","gameType":2,"gameName":"逃出烤箱","gameLogo":"http://39.104.49.78/apps/appIcon/Y3.png","gameDescription":"一根手指就能玩的游戏，看你能逃多远","gameLink":"http://39.104.49.78/apps/FlappyBirdStyle.apk","gameImages":[{"image":"http://39.104.49.78/apps/appImage/Y3/Y3_1.png"},{"image":"http://39.104.49.78/apps/appImage/Y3/Y3_2.png"},{"image":"http://39.104.49.78/apps/appImage/Y3/Y3_3.png"}],"gamePlay":"通过手指在屏幕上点击来保证小鸡一直处于飞行状态并且不要碰到任何障碍物，能走多远就看你的本事了","gamePackage":"com.kk.FlappyBirdStyle"}
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
         * gameId : 935782502251888640
         * gameType : 2
         * gameName : 逃出烤箱
         * gameLogo : http://39.104.49.78/apps/appIcon/Y3.png
         * gameDescription : 一根手指就能玩的游戏，看你能逃多远
         * gameLink : http://39.104.49.78/apps/FlappyBirdStyle.apk
         * gameImages : [{"image":"http://39.104.49.78/apps/appImage/Y3/Y3_1.png"},{"image":"http://39.104.49.78/apps/appImage/Y3/Y3_2.png"},{"image":"http://39.104.49.78/apps/appImage/Y3/Y3_3.png"}]
         * gamePlay : 通过手指在屏幕上点击来保证小鸡一直处于飞行状态并且不要碰到任何障碍物，能走多远就看你的本事了
         * gamePackage : com.kk.FlappyBirdStyle
         */

        private String gameId;
        private int gameType;
        private String gameName;
        private String gameLogo;
        private String gameDescription;
        private String gameLink;
        private String gamePlay;
        private String gamePackage;
        private List<GameImagesBean> gameImages;

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
        }

        public int getGameType() {
            return gameType;
        }

        public void setGameType(int gameType) {
            this.gameType = gameType;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public String getGameLogo() {
            return gameLogo;
        }

        public void setGameLogo(String gameLogo) {
            this.gameLogo = gameLogo;
        }

        public String getGameDescription() {
            return gameDescription;
        }

        public void setGameDescription(String gameDescription) {
            this.gameDescription = gameDescription;
        }

        public String getGameLink() {
            return gameLink;
        }

        public void setGameLink(String gameLink) {
            this.gameLink = gameLink;
        }

        public String getGamePlay() {
            return gamePlay;
        }

        public void setGamePlay(String gamePlay) {
            this.gamePlay = gamePlay;
        }

        public String getGamePackage() {
            return gamePackage;
        }

        public void setGamePackage(String gamePackage) {
            this.gamePackage = gamePackage;
        }

        public List<GameImagesBean> getGameImages() {
            return gameImages;
        }

        public void setGameImages(List<GameImagesBean> gameImages) {
            this.gameImages = gameImages;
        }

        public static class GameImagesBean {
            /**
             * image : http://39.104.49.78/apps/appImage/Y3/Y3_1.png
             */

            private String image;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }
    }
}