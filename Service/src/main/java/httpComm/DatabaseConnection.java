package httpComm;

import java.text.SimpleDateFormat;
import java.util.Date;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import model.DataPoint;

public class DatabaseConnection {
    
    private MySQLPool client;
    
    public DatabaseConnection() {
        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(3306)
                .setHost("localhost")
                .setDatabase("damdb")
                .setUser("root");
        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
        client = MySQLPool.pool(connectOptions, poolOptions);
    }
    
    public void selectAll() {
        client
        .preparedQuery("SELECT * FROM MEASUREMENTS")
        .execute(ar -> {
        if (ar.succeeded()) {
          RowSet<Row> rows = ar.result();
          System.out.println("Got " + rows.size() + " rows ");
          for(Row row : rows) {
              System.out.println(row.getValue("Distance"));
          }
        } else {
          System.out.println("Failure: " + ar.cause().getMessage());
        }
      });
    }
    
    public Future<RowSet<Row>> getLatestData(int dataNumber) {
        Promise<RowSet<Row>> dataPromise = Promise.promise();
        
        client
        .preparedQuery("SELECT * FROM `MEASUREMENTS` ORDER BY Time DESC LIMIT ?")
        .execute(Tuple.of(dataNumber), ar -> {
            if(ar.succeeded()) {
                if(ar.result() != null) {
                    dataPromise.complete(ar.result());
                }
                else {
                    dataPromise.complete(null);
                }
            }
            else {
                System.out.println("Failure: " + ar.cause().getMessage());
            }
        });
        return dataPromise.future();
    }
    
    public void insertData(final DataPoint data) {
        Date dt = new Date(data.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(dt);
        client
        .preparedQuery("INSERT INTO MEASUREMENTS VALUES  (?,?)")
        .execute(Tuple.of(dateTime, data.getDistance()), ar -> {
            
            if (ar.succeeded()) {
                //System.out.println("Data inserted.");
              } 
            else {
                //System.out.println("Failure: " + ar.cause().getMessage());
              } 
            
            
        });
    }
    
}
