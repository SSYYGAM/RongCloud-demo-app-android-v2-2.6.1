package io.rong.app.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.rong.app.R;
import io.rong.app.model.Friend;
import io.rong.imkit.widget.AsyncImageView;
import com.sea_monster.resource.Resource;

public class ContactsMultiChoiceAdapter extends ContactsAdapter {

    private static final String TAG = ContactsMultiChoiceAdapter.class.getSimpleName();
    private ArrayList<Friend> mFriends;


    public ContactsMultiChoiceAdapter(Context context, List<Friend> friends) {
        super(context, friends);
        this.mFriends = (ArrayList<Friend>) friends;
    }

    @Override
    protected void bindView(View v, int partition, List<Friend> data, int position) {
        super.bindView(v, partition, data, position);

        ViewHolder holder = (ViewHolder) v.getTag();
        TextView name = holder.name;
        AsyncImageView photo = holder.photo;

        Friend friend = data.get(position);
        name.setText(friend.getNickname());

        Resource res = new Resource(friend.getPortrait());
        int defaultId;
        if(friend.getUserId().equals("★001")){
            defaultId = R.drawable.de_address_new_friend;
        }else if(friend.getUserId().equals("★002")){
            defaultId = R.drawable.de_address_group;
        }else if(friend.getUserId().equals("★003")){
            defaultId = R.drawable.de_address_public;
        }else{
            defaultId = R.drawable.de_default_portrait;
        }
        photo.setResource(friend.getPortrait(), defaultId);

        String userId = friend.getUserId();
        holder.userId = userId;

    }

    @Override
    protected void newSetTag(View view, ViewHolder holder, int position, List<Friend> data) {
        super.newSetTag(view, holder, position, data);
    }

    @Override
    public void onItemClick(String friendId) {


    }


}
