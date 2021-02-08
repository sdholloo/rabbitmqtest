package rabbitmq.rpc;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class RPCServer {
	public static void main(String[] args) throws Exception {
		//1.接收客户端发来的数据
		ConnectionFactory f=new ConnectionFactory();
		f.setHost("tx.sonteki.top");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		Channel c = f.newConnection().createChannel();
		c.queueDeclare("rpc_queue", false, false, false, null);
		c.queuePurge("rpc_queue");
		
		
		DeliverCallback deliverCallback=new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				// 整数值n，返回队列的队列名，关联id
				String s=new String(message.getBody());
				String replyTo=message.getProperties().getReplyTo();
				String cid=message.getProperties().getCorrelationId();
				long fbnq = fbnq(Integer.parseInt(s));
				
				BasicProperties props = new BasicProperties.Builder().correlationId(cid).build();
				c.basicPublish("", replyTo, props, (""+fbnq).getBytes());
			}
		};
		CancelCallback cancelCallback=new CancelCallback() {
			
			@Override
			public void handle(String consumerTag) throws IOException {
				// TODO Auto-generated method stub
				
			}
		};
		
		c.basicConsume("rpc_queue", deliverCallback, cancelCallback);
		
		
		
		//2.执行运算求出发波那契数
		
		
		
		//3.向客户端发回结果
	}
	
	static long fbnq(int n) {
		if(n==1||n==2) {
			return 1;
		}
		return fbnq(n-1)+fbnq(n-2);
	}
}
