package org.dragberry.ozo.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.level.settings.LevelSettings;
import org.dragberry.ozo.game.util.Constants;

/**
 * Created by maksim on 28.01.17.
 */
public abstract class AbstractSelectLevelMenuScreen extends AbstractGameScreen {

    private Stage stage;
    private String screenTitle;

    public AbstractSelectLevelMenuScreen(DirectedGame game, String titleKey) {
        super(game);
        this.screenTitle = Assets.instance.translation.get(titleKey);
    }

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(MenuSkin.BACKGROUND_COLOR.r, MenuSkin.BACKGROUND_COLOR.g, MenuSkin.BACKGROUND_COLOR.b, MenuSkin.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        stage = new Stage(new ScalingViewport(Scaling.stretch,
                Constants.VIEWPORT_GUI_WIDTH,
                Gdx.graphics.getHeight() *  Constants.VIEWPORT_GUI_WIDTH / Gdx.graphics.getWidth()));

        rebuildStage();
    }

    private void rebuildStage() {
        Table table = new Table();
        table.setFillParent(true);

        Label label = new Label(screenTitle, MenuSkin.getSkin());
        label.setAlignment(Align.center);
        table.add(label).fill().expand().pad(50f, 50f, 25f, 50f);
        table.row();

        Table scrollTable = new Table();
        LevelState state = LevelState.COMPLETED;
        for (LevelSettings levelSettings : game.levels) {
            if (state == LevelState.COMPLETED && !levelSettings.completed) {
                state = LevelState.OPENED;
            } else if (levelSettings.completed) {
                state = LevelState.COMPLETED;
            } else {
                state = LevelState.OPENED;
            }
            scrollTable.add(createLevelBtn(state, levelSettings)).fillX().expand(true, false).pad(5, 10, 5, 10);
            scrollTable.row();
        }
        ScrollPane scroller = new ScrollPane(scrollTable);
        table.add(scroller).fill().expand();
        table.row().fill().expand();

        TextButton backBtn = new TextButton(Assets.instance.translation.format("ozo.back"), MenuSkin.getSkin());
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.back();
            }
        });
        table.add(backBtn).fill(0.5f, 1f).pad(30f, 0f, 30f, 5f);
        this.stage.addActor(table);
    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void pause() {
    }

    private TextButton createLevelBtn(LevelState state, final LevelSettings levelSettings) {
        TextButton btn = new TextButton(levelSettings.name, MenuSkin.getSkin().get(state.style, TextButton.TextButtonStyle.class));
        if (state != LevelState.CLOSED) {
            btn.setDisabled(true);
            btn.addListener(getActionListener(levelSettings));
        }
        return btn;
    }
    
    protected abstract ClickListener getActionListener(LevelSettings levelSettings);

    private enum LevelState {
        CLOSED("level-closed"), OPENED("level-open"), COMPLETED("default");

        private String style;

        LevelState(String style) {
            this.style = style;
        }
    }

}
