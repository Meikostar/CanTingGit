package com.zhongchuang.canting.easeui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.hyphenate.util.EMLog;
import com.zhongchuang.canting.R;;
import com.zhongchuang.canting.activity.ChatActivity;
import com.zhongchuang.canting.activity.chat.ChatMessageActivity;
import com.zhongchuang.canting.easeui.EaseUI;
import com.zhongchuang.canting.easeui.domain.EaseAvatarOptions;
import com.zhongchuang.canting.easeui.domain.EaseUser;
import com.zhongchuang.canting.easeui.utils.EaseUserUtils;
import com.zhongchuang.canting.easeui.widget.EaseImageView;
import com.zhongchuang.canting.widget.MCheckBox;

import java.util.ArrayList;
import java.util.List;

public class EaseContactAdapter extends ArrayAdapter<EaseUser> implements SectionIndexer{
    private static final String TAG = "ContactAdapter";
    List<String> list;
    List<EaseUser> userList;
    List<EaseUser> copyUserList;
    private LayoutInflater layoutInflater;
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;
    private int res;
    private MyFilter myFilter;
    private boolean notiyfyByFilter;
    private int status;//1表示添加或删除时候的item
   private Context context;
    public EaseContactAdapter(Context context, int resource, List<EaseUser> objects, int status) {
        super(context, resource, objects);
        this.status = status;
        this.context = context;
        this.res = resource;
        this.userList = objects;
        copyUserList = new ArrayList<EaseUser>();
        copyUserList.addAll(objects);
        layoutInflater = LayoutInflater.from(context);
    }
    
    private static class ViewHolder {
        EaseImageView avatar;
        TextView headerView;
        MCheckBox iv_choose;

        TextView address;
        LinearLayout ll_bg;
        TextView judge;
        ImageView member_select;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            if (res == 0)
                convertView = layoutInflater.inflate(R.layout.ease_row_contact, parent, false);
            else
                convertView = layoutInflater.inflate(res, null);
            holder.avatar = (EaseImageView) convertView.findViewById(R.id.avatar);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.ll_bg = (LinearLayout) convertView.findViewById(R.id.ll_bg);
            holder.iv_choose = (MCheckBox) convertView.findViewById(R.id.iv_choose);
            holder.judge = (TextView) convertView.findViewById(R.id.judge);
            holder.headerView = (TextView) convertView.findViewById(R.id.header);
            holder.member_select = (ImageView) convertView.findViewById(R.id.member_select);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final EaseUser user = getItem(position);
        if(status==1){
            holder.iv_choose.setVisibility(View.VISIBLE);
            if(user.state==1){
                holder.ll_bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentc = new Intent(context, ChatActivity.class);
                        intentc.putExtra("userId", user.userid);
                        context.startActivity(intentc);
                    }
                });
            }else {
                holder.ll_bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(user.isChoose){
                            holder.iv_choose.setChecked(false);
                            user.isChoose=false;
                        }else {
                            holder.iv_choose.setChecked(true);
                            user.isChoose=true;
                        }
                    }
                });
            }

        }else {
            holder.iv_choose.setVisibility(View.GONE);

        }



        if(user.isChoose){
            holder.iv_choose.setChecked(true);
        }else {
            holder.iv_choose.setChecked(false);
        }
        if(user.state==1){
            holder.iv_choose.setVisibility(View.GONE);
        }else {
            holder.iv_choose.setVisibility(View.VISIBLE);
        }
        if(user == null)
            Log.d("ContactAdapter", position + "");
        String username = user.getUsername();
        String header = user.getInitialLetter();
        
        if (position == 0 || header != null && !header.equals(getItem(position - 1).getInitialLetter())) {
            if (TextUtils.isEmpty(header)) {
                holder.headerView.setVisibility(View.GONE);
            } else {
                holder.headerView.setVisibility(View.VISIBLE);
                holder.headerView.setText(header);
            }
        } else {
            holder.headerView.setVisibility(View.GONE);
        }

        EaseAvatarOptions avatarOptions = EaseUI.getInstance().getAvatarOptions();
        if(avatarOptions != null && holder.avatar instanceof EaseImageView) {
            EaseImageView avatarView = ((EaseImageView) holder.avatar);
            if (avatarOptions.getAvatarShape() != 0)
                avatarView.setShapeType(avatarOptions.getAvatarShape());
            if (avatarOptions.getAvatarBorderWidth() != 0)
                avatarView.setBorderWidth(avatarOptions.getAvatarBorderWidth());
            if (avatarOptions.getAvatarBorderColor() != 0)
                avatarView.setBorderColor(avatarOptions.getAvatarBorderColor());
            if (avatarOptions.getAvatarRadius() != 0)
                avatarView.setRadius(avatarOptions.getAvatarRadius());
        }


