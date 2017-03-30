package org.dragberry.ozo.screen.popup;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.DirectedGame;

/**
 * Created by maksim on 30.03.17.
 */

public class FreeplayFinishedPopup extends AbstractGameFinishedPopup {

    private Label gameOverLbl;
    private Label noNewResultLbl;

    private TextButton playAgainBtn;
    private TextButton mainMenuBtn;

    public FreeplayFinishedPopup(DirectedGame game) {
        super(game);
    }

    @Override
    protected Label getTitleLabel() {
        return gameOverLbl;
    }

    @Override
    protected Label getNoNewResultsLabel() {
        return noNewResultLbl;
    }

    protected void addButtons() {
        popupWindow.add(playAgainBtn).fill().expand().pad(10f);
        popupWindow.row();
        popupWindow.add(mainMenuBtn).fill().expand().pad(10f);
    }

    protected void createLabels() {
        super.createLabels();

        gameOverLbl = new Label(Assets.instance.translation.get("ozo.gameOver"), Assets.instance.skin.skin);
        gameOverLbl.setAlignment(Align.center);

         noNewResultLbl = new Label(Assets.instance.translation.get("ozo.noNewResults"), Assets.instance.skin.skin);
        noNewResultLbl.setAlignment(Align.center);
    }

    protected void createButtons() {
        playAgainBtn = createPlayAgainBtn();
        mainMenuBtn = createMainMenuBtn();
    }

}
