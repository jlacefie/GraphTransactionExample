import com.datastax.driver.dse.DseCluster;
import com.datastax.driver.dse.DseSession;
import com.datastax.driver.dse.graph.GraphResultSet;
import com.datastax.driver.dse.graph.GraphStatement;
import com.datastax.driver.dse.graph.SimpleGraphStatement;

import com.datastax.driver.dse.graph.VertexProperty;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Example {
    private static final Logger logger = LoggerFactory.getLogger(Example.class);
    private static final String GRAPH_HOST = "192.168.56.20";
    private static final String GRAPH_NAME = "Customer";

    public static void main(String[] args){

       Example e = new Example();
    }

    private Example() {
        DseCluster dseCluster = DseCluster.builder()
                .addContactPoint(GRAPH_HOST).build();
        DseSession dseSession = dseCluster.connect();
        logger.debug("Connected to " + GRAPH_HOST);


        // add vertices and edges
        logger.debug("Test File Output: " + getQuery("Traversal"));
        GraphStatement s1 = new SimpleGraphStatement(getQuery("Traversal")).setGraphName(GRAPH_NAME);
        dseSession.executeGraph(s1);

        // Query the graph for the new vertex
        GraphStatement s2 = new SimpleGraphStatement("g.V().hasLabel('customer').out()").setGraphName(GRAPH_NAME);
        GraphResultSet rs = dseSession.executeGraph(s2);
        VertexProperty vp = rs.one().asVertex().getProperty("name");

        logger.debug("Vertex : " + vp.toString());
    }

    private String getQuery(String name) {
        try {
            return IOUtils.toString(getClass().getResourceAsStream(name+".groovy"),"US-ASCII");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}