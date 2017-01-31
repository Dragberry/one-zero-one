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

import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.level.MashroomRainLevel;
import org.dragberry.ozo.game.level.NoAnnihilationLevel;
import org.dragberry.ozo.game.level.ReachMultiGoalLevel;
import org.dragberry.ozo.game.level.ReachTheGoalLevel;
import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.screen.transitions.ScreenTransitionFade;

import java.lang.reflect.Constructor;

/**
 * Created by maksim on 28.01.17.
 */
public class SelectLevelMenuScreen extends AbstractGameScreen {

    private static final String TAG = SelectLevelMenuScreen.class.getName();

    private static class LevelInfo {
        private Class<? extends Level> clazz;
        private Object[] params;

        LevelInfo(Class<? extends Level> clazz, String name, Object... params) {
        	this.clazz = clazz;
            this.params = new Object[params.length + 1]; 
            this.params[0] = name;
            System.arraycopy(params, 0, this.params, 1, params.length);
        }
    }

    private static final ArrayMap<String, LevelInfo> levels = new ArrayMap<String, LevelInfo>(true, 1);
    static {
        levels.put("Let's start!", new LevelInfo(ReachTheGoalLevel.class, "Let's start!", -10, 5, JustReachGoal.Operator.MORE));
        levels.put("A little bit harder", new LevelInfo(ReachTheGoalLevel.class, "A little bit harder", -5, 5));
        levels.put("We need more!", new LevelInfo(ReachTheGoalLevel.class, "We need more!", -10, 25));
        levels.put("Double 5", new LevelInfo(ReachMultiGoalLevel.class, "Double 5", -10, new Integer[] { 5, 5 }));
        levels.put("Casino Royale", new LevelInfo(ReachMultiGoalLevel.class, "Casino Royale", -10, new Integer[] { 7, 7, 7 }));
        levels.put("No annihilation (5)", new LevelInfo(NoAnnihilationLevel.class, "No annihilation (5)", 5, 10));
        levels.put("The Mashroom Rain", new LevelInfo(MashroomRainLevel.class, "The Mashroom Rain", -10, 25));
    }

    private Stage stage;

    SelectLevelMenuScreen(DirectedGame game) {
        super(game);
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
        stage = new Stage();
        rebuildStage();
    }

    private void rebuildStage() {
        Table table = new Table();
        table.setFillParent(true);

        Label label = new Label("Select Level", MenuSkin.getSkin());
        label.setAlignment(Align.center);
        table.add(label).fill().expand();
        table.row();

        Table scrollTable = new Table();
        for (ObjectMap.Entry<String, LevelInfo> entry : levels.entries()) {
            scrollTable.add(createLevelBtn(entry.key, entry.value)).fillX().expand(true, false);
            scrollTable.row();
        }
        ScrollPane scroller = new ScrollPane(scrollTable);
        table.add(scroller).fill().expand();
        table.row().fill().expand();

        TextButton backBtn = new TextButton("Back", MenuSkin.getSkin());
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game), ScreenTransitionFade.init());
            }
        });
        table.add(backBtn);
        this.stage.addActor(table);
    }


    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void pause() {
    }

    private TextButton createLevelBtn(String btnLabel, final LevelInfo levelInfo) {
        TextButton btn = new TextButton(btnLabel, MenuSkin.getSkin());
        btn.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    Class<?>[] paramClasses = new Class<?>[levelInfo.params.length];
                    for (int i = 0; i < levelInfo.params.length; i++) {
                        paramClasses[i] = levelInfo.params[i].getClass();
                    }
                    Constructor<? extends Level> constructor = levelInfo.clazz.getConstructor(paramClasses);
                    Level level = constructor.newInstance(levelInfo.params);
                    game.setScreen(new GameScreen(game, level), ScreenTransitionFade.init());
                } catch (Exception exc) {
                    Gdx.app.debug(TAG, "An exception has occured during level creation", exc);
                }
            }
        });
        return btn;
    }

}