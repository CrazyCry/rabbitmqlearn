package main.exchange;

import com.rabbitmq.client.*;
import utils.ConnectionUtil;

import java.io.IOException;

public class consumer2 {
    private final static String QUEUE_NAME = "test_queue_work2";
    private final static String EXCHANGE_NAME = "test_exchange_fanout";

    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        final Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 绑定队列到交换机
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
        channel.basicQos(1);
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
