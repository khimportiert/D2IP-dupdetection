package app.db;

//import app.model.StorageDevice;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import org.bson.Document;
//
//import java.util.ArrayList;

public class MongoDB {
//    public static void insertStorageDevices(ArrayList<StorageDevice> storageDevices) {
//        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
//            MongoDatabase database = mongoClient.getDatabase("dupdetect");
//            MongoCollection<Document> collection = database.getCollection("storage_device");
//
//            for (StorageDevice s : storageDevices) {
//                Document doc = new Document("id", s.getId())
//                        .append("name", s.getName())
//                        .append("price", s.getPrice())
//                        .append("brand", s.getBrand())
//                        .append("description", s.getDescription())
//                        .append("category", s.getCategory());
//                collection.insertOne(doc);
//            }
//        }
//    }
}
