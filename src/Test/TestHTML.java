package Test;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class TestHTML {


	    public static void main(String[] args) {
	        JFrame frame = new JFrame();
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(400, 400);
	        frame.setLayout(new FlowLayout());

	        String leftText = "Left-aligned text";
	        String rightText = "<font size='2'>Right-aligned text</font>";

	        // Use HTML to align the text
	        String labelText = "<html><body>" +
	                           "<table width='100%'><tr>" +
	                           "<td style='text-align: left;'>" + leftText + "</td>" +
	                           "<td style='text-align: right;vertical-align: top;'>" + rightText + "</td>" +
	                           "</tr></table>" +
	                           "</body></html>";

	        JLabel label = new JLabel(labelText);
	        frame.add(label);
	        frame.setVisible(true);
	    }
	}


