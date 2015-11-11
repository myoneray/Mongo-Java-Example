package mongo;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

public enum MongoTools {
    INSTANCE;
    public static final Logger LOGGER = LoggerFactory.getLogger(MongoTools.class);
    /**
     * 定义一个枚举的元素
     */
    private MongoClient mongoClient;

    /**
     * 定义一个块 读取配置信息
     */
    static {
        CompositeConfiguration conf = new CompositeConfiguration();
        try {
            conf.addConfiguration(new PropertiesConfiguration("mongodb.properties"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        String host = conf.getString("host");
        int port = conf.getInt("port");
        INSTANCE.mongoClient = new MongoClient(host, port);
    }

    /**
     * 查询所有DB名
     */
    public void getAllDatabaseNames() {
        LOGGER.info("===============查询所有DB名========================");
        MongoIterable<String> listDatabaseNames = mongoClient.listDatabaseNames();
        for (String string : listDatabaseNames) {
            System.out.println(string);
        }
    }

    /**
     * 获取DB实例 - 指定DB
     * 
     * @param dbName
     * @return
     */
    public MongoDatabase getMongoDatabase(String dbName) {
        if (dbName != null && !"".equals(dbName)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            return database;
        }
        return null;
    }

    /**
     * 查询DB下的所有表名
     * 
     * @param databaseName
     *            DB名
     */
    public void getAllCollections(String databaseName) {
        LOGGER.info("========查询" + databaseName + "下的所有表名========");
        MongoIterable<String> colls = getMongoDatabase(databaseName).listCollectionNames();
        for (String string : colls) {
            System.out.println(string);
        }
    }

    /**
     * 获取一個Collection实例
     * 
     * @param databaseName
     *            DB名
     * @param collectionName
     *            表名
     * @return MongoCollection<Document>
     */
    public MongoCollection<Document> getMongoCollection(String databaseName, String collectionName) {
        if (null == databaseName || "".equals(databaseName)) {
            return null;
        }
        if (null == collectionName || "".equals(collectionName)) {
            return null;
        }
        return getMongoDatabase(databaseName).getCollection(collectionName);
    }

    /**
     * 删除一个Collection
     */
    public void dropCollection(String dbName, String collName) {
        getMongoDatabase(dbName).getCollection(collName).drop();
        LOGGER.info(" 数据库{}中表{}已经删除！", dbName, collName);
    }

    /**
     * 删除一个数据库
     */
    public void dropDB(String dbName) {
        getMongoDatabase(dbName).drop();
        LOGGER.info(" 数据库{}已经删除！", dbName);
    }

    /**
     * 插入数据
     * 
     * @param collection
     *            Collection实例
     * @param document
     *            文件
     */
    public void insertDocument(MongoCollection<Document> collection, Document document) {
        collection.insertOne(document);
    }

    /**
     * 根据主键_id查询
     * 
     * @param collection
     * @param id
     * @return
     */
    public Document findById(MongoCollection<Document> collection, String id) {
        ObjectId _idobj = new ObjectId(id);
        Bson filter = Filters.eq("_id", _idobj);
        Document document = collection.find(filter).first();
        return document;
    }

    /**
     * 查询所有
     * 
     * @param collection
     * @return
     */
    public FindIterable<Document> findAll(MongoCollection<Document> collection) {
        return collection.find();
    }

    /**
     * 根据條件查询
     * 
     * @param coll
     * @param filter
     * @return
     */
    public MongoCursor<Document> findBy(MongoCollection<Document> coll, Bson filter) {
        return coll.find(filter).iterator();
    }

    /**
     * 根据條件删除
     * 
     * @param coll
     * @param filter
     * @return
     */
    public DeleteResult deleteBy(MongoCollection<Document> coll, Bson filter) {
        return coll.deleteMany(filter);
    }

    /** 分页查询 */
    public MongoCursor<Document> findByPage(MongoCollection<Document> coll, Bson filter, int pageNo, int pageSize) {
        Bson orderBy = new BasicDBObject("_id", 1);
        return coll.find(filter).sort(orderBy).skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
    }

    /**
     * 通过ID删除
     * 
     * @param coll
     * @param id
     * @return
     */
    public int deleteById(MongoCollection<Document> coll, String id) {
        int count = 0;
        ObjectId _id = null;
        try {
            _id = new ObjectId(id);
        } catch (Exception e) {
            return 0;
        }
        Bson filter = Filters.eq("_id", _id);
        DeleteResult deleteResult = coll.deleteOne(filter);
        count = (int) deleteResult.getDeletedCount();
        return count;
    }

    /**
     * 根据ID更新
     * 
     * @param coll
     * @param id
     * @param newdoc
     * @return
     */
    public Document updateById(MongoCollection<Document> coll, String id, Document newdoc) {
        ObjectId _idobj = null;
        try {
            _idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        Bson filter = Filters.eq("_id", _idobj);
        coll.updateOne(filter, new Document("$set", newdoc));
        return newdoc;
    }

    /**
     * 根据ID替代
     * 
     * @param coll
     * @param id
     * @param newdoc
     * @return
     */
    public Document replaceById(MongoCollection<Document> coll, String id, Document newdoc) {
        ObjectId _idobj = null;
        try {
            _idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        Bson filter = Filters.eq("_id", _idobj);
        coll.replaceOne(filter, newdoc);
        return newdoc;
    }

    /**
     * 关闭Mongodb
     */
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            LOGGER.info("Mongodb已经关闭！");
        }
    }
}
