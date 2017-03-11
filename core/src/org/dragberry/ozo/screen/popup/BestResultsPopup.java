package org.dragberry.ozo.screen.popup;

import org.dragberry.ozo.common.levelresult.LevelResultName;
import org.dragberry.ozo.common.levelresult.LevelSingleResult;
import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.level.settings.LevelSettings;
import org.dragberry.ozo.screen.DirectedGame;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap.Entry;

import java.util.Map;

public class BestResultsPopup extends AbstractPopup {

	private final LevelSettings levelSettings;
	
	public BestResultsPopup(DirectedGame game, LevelSettings levelSettings) {
		super(game);
		this.levelSettings = levelSettings;
	}

	@Override
	protected void rebuildStage(float viewportWidth, float viewportHeight) {
		 popupWindow.setWidth(viewportWidth * 0.75f);
		 popupWindow.setHeight(viewportHeight / 2);
		 Skin skin = Assets.instance.skin.skin;
		 Label msgLbl = new Label(levelSettings.name, skin);
		 msgLbl.setWrap(true);
		 msgLbl.setAlignment(Align.center);
		 popupWindow.add(msgLbl).fillX().expandX();
		 popupWindow.row().expand().fill();

		if (!levelSettings.completed) {
			Label uncompleteLbl = new Label(
					Assets.instance.translation.get("ozo.levelUncomplete"), skin);
			uncompleteLbl.setWrap(true);
			uncompleteLbl.setAlignment(Align.center);
			popupWindow.add(uncompleteLbl).fillX().expandX();
			popupWindow.row().expand().fill();
		}


		 Table resultTable = new Table();
		 Label lbl = new Label("", skin);
		 resultTable.add(lbl).colspan(4).fill().expand().pad(5f);
		 lbl = new Label(Assets.instance.translation.get("ozo.personal"), skin);
		 resultTable.add(lbl).colspan(1).fill().expand().pad(5f);
		 lbl = new Label(Assets.instance.translation.get("ozo.world"), skin);
		 resultTable.add(lbl).colspan(1).fill().expand().pad(5f);
		 resultTable.row();

		 for (Map.Entry<LevelResultName, LevelSingleResult<Integer>> result : levelSettings.results.getResults().entrySet()) {
			 LevelResultName name = result.getKey();
			 lbl = new Label(Assets.instance.translation.get(name.key()), skin);
			 lbl.setWrap(true);
			 resultTable.add(lbl).colspan(4).fill().expand().pad(5f);
			 LevelSingleResult<Integer> value = result.getValue();
			 lbl = new Label(name.toString(value.getPersonal()), skin);
			 lbl.setWrap(true);
			 lbl.setAlignment(Align.center);
			 resultTable.add(lbl).colspan(1).fill().expand().pad(5f, 0f, 5f, 10f);
			 lbl = new Label(name.toString(value.getWorlds()), skin);
			 lbl.setWrap(true);
			 lbl.setAlignment(Align.center);
			 resultTable.add(lbl).colspan(1).fill().expand().pad(5f, 0f, 5f, 10f);
			 resultTable.row();
		 }
		 popupWindow.add(resultTable).row();
		 popupWindow.add(createBackBtn());
	}
	
	private TextButton createBackBtn() {
        TextButton btn = new TextButton(Assets.instance.translation.get("ozo.back"), Assets.instance.skin.skin);
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
