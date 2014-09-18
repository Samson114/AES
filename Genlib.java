package test;
import java.io.*;
import javax.crypto.*;
import java.security.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class GenLib extends JFrame implements ActionListener {
private static final long serialVersionUID = -2172390658776366128L;
JTextField area1,area2;
JTextField area3,area4;
JButton button1,button2;
JButton dictionay;
JFileChooser filechooser;Container con=getContentPane();

public GenLib ()throws Exception
{
   super("AES 字典破解");
   filechooser=new JFileChooser();
   area1=new JTextField("JFrame",30);
   area2=new JTextField(30);
   area3=new JTextField(30);
   area4=new JTextField(30);
   button1=new JButton("AES的加密 结果如下");
   button1.addActionListener(this);
   button2=new JButton("破解AES 结果如下");
   button2.addActionListener(this);
   dictionay=new JButton("生成和导入字典");
   dictionay.addActionListener(this);
   setLayout(new FlowLayout());
   add(new JLabel("请输入原文"));
   add(area1); 
   add(button1);//add(button2);
   add(area2);
   add(new JLabel("请输入密文"));
   add(area3); 
   add(dictionay); add(button2); 
   add(area4); 
   setVisible(true);
   setSize(400,400);
   //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
public void actionPerformed(ActionEvent e) 
{
if(e.getSource()==button1)
{
 MyAES sha=new MyAES();
 try{area2.setText(sha.getString(area1.getText()));}catch(Exception ee){}
 area3.setText("");
 area4.setText("");
}
else if(e.getSource()==button2)
{
   long time2=0;
   int flag=0;
   long time1=System.currentTimeMillis();
   String temp="";
   area4.setText("请耐心等待。。。。");
   String getMyString=area3.getText();
  try
  {BufferedReader bb=new BufferedReader (new FileReader(new File("字典.txt")));
     String s="";
  while((s=bb.readLine())!=null){      
   try{ temp=new MyAES().getString(s);}catch(Exception ee){}
    if(temp.equals(getMyString))
     {
     time2=System.currentTimeMillis();    
     area4.setText("原文： "+s+"       所耗时："+((time2-time1)/1000)+"秒");
     flag=1;
     break;
     }
   }
 bb.close();
 if(flag==0)
       area4.setText("原文内容不在这个字典之中，请重新选择字典");
  }catch(Exception mye){}
 }
else if (e.getSource()==dictionay){
 try
 {
     MySplit();
 }catch(Exception myee){}
}
}
public  StringBuffer read()throws Exception{//读取源文件
 StringBuffer temp=new StringBuffer();
 File name=null;
    JFileChooser fc=new JFileChooser();
    int n=fc.showOpenDialog(this);
    if(n==JFileChooser.APPROVE_OPTION)
    {
     name=fc.getSelectedFile();                           
    }
 BufferedReader buf=new BufferedReader (new FileReader(name));
    String s="";
 while((s=buf.readLine())!=null){
     temp.append(s);    
    } 
 return temp;
}
public  void MySplit()throws Exception{
 String s[]=read().toString().split(" ");
 BufferedWriter  buf=new BufferedWriter(new FileWriter(new File("字典.txt")));
 for(int i=0;i<s.length;i++)
  buf.write(s[i]+"\r\n");
 buf.close();
 
}
public static void main(String args[]){
 try{new GenLib();}catch(Exception e){}
}
}

 class MyAES {
 private KeyGenerator kg=null;
    SecretKey key=null;
 public MyAES() {
  try{
   kg= KeyGenerator.getInstance("AES");
   }
  catch(Exception e){System.out.println(e);}
      kg.init(128, new SecureRandom("mamin".getBytes()));
      key = kg.generateKey();    
 }
   public SecretKey getKey()//返回Key
    {
  return key;
    }

   private  final char[] bcdLookup = { '0', '1', '2', '3', '4', '5',
   '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
  public String mybytetoString(byte[] digest){
    StringBuffer s = new StringBuffer(digest.length * 2);
    for (int i = 0; i < digest.length; i++) {
       s.append(bcdLookup[(digest[i] >>> 4) & 0x0f]);
       s.append(bcdLookup[digest[i] & 0x0f]);
       } 
    return s.toString();
  } 
   //加密
   public  String getString(String str) throws  Exception{
     Cipher cp = Cipher.getInstance("AES");//创建密码器
     cp.init(Cipher.ENCRYPT_MODE, key);//初始化
     byte [] ptext = str.getBytes("UTF-8"); 
     return  mybytetoString(cp.doFinal(ptext));
   
    }
}

