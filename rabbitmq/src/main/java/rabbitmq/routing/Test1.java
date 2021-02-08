package rabbitmq.routing;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
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
		
		c.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);
		
		while (true) {
			System.out.println("输入路由键：");
			String rk=new Scanner(System.in).nextLine();
			
			System.out.println("输入消息：");
			String s=new Scanner(System.in).nextLine();
			
			if ("exit".equals(s)) {
				System.out.println("exit........");
				break;
			}
			c.basicPublish("direct_logs", rk, null, s.getBytes());
			
			System.out.println("===================");
			
		}
		c.close();
	}
}