//        if(user != null && user.getNick() != null){
//            holder.nameView.setText(user.getNick());
//        }else{
//            holder.nameView.setText(username);
//        }
//
//        if(user != null && user.getAvatar() != null){
//            try {
//                int avatarResId = Integer.parseInt(user.getAvatar());
//                Glide.with(getContext()).load(avatarResId).into(holder.avatar);
//            } catch (Exception e) {
//                //use default avatar
//                Glide.with(getContext()).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(holder.avatar);
//            }
//        }else{
//            Glide.with(getContext()).load(R.drawable.ease_default_avatar).into(holder.avatar);
//        }

        EaseUserUtils.setUserNick(username, holder.address);
        EaseUserUtils.setUserAvatar(getContext(), username, holder.avatar);
        
       
        if(primaryColor != 0)
            holder.address.setTextColor(primaryColor);
        if(primarySize != 0)
            holder.address.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize);
        if(initialLetterBg != null)
            holder.headerView.setBackgroundDrawable(initialLetterBg);
        if(initialLetterColor != 0)
            holder.headerView.setTextColor(initialLetterColor);
        
        return convertView;
    }
    
    @Override
    public EaseUser getItem(int position) {
        return super.getItem(position);
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public int getPositionForSection(int section) {
        return positionOfSection.get(section);
    }

    @Override
    public int getSectionForPosition(int position) {
        return sectionOfPosition.get(position);
    }
    
    @Override
    public Object[] getSections() {
        positionOfSection = new SparseIntArray();
        sectionOfPosition = new SparseIntArray();
        int count = getCount();
        list = new ArrayList<String>();
        list.add(getContext().getString(R.string.search_header));
        positionOfSection.put(0, 0);
        sectionOfPosition.put(0, 0);
        for (int i = 1; i < count; i++) {

            String letter = getItem(i).getInitialLetter();
            int section = list.size() - 1;
            if (list.get(section) != null && !list.get(section).equals(letter)) {
                list.add(letter);
                section++;
                positionOfSection.put(section, i);
            }
            sectionOfPosition.put(i, section);
        }
        return list.toArray(new String[list.size()]);
    }
    
    @Override
    public Filter getFilter() {
        if(myFilter==null){
            myFilter = new MyFilter(userList);
        }
        return myFilter;
    }
    
    protected class  MyFilter extends Filter{
        List<EaseUser> mOriginalList = null;
        
        public MyFilter(List<EaseUser> myList) {
            this.mOriginalList = myList;
        }

        @Override
        protected synchronized FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if(mOriginalList==null){
                mOriginalList = new ArrayList<EaseUser>();
            }
            EMLog.d(TAG, "contacts original size: " + mOriginalList.size());
            EMLog.d(TAG, "contacts copy size: " + copyUserList.size());
            
            if(prefix==null || prefix.length()==0){
                results.values = copyUserList;
                results.count = copyUserList.size();
            }else{

                if (copyUserList.size() > mOriginalList.size()) {
                    mOriginalList = copyUserList;
                }
                String prefixString = prefix.toString();
                final int count = mOriginalList.size();
                final ArrayList<EaseUser> newValues = new ArrayList<EaseUser>();
                for(int i=0;i<count;i++){
                    final EaseUser user = mOriginalList.get(i);
                    String username = user.getUsername();
                    
                    if(username.startsWith(prefixString)){
                        newValues.add(user);
                    }
                    else{
                         final String[] words = username.split(" ");
                         final int wordCount = words.length;
    
                         // Start at index 0, in case valueText starts with space(s)
                        for (String word : words) {
                            if (word.startsWith(prefixString)) {
                                newValues.add(user);
                                break;
                            }
                        }
                    }
                }
                results.values=newValues;
                results.count=newValues.size();
            }
            EMLog.d(TAG, "contacts filter results size: " + results.count);
            return results;
        }

        @Override
        protected synchronized void publishResults(CharSequence constraint,
                FilterResults results) {
            userList.clear();
            userList.addAll((List<EaseUser>)results.values);
            EMLog.d(TAG, "publish contacts filter results size: " + results.count);
            if (results.count > 0) {
                notiyfyByFilter = true;
                notifyDataSetChanged();
                notiyfyByFilter = false;
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
    
    
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if(!notiyfyByFilter){
            copyUserList.clear();
            copyUserList.addAll(userList);
        }
    }
    public List<EaseUser> getUserList(){
        return userList;
    }
    
    protected int primaryColor;
    protected int primarySize;
    protected Drawable initialLetterBg;
    protected int initialLetterColor;

    public EaseContactAdapter setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
        return this;
    }


    public EaseContactAdapter setPrimarySize(int primarySize) {
        this.primarySize = primarySize;
        return this;
    }

    public EaseContactAdapter setInitialLetterBg(Drawable initialLetterBg) {
        this.initialLetterBg = initialLetterBg;
        return this;
    }

    public EaseContactAdapter setInitialLetterColor(int initialLetterColor) {
        this.initialLetterColor = initialLetterColor;
        return this;
    }
    
}
