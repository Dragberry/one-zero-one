package org.dragberry.ozo.screen.popup;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.DirectedGame;

/**
 * Created by maksim on 02.02.17.
 */

public class RatingPopup extends AbstractPopup {


    public RatingPopup init() {
        return this;
    }

    public RatingPopup(DirectedGame game) {
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
                Assets.instance.translation.get("ozo.likeTheGame"),
                Assets.instance.skin.skin.get("header", Label.LabelStyle.class));
        msgLbl.setWrap(true);
        msgLbl.setAlignment(Align.center);
        popupWindow.add(msgLbl).padBottom(20f).fillX().expandX();
        popupWindow.row().expand().fill();

        msgLbl = new Label(
                Assets.instance.translation.get("ozo.ratingProposal"),
                Assets.instance.skin.skin);
        msgLbl.setWrap(true);
        msgLbl.setAlignment(Align.center);
        popupWindow.add(msgLbl).padBottom(20f).fillX().expandX();
        popupWindow.row().expand().fill();

        popupWindow.add(createRateBtn()).fill().expand().pad(10f);
        popupWindow.row();
        popupWindow.add(createNoThanksBtn()).fill().expand().pad(10f);
    }

    private TextButton createNoThanksBtn() {
        TextButton btn = new TextButton(Assets.instance.translation.get("ozo.noThanks"),
                Assets.instance.skin.skin.get("background-button", TextButton.TextButtonStyle.class));
        btn.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.platform.getHttpClient().isConnected()) {
                    game.platform.getAdsController().showInterstitialAd(new Runnable() {
                        @Override
                        public void run() {
                            game.exit();
                        }
                    });
                } else {
                    game.exit();
                }
            }
        });
        return btn;
    }

    private TextButton createRateBtn() {
        TextButton btn = new TextButton(Assets.instance.translation.get("ozo.rateTheGame"),
                Assets.instance.skin.skin);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.platform.openMarketUrl();
                game.ratingPopupShowCounter = DirectedGame.RATING_POPUP_ALREADY_SHOWN;
                game.exit();
            }
        });
        return btn;
    }

}
