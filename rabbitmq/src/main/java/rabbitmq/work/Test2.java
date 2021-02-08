package rabbitmq.work;

import java.io.IOException;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
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
		
		
		Connection c = f.newConnection();
		Channel ch = c.createChannel();
		
		ch.queueDeclare("helloworld",true , false, false, null);
		DeliverCallback cancelCallback =new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				// TODO Auto-generated method stub
				String s=new String(message.getBody());
				System.out.println("收到："+s);
				for (int i = 0; i < s.length(); i++) {
					
					if (s.charAt(i)=='.') {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				System.out.println("消息处理完成！");
				ch.basicAck(message.getEnvelope().getDeliveryTag(), false);
			}
		};
		
		CancelCallback deliverCallback=new CancelCallback() {
			
			@Override
			public void handle(String consumerTag) throws IOException {
				// TODO Auto-generated method stub
				
			}
		};
		ch.basicQos(1);
		ch.basicConsume("helloworld", false, cancelCallback, deliverCallback);
		
		
		
		
		
		
	}
}
