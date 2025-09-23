package com.napier.sem;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.MongoException;
import org.bson.Document;

public class App
{
    public static void main(String[] args)
    {
        // ------------------------------
        // Step 1: Determine MongoDB URI
        // ------------------------------
        // Try reading from environment variable first
        String mongoUri = System.getenv("MONGO_URI");

        if (mongoUri == null || mongoUri.isEmpty()) {
            // Detect OS
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("linux")) {
                // WSL2/Linux needs this to connect to Windows MongoDB
                mongoUri = "mongodb://host.docker.internal:27017";
            } else {
                // Default for native Windows
                mongoUri = "mongodb://localhost:27017";
            }

            // Optional: if using Docker container, use container hostname
            // mongoUri = "mongodb://mongo-dbserver:27017";
        }

        System.out.println("Connecting to MongoDB at: " + mongoUri);

        // ----------------------------------------
        // Step 2: Connect, create database & collection
        // ----------------------------------------
        try (MongoClient mongoClient = MongoClients.create(mongoUri)) {

            // Test connection
            mongoClient.listDatabaseNames().first(); // throws if cannot connect

            // Get database and collection
            MongoDatabase database = mongoClient.getDatabase("mydb");
            MongoCollection<Document> collection = database.getCollection("test");

            // Create a document
            Document doc = new Document("name", "Kevin Sim")
                    .append("class", "Software Engineering Methods")
                    .append("year", "2021")
                    .append("result", new Document("CW", 95).append("EX", 85));

            // Insert document
            collection.insertOne(doc);

            // Retrieve first document and print
            Document myDoc = collection.find().first();
            System.out.println("Inserted document:");
            System.out.println(myDoc.toJson());

        } catch (MongoException e) {
            System.err.println("Failed to connect to MongoDB. Please make sure MongoDB is running and reachable.");
            e.printStackTrace();
        }
    }
}
