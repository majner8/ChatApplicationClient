package Backend;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class TextFieldPlaceHolder implements KeyListener {
	private  String StringHolder;
	private boolean isEmpty=true;
	private JTextField component;
	private TextFieldPlaceHolder main=this;
	public TextFieldPlaceHolder(String StringPlaceHolder) {
		this.StringHolder=StringPlaceHolder;
	}

	
	protected JTextField getJTextField() {
		
		return this.component;
	}
	/**Metod clear text on TextField, and put default one */
	public synchronized void Clear() {
		this.getJTextField().setText(StringHolder);
		this.isEmpty=true;
	}
	
	public  class TextField extends JTextField  {

		public TextField() {
			component=this;
			super.addKeyListener(main);
			super.setText(StringHolder);
		}
		public boolean PlaceHolderIsEmpty() {
			return isEmpty;
		}
		
		 public  boolean isValidEmail() {
		        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
		        Pattern pattern = Pattern.compile(emailRegex);
		        Matcher matcher = pattern.matcher(super.getText());
		        return matcher.matches();
		    }

	}

	public class PasswordTextField extends JPasswordField  {
		public PasswordTextField() {
			component=this;
			super.addKeyListener(main);
			super.setText(StringHolder);

		}
		public boolean isSame(PasswordTextField another) {
			if(another==null) {
				return !this.isEmpty();
			}
			if(!this.isEmpty()&&!another.isEmpty()&&this.getText().equals(another.getText())) {
				return true;
			}
			return false;
		}
		private boolean isEmpty() {
			return isEmpty;
		}
	}

	

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if((e.getKeyCode()==KeyEvent.VK_DELETE||e.getKeyCode()==8)&&this.isEmpty) {
			e.consume();
			return;
		}
		if(this.isEmpty) {
			this.isEmpty=false;
			component.setText("");
			return;
		}
		
		
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(!this.isEmpty&&component.getText().isEmpty()) {
			this.isEmpty=true;
			component.setText(this.StringHolder);
		}
	}



}

