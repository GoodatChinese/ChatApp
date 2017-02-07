import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ChatServer {
	private DataInputStream dis = null;
	private ServerSocket ss = null;
	private DataOutputStream dos = null;
	
	List<Client> clients = new ArrayList<Client>();
	
	public static void main(String[] args) {
		new ChatServer().Start();
		
	}
	public void Start(){
		boolean isstarted = false;
		
		try {
			
			ss =new ServerSocket(5555);
			
		}catch(IOException e){
			System.out.println("端口被占用 换一个哦！");
		}
		try{	
			isstarted = true;
			while(isstarted){
				Socket s = ss.accept();
				Client c=new Client(s);
				System.out.println("上线");
				new Thread(c).start();
				clients.add(c);
//			dis.close();
		}
	}catch(IOException e){
		e.printStackTrace();
	}
		finally{
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	

	
	
	class Client implements Runnable{
		Socket s;
		DataInputStream dis = null;
		boolean isread = false;
		
		Client(Socket s){
			this.s=s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				isread = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void send(String str){
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			try{
			while(isread){
				String str = dis.readUTF();
				System.out.println(str);
				for(int i = 0;i < clients.size();i++){
					Client c = clients.get(i);
					c.send(str);
					System.out.println(str+clients.get(i)+"  "+i);
				}
			}
			} catch (Exception e) {
				try {
					if(dis != null)dis.close();
					if(dos != null)dos.close();
					if(s != null)s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.out.println("一个链接下线了");
//				e.printStackTrace();
			}
		}

		
		
	}
}
