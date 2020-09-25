package verticles;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.*;
import kafka.Producer;
import service.BarringService;


public class Server extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private BarringService barringService;
    private Producer producer;

    public void start() {
       // HttpServerOptions options = new HttpServerOptions().setLogActivity(true);
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        barringService = new BarringService();
        producer = new Producer(vertx);


        router.get("/getBarrings").produces("*/json").handler(routingContext -> {
            routingContext.response().setChunked(true).end(Json.encodePrettily(barringService.findAll()));
        });


        router.get("/getBarrings/:name").produces("*/json").handler(routingContext -> {
            String name = routingContext.request().getParam("name");
            routingContext.response().setChunked(true).end(Json.encodePrettily(barringService.findByBarringname(name)));

        });

        router.get("/").produces("*/json").handler(
                routingContext -> routingContext.response().setChunked(true).end("Hello Vertx"));

        router.post("/addBarring/:barring").handler(BodyHandler.create()).handler(routingContext -> {
            String barringName = routingContext.request().getParam("barring");
            HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            barringService.create(barringName);
            producer.sendBarring(barringName);
            response.end("Barring added");
        });

        server.requestHandler(router::accept).listen(8080);
    }
}
