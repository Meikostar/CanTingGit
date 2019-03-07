package com.zhongchuang.canting.easeui.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.FindFriendActivity;
import com.zhongchuang.canting.activity.chat.ContactListFragment;
import com.zhongchuang.canting.adapter.BannerAdapters;
import com.zhongchuang.canting.adapter.BannerAdaptes;
import com.zhongchuang.canting.adapter.ChooseAdapter;
import com.zhongchuang.canting.been.Banner;
import com.zhongchuang.canting.been.HXFriend;
import com.zhongchuang.canting.been.HXFriendListBean;
import com.zhongchuang.canting.db.DemoDBManager;
import com.zhongchuang.canting.easeui.domain.EaseUser;
import com.zhongchuang.canting.easeui.widget.ContactItemView;
import com.zhongchuang.canting.net.HXRequestService;
import com.zhongchuang.canting.net.netService;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.LogUtil;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.MPopupWindow;
import com.zhongchuang.canting.widget.banner.BannerBaseAdapter;
import com.zhongchuang.canting.widget.banner.BannerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/11/22.
 */

@SuppressLint("ValidFragment")
public class addFriendFragment extends ContactListFragment implements View.OnClickListener, ContactListFragment.EaseContactListItemClickListener, BaseContract.View {


    EditText tongxunluIpt;
    BannerView bannerView;


    private Context mContext;
    private PopupWindow mPopWindow;
    private List<HXFriend> friends = new ArrayList<>();
    private HXFriend place;

    public addFriendFragment() {
    }


    public void setType(String ids, int type) {
        this.ids = ids;
        this.type = type;
        mp.clear();
        if (TextUtil.isNotEmpty(ids)) {
            String[] split = ids.split(",");
            for (String id : split) {
                mp.put(id, id);
            }
        }
    }

    public addFriendFragment(Context mContext) {
        this.mContext = mContext;
    }

    public void setInfo(HXFriend place) {
        this.place = place;
        for (HXFriend hx : place.data) {
            String uid = hx.getChatUserId();

            LogUtil.d("好友的ID=" + uid);
            EaseUser easeUser = new EaseUser(uid);
            easeUser.setAvatar(hx.getHeadImage());
            easeUser.userid = hx.getChatUserId();
            easeUser.setNickname(hx.getNickname());
            maps.put(uid, easeUser);
        }
        setContactsMap(maps);
    }

    private BasesPresenter presenter;

    @Override
    protected void initView() {
        super.initView();
        status = 1;

        tongxunluIpt = super.getView().findViewById(R.id.query);
        bannerView = super.getView().findViewById(R.id.bannerView);
        presenter = new BasesPresenter(this);
        presenter.getBanners(2 + "");


        bannerAdapter = new BannerAdaptes(getActivity());
        bannerView.setAdapter(bannerAdapter);
        initListener();


    }

