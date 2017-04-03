package org.dragberry.ozo.screen.popup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import org.dragberry.ozo.common.user.NewUserRequest;
import org.dragberry.ozo.common.user.NewUserResponse;
import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.DirectedGame;
import org.dragberry.ozo.game.util.StringConstants;
import org.dragberry.ozo.http.HttpClient;
import org.dragberry.ozo.http.PostHttpTask;
import org.springframework.http.HttpStatus;

/**
 * Created by maksim on 02.02.17.
 */

public class NewUserPopup extends AbstractPopup {

    private final static String TAG = NewUserPopup.class.getName();

    private static final String USER_ID = "userID";
    private static final String USER_NAME = "userName";
    private static final String SETTINGS_EXTENSION = ".settings";

    private TextField userNameTxt;
    private Label errorLbl;

    public NewUserPopup init() {
        return this;
    }

    public NewUserPopup(DirectedGame game) {
        super(game);
    }

    @Override
    protected void rebuildStage() {

    }

    @Override
    protected void buildStage(float viewportWidth, float viewportHeight) {
        Label msgLbl = new Label(
                Assets.instance.translation.get("ozo.registration"),
                Assets.instance.skin.skin.get("header", Label.LabelStyle.class));
        msgLbl.setWrap(true);
        msgLbl.setAlignment(Align.center);
        popupWindow.add(msgLbl).padBottom(20f).fillX().expandX();
        popupWindow.row().expand().fill();

        userNameTxt = createTextField();
        popupWindow.add(userNameTxt).fill().expand().pad(10f);
        popupWindow.row();
        errorLbl = new Label(StringConstants.EMPTY,
                Assets.instance.skin.skin.get("error", Label.LabelStyle.class));
        errorLbl.setWrap(true);
        errorLbl.setAlignment(Align.center);
        errorLbl.setVisible(false);
        popupWindow.add(errorLbl).fill().expand().pad(10f);
        popupWindow.row();
        popupWindow.add(createOkBtn()).fill().expand().pad(10f);
        popupWindow.row();
        popupWindow.add(createContinueBtn()).fill().expand().pad(10f);
        popupWindow.row();
        popupWindow.add(createExitBtn()).fill().expand().pad(10f);

        popupWindow.setWidth(popupWindow.getPrefWidth());
        popupWindow.setHeight(popupWindow.getPrefHeight());
    }


    private TextField createTextField() {
        String pattern = Assets.instance.translation.get("ozo.inputYourName");
        TextField field = new TextField(pattern, Assets.instance.skin.skin);
        field.setAlignment(Align.center);
        field.selectAll();
        return field;
    }

    private TextButton createOkBtn() {
        TextButton btn = new TextButton(Assets.instance.translation.get("ozo.register"), Assets.instance.skin.skin);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                errorLbl.setVisible(true);
                String userName = userNameTxt.getText();
                if (userName.length() < 3 || userName.length() > 32) {
                    errorLbl.setText(Assets.instance.translation.get("ozo.err.userNameLength"));
                    return;
                }
                NewUserRequest req = new NewUserRequest();
                req.setUserName(userNameTxt.getText());
                game.platform.getHttpClient().executeTask(new PostHttpTask<NewUserRequest, NewUserResponse>(
                        req, NewUserResponse.class, HttpClient.URL.NEW_USER) {

                    @Override
                    public void onComplete(NewUserResponse result) {
                        Gdx.app.debug(TAG, "New user has been created with id=" + result);
                        final Preferences prefs = Gdx.app.getPreferences(getClass() + SETTINGS_EXTENSION);
                        prefs.putString(USER_ID, result.getUserId());
                        prefs.putString(USER_NAME, result.getUserName());
                        prefs.flush();
                        game.platform.getUser().setUserId(result.getUserId());
                        game.platform.getUser().setUserName(result.getUserName());
                        errorLbl.setText(StringConstants.EMPTY);
                        errorLbl.setVisible(false);
                        game.setPopup(null);
                    }

                    @Override
                    public void onFail(HttpStatus status) {
                        errorLbl.setVisible(true);
                        if (HttpStatus.CONFLICT == status) {
                            errorLbl.setText(Assets.instance.translation.get("ozo.err.userNameExists"));
                        }
                    }
                });
            }
        });
        return btn;
    }

    private TextButton createContinueBtn() {
        TextButton btn = new TextButton(Assets.instance.translation.get("ozo.continueWithoutRegistration"), Assets.instance.skin.skin);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setPopup(null);
            }
        });
        return btn;
    }

    private TextButton createExitBtn() {
        TextButton btn = new TextButton(Assets.instance.translation.get("ozo.exit"), Assets.instance.skin.skin);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.exit();
            }
        });
        return btn;
    }

}
