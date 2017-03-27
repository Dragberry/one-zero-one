package org.dragberry.ozo.screen.popup;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import org.dragberry.ozo.common.audit.LevelAttemptAuditEventRequest;
import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.DirectedGame;
import org.dragberry.ozo.game.GameController;
import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.screen.ActionExecutor;

/**
 * Created by maksim on 02.02.17.
 */

public class WrongVersionPopup extends AbstractPopup {


    public WrongVersionPopup init() {
        return this;
    }

    public WrongVersionPopup(DirectedGame game) {
        super(game);
    }

    @Override
    protected void rebuildStage() {

    }

    @Override
    protected void buildStage(float viewportWidth, float viewportHeight) {
        popupWindow.setWidth(viewportWidth * 0.75f);
        popupWindow.setHeight(viewportHeight * 0.375f);
        Label msgLbl = new Label(
                Assets.instance.translation.get("ozo.applicationVersionDeprecated"),
                Assets.instance.skin.skin.get("header", Label.LabelStyle.class));
        msgLbl.setWrap(true);
        msgLbl.setAlignment(Align.center);
        popupWindow.add(msgLbl).padBottom(20f).fillX().expandX();
        popupWindow.row().expand().fill();

        popupWindow.add(createExitBtn()).fill().expand().pad(10f);
        popupWindow.row();
        popupWindow.add(createUpdatetBtn()).fill().expand().pad(10f);
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

    private TextButton createUpdatetBtn() {
        TextButton btn = new TextButton(Assets.instance.translation.get("ozo.updateApplication"), Assets.instance.skin.skin);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.platform.openMarketUrl();
            }
        });
        return btn;
    }

}
