package rabbitmq.routing;

import java.io.IOException;
import java.util.Scanner;

import com.rabbitmq.client.BuiltinExchangeType;
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
		
		c.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);
		
		String queue = c.queueDeclare().getQueue();
		
		
		System.out.print("请输入绑定键：");
		String keys = new Scanner(System.in).nextLine();
		String[] a = keys.split("\\s+");
		for (String key : a) {
			c.queueBind(queue, "direct_logs", key);
		}
		
		
		
		
		
		
		DeliverCallback deliverCallback = new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				String msg = new String(message.getBody());
				String routingKey = message.getEnvelope().getRoutingKey();
				System.out.println("收到："+routingKey+"-"+msg);
				
			}
		};
		
		CancelCallback cancelCallback =new CancelCallback() {
			
			@Override
			public void handle(String consumerTag) throws IOException {
				// TODO Auto-generated method stub
				
			}
		};
		
		
		
		c.basicConsume(queue, deliverCallback, cancelCallback);
		
	}
}
