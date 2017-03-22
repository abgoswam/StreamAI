import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;
import java.util.Random;

/**
 * Created by abgoswam on 3/21/17.
 */

//Create java class named "SimplePredictor"
public class SimplePredictor {

    public static void main(String[] args) throws Exception{

        //Assign topicName to string variable
        String topicName = "teststreamai2";

        // create instance for properties to access producer configs
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092"); //Assign localhost id
        props.put("acks", "all"); //Set acknowledgements for producer requests.
        props.put("retries", 0); //If the request fails, the producer can automatically retry,
        props.put("batch.size", 16384); //Specify buffer size in config
        props.put("linger.ms", 1); //Reduce the no of requests less than 0
        props.put("buffer.memory", 33554432); //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        System.out.println("Message sending...");

        Random rand = new Random();
        while(true) {

            float x = rand.nextFloat() * 10;
            float y = rand.nextFloat() * 10;

            String pair = String.format("(%f,%f)", x, y);
            System.out.println(pair);

            // producer.send(new ProducerRecord<String, String>(topicName, "abhishek", "(5,[0.1,0.2,0.5])"));
            producer.send(new ProducerRecord<String, String>(topicName, "abhishek", pair));

            //sleep 1 seconds
            Thread.sleep(1000);
        }
    }
}
