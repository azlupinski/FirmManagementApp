package com.devTeam.FirmManagementApp.dao;

import com.devTeam.FirmManagementApp.MongoDBConfiguration;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = {MongoDBConfiguration.class})
@EnableConfigurationProperties
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ConnectionTest  {


  @Value("${spring.mongodb.uri}")
  private String mongoUri;
  @Autowired
  MongoClient mongoClient;

  @Value("${spring.mongodb.database}")
  String databaseName;


  @Test
  public void testConnectionFindsDatabase() {

    MongoClient mc = MongoClients.create(mongoUri);
    String expectedDBName = "firmdb";
    boolean found = false;
    for (String dbname : mc.listDatabaseNames()) {
      if (expectedDBName.equals(dbname)) {
        found = true;
        break;
      }
    }
    Assert.assertTrue(
        "We can connect to MongoDB, but couldn't find `mflix` database. Check the restore step",
        found);
  }

  @Test
  public void testConnectionFindsCollections() {

    MongoClient mc = MongoClients.create(mongoUri);
    // needs to find at least these collections
    List<String> collectionNames = Arrays.asList("firms", "users");

    int found = 0;
    for (String colName : mc.getDatabase("firmdb").listCollectionNames()) {

      if (collectionNames.contains(colName)) {
        found++;
      }
    }

    Assert.assertEquals(
        "Could not find all expected collections. Check your restore step",
        found,
        collectionNames.size());
  }
}
