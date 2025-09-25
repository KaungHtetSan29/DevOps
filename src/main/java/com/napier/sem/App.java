package com.napier.sem;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.MongoException;
import org.bson.Document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App
{
    public static void main(String[] args)
    {
        // ------------------------------
        // Part 1: MongoDB Connection
        // ------------------------------
        String mongoUri = System.getenv("MONGO_URI");

        if (mongoUri == null || mongoUri.isEmpty()) {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("linux")) {
                mongoUri = "mongodb://host.docker.internal:27017";
            } else {
                mongoUri = "mongodb://localhost:27017";
            }
            // mongoUri = "mongodb://mongo-dbserver:27017"; // Optional Docker hostname
        }

        System.out.println("Connecting to MongoDB at: " + mongoUri);

        try (MongoClient mongoClient = MongoClients.create(mongoUri)) {
            // Test connection
            mongoClient.listDatabaseNames().first();

            MongoDatabase database = mongoClient.getDatabase("mydb");
            MongoCollection<Document> collection = database.getCollection("test");

            Document doc = new Document("name", "Kevin Sim")
                    .append("class", "Software Engineering Methods")
                    .append("year", "2021")
                    .append("result", new Document("CW", 95).append("EX", 85));

            collection.insertOne(doc);

            Document myDoc = collection.find().first();
            System.out.println("Inserted document into MongoDB:");
            System.out.println(myDoc.toJson());

        } catch (MongoException e) {
            System.err.println("Failed to connect to MongoDB. Please make sure MongoDB is running and reachable.");
            e.printStackTrace();
        }

        // ------------------------------
        // Part 2: MySQL Connection
        // ------------------------------
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        Connection con = null;
        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to MySQL database...");
            try {
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/employees?useSSL=false&allowPublicKeyRetrieval=true",
                        "root",
                        "example"
                );
                System.out.println("Successfully connected to MySQL");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to MySQL attempt " + i);
                System.out.println(sqle.getMessage());
                try {
                    Thread.sleep(5000); // wait 5s before retry
                } catch (InterruptedException ie) {
                    System.out.println("Thread interrupted? Should not happen.");
                }
            }
        }

        if (con != null) {
            try {
                con.close();
                System.out.println("Closed MySQL connection");
            } catch (Exception e) {
                System.out.println("Error closing connection to MySQL");
            }
        }
    }
}

