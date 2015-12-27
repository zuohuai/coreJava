package com.edu.mapEditor.view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author zys59三仙半（QQ：597882752）<br>
 *         创建时间：2015年7月13日 上午7:59:59
 */
public class RectWithInt extends JFrame {
    private static final long serialVersionUID = 3456314340308050741L;
 
    private JPanel contentPane;
 
    private RectCanvas canvas;
 
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    RectWithInt frame = new RectWithInt();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
 
    /**
     * Create the frame.
     */
    public RectWithInt() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 450);
        setTitle("一个动态填充矩形的例子");
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);
 
        canvas = new RectCanvas(20, 300, 150);
        canvas.setLocation(50, 20);
        getContentPane().add(canvas);
         
        JLabel lblNewLabel = new JLabel("请单击屏幕上红框内的位置，查看效果。");
        lblNewLabel.setBounds(120, 247, 241, 15);
        contentPane.add(lblNewLabel);
 
        //这里是为了演示动态的修改矩形
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                canvas.setData(canvas.getHeight() - e.getY());
            }
        });
    }
 
    public class RectCanvas extends Canvas {
        private static final long serialVersionUID = 5070517776553228277L;
 
        private int data = 100;
 
        public RectCanvas(int w, int h, int data) {
            this.setSize(w, h);
            this.data = data;
        }
 
        //这里在根据输入数据画矩形
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Color c = g.getColor();
            g.setColor(new Color(0, 255, 0));
            g.fillRect(0, this.getHeight() - data - 1, this.getWidth() - 1,
                    data);
            g.setColor(new Color(255, 0, 0));
            g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
            g.setColor(c);
        }
 
        public int getData() {
            return data;
        }
 
        //这里接受外部数据
        public void setData(int data) {
            this.data = data;
            //接收数据后，重新绘制矩形
            repaint();
        }
    }
}