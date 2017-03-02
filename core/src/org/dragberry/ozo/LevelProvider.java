package org.dragberry.ozo;

import static org.dragberry.ozo.common.level.Levels.*;

import com.badlogic.gdx.utils.Array;

import org.dragberry.ozo.common.levelresult.AllLevelResults;
import org.dragberry.ozo.common.levelresult.LevelResults;
import org.dragberry.ozo.game.level.ChessboardLevel;
import org.dragberry.ozo.game.level.MushroomRainLevel;
import org.dragberry.ozo.game.level.NoAnnihilationQueueLevel;
import org.dragberry.ozo.game.level.NoAnnihilationWavesLevel;
import org.dragberry.ozo.game.level.QueueLevel;
import org.dragberry.ozo.game.level.WavesLevel;
import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.settings.LevelSettings;
import org.dragberry.ozo.game.level.settings.NoAnnihilationLevelSettings;
import org.dragberry.ozo.game.level.settings.ReachMultiGoalLevelSettings;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;
import org.dragberry.ozo.http.HttpClient;
import org.dragberry.ozo.http.HttpTask;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by maksim on 02.03.17.
 */

public class LevelProvider {

    public final Array<LevelSettings> levels = new Array<LevelSettings>();

    public LevelProvider() {
//		levels.add(new ReachTheGoalLevelSettings("ozo.lvl.test", -10, 2, JustReachGoal.Operator.MORE));
        levels.add(new ReachTheGoalLevelSettings(L001_LETS_START, -10, 10, JustReachGoal.Operator.MORE));
        levels.add(new ReachTheGoalLevelSettings(L002_LITTLE_BIT_HARDER, -5, 25));
        levels.add(new ReachTheGoalLevelSettings(L003_NEED_MORE, -7, 49));
        levels.add(new ReachMultiGoalLevelSettings(L004_DOUBLE_5, -10, 5, 5));
        levels.add(new ReachTheGoalLevelSettings(MushroomRainLevel.class, L005_MUSHROOM_RAIN ,-10, 25));
        levels.add(new NoAnnihilationLevelSettings(L006_SAVE_US, 5, 25));
        levels.add(new ReachMultiGoalLevelSettings(L007_ROULETTE, -7, 7, 7, 7));
        levels.add(new ReachTheGoalLevelSettings(QueueLevel.class, L008_QUEUES, -15, 50));
        levels.add(new ReachMultiGoalLevelSettings(L009_STRAIGHT_FLASH, -15, 6, 7, 8, 9, 10));
        levels.add(new ReachTheGoalLevelSettings(ChessboardLevel.class, L010_CHESSBOARD, -20, 50));
        levels.add(new ReachTheGoalLevelSettings(MushroomRainLevel.class, L011_MUSHROOM_SHOWER, -25, 75));
        levels.add(new ReachTheGoalLevelSettings(WavesLevel.class, L012_WAVES, -15, 50));
        levels.add(new ReachMultiGoalLevelSettings(L013_CASINO_ROYALE, -99, 50, 50, 50));
        levels.add(new ReachTheGoalLevelSettings(QueueLevel.class, L014_REGULARITY, -33, 99));
        levels.add(new NoAnnihilationLevelSettings(L015_UNSAFE_PLACE, 9, 99));
        levels.add(new NoAnnihilationLevelSettings(NoAnnihilationQueueLevel.class, L016_UNSAFE_REGULARITY, 9, 99));
        levels.add(new NoAnnihilationLevelSettings(NoAnnihilationWavesLevel.class, L017_STORM, 9, 99));
        levels.add(new NoAnnihilationLevelSettings(NoAnnihilationWavesLevel.class, L018_TSUNAMI, 99, 999));

    }

    public void loadResults(HttpClient httpClient) {
        if (httpClient.isConnected()) {
            httpClient.executeTask(
                    new HttpTask<Void, AllLevelResults>(AllLevelResults.class, "/results/user/{0}", "userId") {

                @Override
                public void onComplete(AllLevelResults result) {
                    Map<String, LevelResults> allResults = new AllLevelResults().getLevelResults();

                    for (LevelSettings levelSettings : levels) {
                        LevelResults results = allResults.get(levelSettings.nameKey);
                        levelSettings.updateResults(results);
                        levelSettings.save();
                    }
                }
            });
        }
    }

}
