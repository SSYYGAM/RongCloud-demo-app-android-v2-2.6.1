package io.rong.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.app.database.DBManager;
import io.rong.app.database.UserInfos;
import io.rong.app.database.UserInfosDao;
import io.rong.app.utils.Constants;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Bob on 2015/1/30.
 */
public class DemoContext {

    private static DemoContext mDemoContext;
    public Context mContext;
    private DemoApi mDemoApi;
    private HashMap<String, Group> groupMap;
    private HashMap<String, GroupUserInfo> groupUserInfoMap;
    private SharedPreferences mPreferences;
    private RongIM.LocationProvider.LocationCallback mLastLocationCallback;
    private UserInfosDao mUserInfoDao;


    public static void init(Context context) {
        mDemoContext = new DemoContext(context);
    }

    public static DemoContext getInstance() {

        if (mDemoContext == null) {
            mDemoContext = new DemoContext();
        }
        return mDemoContext;
    }

    private DemoContext() {

    }

    private DemoContext(Context context) {
        mContext = context;
        mDemoContext = this;

        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        //http初始化 用于登录、注册使用
        mDemoApi = new DemoApi(context);

        mUserInfoDao = DBManager.getInstance(mContext).getDaoSession().getUserInfosDao();
    }


    /**
     * 查询所有数据
     */
    public List<UserInfos> loadAllUserInfos() {

        return mUserInfoDao.loadAll();
    }


    /**
     * 删除 userinfos 表
     */
    public void deleteUserInfos() {

        mUserInfoDao.deleteAll();
    }

    /**
     * 更新 好友信息
     *
     * @param targetid
     * @param status
     */
    public void updateUserInfos(String targetid, String status) {

        UserInfos userInfos = mUserInfoDao.queryBuilder().where(UserInfosDao.Properties.Userid.eq(targetid)).unique();
        userInfos.setStatus(status);
        userInfos.setUsername(userInfos.getUsername());
        userInfos.setPortrait(userInfos.getPortrait());
        userInfos.setUserid(userInfos.getUserid());

        mUserInfoDao.update(userInfos);

    }

    /**
     * 向数据库插入数据
     *
     * @param info 用户信息
     */
    public void insertOrReplaceUserInfos(UserInfos info) {


        mUserInfoDao.insertOrReplace(info);
    }

    /**
     * 向数据库插入数据
     *
     * @param info   用户信息
     * @param status 状态
     */
    public void insertOrReplaceUserInfo(UserInfo info, String status) {

        UserInfos userInfos = new UserInfos();
        userInfos.setStatus(status);
        userInfos.setUsername(info.getName());
        userInfos.setPortrait(String.valueOf(info.getPortraitUri()));
        userInfos.setUserid(info.getUserId());
        mUserInfoDao.insertOrReplace(userInfos);
    }

