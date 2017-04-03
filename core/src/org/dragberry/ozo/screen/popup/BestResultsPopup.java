package org.dragberry.ozo.screen.popup;

import org.dragberry.ozo.common.levelresult.LevelResultName;
import org.dragberry.ozo.common.levelresult.LevelSingleResult;
import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.level.settings.LevelSettings;
import org.dragberry.ozo.game.DirectedGame;
import org.dragberry.ozo.game.util.StringConstants;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.Map;

public class BestResultsPopup extends AbstractPopup {

	private LevelSettings levelSettings;

	private Skin skin = Assets.instance.skin.skin;

	private Label msgLbl;
	private Label incompleteLbl;
	private Label personalLbl;
	private Label worldLbl;
	private Label emptyLbl;

	private Table resultTable;

	private TextButton backBtn;

	public BestResultsPopup init(LevelSettings levelSettings) {
		this.levelSettings = levelSettings;
		return this;
	}

	public BestResultsPopup(DirectedGame game) {
		super(game);
	}

	@Override
	protected void rebuildStage() {
		popupWindow.clear();
		resultTable.clear();

		popupWindow.add(msgLbl).fillX().expandX();
		popupWindow.row().expand().fill();

		if (!levelSettings.completed) {
			popupWindow.add(incompleteLbl).fillX().expandX();
			popupWindow.row().expand().fill();
		}

		resultTable.add(emptyLbl).colspan(4).fill().expand().pad(5f);
		resultTable.add(personalLbl).colspan(1).fill().expand().pad(5f);
		resultTable.add(worldLbl).colspan(1).fill().expand().pad(5f);
		resultTable.row();


		Label lbl;
		String owner = Assets.instance.translation.get("ozo.owner");
		for (Map.Entry<LevelResultName, LevelSingleResult<Integer>> result : levelSettings.results.getResults().entrySet()) {
			LevelSingleResult<Integer> value = result.getValue();

			LevelResultName name = result.getKey();
			lbl = new Label(Assets.instance.translation.get(name.key()), skin);
			lbl.setWrap(true);
			resultTable.add(lbl).colspan(4).fill().expand().pad(3f);

			lbl = new Label(name.toString(value.getPersonal()), skin);
			lbl.setWrap(true);
			lbl.setAlignment(Align.center);
			resultTable.add(lbl).colspan(1).fill().expand().pad(3f, 0f, 0f, 10f);

			lbl = new Label(name.toString(value.getWorlds()), skin);
			lbl.setWrap(true);
			lbl.setAlignment(Align.center);
			resultTable.add(lbl).colspan(1).fill().expand().pad(3f, 0f, 0f, 10f);
			resultTable.row();

			lbl = new Label(owner + StringConstants.SPACE
					+ value.getOwner() == null ? StringConstants.DASH : value.getOwner(), skin);
			lbl.setWrap(true);
			lbl.setAlignment(Align.left);
			resultTable.add(lbl).colspan(6).fill().expand().pad(0f, 20f, 0f, 10f);
			resultTable.row();
		}
		popupWindow.add(resultTable).row();
		popupWindow.add(backBtn);
	}

	@Override
	protected void buildStage(float viewportWidth, float viewportHeight) {
		popupWindow.setWidth(viewportWidth * 0.75f);
		popupWindow.setHeight(viewportHeight * 0.6f);

		msgLbl = new Label(levelSettings.name, skin);
		msgLbl.setWrap(true);
		msgLbl.setAlignment(Align.center);

		incompleteLbl = new Label(
				Assets.instance.translation.get("ozo.levelIsNotCompleted"), skin);
		incompleteLbl.setWrap(true);
		incompleteLbl.setAlignment(Align.center);

		resultTable = new Table();

		emptyLbl = new Label(StringConstants.EMPTY, skin);
		personalLbl = new Label(Assets.instance.translation.get("ozo.personal"), skin);
		worldLbl = new Label(Assets.instance.translation.get("ozo.world"), skin);

		backBtn = createBackBtn();

		rebuildStage();
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
