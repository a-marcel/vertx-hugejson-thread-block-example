package example;

import com.hazelcast.config.Config;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.logging.SLF4JLogDelegateFactory;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class TestApplication {
	static Logger logger = LoggerFactory.getLogger(TestApplication.class);

	public static void main(String[] args) {

		Config hazelcastConfig = new Config();

		ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);

		VertxOptions vertxOptions = new VertxOptions().setClusterManager(mgr);

		Vertx.clusteredVertx(vertxOptions, res -> {
			if (res.succeeded()) {
				logger.info("Cluster ready, starting verticle deploy");

				Vertx vertx = res.result();

				vertx.deployVerticle("server.js", ar -> {
					if (ar.succeeded()) {
						logger.info("Done");
					} else {
						logger.error("Cannot deploy", ar.cause());
					}
				});
			}
		});
	}
}
