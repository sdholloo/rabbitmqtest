package rabbitmq.simple;

import java.io.IOException;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Test2 {
	public static void main(String[] args) throws Exception{
		ConnectionFactory f=new ConnectionFactory();
		f.setHost("172.81.240.56");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		Connection c=f.newConnection();
		Channel ch=c.createChannel();
		
		
		ch.queueDeclare("helloworld", false, false, false, null);
		
		//处理消息的回调对象
		DeliverCallback callback =new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				String msg=new String(message.getBody(),"UTF-8");
				System.out.println("收到-"+msg);
			}
		};
		//取消消息的回调对象
		CancelCallback cancel=new CancelCallback() {
			
			@Override
			public void handle(String consumerTag) throws IOException {
		
			}
		};
		
		ch.basicConsume("helloworld", true,callback,cancel);
	}
}
