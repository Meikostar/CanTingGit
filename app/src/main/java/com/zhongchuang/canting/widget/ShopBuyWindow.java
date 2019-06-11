package com.zhongchuang.canting.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aliyun.vod.common.utils.ToastUtil;
import com.bumptech.glide.Glide;
import com.zhongchuang.canting.R;
import com.zhongchuang.canting.adapter.GeguarAdapter;
import com.zhongchuang.canting.been.ProductBuy;
import com.zhongchuang.canting.been.Type;
import com.zhongchuang.canting.presenter.BaseContract;
import com.zhongchuang.canting.utils.DensityUtil;
import com.zhongchuang.canting.utils.StringUtil;
import com.zhongchuang.canting.utils.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/***
 * 功能描述:底部弹出的popupwindow
 * 作者:chenwei
 * 时间:2016/12/26
 * 版本:1.0
 ***/

public class ShopBuyWindow extends PopupWindow {

    private View mView;
    private Context mContext;
    private TextView tv_exit;
    private TextView tv_cancel;
    private GeguarAdapter adapter;
    private ImageView image;
    private StickyScrollView scroll;
    private TextView price;
    private TextView amount;
    private TextView choose;
    private LinearLayout topContainer;
    private ImageView close;
    private RegularListView rlMenu;
    private AddEditText add;
    private TextView sureButton;

    public ShopBuyWindow(final Activity context) {
        super(context);
        mContext = context;
        mView = View.inflate(context, R.layout.shop_tab_dialog, null);
        image = mView.findViewById(R.id.image);
        scroll = mView.findViewById(R.id.scroll);
        price = mView.findViewById(R.id.price);
        amount = mView.findViewById(R.id.amount);
        choose = mView.findViewById(R.id.choose);
        topContainer = mView.findViewById(R.id.top_container);
        close = mView.findViewById(R.id.close);
        rlMenu = mView.findViewById(R.id.rl_menu);
        add = mView.findViewById(R.id.add);
        sureButton = mView.findViewById(R.id.sure_button);


        price.setFocusable(true);
        price.setFocusableInTouchMode(true);
        price.requestFocus();
        scroll.setFocusable(false);
        adapter = new GeguarAdapter(context);
        rlMenu.setAdapter(adapter);
       add.setListener(new AddEditText.ChangeListener() {
           @Override
           public void change(String name) {
            if(Integer.valueOf(productBuy.pro_stock)<Integer.valueOf(name)){
                ToastUtil.showToast(context,"添加量不能大于库存");
                add.setTexts(productBuy.pro_stock);
            }
           }
       });
        sureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.clear();
                List<List<Type>> data = adapter.getData();
                for (List<Type> type : data) {

                    for (Type tp : type) {
                        if (tp.select == 2) {
                            select.add(tp);
                        }
                    }

                }
                if (select.size() == cout) {
//                    sel = select;
                    isHavae(select);
                    listener.clickListener(sku_id,add.getCout(),productBuy);
                    dismiss();
                }else {
                    ToastUtil.showToast(context,"请选择商品属性");
                }


            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clickListener("",add.getCout(),null);
                dismiss();
            }
        });
        adapter.setListener(new GeguarAdapter.ItemClickListener() {
            @Override
            public void itemclick(int poistion, String id) {
                select.clear();
                List<List<Type>> data = adapter.getData();
                for (List<Type> type : data) {

                    for (Type tp : type) {
                        if (tp.select == 2) {
                            select.add(tp);
                        }
                    }

                }

                int cout = 1;
                for (List<Type> type : data) {
                    boolean isChoos = false;
                    List<Type> sel = new ArrayList<>();
                    for (Type tp : select) {
                        if (tp.poition == cout) {
                            isChoos = true;
                        }
                    }
                    if (!isChoos) {
                        for (Type te : type) {
                            sel.clear();
                            sel.addAll(select);
                            sel.add(te);
                            if (isHavae(sel)) {
                                te.select = 1;
                            } else {
                                te.select = 0;
                            }

                        }
                    }
                    cout++;

                }

                adapter.setData(data);
                setData();
//                if (select.size() != 0) {
//                    list = getSelect();
//                    initData();
//                    for (List<Type> type : data) {
//                        for (Type tp : type) {
//                            if (tp.select == 2) {
//                                select.add(tp.cont);
//                            }
//                        }
//                    }
//
//                } else {
//                    return;
//                }

            }
        });
    }

    public void setData() {
        isHavae(select);
        if (productBuy != null) {
            if(TextUtil.isNotEmpty(productBuy.picture_sku_url)){
                String[] split = productBuy.picture_sku_url.split(",");

                Glide.with(mContext).load(StringUtil.changeUrl(split.length>0?split[0]:"")).asBitmap().placeholder(R.drawable.moren1).into(image);
            }

            if (productBuy.pro_site == 1) {
                price.setText("￥" + productBuy.pro_price+(Integer.valueOf(productBuy.integral_price)>0?"+"+productBuy.integral_price + mContext.getString(R.string.jf):""));
            } else if(productBuy.pro_site == 3) {
                price.setText("￥" + productBuy.pro_price+(Integer.valueOf(productBuy.integral_price)>0?"+"+productBuy.integral_price + mContext.getString(R.string.jf):""));
            } else {
                price.setText(productBuy.integral_price + mContext.getString(R.string.jf));
            }
            if (TextUtil.isNotEmpty(productBuy.pro_stock)) {
                amount.setText(mContext.getString(R.string.kc) + productBuy.pro_stock + mContext.getString(R.string.jian));
            }
            String cont = "";
            for (Type type : select) {
                if (TextUtil.isEmpty(cont)) {
                    cont = type.cont;
                } else {
                    cont = cont + "," + type.cont;
                }
            }
            choose.setText(mContext.getString(R.string.yx) + cont);
        }
    }

    private List<ProductBuy> datass = new ArrayList<>();
    private String sku_id;
    private ProductBuy productBuy;

    public boolean isHavae(List<Type> sel) {
        datass.clear();
        boolean ishave = false;
        for (ProductBuy bug : list) {
            int i = 0;
            for (Type cot : sel) {
                if ((TextUtil.isNotEmpty(bug.firstSpeciValue) && bug.firstSpeciValue.equals(cot.cont))
                        || (TextUtil.isNotEmpty(bug.secondSpeciValue) && bug.secondSpeciValue.equals(cot.cont))
                        || (TextUtil.isNotEmpty(bug.threeSpeciValue) && bug.threeSpeciValue.equals(cot.cont))
                        || (TextUtil.isNotEmpty(bug.fourSpeciValue) && bug.fourSpeciValue.equals(cot.cont))
                        || (TextUtil.isNotEmpty(bug.fiveSpeciValue) && bug.fiveSpeciValue.equals(cot.cont))
                        ) {
                    i++;
                }
            }
            if (sel.size() == i) {
                sku_id = bug.product_sku_id;
                productBuy = bug;
                ishave = true;

            }

        }
        return ishave;
    }

    private List<Type> select = new ArrayList<>();
