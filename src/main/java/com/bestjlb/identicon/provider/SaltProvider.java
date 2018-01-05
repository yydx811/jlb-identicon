package com.bestjlb.identicon.provider;

/**
 * Created by yydx811 on 2018/1/5.
 */
public interface SaltProvider {

    String getSalt();

    byte[] getByteWithSalt(byte[] data);

    boolean needSalt();
}
