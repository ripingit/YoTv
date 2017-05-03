package top.lhzbxx.base;

import top.lhzbxx.util.Cache;

import java.util.UUID;

/**
 * Author: LuHao
 * Date: 16/5/30
 * Mail: lhzbxx@gmail.com
 */

public class AuthToken {

    private String accessToken;
    private String refreshToken;
    private String userId;

    public AuthToken(String userId) {
        this.userId = userId;
        this.initAuthToken();
    }

    private void initAuthToken() {
        this.accessToken = UUID.randomUUID().toString();
        this.refreshToken = UUID.randomUUID().toString();
        this.setAccessToken();
        this.setRefreshToken();
    }

    private void setAccessToken() {
        Cache.setAccessToken(this.accessToken, userId);
    }

    private void setRefreshToken() {
        Cache.setRefreshToken(this.refreshToken, userId);
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void refreshAuthToken(String refreshToken) {
        String userId = Cache.checkRefreshToken(refreshToken);
        if (userId != null) {
            this.userId = userId;
            this.initAuthToken();
        }
    }

}
