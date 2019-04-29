package main.topicexchange;

import com.rabbitmq.client.*;
import utils.ConnectionUtil;

import java.io.IOException;

public class Consumer1 {
    private static final String QUEUE_NAME="test_quequ_topic_work_1";
    private static final String EXCHANGE_NAME="test_exchange_topic";

    public static void main(String[] args) throws Exception{
        Connection connection = ConnectionUtil.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 绑定队列到交换机
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "routekey.#");
        // 同一时刻服务器只会发一条消息给消费者
        channel.basicQos(1);
        channel.basicConsume(QUEUE_NAME, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }
}
