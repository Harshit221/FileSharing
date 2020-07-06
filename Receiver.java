import java.awt.*;
import java.awt.event.*;
import java.io.*; 
import java.net.*;
class Receiver extends Frame
{
	TextField port_t;
	Label status,port_l;
	ServerSocket ss;
	Socket cs;
	boolean flag; // while receiving the file
	public Receiver()
	{
		setTitle("Receiver");
		port_t = new TextField();	
		port_l = new Label("Port Number");
		port_t = new TextField();
		port_t.setText("5555");
		status = new Label();
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 1; c.ipadx = 0;
		add(port_l, c);
		c.gridx = 1; c.gridy = 1; c.ipadx = 400;
		add(port_t, c);
		c.gridx = 1; c.gridy = 2; c.ipadx = 400;
		add(status, c);
		status.setText("Waiting");
		pack();
		flag = false;
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){dispose();}
		});
		setVisible(true);
		receive();
		
	}
	public void receive()
	{
		try
		{
			ss = new ServerSocket(Integer.parseInt(port_t.getText().trim()));
			cs = ss.accept();
			BufferedReader f = new BufferedReader(new InputStreamReader(cs.getInputStream()));
			toReceive(f.readLine(),cs);
			PrintWriter ack = new PrintWriter(cs.getOutputStream(), true);
			if(flag)
			{
				ack.println("Send");
				receiveFile();
				status.setText("Received Successfully");
			}
			else
			{
				ack.println("Do not send");
				cs.close();
			}
		catch(Exception e){e.printStackTrace();}
	}
	void toReceive(String name,Socket cs)
	{
		Dialog d = new MyDialog(this, cs.getRemoteSocketAddress() + "is sending \" " + name + "\". Do you want to receive? ");
	}
	public void receiveFile()
	{
		BufferedReader f = new BufferedReader(new InputStreamReader(cs.getInputStream()));
	}
	public static void main(String[] args)
	{
		new Receiver();
	}
	void setFlag(boolean b){flag = b;}
}
class MyDialog extends Dialog implements ActionListener
{
	Button y,n;
	TextField tf;
	Receiver f;
	String s;
	public MyDialog(Receiver f,String s)
	{
		super(f);
		this.f = f;
		this.s = s;
		y = new Button("Yes");
		n = new Button("No");
		y.addActionListener(this);
		n.addActionListener(this);
		tf = new TextField();
		tf.setText(s);
		tf.setEditable(false);
		setSize(500,250);
		setLayout(new BorderLayout());
		Panel p = new Panel();
		p.add(y);
		p.add(n);
		add(BorderLayout.SOUTH, p);
		add(tf);
		setVisible(true); 
	}
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() == y)
			f.setFlag(true);
		else
			f.setFlag(false);
		dispose();
	}
}