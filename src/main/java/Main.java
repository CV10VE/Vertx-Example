import io.netty.channel.DefaultChannelId;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import kafka.Producer;
import verticles.Server;

import java.text.MessageFormat;
import java.util.Calendar;


public class Main extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        long startTime = Calendar.getInstance().getTimeInMillis();
        DefaultChannelId.newInstance();
        DeploymentOptions defaultDeploymentOptions = new DeploymentOptions();
        defaultDeploymentOptions.setInstances(1);
        VertxOptions vertxOptions = new VertxOptions();

        final Vertx vertx = Vertx.vertx(vertxOptions);
        vertx.rxDeployVerticle(Producer.class.getName(), defaultDeploymentOptions).subscribe(verticleID -> {
            LOGGER.info(String.format("verticle ID %s deployed!", verticleID));
        });
        vertx.rxDeployVerticle(Server.class.getName(), defaultDeploymentOptions).subscribe(verticleID -> {
            LOGGER.info(String.format("verticle ID %s deployed!", verticleID));
        });



        long endTime = Calendar.getInstance().getTimeInMillis();
        double startingAppTimeInSeconds = (endTime - startTime) / 1000.0;

        LOGGER.info("-----------------------------------------------------------------------------");
        LOGGER.info(
                MessageFormat.format(
                        "|||| Simple Vertx with Kafka - Application started successfully in {0} seconds ||||",
                        startingAppTimeInSeconds));
        LOGGER.info("------------------------------------------------------------------------------");

    }
}