    /**
     * 通过userid 查找 UserInfos,判断是否为好友，查找的是本地的数据库
     *
     * @param userId
     * @return
     */
    public boolean searcheUserInfosById(String userId) {
        if (userId != null) {

            UserInfos userInfos = mUserInfoDao.queryBuilder().where(UserInfosDao.Properties.Userid.eq(userId)).unique();

            if (userInfos == null)
                return false;

            if (userInfos.getStatus().equals("1") || userInfos.getStatus().equals("3") || userInfos.getStatus().equals("5")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过userid 查找 UserInfos，查找的是本地的数据库
     *
     * @param userId
     * @return
     */
    public UserInfos getUserInfosById(String userId) {

        if (userId == null)
            return null;

        UserInfos userInfos = mUserInfoDao.queryBuilder().where(UserInfosDao.Properties.Userid.eq(userId)).unique();

        if (userInfos == null)
            return null;

        return userInfos;
    }


    /**
     * 通过userid 查找 UserInfo，查找的是本地的数据库
     *
     * @param userId
     * @return
     */
    public UserInfo getUserInfoById(String userId) {

        if (userId == null)
            return null;
        UserInfos userInfos = mUserInfoDao.queryBuilder().where(UserInfosDao.Properties.Userid.eq(userId)).unique();
        if (userInfos == null && DemoContext.getInstance() != null) {
            return null;
        }


        return new UserInfo(userInfos.getUserid(), userInfos.getUsername(), Uri.parse(userInfos.getPortrait()));
    }

    public boolean hasUserId(String userId) {

        if (userId != null) {

            UserInfos userInfos = mUserInfoDao.queryBuilder().where(UserInfosDao.Properties.Userid.eq(userId)).unique();

            if (userInfos == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获得好友列表 ID
     *
     * @return
     */
    public List getFriendListId() {
        List userInfoList = new ArrayList();

        List<UserInfos> userInfos = mUserInfoDao.queryBuilder().where(UserInfosDao.Properties.Status.eq("1")).list();

        if (userInfos == null)
            return null;

        for (int i = 0; i < userInfos.size(); i++) {

            userInfoList.add(userInfos.get(i).getUserid());
        }

        return userInfoList;
    }


    /**
     * 获得好友列表
     *
     * @return
     */
    public ArrayList<UserInfo> getFriendList() {
        List<UserInfo> userInfoList = new ArrayList<UserInfo>();

        List<UserInfos> userInfos = mUserInfoDao.queryBuilder().where(UserInfosDao.Properties.Status.eq("1")).list();

        if (userInfos == null)
            return null;

        for (int i = 0; i < userInfos.size(); i++) {
            UserInfo userInfo = new UserInfo(userInfos.get(i).getUserid(), userInfos.get(i).getUsername(), Uri.parse(userInfos.get(i).getPortrait()));

            userInfoList.add(userInfo);
        }
        return (ArrayList) userInfoList;
    }

    /**
     * 根据userids获得好友列表
     *
     * @return
     */
    public ArrayList<UserInfo> getUserInfoList(String[] userIds) {

        List<UserInfo> userInfoList = new ArrayList<UserInfo>();
        List<UserInfos> userInfosList = new ArrayList<UserInfos>();
        UserInfo userInfo;
        UserInfos userInfos;

        for (int i = 0; i < userIds.length; i++) {
            userInfos = mUserInfoDao.queryBuilder().where(UserInfosDao.Properties.Userid.eq(userIds[i])).unique();
            userInfosList.add(userInfos);
            if (mUserInfoDao.getKey(userInfosList.get(i)) != null) {
                userInfo = new UserInfo(userInfosList.get(i).getUserid(), userInfosList.get(i).getUsername(), Uri.parse(userInfosList.get(i).getPortrait()));
                userInfoList.add(userInfo);
            }
        }
        if (userInfosList == null)
            return null;


        return (ArrayList) userInfoList;
    }

    /**
     * 根据userids获得好友列表
     *
     * @return
     */
    public ArrayList<UserInfo> getUserInfoList(List list) {

        List<UserInfo> userInfoList = new ArrayList<UserInfo>();
        List<UserInfos> userInfosList = new ArrayList<UserInfos>();
        UserInfo userInfo;
        UserInfos userInfos;

        for (int i = 0; i < list.size(); i++) {
            userInfos = mUserInfoDao.queryBuilder().where(UserInfosDao.Properties.Userid.eq(list.get(i))).unique();
            userInfosList.add(userInfos);
            if (mUserInfoDao.getKey(userInfosList.get(i)) != null) {
                userInfo = new UserInfo(userInfosList.get(i).getUserid(), userInfosList.get(i).getUsername(), Uri.parse(userInfosList.get(i).getPortrait()));
                userInfoList.add(userInfo);
            }
        }

        if (userInfosList == null)
            return null;


        return (ArrayList) userInfoList;
    }

    /**
     * 通过groupid 获得groupname
     *
     * @param groupid
     * @return
     */
    public String getGroupNameById(String groupid) {
        Group groupReturn = null;
        if (!TextUtils.isEmpty(groupid) && groupMap != null) {

            if (groupMap.containsKey(groupid)) {
                groupReturn = groupMap.get(groupid);
            } else
                return null;

        }
        if (groupReturn != null)
            return groupReturn.getName();
        else
            return null;
    }

    /**
     * 通过群组 Id 修改自己所在的这个群组的群昵称。
     *
     * @param groupId
     * @return
     */
    public GroupUserInfo setGroupUserinfoByGroupId(String groupId, String nickName) {

        String userId = DemoContext.getInstance().getSharedPreferences().getString(Constants.APP_USER_ID, Constants.DEFAULT);

        GroupUserInfo groupUserInfo = new GroupUserInfo(groupId, userId, nickName);

        return groupUserInfo;
    }

    /**
     * 通过群组 Id 修改自己所在的这个群组的群昵称。
     *
     * @param groupId
     * @return
     */
    public GroupUserInfo setGroupUserinfoByGroupId(String groupId,String userid, String nickName) {


        GroupUserInfo groupUserInfo = new GroupUserInfo(groupId, userid, nickName);

        return groupUserInfo;
    }

    public SharedPreferences getSharedPreferences() {
        return mPreferences;
    }


    public HashMap<String, GroupUserInfo> getGroupUserInfoMap() {
        return groupUserInfoMap;
    }

    public void setGroupUserInfoMap(HashMap<String, GroupUserInfo> groupUserInfoMap) {
        this.groupUserInfoMap = groupUserInfoMap;
    }

    public void setGroupMap(HashMap<String, Group> groupMap) {
        this.groupMap = groupMap;
    }

    public HashMap<String, Group> getGroupMap() {
        return groupMap;
    }


    public DemoApi getDemoApi() {
        return mDemoApi;
    }


    public RongIM.LocationProvider.LocationCallback getLastLocationCallback() {
        return mLastLocationCallback;
    }

    public void setLastLocationCallback(RongIM.LocationProvider.LocationCallback lastLocationCallback) {
        this.mLastLocationCallback = lastLocationCallback;
    }


}
