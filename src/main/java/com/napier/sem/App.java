package com.napier.sem;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class App {
    public static void main(String[] args) {
        // Use try-with-resources to ensure MongoClient closes properly
        try (MongoClient mongoClient = new MongoClient("localhost", 27000)) {
            // Get database (created automatically if it doesn’t exist)
            MongoDatabase database = mongoClient.getDatabase("mydb");

            // Get collection (created automatically if it doesn’t exist)
            MongoCollection<Document> collection = database.getCollection("test");

            // Create a document
            Document doc = new Document("name", "Kevin Sim")
                    .append("class", "Software Engineering Methods")
                    .append("year", "2021")
                    .append("result", new Document("CW", 95).append("EX", 85));

            // Insert into collection
            collection.insertOne(doc);

            // Retrieve first document
            Document myDoc = collection.find().first();
            if (myDoc != null) {
                System.out.println("Inserted Document: " + myDoc.toJson());
            } else {
                System.out.println("No document found.");
            }
        }
    }
}
