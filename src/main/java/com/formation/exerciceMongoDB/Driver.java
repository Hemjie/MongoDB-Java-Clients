package com.formation.exerciceMongoDB;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;

import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

public class Driver {
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient("localhost", 27017);

        MongoDatabase db = mongoClient.getDatabase("statistiques_user");

        MongoCollection<Document> clients = db.getCollection("client");

        // 1. Chercher s’il existe un client s’appelant : "Brenda Oneal"
        // find({"name.first': "Brenda", "name.last": ""})
        System.out.println("----- Recherche de Brenda Oneal -----");

        MongoIterable<Document> q1 = clients.find(and(
            eq("name.first", "Brenda"),
            eq("name.last", "Oneal")
        ));

        for (Document client : q1) {
            System.out.println(client);
        }

        // 2. Chercher le nombre de clients dans la collection
        System.err.println("----- Recherche du nombre de clients -----");

        long q2 = clients.countDocuments();

        System.err.println("Il y a " + q2 + " clients dans la collection");

        // 3. Chercher les personnes ayant des cheveux blond

        System.err.println("----- Liste des clients ayant les cheveux blonds -----");

        MongoIterable<Document> q3 = clients.find(new Document("hairColor", "blond"));

        for (Document client : q3) {
            System.out.println(client);
        }

        // 4. Chercher les personnes ayant des cheveux blond et avec des yeux bleus
        System.err.println("----- Liste des clients ayant les cheveux blonds et les yeux bleus -----");

        MongoIterable<Document> q4 = clients.find(and(
                eq("hairColor", "blond"),
                eq("eyeColor", "blue")
        ));

        for (Document client : q4) {
            System.out.println(client);
        }

        // 5. Combien de clients ont les cheveux roux

        long q5 = clients.countDocuments(or(
                eq("hairColor", "auburn"),
                eq("hairColor", "ginger red")
        ));

        System.out.println(q5 + " clients ont les cheveux roux");

        // 6. Lister toutes les compagnies de cette manière : Tonya Rogers est dans la compagnie PORTICO

        System.err.println("----- Liste des clients avec leur nom de leur compagnie -----");

        MongoIterable<Document> q6 = clients.find().projection(
                fields(excludeId(),include("name.first", "name.last", "company")));

        for (Document client : q6) {
            Document name = (Document)client.get("name");
            System.out.println(name.get("first") + " " + name.get("last") +
                    " est dans la compagnie " + client.get("company"));
        }

        // 7. Chercher les clients ayant un poids entre 67 et 92 kg

        System.err.println("----- Liste des clients pesant entre 67 et 92 kgs -----");

        MongoIterable<Document> q7 = clients.find(and(
                gte("weight", 67),
                lte("weight", 92)
        ));

        for (Document client : q7) {
            System.out.println(client);
        }

        // 8. Combien de clients ont un email qui termine par : .tv

        long q8 = clients.countDocuments(new Document("email", new Document(
                "$regex", Pattern.compile("\\.tv$")
        )));

        System.out.println(q8 + " clients ont un email finissant par .tv");

        // 9. Combien de clients ont un email qui termine par : .com

        long q9 = clients.countDocuments(new Document("email", new Document(
                "$regex", Pattern.compile(".com$")
        )));

        System.out.println(q9 + " clients ont un email finissant par .com");

        // 10. Combien de clients ont un email qui termine par : .tv et .com

        long q10 = clients.countDocuments(new Document("email", new Document(
                "$regex", Pattern.compile("(.com|.tv)$")
        )));

        System.out.println(q10 + " clients ont un email finissant par .tv ou .com");

        // 11. Combien de clients habitent en Californie

        long q11 = clients.countDocuments(new Document("address",
                Pattern.compile("California", Pattern.CASE_INSENSITIVE)
        ));

        System.out.println(q11 + " clients vivent en Californie");
    }
}
