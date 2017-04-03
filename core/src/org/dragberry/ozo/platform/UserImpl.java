package org.dragberry.ozo.platform;

import org.dragberry.ozo.common.CommonConstants;

/**
 * Created by maksim on 11.03.17.
 */

public class UserImpl implements User {

    private String userId = CommonConstants.DEFAULT_USER_ID;
    private String userName;

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean isDefault() {
        return CommonConstants.DEFAULT_USER_ID.equals(userId);
    }
}
