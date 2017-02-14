package org.dragberry.ozo.screen.popup;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.screen.ActionExecutor;
import org.dragberry.ozo.screen.DirectedGame;

/**
 * Created by maksim on 02.02.17.
 */

public class ConfirmationPopup extends AbstractPopup {

    public ConfirmationPopup(DirectedGame game) {
        super(game);
    }

    @Override
    protected void rebuildStage(float viewportWidth, float viewportHeight) {
        popupWindow.setWidth(viewportWidth * 0.75f);
        popupWindow.setHeight(viewportHeight / 4);
        popupWindow.setFillParent(true);
        Label msgLbl = new Label(
                Assets.instance.translation.get("ozo.exitConfirmationMsg"),
                Assets.instance.skin.skin);
        msgLbl.setWrap(true);
        msgLbl.setAlignment(Align.center);
        popupWindow.add(msgLbl).fillX().expandX();
        popupWindow.row().expand().fill();
        Table btns = new Table();
        btns.add(createConfirmBtn()).fill().expand().pad(10f);
        btns.add(createCancelBtn()).fill().expand().pad(10f);
        popupWindow.add(btns);
    }

    private TextButton createConfirmBtn() {
        TextButton btn = new TextButton(Assets.instance.translation.get("ozo.yes"), Assets.instance.skin.skin);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	game.hidePopup(new ActionExecutor() {
					
					@Override
					public void execute() {
						game.back();
					}
				});
            }
        });
        return btn;
    }

    private TextButton createCancelBtn() {
        TextButton btn = new TextButton(Assets.instance.translation.get("ozo.no"), Assets.instance.skin.skin);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setPopup(null);
            }
        });
        return btn;
    }

}
