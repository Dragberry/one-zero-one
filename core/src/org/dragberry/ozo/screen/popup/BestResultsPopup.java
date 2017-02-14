package org.dragberry.ozo.screen.popup;

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
		 
		 if (levelSettings.completed) {
			 Table resultTable = new Table();
			 for (Entry<String, Object> result : levelSettings.getResults().entries()) {
				 resultTable.add(new Label(result.key, skin)).fill().expand().pad(10f);
				 resultTable.add(new Label(result.value.toString(), skin)).fill().expand().pad(10f);
				 resultTable.row();
			 }
			 popupWindow.add(resultTable).row();
		 } else {
			 Label uncompleteLbl = new Label(
					 Assets.instance.translation.get("ozo.levelUncomplete"), skin);
			 uncompleteLbl.setWrap(true);
			 uncompleteLbl.setAlignment(Align.center);
			 popupWindow.add(uncompleteLbl).fillX().expandX().row();
		 }
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

}
