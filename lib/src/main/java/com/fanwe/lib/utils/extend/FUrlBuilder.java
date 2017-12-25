package com.fanwe.lib.utils.extend;

import android.text.TextUtils;

/**
 * url链接拼装工具
 */
public class FUrlBuilder
{
    private String mUrl;
    private StringBuilder mStringBuilder;

    public FUrlBuilder(String url)
    {
        this.mUrl = url;
        reset();
    }

    /**
     * 添加参数
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public FUrlBuilder add(String key, String value)
    {
        if (!TextUtils.isEmpty(key) && value != null)
        {
            beforeAdd();
            mStringBuilder.append(key).append("=").append(value);
        }
        return this;
    }

    /**
     * 返回拼接的url链接
     *
     * @return
     */
    public String build()
    {
        return mStringBuilder.toString();
    }

    /**
     * 重置builder，会清空已经拼接的参数
     */
    public void reset()
    {
        mStringBuilder = new StringBuilder(mUrl);
    }

    private void beforeAdd()
    {
        String currentUrl = mStringBuilder.toString();
        if (currentUrl.contains("?"))
        {
            if (currentUrl.endsWith("?"))
            {

            } else if (currentUrl.endsWith("&"))
            {

            } else
            {
                mStringBuilder.append("&");
            }
        } else
        {
            mStringBuilder.append("?");
        }
    }

}
