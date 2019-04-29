package main;

import com.rabbitmq.client.*;
import utils.ConnectionUtil;

import java.io.IOException;

public class Recv {
    private final static String QUEUE_NAME = "q_test_01";

    public static void main(String[] argv) throws Exception {

        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        boolean autoAck = true;

        // 从连接中创建通道
        final Channel channel = connection.createChannel();
                // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.basicConsume(QUEUE_NAME, autoAck, "myConsumerTag", new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String routingKey = envelope.getRoutingKey();
                String contentType = properties.getContentType();
                long deliveryTag = envelope.getDeliveryTag();
                // (process the message components here ...)
                System.out.println(routingKey+"  "+contentType+"  "+new String(body));
                channel.basicAck(deliveryTag, false);
            }
        });

//        // 定义队列的消费者
//        QueueingConsumer consumer = new QueueingConsumer(channel);
//
//        // 监听队列
//        channel.basicConsume(QUEUE_NAME, true, consumer);
//
//        // 获取消息
//        while (true) {
//            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//            String message = new String(delivery.getBody());
//            System.out.println(" [x] Received '" + message + "'");
//        }
    }
}
