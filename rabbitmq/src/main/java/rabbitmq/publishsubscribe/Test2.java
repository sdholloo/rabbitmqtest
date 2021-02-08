package rabbitmq.publishsubscribe;

import java.io.IOException;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Test2 {
	public static void main(String[] args) throws Exception {
		ConnectionFactory f=new ConnectionFactory();
		f.setHost("tx.sonteki.top");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		Channel c = f.newConnection().createChannel();
		
		c.exchangeDeclare("logs", "fanout");
		String queue = c.queueDeclare().getQueue();
		c.queueBind(queue, "logs", "");//第三个参数对于发布与订阅模式无效
		
		DeliverCallback deliverCallback =new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				String s=new String(message.getBody());
				System.out.println("收到："+s);
				
			}
		};
		CancelCallback cancelCallback =new CancelCallback() {
			
			@Override
			public void handle(String consumerTag) throws IOException {
				// TODO Auto-generated method stub
				
			}
		};
		
		c.basicConsume(queue,true, deliverCallback, cancelCallback);
		
		
		
	}
}
