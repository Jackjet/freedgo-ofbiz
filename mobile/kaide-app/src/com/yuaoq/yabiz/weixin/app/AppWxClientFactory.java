package com.yuaoq.yabiz.weixin.app;

import com.yuaoq.yabiz.weixin.app.base.AppSetting;
import com.yuaoq.yabiz.weixin.app.base.WxEndpoint;
import com.yuaoq.yabiz.weixin.common.AccessTokenHolder;
import com.yuaoq.yabiz.weixin.common.DefaultAccessTokenHolder;
import com.yuaoq.yabiz.weixin.common.WxClient;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @borball on 11/7/2016.
 */
public class AppWxClientFactory {

    private static AppWxClientFactory instance = null;
    private static ConcurrentHashMap<String, WxClient> wxClients = new ConcurrentHashMap<>();

    private AppWxClientFactory() {
    }

    public synchronized static AppWxClientFactory getInstance() {
        if (instance == null) {
            instance = new AppWxClientFactory();
        }
        return instance;
    }

    public WxClient defaultWxClient() {
        return with(AppSetting.defaultSettings());
    }

    public WxClient with(AppSetting appSetting) {
        if (!wxClients.containsKey(key(appSetting))) {
            String url = WxEndpoint.get("url.token.get");
            String clazz = appSetting.getTokenHolderClass();

            AccessTokenHolder accessTokenHolder = null;
            if(clazz == null || "".equals(clazz)) {
                accessTokenHolder = new DefaultAccessTokenHolder(url, appSetting.getAppId(), appSetting.getSecret());
            } else {
                try {
                    accessTokenHolder = (AccessTokenHolder)Class.forName(clazz).newInstance();
                    accessTokenHolder.setClientId(appSetting.getAppId());
                    accessTokenHolder.setClientSecret(appSetting.getSecret());
                    accessTokenHolder.setTokenUrl(url);
                } catch (Exception e) {
                    accessTokenHolder = new DefaultAccessTokenHolder(url, appSetting.getAppId(), appSetting.getSecret());
                }
            }

            WxClient wxClient = new WxClient(appSetting.getAppId(), appSetting.getSecret(), accessTokenHolder);
            wxClients.putIfAbsent(key(appSetting), wxClient);
        }

        return wxClients.get(key(appSetting));
    }

    private String key(AppSetting appSetting) {
        return appSetting.getAppId() + ":" + appSetting.getSecret();
    }
}
