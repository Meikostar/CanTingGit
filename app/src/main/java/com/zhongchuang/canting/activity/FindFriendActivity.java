package com.zhongchuang.canting.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.activity.CaptureActivity;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.chat.AddFriendActivity;
import com.zhongchuang.canting.adapter.Liaotian_haoyouRecAdapter;
import com.zhongchuang.canting.adapter.recycle.RecommendDetailRecycleAdapter;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.FriendSearchBean;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.Profit;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.easeui.ui.BaseActivity;
import com.zhongchuang.canting.hud.ToastUtils;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.HXRequestService;
import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.permission.PermissionConst;
import com.zhongchuang.canting.permission.PermissionGen;
import com.zhongchuang.canting.permission.PermissionSuccess;
import com.zhongchuang.canting.utils.KeyboardUtils;
import com.zhongchuang.canting.utils.LogUtil;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StatusBarUtils;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.DivItemDecoration;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.RxBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/11/23.
 */

public class FindFriendActivity extends BaseActivity {


    @BindView(R.id.find_bacbut)
    ImageView findBacbut;//back
    @BindView(R.id.query)
    EditText findFriendsearch;//搜索
    @BindView(R.id.find_frdRecy)
    SuperRecyclerView mSuperRecyclerView;//清单列表
    @BindView(R.id.iv_scan)
    ImageView iv_scan;
    @BindView(R.id.search_clear)
    ImageButton searchClear;
    @BindView(R.id.tv_seach)
    Button tvSeach;
//    private ArrayList<Integer> mList;


//    private ArrayList<String> tongxunimgUrlList;
//    private ArrayList<String> tongxunnicNameList;
//    private ArrayList<String> tongxunqianMinList;
//
//    private ArrayList<String> imgUrlList;//图片url集合
//    private ArrayList<String> nicNameList;//昵称集合
//    private ArrayList<String> qianMinList;//签名集合

    private Context mContext;
    private String mPhone;
    private String headImage;
    private String nickname;
    private String personalitySign;
    private Liaotian_haoyouRecAdapter mLiaotian_haoyouRecAdapter;
    private GAME game;
//    private TongXunLuFragment tongXunLuFragment;

    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private LinearLayoutManager mLinearLayoutManager;

    private final int TYPE_PULL_REFRESH = 1;
    private final int TYPE_PULL_MORE = 2;
    private final int TYPE_REMOVE = 3;
    public FindFriendActivity() {
    }

    public FindFriendActivity(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = (GAME) getIntent().getSerializableExtra("data");
        StatusBarUtils.setWindowStatusBarColor(this, getResources().getColor(R.color.wordColor));
        setContentView(R.layout.liaotian_findfriend);
        ButterKnife.bind(this);
        mLinearLayoutManager = new LinearLayoutManager(this);

        mSuperRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));
        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                mLiaotian_haoyouRecAdapter = new Liaotian_haoyouRecAdapter(FindFriendActivity.this, new ArrayList<FriendSearchBean.DataBean>());
        mSuperRecyclerView.setAdapter(mLiaotian_haoyouRecAdapter);
        initView();
        mPhone="1";

        reflash();
        // mSuperRecyclerView.setRefreshing(false);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSuperRecyclerView.showMoreProgress();

                SearchNet(currpage,TYPE_PULL_REFRESH);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mSuperRecyclerView != null) {
                            mSuperRecyclerView.hideMoreProgress();

                        }

                    }
                }, 2000);
            }
        };
        mSuperRecyclerView.setRefreshListener(refreshListener);
