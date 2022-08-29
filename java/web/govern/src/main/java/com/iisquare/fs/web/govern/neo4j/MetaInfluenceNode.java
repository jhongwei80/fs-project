package com.iisquare.fs.web.govern.neo4j;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iisquare.fs.base.core.util.DPUtil;
import com.iisquare.fs.base.neo4j.mvc.Neo4jNodeBase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.springframework.stereotype.Component;

@Component
public class MetaInfluenceNode extends Neo4jNodeBase {

    public MetaInfluenceNode() {
        this.LABEL_NAME = "GovernInfluence";
    }

    public ObjectNode formatColumn(JsonNode db, Long time) {
        ObjectNode result = DPUtil.objectNode();
        result.put("id", db.get("catalog").asText() + db.get("model").asText() + "/" + db.get("code").asText());
        result.put("catalog", db.get("catalog").asText());
        result.put("model", db.get("model").asText());
        result.put("code", db.get("code").asText());
        if (null != time) result.put("time", time);
        return result;
    }

    public long deleteWithTime(Long time) {
        String cql = String.format("MATCH (n:%s) WHERE n.time <> $time DETACH DELETE n RETURN COUNT(n)", LABEL_NAME);
        try (Session session = driver.session()) {
            Result result = session.run(cql, Values.parameters("time", time));
            return result.single().get(0).asLong();
        }
    }

}
