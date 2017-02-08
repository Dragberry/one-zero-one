package org.dragberry.ozo.screen.popup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.screen.DirectedGame;
import org.dragberry.ozo.screen.MenuSkin;

/**
 * Created by maksim on 02.02.17.
 */

public class ConfirmationPopup extends AbstractPopup {

    private Stage stage;

    public ConfirmationPopup(DirectedGame game) {
        super(game);
    }

    @Override
	public InputProcessor getInputProcessor() {
        return stage;
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void show() {
        stage = new Stage();
        rebuildStage();
    }

    private void rebuildStage() {
        Table tbl = new Table();
        tbl.align(Align.center);
        final float viewportWidth = stage.getViewport().getCamera().viewportWidth;
        final float viewportHeight = stage.getViewport().getCamera().viewportHeight;
        tbl.setWidth(viewportWidth * 0.75f);
        tbl.setHeight(viewportHeight / 4);
        tbl.setPosition(viewportWidth * 0.125f, viewportHeight * 0.375f);
        stage.addActor(tbl);
        tbl.row().fill().expand();
        Label msgLbl = new Label(Assets.instance.translation.get("ozo.exitConfirmationMsg"), MenuSkin.getSkin());
        msgLbl.setAlignment(Align.center);
        tbl.add(msgLbl).fillX().expandX();
        tbl.row().expand().fill();
        Table btns = new Table();
        btns.add(createConfirmBtn()).fill().expand();
        btns.add(createCancelBtn()).fill().expand();
        tbl.add(btns);
    }

    private TextButton createConfirmBtn() {
        TextButton btn = new TextButton(Assets.instance.translation.get("ozo.yes"), MenuSkin.getSkin());
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.back();
            }
        });
        return btn;
    }

    private TextButton createCancelBtn() {
        TextButton btn = new TextButton(Assets.instance.translation.get("ozo.no"), MenuSkin.getSkin());
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setPopup(null);
            }
        });
        return btn;
    }

    @Override
    public void hide() {
        stage.dispose();
    }
}