//        tongXunLuFragment = new TongXunLuFragment();
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {


            @Override
            public void onContactInvited(String username, String reason) {
                LogUtil.d("收到好友邀请");
                //收到好友邀请
                Toast.makeText(mContext, username + "-" + reason, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFriendRequestAccepted(String s) {
                LogUtil.d("对方同意添加好友了");
                //同意
                Toast.makeText(mContext, s + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFriendRequestDeclined(String s) {
                LogUtil.d("对方拒绝了");
                //拒绝
                Toast.makeText(mContext, s + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onContactDeleted(String username) {

                Toast.makeText(FindFriendActivity.this, getString(R.string.hybscl) + username, Toast.LENGTH_SHORT).show();
                //被删除时回调此方法

                new Thread() {//需要在子线程中调用
                    @Override
                    public void run() {
                        //需要设置联系人列表才能启动fragment
//                        tongXunLuFragment.setContactsMap(getContact());
//                        tongXunLuFragment.refresh();
                    }
                }.start();
            }


            @Override
            public void onContactAdded(String username) {
                LogUtil.d("添加好友了");
                //增加了联系人时回调此方法
                Toast.makeText(mContext, getString(R.string.tjhyl) + username, Toast.LENGTH_SHORT).show();

                new Thread() {//需要在子线程中调用
                    @Override
                    public void run() {
                        //需要设置联系人列表才能启动fragment
//                        tongXunLuFragment.setContactsMap(getContact());
//                        tongXunLuFragment.refresh();
                    }
                }.start();

            }
        });

        setEvents();

    }
    private void reflash() {
        if (mSuperRecyclerView != null) {
            //实现自动下拉刷新功能
            mSuperRecyclerView.getSwipeToRefresh().post(new Runnable() {
                @Override
                public void run() {
                    mSuperRecyclerView.setRefreshing(true);//执行下拉刷新的动画
                    refreshListener.onRefresh();//执行数据加载操作
                }
            });
        }
    }
    private void initView() {
        findFriendsearch.setHint(R.string.sjhnc);
//        findFrdRecy.setLayoutManager(new LinearLayoutManager(FindFriendActivity.this));


    }

    private void setEvents() {
        mLiaotian_haoyouRecAdapter.setOnAddClickListener(new Liaotian_haoyouRecAdapter.OnItemAddListener() {
            @Override
            public void onItemClick(FriendSearchBean.DataBean dataBean) {
                Intent intent = new Intent(FindFriendActivity.this, AddFriendActivity.class);
                intent.putExtra("data", game);
                intent.putExtra("datas", dataBean);
                startActivityForResult(intent, 2);
            }
        });
        //子条目点击事件处理
        mLiaotian_haoyouRecAdapter.setOnItemClickListener(new Liaotian_haoyouRecAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                            /*
                            String img=imgUrlList.get(position);
                            String name=nicNameList.get(position);
                            String qianmin=qianMinList.get(position);*/


//                            Intent mIntent=new Intent(FindFriendActivity.this,ChatMessageActivity.class);
//                            startActivity(mIntent);

//                            Toast.makeText(FindFriendActivity.this, "click " + position, Toast.LENGTH_SHORT).show();
            }
        });


        mLiaotian_haoyouRecAdapter.setOnItemLongClickListener(new Liaotian_haoyouRecAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(FindFriendActivity.this, "long click " + position, Toast.LENGTH_SHORT).show();
            }
        });


        findFriendsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                mPhone = findFriendsearch.getText().toString().trim();
                LogUtil.d("键盘搜索相应====" + mPhone);
                if (!TextUtils.isEmpty(mPhone)) {
                    currpage=1;
                    SearchNet(currpage,TYPE_PULL_REFRESH);
                }

                return true;
            }


        });
        iv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PermissionGen.with(FindFriendActivity.this)
                        .addRequestCode(PermissionConst.REQUECT_CODE_CAMERA)
                        .permissions(Manifest.permission.CAMERA)
                        .request();
            }
        });
        tvSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhone = findFriendsearch.getText().toString().trim();
                LogUtil.d("键盘搜索相应====" + mPhone);
                if (!TextUtils.isEmpty(mPhone)) {
                    currpage=1;
                    SearchNet(currpage,TYPE_PULL_REFRESH);
                }
            }
        });
        findFriendsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())) {
                    mLiaotian_haoyouRecAdapter.clear();
                }
            }
        });


    }

    @PermissionSuccess(requestCode = PermissionConst.REQUECT_CODE_CAMERA)
    public void requestSuccess() {
        Intent intent = new Intent(FindFriendActivity.this, CaptureActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                finish();
            } else {


                String result = data.getStringExtra("result");
                String[] userids = result.split(",");
                if (result.contains("@@!!##$$%%")) {
                    if (userids == null || userids.length != 3) {
                        return;
                    }
                    showPopwindows(userids[0], userids[1]);
                } else {
                    if (userids == null || userids.length != 3) {
                        return;
                    }
                    FriendSearchBean.DataBean dataBean = new FriendSearchBean.DataBean();
                    dataBean.setNickname(userids[0]);
                    dataBean.setRingLetterName(userids[1]);
                    Intent intent = new Intent(FindFriendActivity.this, AddFriendActivity.class);
                    intent.putExtra("data", game);
                    intent.putExtra("datas", dataBean);
                    startActivityForResult(intent, 2);
//                    showPopwindow(userids[0], userids[1]);
                }
            }

        }
    }

    private MarkaBaseDialog dialog;

    public void showPopwindows(final String name, final String id) {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(this, R.layout.base_dailog_view, null);
        sure = views.findViewById(R.id.txt_sure);
        cancel = views.findViewById(R.id.txt_cancel);
        title = views.findViewById(R.id.txt_title);

        title.setText("加入 " + name + " 群");
        dialog = BaseDailogManager.getInstance().getBuilder(this).setMessageView(views).create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addFriendList(id, name);
                dialog.dismiss();

            }
        });
    }

    public void addFriendList(String id, final String groupsName) {

        Map<String, String> map = new HashMap<>();
//        map.put("userInfoId", CanTingAppLication.userId);
        map.put("addusers", SpUtil.getUserInfoId(FindFriendActivity.this));
        map.put("groupId", id);
        map.put("groupsName", groupsName);

        netService api = HttpUtil.getInstance().create(netService.class);
        api.addFriendList(map).enqueue(new BaseCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse group) {
                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.REFRESSH, ""));
                ToastUtils.showNormalToast(getString(R.string.nyjs) + groupsName + getString(R.string.qcy));
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                ToastUtils.showNormalToast(t);
            }
        });
    }

    public void showPopwindow(final String name, final String id) {
        TextView sure = null;
        TextView cancel = null;
        TextView title = null;
        EditText reson = null;
        View views = View.inflate(this, R.layout.base_dailog_view, null);
        sure = views.findViewById(R.id.txt_sure);
        cancel = views.findViewById(R.id.txt_cancel);
        title = views.findViewById(R.id.txt_title);

        title.setText(getString(R.string.add) + name + getString(R.string.why));
        dialog = BaseDailogManager.getInstance().getBuilder(this).setMessageView(views).create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addFriendRequest(name, id);
                dialog.dismiss();

            }
        });
    }

    @OnClick({R.id.find_bacbut})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.find_bacbut:
                KeyboardUtils.hideSoftInput(this);
                this.finish();
                break;

        }
    }

    private void addFriendRequest(final String nickName, String hxNameId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(netService.TOM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        HXRequestService api = retrofit.create(HXRequestService.class);
        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", SpUtil.getUserInfoId(this));
        map.put("token", SpUtil.getString(this, "token", ""));
        map.put("chatUserId", hxNameId);

        Call<BaseResponse> call = api.addFriend(map);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse bs = response.body();

                Toast.makeText(FindFriendActivity.this, getString(R.string.hyqqfscg) + nickName + getString(R.string.hy), Toast.LENGTH_SHORT).show();
                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.FRIEND, ""));
                finish();


            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                LogUtil.d(t.toString());
                Toast.makeText(FindFriendActivity.this, getString(R.string.tjhysb), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SearchNet(int currpage,final int loadType) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(netService.TOM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        netService mFriendapi = retrofit.create(netService.class);

        String userInfoId = SpUtil.getString(this, "userInfoId", "");
        String token = SpUtil.getString(this, "token", "");
        String pageNum = currpage+"";
        String pageSize = "30";
        String findStr = mPhone;

        if (userInfoId.equals("") || TextUtils.isEmpty(userInfoId)
                || token.equals("") || TextUtils.isEmpty(token)
                ) {
            return;
        }

        Map<String, String> map = new HashMap();

        map.put("userInfoId", userInfoId);
        map.put("token", token);
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);
        map.put("findStr", findStr);

        Call<FriendSearchBean> call = mFriendapi.getFriendMessage(map);


        call.enqueue(new Callback<FriendSearchBean>() {

            @Override
            public void onResponse(Call<FriendSearchBean> call, Response<FriendSearchBean> response) {

                FriendSearchBean mFriendBean = response.body();
                int code = mFriendBean.getStatus();
                LogUtil.d("请求响应了" + mFriendBean.toString());
                final List<FriendSearchBean.DataBean> mListBean = mFriendBean.getData();

                if (code == 301) {
                    onDataLoaded(loadType,mListBean.size()>=30,mListBean);
//                    findFrdRecy.setLayoutManager(new LinearLayoutManager(FindFriendActivity.this));
//                    mLiaotian_haoyouRecAdapter = new Liaotian_haoyouRecAdapter(FindFriendActivity.this, mListBean);


                    call.cancel();
                    return;


                }


                if (code == 302) {
                   /* findFrdRecy.setLayoutManager(new LinearLayoutManager(FindFriendActivity.this));
                    findFrdRecy.setAdapter(new Liaotian_haoyouRecAdapter(FindFriendActivity.this, imgUrlList, nicNameList, qianMinList));*/
                    Toast.makeText(FindFriendActivity.this, R.string.shcs, Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(FindFriendActivity.this, R.string.wcr, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<FriendSearchBean> call, Throwable t) {

                Toast.makeText(FindFriendActivity.this, R.string.wlbgl, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public List<FriendSearchBean.DataBean> list = new ArrayList<>();
    public List<FriendSearchBean.DataBean> data = new ArrayList<>();
    public int currpage = 1;

    public void onDataLoaded(int loadtype, final boolean haveNext, List<FriendSearchBean.DataBean> datas) {

        if (loadtype == TYPE_PULL_REFRESH) {
            currpage = 1;
            list.clear();
            for (FriendSearchBean.DataBean info : datas) {
                list.add(info);
            }
        } else {
            for (FriendSearchBean.DataBean info : datas) {
                list.add(info);
            }
        }
        mLiaotian_haoyouRecAdapter.setData(list);
        mLiaotian_haoyouRecAdapter.notifyDataSetChanged();
        mSuperRecyclerView.setLoadingMore(false);
        mSuperRecyclerView.hideMoreProgress();
        /**
         * 判断是否需要加载更多，与服务器的总条数比
         */
        if (haveNext) {
            mSuperRecyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                    currpage++;
                    mSuperRecyclerView.showMoreProgress();
                    SearchNet(currpage,TYPE_PULL_MORE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (haveNext)
                                mSuperRecyclerView.hideMoreProgress();


                        }
                    }, 2000);
                }
            }, 1);
        } else {
            mSuperRecyclerView.removeMoreListener();
            mSuperRecyclerView.hideMoreProgress();

        }
    }

}
