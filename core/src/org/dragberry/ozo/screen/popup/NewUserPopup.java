package org.dragberry.ozo.screen.popup;

import com.badlogic.gdx.Application;
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
import org.dragberry.ozo.game.util.Constants;
import org.dragberry.ozo.game.util.StringConstants;
import org.dragberry.ozo.http.HttpClient;
import org.dragberry.ozo.http.PostHttpTask;
import org.springframework.http.HttpStatus;

/**
 * Created by maksim on 02.02.17.
 */

public class NewUserPopup extends AbstractPopup {

    private final static String TAG = NewUserPopup.class.getName();

    private TextField userNameTxt;
    private TextButton registerBtn;
    private Label errorLbl;

    public NewUserPopup init() {
        return this;
    }

    public NewUserPopup(DirectedGame game) {
        super(game);
    }

    @Override
    protected void rebuildStage() {
        userNameTxt.selectAll();
        errorLbl.setText(StringConstants.EMPTY);
        Gdx.input.setInputProcessor(stage);
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
        registerBtn = createOkBtn();
        popupWindow.add(registerBtn).fill().expand().pad(10f);
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
        return field;
    }

    private TextButton createOkBtn() {
        TextButton btn = new TextButton(Assets.instance.translation.get("ozo.register"), Assets.instance.skin.skin);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                errorLbl.setText(StringConstants.EMPTY);
                errorLbl.setVisible(true);
                Gdx.input.setInputProcessor(null);
                String userName = userNameTxt.getText();
                if (userName.length() < 3 || userName.length() > 32) {
                    errorLbl.setText(Assets.instance.translation.get("ozo.err.userNameLength"));
                    Gdx.input.setInputProcessor(stage);
                    return;
                }
                NewUserRequest req = new NewUserRequest();
                req.setUserName(userNameTxt.getText());
                game.platform.getHttpClient().executeTask(new PostHttpTask<NewUserRequest, NewUserResponse>(
                        req, NewUserResponse.class, HttpClient.URL.NEW_USER) {

                    @Override
                    public void onComplete(NewUserResponse result) {
                        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
                            Gdx.app.debug(TAG, "New user has been created with id=" + result);
                        }
                        final Preferences prefs = Gdx.app.getPreferences(Constants.SETTINGS_PATH);
                        prefs.putString(StringConstants.USER_ID, result.getUserId());
                        prefs.putString(StringConstants.USER_NAME, result.getUserName());
                        prefs.flush();
                        game.platform.getUser().setUserId(result.getUserId());
                        game.platform.getUser().setUserName(result.getUserName());

                        game.levelProvider.refreshResultsWithOwner();

                        errorLbl.setText(StringConstants.EMPTY);
                        errorLbl.setVisible(false);
                        Gdx.input.setInputProcessor(stage);
                        game.setPopup(null);
                    }

                    @Override
                    public void onFail(HttpStatus status) {
                        Gdx.input.setInputProcessor(stage);
                        errorLbl.setVisible(true);
                        switch (status) {
                            case CONFLICT:
                                errorLbl.setText(Assets.instance.translation.get("ozo.err.userNameExists"));
                                break;
                            case SERVICE_UNAVAILABLE:
                            case BAD_GATEWAY:
                                errorLbl.setText(Assets.instance.translation.get("ozo.err.serverUnavailable"));
                                break;
                            case INTERNAL_SERVER_ERROR:
                                errorLbl.setText(Assets.instance.translation.get("ozo.err.serverError"));
                                break;
                        }
                    }

                    @Override
                    public void onFail() {
                        Gdx.input.setInputProcessor(stage);
                        errorLbl.setVisible(true);
                        errorLbl.setText(Assets.instance.translation.get("ozo.err.serverError"));
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
