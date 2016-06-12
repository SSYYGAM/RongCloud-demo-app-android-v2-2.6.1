package io.rong.app.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.HashMap;

import io.rong.app.DemoContext;
import io.rong.app.R;
import io.rong.app.utils.Constants;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.utils.StringUtils;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.CommandMessage;

/**
 * Created by Bob on 15/11/13.
 */
public class UpdateGroupUserInfoActivity extends BaseActionBarActivity {

    private EditText mNewName;
    String mGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.de_ac_update_group_info);

        getSupportActionBar().setTitle(R.string.de_actionbar_update_group);
        mNewName = (EditText) findViewById(R.id.et_new_name);

        Intent intent = getIntent();
        mGroupId = intent.getStringExtra("DEMO_GROUP_ID");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.de_fix_username, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon:
                if (RongIM.getInstance() == null || DemoContext.getInstance() == null)
                    return true;

                HashMap<String, GroupUserInfo> groupM = new HashMap<String, GroupUserInfo>();

                GroupUserInfo groupUserInfo = DemoContext.getInstance().setGroupUserinfoByGroupId(mGroupId, mNewName.getText().toString());
                String key = StringUtils.getKey(mGroupId, groupUserInfo.getUserId());
                groupM.put(key, groupUserInfo);

                DemoContext.getInstance().setGroupUserInfoMap(groupM);
                RongUserInfoManager.getInstance().setGroupUserInfo(groupUserInfo);
                CommandMessage commandMessage = CommandMessage.obtain("updata", "jejddsfdsdewxfe");
                String id = DemoContext.getInstance().getSharedPreferences().getString(Constants.APP_USER_ID, Constants.DEFAULT);
                String name = mNewName.getText().toString();
                UserInfo userInfo = new UserInfo(id, name, Uri.parse("hhhh"));
                commandMessage.setUserInfo(userInfo);


                RongIM.getInstance().sendMessage(Conversation.ConversationType.GROUP, "36", commandMessage, null, null, new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer messageId, RongIMClient.ErrorCode e) {

                    }

                    @Override
                    public void onSuccess(Integer integer) {
                    }
                }, new RongIMClient.ResultCallback<Message>() {
                    @Override
                    public void onSuccess(Message message) {

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode e) {

                    }
                });

                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
