package org.dragberry.ozo.game.level;

import static org.dragberry.ozo.common.level.Levels.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import org.dragberry.ozo.common.CommonConstants;
import org.dragberry.ozo.common.level.Levels;
import org.dragberry.ozo.common.levelresult.AllLevelResults;
import org.dragberry.ozo.common.levelresult.LevelResults;
import org.dragberry.ozo.common.levelresult.NewLevelResultsRequest;
import org.dragberry.ozo.common.levelresult.NewLevelResultsResponse;
import org.dragberry.ozo.game.DirectedGame;
import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.settings.FreeplayReachTheGoalLevelSettings;
import org.dragberry.ozo.game.level.settings.LevelSettings;
import org.dragberry.ozo.game.level.settings.NoAnnihilationLevelSettings;
import org.dragberry.ozo.game.level.settings.ReachMultiGoalLevelSettings;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;
import org.dragberry.ozo.game.level.settings.SequenceReachTheGoalLevelSettings;
import org.dragberry.ozo.http.GetHttpTask;
import org.dragberry.ozo.http.HttpClient;
import org.dragberry.ozo.http.PostHttpTask;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by maksim on 02.03.17.
 */

public class LevelProvider {

    private static final String TAG = LevelProvider.class.getName();

    public final Array<LevelSettings> levels = new Array<LevelSettings>();

    public final LevelSettings freeplayLevel;

    public LevelProvider() {
        freeplayLevel = new FreeplayReachTheGoalLevelSettings(FreeplayLevel.class, Levels.L999_FREEPLAY, 0.8f, 10);

		levels.add(new ReachTheGoalLevelSettings(L000_TEST, -10, 2, JustReachGoal.Operator.MORE));
        levels.add(new ReachTheGoalLevelSettings(L001_LETS_START, -10, 10, JustReachGoal.Operator.MORE));
        levels.add(new ReachTheGoalLevelSettings(L002_LITTLE_BIT_HARDER, -15, 25, JustReachGoal.Operator.MORE));
        levels.add(new ReachTheGoalLevelSettings(L003_NEED_MORE, -11, 33, JustReachGoal.Operator.MORE));
        levels.add(new ReachMultiGoalLevelSettings(L004_DOUBLE_5, -10, 5, 5));
        levels.add(new ReachTheGoalLevelSettings(MushroomRainLevel.class, L005_MUSHROOM_RAIN ,-10, 25));
        levels.add(new NoAnnihilationLevelSettings(L006_SAVE_US, 10, 20));
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
        levels.add(new ReachTheGoalLevelSettings(RepentanceLevel.class, L018_REPENTANCE, -25, 5));
        levels.add(new ReachTheGoalLevelSettings(HighwayLevel.class, L019_HIGHWAY, -35, 10));
        levels.add(new SequenceReachTheGoalLevelSettings(DoublingLevel.class, L020_DOUBLING, -129, 8, -1));
        levels.add(new SequenceReachTheGoalLevelSettings(FibonacciLevel.class, L021_FIBONACCI, -145, 12, -1));
        levels.add(new SequenceReachTheGoalLevelSettings(RepentanceSequenceLevel.class, L022_SERIAL_REPENTANCE, -30, 5, 0));
        levels.add(new NoAnnihilationLevelSettings(NoAnnihilationWavesLevel.class, L023_TSUNAMI, 99, 999));

    }

    public void loadResults() {
        Gdx.app.debug(TAG, "loadResults...");
        freeplayLevel.load();
        for (LevelSettings levelSettings : levels) {
            levelSettings.load();
        }

        if (DirectedGame.game.platform.getHttpClient().isConnected()) {
            DirectedGame.game.platform.getHttpClient().executeTask(
                    new GetHttpTask<AllLevelResults>(AllLevelResults.class, HttpClient.URL.GET_ALL_RESULTS,
                            DirectedGame.game.platform.getUser().getId()) {

                @Override
                public void onComplete(AllLevelResults result) {
                    if (!CommonConstants.APP_VERSION.equals(result.getVersion())) {
                        DirectedGame.game.wrongAppVersion = true;
                        return;
                    }
                    Map<String, LevelResults> allResults = result.getLevelResults();

                    processLevel(allResults, freeplayLevel);
                    for (LevelSettings levelSettings : levels) {
                        processLevel(allResults, levelSettings);
                    }
                }
            });
        }
    }

    private void processLevel(Map<String, LevelResults> allResults, final LevelSettings levelSettings) {
        synchronized (levelSettings) {
            LevelResults results = allResults.get(levelSettings.levelId);
            if (results == null) {
                DirectedGame.game.wrongAppVersion = true;
                return;
            }
            NewLevelResultsRequest newResultsRequest = levelSettings.updateResults(results);
            newResultsRequest.setUserId(DirectedGame.game.platform.getUser().getId());
            levelSettings.save();
            if (!newResultsRequest.getResults().isEmpty() &&  !DirectedGame.game.platform.getUser().isDefault()) {
                Gdx.app.debug(TAG, "Level [" + levelSettings.levelId + "] results has changed offline");
                DirectedGame.game.platform.getHttpClient().executeTask(new PostHttpTask<NewLevelResultsRequest, NewLevelResultsResponse>(
                        newResultsRequest, NewLevelResultsResponse.class, "/level/result/new") {

                    @Override
                    public void onComplete(NewLevelResultsResponse result) {
                        Gdx.app.debug(TAG, "Level [" + levelSettings.levelId + "] results've updated after changing offline");
                        levelSettings.updateResults(result);
                        levelSettings.save();
                    }
                });
            }
        }
    }

    public void refreshResultsWithOwner() {
        es.submit(new Runnable() {
            @Override
            public void run() {
                freeplayLevel.refreshResultsWithOwner();
            }
        });

        for (final LevelSettings levelSettings : levels) {
            es.submit(new Runnable() {
                @Override
                public void run() {
                    levelSettings.refreshResultsWithOwner();
                }
            });
        }
    }

    public static final ExecutorService es = Executors.newFixedThreadPool(4);
}
