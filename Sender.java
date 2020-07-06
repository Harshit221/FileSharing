import java.awt.*;
import java.awt.event.*;
import java.io.*; 
import java.net.*;
class Sender extends Frame implements ActionListener
{
	String ip;
	int port;
	TextField ip_t,port_t,filename_t;
	Label status,ip_l,port_l,filename_l;;
	Button select,send;
	FileDialog send_d;
	public Sender()
	{
		setTitle("Sender");
		send_d = null;
		setup();
	}
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() == send)
			sendFile();
		if(ae.getSource() == select)
			selectFile();
	}
	public void sendFile()
	{
		if(port_t.getText() == null || ip_t.getText() == null)
		{
			status.setText("Enter IP and Port Number");
			return;
		}
		if(send_d == null)
		{
			status.setText("Select the file");
			return;
		}
		try
		{
			Socket cs = new Socket(ip_t.getText().trim(),Integer.parseInt(port_t.getText().trim()));
			BufferedReader f = new BufferedReader(new FileReader(filename_t.getText()));
			PrintWriter p = new PrintWriter(cs.getOutputStream(), true);
			BufferedReader ack = new BufferedReader(new InputStreamReader(cs.getInputStream()));
			String data;
			p.println(send_d.getFile());
			status.setText(ack.readLine());
			if( !(status.getText().equals("Send")) )
			{	
				ack.close();p.close();f.close();cs.close();
				return;
			}
			while( (data = f.readLine()) != null )
				p.println(data);
			status.setText(ack.readLine());
			if( !(status.getText().equals("Failed")) )
			{
				send_d = null;
				filename_t.setText("");
			}
			f.close();
			p.close();
			ack.close();
			cs.close();
		}
		catch(NumberFormatException ne){}
		catch(Exception e){e.printStackTrace();}
	}
	public void selectFile()
	{
		send_d = new FileDialog(this, "Select File");
		send_d.setVisible(true);
		filename_t.setText(send_d.getDirectory() + send_d.getFile());
	}
	public static void main(String[] args)
	{
		new Sender();
	}
	public void setup()
	{
		ip_t = new TextField();
		port_t = new TextField();
		filename_t = new TextField();
		filename_t.setEditable(false);
		status = new Label("");
		ip_l = new Label("IP Address");
		port_l = new Label("Port Number");
		filename_l = new Label("File");
		select = new Button("Select");
		send = new Button("Send");
		send.addActionListener(this);
		select.addActionListener(this);
		setVisible(true);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){dispose();}
		});
		//setResizable(false);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.ipadx = 0;
		add(ip_l, c);
		c.gridx = 1; c.gridy = 0; c.ipadx = 400;
		add(ip_t, c);
		c.gridx = 0; c.gridy = 1; c.ipadx = 0;
		add(port_l, c);
		c.gridx = 1; c.gridy = 1; c.ipadx = 400;
		add(port_t, c);
		c.gridx = 0; c.gridy = 2; c.ipadx = 0;
		add(filename_l, c);
		c.gridx = 1; c.gridy = 2; c.ipadx = 400;
		add(filename_t, c);
		c.gridx = 0; c.gridy = 3; c.ipadx = 0;
		add(select, c);
		c.gridx = 1; c.gridy = 3; c.ipadx = 0;
		add(send, c);
		c.gridx = 1; c.gridy = 4; c.ipadx = 400;
		add(status, c);
		pack();
	}
}