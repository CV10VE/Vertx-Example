package database;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.pgclient.PgPool;
import io.vertx.reactivex.sqlclient.Row;
import io.vertx.reactivex.sqlclient.RowSet;
import io.vertx.reactivex.sqlclient.SqlConnection;
import io.vertx.sqlclient.PoolOptions;



@Singleton
public class PgDbClient extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(PgDbClient.class);

  @Override
  public void start() throws Exception {

    PgConnectOptions connectOptions = new PgConnectOptions()
        .setPort(5432)
        .setHost("localhost")
        .setDatabase("database_barring")
        .setUser("root")
        .setPassword("root");

// Pool options
    PoolOptions poolOptions = new PoolOptions()
        .setMaxSize(5);

// Create the pooled client
    PgPool client = PgPool.pool(vertx, connectOptions, poolOptions);


// Get a connection from the pool
    client.getConnection(ar1 -> {

      if (ar1.succeeded()) {

        System.out.println("Connected");

        // Obtain our connection
        SqlConnection conn = ar1.result();

        // All operations execute on the same connection

        conn
            .query("SELECT * FROM barring")
            .execute(ar2 -> {
              if (ar2.succeeded()) {
                RowSet<Row> result = ar2.result();
                System.out.println("Got " + result.size() + " rows ");
              } else {
                // Release the connection to the pool
                conn.close();
              }
            });
      } else {
        System.out.println("Could not connect: " + ar1.cause().getMessage());
      }
    });

  }
}
