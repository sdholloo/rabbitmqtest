package rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Test1 {
	public static void main(String[] args) throws Exception{
		//建立连接
		ConnectionFactory f=new ConnectionFactory();
		f.setHost("172.81.240.56");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		Connection c=f.newConnection();//创建连接
		Channel ch=c.createChannel();//创建通道
		//              队列名称       队列持久化 排他 当最后一个消费者断开后，是否删除队列
		//					|			|		|	｜		其他参数
		ch.queueDeclare("helloworld", false, false, false, null);
		//              忽略	 队列名       其他参数		byte数组的消息体
		ch.basicPublish("", "helloworld", null, "helloworld".getBytes());
		
		System.out.println("消息已经发出");
		
		c.close();
		
	}
}
