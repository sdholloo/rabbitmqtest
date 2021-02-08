package rabbitmq.rpc;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import javax.xml.namespace.QName;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class RPCClient {
	static BlockingQueue<Long> q=new ArrayBlockingQueue<>(10);
	public static void main(String[] args) throws Exception {
		System.out.print("输入要求的斐波那契数：");
		int n =new Scanner(System.in).nextInt();
		long fbnq=fbnq(n);
		System.out.println("第"+n+"个斐波那契数是"+fbnq);
	}

	private static long fbnq(int n) throws Exception {
		ConnectionFactory f=new ConnectionFactory();
		f.setHost("tx.sonteki.top");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		Channel c = f.newConnection().createChannel();
		c.queueDeclare("rpc_queue", false, false, false, null);
		String replyTo=c.queueDeclare().getQueue();
		String cid=UUID.randomUUID().toString();
		
		BasicProperties props=new BasicProperties().builder().replyTo(replyTo).correlationId(cid).build();
		c.basicPublish("", "rpc_queue", props, (""+n).getBytes());
		System.out.println("消息已发送");
		System.out.println("这里模拟其他运算，不立即等待计算结果");
		DeliverCallback deliverCallback=new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				// TODO Auto-generated method stub
				if (cid.equals(message.getProperties().getCorrelationId())) {
					String s=new String(message.getBody());
					long fbnq=Long.parseLong(s);
					q.offer(fbnq);
					c.getConnection().close();//关闭连接
				}
			}
		};
		CancelCallback cancelCallback=new CancelCallback() {
			
			@Override
			public void handle(String consumerTag) throws IOException {
				// TODO Auto-generated method stub
				
			}
		};
		
		
		c.basicConsume(replyTo, true, deliverCallback, cancelCallback);
		return q.take();
	}
}
