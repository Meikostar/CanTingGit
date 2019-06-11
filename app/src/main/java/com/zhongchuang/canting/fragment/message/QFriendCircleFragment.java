package com.zhongchuang.canting.fragment.message;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.activity.chat.QfriendActivity;
import com.zhongchuang.canting.activity.chat.SendDynamicActivity;
import com.zhongchuang.canting.adapter.recycle.QFriendRecyleAdapter;
import com.zhongchuang.canting.base.BaseFragment;
import com.zhongchuang.canting.been.CommetLikeBean;
import com.zhongchuang.canting.been.QfriendBean;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.presenter.BasesPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;
import com.zhongchuang.canting.widget.RxBus;
import com.zhongchuang.canting.widget.popupwindow.PopView_CancelOrSure;
import com.zhongchuang.canting.widget.popupwindow.PopView_Comment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by mykar on 17/4/10.
 */
public class QFriendCircleFragment extends BaseFragment implements BaseContract.View {


    @BindView(R.id.tv_send)
    ImageView tvSend;
    @BindView(R.id.fr_send)
    FrameLayout frSend;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;
    @BindView(R.id.rl_body_area)
    RelativeLayout mTodayNewGoodsBodyArea;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.rl_bg)
    RelativeLayout rl_bg;

    @BindView(R.id.tv_cout)
    TextView tvCout;

    private LinearLayoutManager mLinearLayoutManager;

    private SwipeRefreshLayout.OnRefreshListener refreshListener;

    private QFriendRecyleAdapter mTodayNewGoodsAdapter;

    private final static int TYPE_PULL_REFRESH = 1;
    private final static int TYPE_PULL_MORE = 2;

    private BasesPresenter presenter;

    private int currpage = 1;

    private int poistion;
    private int type = 0;
    private int commentPosition;
    private String username = SpUtil.getNick(getActivity());
    private String userid = SpUtil.getUserInfoId(getActivity());
    private String myname;
    List<QfriendBean> list = new ArrayList<>();
    List<QfriendBean> lists;
    List<String> prase_list = new ArrayList<>();
    QfriendBean info;
    private boolean isInit = false;
    private boolean isFirst = false;
    List<CommetLikeBean> comments = new ArrayList<>();
    private Subscription mSubscription;
    private PopView_Comment popComment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qfridnd, null);
        ButterKnife.bind(this, view);
        presenter = new BasesPresenter(this);
        initViews();
        if (type != 0) {
            ivBack.setVisibility(View.VISIBLE);
            tvTitle.setText(R.string.wdllq);
        }
        if(type==6){
            rl_bg.setVisibility(View.GONE);
        }
        if (TextUtil.isNotEmpty(conts)) {
            ivBack.setVisibility(View.VISIBLE);
            tvTitle.setText(R.string.llqxx);
            tvSend.setVisibility(View.GONE);
        }
        if (TextUtil.isNotEmpty(id)) {
            ivBack.setVisibility(View.VISIBLE);
            tvTitle.setText(R.string.tdllq);
            tvSend.setVisibility(View.GONE);
        }
