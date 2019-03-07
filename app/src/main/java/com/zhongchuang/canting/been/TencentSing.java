package com.zhongchuang.canting.been;

/**
 * Created by Administrator on 2017/11/18.
 */

public class TencentSing {

    /**
     * status : 301
     * message : 成功
     * data : {"signStr":"eJxlj01PhDAURff8CsLaaKGUgokLdGb8CI7GcSDOhiAtzAuhYCkjaPzvIk5iE9-2nNz77qdhmqb1HG1OszxveqFSNbbcMs9NC1knf7BtgaWZSrFk-yAfWpA8zQrF5QxtQoiDkO4A40JBAUejl9AEGHm*59q*49mYUuS4mt*xKp1LfwPdKY0gzw10BcoZ3i*3V7eXa7yvX5Jy90bVcDPELIzdqs7o4MU1Zk9dlBRxkuHksV*FsAzH17LZiQW5k4cI6LoiFOV9uCeD*Mg38apdnG3t-vr9YYyaC61SQc2PC20UTDN9-ecDlx00YhYcNCkORj9nGV-GN9G*YXA_"}
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
         * signStr : eJxlj01PhDAURff8CsLaaKGUgokLdGb8CI7GcSDOhiAtzAuhYCkjaPzvIk5iE9-2nNz77qdhmqb1HG1OszxveqFSNbbcMs9NC1knf7BtgaWZSrFk-yAfWpA8zQrF5QxtQoiDkO4A40JBAUejl9AEGHm*59q*49mYUuS4mt*xKp1LfwPdKY0gzw10BcoZ3i*3V7eXa7yvX5Jy90bVcDPELIzdqs7o4MU1Zk9dlBRxkuHksV*FsAzH17LZiQW5k4cI6LoiFOV9uCeD*Mg38apdnG3t-vr9YYyaC61SQc2PC20UTDN9-ecDlx00YhYcNCkORj9nGV-GN9G*YXA_
         */

        private String signStr;

        public String getSignStr() {
            return signStr;
        }

        public void setSignStr(String signStr) {
            this.signStr = signStr;
        }
    }
}
