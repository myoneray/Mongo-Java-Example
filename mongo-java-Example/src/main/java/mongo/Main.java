package mongo;
import java.util.Date;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

public class Main {

    public static void main(String[] args) {
        MongoTools tools = MongoTools.INSTANCE;
        MongoCollection<Document> coll = tools.getMongoCollection("test", "javaTestCollection");
        // 根据主键查找
        tools.findById(coll, "56429fcb210710079fe15193");
        // 添加
        for (int i = 0; i < 1000; i++) {
            Document document = new Document();
            document.put("no", (int) (Math.random() * 100));
            document.put("name", "maizi"+(int) (Math.random() * 10));
            document.put("tel", "1888" + (int) (Math.random() * 100000000));
            tools.insertDocument(coll, document);
        }
        // 删除
        tools.deleteById(coll, "5642b51221071043ddad87a6");
        // 更新
        Document newdoc = new Document();
        newdoc.put("updateName", "update" + new Date().toString());
        // 修改
        tools.updateById(coll, "5642b7822107104c81cba961", newdoc);
        // 替换
        tools.replaceById(coll, "5642b7822107104c81cba961", newdoc);
        // 分页
        MongoCursor<Document> mongoCursor = tools.findByPage(coll, Filters.eq("name", "maizi"), 2, 3);
        while (mongoCursor.hasNext()) {
            Document document2 = mongoCursor.next();
            System.out.println("分页:" + document2);
        }
        // 依据条件删除
        DeleteResult result = tools.deleteBy(coll, Filters.eq("key0", "value0"));
        System.out.println("刪除：" + result.getDeletedCount());

        // 查询所有
        FindIterable<Document> findIterable = tools.findAll(coll);
        for (Document document2 : findIterable) {
            System.out.println(document2);
        }
    }
}