    /**
     * 关闭键盘
     */
    public void closeKeyBoard() {
        try {
            if (getActivity().getCurrentFocus() == null) {
                return;
            }
            IBinder iBinder = getActivity().getCurrentFocus().getWindowToken();
            if (iBinder == null) {
                return;
            }
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(iBinder, inputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Map<String, EaseUser> maps = new HashMap<>();


    @Override
    protected void setUpView() {

//        getFriendList();
        setContactListItemClickListener(this);
        super.setUpView();

    }

    private HXFriendListBean hxFriendListBean;

    private void getFriendList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(netService.TOM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        HXRequestService api = retrofit.create(HXRequestService.class);

        String userInfoId = SpUtil.getString(getActivity(), "userInfoId", "");
        String token = SpUtil.getString(getActivity(), "token", "");

        if (TextUtils.isEmpty(userInfoId)
                || TextUtils.isEmpty(token)
                ) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("userInfoId", userInfoId);
        map.put("token", token);
//            map.put("chatUserId", hxNameId);

        Call<HXFriendListBean> call = api.getFriendList(map);

        call.enqueue(new Callback<HXFriendListBean>() {
            @Override
            public void onResponse(Call<HXFriendListBean> call, Response<HXFriendListBean> response) {
                friends.clear();
                if (response == null) return;
                hxFriendListBean = response.body();
                LogUtil.d("好友返回数据=" + hxFriendListBean.toString());
                selectCont("");

            }

            @Override
            public void onFailure(Call<HXFriendListBean> call, Throwable t) {
                friends.clear();
                getContactList();
                contactListLayout.refresh();
            }
        });
    }
    public String name;
    public void selectCont(String content) {
        maps.clear();
        if (hxFriendListBean != null) {

            List<HXFriend> hxFriends = hxFriendListBean.getData();
            if ((hxFriends != null) && (hxFriends.size() > 0)) {
                LogUtil.d("好有个数=" + hxFriends.size());
                friends.addAll(hxFriends);

                for (HXFriend hx : hxFriends) {
                    String uid = hx.getChatUserId();
                    LogUtil.d("好友的ID=" + uid);
                    EaseUser easeUser = new EaseUser(uid);
                    easeUser.setAvatar(hx.getHeadImage());
                    easeUser.userid = hx.getChatUserId();
                    easeUser.setNickname(hx.getNickname());
                    if (TextUtil.isNotEmpty(content)) {
                        name=hx.getNickname();
                        if(TextUtil.isEmpty(name)){
                            name=hx.chatUserId;
                        }
                        if (name.contains(content)) {
                            maps.put(uid, easeUser);
                        }
                    } else {
                        maps.put(uid, easeUser);
                    }


                }

            }
        }
        bannerView.setVisibility(View.VISIBLE);
        setContactsMap(maps);
        getContactList();

        LogUtil.d("数据长度=" + contactList.size());
        DemoDBManager.getInstance().saveContactList(contactList);
        contactListLayout.refresh();
    }

    @Override
    public void refresh() {
//        super.refresh();
        if (place == null) {
            getFriendList();
        }

//        DemoDBManager.getInstance().saveContactList(contactList);
    }

    private void initListener() {

        tongxunluIpt.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactListLayout.filter(s);
                if (TextUtil.isNotEmpty(s.toString())) {
                    selectCont(s.toString());
                } else {
                    selectCont("");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        bannerAdapter.setOnPageTouchListener(new BannerBaseAdapter.OnPageTouchListener() {
            @Override
            public void onPageClick(int position, Object o) {
//                Banner banner= (Banner) o;
//                Intent intent = new Intent(getActivity(), ShopMallDetailActivity.class);
//                intent.putExtra("id", banner.product_sku_id);
//                intent.putExtra("type", 2);
//                startActivity(intent);
            }

            @Override
            public void onPageDown() {

            }

            @Override
            public void onPageUp() {

            }
        });

    }


    //popowindow  使用
    private void showPopupWindow() {

        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.tongxunlu_popuplayout, null);
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);


        LinearLayout tongxunluButQunliao = contentView.findViewById(R.id.tongxunlu_but_qunliao);
        LinearLayout tongxunluButFindFrd = contentView.findViewById(R.id.tongxunlu_but_findFrd);
        LinearLayout tongxunluButSaoyisao = contentView.findViewById(R.id.tongxunlu_but_saoyisao);


        tongxunluButQunliao.setOnClickListener(addFriendFragment.this);
        tongxunluButFindFrd.setOnClickListener(addFriendFragment.this);
        tongxunluButSaoyisao.setOnClickListener(addFriendFragment.this);
        //tongxunluRecy.setOnClickListener(TongXunLuFragment.this);

        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setFocusable(true);

        mPopWindow.showAsDropDown(bannerView, 0, 60);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tongxunlu_recy:
                mPopWindow.dismiss();
                break;
            case R.id.group_item:
                mPopWindow.dismiss();
                Intent fridIntent = new Intent(getActivity(), FindFriendActivity.class);
                startActivity(fridIntent);
                break;
            case R.id.tongxunlu_but_qunliao:
                mPopWindow.dismiss();
                break;
            case R.id.tongxunlu_but_findFrd:
                mPopWindow.dismiss();
                Intent intent = new Intent(getActivity(), GroupsActivity.class);
                startActivity(intent);
                break;
            case R.id.tongxunlu_but_saoyisao:
                mPopWindow.dismiss();
                break;
            case R.id.tongxunlu_bacbut:
                getActivity().finish();
                break;
            case R.id.tongxunlu_find:
                showPopupWindow();
                break;
            case R.id.tongxunlu_search:
                break;
            case R.id.tongxunlu_ipt:
                break;
        }
    }

    @Override
    public void onListItemClicked(EaseUser user) {
//        if (user != null) {
//
//
//            Intent intentc = new Intent(getActivity(), ChatActivity.class);
//            intentc.putExtra("userId",user.userid);
//            startActivity(intentc);
//        }
    }

    @Override
    public void onLongListItemClicked(EaseUser user) {

    }


    /**
     * 弹框
     */
    public MPopupWindow mPopupWindow;
    public ListView listView;
    public ChooseAdapter adapter;

    public View view;
    private BannerAdaptes bannerAdapter;

    @Override
    public <T> void toEntity(T entity, int type) {
        Banner banner = (Banner) entity;
        bannerAdapter.setData(banner.data);
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }
}

