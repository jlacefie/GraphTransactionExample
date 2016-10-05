# GraphTransactionExample
This is a simple DSE Graph Transaction example using string passing via the Java driver.

# Overview
This example contains the current recommended approach to achieving transaction [durability] (https://en.wikipedia.org/wiki/Durability_(database_systems)) and [atomicity](https://en.wikipedia.org/wiki/Atomicity_(database_systems)) with DSE Graph.  

The string execution functionality that exists within the DSE Graph [drivers](http://docs.datastax.com/en/developer/java-driver-dse//1.1/manual/graph/) enables the mechanism to provide atomicity with Graph transactions.  When a Gremlin string is executed within a session, and assuming the default mode for [logged_batch](https://docs.datastax.com/en/latest-dse/datastax_enterprise/graph/reference/schema/refSchemaConfig.html) is true for the graph, the contents of that string are treated as a [Cassandra Batch](http://docs.datastax.com/en/cql/3.3/cql/cql_reference/batch_r.html).  That is, the entire contents of the Gremlin string are ensured to succeed if any of the Gremlin string succeeds.

One can use the Gremlin String context to create advanced DSE Graph “transactions”.   To do this, first create the logic to be executed in the transaction in a separate groovy or java file.  Place this file as a resource in your java project.  Finally use something like the following method to execute the string in a session.

```
  public String getQuery(String name) {
        try {
            return IOUtils.toString(getClass().getResourceAsStream(name+".groovy"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
```

# Example
This example contains 2 files:
 1. src\main\java\Example.java - the java code that executes the Gremlin traversal with parameters
 2. src\main\resources\Traversal.groovy - the file that contains the Gremline traversal
 
To execute this example, you will need to create a simple Graph in DSE Graph that contains:
 1. a Vertex Label - Customer
 2. an Edge - Related
 
Here is the ddl to create the schema.

```
schema.propertyKey("rType").Text().single().create()
schema.propertyKey("name").Text().single().create()
schema.propertyKey("age").Text().single().create()
schema.edgeLabel("related").multiple().properties("rType").create()
schema.vertexLabel("customer").properties("name", "age").create()
schema.edgeLabel("related").connection("customer", "customer").add()
```
