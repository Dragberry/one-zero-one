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
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import org.dragberry.ozo.game.*;
import org.dragberry.ozo.game.level.settings.LevelSettings;
import org.dragberry.ozo.game.util.Constants;

/**
 * Created by maksim on 28.01.17.
 */
public abstract class AbstractSelectLevelMenuScreen extends AbstractGameScreen {

    private Stage stage;
    private String screenTitle;

    protected ArrayMap<LevelSettings, TextButton> levelButtons;

    public AbstractSelectLevelMenuScreen(org.dragberry.ozo.game.DirectedGame game, String titleKey) {
        super(game);
        this.screenTitle = Assets.instance.translation.get(titleKey);
    }

    public AbstractSelectLevelMenuScreen init() {
        return this;
    }

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }

    @Override
    public void render(float deltaTime) {
    	Gdx.gl.glClearColor(Constants.BACKGROUND.r, Constants.BACKGROUND.g, Constants.BACKGROUND.b, Constants.BACKGROUND.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        game.platform.getAdsController().showBannerAd();
        if (stage == null) {
            levelButtons = new ArrayMap<LevelSettings, TextButton>();

            stage = new CustomStage(new ScalingViewport(Scaling.stretch,
                    Constants.VIEWPORT_GUI_WIDTH,
                    Gdx.graphics.getHeight() * Constants.VIEWPORT_GUI_WIDTH / Gdx.graphics.getWidth()),
                    new ActionExecutor() {

                        @Override
                        public void execute() {
                            game.back();
                        }
                    });
            buildStage();
        } else {
            rebuildStage();
        }
    }

    protected void addLevels(Table scrollTable) {
        for (LevelSettings levelSettings : game.levelProvider.levels) {
            TextButton btn = createLevelBtn(levelSettings);
            scrollTable.add(btn).fillX().expand(true, false).pad(5, 10, 5, 10);
            scrollTable.row();
        }
    }

    private void buildStage() {
        Table table = new Table();
        table.setFillParent(true);

        Label label = new Label(screenTitle, Assets.instance.skin.skin);
        label.setAlignment(Align.center);
        table.add(label).fill().expand().pad(game.platform.getAdsController().isBannerShown() ? 125f : 50f, 50f, 25f, 50f);
        table.row();

        Table scrollTable = new Table();

        addLevels(scrollTable);

        ScrollPane scroller = new ScrollPane(scrollTable);
        table.add(scroller).fill().expand();
        table.row().fill().expand();

        TextButton backBtn = new TextButton(Assets.instance.translation.format("ozo.back"), Assets.instance.skin.skin);
        backBtn.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.back();
            }
        });
        table.add(backBtn).fill(0.5f, 1f).pad(30f, 0f, 30f, 5f);
        this.stage.addActor(table);

        rebuildStage();
    }

    private void rebuildStage() {
        LevelState state = LevelState.COMPLETED;
        for (ObjectMap.Entry<LevelSettings, TextButton> entry : levelButtons.entries()) {
            state = getLevelState(state, entry);
            entry.value.setDisabled(isButtonDisabled(state));
            entry.value.setStyle(Assets.instance.skin.skin.get(state.style, TextButton.TextButtonStyle.class));
        }
    }

    protected abstract boolean isButtonDisabled(LevelState state);

    protected LevelState getLevelState(LevelState state, ObjectMap.Entry<LevelSettings, TextButton> entry) {
        if (state == LevelState.COMPLETED && !entry.key.completed) {
            state = LevelState.OPENED;
        } else if (entry.key.completed) {
            state = LevelState.COMPLETED;
        } else {
            state = LevelState.CLOSED;
        }
        return state;
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {
    }

    protected TextButton createLevelBtn(final LevelSettings levelSettings) {
        TextButton btn = new TextButton(levelSettings.name, Assets.instance.skin.skin);
        addButtonListener(btn, levelSettings);
        levelButtons.put(levelSettings, btn);
        return btn;
    }

    protected abstract void addButtonListener(TextButton btn, LevelSettings levelSettings);
    
    protected enum LevelState {
        CLOSED("level-closed"), OPENED("level-open"), COMPLETED("default");

        private String style;

        LevelState(String style) {
            this.style = style;
        }
    }

}
