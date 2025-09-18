package imc.com;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class App {
    public static void main(String[] args) {
        // Connect to MongoDB on local system (port 27000)
        MongoClient mongoClient = new MongoClient("localhost", 27000);

        // Get a database (it will be created if it doesn't exist)
        MongoDatabase database = mongoClient.getDatabase("mydb");

        // Get a collection from the database (created if not exist)
        MongoCollection<Document> collection = database.getCollection("test");

        // Create a document to store
        Document doc = new Document("name", "Kevin Sim")
                .append("class", "Software Engineering Methods")
                .append("year", "2021")
                .append("result", new Document("CW", 95).append("EX", 85));

        // Add document to collection
        collection.insertOne(doc);

        // Retrieve and print the first document in the collection
        Document myDoc = collection.find().first();
        System.out.println(myDoc.toJson());

        // Close MongoDB connection
        mongoClient.close();
    }
}

