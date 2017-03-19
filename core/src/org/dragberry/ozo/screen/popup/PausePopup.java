package org.dragberry.ozo.screen.popup;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import org.dragberry.ozo.common.audit.LevelAttemptAuditEventRequest;
import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.GameController;
import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.screen.ActionExecutor;
import org.dragberry.ozo.game.DirectedGame;

/**
 * Created by maksim on 02.02.17.
 */

public class PausePopup extends AbstractPopup {

    private LevelAttemptAuditEventRequest levelAttempt;

    private Level<?> level;

    private static PausePopup instance;

    public static PausePopup init(DirectedGame game, Level<?> level, LevelAttemptAuditEventRequest attempt) {
        if (instance == null) {
            instance = new PausePopup(game);
        }
        instance.level = level;
        instance.levelAttempt = attempt;
        return instance;
    }

    private PausePopup(DirectedGame game) {
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
                Assets.instance.translation.get("ozo.pause"),
                Assets.instance.skin.skin.get("header", Label.LabelStyle.class));
        msgLbl.setWrap(true);
        msgLbl.setAlignment(Align.center);
        popupWindow.add(msgLbl).padBottom(20f).fillX().expandX();
        popupWindow.row().expand().fill();

        popupWindow.add(createContinueBtn()).fill().expand().pad(10f);
        popupWindow.row();
        popupWindow.add(createRestartBtn()).fill().expand().pad(10f);
        popupWindow.row();
        popupWindow.add(creatExitBtn()).fill().expand().pad(10f);
    }

    private TextButton createContinueBtn() {
        TextButton btn = new TextButton(Assets.instance.translation.get("ozo.continue"), Assets.instance.skin.skin);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setPopup(null);
            }
        });
        return btn;
    }

    private TextButton createRestartBtn() {
        TextButton btn = new TextButton(Assets.instance.translation.get("ozo.restart"), Assets.instance.skin.skin);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.hidePopup(new ActionExecutor() {

                    @Override
                    public void execute() {
                        GameController.instance.init(game, level, true);
                        level.started = true;
                    }
                });
            }
        });
        return btn;
    }

    private TextButton creatExitBtn() {
        TextButton btn = new TextButton(Assets.instance.translation.get("ozo.exit"), Assets.instance.skin.skin);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.hidePopup(new ActionExecutor() {

                    @Override
                    public void execute() {
                        level.settings.saveState(level, true);
                        game.logAuditEvent(levelAttempt);
                        game.back();
                    }
                });
            }
        });
        return btn;
    }

}
