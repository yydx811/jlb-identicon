package com.bestjlb.identicon.provider;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Created by yydx811 on 2018/1/5.
 */
public class DefaultSaltProvider implements SaltProvider {

    /**
     * saltType. 0, no slat;1, fixed slat;2, random salt.
     */
    private int saltType;

    private String salt;

    public DefaultSaltProvider() {
    }

    public DefaultSaltProvider(int saltType, String salt) {
        switch (saltType) {
            case 1:
                if (salt == null) {
                    throw new IllegalArgumentException("fixed salt can not be null!");
                }
                this.salt = salt;
            case 0:
            case 2:
                this.saltType = saltType;
                break;
            default:
                this.saltType = 0;
        }
    }

    @Override
    public String getSalt() {
        switch (this.saltType) {
            case 1:
                return this.salt;
            case 2:
                return UUID.randomUUID().toString().replace("-", "");
            case 0:
            default:
                return "";
        }
    }

    /**
     * 获取带盐的数据
     *
     * @param data
     *          数据byte数组，如果为null，等于获取盐的byte数组。
     * @return
     *          如果data为null，返回结果也为null表示不需要加盐。
     */
    @Override
    public byte[] getByteWithSalt(byte[] data) {
        String salt = getSalt();
        if (StringUtils.isNotEmpty(salt)) {
            data = ArrayUtils.addAll(data, salt.getBytes(StandardCharsets.UTF_8));
        }
        return data;
    }

    @Override
    public boolean needSalt() {
        return this.saltType != 0;
    }
}
