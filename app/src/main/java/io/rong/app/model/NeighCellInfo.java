package io.rong.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.sea_monster.common.ParcelUtils;

import java.io.Serializable;

/**
 * Created by Bob_ge on 16/3/28.
 */
public class NeighCellInfo implements Serializable, Parcelable {

    private String lac;
    private String cid;
    private String RxLev;


    public static final Creator<NeighCellInfo> CREATOR = new Creator<NeighCellInfo>() {
        @Override
        public NeighCellInfo createFromParcel(Parcel in) {
            return new NeighCellInfo(in);
        }

        @Override
        public NeighCellInfo[] newArray(int size) {
            return new NeighCellInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    protected NeighCellInfo(Parcel in) {
        lac = ParcelUtils.readFromParcel(in);
        cid = ParcelUtils.readFromParcel(in);
        RxLev = ParcelUtils.readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        ParcelUtils.writeToParcel(parcel, lac);
        ParcelUtils.writeToParcel(parcel, cid);
        ParcelUtils.writeToParcel(parcel, RxLev);
    }

    public String getLac() {
        return lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getRxLev() {
        return RxLev;
    }

    public void setRxLev(String rxLev) {
        RxLev = rxLev;
    }


}
