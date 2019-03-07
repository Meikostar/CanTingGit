package com.zhongchuang.canting.easeui.bean;




import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.zhongchuang.canting.install.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by syj on 2016/11/30.
 */
public class GROUPS implements Serializable {


    /**
     * guid : 31
     * groupname : 迈卡
     * easemob_group_id : 272158770295996944
     * user_id : 27
     * gid : 9
     * group_img : ["http://concrete.yunchedian.com/Upload/avatar/aef619c4-adea-34f2-841c-63f576bd1337.jpg"]
     */

    // 记得导入ormlite库
    @DatabaseField(generatedId = true)
    private String group_id;
    @DatabaseField
    private int guid;
    @DatabaseField
    private String groupname;
    @DatabaseField
    private String easemob_group_id;
    private String user_id;
    @DatabaseField
    private String gid;
    private ArrayList<String> group_img;
    @DatabaseField
    private String group_img_str;
    /**
     * gid : 12
     * group_host : 27
     * add_time : 1480931882
     * easemob_group_id : 272159547219509780
     * project_place : []
     * user_groupinfo : {"guid":34,"gid":12,"user_id":27,"nickname":"","user_type":0,"user_positioning":"0,0","is_positioning":0,"is_disturb":0,"is_map":0,"is_top":0,"add_time":0,"is_host":1}
     */

    private int group_host;
    private int add_time;
    private List<PLACE> project_place;
    private GROUP_USER user_groupinfo;
    /**
     * gid : 16
     * user_id : 18
     * nickname :
     * user_type : 0
     * user_positioning : 0,0
     * is_positioning : 0
     * is_disturb : 0
     * is_map : 0
     * is_top : 0
     * easemob_group_id : 276175539079741972
     * group_img : ["http://concrete.yunchedian
     * .com/Upload/avatar/4fb7a34c-f072-eb94-a674-8b026a45b453.jpg","http://concrete.yunchedian
     * .com/Upload/avatar/8cdc1b45-cd8c-824f-bc7d-9ed599630551.jpg","http://concrete.yunchedian
     * .com/Upload/avatar/92d75054-04ed-ca36-b39c-6bb46582a596.jpg"]
     */

    private String nickname;
    private int user_type;
    private String user_positioning;
    private int is_positioning;
    @DatabaseField
    private int is_disturb;
    private int is_map;
    private int is_top;
    private int user_count;

    public int getUser_count() {
        return user_count;
    }

    public void setUser_count(int user_count) {
        this.user_count = user_count;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_img_str() {
//        if (TextUtils.isEmpty(group_img_str)) {
//            setGroup_img_str(Utils.getGroupImgStr(group_img));
//            return getGroup_img_str();
//        }
        return group_img_str;
    }

    public void setGroup_img_str(String group_img_str) {
        this.group_img_str = group_img_str;
    }

    public List<PLACE> getProject_place() {
        return project_place;
    }

    public void setProject_place(List<PLACE> project_place) {
        this.project_place = project_place;
    }

    public GROUP_USER getUser_groupinfo() {
        return user_groupinfo;
    }

    public void setUser_groupinfo(GROUP_USER user_groupinfo) {
        this.user_groupinfo = user_groupinfo;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public int getGroup_host() {
        return group_host;
    }

    public void setGroup_host(int group_host) {
        this.group_host = group_host;
    }

    public int getGuid() {
        return guid;
    }

    public void setGuid(int guid) {
        this.guid = guid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getEasemob_group_id() {
        return easemob_group_id;
    }

    public void setEasemob_group_id(String easemob_group_id) {
        this.easemob_group_id = easemob_group_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public ArrayList<String> getGroup_img() {
        return group_img;
    }

    public void setGroup_img(ArrayList<String> group_img) {
        this.group_img = group_img;
        //setGroupname(Utils.getGroupImgStr(group_img));
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getUser_positioning() {
        return user_positioning;
    }

    public void setUser_positioning(String user_positioning) {
        this.user_positioning = user_positioning;
    }

    public int getIs_positioning() {
        return is_positioning;
    }

    public void setIs_positioning(int is_positioning) {
        this.is_positioning = is_positioning;
    }

    public int getIs_disturb() {
        return is_disturb;
    }

    public void setIs_disturb(int is_disturb) {
        this.is_disturb = is_disturb;
    }

    public int getIs_map() {
        return is_map;
    }

    public void setIs_map(int is_map) {
        this.is_map = is_map;
    }

    public int getIs_top() {
        return is_top;
    }

    public void setIs_top(int is_top) {
        this.is_top = is_top;
    }

    public String getGavatar() {
        if(!TextUtils.isEmpty(group_img_str)){
            return group_img_str;
        }
        if(group_img != null && group_img.size()>0){
            return Utils.getGroupImgStr(group_img);
        }
        return "";
    }
}
