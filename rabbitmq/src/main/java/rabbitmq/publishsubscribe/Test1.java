package rabbitmq.publishsubscribe;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

public class Test1 {
	public static void main(String[] args) throws Exception {
		ConnectionFactory f=new ConnectionFactory();
		f.setHost("tx.sonteki.top");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		Channel c = f.newConnection().createChannel();
		
		c.exchangeDeclare("logs", "fanout");
		
		while(true) {
			System.out.print("输入：");
			String msg=new Scanner(System.in).nextLine();
			c.basicPublish("logs", "", null, msg.getBytes());
		}
		
	}
}
