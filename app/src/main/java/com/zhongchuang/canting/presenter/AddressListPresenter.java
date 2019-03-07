package com.zhongchuang.canting.presenter;

/**
 * Created by Administrator on 2017/12/9.
 */

public interface AddressListPresenter {

    void getAddressList();

    void deleteAddress(String id ,int pos);

    void defaultAddress(String id ,int pos);


}