//    private List<Type> sel = new ArrayList<>();

    public void setColor(int top, int bot) {
        if (top != 0) {
            tv_exit.setTextColor(mContext.getResources().getColor(top));
        }
        if (bot != 0) {
            tv_cancel.setTextColor(mContext.getResources().getColor(bot));
        }

    }


    public void setSureListener(ClickListener listener) {
        this.listener = listener;
    }

    private ClickListener listener;

    public interface ClickListener {
        void clickListener(String type, String cout, ProductBuy product);
    }

    @Override
    public void showAsDropDown(View anchor) {
        init();
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        init();
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        init();
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        init();
        super.showAtLocation(parent, gravity, x, y);
    }

    private void init() {



        // TODO Auto-generated method stub
        //设置SelectPicPopupWindow的View
        this.setContentView(mView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(DensityUtil.dip2px(520));
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置PopupWindow可触摸
        this.setTouchable(true);
        //设置非PopupWindow区域是否可触摸
//        this.setOutsideTouchable(false);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popwin_anim);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        backgroundAlpha((Activity) mContext, 0.5f);//0.0-1.0
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                backgroundAlpha((Activity) mContext, 1f);
            }
        });

    }

    private List<List<Type>> data = new ArrayList<>();
    private List<List<Type>> datas = new ArrayList<>();


    private List<Type> dty;
    private List<ProductBuy> list;
    private int cout;


    public void initData() {
        data.clear();
        for (int i = 0; i < cout; i++) {
            Map<String, String> map = new HashMap<>();
            List<Type> dat = new ArrayList<>();
            for (ProductBuy bug : list) {
                if (i == 0) {
                    map.put(bug.firstSpeciValue, bug.firstSpeci);
                } else if (i == 1) {
                    map.put(bug.secondSpeciValue, bug.secondSpeci);
                } else if (i == 2) {
                    map.put(bug.threeSpeciValue, bug.threeSpeci);
                } else if (i == 3) {
                    map.put(bug.fourSpeciValue, bug.fourSpeci);
                } else if (i == 4) {
                    map.put(bug.fiveSpeciValue, bug.fiveSpeci);
                }
            }
            for (String cont : map.keySet()) {
                Type type = new Type();
                type.select = 1;
                type.poition = i + 1;
                type.title = map.get(cont);
                type.cont = cont;
                dat.add(type);
            }
            data.add(dat);
            if (datas.size() != cout) {
                datas.add(dat);
            }
        }
        adapter.setData(data);

        if(TextUtil.isNotEmpty(productBuy.picture_sku_url)){
            String[] split = productBuy.picture_sku_url.split(",");

            Glide.with(mContext).load(StringUtil.changeUrl(split.length>0?split[0]:"")).asBitmap().placeholder(R.drawable.moren1).into(image);
        }
        if (productBuy.pro_site == 1) {
            price.setText("￥" + productBuy.pro_price+(Integer.valueOf(productBuy.integral_price)>0?"+"+productBuy.integral_price + mContext.getString(R.string.jf):""));
        }else  if (productBuy.pro_site == 3) {
            price.setText("￥" + productBuy.pro_price+(Integer.valueOf(productBuy.integral_price)>0?"+"+productBuy.integral_price + mContext.getString(R.string.jf):""));
        } else {
            price.setText(productBuy.integral_price + mContext.getString(R.string.jf));
        }
        if (TextUtil.isNotEmpty(productBuy.pro_stock)) {
            amount.setText(mContext.getString(R.string.jf) + productBuy.pro_stock + mContext.getString(R.string.jf));
        }
        String cont = "";
        for (Type type : select) {
            if (TextUtil.isEmpty(cont)) {
                cont = type.cont;
            } else {
                cont = cont + "," + type.cont;
            }
        }
        choose.setText(mContext.getString(R.string.yx) + cont);

    }

    public void selectData(List<ProductBuy> list, int cout) {
        this.list = list;
        this.cout = cout;
        productBuy=list.get(0);
        initData();

    }

    public void notifichange() {

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public void showPopView(View imgs) {
        showAtLocation(imgs, Gravity.BOTTOM, 0, 0);
    }


}
