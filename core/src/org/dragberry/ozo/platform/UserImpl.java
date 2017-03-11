package org.dragberry.ozo.platform;

import org.dragberry.ozo.common.CommonConstants;

/**
 * Created by maksim on 11.03.17.
 */

public class UserImpl implements User {

    private String userId = CommonConstants.DEFAULT_USER_ID;

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
