package com.zhongchuang.canting.easeui.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.easeui.adapter.EaseContactAdapter;
import com.zhongchuang.canting.easeui.domain.EaseUser;
import com.zhongchuang.canting.easeui.utils.EaseUserUtils;
import com.zhongchuang.canting.easeui.widget.EaseSidebar;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickAtUserActivity extends BaseActivity {

    View headerView;

    String groupId;
    EMGroup group;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;
    @BindView(R.id.list)
    ListView listView;
    @BindView(R.id.sidebar)
    EaseSidebar sidebar;
    @BindView(R.id.floating_header)
    TextView floatingHeader;
     private String userNames;
     private String userIds;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_pick_at_user);
        ButterKnife.bind(this);

        groupId = getIntent().getStringExtra("groupId");
        group = EMClient.getInstance().groupManager().getGroup(groupId);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sidebar.setListView(listView);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<EaseUser> contacts = adapter.getUserList();

                int i = 0;
                userNames="";
                userIds="";
                for (EaseUser user : contacts) {
                    if (user.isChoose) {
                        if (i == 0) {
                            userIds = user.getUsername();
                            userNames =getNick(user.getNickname());
//                            userNames = EaseUserUtils.getUserNick(user.userid);
                        } else {
                            userIds = userIds + "," + user.getUsername();
                            userNames = userNames + "@" +getNick(user.getNickname());
                        }
                        i++;
                    }

                }
                if(TextUtil.isNotEmpty(userIds)){
                    Intent intent = new Intent();
                    intent.putExtra("username", userNames);
                    intent.putExtra("userid", userIds);
                    setResult(RESULT_OK, intent);
                }

                 finish();
//                gategoryFragment.addMenber();
            }
        });
        updateList();

        updateGroupData();
    }
    public String getNick(String id){
        if(CanTingAppLication.easeDatas==null){
            return id;
        }else {
            String nick = CanTingAppLication.easeDatas.get(id);
            if(TextUtil.isNotEmpty(nick)){
                String[] split = nick.split(",");
                nick=split[0];
                return nick;
            }else {
                return id;
            }
        }


    }
    void updateGroupData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    group = EMClient.getInstance().groupManager().getGroupFromServer(groupId);
                    EMClient.getInstance().groupManager().fetchGroupMembers(groupId, "", 200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateList();
                    }
                });
            }
        }).start();
    }
    private PickUserAdapter adapter;
    void updateList() {
        List<String> members = group.getMembers();
        List<EaseUser> userList = new ArrayList<EaseUser>();
        members.addAll(group.getAdminList());
        members.add(group.getOwner());
        for (String username : members) {
            EaseUser user = EaseUserUtils.getUserInfo(username);
            if(!EMClient.getInstance().getCurrentUser().equals(user.getUsername())){
                userList.add(user);
            }

        }

        Collections.sort(userList, new Comparator<EaseUser>() {

            @Override
            public int compare(EaseUser lhs, EaseUser rhs) {
                if (lhs.getInitialLetter().equals(rhs.getInitialLetter())) {
                    return lhs.getNickname().compareTo(rhs.getNickname());
                } else {
                    if ("#".equals(lhs.getInitialLetter())) {
                        return 1;
                    } else if ("#".equals(rhs.getInitialLetter())) {
                        return -1;
                    }
                    return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                }

            }
        });
        final boolean isOwner = EMClient.getInstance().getCurrentUser().equals(group.getOwner());
        if (isOwner) {
            addHeadView();
        } else {
            if (headerView != null) {
                listView.removeHeaderView(headerView);
                headerView = null;
            }
        }
       adapter= new PickUserAdapter(this, 0, userList, 1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isOwner) {
                    if (position != 0) {
                        adapter.notifyDataSetChanged();
//                        EaseUser user = (EaseUser) listView.getItemAtPosition(position);
//                        if (EMClient.getInstance().getCurrentUser().equals(user.getUsername()))
//                            return;
//                        setResult(RESULT_OK, new Intent().putExtra("username", user.getUsername()));
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("username", getString(R.string.all_members));
                        intent.putExtra("userid", "ALL");
                        setResult(RESULT_OK,intent );
                        finish();
                    }

                } else {
                    adapter.notifyDataSetChanged();

//                    EaseUser user = (EaseUser) listView.getItemAtPosition(position);
//                    if (EMClient.getInstance().getCurrentUser().equals(user.getUsername()))
//                        return;
//                    if(user.isChoose){
//                        user.isChoose=false;
//                    }else {
//                        user.isChoose=true;
//
//                    }

//                    setResult(RESULT_OK, new Intent().putExtra("username", user.getUsername()));
                }


            }
        });
    }

    private void addHeadView() {
        if (listView.getHeaderViewsCount() == 0) {
            View view = LayoutInflater.from(this).inflate(R.layout.ease_row_contacts, listView, false);
            ImageView avatarView = view.findViewById(R.id.avatar);
            TextView textView = view.findViewById(R.id.name);
            textView.setText(getString(R.string.all_members));
            avatarView.setImageResource(R.drawable.ease_groups_icon);
            listView.addHeaderView(view);
            headerView = view;
        }
    }

    public void back(View view) {
        finish();
    }

    private class PickUserAdapter extends EaseContactAdapter {

        public PickUserAdapter(Context context, int resource, List<EaseUser> objects, int status) {
            super(context, resource, objects, status);
        }
    }
}
