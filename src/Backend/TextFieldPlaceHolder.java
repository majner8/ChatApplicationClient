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
	
	private ComponentEmpty cm;
	public  class TextField extends JTextField implements ComponentEmpty  {

		public TextField() {
			component=this;
			cm=this;
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
		@Override
		public void Visitible(boolean x) {
			// TODO Auto-generated method stub
			
		}

	}

	public class PasswordTextField extends JPasswordField implements ComponentEmpty {
		public PasswordTextField() {
			component=this;
			cm=this;
			super.addKeyListener(main);
			super.setText(StringHolder);
			this.Visitible(false);

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
		@Override
		public void Visitible(boolean x) {
			// TODO Auto-generated method stub
			if(x) {
				super.setEchoChar('*');
			}
			else {
				super.setEchoChar((char)0);
			}
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
		char keyChar = e.getKeyChar();
        
		if(this.isEmpty&&Character.isDefined(keyChar) && !Character.isISOControl(keyChar)) {
			this.isEmpty=false;
			component.setText("");
			this.cm.Visitible(true);

			return;
		}
		
		
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(!this.isEmpty&&component.getText().isEmpty()) {
			this.isEmpty=true;
			component.setText(this.StringHolder);
			this.cm.Visitible(false);
		}
	}

	private static interface ComponentEmpty{
		public void Visitible(boolean isVisitible);
	}
	

}