//        mSuperRecyclerView.setNestedScrollingEnabled(false);
        bindEvents();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        reflash();
        haveComment();
    }

    public void setType(int type) {
        this.type = type;
    }

    private String conts;

    public void setData(String data) {
        this.conts = data;
    }
    private String id;

    public void setId(String data) {
        this.id = data;
    }
        public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    private PopView_CancelOrSure popView_cancelOrSure;
    private PopView_CancelOrSure popView_cancelOrSures;

    public void initViews() {
        iniPopView();
        popComment = new PopView_Comment(getActivity());
        popComment.setOnPop_CommentListenerr(new PopView_Comment.OnPop_CommentListener() {
            @Override
            public void chooseDeviceConfig(String commentStr) {
                sendNewGoodsComment(commentStr);
            }
        });
        myname = SpUtil.getName(getActivity());
        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        mSuperRecyclerView.setLayoutManager(mLinearLayoutManager);

        //  mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));

        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

//        mSuperRecyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (mEditNewGoodsComment.getVisibility() == View.VISIBLE) {
//                    updateEditTextBodyVisible(View.GONE, null);
//                    return true;
//                }
//                return false;
//            }
//        });
//        mTodayNewGoodsPresenter.loadData(1,0,TYPE_PULL_REFRESH);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (TextUtil.isNotEmpty(conts)) {
                    presenter.getFriendById(conts);
                } else {
                    presenter.getFriendCirclesList(1, currpage + "", type == 0 ? null : (TextUtil.isNotEmpty(id)?id:userid));
                }


            }
        };

        mSuperRecyclerView.setRefreshListener(refreshListener);

        mSuperRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                try {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        Glide.with(getActivity()).resumeRequests();
                    } else {
                        Glide.with(getActivity()).pauseRequests();
                    }
                } catch (Exception e) {

                }

            }
        });

        mTodayNewGoodsAdapter = new QFriendRecyleAdapter(getActivity());
        mTodayNewGoodsAdapter.setType(type);
        mSuperRecyclerView.setAdapter(mTodayNewGoodsAdapter);


        frSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SendDynamicActivity.class));
            }
        });


    }

    private String ids;

    public void haveComment() {
        String qfriend = SpUtil.getString(getActivity(), "qfriend", "");

        if (TextUtil.isNotEmpty(qfriend)) {
            String[] split = qfriend.split(",");
            tvCout.setText(split.length + getString(R.string.twdxx));
            String[] split1 = split[0].split("#");
            int i = 0;
            for (String data : split) {
                String[] split2 = data.split("#");
                if (i == 0) {
                    ids = split2[0];
                } else {
                    if (!ids.contains(split2[0])) {
                        ids = ids + split2[0];
                    }

                }
                i++;
            }
            llBg.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(StringUtil.changeUrl(split1[1])).asBitmap().placeholder(R.drawable.moren).into(ivImg);
        } else {
            llBg.setVisibility(View.GONE);
        }

    }

    private void iniPopView() {
        popView_cancelOrSure = new PopView_CancelOrSure(getActivity());
        popView_cancelOrSure.setTitle(getString(R.string.qrscgtd));
        popView_cancelOrSure.setOnPop_CancelOrSureLister(new PopView_CancelOrSure.OnPop_CancelOrSureLister() {
            @Override
            public void choose4Sure() {
                /**
                 * 删除
                 */
                if (TextUtil.isNotEmpty(qfd_id) && TextUtil.isNotEmpty(sendId)) {
                    presenter.delFriendCircle(qfd_id, sendId);
                }

            }
        });

        popView_cancelOrSures = new PopView_CancelOrSure(getActivity());
        popView_cancelOrSures.setTitle(getString(R.string.qrscgpl));
        popView_cancelOrSures.setOnPop_CancelOrSureLister(new PopView_CancelOrSure.OnPop_CancelOrSureLister() {
            @Override
            public void choose4Sure() {
                /**
                 * 删除
                 */
                if (TextUtil.isNotEmpty(qfd_id) && TextUtil.isNotEmpty(sendId)) {
                    presenter.delFriendComment(qfd_id, sendId, fromId, Id);
                }

            }
        });
    }

    private boolean isProse;
    private List<CommetLikeBean> datas = new ArrayList<>();
    private String sendId;
    private String Id;
    private String toId;
    private String fromId;

    public void bindEvents() {

        llBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QfriendActivity.class);
                intent.putExtra("ids", ids);
                startActivity(intent);
                SpUtil.putString(getActivity(), "qfriend", "");
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mTodayNewGoodsAdapter.setListener(new QFriendRecyleAdapter.QFriendClickListener() {
            @Override
            public void clicks(int poistions, int commentPositions, QfriendBean infos) {
                commentPosition = commentPositions;
                qfd_id = infos.id;

                if (commentPositions == -1) {
                    commentPosition = -1;
                    to_id = userid;
                    toId= infos.user_info_id;
                    sendId = infos.user_info_id;
                    info = infos;
                    poistion = poistions;
                    popComment.showPopView("");
                } else if (commentPositions == -2) {
                    poistion = poistions;
                    isProse = false;
                    datas.clear();
                    if (infos.likeList != null && infos.likeList.size() > 0) {
                        for (CommetLikeBean commetLikeBean : infos.likeList) {
                            if (commetLikeBean.from_uid.equals(userid)) {
                                isProse = true;
                            } else {
                                datas.add(commetLikeBean);
                            }
                        }
                        if (!isProse) {
                            CommetLikeBean bean = new CommetLikeBean();
                            bean.from_nickname = username;
                            bean.from_uid = userid;
                            datas.add(bean);
                        }
                    }
                    list.get(poistions).likeList = datas;
                    presenter.changeFriendLike(qfd_id, isProse ? "2" : "1");
                } else if (commentPositions == -3) {
                    sendId = infos.user_info_id;
                    poistion = poistions;
                    popView_cancelOrSure.showPopView();
                } else {
                    to_id = infos.commentList.get(commentPosition).from_uid;
                    toId = infos.commentList.get(commentPosition).from_uid;
                    popComment.showPopView("");
                    if (TextUtil.isNotEmpty(infos.commentList.get(commentPosition).from_uname)) {
                        popComment.setTextHints(getString(R.string.hf) + infos.commentList.get(commentPosition).from_uname + ":");

                    } else {
                        popComment.setTextHints(getString(R.string.pl) + infos.commentList.get(commentPosition).from_uname + ":");
                    }

                    poistion = poistions;
                    info = infos;

                }
            }

            @Override
            public void click(int poistions, int commentPosition, QfriendBean infos) {
                qfd_id = infos.id;
                sendId = infos.user_info_id;
                Id = infos.commentList.get(commentPosition).id;
                fromId = infos.commentList.get(commentPosition).from_uid;

                poistion = poistions;
                popView_cancelOrSures.showPopView();
            }
        });

        mSubscription = RxBus.getInstance().toObserverable(SubscriptionBean.RxBusSendBean.class).subscribe(new Action1<SubscriptionBean.RxBusSendBean>() {
            @Override
            public void call(SubscriptionBean.RxBusSendBean bean) {
                if (bean == null) return;
                if (bean.type == SubscriptionBean.QFRIED_FRESH) {
                    reflash();
                } else if (bean.type == SubscriptionBean.QFRIED_SEND_FRESH) {

                    haveComment();

                }

            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        RxBus.getInstance().addSubscription(mSubscription);

    }

    public void initData() {
//        if (!isInit) {
//            reflash();
//            isInit = true;
//        }

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


    private void sendNewGoodsComment(String commentStr) {
        String content = commentStr.trim();

        presenter.addFriendComment(qfd_id, to_id, userid, content);


    }

    private String qfd_id;
    private String to_id;

    /**
     * 获取数据回调
     *
     * @param loadType
     * @param havaNext
     * @param datas
     */

    public void onDataLoaded(int loadType, boolean havaNext, List<QfriendBean> datas) {
        if (loadType == TYPE_PULL_REFRESH) {
            currpage = 1;
            list.clear();
            for (QfriendBean info : datas) {
                list.add(info);
            }
        } else {
            for (QfriendBean info : datas) {
                list.add(info);
            }
        }
        lists = list;
        mTodayNewGoodsAdapter.setDatas(list);

        mTodayNewGoodsAdapter.notifyDataSetChanged();

        switch (loadType) {
            case TYPE_PULL_REFRESH:
                mSuperRecyclerView.setRefreshing(false);
                break;

            case TYPE_PULL_MORE:
                mSuperRecyclerView.setLoadingMore(false);
                break;
        }
        /**
         * 判断是否需要加载更多，与服务器的总条数比
         */
        if (havaNext) {
            mSuperRecyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            currpage++;
                            presenter.getFriendCirclesList(TYPE_PULL_MORE, currpage + "", type == 0 ? null : userid);

                        }
                    }, 2000);

                }
            }, 1);
        } else {
            mSuperRecyclerView.removeMoreListener();
            mSuperRecyclerView.hideMoreProgress();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscription != null) {
            RxBus.getInstance().unSub(mSubscription);
        }


    }

    public static final String AVATER = "hx_avater";
    public static final String NAME = "hx_name";
    public static final String UID = "hx_uid";


    public static final String GID = "hx_gid";
    public static final String ORDER_ID = "order_id";

    public static final String GNAME = "hx_gname";
    public static final String GNICK = "hx_gnick";

    public static final String GAVATER = "hx_gavater";
    public static final String FAVATER = "hx_favater";
    public static final String FUID = "hx_fuid";
    public static final String FNAME = "hx_fname";
    private List<QfriendBean> data = new ArrayList<>();

    @Override
    public <T> void toEntity(T entity, int type) {
        if (type == 66) {//评论

            if (TextUtil.isNotEmpty(userid) && TextUtil.isNotEmpty(sendId)) {
                if (!toId.equals(sendId)) {
                    //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
                  final   EMMessage message = EMMessage.createTxtSendMessage("!@#$$#@!", toId);
                    message.setAttribute("em_robot_message", true);
                    message.setAttribute(AVATER, SpUtil.getAvar(getActivity()));
                    message.setAttribute(NAME, "!@#$$#@!" + "," + qfd_id);
                    message.setAttribute(FUID, sendId);
                    EMClient.getInstance().chatManager().sendMessage(message);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            EMConversation conv = EMClient.getInstance().chatManager().getConversation(message.getTo());
                            if(conv==null){
                                return;
                            }
                            conv.removeMessage(message.getMsgId());
                        }
                    }).start();

                }
             final    EMMessage message = EMMessage.createTxtSendMessage("!@#$$#@!", sendId);
                message.setAttribute("em_robot_message", true);
                message.setAttribute(AVATER, SpUtil.getAvar(getActivity()));
                message.setAttribute(NAME, "!@#$$#@!" + "," + qfd_id);
                message.setAttribute(FUID, sendId);
                EMClient.getInstance().chatManager().sendMessage(message);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        EMConversation conv = EMClient.getInstance().chatManager().getConversation(message.getTo());
                        if(conv==null){
                            return;
                        }
                        conv.removeMessage(message.getMsgId());
                    }
                }).start();
            }
            List<QfriendBean> datas = (List<QfriendBean>) entity;
            int i = 0;
            data.clear();
            if (datas != null && datas.size() > 0) {
                for (QfriendBean bean : list) {
                    if (i == poistion) {
                        data.add(datas.get(0));
                    } else {
                        data.add(bean);
                    }
                    i++;
                }
                mTodayNewGoodsAdapter.setDatas(data);
                mTodayNewGoodsAdapter.notifyDataSetChanged();
            }
            closeKeyBoard(tvSend);
        } else if (type == 77) {
            List<QfriendBean> datas = (List<QfriendBean>) entity;
            int i = 0;
            data.clear();
            if (datas != null && datas.size() > 0) {
                for (QfriendBean bean : list) {
                    if (i == poistion) {
                        data.add(datas.get(0));
                    } else {
                        data.add(bean);
                    }
                    i++;
                }
                mTodayNewGoodsAdapter.setDatas(data);
                mTodayNewGoodsAdapter.notifyDataSetChanged();
            }
            closeKeyBoard(tvSend);
        } else if (type == 88) {
            List<QfriendBean> datas = (List<QfriendBean>) entity;
            int i = 0;
            data.clear();
            if (datas != null && datas.size() > 0) {
                for (QfriendBean bean : list) {
                    if (i == poistion) {
                        data.add(datas.get(0));
                    } else {
                        data.add(bean);
                    }
                    i++;
                }
                mTodayNewGoodsAdapter.setDatas(data);
                mTodayNewGoodsAdapter.notifyDataSetChanged();
            }
            closeKeyBoard(tvSend);
        } else if (type == 99) {
            reflash();
        } else if (type == 55) {
            List<QfriendBean> datas = (List<QfriendBean>) entity;
            onDataLoaded(TYPE_PULL_REFRESH, false, datas);
        } else {
            List<QfriendBean> datas = (List<QfriendBean>) entity;
            onDataLoaded(type, datas.size() == 12, datas);
        }
    }

    @Override
    public void toNextStep(int type) {

    }

    @Override
    public void showTomast(String msg) {
        reflash();
    }


    public static void closeKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

}
