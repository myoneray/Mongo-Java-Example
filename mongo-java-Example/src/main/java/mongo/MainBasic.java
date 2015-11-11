package mongo;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryOperators;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class MainBasic {

    public static void main(String[] args) {
        MongoTools tools = MongoTools.INSTANCE;
        MongoCollection<Document> coll = tools.getMongoCollection("test", "javaTestCollection");

        // AND 查询所有 "name"＝"maizi9" AND "no"＝ 1
        BasicDBObject queryObject = new BasicDBObject();
        queryObject.put(QueryOperators.AND, new BasicDBObject[] { new BasicDBObject("name", "maizi9"),
                new BasicDBObject("no", 1) });
        MongoCursor<Document> findAndResult = coll.find(queryObject).iterator();
        while (findAndResult.hasNext()) {
            System.out.println(" AND: " + findAndResult.next());
        }

        // regex 查询所有 "name"以"maizi1"開頭 並且"tel"以"1888971"開頭
        BasicDBObject regexQuery = new BasicDBObject();
        regexQuery.put("name", new BasicDBObject("$regex", "^maizi1"));
        regexQuery.put("tel", new BasicDBObject("$regex", "^1888971"));
        MongoCursor<Document> cursor = coll.find(regexQuery).iterator();
        while (cursor.hasNext()) {
            System.out.println(" REGEX: " + cursor.next());
        }

        // 大於
        BasicDBObject gtDBObject = new BasicDBObject();
        gtDBObject.put("no", new BasicDBObject(QueryOperators.GTE, 99));
        gtDBObject.put("name", "maizi1");
        MongoCursor<Document> gtmongoCursor = coll.find(gtDBObject).iterator();
        while (gtmongoCursor.hasNext()) {
            System.out.println(" GT " + gtmongoCursor.next());
        }

    }
}
