package com.zhongchuang.canting.fragment.message;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.chat.AddGroupActivity;
import com.zhongchuang.canting.activity.chat.ChatMessageActivity;
import com.zhongchuang.canting.adapter.ChatMenberAdapter;
import com.zhongchuang.canting.adapter.FragmentViewPagerAdapter;
import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.FriendListBean;
import com.zhongchuang.canting.been.GAME;
import com.zhongchuang.canting.been.Hand;
import com.zhongchuang.canting.been.IMG;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.easeui.ui.MessageActivity;
import com.zhongchuang.canting.fragment.LiaoTianFragment;
import com.zhongchuang.canting.hud.HudHelper;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.widget.BaseDailogManager;
import com.zhongchuang.canting.widget.MarkaBaseDialog;
import com.zhongchuang.canting.widget.NoScrollViewPager;
import com.zhongchuang.canting.widget.RxBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/10/25.
 */
@SuppressLint("ValidFragment")
public class MessageChatFragment extends Fragment implements BaseContract.View {


    @BindView(R.id.viewpager_main)
    NoScrollViewPager viewpagerMain;
    @BindView(R.id.grid)
    GridView gridView;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;


    private List<Fragment> list_zhibofragment;   //定义要装fragment的列表
    private FragmentViewPagerAdapter mainViewPagerAdapter;

    private Context mContext;
    private String token;

    public static String pushURL;
    public static String roomID;
    public static String liveId;
    public static String roomPic;

    private ChatMenberAdapter adapter;

    public MessageChatFragment() {

    }

    public MessageChatFragment(Context context) {
        this.mContext = context;
    }


    private int current = 0;
    private GAME gameinfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_message, container, false);
        bind = ButterKnife.bind(this, view);

        checkPublishPermission();
        presenter = new BasesPresenter(this);
        adapter = new ChatMenberAdapter(getActivity());
        viewpagerMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.MESSAGECONT) {
                    List<EMConversation> emConversations = loadConversationList();
                    for (EMConversation data : emConversations) {
                        if (data.getUnreadMsgCount() > 0) {
                            String type = CanTingAppLication.list.get(data.conversationId());
                            for (GAME game : gameinfo.data) {
                                if (type.equals(game.id)) {
                                    game.havaMessage = true;
                                }
                            }
                        }
                    }


                }


            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);
        return view;

    }
    private MessageActivity msgListener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if(activity!=null){
                msgListener=(MessageActivity)activity;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement showMsgListener");
        }
    }

    private IMG guide_img = new IMG();


    private void iniGridView(final List<GAME> list) {

        int length = 69;  //定义一个长度
        int size = 0;  //得到集合长度
        //获得屏幕分辨路
        DisplayMetrics dm = new DisplayMetrics();
        if(dm==null||getActivity()==null){
            return;
        }
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int gridviewWidth = (int) (list.size() * (length + 5) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(10, 0, 0, 0);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(10); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(list.size()); // 设置列数量=列表集合数
        adapter.setDatas(list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.size() - 1 == position) {
                    Intent intent = new Intent(getActivity(), AddGroupActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), ChatMessageActivity.class);
                    intent.putExtra("game", list.get(position));
                    startActivity(intent);
                }


            }
        });
    }

    private BasesPresenter presenter;

    @Override
    public void onResume() {
        if (presenter != null) {
            presenter.getChatGroupList();
        }
        super.onResume();
    }

    public void getMessageCont() {

    }

    public List<EMConversation> message;

    /**
     * load conversation list
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug

            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    private Subscription mSubscription;
    private String cont = "";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gameinfo = (GAME) getArguments().getSerializable("data");
        if (gameinfo.data == null) {
            return;
        }
        List<GAME> dat = gameinfo.data;
        iniGridView(dat);
        if (gameinfo == null) {
            List<GAME> list = new ArrayList<>();
            gameinfo = new GAME();
            GAME game = new GAME();
            game.id = "0";
            game.cont = 1;
            game.chatGroupName = "全部";
            game.isChoose = true;

            GAME games = new GAME();
            games.id = "2";
            games.cont = 2;
            games.isChoose = false;
            games.directTypeName = "同事";
            GAME gamess = new GAME();
            gamess.id = "3";
            gamess.cont = 3;
            gamess.directTypeName = "朋友";

            list.add(game);
            list.add(games);
            list.add(gamess);
            gameinfo.data = list;

        } else {
            datas.clear();
            datas.addAll(gameinfo.data);

            datas.remove(datas.size() - 1);
        }
        initFragMents();
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgress("");
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        EMClient.getInstance().chatManager().markAllConversationsAsRead();


                        handler.sendEmptyMessageDelayed(1,1000);
                    }
                }).start();




            }
        });

    }
    private ProgressDialog pd;
    //进度条
    public void showProgress(String msg) {
        if (pd == null) {
            pd = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
            pd.setCanceledOnTouchOutside(false);
        }
        if (msg == null) {
            msg = "加载中...";
        }
        pd.setMessage(msg);
        pd.show();
    }

    public void dimessProgress() {
        if (pd != null) {
            pd.dismiss();
        }
    }
   public LiaoTianFragment liaoTianFragment;
    public List<GAME> datas = new ArrayList<>();
    public List<GAME> dat = new ArrayList<>();

    private void
    initFragMents() {
        list_zhibofragment = new ArrayList<>();
        if (gameinfo == null || gameinfo.data == null) {
            return;
        }

         liaoTianFragment = new LiaoTianFragment(getActivity(), null, 0 + "");

        list_zhibofragment.add(liaoTianFragment);


        mainViewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), list_zhibofragment);
        viewpagerMain.setAdapter(mainViewPagerAdapter);
        viewpagerMain.setOffscreenPageLimit(1);//设置缓存view 的个数
        viewpagerMain.setCurrentItem(current);

    }
   public Handler handler =new Handler(new Handler.Callback() {
       @Override
       public boolean handleMessage(Message msg) {
           RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.MESSAGENOTIFI,""));
           if(msgListener!=null){
               msgListener.getReadCout();
           }


           if(handler!=null){

               dimessProgress();
           }
           return false;
       }
   });

    /**
     * 6.0权限处理
     **/
    private boolean bPermission = false;
    private final int WRITE_PERMISSION_REQ_CODE = 100;
    private Unbinder bind;

    private boolean checkPublishPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(getActivity(),
                        (String[]) permissions.toArray(new String[0]),
                        WRITE_PERMISSION_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_PERMISSION_REQ_CODE:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                bPermission = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        bind.unbind();
    }

    @Override
    public <T> void toEntity(T entity, int type) {
        gameinfo = (GAME) entity;
        if (gameinfo != null && gameinfo.data != null && gameinfo.data.size() > 0) {
            List<GAME> dat = gameinfo.data;
            iniGridView(dat);

        } else {
            return;
        }


    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {

    }







    /*@OnClick({R.id.shouye_zhubo, R.id.zhubo_guanzhong})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shouye_zhubo:

                  Intent zhuboIntent = new Intent(getActivity(), ZhuBoActivity.class);
                startActivity(zhuboIntent);

                break;
            case R.id.zhubo_guanzhong:

                Intent guanZhongIntent = new Intent(getActivity(), GuanZhongActivity.class);
                startActivity(guanZhongIntent);
                break;
        }
    }*/


}



