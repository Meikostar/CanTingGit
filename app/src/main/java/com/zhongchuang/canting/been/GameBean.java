package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/11/25.
 */

public class GameBean {


    /**
     * status : 301
     * message : 成功
     * data : [{"gameId":"935782502251888640","gameName":"逃出烤箱","gameLogo":"http://39.104.49.78/apps/appIcon/Y3.png","gameDescription":"一根手指就能玩的游戏，看你能逃多远","gamePackage":"com.kk.FlappyBirdStyle"},{"gameId":"935782988724043776","gameName":"欢乐切水果","gameLogo":"http://39.104.49.78/apps/appIcon/Y4.png","gameDescription":"尽情用你的手指在屏幕上滑动吧，不过小心炸弹","gamePackage":"com.Fruit.CutFruit"}]
     */

    private int status;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * gameId : 935782502251888640
         * gameName : 逃出烤箱
         * gameLogo : http://39.104.49.78/apps/appIcon/Y3.png
         * gameDescription : 一根手指就能玩的游戏，看你能逃多远
         * gamePackage : com.kk.FlappyBirdStyle
         */

        private String gameId;
        private String gameName;
        private String gameLogo;
        private String gameDescription;
        private String gamePackage;

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
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

        public String getGamePackage() {
            return gamePackage;
        }

        public void setGamePackage(String gamePackage) {
            this.gamePackage = gamePackage;
        }
    }
}
