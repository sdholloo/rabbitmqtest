package rabbitmq.work;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Test1 {
	public static void main(String[] args) throws Exception {
		ConnectionFactory f=new ConnectionFactory();
		f.setHost("tx.sonteki.top");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		Connection c = f.newConnection();
		Channel ch = c.createChannel();
		
		ch.queueDeclare("helloworld", true, false, false, null);
		while (true) {
			System.out.print("输入：");
			String s=new Scanner(System.in).nextLine();
			if ("exit".equals(s)) {
				System.out.println("退出发送！");
				break;
			}
			ch.basicPublish("", "helloworld", MessageProperties.PERSISTENT_TEXT_PLAIN, s.getBytes());
			System.out.println("消息已发出");
		}
		
		c.close();
		
		
		
		
		
	}
}
